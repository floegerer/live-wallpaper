package com.raybritton.template

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * This allows the settings activity to show a preview of the wallpaper to the user
 */
class WallpaperDemoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Runnable {
    val renderer: WallpaperRenderer? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post { renderer!!.setCanvasSize(width, height) }
        post(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        renderer!!.draw(canvas)
    }

    override fun run() {
        postInvalidate()
        postDelayed(this, 15)
    }
}
