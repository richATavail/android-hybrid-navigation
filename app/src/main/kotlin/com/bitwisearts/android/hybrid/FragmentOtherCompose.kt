package com.bitwisearts.android.hybrid

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.navArgs
import com.bitwisearts.android.hybridnavigation.ui.theme.HybridNavigationTheme
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * The [Fragment] that contains a Compose specific navigation subgraph. This
 * is present to demonstrate how this Compose UI is independent of the Compose
 * UI in [FragmentCompose].
 */
class FragmentOtherCompose : Fragment() {
	/**
	 * The target [ComposeScreen] destination for the navigation action. This is
	 * passed as a [Parcelize] argument to the Fragment.
	 */
	lateinit var destination: OtherComposeScreen

	/**
	 * The [FragmentOtherComposeArgs] that are passed to the Fragment that
	 * contains the [OtherComposeScreen] destination. This uses [Parcelize] to
	 * "serialize" the [OtherComposeScreen] to enable it to be passed as a
	 * Fragment argument in fragment navigation.
	 */
	val args: FragmentOtherComposeArgs by navArgs()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		destination = args.otherComposeTarget
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View = ComposeView(requireContext()).apply {
		// Dispose of the Composition when the view's LifecycleOwner
		// is destroyed
		setViewCompositionStrategy(
			ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
		)
		setContent {
			HybridNavigationTheme {
				OtherComposeScreensNavGraph(destination)
			}
		}
	}

	/**
	 * Defines this Compose UI's navigation graph that lives within the Fragment
	 * as its lifecycle owner. This Compose UI is independent of any other
	 * Compose UI in the app defined in other Fragments or Activities.
	 *
	 * The [ComposeScreen]s are navigated to as [Serializable] compose arguments.
	 * This takes the [destination], the [ComposeScreen] that targets the
	 * location in the compose navigation graph to navigate to.
	 *
	 * @param startDestination
	 *   The starting destination of the Compose navigation represented by the
	 *   [ComposeScreen] sealed class.
	 * @param modifier
	 *   The [Modifier] that is applied to the [NavHost] that contains the
	 *   Compose navigation graph.
	 */
	@Composable
	fun OtherComposeScreensNavGraph(
		startDestination: OtherComposeScreen,
		modifier: Modifier = Modifier
	) {
		// Create a NavHostController to manage the navigation in the Compose
		// preserving the navigation state during configuration changes, however
		// the only the state is preserved, not the actual NavHostController.
		// After reconfiguration, the NavHostController is recreated and the
		// state is restored from the Bundle where it was saved.
		val navController = rememberNavController()

		NavHost(
			navController = navController,
			startDestination = startDestination,
			modifier = modifier
		) {
			// Nav targets are parameterized by the OtherComposeScreen sealed
			// types
			composable<OtherComposeScreen.LandingPage> {
				LandingPage(navController)
			}
			composable<OtherComposeScreen.SomePage> {
				SomePage()
			}
		}
	}

	companion object {
		/**
		 * The key used to get target destination for the navigation action
		 * for the fragment navigation argument.
		 */
		const val OTHER_COMPOSE_TARGET = "otherComposeTarget"
	}
}

@Composable
fun LandingPage(
	navHostController: NavHostController
) {
	Column(modifier = Modifier.padding(16.dp)) {
		Text("Compose Landing Page", fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.padding(16.dp))

		Button(
			onClick = {
				navHostController.navigate(OtherComposeScreen.SomePage)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Some Page")
		}
	}
}

@Composable
fun SomePage() {
	Column(modifier = Modifier.padding(16.dp)) {
		Text("Compose Some Page", fontWeight = FontWeight.Bold)
	}
}

/**
 * Represents the screens that can be navigated to in the Other Compose
 * navigation graph.
 *
 * This is a [Parcelize] to enable it to be passed as a Fragment nav argument.
 * This is a [Serializable] to enable it to be passed as a Compose nav argument.
 */
@Serializable
@Parcelize
sealed class OtherComposeScreen: Parcelable {
	/**
	 * Represents the Home composable screen which includes a data parameter.
	 */
	@Serializable
	data object LandingPage : OtherComposeScreen()

	@Serializable
	data object SomePage : OtherComposeScreen()
}