package edu.wgu.sgeor17.wguscheduler.ui.course;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Assessment;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.model.CourseStatus;
import edu.wgu.sgeor17.wguscheduler.model.Mentor;
import edu.wgu.sgeor17.wguscheduler.model.Note;
import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.AssessmentListAdapter;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.MentorListAdapter;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.NoteListAdapter;
import edu.wgu.sgeor17.wguscheduler.ui.assessment.AssessmentDetailActivity;
import edu.wgu.sgeor17.wguscheduler.ui.main.MainActivity;
import edu.wgu.sgeor17.wguscheduler.ui.mentor.MentorDetailActivity;
import edu.wgu.sgeor17.wguscheduler.ui.note.NoteDetailActivity;
import edu.wgu.sgeor17.wguscheduler.utility.AlertBroadcastReceiver;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;
import edu.wgu.sgeor17.wguscheduler.utility.TextFormatting;

public class CourseDetailActivity extends AppCompatActivity {
    private enum NotificationType {
        START,
        END
    }
    private boolean newCourse, editing;
    private boolean noteExpanded, mentorExpanded, assessmentExpanded;
    private CourseDetailViewModel viewModel;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat requestCodeFormat;

    private EditText titleInput;
    private EditText startInput;
    private EditText endInput;
    private Spinner statusInput;
    private Spinner termInput;

    private FloatingActionButton startDateFAB;
    private FloatingActionButton endDateFAB;
    private FloatingActionButton addNoteFAB;
    private FloatingActionButton addAssessmentFAB;
    private FloatingActionButton addMentorFAB;

    private ImageButton noteHideButton;
    private ImageButton mentorHideButton;
    private ImageButton assessmentHideButton;

    private RecyclerView noteRecyclerView;
    private RecyclerView mentorRecyclerView;
    private RecyclerView assessmentRecyclerView;

    private int courseID = 0;
    private int termID;
    private final Term UNASSIGNED_TERM = Constants.UNASSIGNED_TERM;
    private ArrayAdapter<CourseStatus> courseStatusAdapter;
    private ArrayAdapter<Term> termAdapter;

    private NoteListAdapter noteAdapter;
    private List<Note> noteData = new ArrayList<>();
    private AssessmentListAdapter assessmentAdapter;
    private List<Assessment> assessmentData = new ArrayList<>();
    private MentorListAdapter mentorAdapter;
    private List<Mentor> mentorData = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();


    private int previousStartRequestCode;
    private int previousEndRequestCode;

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
        requestCodeFormat = TextFormatting.integerDateFormat;

        titleInput = findViewById(R.id.course_title_text_input);
        startInput = findViewById(R.id.course_start_date_input);
        endInput = findViewById(R.id.course_end_date_input);
        statusInput = findViewById(R.id.course_status_spinner_input);
        termInput = findViewById(R.id.course_term_spinner_input);
        startDateFAB = findViewById(R.id.course_start_date_fab);
        endDateFAB = findViewById(R.id.course_end_date_fab);
        addNoteFAB = findViewById(R.id.course_editor_add_note_fab);
        addAssessmentFAB = findViewById(R.id.course_editor_add_assessment_fab);
        addMentorFAB = findViewById(R.id.course_editor_add_mentor_fab);
        noteHideButton = findViewById(R.id.course_notes_title_image_view);
        mentorHideButton = findViewById(R.id.course_mentor_title_image_view);
        assessmentHideButton = findViewById(R.id.course_assessment_title_image_view);



        if (savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(Constants.EDITING_KEY);
        }


        initRecyclerView();
        setButtonOnClick();
        enableAddButtons();
        addSpinnerItems();
        initViewModel();

    }

    private void addSpinnerItems() {
        courseStatusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                CourseStatus.values()
        );
        statusInput.setAdapter(courseStatusAdapter);

        termAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                terms
        );
        termInput.setAdapter(termAdapter);

        termAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if(!editing) {
                    Term tempTerm = new Term (termID, "TEST", new Date(), new Date());
                    int position = termAdapter.getPosition(tempTerm);
                    termInput.setSelection(position);
                }
                super.onChanged();
            }
        });
    }

    private CourseStatus getSpinnerValue() {
        return (CourseStatus) statusInput.getSelectedItem();
    }

    private int getSpinnerPosition(CourseStatus status) {
        return courseStatusAdapter.getPosition(status);
    }

    private int getTermSpinnerPosition(Term term) {
        return termAdapter.getPosition(term);
    }

    private Term getTermSpinnerValue() {
        return (Term) termInput.getSelectedItem();
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
            Intent intent = new Intent(this, NoteDetailActivity.class);
            intent.putExtra(Constants.COURSE_ID_KEY, viewModel.getCourseData().getValue().getId());
            startActivity(intent);
        });

        addAssessmentFAB.setOnClickListener((view) -> {
            Intent intent = new Intent(this, AssessmentDetailActivity.class);
            intent.putExtra(Constants.COURSE_ID_KEY, viewModel.getCourseData().getValue().getId());
            startActivity(intent);
        });

        addMentorFAB.setOnClickListener((view) -> {
            Intent intent = new Intent(this, MentorDetailActivity.class);
            intent.putExtra(Constants.COURSE_ID_KEY, viewModel.getCourseData().getValue().getId());
            startActivity(intent);
        });

        noteHideButton.setOnClickListener((view) -> {
            noteExpanded = noteRecyclerView.getVisibility() == View.VISIBLE;
            if (noteExpanded) {
                setNoteVisibility(false);
            } else {
                setNoteVisibility(true);
                setMentorVisibility(false);
                setAssessmentVisibility(false);
            }
        });

        mentorHideButton.setOnClickListener((view) -> {
            mentorExpanded = mentorRecyclerView.getVisibility() == View.VISIBLE;
            if (mentorExpanded) {
                setMentorVisibility(false);
            } else {
                setMentorVisibility(true);
                setNoteVisibility(false);
                setAssessmentVisibility(false);
            }

        });

        assessmentHideButton.setOnClickListener((view) -> {
            assessmentExpanded = assessmentRecyclerView.getVisibility() == View.VISIBLE;

            if (assessmentExpanded) {
                setAssessmentVisibility(false);
            } else {
                setAssessmentVisibility(true);
                setNoteVisibility(false);
                setMentorVisibility(false);
            }
        });
    }

    private void setNoteVisibility(boolean expandSection) {
        if(expandSection) {
            noteRecyclerView.setVisibility(View.VISIBLE);
            noteHideButton.setImageResource(R.drawable.ic_expand_less);
        } else {
            noteRecyclerView.setVisibility(View.GONE);
            noteHideButton.setImageResource(R.drawable.ic_expand_more);
        }
    }

    private void setMentorVisibility(boolean expandSection) {
        if (expandSection) {
            mentorRecyclerView.setVisibility(View.VISIBLE);
            mentorHideButton.setImageResource(R.drawable.ic_expand_less);
        } else {
            mentorRecyclerView.setVisibility(View.GONE);
            mentorHideButton.setImageResource(R.drawable.ic_expand_more);
        }

    }

    private void setAssessmentVisibility(boolean expandSection) {
        if (expandSection) {
            assessmentRecyclerView.setVisibility(ImageButton.VISIBLE);
            assessmentHideButton.setImageResource(R.drawable.ic_expand_less);
        } else {
            assessmentRecyclerView.setVisibility(ImageButton.GONE);
            assessmentHideButton.setImageResource(R.drawable.ic_expand_more);
        }
    }

    private void initRecyclerView() {
        noteRecyclerView = findViewById(R.id.course_note_recycler_view);
        noteRecyclerView.setHasFixedSize(true);
        LinearLayoutManager noteLayoutManager = new LinearLayoutManager(this);
        noteRecyclerView.setLayoutManager(noteLayoutManager);

        mentorRecyclerView = findViewById(R.id.course_mentor_recycler_view);
        mentorRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mentorLayoutManager = new LinearLayoutManager(this);
        mentorRecyclerView.setLayoutManager(mentorLayoutManager);

        assessmentRecyclerView = findViewById(R.id.course_assessment_recycler_view);
        assessmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager assessmentLayoutManager = new LinearLayoutManager(this);
        assessmentRecyclerView.setLayoutManager(assessmentLayoutManager);
    }

    private void initViewModel() {
        final Observer<List<Term>> termObserver = (termEntities) -> {
            terms.clear();
            terms.add(UNASSIGNED_TERM);
            terms.addAll(termEntities);
            termAdapter.notifyDataSetChanged();
        };

        final Observer<List<Note>> noteObserver = (notes) -> {
            noteData.clear();
            noteData.addAll(notes);

            if (noteAdapter == null) {
                noteAdapter = new NoteListAdapter(
                        CourseDetailActivity.this,
                        noteData);
                noteRecyclerView.setAdapter(noteAdapter);
            } else {
                noteAdapter.notifyDataSetChanged();
            }
        };

        final Observer<List<Assessment>> assessmentObserver = (assessments) -> {
            assessmentData.clear();
            assessmentData.addAll(assessments);

            if (assessmentAdapter == null) {
                assessmentAdapter = new AssessmentListAdapter(
                        CourseDetailActivity.this,
                        assessmentData);
                assessmentRecyclerView.setAdapter(assessmentAdapter);
            } else {
                assessmentAdapter.notifyDataSetChanged();
            }
        };

        final Observer<List<Mentor>> mentorObserver = (mentors) -> {
            mentorData.clear();
            mentorData.addAll(mentors);

            if (mentorAdapter == null) {
                mentorAdapter = new MentorListAdapter(
                        CourseDetailActivity.this,
                        mentorData);
                mentorRecyclerView.setAdapter(mentorAdapter);
            } else {
                mentorAdapter.notifyDataSetChanged();
            }
        };

        viewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        viewModel.getTermData().observe(this, termObserver);

        viewModel.getCourseData().observe(this, course -> {
            if (course != null && !editing) {
                titleInput.setText(course.getTitle());
                startInput.setText(dateFormat.format(course.getStartDate()));
                endInput.setText(dateFormat.format(course.getEstEndDate()));
                int position = getSpinnerPosition(course.getStatus());
                statusInput.setSelection(position);
                termID = course.getTermID();
                courseID = course.getId();
                previousStartRequestCode = Integer.parseInt(requestCodeFormat.format(course.getStartDate())+courseID);
                previousEndRequestCode = Integer.parseInt(requestCodeFormat.format(course.getEstEndDate())+courseID);

                Term tempTerm = new Term(termID, "TEST", new Date(), new Date());
                int positionTerm = getTermSpinnerPosition(tempTerm);
                termInput.setSelection(positionTerm);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null || extras.getInt(Constants.COURSE_ID_KEY, Constants.DEFAULT_COURSE_ID) == -1) {
            setTitle(getString(R.string.course_editor_title_new));
            newCourse = true;
            termID = extras == null ? -1 : extras.getInt(Constants.TERM_ID_KEY, -Constants.DEFAULT_TERM_ID);
        } else {
            setTitle(getString(R.string.course_editor__title_edit));
            int courseID = extras.getInt(Constants.COURSE_ID_KEY);
            viewModel.loadData(courseID);
            viewModel.getNoteData().observe(this, noteObserver);
            viewModel.getAssessmentsData().observe(this, assessmentObserver);
            viewModel.getMentorData().observe(this, mentorObserver);
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
            Date today = getTodayDate();
            viewModel.saveCourse(
                    titleInput.getText().toString(),
                    startDate,
                    endDate,
                    getSpinnerValue(),
                    getTermSpinnerValue().getId());

            //Logic checks against current date in order to prevent unnecessary alerts for past dates.
            if(startDate.after(today) || startDate.equals(today)) {
                scheduleNotification(NotificationType.START, startDate);
            }

            if(endDate.after(today) || endDate.equals(today)) {
                scheduleNotification(NotificationType.END, endDate);
            }


        } catch (ParseException e) {
            Log.e(TAG, "saveAndReturn: "+ e.getLocalizedMessage());
        }
        finish();
    }

    private void scheduleNotification (NotificationType type, Date date) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        String tag = "default";
        String title = "No title set";
        String message = "No Message set";
        int requestCode = 0;
        int previousRequestCode = 0;

        switch (type) {
            case START:
                tag = Constants.NOTIFICATION_COURSE_START_ID_TAG;
                title = getString(R.string.notification_course_title_start);
                message = getString(R.string.notification_course_message_start, titleInput.getText(), dateFormat.format(date));
                break;
            case END:
                tag = Constants.NOTIFICATION_COURSE_END_ID_TAG;
                title = getString(R.string.notification_course_title_end);
                message = getString(R.string.notification_course_message_end, titleInput.getText(), dateFormat.format(date));
                break;
        }

        requestCode = Integer.parseInt(requestCodeFormat.format(date)+courseID);
        Intent notificationIntent = new Intent(this, AlertBroadcastReceiver.class);

        if (!newCourse) {
            if(type == NotificationType.START) {
                previousRequestCode = previousStartRequestCode;
            } else {
                previousRequestCode = previousEndRequestCode;
            }
            Intent oldIntent = new Intent(this, AlertBroadcastReceiver.class);
            PendingIntent oldPendingIntent = PendingIntent.getBroadcast(this, previousRequestCode, oldIntent, 0);
            alarmManager.cancel(oldPendingIntent);

            //Set Course ID for future click action if not a new course
            notificationIntent.putExtra(Constants.COURSE_ID_KEY, courseID);
        }

        notificationIntent.putExtra(Constants.NOTIFICATION_ID_KEY, courseID);
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

    private Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
