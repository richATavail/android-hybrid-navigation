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
 * The root fragment class where we start
 */
class FragmentA : Fragment()
{
	private val fragmentNavController: NavController get() = findNavController()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View = ComposeView(requireContext()).apply {
		setViewCompositionStrategy(
			ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
		)
		setContent {
			HybridNavigationTheme {
				Scaffold(
					modifier = Modifier.fillMaxSize().padding(16.dp)
				) { innerPadding ->
					Column(modifier = Modifier.padding(innerPadding)) {
						Text("Fragment A", fontWeight = FontWeight.Bold)
						Spacer(modifier = Modifier.padding(12.dp))
						Button(
							onClick = {
								fragmentNavController.navigate(
									R.id.action_FragmentA_to_FragmentB)
							},
							modifier = Modifier.padding(12.dp)
						) {
							Text("Navigate to Fragment B")
						}
					}
				}
			}
		}
	}
}