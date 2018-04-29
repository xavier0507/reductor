package javatest.colorhaake.com.example3.network

import io.reactivex.Observable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import io.reactivex.Scheduler
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RxJava2CallAdapterFactoryWithErrorInterceptor private constructor(
        scheduler: Scheduler?, isAsync: Boolean
) : CallAdapter.Factory() {

    private val adapterFactory: RxJava2CallAdapterFactory = when {
        scheduler != null -> RxJava2CallAdapterFactory.createWithScheduler(scheduler)
        isAsync -> RxJava2CallAdapterFactory.createAsync()
        else -> RxJava2CallAdapterFactory.create()
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(
            returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        val isBody = rawObservableType != Response::class.java
                && rawObservableType != Result::class.java

        val callAdapter = adapterFactory.get(returnType, annotations, retrofit)
                as? CallAdapter<*, Observable<javatest.colorhaake.com.example3.model.Response<*>>>
        return callAdapter?.let { RxJava2CallAdapterWithErrorInterceptor(it, isBody) }
    }

    companion object {
        fun create(): RxJava2CallAdapterFactoryWithErrorInterceptor {
            return RxJava2CallAdapterFactoryWithErrorInterceptor(null, false)
        }

        fun createAsync(): RxJava2CallAdapterFactoryWithErrorInterceptor {
            return RxJava2CallAdapterFactoryWithErrorInterceptor(null, true)
        }

        fun createWithScheduler(scheduler: Scheduler): RxJava2CallAdapterFactoryWithErrorInterceptor {
            return RxJava2CallAdapterFactoryWithErrorInterceptor(scheduler, false)
        }
    }
}
