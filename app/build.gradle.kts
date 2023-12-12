/*
 *  Copyright 2023 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import java.io.FileInputStream
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.hilt)
    alias(libs.plugins.schema.parser)
    alias(libs.plugins.junit)
}

android {

    namespace = project.property("basePackageName") as String?
    compileSdk = (project.property("targetSdk") as String).toInt()

    defaultConfig {

        applicationId = project.property("basePackageName") as String
        minSdk = (project.property("minSdk") as String).toInt()
        targetSdk = (project.property("targetSdk") as String).toInt()
        versionCode = (project.property("versionCode") as String).toInt()
        versionName = project.property("versionName") as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"

        resValue("string", "app_name", project.property("appName") as String)

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
    }

    buildFeatures {
        // Enable dataBinding if needed.
        // dataBinding = true
        viewBinding = true
    }

}

dependencies {

    // .jar-based libraries included in project

    // Basic Android components
    implementation(libs.app.compat)
    implementation(libs.constraint.layout)
    implementation(libs.recycler.view)

    // Navigation libraries
    implementation(libs.navigation.fragment)
    implementation(libs.fragment)
    implementation(libs.navigation.ui)

    // Lifecycle (LiveData and ViewModel) libraries
    runtimeOnly(libs.lifecycle.viewmodel)
    runtimeOnly(libs.lifecycle.livedata)

    // Preferences/settings components
    implementation(libs.preference)

    // Material Design components
    implementation(libs.material)

    // Apache Commons RNG Simple
    implementation(libs.commons.rng.simple)
    // Libraries for Reactive streams
    implementation(libs.rx.java)
    implementation(libs.rx.android)

    // Libraries for dependency injection
    implementation(libs.hilt.android.core)
    annotationProcessor(libs.hilt.compiler)

    // Libraries for JVM-based testing.
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)

    // Libraries for instrumented (run in Android) testing.
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.junit.android.core)
    androidTestRuntimeOnly(libs.junit.android.runner)
    androidTestImplementation(libs.junit.api)
    androidTestImplementation(libs.junit.params)
    androidTestImplementation(libs.espresso.core)

}

if (project.hasProperty("javadocDestDir")) {
    tasks.clean {
        delete.add(projectDir.toPath().resolve(project.property("javadocDestDir") as String).toFile())
    }
}

tasks.register("generateApiDoc") {
    group = "reporting"
    description = "Generates Javadoc HTML for all build variants. (Generated output for variants " +
            "after the first will overwrite the output for previous variants.)"
}

android.applicationVariants.configureEach {

    val simpleName = name
    val variantName = name.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }

    val docTitle = "${project.property("appName")} ${android.defaultConfig.versionName}"

    val task = project.tasks.create("generate${variantName}Javadoc", Javadoc::class.java) {
        title = docTitle
        group = "reporting"
        description = "Generates Javadoc for $simpleName build variant."

        source = javaCompileProvider.get().source
        exclude(
            "**/*FragmentDirections.java",
            "**/*FragmentArgs.java",
            "**/databinding/*.java",
            "**/*_*.java",
            "**/R.java",
            "**/BuildConfig*.java"
        )
        
        if (project.hasProperty("javadocDestDir")) {
            setDestinationDir(projectDir.toPath().resolve(project.property("javadocDestDir") as String).toFile())
        }
        
        doFirst {
            classpath = project.files(
                projectDir.resolve("build/intermediates/javac/$simpleName/classes"),
                javaCompileProvider.get().classpath.files,
                android.bootClasspath
            )
        }

        with(options as StandardJavadocDocletOptions) {
            windowTitle = docTitle
            memberLevel = JavadocMemberLevel.PROTECTED
            isLinkSource = true
            isAuthor = false
            links(
                "https://docs.oracle.com/en/java/javase/${libs.versions.java.get()}/docs/api/",
                "https://reactivex.io/RxJava/3.x/javadoc/",
                "https://javadoc.io/doc/com.google.dagger/dagger/${libs.versions.hilt.get()}/",
                "https://javadoc.io/doc/com.google.code.gson/gson/${libs.versions.gson.get()}/",
                "https://square.github.io/retrofit/2.x/retrofit/"
            )
            linksOffline("https://developer.android.com/reference", "$projectDir/..")
            addBooleanOption("html5", true)
            addStringOption("Xdoclint:none", "-quiet")
        }

        isFailOnError = true
    }

    task.dependsOn(tasks["assemble$variantName"])
    tasks["generateApiDoc"].dependsOn(task)
}

fun getLocalProperty(name: String): String? {
    return getProperty("$projectDir/local.properties".toString(), name)
}

fun getProperty(filename: String, name: String): String? {
    return try {
        FileInputStream(filename).use {
            val props = Properties()
            props.load(it)
            props.getProperty(name)
        }
    } catch (e: Throwable) {
        null
    }
}
