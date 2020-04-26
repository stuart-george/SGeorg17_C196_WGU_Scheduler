package edu.wgu.sgeor17.wguscheduler.ui.course;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;
import edu.wgu.sgeor17.wguscheduler.utility.TextFormatting;

public class CourseDetailActivity extends AppCompatActivity {
    private boolean newCourse, editing;
    private CourseDetailViewModel viewModel;
    private SimpleDateFormat dateFormat;

    private EditText titleInput;
    private EditText startInput;
    private EditText endInput;
    private Spinner statusInput;

    private ImageView notesImageView;
    private ImageView assessmentImageView;
    private ImageView mentorImageView;

    private FloatingActionButton startDateFAB;
    private FloatingActionButton endDateFAB;
    private FloatingActionButton addNoteFAB;
    private FloatingActionButton addAssessmentFAB;
    private FloatingActionButton addMentorFAB;

    private int termID;
    private ArrayAdapter<CourseStatus> courseStatusAdapter;

    private String TAG = "CourseDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        dateFormat = TextFormatting.mediumDateFormat;

        titleInput = findViewById(R.id.course_title_text_input);
        startInput = findViewById(R.id.course_start_date_input);
        endInput = findViewById(R.id.course_end_date_input);
        statusInput = findViewById(R.id.course_status_spinner_input);
        startDateFAB = findViewById(R.id.course_start_date_fab);
        endDateFAB = findViewById(R.id.course_end_date_fab);
        addNoteFAB = findViewById(R.id.course_editor_add_note_fab);
        addAssessmentFAB = findViewById(R.id.course_editor_add_assessment_fab);
        addMentorFAB = findViewById(R.id.course_editor_add_mentor_fab);


        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(Constants.EDITING_KEY);
        }

        setButtonOnClick();
        initViewModel();
        enableAddButtons();
        addSpinnerItems();

    }

    private void addSpinnerItems() {
        courseStatusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                CourseStatus.values()
        );
        statusInput.setAdapter(courseStatusAdapter);
    }

    private CourseStatus getSpinnerValue() {
        return (CourseStatus) statusInput.getSelectedItem();
    }

    private int getSpinnerPosition(CourseStatus status) {
        return courseStatusAdapter.getPosition(status);
    }

    private void setButtonOnClick() {
        startDateFAB.setOnClickListener((myView) -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                startInput.setText(dateFormat.format(myCalendar.getTime()));
            };
            new DatePickerDialog(
                    this,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        endDateFAB.setOnClickListener((myView) -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                endInput.setText(dateFormat.format(myCalendar.getTime()));
            };
            new DatePickerDialog(
                    this,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        addNoteFAB.setOnClickListener((view) -> {
            // TODO: 4/25/2020 Set the page to load the note activity
        });

        addAssessmentFAB.setOnClickListener((view) -> {
            // TODO: 4/25/2020 Set the page to load the assessment activity
        });

        addMentorFAB.setOnClickListener((view) -> {
            // TODO: 4/25/2020 Set the page to load the mentor activity
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        viewModel.getCourseData().observe(this, course -> {
            if (course != null && !editing) {
                titleInput.setText(course.getTitle());
                startInput.setText(dateFormat.format(course.getStartDate()));
                endInput.setText(dateFormat.format(course.getEstEndDate()));
                int position = getSpinnerPosition(course.getStatus());
                statusInput.setSelection(position);
                termID = course.getTermID();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || extras.getInt(Constants.COURSE_ID_KEY, -1) == -1) {
            setTitle(getString(R.string.course_editor_title_new));
            newCourse = true;
            termID = extras == null ? -1 : extras.getInt(Constants.TERM_ID_KEY, -1);
        } else {
            setTitle(getString(R.string.course_editor__title_edit));
            int courseID = extras.getInt(Constants.COURSE_ID_KEY);
            viewModel.loadData(courseID);

        }

    }

    private void enableAddButtons() {
        addNoteFAB.setEnabled(!newCourse);
        addAssessmentFAB.setEnabled(!newCourse);
        addMentorFAB.setEnabled(!newCourse);

        if(!addNoteFAB.isEnabled()) {
            addNoteFAB.setVisibility(FloatingActionButton.INVISIBLE);
        } else {
            addNoteFAB.setVisibility(FloatingActionButton.VISIBLE);
        }

        if(!addAssessmentFAB.isEnabled()) {
            addAssessmentFAB.setVisibility(FloatingActionButton.INVISIBLE);
        } else {
            addAssessmentFAB.setVisibility(FloatingActionButton.VISIBLE);
        }

        if(!addMentorFAB.isEnabled()) {
            addMentorFAB.setVisibility(FloatingActionButton.INVISIBLE);
        } else {
            addMentorFAB.setVisibility(FloatingActionButton.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.course_editor_cancel_title);
        builder.setMessage(R.string.course_editor_cancel_message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.course_editor_cancel_yes), (dialog, id) -> {
            dialog.dismiss();
            super.onBackPressed();
        });
        builder.setNegativeButton(getString(R.string.course_editor_cancel_no), (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveAndReturn();
            return true;
        } else if (id == R.id.action_delete) {
            handleDelete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleDelete() {
        String courseTitle = viewModel.getCourseData().getValue().getTitle();
        Context context = MainActivity.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(context.getString(R.string.course_editor_delete_title, courseTitle));

        builder.setMessage(context.getString(
                R.string.course_editor_delete_message_no_associated_objects,
                courseTitle)
        );

        builder.setPositiveButton(R.string.course_editor_delete_yes, (dialog, id) -> {
            dialog.dismiss();
            viewModel.deleteCourse();
            finish();
        });

        builder.setNegativeButton(R.string.course_editor_delete_no, (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveAndReturn() {
        try {
            Date startDate = dateFormat.parse(startInput.getText().toString());
            Date endDate = dateFormat.parse(endInput.getText().toString());
            viewModel.saveCourse(
                    titleInput.getText().toString(),
                    startDate,
                    endDate,
                    getSpinnerValue(),
                    termID);
        } catch (ParseException e) {
            Log.e(TAG, "saveAndReturn: "+ e.getLocalizedMessage());
        }
        finish();
    }

}
