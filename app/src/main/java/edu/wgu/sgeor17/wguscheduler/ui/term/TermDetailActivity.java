package edu.wgu.sgeor17.wguscheduler.ui.term;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.CourseListAdapter;
import edu.wgu.sgeor17.wguscheduler.ui.course.CourseDetailActivity;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;
import edu.wgu.sgeor17.wguscheduler.utility.TextFormatting;

public class TermDetailActivity extends AppCompatActivity {
    private boolean newTerm, editing;
    private TermDetailViewModel viewModel;
    private SimpleDateFormat dateFormat;
    private int termID;

    private RecyclerView recyclerView;
    private CourseListAdapter adapter;
    private List<Course> courseData = new ArrayList<>();

    private EditText termTitleInput;
    private EditText termStartDateInput;
    private EditText termEndDateInput;
    private FloatingActionButton startFAB;
    private FloatingActionButton endFAB;
    private FloatingActionButton addCourseFAB;

    private String TAG = "TermDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        dateFormat = TextFormatting.mediumDateFormat;

        termTitleInput = findViewById(R.id.term_title_input);
        termStartDateInput = findViewById(R.id.term_start_date_input);
        termEndDateInput = findViewById(R.id.term_end_date_input);
        startFAB = findViewById(R.id.term_start_date_fab);
        endFAB = findViewById(R.id.term_end_date_fab);
        addCourseFAB = findViewById(R.id.term_editor_add_course_fab);

        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(Constants.EDITING_KEY);
        }

        setButtonOnClick();
        initRecyclerView();
        initViewModel();
        enableCourseAddButton();
    }

    private void enableCourseAddButton() {
        addCourseFAB.setEnabled(!newTerm);
        if(!addCourseFAB.isEnabled()) {
            addCourseFAB.setVisibility(FloatingActionButton.INVISIBLE);
        }

    }

    private void setButtonOnClick() {
        startFAB.setOnClickListener((myView) -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                termStartDateInput.setText(dateFormat.format(myCalendar.getTime()));
            };
            new DatePickerDialog(
                    this,
                    date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        endFAB.setOnClickListener((myView) -> {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                termEndDateInput.setText(dateFormat.format(myCalendar.getTime()));
            };
            new DatePickerDialog(
                    this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        addCourseFAB.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra(Constants.TERM_ID_KEY, viewModel.getTermData().getValue().getId());
            startActivity(intent);
        });
    }

    private void initViewModel() {
        final Observer<List<Course>> courseObserver = (courses) -> {
            courseData.clear();
            courseData.addAll(courses);

            if (adapter == null) {
                adapter = new CourseListAdapter(TermDetailActivity.this, courseData);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        };

        viewModel = new ViewModelProvider(this).get(TermDetailViewModel.class);
        viewModel.getTermData().observe(this, term -> {
            if (term != null && !editing) {
                termTitleInput.setText(term.getTitle());
                termStartDateInput.setText(dateFormat.format(term.getStartDate()));
                termEndDateInput.setText(dateFormat.format(term.getEndDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.term_editor_title_new));
            newTerm = true;
        } else {
            setTitle(getString(R.string.term_editor_title_edit));
            termID = extras.getInt(Constants.TERM_ID_KEY);
            viewModel.loadData(termID);
            viewModel.getCourseData().observe(this, courseObserver);
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.term_course_list_recycle_viewer);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.term_editor_cancel_title);
        builder.setMessage(R.string.term_editor_cancel_message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.term_editor_cancel_yes_button), (dialog, id) -> {
            dialog.dismiss();
            super.onBackPressed();
        });
        builder.setNegativeButton(getString(R.string.term_editor_cancel_no), (dialog, id) -> {
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

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (newTerm) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setEnabled(false);
        }
        return true;
    }

    private void saveAndReturn() {
        try {
            Date startDate = dateFormat.parse(termStartDateInput.getText().toString());
            Date endDate = dateFormat.parse(termEndDateInput.getText().toString());
            viewModel.saveTerm(termTitleInput.getText().toString(), startDate, endDate);
        } catch (ParseException e) {
            Log.e(TAG, "saveAndReturn: "+ e.getLocalizedMessage());
        }
        finish();
    }

    private void handleDelete() {
        String termTitle = viewModel.getTermData().getValue().getTitle();
        Context context = MainActivity.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(context.getString(R.string.term_editor_delete_title, termTitle));

        builder.setMessage(context.getString(
                R.string.term_editor_delete_message_no_courses,
                termTitle)
        );

        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();
            viewModel.deleteTerm();
            finish();
        });

        builder.setNegativeButton("Cancel", (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
