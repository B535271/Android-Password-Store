/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */
@file:Suppress("UnstableApiUsage")

plugins {
  id("com.github.android-password-store.android-application")
  id("com.github.android-password-store.kotlin-android")
  id("com.github.android-password-store.versioning-plugin")
  id("com.github.android-password-store.rename-artifacts")
  //  alias(libs.plugins.ksp)
  id("com.github.android-password-store.kotlin-kapt")
  //  kotlin("kapt") version "2.0.21" // already included by build-logic
  alias(libs.plugins.hilt)
  alias(libs.plugins.kotlin.composeCompiler)
}

android {
  namespace = "app.passwordstore"

  defaultConfig {
    applicationId = "app.passwordstore"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures { compose = true }

  androidResources { generateLocaleConfig = true }

  packaging { resources.excludes.add("META-INF/versions/**") }
}

composeCompiler { reportsDestination = layout.buildDirectory.dir("compose_compiler") }

kapt { correctErrorTypes = true } // Allow references to generated code

hilt { enableAggregatingTask = true }

dependencies {
  implementation(platform(libs.compose.bom))
  
  kapt(libs.dagger.hilt.compiler)
  //  ksp(libs.dagger.hilt.compiler)
  implementation(libs.dagger.hilt.android)
  implementation(libs.androidx.annotation)

  implementation(projects.coroutineUtils)
  implementation(projects.crypto.pgpainless)
  implementation(projects.format.common)
  implementation(projects.passgen.diceware)
  implementation(projects.passgen.random)
  implementation(projects.ui.compose)

  implementation(libs.bundles.androidxLifecycle)
  kapt(libs.androidx.lifecycle.compiler)

  implementation(libs.androidx.activity.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.documentfile)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.preference.ktx)
  implementation(libs.androidx.recyclerview)
  implementation(libs.androidx.recyclerviewSelection)
  implementation(libs.androidx.security)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.bundles.compose)

  implementation(libs.android.material.components)

  implementation(libs.kotlinx.collections.immutable)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)

  implementation(libs.aps.sublimeFuzzy)
  implementation(libs.aps.zxingAndroidEmbedded)

  implementation(libs.thirdparty.eddsa)
  implementation(libs.thirdparty.fastscroll)
  implementation(libs.thirdparty.flowbinding.android)
  implementation(libs.thirdparty.jgit) {
    exclude(group = "org.apache.httpcomponents", module = "httpclient")
  }
  implementation(libs.thirdparty.kotlinResult)
  implementation(libs.thirdparty.logcat)
  implementation(libs.thirdparty.modernAndroidPrefs)
  implementation(libs.thirdparty.leakcanary.plumber)
  implementation(libs.thirdparty.sshj)
  implementation(libs.thirdparty.bouncycastle.bcprov)
  implementation(libs.thirdparty.bouncycastle.bcutil)

  implementation(libs.thirdparty.slf4j.api) {
    because("SSHJ now uses SLF4J 2.0 which we don't want")
  }

  testImplementation(libs.testing.robolectric)
  testImplementation(libs.testing.sharedPrefsMock)
  testImplementation(libs.bundles.testDependencies)
}
