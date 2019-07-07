package test.example.com.s4m3r

import androidx.lifecycle.LiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataTestUtil {

    /**
     * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
     * Once we got a notification via onChanged, we stop observing.
     */


    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        liveData.observeForever { o ->
            data = o
            latch.countDown()
        }
        latch.await(2, TimeUnit.SECONDS)
        return data!!
    }

}
