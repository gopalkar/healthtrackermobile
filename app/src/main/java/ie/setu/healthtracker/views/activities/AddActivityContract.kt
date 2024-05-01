package ie.setu.healthtracker.views.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ie.setu.healthtracker.models.ActivityModel

class AddActivityContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, AddActivity::class.java).apply {

        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Unit {

    }
}
