package com.bitwisearts.android.hybrid

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import androidx.navigation.fragment.findNavController
import com.bitwisearts.android.hybrid.databinding.ActivityMainBinding
import androidx.navigation.NavController

/**
 * The main activity that hosts the entire fragment-based navigation graph. This
 * is a single activity application (SAA).
 *
 * It is possible to intercept the back button press event and handle it in the
 * activity. This isn't recommended, but it is done by adding an
 * [OnBackPressedCallback] to the [onBackPressedDispatcher] of the activity:
 * ```kotlin
 * // the field used to preserve the OnBackPressedCallback that has been
 * // registered
 * private lateinit var callback: OnBackPressedCallback
 *
 * // Adds the the callback to the onBackPressedDispatcher of the activity
 * private fun addOnBackPressedCallback(fragmentNavController: NavController) {
 * 	object: OnBackPressedCallback(true) {
 * 		override fun handleOnBackPressed() {
 * 			Log.d(
 * 				"MainActivity",
 * 				"Back button pressed is always handled here")
 * 			// Handle the back button event
 * 			if (supportFragmentManager.backStackEntryCount > 0) {
 * 				fragmentNavController.popBackStack()
 * 			} else {
 * 				finish()
 * 			}
 * 		}
 * 	}.apply {
 * 		callback = this
 * 		onBackPressedDispatcher
 * 			.addCallback(this@MainActivity, this)
 * 	}
 * }
 * ```
 * This callback is called when the back button is pressed and can be used to
 * handle the back button press event. This however cannot distinguish between
 * back action in the fragment navigation or the Compose navigation. It is not
 * possible to distinguish between the two navigation systems using this method
 * without external custom tracking of the navigation state of the application.
 * This would require maintaining a reference to the compose navigation
 * [NavController] that would need to be managed to avoid memory leaks.
 */
class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		// Get the xml defined fragment container in the main activity layout as
		// it is the host for the entire fragment-based navigation graph.
		supportFragmentManager.findFragmentById(
			R.id.nav_host_fragment_content_main
		)?.let {
			addDestinationChangedListener(it)
			addBackStackChangedListener(it)
		}
		setContentView(binding.root)
	}

	/**
	 * Adds a [NavController.OnDestinationChangedListener] to the [NavController]
	 * of the fragment that is the host for the entire fragment-based navigation
	 * graph. This listener is called when navigation occurs to a new [Fragment]
	 * destination in the navigation graph.
	 *
	 * This is how you can listen for navigation events in the fragment-based
	 * to perform actions when a change in destination occurs such as capturing
	 * analytics data, logging, or other actions that need to be taken when
	 * navigation occurs.
	 *
	 * It should be noted that this listener is called after the navigation but
	 * doesn't provide context for the navigation that occurred as it could be
	 * both a pop (back button press) or navigate to action. To get more context
	 * for the navigation that occurred, see [addBackStackChangedListener].
	 *
	 * @param fragmentHolder
	 *   The [Fragment] that is the host for the entire fragment-based
	 *   navigation as defined by the [R.id.nav_host_fragment_content_main].
	 */
	private fun addDestinationChangedListener(fragmentHolder: Fragment) {
		// The navigation controller belonging to the fragment is used to
		// navigate between fragments so it is used to add the destination
		// changed listener.
		fragmentHolder.findNavController()
			.addOnDestinationChangedListener { _, destination, _ ->
				Log.d("Fragment Destination", destination.label.toString())
			}
	}

	/**
	 * Add a [OnBackStackChangedListener] to the [FragmentManager] of the
	 * fragment that is the host for the entire fragment-based navigation graph.
	 * This listener is called when the back stack changes.
	 *
	 * This is how you can have more context for the navigation that occurred
	 * differentiating between a pop (back button press) or navigate to action.
	 *
	 * The caveat is that the functions that provide that contextual information,
	 * [OnBackStackChangedListener.onBackStackChangeStarted] and
	 * [OnBackStackChangedListener.onBackStackChangeCommitted], are called twice
	 * for each navigation event. The order of the calls is dependent on the
	 * navigation event.
	 *
	 * * If the event is a pop (back button press) the first call is for the
	 * fragment that is now on top of the stack and the second call is for the
	 * fragment that was popped.
	 * * If the event is a navigate to action the first call is for the fragment
	 * that is being replaced and the second call is for the fragment that is
	 * now on top of the stack.
	 *
	 * @param fragmentHolder
	 *   The [Fragment] that is the host for the entire fragment-based
	 *   navigation as defined by the [R.id.nav_host_fragment_content_main].
	 */
	private fun addBackStackChangedListener(fragmentHolder: Fragment) {
		// The main fragment container is the host for the entire fragment-based
		// navigation graph. Its FragmentManager is used to listen for
		// changes to the back stack. This is different from the
		// supportFragmentManager as it is the FragmentManager for the
		// activity, not the fragment.
		fragmentHolder.childFragmentManager.addOnBackStackChangedListener(
			object : OnBackStackChangedListener {
				private var first: Fragment? = null
				private var last: Fragment? = null
				private var popped: Boolean? = null
				override fun onBackStackChanged() {
					// called once for each Fragment transaction
					val dir =
						when (popped) {
							false -> "->"
							else -> "<-"
						}
					Log.d(
						"MainActivity",
						"Back stack changed: " +
							"${first?.let { f -> f::class.simpleName }} $dir " +
							"${last?.let { f -> f::class.simpleName }} " +
							"(popped=$popped)"
					)
					first = null
					last = null
					popped = null
				}

				override fun onBackStackChangeStarted(
					fragment: Fragment,
					pop: Boolean
				) {
					// called twice for each Fragment transaction if the
					// transaction is a pop operation (back button) it is
					// called once for the fragment that is now on top of
					// the stack and once for the pop fragment .
					// If the transaction is a push operation (navigate)
					// it is called once for the origin fragment that is
					// being replaced and once for the destination fragment
					// that is now on top of the stack.
					Log.d(
						"MainActivity",
						"Back stack started: pop=$pop (${fragment::class.simpleName})"
					)
				}

				override fun onBackStackChangeCommitted(
					fragment: Fragment,
					pop: Boolean
				) {
					// called twice for each Fragment transaction if the
					// transaction is a pop operation (back button) it is
					// called once for the fragment that is now on top of
					// the stack and once for the pop fragment .
					// If the transaction is a push operation (navigate)
					// it is called once for the origin fragment that is
					// being replaced and once for the destination fragment
					// that is now on top of the stack.
					if (first == null) {
						first = fragment
					} else {
						last = fragment
						popped = pop
					}

					Log.d(
						"MainActivity",
						"Back stack changed: pop=$pop (${fragment::class.simpleName})"
					)
				}
			}
		)
	}
}