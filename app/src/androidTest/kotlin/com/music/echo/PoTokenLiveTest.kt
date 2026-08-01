package com.music.echo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.music.echo.utils.potoken.PoTokenGenerator
import com.music.echo.utils.potoken.PoTokenWebView
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PoTokenLiveTest {
    @Test
    fun testGeneratePoToken() = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        println("STARTING POTOKEN TEST")
        try {
            val generator = PoTokenWebView.getNewPoTokenGenerator(appContext)
            val result = generator.generatePoToken("jNQXAC9IVRw")
            println("POTOKEN SUCCESS: $result")
        } catch (e: Exception) {
            println("POTOKEN FAILED: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
