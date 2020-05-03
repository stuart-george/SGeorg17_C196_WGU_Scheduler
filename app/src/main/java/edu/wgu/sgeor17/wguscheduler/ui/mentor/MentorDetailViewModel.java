package edu.wgu.sgeor17.wguscheduler.ui.mentor;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.Mentor;
import edu.wgu.sgeor17.wguscheduler.repository.AppRepository;

public class MentorDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Mentor> mentorData = new MutableLiveData<>();
    private LiveData<List<Course>> liveCourses;
    private AppRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MentorDetailViewModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
        liveCourses = repository.getAllCourses();
    }

    public void loadData(final int mentorID) {
        executor.execute(() -> {
            Mentor tempMentor = repository.getMentorByID(mentorID);
            mentorData.postValue(tempMentor);
        });
    }

    public MutableLiveData<Mentor> getMentorData() {return mentorData;}
    public LiveData<List<Course>> getCourseData() {return liveCourses;}

    public void saveMentor(String firstName, String lastName, String email, String phone, int courseID) {
        Mentor mentor = mentorData.getValue();
        if (mentor == null) {
            if(TextUtils.isEmpty(firstName.trim()) || TextUtils.isEmpty(lastName.trim())) {
                return;
            }
            mentor = new Mentor(firstName.trim(), lastName.trim(), email.trim(), phone.trim(), courseID);
        } else {
            mentor.setFirstName(firstName.trim());
            mentor.setLastName(lastName.trim());
            mentor.setEmail(email.trim());
            mentor.setPhone(phone.trim());
            mentor.setCourseID(courseID);
        }
        repository.insertMentor(mentor);
    }

    public void deleteMentor() {
        repository.deleteMentor(mentorData.getValue());
    }
}
