package edu.wgu.sgeor17.wguscheduler.ui.term;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;
import edu.wgu.sgeor17.wguscheduler.utility.SampleData;

public class TermViewModel extends AndroidViewModel {
    private LiveData<List<Term>> liveTerms;
    private AppRepository repository;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        liveTerms = repository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return liveTerms;
    }
}
