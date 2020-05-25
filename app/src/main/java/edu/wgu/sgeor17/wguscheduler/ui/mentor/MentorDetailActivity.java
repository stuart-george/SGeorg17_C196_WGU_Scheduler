package edu.wgu.sgeor17.wguscheduler.ui.mentor;

import android.app.AlertDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;

public class MentorDetailActivity extends AppCompatActivity {
    private boolean newMentor, editing;
    private MentorDetailViewModel viewModel;

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private Spinner courseInput;

    private int courseID;
//    private final Course UNASSIGNED_COURSE = Constants.UNASSIGNED_COURSE;

    private List<Course> courseList = new ArrayList<>();
    private ArrayAdapter<Course> courseArrayAdapter;

//    private String TAG = "MentorDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(Constants.EDITING_KEY);
        }

        bindViewsToFields();
        addSpinnerItems();
        initViewModel();

    }

    private void bindViewsToFields() {
        firstNameInput = findViewById(R.id.mentor_name_first_input);
        lastNameInput = findViewById(R.id.mentor_name_last_input);
        emailInput = findViewById(R.id.mentor_email_input);
        phoneInput = findViewById(R.id.mentor_phone_input);
        courseInput = findViewById(R.id.mentor_course_input);
    }

    private void initViewModel() {
        final Observer<List<Course>> courseObserver = (courses) -> {
            courseList.clear();
//            courseList.add(UNASSIGNED_COURSE);
            courseList.addAll(courses);
            courseArrayAdapter.notifyDataSetChanged();
        };

        viewModel = new ViewModelProvider(this).get(MentorDetailViewModel.class);
        viewModel.getCourseData().observe(this, courseObserver);

        viewModel.getMentorData().observe(this, mentor -> {
            if (mentor != null && !editing) {
                firstNameInput.setText(mentor.getFirstName());
                lastNameInput.setText(mentor.getLastName());
                emailInput.setText(mentor.getEmail());
                phoneInput.setText(mentor.getPhone());
                courseID = mentor.getCourseID();
                Date tempDate = new Date();
                Course temp = new Course(
                        courseID,
                        "Temp",
                        tempDate,
                        tempDate,
                        CourseStatus.COMPLETE,
                        Constants.DEFAULT_TERM_ID
                );
                int position = getCourseSpinnerPosition(temp);
                courseInput.setSelection(position);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null ||
                extras.getInt(
                        Constants.MENTOR_ID_KEY,
                        Constants.DEFAULT_MENTOR_ID)
                        == Constants.DEFAULT_MENTOR_ID) {
            setTitle("New Mentor");
            newMentor = true;
            courseID = extras == null ?
                    Constants.DEFAULT_COURSE_ID :
                    extras.getInt(Constants.COURSE_ID_KEY, Constants.DEFAULT_COURSE_ID);
        } else {
            setTitle("Edit Mentor");
            int mentorID = extras.getInt(Constants.MENTOR_ID_KEY);
            viewModel.loadData(mentorID);
        }
    }

    private void addSpinnerItems() {
        courseArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                courseList);
        courseInput.setAdapter(courseArrayAdapter);
        courseArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (!editing) {
                    Course temp = new Course(
                            courseID,
                            "Temp",
                            new Date(),
                            new Date(),
                            CourseStatus.COMPLETE,
                            Constants.DEFAULT_TERM_ID);
                    int position = getCourseSpinnerPosition(temp);
                    courseInput.setSelection(position);
                }
                super.onChanged();
            }
        });
    }

    private int getCourseSpinnerPosition(Course course) {
        return courseArrayAdapter.getPosition(course);
    }

    private Course getCourseSpinnerValue() {
        return (Course) courseInput.getSelectedItem();
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
        builder.setTitle(R.string.mentor_editor_cancel_title);
        builder.setMessage(R.string.mentor_editor_cancel_message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.mentor_editor_cancel_yes), (dialog, id) -> {
            dialog.dismiss();
            super.onBackPressed();
        });
        builder.setNegativeButton(getString(R.string.mentor_editor_cancel_no), (dialog, id) -> dialog.dismiss());
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

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (newMentor) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setEnabled(false);
        }
        return true;
    }

    private void handleDelete() {
        String mentorName = firstNameInput.getText().toString() +
                " " +
                lastNameInput.getText().toString();

        Context context = MainActivity.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setTitle(context.getString(R.string.mentor_editor_delete_title, mentorName));

        builder.setMessage(context.getString(R.string.mentor_editor_delete_message));

        builder.setPositiveButton(R.string.mentor_editor_delete_yes, (dialog, id) -> {
            dialog.dismiss();
            viewModel.deleteMentor();
            popNotificationToast(getString(R.string.mentor_editor_notification_delete, mentorName));
            finish();
        });

        builder.setNegativeButton(R.string.mentor_editor_delete_no, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveAndReturn() {
        viewModel.saveMentor(
                firstNameInput.getText().toString(),
                lastNameInput.getText().toString(),
                emailInput.getText().toString(),
                phoneInput.getText().toString(),
                getCourseSpinnerValue().getId());
        popNotificationToast(getString(
                R.string.mentor_editor_notification_saved,
                firstNameInput.getText().toString()));
        finish();
    }

    private void popNotificationToast (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
