package io.shortway.notes.notes;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.shortway.notes.domain.model.notes.Note;
import io.shortway.notes.domain.model.notes.NotesStore;
import timber.log.Timber;

/**
 * TODO v2 of this class could probably be more efficient by using a (Behavior)Subject.
 */
public class SharedPrefsNotesStore extends NotesStore {
    private static final String NAME = "notes";

    private SharedPreferences notesSharedPreferences;
    private Observable<Map<Long, Note>> notes;

    private static SharedPrefsNotesStore instance;

    public static synchronized SharedPrefsNotesStore getInstance(Context context){
        if(instance == null){
            instance = new SharedPrefsNotesStore(context.getApplicationContext());
        }
        return instance;
    }

    private SharedPrefsNotesStore(Context context){
        notesSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        notes = Observable.create(new ObservableOnSubscribe<Map<Long, Note>>() {
            NotesChangeForwarder notesListener;

            @Override
            public void subscribe(final ObservableEmitter<Map<Long, Note>> emitter) {
                // Emit last value
                emitter.onNext(createNotes(notesSharedPreferences.getAll()));
                // Storing a reference to our listeners, because the preference manager doesn't,
                // making our listeners susceptible to being garbage collected.
                notesListener = new NotesChangeForwarder(emitter);
                notesSharedPreferences.registerOnSharedPreferenceChangeListener(notesListener);
            }
        });

        notes.share()       // Allow multiple subscribers.
                .replay()   // Make sure new subscribers get values they missed.
                .connect(); // 'Start' the observable (necessary because of share() and replay()).
    }


    @Override
    public Observable<List<Note>> getNotes() {
        return notes.map(new Function<Map<Long, Note>, List<Note>>() {
            @Override
            public List<Note> apply(Map<Long, Note> notesMap) {
                List<Note> notes = new ArrayList<>(notesMap.values());
                Collections.sort(notes);
                return notes;
            }
        });
    }

    @Override
    public Observable<Note> getNote(final long id) {
        return notes.map(new Function<Map<Long, Note>, Note>() {
            @Override
            public Note apply(Map<Long, Note> notesMap) {
                return notesMap.get(id); // TODO handle errors.
            }
        });
    }

    @Override
    public void addNote(String title, String body) {
        long id = getNewId();
        String noteJson = createNote(id, title, body).toJson();
        notesSharedPreferences.edit().putString(String.valueOf(id), noteJson).apply();
    }

    private void addNote(long id, Note note){
        notesSharedPreferences.edit().putString(String.valueOf(id), note.toJson()).apply();
    }

    @Override
    public void deleteNote(long id) {
        notesSharedPreferences.edit().remove(String.valueOf(id)).apply();
    }

    @Override
    public void updateNote(long id, @Nullable String title, @Nullable String body) {
        String idString = String.valueOf(id); // We only need to convert this once, to use it twice.
        String json = notesSharedPreferences.getString(idString, null);
        if(json != null){
            Note existingNote = createNote(json);
            // Update the title and body only if the respective arguments are non-null.
            String updatedTitle = title != null ? title : existingNote.getTitle();
            String updatedBody = body != null ? body : existingNote.getBody();
            Note updatedNote = createNote(id, updatedTitle, updatedBody);
            notesSharedPreferences.edit().putString(idString, updatedNote.toJson()).apply();
        } else {
            Timber.e("There is no Note with id %d", id);
        }
    }

    @Override
    public void clearNotes() {
        notesSharedPreferences.edit().clear().apply();
    }

    @Override
    public void importNotes(String[] noteJsons) {
        Timber.d("Importing notes: %s", Arrays.toString(noteJsons));
        int num = noteJsons != null ? noteJsons.length : 0;
        for(int i=0; i<num; i++){
            Note note = createNote(noteJsons[i]);
            addNote(note.getId(), note);
            // TODO We have just de-serialized and serialized the Note just to get the ID.
            // Should be more elegant.
        }
    }

    /**
     * Get an unused ID to be used when adding Notes.
     * @return An unused ID.
     */
    private long getNewId(){
        Set<String> ids = notesSharedPreferences.getAll().keySet();
        long maxId = -1;
        for(String idString : ids){
            long id = Long.parseLong(idString);
            if(id > maxId){
                maxId = id;
            }
        }
        long newId = maxId + 1;
        // We have our newId, but let's do a final check whether it already exists.
        while (ids.contains(String.valueOf(newId))){
            newId++;
        }
        return newId;
    }


    private static Map<Long, Note> createNotes(@NonNull Map<String, ?> notesSharedPreferences){
        int num = notesSharedPreferences.size();
        Map<Long, Note> notes = new HashMap<>(num);

        for(String idString : notesSharedPreferences.keySet()){
            long id = Long.parseLong(idString);
            String noteJson = (String) notesSharedPreferences.get(idString);
            Note note = createNote(noteJson);
            notes.put(id, note);
        }

        return notes;
    }

    private class NotesChangeForwarder extends SharedPreferencesChangeForwarder<Map<Long, Note>> {

        NotesChangeForwarder(ObservableEmitter<Map<Long, Note>> emitter) {
            super(emitter);
        }

        @Override
        public Map<Long, Note> convert(SharedPreferences notesSharedPreferences, String idString) {
            return createNotes(notesSharedPreferences.getAll());
        }
    }
}
