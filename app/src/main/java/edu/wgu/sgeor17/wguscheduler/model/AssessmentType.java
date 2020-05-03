package edu.wgu.sgeor17.wguscheduler.model;

import androidx.annotation.NonNull;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;

public enum AssessmentType {
    PERFORMANCE(0, R.string.assessment_type_performance),
    OBJECTIVE(1, R.string.assessment_type_objective);

    private int id;
    private int resource;

    AssessmentType(int id, int resourceID) {
        this.id = id;
        this.resource = resourceID;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return MainActivity.getContext().getString(resource);
    }

    public static AssessmentType valueOfId(int id) {
        for (AssessmentType at : values()) {
            if (at.id == id) {
                return at;
            }
        }
        return null;
    }
}
