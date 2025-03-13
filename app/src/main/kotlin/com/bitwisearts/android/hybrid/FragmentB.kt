package com.bitwisearts.android.hybrid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bitwisearts.android.hybridnavigation.ui.theme.HybridNavigationTheme

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentB : Fragment() {
	/**
	 * The fragment [NavController] that is used to navigate between fragments
	 * is made available to be used in the Compose UI to move from the Compose
	 * navigation graph to the Fragment navigation graph.
	 */
	private val fragmentNavController: NavController get() = findNavController()

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
				Scaffold(
					modifier = Modifier.fillMaxSize().padding(16.dp)
				) { innerPadding ->
					Column {
						Text("Fragment B", fontWeight = FontWeight.Bold)
						Spacer(modifier = Modifier.padding(12.dp))
						Button(
							onClick = {
								fragmentNavController.navigate(
									R.id.action_FragmentB_to_FragmentCompose,
									Bundle().apply {
										putParcelable(
											FragmentCompose.COMPOSE_TARGET,
											ComposeScreen.Home("Home Name!"))
									})
							},
							modifier = Modifier.padding(innerPadding)
						) {
							Text("Navigate to Home")
						}

						Spacer(modifier = Modifier.padding(12.dp))

						Button(
							onClick = {
								fragmentNavController.navigate(
									R.id.action_FragmentB_to_FragmentCompose,
									Bundle().apply {
										putParcelable(
											FragmentCompose.COMPOSE_TARGET,
											ComposeScreen.Settings)
									})
							},
							modifier = Modifier.padding(innerPadding)
						) {
							Text("Navigate to Settings")
						}

						Spacer(modifier = Modifier.padding(12.dp))

						Button(
							onClick = {
								fragmentNavController.navigate(
									R.id.action_FragmentB_to_FragmentTerminal)
							},
							modifier = Modifier.padding(innerPadding)
						) {
							Text("Navigate to Terminal Fragment")
						}
						Spacer(modifier = Modifier.padding(12.dp))

						// Navigates to the Fragment Other Compose nav graph
						Button(
							onClick = {
								fragmentNavController.navigate(
									R.id.action_FragmentB_to_FragmentOtherCompose,
									Bundle().apply {
										putParcelable(
											FragmentOtherCompose.OTHER_COMPOSE_TARGET,
											OtherComposeScreen.LandingPage)
									})
							},
							modifier = Modifier.padding(innerPadding)
						) {
							Text("Navigate to Landing Page")
						}
					}
				}
			}
		}
	}
}