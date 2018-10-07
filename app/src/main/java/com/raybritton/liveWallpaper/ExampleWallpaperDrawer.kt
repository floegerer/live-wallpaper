package com.raybritton.liveWallpaper

import android.graphics.Canvas
import com.raybritton.template.WallpaperRenderer

class ExampleWallpaperDrawer : WallpaperRenderer {
    private var width: Int = 0
    private var height: Int = 0

    override fun setCanvasSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun draw(canvas: Canvas) {

    }
}