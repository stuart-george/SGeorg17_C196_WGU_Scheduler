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
import edu.wgu.sgeor17.wguscheduler.model.Term;
import edu.wgu.sgeor17.wguscheduler.ui.term.TermDetailActivity;
import edu.wgu.sgeor17.wguscheduler.utility.Constants;
import edu.wgu.sgeor17.wguscheduler.utility.TextFormatting;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private List<Term> terms;
    private SimpleDateFormat dateFormat;

    public TermListAdapter(Context context, List<Term> terms) {
        this.context = context;
        this.terms = terms;
        this.inflater = LayoutInflater.from(context);
        this.dateFormat = TextFormatting.mediumDateFormat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_detail_content, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (terms != null) {
            final Term current = terms.get(position);
            holder.titleView.setText(current.getTitle());
            holder.summaryView.setText(
                    context.getString(
                            R.string.list_item_summary_format_term,
                            dateFormat.format(current.getStartDate()),
                            dateFormat.format(current.getEndDate())
                    ));
            holder.editFAB.setOnClickListener((view) -> {
                Intent intent = new Intent(context, TermDetailActivity.class);
                intent.putExtra(Constants.TERM_ID_KEY, current.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.terms.size();
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
