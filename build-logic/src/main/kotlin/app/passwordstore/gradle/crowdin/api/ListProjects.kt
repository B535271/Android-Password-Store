/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.passwordstore.gradle.crowdin.api

import com.squareup.moshi.Json

data class ListProjects(@Json(name = "data") val projects: List<ProjectData>)

data class ProjectData(@Json(name = "data") val project: Project)

data class Project(val id: Long, val identifier: String)
