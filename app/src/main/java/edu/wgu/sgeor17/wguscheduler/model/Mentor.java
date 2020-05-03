package edu.wgu.sgeor17.wguscheduler.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "mentors")
public class Mentor {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "mentor_id")
    private int id;
    @ColumnInfo (name = "first_name")
    private String firstName;
    @ColumnInfo (name = "last_name")
    private String lastName;
    private String phone;
    private String email;
    @ColumnInfo (name = "course_id")
    private int courseID;

    @Ignore
    public Mentor(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    @Ignore
    public Mentor(String firstName, String lastName, String phone, String email, int courseID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.courseID = courseID;
    }

    public Mentor(int id, String firstName, String lastName, String phone, String email, int courseID) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.courseID = courseID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
