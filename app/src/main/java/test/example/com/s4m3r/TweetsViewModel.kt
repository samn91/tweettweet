package test.example.com.s4m3r

import TweetDto
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3

/**
 * Created by Samer on 7/7/2019 7:18 AM.
 */

private const val TAG = "TweetsViewModel"

class TweetsViewModel : ViewModel() {

    private val tweetsLiveData = SingleLiveEvent<List<TweetDto>>()
    private val clearLiveData = SingleLiveEvent<Nothing?>()
    private val errorLiveData = SingleLiveEvent<Nothing?>()

    fun getTweetsLiveData(): LiveData<List<TweetDto>> = tweetsLiveData
    //    fun getClearLiveData(): LiveData<Nothing?> = clearLiveData
    fun getErrorLiveData(): LiveData<Nothing?> = errorLiveData

    private var minTweetId: Long? = null
    private val compositeDisposable = CompositeDisposable()
    private val tweetList = mutableListOf<TweetDto>()
    private var screenName: String = ""

    fun init(screenName: String) {
        this.screenName = screenName
    }

    private fun checkScreenNameSet() {
        if (screenName.isEmpty()) {
            throw IllegalArgumentException("screenName is not set call init before you start using this VM")
        }
    }

    fun loadInitTweets() {
        checkScreenNameSet()
        Single.zip(
                RetrofitHelper.ApiInterface.getTweetsWithPage(screenName, 1),
                RetrofitHelper.ApiInterface.getTweetsWithPage(screenName, 2),
                RetrofitHelper.ApiInterface.getTweetsWithPage(screenName, 3),
                Function3<List<TweetDto>, List<TweetDto>, List<TweetDto>, List<TweetDto>> { it1, it2, it3 ->
                    val res = mutableListOf<TweetDto>()
                    res.addAll(it1)
                    res.addAll(it2)
                    res.addAll(it3)
                    return@Function3 res
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    notifyWithNewTweets(it, true)
                }, {
                    errorLiveData.value = null
                }).disposeOnCleared()
    }

    fun loadNextTweets() {
        checkScreenNameSet()
        if (minTweetId == null) {
            //got to the end of tweets
            return
        }

        RetrofitHelper.ApiInterface.getTweetsWithMaxId(screenName, minTweetId!! - 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    notifyWithNewTweets(it, false)
                }, {
                    errorLiveData.value = null
                }).disposeOnCleared()
    }

    private fun notifyWithNewTweets(list: List<TweetDto>, clearList: Boolean) {
        if (clearList) {
            tweetList.clear()
        }
        minTweetId = list.lastOrNull()?.id
        tweetList.addAll(list)
        tweetsLiveData.value = tweetList
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }
}