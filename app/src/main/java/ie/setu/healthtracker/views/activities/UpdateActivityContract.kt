package ie.setu.healthtracker.views.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ie.setu.healthtracker.models.ActivityModel

class UpdateActivityContract : ActivityResultContract<ActivityModel, Boolean>() {
    override fun createIntent(context: Context, input: ActivityModel): Intent {
        return Intent(context, UpdateActivity::class.java).apply {
            putExtra("currentActivity", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
