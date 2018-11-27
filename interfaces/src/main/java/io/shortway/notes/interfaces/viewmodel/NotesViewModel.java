package io.shortway.notes.interfaces.viewmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import io.reactivex.BackpressureStrategy;
import io.shortway.notes.domain.model.notes.Note;
import io.shortway.notes.domain.repository.NotesRepository;
import io.shortway.notes.interfaces.LiveDataUtils;

public class NotesViewModel extends ViewModel {

    // In-memory cache:
    private LiveData<List<Note>> notes;
    private Map<Long, LiveData<Note>> singleNotes = new HashMap<>();
    private MutableLiveData<Long> selectedNoteId = new MutableLiveData<>();


    private NotesRepository repository;

    public NotesViewModel(NotesRepository repository){
        this.repository = repository;
    }

    /**
     * Retrieves an observable List of all Notes to display.
     * @return A LiveData object holding a List of Notes.
     */
    public LiveData<List<Note>> getNotes(){
        // Do we have it cached already?
        if(notes == null){
            // No  cache. Let's retrieve it from the repository, and cache it.
            notes = LiveDataUtils.from(repository.getNotes(), BackpressureStrategy.LATEST);
        }
        return notes;
    }

    /**
     * Retrieve a single, observable Note to display.
     * @param id The ID of the Note to retrieve.
     * @return A LiveData object holding a Note.
     */
    public LiveData<Note> getNote(long id){
        // Check if the ID is valid:
        if(id >= 0){
            // Do we have it cached already?
            LiveData<Note> note = singleNotes.get(id);
            if(note == null){
                // No cache. Let's retrieve it from the repository, and cache it.
                note = LiveDataUtils.from(repository.getNote(id), BackpressureStrategy.LATEST);
                singleNotes.put(id, note);
            }
            return note;
        } else {
            // The ID is invalid. Return an empty LiveData.
            return new MutableLiveData<>();
        }
    }

    /**
     * Call this method from the view displaying a list of Notes to select one to be viewed in
     * the detail view.
     * @param id The ID of the Note to select.
     */
    public void selectNote(long id){
        selectedNoteId.setValue(id);
    }

    /**
     * Observe this from the view displaying a Note's details to know which Note's details to
     * display.
     * @return A LiveData holding a Note to display the details of.
     */
    public LiveData<Note> getSelectedNote(){
        return Transformations.switchMap(selectedNoteId, new Function<Long, LiveData<Note>>() {
            @Override
            public LiveData<Note> apply(Long id) {
                return getNote(id);
            }
        });
    }

    /**
     * Adds a Note to the repository.
     * <br/><br/>
     * TODO Return whether the operation was successful.
     * @param title The title of the Note to add.
     * @param body The body of the Note to add.
     */
    public void addNote(String title, String body){
        repository.addNote(title, body);
    }

    /**
     * Updates an existing Note.
     * @param id The ID of the Note to update.
     * @param title The new title of the Note. Can be null to use the existing one.
     * @param body The new body of the Note. Can be null to use the existing one.
     */
    public void updateNote(long id, @Nullable String title, @Nullable String body){
        repository.updateNote(id, title, body);
    }

    /**
     * Delete a Note from the repository.
     * <br/><br/>
     * TODO Return whether the operation was successful.
     * @param id The ID of the Note to delete.
     */
    public void deleteNote(long id){
        repository.deleteNote(id);
    }


}
