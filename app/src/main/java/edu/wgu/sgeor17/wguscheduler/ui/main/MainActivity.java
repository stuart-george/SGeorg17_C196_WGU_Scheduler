package edu.wgu.sgeor17.wguscheduler.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.AssessmentType;
import edu.wgu.sgeor17.wguscheduler.ui.assessment.AssessmentActivity;
import edu.wgu.sgeor17.wguscheduler.ui.assessment.AssessmentDetailActivity;
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

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public static Context getContext() {
        return context;
    }
}
