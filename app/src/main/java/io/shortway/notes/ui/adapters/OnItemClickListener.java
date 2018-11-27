package io.shortway.notes.ui.adapters;

import android.view.View;

/**
 * A listener to listen for clicks on items of an {@code Adapter}.
 * @param <E> The type of items held by the {@code Adapter}.
 */
public interface OnItemClickListener<E> {
    /**
     * Callback to be called whenever an {@code Adapter}'s item is clicked.
     * @param view The item's View that was clicked.
     * @param item The item that was clicked.
     * @param position The position of the clicked item.
     */
    void onItemClick(View view, E item, int position);
}
