package ie.setu.healthtracker.views.activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.toolbaraddactivity.ToolBarAddActivity
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import timber.log.Timber
import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TimeInput
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.models.EditViewModel
import ie.setu.healthtracker.models.Location
import ie.setu.healthtracker.views.activitylist.ActivityListViewModel
import ie.setu.healthtracker.views.maps.MapsActivityContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class UpdateActivity : AppCompatActivity() {

    var activityViewModel = AddActivityViewModel()
    private lateinit var activityListViewModel: ActivityListViewModel
    var activityModel = ActivityModel()
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        val currentActivity = intent.getParcelableExtra("currentActivity") as? ActivityModel
        setContent {
            HealthTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background,
                    color = Color(0xFFA1C386)
                ) {
                    ShowUpdateActivity(currentActivity!!)
                }
            }
        }
    }

    data class FormData(
        val activityName: String,
        val activityDuration: Int,
        val activityCalories: Int
    )

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ShowUpdateActivity(currentActivity: ActivityModel?) {

        val activityListViewModel : ActivityListViewModel = viewModel()
        val activityList by activityListViewModel.activitiesList.observeAsState()

        var activityDuration by remember { mutableStateOf("") }
        var activityCalories by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        var expanded by remember { mutableStateOf(false) }
        var activityName by remember { mutableStateOf("Select Activity Type") }
        val items = listOf("Running", "Walking", "Swimming", "Cycling")

        var activityLocation by remember { mutableStateOf(Location(38.8,-94.79, 17f)) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        var selectionMade by remember { mutableStateOf(true) }

        var durationError by remember { mutableStateOf("") }
        var caloriesError by remember { mutableStateOf("") }

        var updateCurrentActivity by remember { mutableStateOf(currentActivity)  }

        val mapsActivityLauncher = rememberLauncherForActivityResult(MapsActivityContract()) {
            location ->
            run {
                updateCurrentActivity!!.lat = location!!.lat
                updateCurrentActivity!!.lng = location.lng
                updateCurrentActivity!!.zoom = location.zoom
            }
            }

        activityModel = updateCurrentActivity!!

        Timber.i("Location Update Activity: $activityLocation")
        var refreshState by remember { mutableStateOf(false) }
        val imageLoader =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
            { result ->
                  if (result != null) {
                            Timber.i("Got Result ${result}")
                      updateCurrentActivity!!.image = result.toString()
                      refreshState = true
                  }
            }

        LaunchedEffect(refreshState) {
            Timber.i("Refresh Triggered")
            delay(1000)
            refreshState = false
        }


        MaterialTheme {
            RelayContainer {
                ToolBarAddActivity(
                    onGoBackTapped = {
                        setResult(Activity.RESULT_OK, Intent().apply {})
                        finish()
                    },
                    appTitleTextContent = "Health Tracker",
                    onOkTapped = {
                        durationError = if (updateCurrentActivity!!.duration!!.isEmpty() || updateCurrentActivity!!.duration!!.toIntOrNull() !in 5..180) "Duration is required and (5 - 180 mins)" else ""
                        caloriesError = if (updateCurrentActivity!!.calories!!.isEmpty() || updateCurrentActivity!!.calories!!.toIntOrNull() !in 100..1000) "Calories is required and (100 - 1000 mins)" else ""
                        errorMessage = ""
                        if (!selectionMade) {
                                errorMessage = "Please select valid Activity Name"
                        } else if (durationError.isNotEmpty()) {
                                errorMessage = durationError
                        } else if (caloriesError.isNotEmpty()) {
                                errorMessage = caloriesError
                        } else if (errorMessage!!.isEmpty()) {
                             if (updateActivity(activityModel) == "Failed") {
                                 errorMessage = "Add New Activity Failed, Check logs"
                            } else {
                                 setResult(Activity.RESULT_OK, Intent())
                                 finish()
                            }
                        }
                        Timber.i("ok tapped")
                    },
                    onCancelTapped = {
                        updateCurrentActivity!!.activityName = "Select Activity Type"
                        updateCurrentActivity!!.duration = ""
                        updateCurrentActivity!!.calories = ""
                        updateCurrentActivity!!.image = Uri.EMPTY.toString()
                        updateCurrentActivity!!.lat = 38.8
                        updateCurrentActivity!!.lng = -94.79
                        updateCurrentActivity!!.zoom = 17f
                        refreshState = true
                    },
                    goBackIconImageContent = painterResource(R.drawable.tool_bar_add_activity_go_back_icon),
                    okIconImageContent = painterResource(R.drawable.tool_bar_add_activity_ok_icon),
                    cancelIconImageContent = painterResource(R.drawable.tool_bar_add_activity_cancel_icon),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(16.dp, 30.dp)
                        )
                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(4.dp, 30.dp)
                .background(Color(0x33A1C386)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
            )
            {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        /*.boxAlign(
                            alignment = Alignment.TopStart,
                            offset = DpOffset(
                                x = 30.0.dp,
                                y = 100.0.dp
                            )
                        )*/
                        .fillMaxWidth()
                        .width(300.dp)
                        .height(60.dp)
                ) {
                    Text(updateCurrentActivity!!.activityName!!)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null // decorative element
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                updateCurrentActivity!!.activityName = item
                                expanded = false
                                selectionMade = true
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = updateCurrentActivity!!.duration!!,
                onValueChange = { updateCurrentActivity = updateCurrentActivity!!.copy(duration = it) },
                label = { Text("Duration(Mins)") },
                isError = durationError.isNotEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        // Move focus to the password field
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = updateCurrentActivity!!.calories!!,
                onValueChange = { updateCurrentActivity = updateCurrentActivity!!.copy(calories = it) },
                label = { Text("Calories") },
                isError = caloriesError.isNotEmpty(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        // Move focus to the password field
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box (modifier = Modifier
                .size(350.dp, 170.dp)
                .fillMaxSize(),
                contentAlignment = Alignment.Center
                ) {
                Uri.parse(updateCurrentActivity!!.image).let { uri ->
                    if (uri != Uri.EMPTY) {

                      val painter:Painter =  rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = uri)
                                .crossfade(true)
                                .apply(block = fun ImageRequest.Builder.() {
                                    scale(Scale.FIT)
                                }).build()
                      )
                       Image(
                           painter = painter,
                           contentDescription = null,
                           contentScale = ContentScale.Fit
                       )
                    } else {
                        // Show a placeholder image if the Uri is null or empty
                        when (updateCurrentActivity!!.activityName) {
                            // Show a placeholder image if the Uri is null or empty
                            "Running" -> Image(
                                painter = painterResource(id = R.drawable.baseline_directions_run_24),
                                contentDescription = "Placeholder Image"
                            )
                            "Walking" -> Image(
                                painter = painterResource(id = R.drawable.baseline_directions_walk_24),
                                contentDescription = "Placeholder Image"
                            )
                            "Cycling" -> Image(
                                painter = painterResource(id = R.drawable.baseline_directions_bike_24),
                                contentDescription = "Placeholder Image"
                            )
                            "Swimming" -> Image(
                                painter = painterResource(id = R.drawable.vecteezy_happy_family_vacation_on_pool_vector_illustration_338574),
                                contentDescription = "Placeholder Image"
                            )
                            else ->
                                Image(
                                    painter = painterResource(R.drawable.image_and_location_image_picker),
                                    contentDescription = "Placeholder Image"
                                )
                        }
                    }
                }
            }

            //LoadImageFromUrl(Uri.parse(updateCurrentActivity!!.image))


            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        imageLoader.launch("image/*")
                    },
                    modifier = Modifier.width(131.dp)
                ) {
                    Text("Image")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        mapsActivityLauncher.launch(Location(updateCurrentActivity!!.lat, updateCurrentActivity!!.lng, updateCurrentActivity!!.zoom))
                    },
                    modifier = Modifier.width(131.dp)
                ) {
                    Text("Location")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

    private @Composable
    fun LoadImageFromUrl(contentUri: Uri) {
        var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        var loading by remember { mutableStateOf(true) }
        val context = LocalContext.current
        LaunchedEffect(contentUri) {
            try {
                val context = context
                val inputStream = context.contentResolver.openInputStream(contentUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageBitmap = bitmap.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loading = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()
            .size(350.dp, 170.dp)
            .fillMaxSize()) {
            if (loading) {
                // Show loading indicator or placeholder while the image is loading
            } else {
                imageBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    fun updateActivity(activity: ActivityModel) : String {
    var addNewActivityResult = ""
    try {
        activityViewModel.updateUserActivity(
            firebaseAuth!!.currentUser!!.uid, activity.uid!!, activity
        )
/*        activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]
        activityListViewModel.refreshData()*/
    }catch (e: Exception) {
        addNewActivityResult = "Failed"
    }
    return addNewActivityResult
}

fun getCurrentTime(): String {
        val currentDateTime = LocalDateTime.now() // Get the current date and time
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Define the desired date-time format
        return currentDateTime.format(formatter) // Format the current date-time as per the formatter
    }

}





