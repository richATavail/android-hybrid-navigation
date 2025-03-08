package com.bitwisearts.android.hybrid

import android.os.Bundle
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
	 * Now the [ComposeScreen]s are navigated to as [Serializable] compose
	 * arguments. This takes the [destination], the [ComposeScreen] that targets
	 * the location in the compose navigation graph to navigate to.
	 */
	@Composable
	fun ComposeScreensNavGraph(
		startDestination: ComposeScreen,
		modifier: Modifier = Modifier
	) {
		val navController = rememberNavController()
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
					R.id.action_FragmentCompose_to_FragmentTerminal)
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Fragment Terminal")
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
				navHostController.navigate(ComposeScreen.Home("Different Name!"))
			},
			modifier = Modifier.padding(16.dp)
		) {
			Text("Navigate to Home")
		}
	}
}