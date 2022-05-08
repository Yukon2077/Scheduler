package com.scheduler.adapters;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.scheduler.R;
import com.scheduler.activities.SelectPeopleActivity;
import com.scheduler.models.People;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    List<People> peopleList;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public PeopleAdapter(List<People> peopleList) {
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public PeopleAdapter.PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_people, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.PeopleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameTextView.setText(peopleList.get(position).getName());
        holder.numberTextView.setText(peopleList.get(position).getNumber());
        holder.checkBox.setChecked(itemStateArray.get(position, false));
        View.OnClickListener checkBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!itemStateArray.get(position, false)) {
                    holder.checkBox.setChecked(true);
                    itemStateArray.put(position, true);
                }
                else  {
                    holder.checkBox.setChecked(false);
                    itemStateArray.put(position, false);
                }
                SelectPeopleActivity.updateSelectedPeople();
            }
        };
        holder.itemView.setOnClickListener(checkBoxListener);
        holder.checkBox.setOnClickListener(checkBoxListener);

    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public List<People> getSelectedPeople() {
        List<People> selectedPeopleList = new ArrayList<>();
        for (int i = 0; i < peopleList.size(); i++) {
            if (itemStateArray.get(i, false)) {
                selectedPeopleList.add(peopleList.get(i));
            }
        }
        return selectedPeopleList;
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView, numberTextView;
        MaterialCheckBox checkBox;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_textview);
            numberTextView = itemView.findViewById(R.id.number_textview);
            checkBox = itemView.findViewById(R.id.materialCheckBox);
        }
    }
}
