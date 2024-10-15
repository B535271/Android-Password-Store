/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.passwordstore

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.StrictMode
import app.passwordstore.data.crypto.PGPPassphraseCache
import app.passwordstore.injection.context.FilesDirPath
import app.passwordstore.injection.prefs.SettingsPreferences
import app.passwordstore.util.coroutines.DispatcherProvider
import app.passwordstore.util.extensions.getString
import app.passwordstore.util.features.Features
import app.passwordstore.util.git.sshj.setUpBouncyCastleForSshj
import app.passwordstore.util.settings.GitSettings
import app.passwordstore.util.settings.PreferenceKeys
import app.passwordstore.util.settings.runMigrations
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors
import javax.inject.Inject
import logcat.AndroidLogcatLogger
import logcat.LogPriority.DEBUG
import logcat.LogPriority.VERBOSE
import logcat.LogcatLogger
import logcat.logcat

@Suppress("Unused")
@HiltAndroidApp
class Application : android.app.Application(), SharedPreferences.OnSharedPreferenceChangeListener {

  @Inject @SettingsPreferences lateinit var prefs: SharedPreferences
  @Inject @FilesDirPath lateinit var filesDirPath: String
  @Inject lateinit var dispatcherProvider: DispatcherProvider
  @Inject lateinit var passphraseCache: PGPPassphraseCache
  @Inject lateinit var gitSettings: GitSettings
  @Inject lateinit var features: Features

  override fun onCreate() {
    super.onCreate()
    instance = this
    if (
      BuildConfig.ENABLE_DEBUG_FEATURES ||
        prefs.getBoolean(PreferenceKeys.ENABLE_DEBUG_LOGGING, false)
    ) {
      LogcatLogger.install(AndroidLogcatLogger(DEBUG))
      setVmPolicy()
    }
    prefs.registerOnSharedPreferenceChangeListener(this)
    setNightMode()
    setUpBouncyCastleForSshj()
    runMigrations(filesDirPath, prefs, gitSettings)
    DynamicColors.applyToActivitiesIfAvailable(this)
    setupScreenOffHandler()
  }

  private fun setupScreenOffHandler() {
    val screenOffReceiver: BroadcastReceiver =
      object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
          if (intent.action == Intent.ACTION_SCREEN_OFF) screenWasOff = true
        }
      }
    val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
    registerReceiver(screenOffReceiver, filter)
  }

  override fun onTerminate() {
    prefs.unregisterOnSharedPreferenceChangeListener(this)
    super.onTerminate()
  }

  override fun onSharedPreferenceChanged(prefs: SharedPreferences, key: String?) {
    if (key == PreferenceKeys.APP_THEME) {
      setNightMode()
    }
  }

  private fun setVmPolicy() {
    val builder =
      StrictMode.VmPolicy.Builder()
        .detectActivityLeaks()
        .detectFileUriExposure()
        .detectLeakedClosableObjects()
        .detectLeakedRegistrationObjects()
        .detectLeakedSqlLiteObjects()

    builder.detectContentUriWithoutPermission()

    builder.detectCredentialProtectedWhileLocked().detectImplicitDirectBoot()

    builder.detectNonSdkApiUsage()

    builder.detectIncorrectContextUse().detectUnsafeIntentLaunch()

    builder.penaltyListener(Executors.newSingleThreadExecutor()) { violation ->
      logcat(VERBOSE) { violation.stackTraceToString() }
    }

    val policy = builder.build()
    StrictMode.setVmPolicy(policy)
  }

  private fun setNightMode() {
    val uim = getSystemService(UI_MODE_SERVICE) as UiModeManager;
    uim.setApplicationNightMode(
      when (prefs.getString(PreferenceKeys.APP_THEME) ?: getString(R.string.app_theme_def)) {
        "light" -> UiModeManager.MODE_NIGHT_NO
        "dark" -> UiModeManager.MODE_NIGHT_YES
        "follow_system" -> UiModeManager.MODE_NIGHT_AUTO
        else -> UiModeManager.MODE_NIGHT_AUTO
      }
    )
  }

  companion object {

    lateinit var instance: Application
    var screenWasOff: Boolean = true
  }
}
