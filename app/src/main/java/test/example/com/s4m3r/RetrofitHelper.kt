package test.example.com.s4m3r

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Samer on 6/7/2019 7:26 PM.
 */
object RetrofitHelper {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://api.twitter.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    val ApiInterface: ApiEndpointInterface by lazy {
        retrofit.create(ApiEndpointInterface::class.java)
    }
}

