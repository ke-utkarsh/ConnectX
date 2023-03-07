package ymsli.ymsli.couriemate.uiautomatortests

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.views.entrance.EntranceActivity
import ymsli.com.ea1h.views.home.HomeActivity

abstract class BaseAutomatorTest {

    lateinit var TAG : String
    lateinit var context : Context
    lateinit var device: UiDevice
    lateinit var resources : Resources

    fun injectDependencies(){
        TAG = generateTag()
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        context = InstrumentationRegistry.getInstrumentation().targetContext
        resources = context.resources
    }

    /**
     * launches login activity
     */
    protected open fun launchEntranceActivity(){
        val loginActivityIntent = Intent(context, EntranceActivity::class.java)
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(loginActivityIntent)
        Thread.sleep(10000)
    }

    protected fun launchHomeActivity(){
        val homeActivityIntent = Intent(context, HomeActivity::class.java)
        homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(homeActivityIntent)
        Thread.sleep(10000)
    }

    protected abstract fun generateTag(): String
}