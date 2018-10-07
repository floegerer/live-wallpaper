package com.raybritton.liveWallpaper

import android.content.ComponentName
import com.raybritton.template.BaseSettingsActivity

class SettingsActivity : BaseSettingsActivity() {

    override val settingsActivityComponent by lazy { ComponentName(this, "$packageName.LauncherSettingsActivity") }
    override val wallpaperServiceComponent by lazy { ComponentName(this, ExampleWallpaperService::class.java) }

    override fun onResume() {
        super.onResume()

    }
}
