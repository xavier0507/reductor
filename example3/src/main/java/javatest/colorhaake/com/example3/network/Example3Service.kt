package javatest.colorhaake.com.example3.network

import io.reactivex.Observable
import javatest.colorhaake.com.example3.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Example3Service {
    @GET("v2/home")
    fun searchData(
            @Query("key") key: String, @Query("q") term: String
    ): Observable<Response<String>>
}
