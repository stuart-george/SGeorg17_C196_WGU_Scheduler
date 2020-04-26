package edu.wgu.sgeor17.wguscheduler.model;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;

public enum CourseStatus {
    IN_PROGRESS(0, R.string.course_status_in_progress),
    COMPLETE(1, R.string.course_status_completed),
    DROPPED(2, R.string.course_status_dropped),
    PLANNED (3, R.string.course_status_planned);

    private int id;
    private int resource;
    private CourseStatus(int id, int resourceID) {
        this.id = id;
        this.resource = resourceID;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MainActivity.getContext().getString(resource);
    }

    public static CourseStatus valueOfId(int id) {
        for (CourseStatus cs : values()) {
            if(cs.id == id) {
                return cs;
            }
        }
        return null;
    }

}
