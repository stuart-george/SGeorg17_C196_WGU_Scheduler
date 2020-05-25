package edu.wgu.sgeor17.wguscheduler.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "course_notes")
public class Note {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "note_id")
    private int id;
    private Date created;
    @ColumnInfo (name = "note_text")
    private String text;
    @ColumnInfo (name = "course_id")
    private int courseID;

    @Ignore
    public Note(Date created, String text, int courseID) {
        this.created = created;
        this.text = text;
        this.courseID = courseID;
    }

    public Note(int id, Date created, String text, int courseID) {
        this.id = id;
        this.created = created;
        this.text = text;
        this.courseID = courseID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Note id:" + id;
    }
}
