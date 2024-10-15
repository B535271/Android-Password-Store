/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.passwordstore.ui.compose.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents light and dark themes. Add this annotation to a
 * composable to render the both themes.
 */
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
public annotation class ThemePreviews

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(name = "phone", device = "spec:width=411dp,height=891dp")
@Preview(name = "landscape", device = "spec:width=891dp,height=411dp")
@Preview(name = "foldable", device = "spec:width=673dp,height=841dp")
@Preview(name = "tablet", device = "spec:width=1280dp,height=800dp,dpi=240")
public annotation class DevicePreviews
