package edu.wgu.sgeor17.wguscheduler.repository.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.wgu.sgeor17.wguscheduler.model.*;
import edu.wgu.sgeor17.wguscheduler.repository.dao.*;
import edu.wgu.sgeor17.wguscheduler.repository.utilities.*;

@Database(entities = {Term.class, Course.class, Note.class, Assessment.class}, version = 4, exportSchema = false)
@TypeConverters({DateConverter.class, CourseStatusConverter.class, AssessmentTypeConverter.class})
public abstract class AppRoomDatabase extends RoomDatabase {
    private static volatile  AppRoomDatabase INSTANCE;
    private static final String DATABASE_NAME = "WGU_Scheduler.db";

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract NoteDao noteDao();
    public abstract AssessmentDao assessmentDao();

    public static AppRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppRoomDatabase.class,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
