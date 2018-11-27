package io.shortway.notes.notes;

import android.content.SharedPreferences;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * A bridge between {@link io.reactivex.Observable Observable} and {@link SharedPreferences}. To be
 * used with {@link io.reactivex.Observable#create(ObservableOnSubscribe) Observable.create()}.
 * @param <T> The type of the items emitted by the Observable.
 */
public abstract class SharedPreferencesChangeForwarder<T> implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ObservableEmitter<T> emitter;

    /**
     * Use this constructor inside
     * {@link io.reactivex.ObservableOnSubscribe#subscribe(ObservableEmitter)
     * ObservableOnSubscribe#subscribe(ObservableEmitter)}.
     * @param emitter The {@link ObservableEmitter} argument provided by
     * {@link io.reactivex.ObservableOnSubscribe#subscribe(ObservableEmitter)
     * subscribe(ObservableEmitter)}.
     */
    public SharedPreferencesChangeForwarder(ObservableEmitter<T> emitter){
        this.emitter = emitter;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        emitter.onNext(convert(sharedPreferences, key));
    }

    /**
     * Converts SharedPreferences to the desired type. This method is called every time
     * {@link #onSharedPreferenceChanged(SharedPreferences, String)} is called. Implementations of
     * this method should convert the full {@code sharedPreferences}, not only the value belonging
     * to {@code key}.
     * @param sharedPreferences The SharedPreferences argument as supplied to
     * {@link #onSharedPreferenceChanged(SharedPreferences, String)}.
     * @param key The String ({@code key}) argument as supplied to
     *      * {@link #onSharedPreferenceChanged(SharedPreferences, String)}.
     * @return The complete {@code sharedPreferences} converted into the desired type.
     */
    public abstract T convert(SharedPreferences sharedPreferences, String key);

}
