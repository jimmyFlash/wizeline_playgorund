package com.wizeline.bookchallenge.utils.interaction

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import kotlin.math.atan2
import kotlin.math.sqrt


interface HandleProgressEvent {
    /**
     * Called when the current event occurred when NO gesture is in progress
     * yet. The handling in this implementation may set the gesture in progress
     * (via mGestureInProgress) or out of progress
     * @param actionCode
     * @param event
     */
    fun handleStartProgressEvent(actionCode: Int, event: MotionEvent)

    /**
     * Called when the current event occurred when a gesture IS in progress. The
     * handling in this implementation may set the gesture out of progress (via
     * mGestureInProgress).
     * @param actionCode
     * @param event
     */
    fun handleInProgressEvent(actionCode: Int, event: MotionEvent)
}

sealed class BaseGestureDetector(val context: Context):HandleProgressEvent{

     var mGestureInProgress = false

     var mPrevEvent: MotionEvent? = null
     var mCurrEvent: MotionEvent? = null

     var mCurrPressure = 0f
     var mPrevPressure = 0f
     var mTimeDelta: Long = 0

    companion object{
        /**
         * This value is the threshold ratio between the previous combined pressure
         * and the current combined pressure. When pressure decreases rapidly
         * between events the position values can often be imprecise, as it usually
         * indicates that the user is in the process of lifting a pointer off of the
         * device. This value was tuned experimentally.
         */
        const val PRESSURE_THRESHOLD = 0.67f
    }

    /**
     * All gesture detectors need to be called through this method to be able to
     * detect gestures. This method delegates work to handler methods
     * (handleStartProgressEvent, handleInProgressEvent) implemented in
     * extending classes.
     * @param event
     * @return Boolean
     */
     fun onTouchEvent(event: MotionEvent, handleInProgressEventImp: HandleProgressEvent): Boolean {
        val actionCode = event.action and MotionEvent.ACTION_MASK
        if (!mGestureInProgress) {
            handleInProgressEventImp.handleStartProgressEvent(actionCode, event)
        } else {
            handleInProgressEventImp.handleInProgressEvent(actionCode, event)
        }
        return true
    }

    open fun updateStateByEvent(curr: MotionEvent) {
        val prev = mPrevEvent

        // Reset mCurrEvent
        if (mCurrEvent != null) {
            mCurrEvent!!.recycle()
            mCurrEvent = null
        }
        mCurrEvent = MotionEvent.obtain(curr)


        mTimeDelta = if (prev != null){
            // Delta time
            curr.eventTime - prev.eventTime
        }else{
            curr.eventTime
        }
        // Pressure
        mCurrPressure = curr.getPressure(curr.actionIndex)
        mPrevPressure = prev?.getPressure(prev.actionIndex) ?: 0F
    }

    open fun resetState() {
        if (mPrevEvent != null) {
            mPrevEvent!!.recycle()
            mPrevEvent = null
        }
        if (mCurrEvent != null) {
            mCurrEvent!!.recycle()
            mCurrEvent = null
        }
        mGestureInProgress = false
    }

    /**
     * Returns `true` if a gesture is currently in progress.
     * @return `true` if a gesture is currently in progress, `false` otherwise.
     */
     fun isInProgress(): Boolean =   mGestureInProgress

    /**
     * Return the time difference in milliseconds between the previous accepted
     * GestureDetector event and the current GestureDetector event.
     *
     * @return Time difference since the last move event in milliseconds.
     */
     fun getTimeDelta(): Long =  mTimeDelta

    /**
     * Return the event time of the current GestureDetector event being
     * processed.
     *
     * @return Current GestureDetector event time in milliseconds.
     */
     fun getEventTime(): Long = mCurrEvent!!.eventTime

    data class MoveGestureDetector(val ctx: Context, val listener: OnMoveGestureListener)
        : BaseGestureDetector(ctx) {

        private var mCurrFocusInternal: PointF? = null
        private var mPrevFocusInternal: PointF? = null
        private val mFocusExternal = PointF()
        private var mFocusDeltaExternal = PointF()

        /**
         * Listener which must be implemented which is used by MoveGestureDetector
         * to perform callbacks to any implementing class which is registered to a
         * MoveGestureDetector via the constructor.
         *
         * @see MoveGestureDetector.SimpleOnMoveGestureListener
         */
        interface OnMoveGestureListener {
            fun onMove(detector: MoveGestureDetector?): Boolean
            fun onMoveBegin(detector: MoveGestureDetector?): Boolean
            fun onMoveEnd(detector: MoveGestureDetector?)

        }

        override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
            when (actionCode) {
                MotionEvent.ACTION_DOWN -> {
                    resetState()
                    mPrevEvent = MotionEvent.obtain(event)
                    mTimeDelta = 0

                    updateStateByEvent(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    mGestureInProgress = listener.onMoveBegin(this)
                }
            }
        }

        override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
            when (actionCode) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_CANCEL -> {
                    listener.onMoveEnd(this)
                    resetState()
                }
                MotionEvent.ACTION_MOVE -> {
                    updateStateByEvent(event)

                    // Only accept the event if our relative pressure is within
                    // a certain limit. This can help filter shaky data as a
                    // finger is lifted.
                    if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
                        val updatePrevious = listener.onMove(this)
                        if (updatePrevious) {
                            mPrevEvent?.recycle()
                            mPrevEvent = MotionEvent.obtain(event)
                        }
                    }
                }
            }
        }

        /**
         * Determine (multi)finger focal point (a.k.a. center point between all
         * fingers)
         *
         * @param  e MotionEvent
         * @return PointF focal point
         */
        private fun determineFocalPoint(e: MotionEvent):PointF{
            // Number of fingers on screen
            val pCount: Int = e.pointerCount
            var x = 0f
            var y = 0f

            for (i in 0 until pCount) {
                x += e.getX(i)
                y += e.getY(i)
            }

            return PointF(x / pCount, y / pCount)
        }

        fun getFocusX():Float = mFocusDeltaExternal.x
        fun getFocusY():Float = mFocusDeltaExternal.y
        fun getFocusDelta():PointF = mFocusDeltaExternal

         override fun updateStateByEvent(curr: MotionEvent){
             super.updateStateByEvent(curr)

             val prev = mPrevEvent

             // Focus intenal
             mCurrFocusInternal = determineFocalPoint(curr)
             mPrevFocusInternal = prev?.let { determineFocalPoint(it) }


             // Focus external
             // - Prevent skipping of focus delta when a finger is added or removed
             val mSkipNextMoveEvent = prev?.pointerCount != curr.pointerCount
             mFocusDeltaExternal = if(mSkipNextMoveEvent){
                  FOCUS_DELTA_ZERO
             }else{
                 PointF(
                     mCurrFocusInternal!!.x - mPrevFocusInternal!!.x,
                     mCurrFocusInternal!!.y - mPrevFocusInternal!!.y
                 )
             }

             // - Don't directly use mFocusInternal (or skipping will occur). Add
             // 	 unskipped delta values to mFocusExternal instead.
             mFocusExternal.x += mFocusDeltaExternal.x
             mFocusExternal.y += mFocusDeltaExternal.y

         }

        companion object{
             val FOCUS_DELTA_ZERO : PointF = PointF()

            open class SimpleOnMoveGestureListener: OnMoveGestureListener {
                override fun onMove(detector: MoveGestureDetector?): Boolean = false

                override fun onMoveBegin(detector: MoveGestureDetector?): Boolean = true

                override fun onMoveEnd(detector: MoveGestureDetector?) {
                    // Do nothing, overridden implementation may be used
                }
            }

        }
    }

    sealed class TwoFingerGestureDetector(val ctx: Context) :  BaseGestureDetector(ctx){

        private var mEdgeSlop = 0f
        private var mRightSlopEdge = 0f
        private var mBottomSlopEdge = 0f

         var mPrevFingerDiffX = 0f
         var mPrevFingerDiffY = 0f
         var mCurrFingerDiffX = 0f
         var mCurrFingerDiffY = 0f

         var mCurrLen = 0f
         var mPrevLen = 0f

        init {
            val config = ViewConfiguration.get(ctx)
            mEdgeSlop = config.scaledEdgeSlop.toFloat()
        }

        override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
        }

        override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
        }

        override fun updateStateByEvent(curr: MotionEvent) {
            super.updateStateByEvent(curr)

            val prev = mPrevEvent;

            mCurrLen = -1f
            mPrevLen = -1f

            val px0 = prev?.getX(0)
            val py0 = prev?.getY(0)
            val px1 = prev?.getX(1)
            val py1 = prev?.getY(1)
            val pvx = px1?.minus(px0!!)
            val pvy = py1?.minus(py0!!)

            mPrevFingerDiffX = pvx ?: 0f
            mPrevFingerDiffY = pvy ?: 0f

            val cx0 = curr.getX(0)
            val cy0 = curr.getY(0)
            val cx1 = curr.getX(1)
            val cy1 = curr.getY(1)
            val cvx = cx1.minus(cx0)
            val cvy = cy1.minus(cy0)

            mCurrFingerDiffX = cvx
            mCurrFingerDiffY = cvy
        }

        /**
         * Return the current distance between the two pointers forming the
         * gesture in progress.
         *
         * @return Distance between pointers in pixels.
         */
         fun getCurrentSpan(): Float {
            if (mCurrLen == -1f) {
                val cvx = mCurrFingerDiffX
                val cvy = mCurrFingerDiffY
                mCurrLen = sqrt(cvx * cvx + cvy * cvy)
            }
            return mCurrLen
        }

        /**
         * Return the previous distance between the two pointers forming the
         * gesture in progress.
         *
         * @return Previous distance between pointers in pixels.
         */
         fun getPreviousSpan(): Float {
            if (mPrevLen == -1f) {
                val pvx = mPrevFingerDiffX
                val pvy = mPrevFingerDiffY
                mPrevLen = sqrt(pvx * pvx + pvy * pvy)
            }
            return mPrevLen
        }

        /**
         * MotionEvent has no getRawX(int) method; simulate it pending future API approval.
         * @param event
         * @param pointerIndex
         * @return
         */
        protected open fun getRawX(event: MotionEvent, pointerIndex: Int): Float {
            val offset = event.x - event.rawX
            return if (pointerIndex < event.pointerCount) {
                event.getX(pointerIndex) + offset
            } else 0f
        }

        /**
         * MotionEvent has no getRawY(int) method; simulate it pending future API approval.
         * @param event
         * @param pointerIndex
         * @return
         */
        protected open fun getRawY(event: MotionEvent, pointerIndex: Int): Float {
            val offset = event.y - event.rawY
            return if (pointerIndex < event.pointerCount) {
//                event.getY(pointerIndex) + offset
                event.getY(pointerIndex)
            } else 0f
        }

        /**
         * Check if we have a sloppy gesture. Sloppy gestures can happen if the edge
         * of the user's hand is touching the screen, for example.
         *
         * @param event
         * @return
         */
         fun isSloppyGesture(event: MotionEvent): Boolean{

            // As orientation can change, query the metrics in touch down
            val metrics: DisplayMetrics = ctx.resources.displayMetrics
            mRightSlopEdge = metrics.widthPixels - mEdgeSlop
            mBottomSlopEdge = metrics.heightPixels - mEdgeSlop

            val edgeSlop: Float = mEdgeSlop
            val rightSlop: Float = mRightSlopEdge
            val bottomSlop: Float = mBottomSlopEdge

            val x0: Float = event.rawX
            val y0: Float = event.rawY
            val x1: Float = getRawX(event, 1)
            val y1: Float = getRawY(event, 1)


            val p0sloppy = x0 < edgeSlop || y0 < edgeSlop || x0 > rightSlop || y0 > bottomSlop
            val p1sloppy = x1 < edgeSlop || y1 < edgeSlop || x1 > rightSlop || y1 > bottomSlop

           return if (p0sloppy && p1sloppy) {
                 true
           } else if (p0sloppy) {
                 true
           } else {
               p1sloppy
           }

        }
        
        data class RotateGestureDetector(
            val ctx_: Context,
            val onRotateGestureListener: OnRotateGestureListener
        ) :  TwoFingerGestureDetector(ctx_){

            var mSloppyGesture:Boolean = false

            /**
             * Listener which must be implemented which is used by RotateGestureDetector
             * to perform callbacks to any implementing class which is registered to a
             * RotateGestureDetector via the constructor.
             *
             * @see RotateGestureDetector.SimpleOnRotateGestureListener
             */
            interface OnRotateGestureListener {
                fun onRotate(detector: RotateGestureDetector?): Boolean
                fun onRotateBegin(detector: RotateGestureDetector?): Boolean
                fun onRotateEnd(detector: RotateGestureDetector?)
            }
            override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
                Log.e("Rotatetetetet", "$actionCode")
                when(actionCode){
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        // At least the second finger is on screen now
                        resetState() // In case we missed an UP/CANCEL event
                        mPrevEvent = MotionEvent.obtain(event)
                        mTimeDelta = 0

                        updateStateByEvent(event)

                        // See if we have a sloppy gesture
                        mSloppyGesture = isSloppyGesture(event)
                        if (!mSloppyGesture) {
                            // No, start gesture now
                            mGestureInProgress = onRotateGestureListener.onRotateBegin(this)
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (!mSloppyGesture) {
                            return
                        }
                        // See if we still have a sloppy gesture
                        mSloppyGesture = isSloppyGesture(event);
                        if (!mSloppyGesture) {
                            // No, start normal gesture now
                            mGestureInProgress = onRotateGestureListener.onRotateBegin(this)
                        }
                    }
                }
            }

            override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
                when(actionCode) {
                    MotionEvent.ACTION_POINTER_UP -> {
                        // Gesture ended but
                        updateStateByEvent(event)

                        if (!mSloppyGesture) {
                            onRotateGestureListener.onRotateEnd(this)
                        }

                        resetState()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        if (!mSloppyGesture) {
                            onRotateGestureListener.onRotateEnd(this)
                        }

                        resetState();
                    }
                    MotionEvent.ACTION_MOVE -> {
                        updateStateByEvent(event);

                        // Only accept the event if our relative pressure is within
                        // a certain limit. This can help filter shaky data as a
                        // finger is lifted.
                        if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
                            val updatePrevious = onRotateGestureListener.onRotate(this)
                            if (updatePrevious) {
                                mPrevEvent?.recycle()
                                mPrevEvent = MotionEvent.obtain(event)
                            }
                        }
                    }
                }
            }


            override fun resetState(){
                super.resetState()
                mSloppyGesture = false

            }

            companion object{
                open class SimpleOnRotateGestureListener (): OnRotateGestureListener{
                    override fun onRotate(detector: RotateGestureDetector?): Boolean= false

                    override fun onRotateBegin(detector: RotateGestureDetector?): Boolean  = true

                    override fun onRotateEnd(detector: RotateGestureDetector?) {
                        // Do nothing, overridden implementation may be used
                    }

                }
            }

            /**
             * Return the rotation difference from the previous rotate event to the current
             * event.
             *
             * @return The current rotation //difference in degrees.
             */
            fun getRotationDegreesDelta(): Float {
                val diffRadians = atan2(mPrevFingerDiffY.toDouble(), mPrevFingerDiffX.toDouble()) -
                        atan2(mCurrFingerDiffY.toDouble(), mCurrFingerDiffX.toDouble())
                return (diffRadians * 180 / Math.PI).toFloat()
            }

            fun getFocusX(): Float {
                return mCurrEvent!!.x + mCurrFingerDiffX * 0.5f
            }

            fun getFocusY(): Float {
                return mCurrEvent!!.y + mCurrFingerDiffY * 0.5f
            }
        }



    }
}