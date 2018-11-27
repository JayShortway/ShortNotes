package io.shortway.notes.navigation;


import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.shortway.notes.R;
import io.shortway.notes.data.notes.DefaultNotesRepository;
import io.shortway.notes.databinding.FragmentNoteDetailsBinding;
import io.shortway.notes.domain.model.notes.Note;
import io.shortway.notes.domain.repository.NotesRepository;
import io.shortway.notes.domain.model.notes.NotesStore;
import io.shortway.notes.interfaces.viewmodel.NotesViewModel;
import io.shortway.notes.interfaces.viewmodel.NotesViewModelFactory;
import io.shortway.notes.notes.SharedPrefsNotesStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NoteDetailsFragment extends BaseFragment {

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
                    navigate(R.id.action_global_noteEditFragment);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentNoteDetailsBinding binding =
                FragmentNoteDetailsBinding.inflate(inflater, container, false);
        notesViewModel.getSelectedNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                binding.setNote(note);
                navViewModel.setTitle(note.getTitle());
            }
        });
        return binding.getRoot();
    }

}
