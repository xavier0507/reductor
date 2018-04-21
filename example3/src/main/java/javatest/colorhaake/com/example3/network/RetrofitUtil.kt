package javatest.colorhaake.com.example3.network

import javatest.colorhaake.com.example3.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitUtil {
    fun genericClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }
}
