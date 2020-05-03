package edu.wgu.sgeor17.wguscheduler.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.wgu.sgeor17.wguscheduler.model.Assessment;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.Mentor;
import edu.wgu.sgeor17.wguscheduler.model.Note;
import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.repository.dao.AssessmentDao;
import edu.wgu.sgeor17.wguscheduler.repository.dao.CourseDao;
import edu.wgu.sgeor17.wguscheduler.repository.dao.MentorDao;
import edu.wgu.sgeor17.wguscheduler.repository.dao.NoteDao;
import edu.wgu.sgeor17.wguscheduler.repository.dao.TermDao;
import edu.wgu.sgeor17.wguscheduler.repository.persistence.AppRoomDatabase;

public class AppRepository {
    private static AppRepository INSTANCE;
    private Executor executor = Executors.newSingleThreadExecutor();

    private TermDao termDao;
    private CourseDao courseDao;
    private NoteDao noteDao;
    private AssessmentDao assessmentDao;
    private MentorDao mentorDao;

    private LiveData<List<Term>> terms;
    private LiveData<List<Course>> courses;
    private LiveData<List<Note>> notes;
    private LiveData<List<Assessment>> assessments;
    private LiveData<List<Mentor>> mentors;

    private AppRepository(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getInstance(application.getApplicationContext());
        this.termDao = database.termDao();
        this.terms = termDao.getAllTerms();

        this.courseDao = database.courseDao();
        this.courses = courseDao.getAllCourses();

        this.noteDao = database.noteDao();
        this.notes = noteDao.getAllNotes();

        this.assessmentDao = database.assessmentDao();
        this.assessments = assessmentDao.getAllAssessments();

        this.mentorDao = database.mentorDao();
        this.mentors = mentorDao.getAllMentors();
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

    //Note Methods
    public LiveData<List<Note>> getAllNotes() {
        return notes;
    }

    public LiveData<List<Note>> getAllNotesForCourse(int courseID) {
        return noteDao.getAllNotesForCourse(courseID);
    }

    public Note getNoteByID (int noteID) {
        return noteDao.getNoteByID(noteID);
    }

    public void insertNote(Note note) {
        executor.execute(() -> noteDao.insertNote(note));
    }

    public void deleteNote(Note note) {
        executor.execute(() -> noteDao.deleteNote(note));
    }

    public void deleteAllNotesForCourse(int courseID) {
        executor.execute(() -> noteDao.deleteAllNotesForCourse(courseID));
    }

    //Assessment Methods
    public LiveData<List<Assessment>> getAllAssessments() {return assessments; }

    public LiveData<List<Assessment>> getAllAssessmentsForCourse(int courseID) {
        return  assessmentDao.getAllAssessmentsForCourse(courseID);
    }

    public LiveData<List<Assessment>> getAllUnassignedAssessments() {
        return assessmentDao.getAllUnAssignedAssessments();
    }

    public Assessment getAssessmentByID (int assessmentID) {
        return assessmentDao.getAssessmentByID(assessmentID);
    }

    public void insertAssessment(Assessment assessment) {
        executor.execute(() -> assessmentDao.insertAssessment(assessment));
    }

    public void deleteAssessment(Assessment assessment) {
        executor.execute(() -> assessmentDao.deleteAssessment(assessment));
    }

    public void deleteAllAssessmentsForCourse (int courseID) {
        executor.execute(() -> assessmentDao.deleteAllAssessmentsForCourse(courseID));
    }

    public void unAssignAllAssessmentsForCourse(int courseID) {
        executor.execute(() -> assessmentDao.unAssignAllAssessmentsForCourse(courseID));
    }

    //Mentor Methods
    public LiveData<List<Mentor>> getAllMentors() {
        return mentors;
    }

    public LiveData<List<Mentor>> getAllCourseMentors(int courseID) {
        return mentorDao.getAllCourseMentors(courseID);
    }

    public LiveData<List<Mentor>> getAllUnAssignedMentors() {
        return mentorDao.getAllUnAssignedMentors();
    }

    public Mentor getMentorByID (int mentorID) {
        return mentorDao.getMentorByID(mentorID);
    }

    public void insertMentor(Mentor mentor) {
        executor.execute(() -> mentorDao.insertMentor(mentor));
    }

    public void deleteMentor(Mentor mentor) {
        executor.execute(() -> mentorDao.deleteMentor(mentor));
    }

    public void deleteAllCourseMentors(int courseID) {
        executor.execute(() -> mentorDao.getAllCourseMentors(courseID));
    }

    public void unAssignAllCourseMentors(int courseID) {
        executor.execute(() -> mentorDao.unAssignAllCourseMentors(courseID));
    }

}
