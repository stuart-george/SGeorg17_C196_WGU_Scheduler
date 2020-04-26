package edu.wgu.sgeor17.wguscheduler.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.repository.dao.CourseDao;
import edu.wgu.sgeor17.wguscheduler.repository.dao.TermDao;
import edu.wgu.sgeor17.wguscheduler.repository.persistence.AppRoomDatabase;

public class AppRepository {
    private static AppRepository INSTANCE;
    private Executor executor = Executors.newSingleThreadExecutor();

    private TermDao termDao;
    private CourseDao courseDao;

    private LiveData<List<Term>> terms;
    private LiveData<List<Course>> courses;

    private AppRepository(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getInstance(application.getApplicationContext());
        this.termDao = database.termDao();
        this.terms = termDao.getAllTerms();

        this.courseDao = database.courseDao();
        this.courses = courseDao.getAllCourses();

    }

    public static AppRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(application);
        }

        return INSTANCE;
    }

    //Term Methods
    public LiveData<List<Term>> getAllTerms() {
        return terms;
    }

    public Term getTermByID(int id) {
        return termDao.getTermByID(id);
    }

    public void insertTerm(Term term) {
        executor.execute(() -> termDao.insertTerm(term));
    }

    public void deleteTerm(Term term) {
        executor.execute(() -> termDao.deleteTerm(term));
    }

    //Course Methods
    public LiveData<List<Course>> getAllCourses() { return courses; }

    public LiveData<List<Course>> getAllCoursesForTerm(int termID) {
        return courseDao.getAllCoursesForTerm(termID);
    }

    public LiveData<List<Course>> getAllUnAssignedCourses() {
        return courseDao.getAllUnAssignedCourses();
    }

    public Course getCourseByID(int id)  {
        return courseDao.getCourseByID(id);
    }

    public void insertCourse(Course course) {
        executor.execute(() -> courseDao.insertCourse(course));
    }

    public void deleteCourse(Course course) {
        executor.execute(() -> courseDao.deleteCourse(course));
    }

    public void unAssignCoursesForTerm(int termID) {
        executor.execute(() -> courseDao.unAssignAllCoursesForTerm(termID));
    }
}
