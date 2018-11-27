package io.shortway.notes.data.notes;

import java.util.List;

import io.reactivex.Observable;
import io.shortway.notes.domain.model.notes.Note;
import io.shortway.notes.domain.repository.NotesRepository;
import io.shortway.notes.domain.model.notes.NotesStore;

/**
 * A default implementation of {@link NotesRepository}. This one delegates all work to its
 * {@link NotesStore}. Future versions could, for example, include support for cloud storage of
 * Notes.
 */
public class DefaultNotesRepository implements NotesRepository {

    private NotesStore store;

    private static DefaultNotesRepository instance;

    /**
     * Get a singleton instance of this class. The {@link NotesStore} argument only exists to
     * be able to use Android-specific implementations of {@code NotesStore}. Therefore, it only
     * takes effect on the first call to this method, when the instance is constructed. It is not
     * meant to change the implementation of {@code NotesStore} at runtime.
     * @param store The {@link NotesStore} implementation to use. Only takes effect on the first
     *              call to this method
     * @return A singleton instance of this class.
     */
    public static synchronized DefaultNotesRepository getInstance(NotesStore store){
        if(instance == null){
            instance = new DefaultNotesRepository(store);
        }
        return instance;
    }

    private DefaultNotesRepository(NotesStore store) {
        this.store = store;
    }

    @Override
    public Observable<List<Note>> getNotes() {
        return store.getNotes();
    }

    @Override
    public Observable<Note> getNote(long id) {
        return store.getNote(id);
    }

    @Override
    public void addNote(String title, String body) {
        store.addNote(title, body);
    }

    @Override
    public void deleteNote(long id) {
        store.deleteNote(id);
    }

    @Override
    public void updateNote(long id, String title, String body) {
        store.updateNote(id, title, body);
    }

    @Override
    public void clearNotes() {
        store.clearNotes();
    }
}
