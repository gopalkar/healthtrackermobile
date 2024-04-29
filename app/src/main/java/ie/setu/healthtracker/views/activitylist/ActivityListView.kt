package ie.setu.healthtracker.views.activitylist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.activitylist.ActivityList
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.toolbar.ToolBar
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import ie.setu.healthtracker.toolbar.*
import ie.setu.healthtracker.views.activities.AddActivity
import timber.log.Timber

class ActivityListView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContent {
            HealthTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,

                ) {
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        ShowToolBar() {menu ->
                            when (menu) {
                                "Menu" -> {
                                    navigationScreen()
                                }
                                "Add" -> {
                                    addActivity()
                                }
                                "Maps" -> {
                                    mapScreen()
                                }
                            }
                        }
                        ShowActivityList()
                    }
                }
            }
        }
    }

    private fun mapScreen() {
        TODO("Not yet implemented")
    }

    private fun addActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigationScreen() {
        TODO("Not yet implemented")
    }
}


@Composable
fun ShowToolBar(onTapped : (String) -> Unit) {
    MaterialTheme {
        RelayContainer {
            ToolBar(
                menu = painterResource(R.drawable.tool_bar_menu),
                onMenuTapped = {
                    Timber.i("Menu Clicked")
                    onTapped("Menu")
                               },
                appTitle = "Health Tracker",
                addActivity = painterResource(R.drawable.tool_bar_plus),
                onPlusTapped = {
                    Timber.i("Add Clicked")
                    onTapped("Add")
                               },
                activityMaps = painterResource(R.drawable.tool_bar_maps),
                onMapsTapped = {
                    Timber.i("Maps Clicked")
                    onTapped("Maps")
                               },
                modifier = Modifier.rowWeight(1.0f)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ShowActivityList() {
    ActivityList(
        Modifier
            .fillMaxWidth()
            .width(360.dp)
            .height(115.dp),
        "100 Calories","60Mins", "10-Apr-2024 8:30AM",
        //R.

    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthTrackerTheme {
        Greeting("Android")
    }
}