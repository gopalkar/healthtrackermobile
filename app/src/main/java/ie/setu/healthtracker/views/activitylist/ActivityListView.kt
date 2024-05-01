package ie.setu.healthtracker.views.activitylist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.relay.compose.EmptyPainter
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.activitylist.ActivityList
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.toolbar.ToolBar
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import ie.setu.healthtracker.views.activities.AddActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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
            modifier = Modifier
                .fillMaxSize()
                //.padding(16.dp)
                .offset(4.dp, 10.dp)
                .background(Color(0x33A1C386))
        ) {
            activityList!!.forEach { activity ->
                ShowEachActivity(activity)
            }
        }

    }
}

@Composable
fun ShowEachActivity(activity: ActivityModel) {
    Timber.i("Image String URL: ${activity.image}")
    val imageUri : Uri = Uri.parse(activity.image)
    var imagePainter: Painter = EmptyPainter()
    if (imageUri == Uri.EMPTY) {
        imagePainter = painterResource(id = R.drawable.baseline_directions_run_24)
    } else {
        imagePainter = rememberImagePainter(
            data = imageUri,
            builder = {
                scale(Scale.FIT)
            }
        )
    }
        ActivityList(
            Modifier
                .fillMaxWidth()
                .width(360.dp)
                .height(115.dp),
            "${activity.calories} Calories",
            "${activity.duration} Mins",
            "${activity.activityTime}",
            imagePainter,
            //painterResource(R.drawable.baseline_directions_bike_24),
            onActivityListTapped = {}
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