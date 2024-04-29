package ie.setu.healthtracker.views.maps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.healthtracker.models.Location
import timber.log.Timber

class MapsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentLocation = intent.getParcelableExtra("currentLocation") as? Location
            MapViewComponent(currentLocation!!)
        }
    }
}

@Composable
fun MapViewComponent(currentLocation: Location?) {
    val context = LocalContext.current
    var defaultLocation = Location(51.5074, -0.1278, 10f)
    if (currentLocation != null) {
        // Move camera to a default location (e.g., city center)
        defaultLocation = currentLocation
    }
    // Create MapView instance
    val mapView = rememberMapViewWithLifecycle()

    AndroidView(
        factory = { context ->
            mapView.apply {
                getMapAsync { googleMap ->
                    // Customize the map settings here
                    googleMap.uiSettings.isZoomControlsEnabled = true


                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(defaultLocation.lat, defaultLocation.lng), 10f))

                    // Add a marker at the default location
                    //googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker"))

                    googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                        override fun onMarkerDragStart(marker: Marker) {
                            // Handle marker drag start event
                        }

                        override fun onMarkerDrag(marker: Marker) {
                            // Handle marker drag event
                        }

                        override fun onMarkerDragEnd(marker: Marker) {
                            defaultLocation.lat = marker.position.latitude
                            defaultLocation.lng = marker.position.longitude
                            defaultLocation.zoom = googleMap.cameraPosition.zoom
                        }

                    })

                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(defaultLocation.lat, defaultLocation.lng))
                            .draggable(true)
                    )

                    googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                        override fun onMarkerClick(marker: Marker): Boolean {
                            val loc = LatLng(defaultLocation.lat, defaultLocation.lng)
                            marker.snippet = "GPS: $loc"
                            return true
                        }
                    })
                }
            }
        },
        update = { mapView ->
            // Update selected location when composable recomposes
            mapView.getMapAsync { googleMap ->
                googleMap.setOnCameraMoveStartedListener {
                    // Hide the soft keyboard if it's visible
                    // You can handle other camera move events here
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    BackHandler {
        val resultIntent = Intent()
        resultIntent.putExtra("location", defaultLocation)
        (context as? Activity)?.setResult(Activity.RESULT_OK, resultIntent)
        (context as? Activity)?.finish()
        Timber.i("Location MapsActivity: ${defaultLocation}")
    }

/*    googleMap.setOnMarkerClickListener { marker ->
        // Handle marker click event
        true // Return true to consume the event
    }*/
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = View.generateViewId()
        }
    }

    // Remember the MapView's lifecycle
    DisposableEffect(context) {
        mapView.onCreate(Bundle())
        onDispose {
            mapView.onDestroy()
        }
    }

    return mapView
}
