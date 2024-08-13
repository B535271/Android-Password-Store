/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.passwordstore.gradle.crowdin

import app.passwordstore.gradle.OkHttp
import app.passwordstore.gradle.crowdin.api.ListProjects
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "This calls into a remote API and has nothing to cache")
abstract class BuildOnApiTask : DefaultTask() {

  @get:Input abstract val crowdinIdentifier: Property<String>
  @get:Internal abstract val crowdinLogin: Property<String>
  @get:Internal abstract val crowdinKey: Property<String>

  @TaskAction
  fun doWork() {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val projectAdapter = moshi.adapter(ListProjects::class.java)
    val projectRequest =
      Request.Builder()
        .url("$CROWDIN_BASE_URL/projects")
        .header("Authorization", "Bearer ${crowdinKey.get()}")
        .get()
        .build()
    OkHttp.CLIENT.newCall(projectRequest).execute().use { response ->
      val projects = projectAdapter.fromJson(response.body.source())
      if (projects != null) {
        val identifier =
          projects.projects
            .first { data -> data.project.identifier == crowdinIdentifier.get() }
            .project
            .id
            .toString()
        val buildRequest =
          Request.Builder()
            .url(CROWDIN_BUILD_API_URL.format(identifier))
            .header("Authorization", "Bearer ${crowdinKey.get()}")
            .post("{}".toRequestBody("application/json".toMediaType()))
            .build()
        OkHttp.CLIENT.newCall(buildRequest).execute().close()
      }
    }
  }

  private companion object {

    private const val CROWDIN_BASE_URL = "https://api.crowdin.com/api/v2"
    private const val CROWDIN_BUILD_API_URL = "$CROWDIN_BASE_URL/projects/%s/translations/builds"
  }
}
