package io.shortway.notes.ui.adapters;

/**
 * A listener to listen for deletions of items of an {@code Adapter}.
 * @param <E> The type of items held by the {@code Adapter}.
 */
public interface OnItemDeleteListener<E> {
    /**
     * Callback to be called whenever an {@code Adapter}'s item is deleted.
     * @param item The item that was deleted.
     * @param position The old position of the deleted item.
     */
    void onItemDelete(E item, int position);
}
