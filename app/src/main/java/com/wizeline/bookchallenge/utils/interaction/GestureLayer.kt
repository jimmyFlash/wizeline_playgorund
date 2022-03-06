package com.wizeline.bookchallenge.utils.interaction

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt


data class GestureLayer(val context: Context, val parent: View, val bitmap: Bitmap,
                        val prntWidth:Int = 0, val prntHeight:Int = 0) {

    val matrix = Matrix()
    private val inverse = Matrix()

    private val bounds: RectF = RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat())

    private var moveGestureDetector: BaseGestureDetector.MoveGestureDetector
    private var rotateGestureDetector: BaseGestureDetector.TwoFingerGestureDetector.RotateGestureDetector
    private var scaleGestureDetector: ScaleGestureDetector


    private val simpleOnMoveListener = object :
        BaseGestureDetector.MoveGestureDetector.Companion.SimpleOnMoveGestureListener(){
            override fun onMove(detector: BaseGestureDetector.MoveGestureDetector?): Boolean {
                val delta = detector!!.getFocusDelta()
                //Post concat the matrix with the specified translation. equal two difference between start and end touch
                matrix.postTranslate(delta.x, delta.y)
                parent.invalidate()
                return true
            }
        }


    val simpleOnScaleGestureListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener(){

        override fun onScale(detector: ScaleGestureDetector): Boolean {

            val scale = detector.scaleFactor

            val values = FloatArray(9)
            matrix.getValues(values)
            val scaleX = values[Matrix.MSCALE_X]
            val skewY = values[Matrix.MSKEW_Y]

            val previousSpan = detector.previousSpan
            val currentSpan = detector.currentSpan

            val zoomState = if(detector.previousSpan > detector.currentSpan){
                ZoomStates.ZoomOut
            }else{
                ZoomStates.ZoomIn
            }

            val rScale = sqrt((scaleX * scaleX + skewY * skewY).toDouble()).toFloat()

            // calculate the degree of rotation
            val rAngle = (atan2(
                values[Matrix.MSKEW_X].toDouble(),
                values[Matrix.MSCALE_X].toDouble()
            ) * (180 / Math.PI)).roundToInt().toFloat()

            when(zoomState){
                ZoomStates.ZoomIn -> {
                    if (rScale < 2.0F) {
                        //Postconcats the matrix with the specified scale. M' = S(sx, sy, px, py) * M,
                        // using current scale and  the X coordinate of the current gesture's focal point.
                        // If a gesture is in progress, the focal point is between each of the pointers forming the gesture
                        matrix.postScale(scale, scale, detector.focusX, detector.focusY)
                        parent.invalidate()
                    }
                }

                ZoomStates.ZoomOut -> {
                    if (rScale > 0.5F) {
                        //Postconcats the matrix with the specified scale. M' = S(sx, sy, px, py) * M,
                        // using current scale and  the X coordinate of the current gesture's focal point.
                        // If a gesture is in progress, the focal point is between each of the pointers forming the gesture
                        matrix.postScale(scale, scale, detector.focusX, detector.focusY)
                        parent.invalidate()
                    }
                }
            }
            return true
        }

    }

    private val simpleOnrotateListener = object : BaseGestureDetector.TwoFingerGestureDetector.
                            RotateGestureDetector.Companion.SimpleOnRotateGestureListener(){

        override fun onRotate(detector: BaseGestureDetector.TwoFingerGestureDetector.
            RotateGestureDetector?): Boolean {

            detector?.let {
                matrix.postRotate(
                    -it.getRotationDegreesDelta(),
                    it.getFocusX(),
                    it.getFocusY()
                )
            }

            parent.invalidate()
            return true
        }
    }


    init {
        // get the bounds of the bitmap at default position point (0,0)

        moveGestureDetector = BaseGestureDetector.MoveGestureDetector(context, simpleOnMoveListener)
        rotateGestureDetector = BaseGestureDetector.TwoFingerGestureDetector.RotateGestureDetector(
            context,
            simpleOnrotateListener
        )
        scaleGestureDetector = ScaleGestureDetector(context, simpleOnScaleGestureListener)


        // translate to random position for each added bitmap
        matrix.postTranslate(prntWidth.toFloat()/2 - bitmap.width.toFloat()/2,
            prntHeight.toFloat()/2 - bitmap.height.toFloat()/2)

    }


    // check if the touch event in on area containing a bitmap
    fun contains(event : MotionEvent):Boolean{

        //If this matrix can be inverted, return true and if inverse is not null, set inverse to be the inverse of this matrix.
        matrix.invert(inverse)

        //store the x,y point values of th emotion event in array
        val pts = floatArrayOf(event.x, event.y)

        // Apply this matrix to the array of 2D points, and write the transformed points back into the array
        inverse.mapPoints(pts)
        // if the bounding rectangle of the bitmap doesn't contain
        if (!bounds.contains(pts[0], pts[1])) {
            return false
        }

        // get the alpha value of the pixel underlying
        return Color.alpha(bitmap.getPixel(pts[0].toInt(), pts[1].toInt())) != 0
    }

    // handle listener on this object for move , rotate and scale
    fun onTouchEvent(event : MotionEvent): Boolean{

        moveGestureDetector.onTouchEvent(event, moveGestureDetector)
        rotateGestureDetector.onTouchEvent(event, rotateGestureDetector)
        scaleGestureDetector.onTouchEvent(event)

        return true
    }

    // draw the bitmap with transform matrix to canvas on the view
    fun draw(canvas : Canvas){

        canvas.drawBitmap(bitmap, matrix, null)
    }



}

sealed class ZoomStates{
    object ZoomOut : ZoomStates()
    object ZoomIn : ZoomStates()
}