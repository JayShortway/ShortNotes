package io.shortway.notes.domain.model;

public interface JsonSerializable<T> {

    /**
     * Converts an Object to JSON. Returns a String so we don't have to commit to a specific
     * implementation of JSON, such as {@link org.json.JSONObject}.
     * @return A JSON-String representation of the Object.
     */
    String toJson();

    /**
     * Converts JSON into an Object of type {@code T}. Takes a String so we don't have to commit to
     * a specific implementation of JSON, such as {@link org.json.JSONObject}.
     * @param json A JSON-String representation of the Object.
     * @return An Object of type {@code T}.
     */
     T fromJson(String json);

}
