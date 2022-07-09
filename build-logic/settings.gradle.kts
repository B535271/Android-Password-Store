/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */
@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

dependencyResolutionManagement {
  repositories {
    exclusiveContent {
      forRepository(::google)
      filter {
        includeGroup("androidx.databinding")
        includeGroup("com.android")
        includeGroup("com.android.tools.analytics-library")
        includeGroup("com.android.tools.build")
        includeGroup("com.android.tools.build.jetifier")
        includeGroup("com.android.databinding")
        includeGroup("com.android.tools.ddms")
        includeGroup("com.android.tools.layoutlib")
        includeGroup("com.android.tools.lint")
        includeGroup("com.android.tools.utp")
        includeGroup("com.google.testing.platform")
        includeModule("com.android.tools", "annotations")
        includeModule("com.android.tools", "common")
        includeModule("com.android.tools", "desugar_jdk_libs")
        includeModule("com.android.tools", "desugar_jdk_libs_configuration")
        includeModule("com.android.tools", "dvlib")
        includeModule("com.android.tools", "play-sdk-proto")
        includeModule("com.android.tools", "repository")
        includeModule("com.android.tools", "sdklib")
        includeModule("com.android.tools", "sdk-common")
      }
    }
    exclusiveContent {
      forRepository(::gradlePluginPortal)
      filter { includeModule("com.github.ben-manes", "gradle-versions-plugin") }
    }
    exclusiveContent {
      forRepository { maven("https://storage.googleapis.com/r8-releases/raw") }
      filter { includeModule("com.android.tools", "r8") }
    }
    mavenCentral()
  }
  versionCatalogs {
    maybeCreate("libs").apply {
      from(files("../gradle/libs.versions.toml"))
      if (System.getenv("DEP_OVERRIDE") == "true") {
        val overrides = System.getenv().filterKeys { it.startsWith("DEP_OVERRIDE_") }
        for ((key, value) in overrides) {
          val catalogKey = key.removePrefix("DEP_OVERRIDE_").toLowerCase()
          println("Overriding $catalogKey with $value")
          version(catalogKey, value)
        }
      }
    }
  }
}

include("android-plugins")

include("automation-plugins")

include("kotlin-plugins")
