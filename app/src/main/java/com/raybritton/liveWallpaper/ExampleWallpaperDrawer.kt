package com.raybritton.liveWallpaper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue
import com.raybritton.template.WallpaperRenderer

class ExampleWallpaperDrawer(val context: Context) : WallpaperRenderer {
    private var width: Int = 0
    private var height: Int = 0

    private var yPos = 0f
    private var increment = 5

    private val preferences = context.getSharedPreferences("example_wallpaper", Context.MODE_PRIVATE)
    private val paint = Paint().apply {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, context.resources.displayMetrics)
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    var color: Int
        get() = paint.color
        set(value) {
            paint.color = value
            preferences.edit().putInt("color", value).apply()
        }

    init {
        color = preferences.getInt("color", Color.BLUE)
    }

    override fun setCanvasSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        yPos = height * 0.5f
    }

    override fun draw(canvas: Canvas) {
        yPos += increment
        if (yPos > (height - paint.textSize)) {
            increment = -5
        } else if (yPos < paint.textSize) {
            increment = 5
        }
        canvas.drawColor(Color.BLACK)
        canvas.drawText("Live Wallpaper Template", width * 0.5f, yPos, paint)
    }
}