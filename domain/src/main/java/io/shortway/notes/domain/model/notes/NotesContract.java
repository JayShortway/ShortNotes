package io.shortway.notes.domain.model.notes;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface NotesContract {

    Observable<List<Note>> getNotes();
    Observable<Note> getNote(long id);

    void addNote(@NonNull String title, @NonNull String body);
    void deleteNote(long id);
    void updateNote(long id, @Nullable String title, @Nullable String body);

    void clearNotes();

}
