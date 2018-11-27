package io.shortway.notes.navigation;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.shortway.notes.R;
import io.shortway.notes.data.notes.DefaultNotesRepository;
import io.shortway.notes.databinding.FragmentNotesListBinding;
import io.shortway.notes.domain.model.notes.Note;
import io.shortway.notes.domain.repository.NotesRepository;
import io.shortway.notes.domain.model.notes.NotesStore;
import io.shortway.notes.interfaces.viewmodel.NotesViewModel;
import io.shortway.notes.interfaces.viewmodel.NotesViewModelFactory;
import io.shortway.notes.notes.SharedPrefsNotesStore;
import io.shortway.notes.ui.SnackBarContent;
import io.shortway.notes.ui.adapters.NotesAdapter;
import io.shortway.notes.ui.adapters.OnItemClickListener;
import io.shortway.notes.ui.adapters.OnItemDeleteListener;
import io.shortway.notes.ui.adapters.SwipeToDeleteCallback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;


public class NotesListFragment extends BaseFragment implements OnItemClickListener<Note>,
        OnItemDeleteListener<Note> {

    private NotesViewModel notesViewModel;
    private NavViewModel navViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get our ViewModels.
        NotesStore store = SharedPrefsNotesStore.getInstance(requireContext());
        NotesRepository repository = DefaultNotesRepository.getInstance(store);
        NotesViewModelFactory factory = new NotesViewModelFactory(repository);
        notesViewModel = ViewModelProviders.of(requireActivity(), factory).get(NotesViewModel.class);
        navViewModel = ViewModelProviders.of(requireActivity()).get(NavViewModel.class);
        navViewModel.onFabClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean fabClicked) {
                if(fabClicked != null && fabClicked){
                    createNewNote();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNotesListBinding binding =
                FragmentNotesListBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(notesViewModel);
        // Set up the RecyclerView...
        binding.recyclerviewNotesList.setHasFixedSize(true);
        // ... with dividers...
        RecyclerView.ItemDecoration divider =
                new DividerItemDecoration(binding.recyclerviewNotesList.getContext(),
                        DividerItemDecoration.VERTICAL);
        binding.recyclerviewNotesList.addItemDecoration(divider);
        // ... its adapter...
        final NotesAdapter adapter = new NotesAdapter(this, this);
        binding.recyclerviewNotesList.setAdapter(adapter);
        // ... and SwipeToDelete.
        ItemTouchHelper.Callback callback = new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItemAt(viewHolder.getAdapterPosition());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewNotesList);

        return binding.getRoot();
    }

    @Override
    public void onItemClick(View view, Note item, int position) {
        notesViewModel.selectNote(item.getId());
        navigate(R.id.action_notesListFragment_to_noteDetailsFragment);
    }


    @Override
    public void onItemDelete(final Note item, int position) {
        notesViewModel.deleteNote(item.getId());
        String message = getString(R.string.success_note_deleted, item.getTitle());
        String actionText = getString(R.string.snackbar_action_undo);
        View.OnClickListener actionListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesViewModel.addNote(item.getTitle(), item.getBody());
            }
        };
        SnackBarContent snackBarContent =
                new SnackBarContent(message, Snackbar.LENGTH_LONG, actionText, actionListener);
        navViewModel.showSnackBar(snackBarContent);
    }

    private void createNewNote(){
        notesViewModel.selectNote(-1); // deselect any Note, because we are creating a new one.
        navigate(R.id.action_global_noteEditFragment);
    }

}
