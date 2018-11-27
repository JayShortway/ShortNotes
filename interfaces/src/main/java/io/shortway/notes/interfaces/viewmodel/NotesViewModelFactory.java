package io.shortway.notes.interfaces.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import io.shortway.notes.domain.repository.NotesRepository;

public class NotesViewModelFactory implements ViewModelProvider.Factory {

    private NotesRepository notesRepository;

    public NotesViewModelFactory(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NotesViewModel(notesRepository);
    }


}
