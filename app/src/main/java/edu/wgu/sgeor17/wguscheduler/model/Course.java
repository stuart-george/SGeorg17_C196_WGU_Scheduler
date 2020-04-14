package edu.wgu.sgeor17.wguscheduler.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "courses")
public class Course {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "course_id")
    private int id;
    private String title;
    @ColumnInfo (name = "start_date")
    private Date startDate;
    @ColumnInfo (name = "est_end_date")
    private Date estEndDate;
    private CourseStatus status;
    private int termID;

    @Ignore
    public Course(String title, Date startDate, Date estEndDate, CourseStatus status) {
        this.title = title;
        this.startDate = startDate;
        this.estEndDate = estEndDate;
        this.status = status;
    }

    @Ignore
    public Course(String title, Date startDate, Date estEndDate, CourseStatus status, int termID) {
        this.title = title;
        this.startDate = startDate;
        this.estEndDate = estEndDate;
        this.status = status;
        this.termID = termID;
    }

    public Course(int id, String title, Date startDate, Date estEndDate, CourseStatus status, int termID) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.estEndDate = estEndDate;
        this.status = status;
        this.termID = termID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEstEndDate() {
        return estEndDate;
    }

    public void setEstEndDate(Date estEndDate) {
        this.estEndDate = estEndDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }
}
