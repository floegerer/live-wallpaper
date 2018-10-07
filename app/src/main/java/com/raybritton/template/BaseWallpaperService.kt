package com.raybritton.template

import android.graphics.Canvas
import android.os.Handler
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

/**
 * The engine is used by the launcher to render the wallpaper
 *
 * A handler is triggered at most every 8 ms to redraw the wallpaper
 */
abstract class BaseWallpaperService : WallpaperService() {

    abstract fun createWallpaperRenderer(): WallpaperRenderer

    override fun onCreateEngine(): WallpaperService.Engine {
        return WallpaperEngine()
    }

    private inner class WallpaperEngine internal constructor() : WallpaperService.Engine() {
        private val handler = Handler()
        private val drawRunner = Runnable { draw() }

        private var visible = true
        private val wallpaperRenderer: WallpaperRenderer = createWallpaperRenderer()

        init {
            handler.removeCallbacks(drawRunner)
            handler.post(drawRunner)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                handler.removeCallbacks(drawRunner)
                handler.post(drawRunner)
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            this.visible = false
            handler.removeCallbacks(drawRunner)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            wallpaperRenderer.setCanvasSize(width, height)
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder) {
            super.onSurfaceRedrawNeeded(holder)
            draw()
        }

        private fun draw() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas() //NOTE: This method can take up to 8ms to run
                if (canvas != null) {
                    wallpaperRenderer.draw(canvas)
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas) //NOTE: This method can take up to 16ms to run
                }
            }

            handler.removeCallbacks(drawRunner)

            if (visible) {
                /**
                 * At most once per 8 ms the wallpaper will be redrawn
                 * This number can be changed to allow for faster updates (by reducing the number)
                 * or to reduce CPU and battery usage (by increasing the number)
                 */
                handler.postDelayed(drawRunner, 8)
            }
        }
    }
}