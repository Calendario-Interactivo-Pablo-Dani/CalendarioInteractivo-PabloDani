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

/*
 * CALENDAR ADAPTER
 *
 * Adapter encargado de pintar el calendario mensual en forma de grid.
 *
 * Este adapter:
 *  - NO calcula fechas
 *  - NO decide navegación
 *  - NO conoce meses ni años
 *
 * Su única responsabilidad es:
 *  - Recibir una lista de CalendarDay ya calculada
 *  - Pintar cada casilla del calendario
 *  - Notificar al fragment cuando se pulsa un día válido
 *
 * La lógica de fechas vive en FragmentMainCalendario.
 */
public class CalendarAdapter
        extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    // Lista de días del mes (incluye huecos con dayNumber = null)
    private List<CalendarDay> dias;

    // Listener que se define en el FragmentMainCalendario
    // El adapter NO sabe qué pasa al hacer click
    private View.OnClickListener clickListener;


    /*
     * Constructor del adapter.
     *
     * dias Lista de días del mes, generada por el fragment
     * clickListener Acción a ejecutar cuando se pulsa un día válido
     */
    public CalendarAdapter(List<CalendarDay> dias,
                           View.OnClickListener clickListener) {
        this.dias = dias;
        this.clickListener = clickListener;
    }


    // --------------------- CREACIÓN DE CELDAS ---------------------

    /*
     * Se llama cuando el RecyclerView necesita crear una nueva celda.
     *
     * Aquí SOLO se infla el layout del día.
     * NO se meten datos.
     */
    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dia_calendario, parent, false);

        return new DayViewHolder(view);
    }


    // --------------------- RELLENO DE CELDAS ---------------------

    /*
     * Se llama para rellenar una celda concreta del calendario.
     *
     * Este méto-do se ejecuta muchas veces, ya que el RecyclerView reutiliza
     * las vistas al hacer scroll o al cambiar de mes.
     */
    @Override
    public void onBindViewHolder(
            @NonNull DayViewHolder holder,
            int position
    ) {

        // Obtenemos el día correspondiente a esta posición
        CalendarDay dia = dias.get(position);

        // CASO 1: casilla vacía (antes del día 1 del mes)
        if (dia.isEmpty()) {

            // No se muestra número
            holder.numeroDia.setText("");

            // Se desactiva el click para evitar pulsaciones inválidas
            holder.itemView.setOnClickListener(null);

        } else {
            // CASO 2: día real del mes

            // Mostramos el número del día
            holder.numeroDia.setText(
                    String.valueOf(dia.getDayNumber())
            );

            /*
             * Guardamos el número del día en el tag de la vista.
             *
             * Esto permite que el FragmentMainCalendario recupere
             * qué día se ha pulsado SIN acoplar el adapter al fragment.
             */
            holder.itemView.setTag(dia.getDayNumber());

            // El click se delega al fragment
            holder.itemView.setOnClickListener(clickListener);
        }
    }


    // --------------------- NÚMERO DE CELDAS ---------------------

    /*
     * Devuelve el número total de celdas del calendario.
     * Incluye días reales + huecos.
     */
    @Override
    public int getItemCount() {
        return dias.size();
    }


    // --------------------- VIEW HOLDER ---------------------

    /*
     * ViewHolder de una celda del calendario.
     *
     * Guarda las referencias a las vistas del item_dia_calendario.xml
     * para evitar llamadas repetidas a findViewById.
     */
    static class DayViewHolder extends RecyclerView.ViewHolder {

        TextView numeroDia;

        DayViewHolder(View itemView) {
            super(itemView);
            numeroDia = itemView.findViewById(R.id.numero_dia);
        }
    }
}


