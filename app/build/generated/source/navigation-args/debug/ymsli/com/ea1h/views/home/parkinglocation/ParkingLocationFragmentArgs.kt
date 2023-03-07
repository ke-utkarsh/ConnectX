package ymsli.com.ea1h.views.home.parkinglocation

import android.os.Bundle
import androidx.navigation.NavArgs
import kotlin.Boolean
import kotlin.jvm.JvmStatic

public data class ParkingLocationFragmentArgs(
  public val loadedInHome: Boolean = false
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putBoolean("loadedInHome", this.loadedInHome)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ParkingLocationFragmentArgs {
      bundle.setClassLoader(ParkingLocationFragmentArgs::class.java.classLoader)
      val __loadedInHome : Boolean
      if (bundle.containsKey("loadedInHome")) {
        __loadedInHome = bundle.getBoolean("loadedInHome")
      } else {
        __loadedInHome = false
      }
      return ParkingLocationFragmentArgs(__loadedInHome)
    }
  }
}
