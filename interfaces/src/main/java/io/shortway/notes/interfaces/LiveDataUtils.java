package io.shortway.notes.interfaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Convenience class that shortens the line-length when converting ReactiveX classes to LiveData.
 */
public class LiveDataUtils {

    /**
     * Converts a {@link Flowable} to {@link LiveData}.
     * @param flowable The {@code Flowable} to convert.
     * @param <T> The type of data held.
     * @return A {@code LiveData} converted from {@code flowable}.
     */
    public static <T> LiveData<T> from(Flowable<T> flowable){
        return LiveDataReactiveStreams.fromPublisher(flowable);
    }

    public static <T> LiveData<T> from(Observable<T> observable, BackpressureStrategy strategy){
        return from(observable.toFlowable(strategy));
    }

    public static <T> LiveData<T> from(Single<T> single){
        return from(single.toFlowable());
    }

}
