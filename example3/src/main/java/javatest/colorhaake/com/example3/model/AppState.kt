package javatest.colorhaake.com.example3.model

import com.google.auto.value.AutoValue
import com.yheriatovych.reductor.annotations.CombinedState

@CombinedState
@AutoValue
abstract class AppState {
    abstract fun searchImageData(): Response<ImageData>

    abstract fun toBuilder(): Builder

    fun withSearchImageData(value: Response<ImageData>): AppState {
        return toBuilder().setSearchImageData(value).build()
    }

    @AutoValue.Builder
    abstract class Builder {
        abstract fun setSearchImageData(value: Response<ImageData>): Builder
        abstract fun build(): AppState
    }

    companion object {
        fun builder(): Builder {
            return AutoValue_AppState.Builder()
        }

        fun initState(): AppState {
            return builder()
                    .setSearchImageData(Response(0, 0, listOf()))
                    .build()
        }
    }
}
