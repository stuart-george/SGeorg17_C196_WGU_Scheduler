package edu.wgu.sgeor17.wguscheduler.ui.assessment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Assessment;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class AssessmentViewModel extends AndroidViewModel {
    private LiveData<List<Assessment>> liveAssessments;
    private AppRepository repository;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        liveAssessments = repository.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return liveAssessments;
    }
}
