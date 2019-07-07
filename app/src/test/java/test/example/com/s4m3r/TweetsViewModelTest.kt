package test.example.com.s4m3r

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by Samer on 7/7/2019 8:03 AM.
 */
class TweetsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tweetsViewModel: TweetsViewModel

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        tweetsViewModel = TweetsViewModel()
        tweetsViewModel.init(SCREEN_NAME)
    }


    @Test
    fun firstLoad() {
        tweetsViewModel.loadInitTweets()
        val value1 = LiveDataTestUtil.getValue(tweetsViewModel.getTweetsLiveData())
        Assert.assertEquals(30, value1.size)

        tweetsViewModel.loadInitTweets()
        val value2 = LiveDataTestUtil.getValue(tweetsViewModel.getTweetsLiveData())
        Assert.assertEquals(30, value2.size)
    }

    @Test
    fun nextLoad() {
        tweetsViewModel.loadInitTweets()
        val value = LiveDataTestUtil.getValue(tweetsViewModel.getTweetsLiveData())
        Assert.assertEquals(30, value.size)
        tweetsViewModel.loadNextTweets()
        val value1 = LiveDataTestUtil.getValue(tweetsViewModel.getTweetsLiveData())
        Assert.assertEquals(40, value1.size)
        tweetsViewModel.loadNextTweets()
        val value2 = LiveDataTestUtil.getValue(tweetsViewModel.getTweetsLiveData())
        Assert.assertEquals(50, value2.size)
    }

}
