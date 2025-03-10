package com.bitwisearts.android.hybrid

import android.os.Bundle
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
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bitwisearts.android.hybridnavigation.ui.theme.HybridNavigationTheme

/**
 * The last Fragment in the navigation graph. Using the system back button will
 * return to the previous Fragment which is either [FragmentB] or
 * [FragmentCompose] which is the root of the Compose navigation graph returning
 * to the last Compose location navigated to in the compose navigation graph.
 */
class FragmentTerminal : Fragment() {
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
					Column(modifier = Modifier.padding(innerPadding)) {
						Text("Fragment Terminal", fontWeight = FontWeight.Bold)
						Spacer(modifier = Modifier.padding(12.dp))
						Text("End of the line!")

						Spacer(modifier = Modifier.padding(12.dp))

						Button(
							onClick = {
								fragmentNavController.popBackStack(R.id.FragmentA, false)
							},
							modifier = Modifier.padding(innerPadding)
						) {
							Text("Pop backstack to Fragment A")
						}
					}
				}
			}
		}
	}
}