package io.shortway.notes;

import android.app.Application;

import io.shortway.notes.domain.model.notes.NotesStore;
import io.shortway.notes.notes.NotesImporter;
import io.shortway.notes.notes.SharedPrefsNotesStore;
import timber.log.Timber;

public class NotesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable logging in debug builds:
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        // Initialize with default Notes if necessary:
        initialize();
    }


    private void initialize(){
        if(!NotesImporter.defaultNotesImported(this)){
            NotesStore notesStore = SharedPrefsNotesStore.getInstance(this);
            String[] defaultNotes = getResources().getStringArray(R.array.notes_default);
            NotesImporter importer = new NotesImporter(this, notesStore, defaultNotes);
            importer.importDefaults();
        }

    }






}
