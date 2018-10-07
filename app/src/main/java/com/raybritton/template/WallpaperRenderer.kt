package com.raybritton.template

import android.graphics.Canvas

/**
 * This interface is for wallpaper drawers (like ExampleWallpaperDrawer) to implement
 */
interface WallpaperRenderer {
    /**
     * Render one frame of the wallpaper
     * This will be called up to 60 times a second
     *
     * As with other views, you should avoid heavy method calls and creating objects in this method
     */
    fun draw(canvas: Canvas)

    /**
     * This sets the size available to draw
     * This may be called at any time
     */
    fun setCanvasSize(width: Int, height: Int)
}