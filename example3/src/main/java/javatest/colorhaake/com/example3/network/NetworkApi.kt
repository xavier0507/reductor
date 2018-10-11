package javatest.colorhaake.com.example3.network

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import javatest.colorhaake.com.example3.Config

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javatest.colorhaake.com.example3.model.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javatest.colorhaake.com.example3.Config.API_KEY
import javatest.colorhaake.com.example3.model.HttpStatusCode
import javatest.colorhaake.com.example3.model.ImageData
import javatest.colorhaake.com.example3.model.Metadata
import java.io.IOException
import java.lang.reflect.Type
import java.net.*
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

object NetworkApi {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactoryWithErrorInterceptor.createWithScheduler(
                                Schedulers.io(), ::mapOnSuccess, ::mapOnError
                        )
                )
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

    private fun mapOnSuccess(response: Response<*>): Response<*> =
            response.copy(
                    _metadata = Metadata(Metadata.TYPE_NORMAL, HttpURLConnection.HTTP_OK, "", "")
            )
}
    private fun mapOnError(t: Throwable, responseType: Type): Response<*> {
        return if (t is retrofit2.HttpException) {
            try {
                val res = t.response()
                Gson().fromJson<Response<*>>(res.errorBody().charStream(), responseType)
                        .let {
                            it.copy(
                                    it?.total ?: 0,
                                    it?.totalHits ?: 0,
                                it?.hits ?: listOf(),
                                    Metadata(Metadata.TYPE_NORMAL, res.code(), "", "")
                            )
                        }
            } catch (e: IllegalStateException) {
                Response.defaultHttpExceptionInstance
            } catch (e: JsonIOException) {
                Response.defaultHttpExceptionInstance
            } catch (e: JsonSyntaxException) {
                Response.defaultHttpExceptionInstance
            }
        } else {
            val metaType = convertExceptionToMetaType(t)
            Response<Any>(
                    0,
                    0,
                    listOf(),
                    Metadata(metaType, HttpStatusCode.INVALID, "", "")
            )
        }
    }

    private fun convertExceptionToMetaType(t: Throwable): Int {
        return when (t) {
            is UnknownHostException -> Metadata.TYPE_UNKNOWN_HOST_EXCEPTION
            is ConnectException -> Metadata.TYPE_CONNECT_EXCEPTION
            is SocketTimeoutException -> Metadata.TYPE_SOCKET_TIMEOUT_EXCEPTION
            is SocketException -> Metadata.TYPE_SOCKET_EXCEPTION
            is SSLHandshakeException -> Metadata.TYPE_SSL_HANDSHAKE_EXCEPTION
            is SSLException -> Metadata.TYPE_SSL_EXCEPTION
            is IOException -> Metadata.TYPE_IO_EXCEPTION
            else -> Metadata.TYPE_DO_NOT_KNOW_HOW_TO_HANDLE_EXCEPTION
        }
    }
