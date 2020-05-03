package edu.wgu.sgeor17.wguscheduler.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Assessment;

@Dao
public interface AssessmentDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(Assessment assessment);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAssessmentList(List<Assessment> assessments);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessments")
    void deleteAllAssessments();

    @Query("DELETE FROM assessments WHERE course_id = :courseID")
    void deleteAllAssessmentsForCourse(int courseID);

    @Query("UPDATE assessments SET course_id = -1 WHERE course_id = :courseID")
    void unAssignAllAssessmentsForCourse(int courseID);

    @Query("SELECT * FROM assessments WHERE assessment_id = :assessmentID")
    Assessment getAssessmentByID(int assessmentID);

    @Query("SELECT * FROM assessments ORDER BY assessment_date ASC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE course_id = -1 ORDER BY assessment_date ASC")
    LiveData<List<Assessment>> getAllUnAssignedAssessments();

    @Query("SELECT * FROM assessments WHERE course_id = :courseID ORDER BY assessment_date ASC")
    LiveData<List<Assessment>> getAllAssessmentsForCourse(int courseID);

    @Query("SELECT COUNT(*) FROM assessments")
    int getCount();

}
