package io.shortway.notes.ui.adapters;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A class holding all our {@link BindingAdapter}s, as annotated methods. This class shouldn't be
 * used directly.
 */
public class BindingAdapters {

    /**
     * Enables the use of the {@code app:data} attribute on a {@link RecyclerView} to auto-fill the
     * {@code RecyclerView}.
     * <br/><br/>
     * <b>This method should not be called directly.</b> It is to be used by the
     * Data Binding library.
     * @param recyclerView The RecyclerView to fill.
     * @param data The data to fill the RecyclerView with.
     * @param <T> The type of data.
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter("data")
    public static  <T> void setRecyclerViewData(RecyclerView recyclerView, T data) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(adapter instanceof BindableAdapter){
            ((BindableAdapter<T>) adapter).setData(data);
        }
    }


}
