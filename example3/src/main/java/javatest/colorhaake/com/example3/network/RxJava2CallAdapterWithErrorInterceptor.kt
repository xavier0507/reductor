package javatest.colorhaake.com.example3.network

import java.lang.reflect.Type

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.CallAdapter

class RxJava2CallAdapterWithErrorInterceptor<R, T> internal constructor(
        private val callAdapter: CallAdapter<R, Observable<T>>,
        private val isBody: Boolean,
        private val mapOnSuccess: ((T) -> T)?,
        private val mapOnError: ((Throwable, Type) -> T)?
) : CallAdapter<R, Observable<T>> {

    override fun responseType(): Type = callAdapter.responseType()

    override fun adapt(call: Call<R>): Observable<T> {
        val type = callAdapter.adapt(call)
        if (isBody.not()) return type

        return type
                .let { t -> mapOnSuccess?.let { t.map(it) } ?: t }
                .let { t -> mapOnError?.let {
                    e -> t.onErrorReturn { e.invoke(it, responseType()) } } ?: t
                }
    }
}
