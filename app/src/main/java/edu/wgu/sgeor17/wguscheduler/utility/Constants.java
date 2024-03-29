package edu.wgu.sgeor17.wguscheduler.utility;

import java.util.Date;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;
import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;

public class Constants {
    public static final int NEW_ACTIVITY_REQUEST_COD = 1;
    public static final String TERM_ID_KEY = "term_id_key";
    public static final String COURSE_ID_KEY = "course_id_key";
    public static final String ASSESSMENT_ID_KEY = "assessment_id_key";
    public static final String MENTOR_ID_KEY = "mentor_id_key";
    public static final String NOTE_ID_KEY = "note_id_key";
    public static final String EDITING_KEY = "editing_key";
    public static final String NOTIFICATION_ID_KEY = "notification_id_key";
    public static final String NOTIFICATION_TITLE_KEY = "notification_title";
    public static final String NOTIFICATION_MESSAGE_KEY = "notification_message";
    public static final String NOTIFICATION_TAG_KEY = "notification_tag";
    public static final String NOTIFICATION_COURSE_START_ID_TAG = "course_start";
    public static final String NOTIFICATION_COURSE_END_ID_TAG = "course_end";
    public static final String NOTIFICATION_ASSESSMENT_ID_TAG = "assessment";

    public static final int DEFAULT_TERM_ID = -1;
    public static final int DEFAULT_COURSE_ID = -1;
    public static final int DEFAULT_ASSESSMENT_ID = -1;
    public static final int DEFAULT_NOTE_ID = -1;
    public static final int DEFAULT_MENTOR_ID = -1;

    public static final Term UNASSIGNED_TERM = new Term (
            DEFAULT_TERM_ID,
            MainActivity.getContext().getString(R.string.term_unassigned),
            new Date(),
            new Date()
    );
    public static final Course UNASSIGNED_COURSE = new Course(
            DEFAULT_COURSE_ID,
            MainActivity.getContext().getString(R.string.course_unassigned),
            new Date(),
            new Date(),
            CourseStatus.IN_PROGRESS,
            DEFAULT_TERM_ID);
}
