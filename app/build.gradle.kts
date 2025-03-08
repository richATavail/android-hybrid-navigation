plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	// Safe Args plugin and Parcelize plugin used to generate the Safe Args
	// classes and Parcelize classes respectively to pass data between
	// fragments in the fragment navigation graph.
	alias(libs.plugins.androidx.navigation.safe.args)
	alias(libs.plugins.kotlin.parcelize)
	// Serialization plugin used to enable the serialization of the ComposeScreen
	// sealed class to be passed as a Compose argument in the Compose nav graph.
	kotlin("plugin.serialization") version "1.9.22"
}

android {
	namespace = "com.bitwisearts.android.hybrid"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.bitwisearts.android.hybrid"
		minSdk = 31
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
		viewBinding = true
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.constraintlayout)
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)
	implementation(libs.androidx.ui.android)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.compose.navigation)
	implementation(libs.kotlinx.serialization.json)
}