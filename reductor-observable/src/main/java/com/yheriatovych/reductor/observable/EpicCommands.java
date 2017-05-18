package com.yheriatovych.reductor.observable;

import com.yheriatovych.reductor.Action;
import com.yheriatovych.reductor.Commands;
import com.yheriatovych.reductor.Store;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class EpicCommands<S, T> implements Commands<S>, Disposable {

    private List<Observable<T>> tasks;
    private Disposable disposable;

    private EpicCommands(List<Observable<T>> tasks) {this.tasks = tasks; }

    public static <S, T> EpicCommands<S, T> create(List<Observable<T>> list) {
        return new EpicCommands<>(list);
    }

    public static <S, T> EpicCommands<S, T> create(Observable<T> task) {
        return create(Arrays.asList(task));
    }

    @Override
    public void run(Store<S> store) {
        if (tasks == null) return;

        // TODO check this
        disposable = Observable.fromIterable(tasks).flatMap(obs -> obs).subscribe(action -> {
            if (action instanceof Action) {
                store.dispatch(action);
            }
        });
    }

    @Override
    public void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public boolean isDisposed() {
        return disposable != null && disposable.isDisposed();
    }
}
