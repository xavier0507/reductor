package javatest.colorhaake.com.example3.network;

import javatest.colorhaake.com.example3.Config;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javatest.colorhaake.com.example3.model.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static javatest.colorhaake.com.example3.Config.API_KEY;

public class NetworkApi {
    public static final String TAG = NetworkApi.class.getName();
    private static Retrofit retrofit = null;
    private static Example3Service service = null;
    static {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(RetrofitUtil.genericClient())
                .baseUrl(Config.BASE_API_URL)
                .build();

        service = retrofit.create(Example3Service.class);
    }

    public static Observable<Response<String>> searchData(String term) {
        return service.searchData(API_KEY, term);
    }
}
