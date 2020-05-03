package edu.wgu.sgeor17.wguscheduler.ui.assessment;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.wgu.sgeor17.wguscheduler.model.Assessment;
import edu.wgu.sgeor17.wguscheduler.model.AssessmentType;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class AssessmentDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Assessment> assessmentData = new MutableLiveData<>();
    private LiveData<List<Course>> courseList;
    private AppRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentDetailViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        courseList = repository.getAllCourses();
    }

    public void loadData(final int assessmentID) {
        executor.execute(() -> {
            Assessment tempAssessment = repository.getAssessmentByID(assessmentID);
            assessmentData.postValue(tempAssessment);
        });
    }

    public MutableLiveData<Assessment> getAssessmentData() { return assessmentData;}

    public LiveData<List<Course>> getCourseList() { return  courseList;};

    public void saveAssessment (String title, Date assessmentDate, AssessmentType type, int courseID) {
        Assessment assessment = assessmentData.getValue();
        if (assessment == null) {
            if (TextUtils.isEmpty(title.trim())) {
                return;
            }

            assessment = new Assessment(title.trim(), assessmentDate, type, courseID);
        } else {
            assessment.setTitle(title.trim());
            assessment.setAssessmentDate(assessmentDate);
            assessment.setType(type);
            assessment.setCourseID(courseID);
        }

        repository.insertAssessment(assessment);
    }

    public void deleteAssessment() {repository.deleteAssessment(assessmentData.getValue());}
}
