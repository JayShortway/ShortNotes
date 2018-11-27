package io.shortway.notes.domain.model.notes;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.shortway.notes.domain.model.JsonSerializable;

public class Note implements JsonSerializable<Note>, Comparable<Note> {
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";

    private long id;
    private String title;
    private String body;

    /**
     * This constructor exists only to be able to use {@link #fromJson(String)}.
     * <br/>
     * <b>Caution:</b> the Note object returned by this constructor should not be used.
     * <br/><br/>
     * TODO: Find a better solution for deserialization from JSON.
     */
    Note(){ }

    Note(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Note: " + toJson();
    }

    @Override
    public String toJson() {
        return new JSONObject()
                .put(KEY_ID, id)
                .put(KEY_TITLE, title)
                .put(KEY_BODY, body)
                .toString();
    }

    @Override
    public Note fromJson(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return new Note(
                jsonObject.getLong(KEY_ID),
                jsonObject.getString(KEY_TITLE),
                jsonObject.getString(KEY_BODY));
    }

    @Override
    public int compareTo(@NonNull Note note) {
        int comparison = getTitle().compareTo(note.getTitle());
        if(comparison == 0){
            comparison = getBody().compareTo(note.getBody());
        }
        return comparison;
    }
}
