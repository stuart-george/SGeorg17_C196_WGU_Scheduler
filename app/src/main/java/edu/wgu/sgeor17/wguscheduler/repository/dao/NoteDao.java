package edu.wgu.sgeor17.wguscheduler.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Note;

@Dao
public interface NoteDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertNoteList(List<Note> notes);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM course_notes")
    void deleteAllNotes();

    @Query("DELETE FROM course_notes WHERE course_id = :courseID")
    void deleteAllNotesForCourse(int courseID);

    @Query("SELECT * FROM course_notes WHERE note_id = :noteID")
    Note getNoteByID(int noteID);

    @Query("SELECT * FROM course_notes ORDER BY created DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM course_notes WHERE course_id = :courseID ORDER BY created DESC")
    LiveData<List<Note>> getAllNotesForCourse(int courseID);

    @Query("SELECT COUNT(*) FROM course_notes")
    int getCount();
}
