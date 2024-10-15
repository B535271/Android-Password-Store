/*
 * Copyright © 2014-2024 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.passwordstore.gradle

import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("Unused")
class KotlinCommonPlugin : Plugin<Project> {

  override fun apply(project: Project) {
    val isAppModule = project.pluginManager.hasPlugin("com.android.application")

    project.tasks.run {

      withType<KotlinCompile>().configureEach task@ {
        compilerOptions {
          allWarningsAsErrors.set(project.providers.environmentVariable("CI").isPresent)
          apiVersion.set(KotlinVersion.KOTLIN_2_0)
          languageVersion.set(KotlinVersion.KOTLIN_2_0)
          jvmTarget.set(JvmTarget.JVM_17)
          optIn.set(listOf("kotlin.RequiresOptIn"))
          freeCompilerArgs.addAll(ADDITIONAL_COMPILER_ARGS)
          if (!this@task.name.contains("test", ignoreCase = true) && !isAppModule) {
            // Force the compiler to report errors on all public API declarations without an explicit visibility or a return type.
            freeCompilerArgs.add("-Xexplicit-api=strict")
          }
        }
      }

      withType<JavaCompile>().configureEach {
        sourceCompatibility = "17"
        targetCompatibility = "17"
      }

      withType<Test>().configureEach {
        maxParallelForks = Runtime.getRuntime().availableProcessors() * 2
        testLogging { events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED) }
      }
    }
  }

  companion object {
    private val ADDITIONAL_COMPILER_ARGS = emptyList<String>()
//      listOf("-Xsuppress-version-warnings")

//    val JVM_TOOLCHAIN_ACTION =
//      Action<JavaToolchainSpec> { languageVersion.set(JavaLanguageVersion.of(17)) }
  }
}
