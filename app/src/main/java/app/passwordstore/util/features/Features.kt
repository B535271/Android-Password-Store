/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.passwordstore.util.features

import android.content.SharedPreferences
import app.passwordstore.injection.prefs.SettingsPreferences
import javax.inject.Inject

class Features
@Inject
constructor(@SettingsPreferences private val preferences: SharedPreferences) {

  fun isEnabled(feature: Feature): Boolean {
    return preferences.getBoolean(feature.configKey, feature.defaultValue)
  }
}
