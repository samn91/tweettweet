package test.example.com.s4m3r

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function4
import test.example.com.s4m3r.dto.TweetDto
import test.example.com.s4m3r.dto.UserDto

/**
 * Created by Samer on 7/7/2019 7:18 AM.
 */

class TweetsViewModel : ViewModel() {
    companion object {
        private const val TAG = "TweetsViewModel"

        // The minimum number of items to have below your current scroll position
        // before loading more.
        private const val THREASHILD = 5
    }

    private val tweetsLiveData = SingleLiveEvent<Pair<UserDto, List<TweetDto>>>()
    private val clearLiveData = SingleLiveEvent<Nothing?>()
    private val errorLiveData = SingleLiveEvent<Nothing?>()

    fun getTweetsLiveData(): LiveData<Pair<UserDto, List<TweetDto>>> = tweetsLiveData

    //    fun getClearLiveData(): LiveData<Nothing?> = clearLiveData
    fun getErrorLiveData(): LiveData<Nothing?> = errorLiveData

    private var minTweetId: Long? = null
    private val compositeDisposable = CompositeDisposable()
    private val tweetList = mutableListOf<TweetDto>()
    private var screenName: String = ""

    private var userDto: UserDto? = null

    private var isFetchinNext = false

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
                RetrofitHelper.ApiInterface.getUserInfo(SCREEN_NAME),
                RetrofitHelper.ApiInterface.getTweetsWithPage(screenName, 1),
                RetrofitHelper.ApiInterface.getTweetsWithPage(screenName, 2),
                RetrofitHelper.ApiInterface.getTweetsWithPage(screenName, 3),
                Function4<UserDto, List<TweetDto>, List<TweetDto>, List<TweetDto>, List<TweetDto>> { user, it1, it2, it3 ->
                    userDto = user
                    val res = mutableListOf<TweetDto>()
                    res.addAll(it1)
                    res.addAll(it2)
                    res.addAll(it3)
                    return@Function4 res
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    notifyWithNewTweets(it, true)
                }, {
                    errorLiveData.value = null
                }).disposeOnCleared()
    }

    fun loadNextTweets() {
        checkScreenNameSet()
        if (isFetchinNext) {
            return
        }
        if (minTweetId == null) {
            //got to the end of tweets
            return
        }

        isFetchinNext = true
        RetrofitHelper.ApiInterface.getTweetsWithMaxId(screenName, minTweetId!! - 1)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    isFetchinNext = false
                }
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
        tweetsLiveData.value = Pair(userDto!!, tweetList.toList())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    fun listScrolled(childCount: Int, lastVisibleItem: Int, itemCount: Int) {
        if (childCount + lastVisibleItem + THREASHILD >= itemCount) {
            loadNextTweets()
        }
    }

    fun loadSavedTweets() {
        val userDtoCopy = userDto
        if (userDtoCopy == null || tweetList.isNullOrEmpty()) {
            loadInitTweets()
        } else {
            tweetsLiveData.value = Pair(userDtoCopy, tweetList.toList())
        }
    }
}