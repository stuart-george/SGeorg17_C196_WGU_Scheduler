package edu.wgu.sgeor17.wguscheduler.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "assessments")
public class Assessment {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "assessment_id")
    private int id;
    private String title;
    @ColumnInfo (name = "assessment_date")
    private Date assessmentDate;
    private AssessmentType type;
    @ColumnInfo (name = "course_id")
    private int courseID;

    @Ignore
    public Assessment(String title, Date assessmentDate, AssessmentType type) {
        this.title = title;
        this.assessmentDate = assessmentDate;
        this.type = type;
    }

    @Ignore
    public Assessment(String title, Date assessmentDate, AssessmentType type, int courseID) {
        this.title = title;
        this.assessmentDate = assessmentDate;
        this.type = type;
        this.courseID = courseID;
    }

    public Assessment(int id, String title, Date assessmentDate, AssessmentType type, int courseID) {
        this.id = id;
        this.title = title;
        this.assessmentDate = assessmentDate;
        this.type = type;
        this.courseID = courseID;
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

    public Date getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Assessment:" + title;
    }
}
