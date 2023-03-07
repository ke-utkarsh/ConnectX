package ymsli.com.ea1h

import okhttp3.*
import org.junit.Test
import ymsli.com.ea1h.utils.common.Utils
import java.io.File
import java.io.IOException

class Temp {

    @Test
    fun manipulateTime() {
        val tim = 1601437447095L
        val display = Utils.getTripDate(tim)
    }
}