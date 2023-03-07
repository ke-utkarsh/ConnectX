package ymsli.com.ea1h.uiautomatortests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ymsli.ymsli.couriemate.uiautomatortests.BaseAutomatorTest

@RunWith(AndroidJUnit4::class)
@LargeTest
class BluetoothFeaturesUIAutomatorTest: BaseAutomatorTest() {

    override fun generateTag(): String = javaClass.simpleName

    @Before
    fun initializeTest() = injectDependencies()

    @Test
    fun validateBluetoothECUFeatures(){
        launchHomeActivity()
        device.findObject(By.res("btn_answer_back")).click()

        Thread.sleep(3000)
        device.findObject(By.res("ymsli.com.ea1h:id/btn_hazard")).click()
        Thread.sleep(5000)
        device.findObject(By.res("ymsli.com.ea1h:id/btn_hazard")).click()
        Thread.sleep(3000)
        device.findObject(By.res("ymsli.com.ea1h:id/btn_locate_bike")).click()
        Thread.sleep(13000)
    }

}