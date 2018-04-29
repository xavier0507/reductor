package javatest.colorhaake.com.example3.network

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException

import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.ArrayList

import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

import io.reactivex.Observable
import javatest.colorhaake.com.example3.model.HttpStatusCode
import javatest.colorhaake.com.example3.model.Metadata
import javatest.colorhaake.com.example3.model.Response
import retrofit2.Call
import retrofit2.CallAdapter

class RxJava2CallAdapterWithErrorInterceptor<R> internal constructor(
        private val callAdapter: CallAdapter<R, Observable<Response<*>>>, private val isBody: Boolean
) : CallAdapter<R, Observable<Response<*>>> {

    override fun responseType(): Type = callAdapter.responseType()

    override fun adapt(call: Call<R>): Observable<Response<*>> {
        val type = callAdapter.adapt(call)
        return if (isBody) {
            type.map { res ->
                    res.copy(
                            _metadata = Metadata(Metadata.TYPE_NORMAL, HttpURLConnection.HTTP_OK, "", "")
                    )
                }
                    .onErrorResumeNext(::handleException)
        } else type
    }

    private fun handleException(t: Throwable): Observable<out Response<*>> {
        return if (t is retrofit2.HttpException) {
            try {
                val res = t.response()
                val error = Gson().fromJson<Response<*>>(
                        res.errorBody().charStream(), responseType()
                ).let {
                    it.copy(
                            it?.total ?: 0,
                            it?.totalHits ?: 0,
                            it?.hits ?: listOf(),
                            Metadata(Metadata.TYPE_NORMAL, res.code(), "", "")
                    )
                }

                Observable.just(error)
            } catch (e: IllegalStateException) {
                Observable.just(Response.defaultHttpExceptionInstance)
            } catch (e: JsonIOException) {
                Observable.just(Response.defaultHttpExceptionInstance)
            } catch (e: JsonSyntaxException) {
                Observable.just(Response.defaultHttpExceptionInstance)
            }
        } else {
            val metaType = convertExceptionToMetaType(t)
            Observable.just(Response<Any>(
                    0,
                    0,
                    listOf(),
                    Metadata(metaType, HttpStatusCode.INVALID, "", "")
            ))
        }
    }

    private fun convertExceptionToMetaType(t: Throwable): Int {
        return if (t is UnknownHostException) {
            Metadata.TYPE_UNKNOWN_HOST_EXCEPTION
        } else if (t is ConnectException) {
            Metadata.TYPE_CONNECT_EXCEPTION
        } else if (t is SocketTimeoutException) {
            Metadata.TYPE_SOCKET_TIMEOUT_EXCEPTION
        } else if (t is SocketException) {
            Metadata.TYPE_SOCKET_EXCEPTION
        } else if (t is SSLHandshakeException) {
            Metadata.TYPE_SSL_HANDSHAKE_EXCEPTION
        } else if (t is SSLException) {
            Metadata.TYPE_SSL_EXCEPTION
        } else if (t is IOException) {
            Metadata.TYPE_IO_EXCEPTION
        } else {
            Metadata.TYPE_DO_NOT_KNOW_HOW_TO_HANDLE_EXCEPTION
        }
    }
}
