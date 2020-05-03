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

import java.text.SimpleDateFormat;
import java.util.List;

import edu.wgu.sgeor17.wguscheduler.R;
import edu.wgu.sgeor17.wguscheduler.model.Assessment;
import edu.wgu.sgeor17.wguscheduler.ui.assessment.AssessmentDetailActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;
import edu.wgu.sgeor17.wguscheduler.utility.TextFormatting;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<Assessment> assessments;
    private SimpleDateFormat dateFormat;

    public AssessmentListAdapter(Context context, List<Assessment> assessments) {
        this.context = context;
        this.assessments = assessments;
        this.inflater = LayoutInflater.from(context);
        this.dateFormat = TextFormatting.mediumDateFormat;
    }

    @NonNull
    @Override
    public AssessmentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_detail_content, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentListAdapter.ViewHolder holder, int position) {
        if (assessments != null) {
            final Assessment current = assessments.get(position);
            holder.titleView.setText(current.getTitle());
            holder.summaryView.setText(
                    context.getString(
                            R.string.list_item_summary_format_assessment,
                            current.getType().toString(),
                            dateFormat.format(current.getAssessmentDate())
                    ));
            holder.editFAB.setOnClickListener((view) -> {
                Intent intent = new Intent(context, AssessmentDetailActivity.class);
                intent.putExtra(Constants.ASSESSMENT_ID_KEY, current.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return assessments.size();
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
