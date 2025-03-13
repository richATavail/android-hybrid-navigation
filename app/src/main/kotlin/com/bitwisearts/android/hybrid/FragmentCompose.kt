package com.bitwisearts.android.hybrid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.toRoute
import com.bitwisearts.android.hybridnavigation.ui.theme.HybridNavigationTheme
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * The Fragment that contains a Compose specific navigation subgraph.
 */
class FragmentCompose : Fragment() {
	/**
	 * The target [ComposeScreen] destination for the navigation action. This is
	 * passed as a [Parcelize] argument to the Fragment.
	 */
	lateinit var destination: ComposeScreen

	/**
	 * The fragment [NavController] that is used to navigate between fragments
	 * is made available to be used in the Compose UI to move from the Compose
	 * navigation graph to the Fragment navigation graph.
	 */
	private val fragmentNavController: NavController get() = findNavController()

	/**
	 * The [FragmentComposeArgs] that are passed to the Fragment that contains
	 * the [ComposeScreen] destination. This uses [Parcelize] to "serialize" the
	 * [ComposeScreen] to enable it to be passed as a Fragment argument in
	 * fragment navigation.
	 */
	val args: FragmentComposeArgs by navArgs()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		destination = args.composeTarget
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
				ComposeScreensNavGraph(destination)
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
	fun ComposeScreensNavGraph(
		startDestination: ComposeScreen,
		modifier: Modifier = Modifier
	) {
		// Create a NavHostController to manage the navigation in the Compose
		// preserving the navigation state during configuration changes, however
		// the only the state is preserved, not the actual NavHostController.
		// After reconfiguration, the NavHostController is recreated and the
		// state is restored from the Bundle where it was saved.
		val navController = rememberNavController()

		DisposableEffect(navController) {
			// This is how you can listen to destination changes in the Compose
			// navigation graph. This is useful for logging or other side effects
			// that need to be performed when the destination changes such as
			// reporting analytics events.
			val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
				Log.d("Destination", "Compose Destination: ${destination.route}")
			}
			navController.addOnDestinationChangedListener(listener)
			onDispose {
				navController.removeOnDestinationChangedListener(listener)
			}
		}
		// Intercept the back button press to handle the back press in the
		// Compose navigation graph. This is only necessary to introduce custom
		// back press handling in the Compose navigation graph. The behavior
		// of this implementation is the same as the default back press handling
		// if this BackHandler was not present.
		BackHandler {
			// Here we intercept the back button press and add custom logic to
			// handle the back press. In this case, we check if there is a back
			// stack entry in the compose navigation graph and pop it if there
			// is. If there is no back stack entry in the compose navigation
			// graph, we pop the back stack in the fragment navigation graph
			// removing this Compose fragment from the back stack. This is
			// effectively the same as the default back press behavior with only
			// the addition of the Log statement.
			Log.d("BackHandler", "Back pressed")
			if(navController.previousBackStackEntry != null) {
				// there is a back stack entry to pop in the compose nav graph
				navController.popBackStack()
			} else {
				// there is no back stack entry to pop in the compose nav graph
				// so we navigate back to the fragment nav graph and pop the
				// back stack there
				fragmentNavController.popBackStack()
			}
		}
		NavHost(
			navController = navController,
			startDestination = startDestination,
			modifier = modifier
		) {
			// Nav targets are parameterized by the ComposeScreen sealed types
			composable<ComposeScreen.Home> { backStackEntry ->
				// Kotlin serialization serializes the Home class allowing it
				// to be passed in with nav argument data
				val home: ComposeScreen.Home = backStackEntry.toRoute()
				HomeScreen(home, navController)
			}
			composable<ComposeScreen.Details> {
				DetailsScreen(fragmentNavController)
			}
			composable<ComposeScreen.Settings> {
				SettingsScreen(navController)
			}
		}
	}

	companion object {
		/**
		 * The key used to get target destination for the navigation action
		 * for the fragment navigation argument.
		 */
		const val COMPOSE_TARGET = "composeTarget"
	}
}

@Composable
fun HomeScreen(
	home: ComposeScreen.Home,
	navHostController: NavHostController
) {
	Column(modifier = Modifier.padding(16.dp)) {
		Text("Compose Home", fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.padding(12.dp))
		Text("Serialized Home.name arg: \"${home.name}\"")
		Spacer(modifier = Modifier.padding(16.dp))

		Button(
			onClick = {
				navHostController.navigate(ComposeScreen.Details)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Details")
		}
		Spacer(modifier = Modifier.padding(16.dp))
		Button(
			onClick = {
				navHostController.navigate(ComposeScreen.Settings)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Settings")
		}
	}
}

@Composable
fun DetailsScreen(
	fragNavController: NavController
) {
	Column(modifier = Modifier.padding(16.dp)) {
		Text("Compose Details Screen", fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.padding(16.dp))
		Button(
			onClick = {
				// Use the fragment navigator to return to the fragment
				// nav graph
				fragNavController.navigate(
					R.id.action_FragmentCompose_to_FragmentTerminal
				)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Fragment Terminal")
		}
		Spacer(modifier = Modifier.padding(16.dp))
		Button(
			onClick = {
				// Use the fragment navigator to return to the fragment
				// nav graph clearing the back stack of all fragments except
				// Fragment A, the entry point of the fragment nav graph.
				fragNavController.navigate(
					R.id.action_FragmentCompose_to_FragmentTerminal_Pop_Up_A
				)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Pop up to Fragment A -> Navigate to Fragment Terminal")
		}
	}
}

@Composable
fun SettingsScreen(
	navHostController: NavHostController,
) {
	Column(modifier = Modifier.padding(16.dp)) {
		Text("Compose Settings Screen", fontWeight = FontWeight.Bold)
		Spacer(modifier = Modifier.padding(16.dp))
		Button(
			onClick = {
				navHostController.navigate(ComposeScreen.Details)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Details")
		}
		Spacer(modifier = Modifier.padding(16.dp))
		Button(
			onClick = {
				// Navigate to the Home screen providing a custom name nav
				// argument
				navHostController.navigate(
					ComposeScreen.Home("Different Name!"))
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Home")
		}
	}
}