package com.scheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scheduler.R;
import com.scheduler.models.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    List<Reminder> reminderList;

    public ReminderAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReminderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reminder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        holder.titleTextView.setText(reminderList.get(position).getTitle());
        holder.timeTextView.setText(reminderList.get(position).getStartTime());
        String description = reminderList.get(position).getDescription();
        if (!description.equals(" ")) {
            holder.descriptionTextView.setText(description);
        } else {
            holder.descriptionTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, timeTextView, descriptionTextView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_textview);
            timeTextView = itemView.findViewById(R.id.time_textview);
            descriptionTextView = itemView.findViewById(R.id.description_textView);
        }
    }
}
