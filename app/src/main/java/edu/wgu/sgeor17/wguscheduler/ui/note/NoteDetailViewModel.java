package edu.wgu.sgeor17.wguscheduler.ui.note;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.wgu.sgeor17.wguscheduler.model.Note;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class NoteDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Note> noteData = new MutableLiveData<>();
    private AppRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public NoteDetailViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public void loadData(final int noteID) {
        executor.execute(() -> {
            Note tempNote = repository.getNoteByID(noteID);
            noteData.postValue(tempNote);
        });
    }

    public MutableLiveData<Note> getNoteData() {
        return noteData;
    }

    public void saveNote(String noteText, int courseID) {
        Note note = noteData.getValue();

        if (note == null) {
            if (TextUtils.isEmpty(noteText.trim())) {
                return;
            }
            note = new Note (new Date(), noteText.trim(), courseID);
        } else {
            note.setText(noteText.trim());
        }
        repository.insertNote(note);
    }

    public void deleteNote () {
        repository.deleteNote(noteData.getValue());
    }
}
