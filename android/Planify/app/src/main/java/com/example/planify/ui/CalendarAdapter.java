package com.example.planify.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Calendario;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    private List<CalendarDay> dias;
    public CalendarAdapter(List<CalendarDay> dias){this.dias=dias;}

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dia_calendario, parent, false);

        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DayViewHolder holder,
            int position
    ) {

        CalendarDay dia = dias.get(position);

        if (dia.isEmpty()) {
            holder.numeroDia.setText("");
            holder.itemView.setClickable(false);
        } else {
            holder.numeroDia.setText(
                    String.valueOf(dia.getDayNumber())
            );
            holder.itemView.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return dias.size();
    }
    static class DayViewHolder extends RecyclerView.ViewHolder{

        TextView numeroDia;
        DayViewHolder(View itemView){
            super(itemView);
            numeroDia=itemView.findViewById(R.id.numero_dia);
        }
    }
}
