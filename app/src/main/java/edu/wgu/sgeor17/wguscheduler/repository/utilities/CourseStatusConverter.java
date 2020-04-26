package edu.wgu.sgeor17.wguscheduler.repository.utilities;

import androidx.room.TypeConverter;

import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;

public class CourseStatusConverter {
    @TypeConverter
    public static CourseStatus toCourseStatus(int id) { return CourseStatus.valueOfId(id); }

    @TypeConverter
    public static int toStatusID(CourseStatus status) { return status.getId(); }
}
