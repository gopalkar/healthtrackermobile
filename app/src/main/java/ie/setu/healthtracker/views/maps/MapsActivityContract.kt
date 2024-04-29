package ie.setu.healthtracker.views.maps

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ie.setu.healthtracker.models.Location

class MapsActivityContract : ActivityResultContract<Location, Location?>() {
    override fun createIntent(context: Context, input: Location): Intent {
        return Intent(context, MapsActivity::class.java).apply {
            putExtra("currentLocation", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Location? {
        return intent!!.getParcelableExtra("location")
    }
}
