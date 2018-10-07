package com.raybritton.liveWallpaper

import android.content.ComponentName
import android.graphics.Color
import android.os.Bundle
import com.raybritton.template.BaseSettingsActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseSettingsActivity() {

    override val settingsActivityComponent by lazy { ComponentName(this, "$packageName.LauncherSettingsActivity") }
    override val wallpaperServiceComponent by lazy { ComponentName(this, ExampleWallpaperService::class.java) } //TODO return your BaseWallpaperService implementation here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings_live_wallpaper.renderer = ExampleWallpaperDrawer(this)
    }

    override fun onResume() {
        super.onResume()

        //TODO handle your settings here

        blue.setOnClickListener {
            blue.isSelected = true
            (settings_live_wallpaper.renderer as ExampleWallpaperDrawer).color = Color.BLUE
        }

        red.setOnClickListener {
            red.isSelected = true
            (settings_live_wallpaper.renderer as ExampleWallpaperDrawer).color = Color.RED
        }

        green.setOnClickListener {
            green.isSelected = true
            (settings_live_wallpaper.renderer as ExampleWallpaperDrawer).color = Color.GREEN
        }
    }
}
