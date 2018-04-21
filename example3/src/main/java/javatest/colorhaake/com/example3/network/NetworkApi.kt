package javatest.colorhaake.com.example3.network

import javatest.colorhaake.com.example3.Config

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javatest.colorhaake.com.example3.model.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import javatest.colorhaake.com.example3.Config.API_KEY
import javatest.colorhaake.com.example3.model.ImageData

object NetworkApi {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(RetrofitUtil.genericClient())
                .baseUrl(Config.BASE_API_URL)
                .build()
    }

    private val service: Example3Service by lazy {
        retrofit.create(Example3Service::class.java)
    }

    fun searchData(term: String, page: Int): Observable<Response<ImageData>> {
        return service.searchData(API_KEY, term, page)
    }
}
