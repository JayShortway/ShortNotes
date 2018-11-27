package io.shortway.notes.notes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.shortway.notes.Common;
import io.shortway.notes.domain.model.notes.NotesStore;
import timber.log.Timber;

/**
 * A utility class that can import several
 * {@link io.shortway.notes.domain.model.notes.Note Note}s into a {@link NotesStore} at once.
 */
public class NotesImporter {
    private static final String KEY_DEFAULT_NOTES_IMPORTED = "default_notes_imported";

    private Context context;
    private NotesStore notesStore;
    private String[] defaultNotes;

    /**
     * Construct a NotesImporter that can import Notes into a NotesStore.
     * @param context An Android Context.
     * @param notesStore The NotesStore to import the Notes into.
     * @param defaultNotes An array of JSON-String representations of default Notes.
     */
    public NotesImporter(Context context, NotesStore notesStore, String[] defaultNotes) {
        this.context = context;
        this.notesStore = notesStore;
        this.defaultNotes = defaultNotes;
    }

    /**
     * Check whether the default Notes have been imported.
     * <br/>
     * This method is static so we can check this condition without having to instantiate a
     * NotesImporter.
     * @param context An Android application Context.
     * @return Whether the default Notes have already been imported.
     */
    public static boolean defaultNotesImported(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_DEFAULT_NOTES_IMPORTED, false);
    }

    /**
     * Import the default Notes into the {@link #notesStore}.
     * <br/>
     * Make sure you first call {@link #defaultNotesImported(Context)} to check whether this operation is
     * necessary at all.
     * <br/><br/>
     * TODO Use a Foreground service.
     */
    public void importDefaults(){
        Completable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        notesStore.importNotes(defaultNotes);
                        return true; //Unused
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        setDefaultNotesImported(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e,"Error while importing default Notes.");
                        setDefaultNotesImported(false);
                    }
                });
    }

    /**
     * Record whether the default Notes have been imported successfully.
     * @param imported Whether the default Notes have been imported successfully.
     */
    private void setDefaultNotesImported(boolean imported){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Common.PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(KEY_DEFAULT_NOTES_IMPORTED, imported).apply();
    }


}
