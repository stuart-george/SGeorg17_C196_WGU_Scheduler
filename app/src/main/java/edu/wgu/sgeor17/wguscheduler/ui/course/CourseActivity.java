package edu.wgu.sgeor17.wguscheduler.ui.course;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Course;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.CourseListAdapter;

public class CourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Course> courseData = new ArrayList<>();
    private CourseListAdapter adapter;
    private CourseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.course_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        CourseActivity.this,
                        CourseDetailActivity.class);
                startActivity(intent);
            }
        });

        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.course_course_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Course>> courseObserver = (courses) -> {
            courseData.clear();
            courseData.addAll(courses);

            if(adapter == null) {
                adapter = new CourseListAdapter(CourseActivity.this, courseData);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        };

        viewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        viewModel.getAllCourses().observe(this, courseObserver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
