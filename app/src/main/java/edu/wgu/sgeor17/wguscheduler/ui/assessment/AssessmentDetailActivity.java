package edu.wgu.sgeor17.wguscheduler.ui.assessment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.AssessmentType;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;
import edu.wgu.sgeor17.wguscheduler.utility.AlertBroadcastReceiver;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;
import edu.wgu.sgeor17.wguscheduler.utility.TextFormatting;

public class AssessmentDetailActivity extends AppCompatActivity {
    private boolean newAssessment, editing;
    private AssessmentDetailViewModel viewModel;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat requestCodeFormat;

    private EditText titleInput;
    private EditText dateInput;
    private Spinner typeInput;
    private Spinner courseInput;

    private FloatingActionButton dateFAB;

    private int courseID;
    private int assessmentID = 0;
    private int previousAssessmentRequestCode;
    private final Course UNASSIGNED_COURSE = Constants.UNASSIGNED_COURSE;

    private ArrayAdapter<AssessmentType> typeAdapter;
    private List<Course> courses = new ArrayList<>();
    private ArrayAdapter<Course> courseAdapter;

    private String TAG = "AssessmentDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        dateFormat = TextFormatting.mediumDateFormat;
        requestCodeFormat = TextFormatting.integerDateFormat;

        titleInput = findViewById(R.id.assessment_title_input);
        dateInput = findViewById(R.id.assessment_date_input);
        typeInput = findViewById(R.id.assessment_type_input);
        courseInput = findViewById(R.id.assessment_course_input);
        dateFAB = findViewById(R.id.assessment_date_fab);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(Constants.EDITING_KEY);
        }

        setButtonOnClick();
        addSpinnerItems();
        initViewModel();
    }

    private void setButtonOnClick() {
        dateFAB.setOnClickListener((myView) -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateInput.setText(dateFormat.format(myCalendar.getTime()));
            };
            new DatePickerDialog(
                    this,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
    }

    private void initViewModel() {
        final Observer<List<Course>> courseObserver = (courseEntities) -> {
            courses.clear();
            courses.add(UNASSIGNED_COURSE);
            courses.addAll(courseEntities);
            courseAdapter.notifyDataSetChanged();
        };

        viewModel = new ViewModelProvider(this).get(AssessmentDetailViewModel.class);
        viewModel.getCourseList().observe(this, courseObserver);

        viewModel.getAssessmentData().observe(this, assessment -> {
            if (assessment != null && !editing) {
                titleInput.setText(assessment.getTitle());
                dateInput.setText(dateFormat.format(assessment.getAssessmentDate()));
                int positionType = getTypeSpinnerPosition(assessment.getType());
                typeInput.setSelection(positionType);
                courseID = assessment.getCourseID();
                assessmentID = assessment.getId();
                previousAssessmentRequestCode = Integer.parseInt(requestCodeFormat.format(assessment.getAssessmentDate())+assessmentID);
                Course tempCourse = new Course(
                        courseID,
                        "",
                        new Date(),
                        new Date(),
                        CourseStatus.COMPLETE,
                        -1
                );
                int positionCourse = getCourseSpinnerPosition(tempCourse);
                courseInput.setSelection(positionCourse);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || extras.getInt(Constants.ASSESSMENT_ID_KEY, -1) == -1) {
            setTitle(getString(R.string.assessment_editor_title_new));
            newAssessment = true;
            courseID = extras == null ? -1 : extras.getInt(Constants.COURSE_ID_KEY, -1);
        } else {
            setTitle(getString(R.string.assessment_editor_title_edit));
            int assessmentID = extras.getInt(Constants.ASSESSMENT_ID_KEY);
            viewModel.loadData(assessmentID);
        }
    }


    private void addSpinnerItems() {
        typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                AssessmentType.values()
        );
        typeInput.setAdapter(typeAdapter);

        courseAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                courses);
        courseInput.setAdapter(courseAdapter);

        courseAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (!editing) {
                    Course tempCourse = new Course (courseID, "TEST", new Date(), new Date(), CourseStatus.COMPLETE, -1);
                    int position = courseAdapter.getPosition(tempCourse);
                    courseInput.setSelection(position);
                }
                super.onChanged();
            }
        });
    }

    private int getTypeSpinnerPosition(AssessmentType type) {
        return typeAdapter.getPosition(type);
    }

    private int getCourseSpinnerPosition(Course course) {
        return courseAdapter.getPosition(course);
    }

    private AssessmentType getTypeSpinnerValue() {
        return (AssessmentType) typeInput.getSelectedItem();
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
        builder.setTitle(R.string.assessment_editor_cancel_title);
        builder.setMessage(R.string.assessment_editor_cancel_message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.assessment_editor_cancel_yes), (dialog, id) -> {
            dialog.dismiss();
            super.onBackPressed();
        });
        builder.setNegativeButton(getString(R.string.assessment_editor_cancel_no), (dialog, id) -> {
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
        String assessmentTitle = titleInput.getText().toString();
        Context context = getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(context.getString(R.string.assessment_editor_delete_title, assessmentTitle));

        builder.setMessage(context.getString(
                R.string.assessment_editor_delete_message)
        );

        builder.setPositiveButton(R.string.assessment_editor_delete_yes, (dialog, id) -> {
            dialog.dismiss();
            viewModel.deleteAssessment();
            popToastNotification(getString(R.string.assessment_editor_notification_delete));
            finish();
        });

        builder.setNegativeButton(R.string.assessment_editor_delete_no, (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveAndReturn() {
        try {
            Date assessmentDate = dateFormat.parse(dateInput.getText().toString());
            Date today = getTodayDate();
            viewModel.saveAssessment(
                    titleInput.getText().toString(),
                    assessmentDate,
                    getTypeSpinnerValue(),
                    getCourseSpinnerValue().getId()
            );

            //Check against current date to prevent updates in title or completion triggering an alert.
            if(assessmentDate.after(today) || assessmentDate.equals(today)) {
                scheduleNotification(assessmentDate);
            }
            popToastNotification(getString(R.string.assessment_editor_notification_saved));
        } catch (ParseException e) {
            Log.e(TAG, "saveAndReturn: "+ e.getLocalizedMessage());
        }
        finish();

    }

    private void scheduleNotification(Date date) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int previousRequestCode;

        String tag = Constants.NOTIFICATION_ASSESSMENT_ID_TAG;
        String title = getString(R.string.notification_assessment_title);
        String message = getString(R.string.notification_assessment_message, titleInput.getText().toString(), dateFormat.format(date));

        int requestCode = Integer.parseInt(requestCodeFormat.format(date)+assessmentID);
        Intent notificationIntent = new Intent(this, AlertBroadcastReceiver.class);

        if(!newAssessment) {
            previousRequestCode = previousAssessmentRequestCode;
            Intent oldIntent = new Intent(this, AlertBroadcastReceiver.class);
            PendingIntent oldPendingIntent = PendingIntent.getBroadcast(this, previousRequestCode, oldIntent, 0);
            alarmManager.cancel(oldPendingIntent);

            notificationIntent.putExtra(Constants.ASSESSMENT_ID_KEY, assessmentID);
        }

        notificationIntent.putExtra(Constants.NOTIFICATION_ID_KEY, assessmentID);
        notificationIntent.putExtra(Constants.NOTIFICATION_TAG_KEY, tag);
        notificationIntent.putExtra(Constants.NOTIFICATION_TITLE_KEY, title);
        notificationIntent.putExtra(Constants.NOTIFICATION_MESSAGE_KEY, message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

    }

    private void popToastNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
