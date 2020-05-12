package edu.wgu.sgeor17.wguscheduler.ui.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.ui.assessment.AssessmentActivity;
import edu.wgu.sgeor17.wguscheduler.ui.course.CourseActivity;
import edu.wgu.sgeor17.wguscheduler.ui.term.TermActivity;

public class MainActivity extends AppCompatActivity {
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();

        setButtonClickActions();



    }

    private void setButtonClickActions() {
        Button termButton = findViewById(R.id.main_term_button);
        termButton.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, TermActivity.class);
            startActivity(intent);
        });

        Button courseButton = findViewById(R.id.main_course_button);
        courseButton.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, CourseActivity.class);
            startActivity(intent);
        });
        Button assessmentButton = findViewById(R.id.main_assessment_button);
        assessmentButton.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, AssessmentActivity.class);
            startActivity(intent);
        });
    }

    public static Context getContext() {
        return context;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String default_channel_id = getString(R.string.default_channel_id);
            CharSequence default_name = getString(R.string.default_channel_name);
            String default_description = getString(R.string.default_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel default_channel = new NotificationChannel(default_channel_id, default_name, importance);
            default_channel.setDescription(default_description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(default_channel);
        }
    }
}
