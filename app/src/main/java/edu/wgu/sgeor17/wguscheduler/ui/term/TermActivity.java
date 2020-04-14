package edu.wgu.sgeor17.wguscheduler.ui.term;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.ui.adapter.TermListAdapter;

public class TermActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Term> termData = new ArrayList<>();
    private TermListAdapter adapter;
    private TermViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.term_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermActivity.this, TermDetailActivity.class);
                startActivity(intent);
            }
        });

        initRecyclerView();
        initViewModel();
    }


    private void initRecyclerView() {
        recyclerView = findViewById(R.id.term_terms_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Term>> termObserver = (terms) -> {
            termData.clear();
            termData.addAll(terms);

            if (adapter == null) {
                adapter = new TermListAdapter(TermActivity.this, termData);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        };

        viewModel = new ViewModelProvider(this).get(TermViewModel.class);
        viewModel.getAllTerms().observe(this, termObserver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
