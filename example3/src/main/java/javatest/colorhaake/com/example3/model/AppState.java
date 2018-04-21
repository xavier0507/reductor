package javatest.colorhaake.com.example3.model;

import com.google.auto.value.AutoValue;
import com.yheriatovych.reductor.annotations.CombinedState;

import java.util.ArrayList;

@CombinedState
@AutoValue
public abstract class AppState {
    public abstract Response<String> searchData();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_AppState.Builder();
    }

    public AppState withSearchData(Response<String> value) {
        return toBuilder().setSearchData(value).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setSearchData(Response<String> value);
        public abstract AppState build();
    }

    public static AppState initState() {
        return builder()
                .setSearchData(new Response<>(0, 0, new ArrayList<>()))
                .build();
    }
}
