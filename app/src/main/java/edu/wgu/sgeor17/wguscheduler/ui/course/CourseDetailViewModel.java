package edu.wgu.sgeor17.wguscheduler.ui.course;

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
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;
import edu.wgu.sgeor17.wguscheduler.model.Note;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class CourseDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Course> courseData = new MutableLiveData<>();
    private LiveData<List<Note>> liveNotes;
    private LiveData<List<Assessment>> liveAssessments;
    private AppRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CourseDetailViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public void loadData(final int courseID) {
        executor.execute(() -> {
            Course tempCourse = repository.getCourseByID(courseID);
            courseData.postValue(tempCourse);
        });
        liveNotes = repository.getAllNotesForCourse(courseID);
        liveAssessments = repository.getAllAssessmentsForCourse(courseID);
    }

    public MutableLiveData<Course> getCourseData() {return courseData;}

    public LiveData<List<Note>> getNoteData() {return  liveNotes;}

    public LiveData<List<Assessment>> getAssessmentsData() { return liveAssessments;}

    public void saveCourse (String title,
                            Date startDate,
                            Date endDate,
                            CourseStatus status,
                            int termID) {

        Course course = courseData.getValue();
        if (course == null) {
            if(TextUtils.isEmpty(title.trim())) {
                return;
            }
            course = new Course(title.trim(), startDate, endDate, status, termID);

        } else {
            course.setTitle(title.trim());
            course.setStartDate(startDate);
            course.setEstEndDate(endDate);
            course.setStatus(status);
            course.setTermID(termID);
        }
        repository.insertCourse(course);
    }

    public void deleteCourse () {
        repository.deleteCourse(courseData.getValue());
    }
}
