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
import edu.wgu.sgeor17.wguscheduler.model.Mentor;
import edu.wgu.sgeor17.wguscheduler.ui.assessment.AssessmentDetailActivity;
import edu.wgu.sgeor17.wguscheduler.ui.mentor.MentorDetailActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;

public class MentorListAdapter extends RecyclerView.Adapter<MentorListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<Mentor> mentors;

    public MentorListAdapter(Context context, List<Mentor> mentors) {
        this.context = context;
        this.mentors = mentors;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MentorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_detail_content, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorListAdapter.ViewHolder holder, int position) {
        if (mentors != null) {
            final Mentor current = mentors.get(position);
            holder.titleView.setText(
                    context.getString(
                            R.string.list_item_title_format_mentor,
                            current.getFirstName(),
                            current.getLastName()
                    ));
            holder.summaryView.setText(
                    context.getString(
                            R.string.list_item_title_format_mentor,
                            current.getEmail(),
                            current.getPhone()
                    ));
            holder.editFAB.setOnClickListener((view) -> {
                Intent intent = new Intent(context, MentorDetailActivity.class);
                intent.putExtra(Constants.MENTOR_ID_KEY, current.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView summaryView;
        private final FloatingActionButton editFAB;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title_text_view);
            summaryView = itemView.findViewById(R.id.summary_text_view);
            editFAB = itemView.findViewById(R.id.edit_fab);
        }
    }
}
