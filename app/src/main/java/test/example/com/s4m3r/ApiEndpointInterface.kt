package test.example.com.s4m3r

import TweetDto
import UserDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val contType = "Content-Type: application/json"
private const val auth = AUTh // stored in a secret file that will not be push to git

interface ApiEndpointInterface {

    @Headers(contType, auth)
    @GET("1.1/statuses/user_timeline.json?include_entities=false&trim_user=false")
    fun getTweetsWithPage(@Query("screen_name") screenName: String,
                          @Query("page") page: Int,
                          @Query("count") count: Int = 10): Single<List<TweetDto>>


    @Headers(contType, auth)
    @GET("1.1/statuses/user_timeline.json?include_entities=false&trim_user=false")
    fun getTweetsWithMaxId(@Query("screen_name") screenName: String,
                           @Query("max_id") maxId: Long,
                           @Query("count") count: Int = 10): Single<List<TweetDto>>

    @Headers(contType, auth)
    @GET("1.1/users/show.json")
    fun getUserInfo(@Query("screen_name") screenName: String): Single<UserDto>



}