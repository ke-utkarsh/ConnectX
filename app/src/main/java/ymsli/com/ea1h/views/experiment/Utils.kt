package ymsli.com.ea1h.views.experiment

import android.content.Context
import androidx.core.content.edit
import ymsli.com.ea1h.R


/**
 * Provides access to SharedPreferences for location to Activities and Services.
 */
internal object SharedPreferenceUtil {

    const val KEY_FOREGROUND_ONLY_ENABLED = "tracking_foreground_only_location"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            context.getString(R.string.project_id), Context.MODE_PRIVATE)
            .getBoolean(KEY_FOREGROUND_ONLY_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            context.getString(R.string.project_id),
            Context.MODE_PRIVATE).edit {
                putBoolean(KEY_FOREGROUND_ONLY_ENABLED, requestingLocationUpdates)
            }
}
