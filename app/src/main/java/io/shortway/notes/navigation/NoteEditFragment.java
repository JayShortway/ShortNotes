package io.shortway.notes.navigation;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.shortway.notes.R;
import io.shortway.notes.data.notes.DefaultNotesRepository;
import io.shortway.notes.databinding.FragmentNoteEditBinding;
import io.shortway.notes.domain.model.notes.Note;
import io.shortway.notes.domain.repository.NotesRepository;
import io.shortway.notes.domain.model.notes.NotesStore;
import io.shortway.notes.interfaces.viewmodel.NotesViewModel;
import io.shortway.notes.interfaces.viewmodel.NotesViewModelFactory;
import io.shortway.notes.notes.SharedPrefsNotesStore;
import io.shortway.notes.ui.SnackBarContent;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

/**
 * This Fragment displays the UI to either edit an existing Note or add a new one.
 */
public class NoteEditFragment extends BaseFragment implements View.OnClickListener {

    private NotesViewModel notesViewModel;
    private NavViewModel navViewModel;
    private FragmentNoteEditBinding binding;
    /**
     * If we are being used to edit a Note, this field will hold that existing Note. This field is
     * null if we are being used to add a new Note.
     */
    private Note existingNote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We have an OptionsMenu (for our save button).
        setHasOptionsMenu(true);

        // Get our ViewModels.
        NotesStore store = SharedPrefsNotesStore.getInstance(requireContext());
        NotesRepository repository = DefaultNotesRepository.getInstance(store);
        NotesViewModelFactory factory = new NotesViewModelFactory(repository);
        notesViewModel = ViewModelProviders.of(requireActivity(), factory).get(NotesViewModel.class);
        navViewModel = ViewModelProviders.of(requireActivity()).get(NavViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoteEditBinding.inflate(inflater, container, false);
        notesViewModel.getSelectedNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                binding.setNote(note); // Bind our Note to the layout.
                existingNote = note;
                if(existingNote != null){
                    navViewModel.setTitle(getString(R.string.fragment_title_note_edit));
                } else {
                    navViewModel.setTitle(getString(R.string.fragment_title_note_add));
                }
            }
        });
        return binding.getRoot();
    }

    /**
     * TODO Implement this, to save contents of the EditTexts on (e.g.) rotation.
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate our menu.
        requireActivity().getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        // Find our save button and attach an OnClickListener. (OnOptionsItemSelected is not called
        // for MenuItems with an ActionView.)
        MenuItem saveItem = menu.findItem(R.id.menu_action_save);
        View actionView = saveItem.getActionView();
        Button saveButton = actionView.findViewById(R.id.button_note_save);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_note_save){
            Editable editableTitle = binding.edittextNoteTitle.getText();
            Editable editableBody = binding.edittextNoteBody.getText();
            String title = editableTitle != null ? editableTitle.toString() : "";
            String body = editableBody != null ? editableBody.toString() : "";

            int titleLength = title.trim().length();
            int bodyLength = body.trim().length();

            if(titleLength > 0 && bodyLength > 0){
                saveNote(title, body);
            } else {
                // TODO Show error in the TextInputLayout
                showSnackBarError(titleLength, bodyLength);
            }
        }
    }

    /**
     * Instructs the NotesViewModel to save a Note, asks the Activity to show a SnackBar, and then
     * pops itself of the stack.
     * @param title The title of the Note to save.
     * @param body The body of the Note to save.
     */
    private void saveNote(String title, String body){
        // TODO Can we do the saving with two-way DataBinding?
        if(existingNote != null){
            // We are editing a note
            notesViewModel.updateNote(existingNote.getId(), title, body);
        } else {
            notesViewModel.addNote(title, body);
        }
        showSnackBarSuccess(title);
        popBackStack();
    }

    private void showSnackBarSuccess(String title){
        String message = getString(R.string.success_note_saved, title);
        navViewModel.showSnackBar(new SnackBarContent(message, Snackbar.LENGTH_SHORT));
    }

    private void showSnackBarError(int titleLength, int bodyLength){
        String message = null;
        if(titleLength < 1 && bodyLength < 1){
            message = getString(R.string.error_note_empty_both);
        } else if(titleLength < 1){
            message = getString(R.string.error_note_empty_title);
        } else if(bodyLength < 1){
            message = getString(R.string.error_note_empty_note);
        }
        if(message != null){
            navViewModel.showSnackBar(new SnackBarContent(message, Snackbar.LENGTH_SHORT));
        }
    }

}
