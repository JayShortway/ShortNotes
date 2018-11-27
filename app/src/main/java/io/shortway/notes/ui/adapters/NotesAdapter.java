package io.shortway.notes.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.shortway.notes.R;
import io.shortway.notes.domain.model.notes.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
        implements BindableAdapter<List<Note>> {
    private static final int SNIPPET_LENGTH = 500;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView titleView;
        TextView snippetView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.titleView = itemView.findViewById(R.id.textview_note_title);
            this.snippetView = itemView.findViewById(R.id.textview_note_snippet);
        }
    }

    private OnItemClickListener<Note> onItemClickListener;
    private OnItemDeleteListener<Note> onItemDeleteListener;
    private List<Note> data;

    public NotesAdapter(OnItemClickListener<Note> onItemClickListener, OnItemDeleteListener<Note> onItemDeleteListener) {
        this.onItemClickListener = onItemClickListener;
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public void setData(List<Note> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Note note = data.get(position);
        // Getting rid of newlines and capping the length for our snippet.
        String snippet = note.getBody().replaceAll("\n","");
        if(snippet.length() > SNIPPET_LENGTH){
            snippet = snippet.substring(0, SNIPPET_LENGTH);
        }
        holder.titleView.setText(note.getTitle());
        holder.snippetView.setText(snippet);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, note, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    /**
     * Delete an Item from this {@code Adapter}.
     * @param position The position of the item to delete.
     */
    public void deleteItemAt(int position){
        Note deleted = data.remove(position);
        if(onItemDeleteListener != null){
            onItemDeleteListener.onItemDelete(deleted, position);
        }
        notifyItemRemoved(position);
    }

}
