package ie.setu.healthtracker.views.navigation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.databinding.ActivityNavDrawerBinding
import ie.setu.healthtracker.healthtrackernavigationnew.HealthTrackerNavigationNew
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import ie.setu.healthtracker.views.login.Login
import ie.setu.healthtracker.views.login.LoginScreen

class NavDrawerActivity : ComponentActivity() {

    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val firebaseHelper: FirebaseHelper = viewModel()
            val context = LocalContext.current
            HealthTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = Color(red = 0.631f, green = 0.765f, blue = 0.525f),

                    ) {
                    MaterialTheme {
                        RelayContainer {
                            HealthTrackerNavigationNew(
                                userNameTextContent = "${firebaseAuth!!.currentUser!!.email}",
                                onNavHomeTapped = {},
                                onNavActivityTapped = {},
                                onNavNutritionTapped = {},
                                onNavLogoutTapped = {
                                    firebaseHelper.loggedOut
                                    val intent = Intent(context, Login::class.java)
                                    startActivity(intent)
                                    finish()
                                                    },
                                navImage = painterResource(R.drawable.image_logo),
                                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
                            )
                        }
                    }
                    }
                }
            }
        }
}