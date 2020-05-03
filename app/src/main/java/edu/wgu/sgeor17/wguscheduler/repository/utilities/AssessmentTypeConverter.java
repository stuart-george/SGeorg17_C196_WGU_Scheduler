package edu.wgu.sgeor17.wguscheduler.repository.utilities;

import androidx.room.TypeConverter;

import edu.wgu.sgeor17.wguscheduler.model.AssessmentType;

public class AssessmentTypeConverter {
    @TypeConverter
    public static AssessmentType toAssessmentType(int id) {return AssessmentType.valueOfId(id);}

    @TypeConverter
    public static int toAssessmentTypeID(AssessmentType type) {return type.getId();}
}
