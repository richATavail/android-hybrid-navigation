package com.bitwisearts.android.hybrid

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Represents the screens that can be navigated to in the Compose navigation
 * graph.
 *
 * This is a [Parcelize] to enable it to be passed as a Fragment nav argument.
 * This is a [Serializable] to enable it to be passed as a Compose nav argument.
 */
@Serializable
@Parcelize
sealed class ComposeScreen: Parcelable {
	/**
	 * Represents the Home composable screen which includes a data parameter.
	 */
	@Serializable
	data class Home(val name: String) : ComposeScreen()

	@Serializable
	data object Details : ComposeScreen()

	@Serializable
	data object Settings : ComposeScreen()
}