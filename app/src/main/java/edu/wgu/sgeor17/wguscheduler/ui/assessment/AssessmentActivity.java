package edu.wgu.sgeor17.wguscheduler.ui.assessment;

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
import edu.wgu.sgeor17.wguscheduler.model.Assessment;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.AssessmentListAdapter;

public class AssessmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Assessment> assessmentData = new ArrayList<>();
    private AssessmentListAdapter adapter;
    private AssessmentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.assessment_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        AssessmentActivity.this,
                        AssessmentDetailActivity.class);
                startActivity(intent);
            }
        });

        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.assessment_assessment_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Assessment>> assessmentObserver = (assessments) -> {
            assessmentData.clear();
            assessmentData.addAll(assessments);
            if (adapter == null) {
                adapter = new AssessmentListAdapter(AssessmentActivity.this, assessmentData);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        };

        viewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        viewModel.getAllAssessments().observe(this, assessmentObserver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
