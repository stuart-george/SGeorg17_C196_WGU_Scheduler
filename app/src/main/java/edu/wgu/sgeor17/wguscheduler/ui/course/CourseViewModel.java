package edu.wgu.sgeor17.wguscheduler.ui.course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class CourseViewModel extends AndroidViewModel {
    private LiveData<List<Course>> liveCourses;
    private AppRepository repository;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        liveCourses = repository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {return liveCourses;}
}
