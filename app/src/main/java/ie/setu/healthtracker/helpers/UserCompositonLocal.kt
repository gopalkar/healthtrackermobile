package ie.setu.healthtracker.helpers

import androidx.compose.runtime.compositionLocalOf
import com.google.firebase.auth.FirebaseUser

// Define a composition local for the logged-in user
val LocalLoggedInUser = compositionLocalOf<FirebaseUser?> { null }
