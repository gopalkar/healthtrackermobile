package ie.setu.healthtracker.views.activitylist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.SwipeDirection
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.firebase.auth.FirebaseAuth
import com.google.relay.compose.EmptyPainter
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.activitylist.ActivityList
import ie.setu.healthtracker.group6.Group6
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.toolbar.ToolBar
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import ie.setu.healthtracker.views.activities.AddActivity
import ie.setu.healthtracker.views.activities.UpdateActivityContract
import ie.setu.healthtracker.views.maps.MapsActivityContract
import ie.setu.healthtracker.views.navigation.NavDrawerActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import timber.log.Timber


class ActivityListView : ComponentActivity() {

    private lateinit var activityListViewModel: ActivityListViewModel
    private lateinit var addActivityLauncher: ActivityResultLauncher<Intent>
    private val REQUEST_CODE_REFRESH = 1
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        addActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]
                activityListViewModel.refreshData()
                //recreate()
            }
        }

        activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]
        activityListViewModel.refreshData()
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
            val intent = Intent(this, NavDrawerActivity::class.java)
            startActivity(intent)
            finish()
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_REFRESH && resultCode == Activity.RESULT_OK) {
            // Refresh the activity or update UI here
            recreate() // This will recreate the activity and refresh its state
        }
    }*/




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
    //val context = LocalContext.current
    //val activityListViewModel : ActivityListViewModel = ViewModelProvider(LocalViewModelStoreOwner.current!!)[ActivityListViewModel::class.java]
    val activityList by activityListViewModel.activitiesList.observeAsState()
    var refreshState by remember { mutableStateOf(false) }

    val updateActivityLauncher = rememberLauncherForActivityResult(UpdateActivityContract()
    ) {result  ->
        if (result) {
            activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]
            activityListViewModel.refreshData()
            recreate()
        }
    }


    if (activityList != null ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(16.dp)
                .offset(4.dp, 10.dp)
                .background(Color(0x33A1C386)),
        ) {
            activityList!!.forEach { activity ->
                ShowEachActivity(activity,
                    onDelete = {
                        val firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
                        activityListViewModel.delete(firebaseAuth!!.currentUser!!.uid, it.uid!!)
                        activityListViewModel.refreshData()
                    },
                    onClick = {
                        updateActivityLauncher.launch(it)
                    })
                }
            refreshState = true

            }
        }

        LaunchedEffect(refreshState) {
            Timber.i("Refresh Triggered")
            delay(1000)
            refreshState = false
        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowEachActivity(activity: ActivityModel, onDelete: (ActivityModel) -> Unit, onClick: (ActivityModel) -> Unit) {
    Timber.i("Image String URL: ${activity.image}")
    var dismissState = rememberDismissState()
    val imageUri : Uri = Uri.parse(activity.image)
    var imagePainter: Painter = EmptyPainter()

    if (imageUri == Uri.EMPTY) {
        when (activity.activityName) {
            // Show a placeholder image if the Uri is null or empty
            "Running" -> imagePainter = painterResource(id = R.drawable.baseline_directions_run_24)
            "Walking" -> imagePainter = painterResource(id = R.drawable.baseline_directions_walk_24)
            "Cycling" -> imagePainter = painterResource(id = R.drawable.baseline_directions_bike_24)
            "Swimming" -> imagePainter = painterResource(id = R.drawable.vecteezy_happy_family_vacation_on_pool_vector_illustration_338574)
        }

    } else {
        imagePainter = rememberImagePainter(
            data = imageUri,
            builder = {
                scale(Scale.FIT)
            }
        )
    }
    var shouldDelete by remember {
        mutableStateOf(false)
    }
    var shouldCancel by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        positionalThreshold = {
            600f // Derived thru trial and error
        })

    // Triggers the command to delete the item
    LaunchedEffect(key1 = shouldDelete) {
        if(shouldDelete) {
            delay(500)
            state.reset()
            onDelete(activity)
            shouldDelete = false
        }
    }

    // Triggers the command to cancel the delete
    LaunchedEffect(key1 = shouldCancel) {
        if(shouldCancel) {
            state.reset()
            shouldCancel = false
        }
    }

    SwipeToDismiss(
        state = state,
        background = {
            DeleteBackground(
                swipeDismissState = state,
                onDelete = {
                    shouldDelete = true
                },
                onCancel = {
                    shouldCancel = true
                }
            )
        },
        dismissContent = {
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
                onActivityListTapped = {
                   onClick(activity)
                }
            )
        },
        directions = setOf(DismissDirection.EndToStart)
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState,
    onDelete: () -> Unit = { },
    onCancel: () -> Unit = { }
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else
        Color(0x33A1C386)
    if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row {
            Text(
                "Delete this activity?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onError
            )
            Spacer(modifier = Modifier.width(20.dp))

            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.clickable {
                    onCancel()
                }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.clickable {
                    onDelete()
                }
            )
        }
        }
    }
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
}