package ie.setu.healthtracker.views.activitylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
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
import ie.setu.healthtracker.R
import ie.setu.healthtracker.activitylist.ActivityList
import ie.setu.healthtracker.toolbar.ToolBar
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme

class ActivityListView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,

                ) {
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        ShowToolBar()
                        ShowActivityList()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowToolBar() {
    ToolBar(
        modifier = Modifier
            .fillMaxWidth()
            .width(360.dp)
            .height(60.dp),
        painterResource(R.drawable.tool_bar_menu),
        "Health Tracker",
        painterResource(R.drawable.tool_bar_plus),
        painterResource(R.drawable.tool_bar_maps)
    )
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