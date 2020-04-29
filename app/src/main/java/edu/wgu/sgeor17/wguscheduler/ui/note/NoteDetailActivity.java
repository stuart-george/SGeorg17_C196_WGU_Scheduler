package edu.wgu.sgeor17.wguscheduler.ui.note;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;

public class NoteDetailActivity extends AppCompatActivity {

    private boolean newNote, editing;
    private EditText noteInput;
    private NoteDetailViewModel viewModel;
    private int courseID;

    private FloatingActionButton share_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);


        noteInput = findViewById(R.id.note_editor_note_input);
        share_fab = findViewById(R.id.share_fab);
        share_fab.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.note_editor_sharing_title));
            sendIntent.putExtra(Intent.EXTRA_TEXT, noteInput.getText().toString());

            Intent shareIntent = Intent.createChooser(sendIntent, "Share using:");
            startActivity(shareIntent);
        });

        initViewModel();
        enableButtons();

    }

    private void enableButtons() {
        share_fab.setEnabled(!newNote);
        if (!share_fab.isEnabled()) {
            share_fab.setVisibility(FloatingActionButton.INVISIBLE);
        } else {
            share_fab.setVisibility(FloatingActionButton.VISIBLE);
        }

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(NoteDetailViewModel.class);
        viewModel.getNoteData().observe(this, note -> {
            if (note != null && !editing) {
                noteInput.setText(note.getText());
                courseID = note.getCourseID();
            }
        });

        Bundle extras =  getIntent().getExtras();
        if (extras != null) {
            int noteId = extras.getInt(Constants.NOTE_ID_KEY, -1);

            if (noteId == -1) {
                setTitle(getString(R.string.note_editor_title_new));
                newNote = true;
                courseID = extras.getInt(Constants.COURSE_ID_KEY, -1);
            } else {
                setTitle(getString(R.string.note_editor_title_edit));
                viewModel.loadData(noteId);
            }

        } else {
            finish();
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

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (newNote) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setEnabled(false);
        }
        return true;
    }

    private void saveAndReturn() {
        viewModel.saveNote(noteInput.getText().toString(), courseID);
        finish();
    }

    private void handleDelete() {

    }
}
