package edu.wgu.sgeor17.wguscheduler.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Course;

@Dao
public interface CourseDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Course course);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertCourseList(List<Course> courses);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Query("DELETE FROM courses WHERE term_id = :termID")
    void deleteAllCoursesForTerm(int termID);

    @Query("UPDATE courses SET term_id = -1 WHERE term_id = :termID")
    void unAssignAllCoursesForTerm(int termID);

    @Query("SELECT * FROM courses WHERE course_id = :courseID")
    Course getCourseByID(int courseID);

    @Query("SELECT * FROM courses ORDER BY start_date DESC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE term_id = :termID ORDER BY start_date DESC")
    LiveData<List<Course>> getAllCoursesForTerm(int termID);

    @Query("SELECT * FROM courses WHERE term_id = -1 ORDER BY start_date DESC")
    LiveData<List<Course>> getAllUnAssignedCourses();

    @Query("SELECT COUNT(*) FROM courses")
    int getCount();

    @Query("SELECT * FROM courses ORDER BY course_id DESC LIMIT 1")
    Course getLastInsertedCourse();
}
