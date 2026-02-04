package com.example.planify.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Calendario;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    private List<CalendarDay> dias;
    private View.OnClickListener clickListener;

    public CalendarAdapter(List<CalendarDay> dias, View.OnClickListener clickListener) {
        this.dias = dias;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dia_calendario, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {

        CalendarDay dia = dias.get(position);

        if (dia.isEmpty()) {
            holder.numeroDia.setText("");
            holder.itemView.setOnClickListener(null);
        } else {
            holder.numeroDia.setText(String.valueOf(dia.getDayNumber()));
            holder.itemView.setTag(dia.getDayNumber());
            holder.itemView.setOnClickListener(clickListener);
        }
    }

    @Override
    public int getItemCount() {
        return dias.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView numeroDia;
        DayViewHolder(View itemView) {
            super(itemView);
            numeroDia = itemView.findViewById(R.id.numero_dia);
        }
    }
}

