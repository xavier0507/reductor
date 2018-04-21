package javatest.colorhaake.com.example3.network;

import io.reactivex.Observable;
import javatest.colorhaake.com.example3.model.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Example3Service {
    @GET("v2/home")
    Observable<Response<String>> searchData(
            @Query("key") String key, @Query("q") String term
    );
}
