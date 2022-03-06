package com.wizeline.bookchallenge.views.customeviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.wizeline.bookchallenge.utils.interaction.GestureLayer
import java.util.*

class InteractiveViewPort @JvmOverloads constructor(
    context: Context, val scaleFactor: Int = 1, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    AppCompatImageView(context, attrs, defStyleAttr){

    private val layers = LinkedList<GestureLayer>()
    private var target: GestureLayer? = null


    override fun setImageResource(@IdRes  resId: Int) {
        val vectorDraw = ContextCompat.getDrawable(context, resId) as VectorDrawable
        val layer = GestureLayer(context, this,
            vectorDraw.toBitmap(vectorDraw.intrinsicWidth * scaleFactor,
                vectorDraw.intrinsicHeight * scaleFactor),
            (parent as View).measuredWidth, (parent as View).measuredHeight)
        layers.add(layer)
    }

    override fun onDraw(canvas: Canvas?) {
       layers.forEach { layer -> canvas?.let { layer.draw(it) }}
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if(event.action == MotionEvent.ACTION_DOWN){
            // reset target reference
            target = null
            // loop through the layer of Layer class objects
            for (n in layers.size-1 downTo 0) {
                val layer = layers[n]
                // check of the layer object contains bitmap where the touch occurred
                if (layer.contains(event)) {
                    target = layer
                    // remove occurrence of that object from list and add it to the end of the list
                    layers.remove(layer)
                    layers.add(layer)
                    invalidate()
                    break
                }
            }
        }
        return target?.onTouchEvent(event) ?: false
    }
}