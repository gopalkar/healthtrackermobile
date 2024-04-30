package ie.setu.healthtracker.views.activitylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.activitylist.ActivityList
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.toolbar.ToolBar
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import ie.setu.healthtracker.toolbar.*
import ie.setu.healthtracker.views.activities.AddActivity
import ie.setu.healthtracker.views.activities.AddActivityContract
import ie.setu.healthtracker.views.maps.MapsActivityContract
import timber.log.Timber

class ActivityListView : ComponentActivity() {

    private lateinit var activityListViewModel: ActivityListViewModel
    private lateinit var addActivityLauncher: ActivityResultLauncher<Intent>
    private val REQUEST_CODE_REFRESH = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        addActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]
                activityListViewModel.refreshData()
                recreate()
            }
        }
        activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]
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

                                    addActivityView()
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

    private fun addActivityView() {
        val intent = Intent(this, AddActivity::class.java)
        addActivityLauncher.launch(intent)
    }

    private fun navigationScreen() {
        TODO("Not yet implemented")
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_REFRESH && resultCode == Activity.RESULT_OK) {
            // Refresh the activity or update UI here
            recreate() // This will recreate the activity and refresh its state
        }
    }*/
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


@Composable
fun ShowActivityList() {
    val activityListViewModel : ActivityListViewModel = viewModel()
    val activityList by activityListViewModel.activitiesList.observeAsState()
    if (activityList != null ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Timber.i("ActivityList: $activityList")
            activityList!!.forEach { activity ->
                ShowEachActivity(activity)
            }
        }
    }
}

@Composable
fun ShowEachActivity(activity: ActivityModel) {
        ActivityList(
            Modifier
                .fillMaxWidth()
                .width(360.dp)
                .height(115.dp),
            "${activity.calories} Calories", "${activity.duration} Mins", "${activity.activityTime}",
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