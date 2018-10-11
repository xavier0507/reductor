package javatest.colorhaake.com.example3.network

import io.reactivex.Observable
import javatest.colorhaake.com.example3.model.ImageData
import javatest.colorhaake.com.example3.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Example3Service {
    @GET("api")
    fun searchData(
            @Query("key") key: String,
            @Query("q") term: String,
            @Query("page") page: Int
    ): Observable<Response<ImageData>>
}
