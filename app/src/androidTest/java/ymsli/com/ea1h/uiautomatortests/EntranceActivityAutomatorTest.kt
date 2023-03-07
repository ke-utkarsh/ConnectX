package ymsli.com.ea1h.uiautomatortests

import android.Manifest
import android.os.SystemClock
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.ymsli.couriemate.uiautomatortests.BaseAutomatorTest


@RunWith(AndroidJUnit4::class)
@LargeTest
class EntranceActivityAutomatorTest: BaseAutomatorTest() {

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun validateLogin_IncorrectUsername(){
        launchEntranceActivity()

        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        //add here your other permissions

        //add here your other permissions
        for (i in 0 until permissions.size) {
            val command = java.lang.String.format(
                "pm grant %s %s",
                context.packageName,
                permissions[i]
            )
            getInstrumentation().getUiAutomation().executeShellCommand(command)
            // wait a bit until the command is finished
            SystemClock.sleep(1000)
        }
        //sign in
        device.findObject(By.res("ymsli.com.ea1h:id/tv_sign_in_email")).text = "fajedo1114@fw025.com"
        device.findObject(By.res("ymsli.com.ea1h:id/tv_password")).text = "ymslilab111"
        device.findObject(By.res("ymsli.com.ea1h:id/btn_sign_in")).click()
        Thread.sleep(20000)
        device.findObject(By.res("ymsli.com.ea1h:id/driving_history_icon")).click()
        Thread.sleep(1500)
        device.findObject(By.res("ymsli.com.ea1h:id/iv_filter")).click()
        Thread.sleep(2000)
    }

/*    *//**
     * used to enter username and password in login activity
     *//*
    private fun setUsernamePassword(username: String, password: String){
        device.findObject(By.res("ymsli.com.ea1h:id/tv_sign_in_email")).text = username
        device.findObject(By.res("ymsli.com.ea1h:id/tv_password")).text = password
        device.findObject(By.res("ymsli.com.ea1h:id/btn_sign_in")).click()
        Thread.sleep(4000)
    }

    *//**
     * verifies the user is logged in
     *//*
    private fun verifyLoginSuccess(){
        val uiObject : UiObject = (device.findObject(UiSelector().resourceId("")))
        assert(!uiObject.exists())
    }

    *//**
     * verifies that user can't login
     *//*
    private fun verifyLoginFailure(){
        val uiObject : UiObject = (device.findObject(UiSelector().resourceId("ymsli.com.ea1h:id/toolbar")))
        assert(!uiObject.exists())
    }*/
}