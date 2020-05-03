package edu.wgu.sgeor17.wguscheduler.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Mentor;

@Dao
public interface MentorDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(Mentor mentor);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMentorAll(List<Mentor> mentors);

    @Delete
    void deleteMentor(Mentor mentor);

    @Query("DELETE FROM mentors")
    void deleteAllMentors();

    @Query("DELETE FROM mentors WHERE course_id = :courseID")
    void deleteAllCourseMentors(int courseID);

    @Query("UPDATE mentors SET course_id = -1 WHERE course_id = :courseID")
    void unAssignAllCourseMentors (int courseID);

    @Query("SELECT * FROM mentors WHERE mentor_id = :id")
    Mentor getMentorByID (int id);

    @Query("SELECT * FROM mentors ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM mentors WHERE course_id = -1 ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Mentor>> getAllUnAssignedMentors();

    @Query("SELECT * FROM mentors WHERE course_id = :courseID ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Mentor>> getAllCourseMentors(int courseID);

    @Query("SELECT COUNT(*) FROM mentors")
    int getCount();
}
