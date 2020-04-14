package edu.wgu.sgeor17.wguscheduler.repository.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.model.Term;

@Dao
public interface TermDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertTerm(Term term);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertTermList(List<Term> terms);

    @Delete
    void deleteTerm(Term term);

    @Query("DELETE FROM terms")
    void deleteAllTerms();

    @Query("SELECT * FROM terms WHERE term_id = :id")
    Term getTermByID(int id);

    @Query("SELECT * FROM terms ORDER BY start_date DESC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT COUNT(*) FROM terms")
    int getCount();
}
