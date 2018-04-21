package javatest.colorhaake.com.example3.model

import com.google.auto.value.AutoValue
import com.yheriatovych.reductor.annotations.CombinedState

import java.util.ArrayList

@CombinedState
@AutoValue
abstract class AppState {
    abstract fun searchData(): Response<String>

    abstract fun toBuilder(): Builder

    fun withSearchData(value: Response<String>): AppState {
        return toBuilder().setSearchData(value).build()
    }

    @AutoValue.Builder
    abstract class Builder {
        abstract fun setSearchData(value: Response<String>): Builder
        abstract fun build(): AppState
    }

    companion object {

        fun builder(): Builder {
            return AutoValue_AppState.Builder()
        }

        fun initState(): AppState {
            return builder()
                    .setSearchData(Response(0, 0, ArrayList()))
                    .build()
        }
    }
}
