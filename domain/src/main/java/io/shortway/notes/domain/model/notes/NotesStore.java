package io.shortway.notes.domain.model.notes;

import io.reactivex.annotations.NonNull;

/**
 * Allows for storage of Notes. This can be offline or online, depending on the implementation.
 * A NotesStore is the only class that can construct a {@link Note}.
 */
public abstract class NotesStore implements NotesContract{

    /**
     * Import multiple {@link Note}s into this NotesStore.
     * @param noteJsons An array of JSON-String representations of {@link Note}s.
     */
    public abstract void importNotes(String[] noteJsons);

    /**
     * Create a new {@link Note}. Use this when supplying a {@link Note} from your underlying
     * storage implementation.
     * @param id The ID of the Note.
     * @param title The title of the Note.
     * @param body The body of the Note.
     * @return A new {@link Note}, with the parameters supplied to this method as arguments.
     */
    protected static Note createNote(long id, @NonNull String title, @NonNull String body){
        return new Note(id, title, body);
    }

    /**
     * Create a new {@link Note}. Use this when supplying a {@link Note} from your underlying
     * storage implementation.
     * @param json A JSON-String representation of a {@link Note}.
     * @return A new {@link Note}, as represented by {@code json}.
     */
    protected static Note createNote(@NonNull String json){
        return new Note().fromJson(json);
    }

}
