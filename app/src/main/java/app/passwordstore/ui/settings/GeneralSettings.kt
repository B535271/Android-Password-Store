/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.passwordstore.ui.settings

import androidx.fragment.app.FragmentActivity
import app.passwordstore.R
import app.passwordstore.util.settings.DirectoryStructure
import app.passwordstore.util.settings.PreferenceKeys
import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.helpers.singleChoice
import de.Maxr1998.modernpreferences.helpers.switch
import de.Maxr1998.modernpreferences.preferences.choice.SelectionItem

class GeneralSettings(private val activity: FragmentActivity) : SettingsProvider {

  override fun provideSettings(builder: PreferenceScreen.Builder) {
    builder.apply {
      val themeValues = activity.resources.getStringArray(R.array.app_theme_values)
      val themeOptions = activity.resources.getStringArray(R.array.app_theme_options)
      val themeItems =
        themeValues.zip(themeOptions).map { SelectionItem(it.first, it.second, null) }
      singleChoice(PreferenceKeys.APP_THEME, themeItems) {
        initialSelection = activity.resources.getString(R.string.app_theme_def)
        titleRes = R.string.pref_app_theme_title
      }

      val values = activity.resources.getStringArray(R.array.directory_structure_values)
      val titles = activity.resources.getStringArray(R.array.directory_structure_entries)
      val items = values.zip(titles).map { SelectionItem(it.first, it.second, null) }
      singleChoice(PreferenceKeys.DIRECTORY_STRUCTURE, items) {
        initialSelection = DirectoryStructure.DEFAULT.value
        titleRes = R.string.pref_directory_structure
      }

      val sortValues = activity.resources.getStringArray(R.array.sort_order_values)
      val sortOptions = activity.resources.getStringArray(R.array.sort_order_entries)
      val sortItems = sortValues.zip(sortOptions).map { SelectionItem(it.first, it.second, null) }
      singleChoice(PreferenceKeys.SORT_ORDER, sortItems) {
        initialSelection = sortValues[0]
        titleRes = R.string.pref_sort_order_title
      }

      switch(PreferenceKeys.DISABLE_SYNC_ACTION) {
        titleRes = R.string.pref_disable_sync_on_pull_title
        summaryRes = R.string.pref_disable_sync_on_pull_summary
        defaultValue = false
      }

      switch(PreferenceKeys.FILTER_RECURSIVELY) {
        titleRes = R.string.pref_recursive_filter_title
        summaryRes = R.string.pref_recursive_filter_summary
        defaultValue = true
      }

      switch(PreferenceKeys.SEARCH_ON_START) {
        titleRes = R.string.pref_search_on_start_title
        summaryRes = R.string.pref_search_on_start_summary
        defaultValue = false
      }

      switch(PreferenceKeys.SHOW_HIDDEN_CONTENTS) {
        titleRes = R.string.pref_show_hidden_title
        summaryRes = R.string.pref_show_hidden_summary
        defaultValue = false
      }
    }
  }
}
