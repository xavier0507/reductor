package com.yheriatovych.reductor.example

import com.google.gson.TypeAdapterFactory
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory

@GsonTypeAdapterFactory
abstract class MyAdapterFactory : TypeAdapterFactory {
    companion object {
        fun create(): TypeAdapterFactory {
            return AutoValueGson_MyAdapterFactory()
        }
    }
}
