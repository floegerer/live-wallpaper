package com.raybritton.template

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.TypedValue
import android.view.View
import android.widget.CompoundButton
import com.raybritton.liveWallpaper.R
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * This provides a large foundation for the settings activity
 *
 * It automatically provides onClickListeners for the Set Wallpaper and Show/Hide App Icon views,
 * enabled drawing behind the status bar, and allowing rotation for tablet only
 */
abstract class BaseSettingsActivity : Activity() {

    /**
     * The app live wallpaper service component (in the template this is ExampleWallpaperService)
     */
    abstract val wallpaperServiceComponent: ComponentName

    /**
     * The child of this class (in the template this is SettingsActivity)
     */
    abstract val settingsActivityComponent: ComponentName

    private val wallpaperManager by lazy { getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager }

    /**
     * This intent opens the screens that lets the user set this wallpaper as their active one
     */
    private val wallpaperIntent by lazy {
        Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, wallpaperServiceComponent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setDrawBelowStatusBar()
        positionViewsInsideSystemViews()

        /**
         * This prevents rotation on phones as the layout in `activity_settings.xml` doesn't work on
         * phones in landscape
         */
        requestedOrientation = if (resources.getBoolean(R.bool.is_tablet)) {
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        } else {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }

        settings_set_wallpaper.setOnClickListener {
            openWallpaperConfirmActivity()
        }

        useShowAppToggle(settings_show_app_icon)
    }

    override fun onResume() {
        super.onResume()
        if (isWallpaperSupported()) {
            settings_set_wallpaper.visibility = if (isThisAppTheSelectedWallpaper()) View.GONE else View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * If we've returned from the screen that lets the user set this as the active one and
         * this wallpaper is now the active one then close as the user is probably finished setting
         * things up
         */
        if (requestCode == REQUEST_SET_WALLPAPER) {
            if (isThisAppTheSelectedWallpaper()) {
                finish()
            }
        }
    }

    private fun useShowAppToggle(view: CompoundButton) {
        view.setOnCheckedChangeListener(null)
        view.isChecked = packageManager.getComponentEnabledSetting(settingsActivityComponent) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||  packageManager.getComponentEnabledSetting(settingsActivityComponent) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
        view.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                packageManager.setComponentEnabledSetting(settingsActivityComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            } else {
                packageManager.setComponentEnabledSetting(settingsActivityComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            }
        }
    }

    private fun openWallpaperConfirmActivity() {
        startActivityForResult(wallpaperIntent, REQUEST_SET_WALLPAPER)
    }

    /**
     * Returns true if the currently active wallpaper is this app
     * Returns false if a different live wallpaper or a image is being used
     */
    private fun isThisAppTheSelectedWallpaper(): Boolean {
        val info = wallpaperManager.wallpaperInfo
        //info.component is null if a static image is being used
        //otherwise is the ComponentName of the live wallpaper
        return info != null && info.component == wallpaperServiceComponent
    }

    private fun isWallpaperSupported(): Boolean {
        return if (intent.resolveActivity(packageManager) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.isWallpaperSupported && wallpaperManager.isSetWallpaperAllowed
            } else {
                true
            }
        } else {
            false
        }
    }

    /**
     * Allows the activity to draw below the status bar
     * This is mainly to allow the wallpaper to appear like it will on the launcher
     */
    private fun setDrawBelowStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    /**
     * Moves the views that user should interact with inside the app window after they moved behind
     * the status and navigation bars by setDrawBelowStatusBar
     */
    private fun positionViewsInsideSystemViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.setOnApplyWindowInsetsListener { _, insets ->
                run {
                    val params = settings_set_wallpaper.layoutParams
                            ?: ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    (params as ConstraintLayout.LayoutParams).topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt() + insets.systemWindowInsetTop
                    settings_set_wallpaper.layoutParams = params
                }
                run {
                    val params = settings_prefs_container.layoutParams
                            ?: ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    (params as ConstraintLayout.LayoutParams).bottomMargin = insets.systemWindowInsetBottom
                    settings_prefs_container.layoutParams = params
                }
                insets.consumeSystemWindowInsets()
            }
        }
    }

    /**
     * Can be used to make status bar icons black
     * Call this if your wallpaper is generally light
     *
     * This can be called at any time repeatedly
     */
    protected fun setDarkStatusBarIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
        }
    }

    /**
     * Can be used to make status bar icons white
     * Call this if your wallpaper is generally dark
     *
     * This can be called at any time repeatedly
     */
    protected fun setLightStatusBarIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }
    }

    companion object {
        private const val REQUEST_SET_WALLPAPER = 100
    }
}