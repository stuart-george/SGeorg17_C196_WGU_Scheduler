package edu.wgu.sgeor17.wguscheduler.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Note;
import edu.wgu.sgeor17.wguscheduler.ui.note.NoteDetailActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<Note> notes;

    public NoteListAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_note_content, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.ViewHolder holder, int position) {
        if(notes != null) {
            final Note current = notes.get(position);
            holder.summaryView.setText(current.getText());
            holder.editFAB.setOnClickListener((view) ->  {
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra(Constants.NOTE_ID_KEY, current.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView summaryView;
        private final FloatingActionButton editFAB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            summaryView = itemView.findViewById(R.id.note_summary_text_view);
            editFAB = itemView.findViewById(R.id.note_edit_fab);
        }
    }
}
