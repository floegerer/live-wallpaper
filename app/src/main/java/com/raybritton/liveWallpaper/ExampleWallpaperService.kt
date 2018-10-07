package com.raybritton.liveWallpaper

import com.raybritton.template.BaseWallpaperService
import com.raybritton.template.WallpaperRenderer

class ExampleWallpaperService : BaseWallpaperService() {
    override fun createWallpaperRenderer(): WallpaperRenderer {
        return ExampleWallpaperDrawer(applicationContext)
    }
}