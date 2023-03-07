package ymsli.com.ea1h.views.home

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import kotlin.Boolean
import kotlin.Int
import ymsli.com.ea1h.R

public class HomeFragmentDirections private constructor() {
  private data class ActionNavHomeToParkingLocationFragment(
    public val loadedInHome: Boolean = false
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_nav_home_to_parkingLocationFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putBoolean("loadedInHome", this.loadedInHome)
      return result
    }
  }

  public companion object {
    public fun actionNavHomeToNavDrivingHistory(): NavDirections =
        ActionOnlyNavDirections(R.id.action_nav_home_to_nav_driving_history)

    public fun actionNavHomeToParkingLocationFragment(loadedInHome: Boolean = false): NavDirections
        = ActionNavHomeToParkingLocationFragment(loadedInHome)

    public fun homeToUserProfile(): NavDirections =
        ActionOnlyNavDirections(R.id.home_to_user_profile)

    public fun actionNavHomeToNavYourVehicles(): NavDirections =
        ActionOnlyNavDirections(R.id.action_nav_home_to_nav_your_vehicles)
  }
}
