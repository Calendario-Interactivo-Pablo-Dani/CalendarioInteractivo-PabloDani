package com.example.planify.ui.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.CalendarDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    private Map<LocalDate, List<String>> coloresPorDia;

    // Listener que se define en el FragmentMainCalendario
    // El adapter NO sabe qué pasa al hacer click
    private View.OnClickListener clickListener;


    /*
     * Constructor del adapter.
     *
     * dias Lista de días del mes, generada por el fragment
     * clickListener Acción a ejecutar cuando se pulsa un día válido
     */
    public CalendarAdapter(
            List<CalendarDay> dias,
            Map<LocalDate, List<String>> coloresPorDia,
            View.OnClickListener clickListener
    ) {
        this.dias = dias;
        this.coloresPorDia = coloresPorDia;
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

        //añadimos esto para limpiar las barras antes de reutilizar la vista, para evitar
        //bugs y traspasos de barras entre dias, así como barras q no se quitan y demás
        holder.huecoBarras.removeAllViews();

        // CASO 1: casilla vacía (antes del día 1 del mes)
        if (dia.isEmpty()) {

            // No se muestra número
            holder.numeroDia.setText("");

            // Se desactiva el click para evitar pulsaciones inválidas
            holder.itemView.setOnClickListener(null);

            //por si acaso se colase alguna barra
            holder.huecoBarras.setVisibility(View.INVISIBLE);

        } else {
            // CASO 2: día real del mes

            // Mostramos el número del día
            holder.numeroDia.setText(
                    String.valueOf(dia.getDayNumber())
            );

            // Guardamos el número del día en el tag
            holder.itemView.setTag(dia.getDayNumber());

            // El click se delega al fragment
            holder.itemView.setOnClickListener(clickListener);

            holder.huecoBarras.setVisibility(View.VISIBLE);

            //FECHA REAL DEL DÍA
            LocalDate fecha = dia.getFecha();

            //COLORES DE LAS TAREAS DE ESE DÍA
            List<String> colores = coloresPorDia.get(fecha);

            if (colores != null && !colores.isEmpty()) {

                int max = Math.min(colores.size(), 3);

                for (int i = 0; i < max; i++) {

                    View barra = LayoutInflater
                            .from(holder.itemView.getContext())
                            .inflate(
                                    R.layout.item_barras_colores_dias,
                                    holder.huecoBarras,
                                    false
                            );

                    // color real
                    barra.setBackgroundColor(
                            obtenerColorDesdeNombre(colores.get(i))
                    );

                    holder.huecoBarras.addView(barra);
                }
            }
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
        LinearLayout huecoBarras;

        DayViewHolder(View itemView) {
            super(itemView);
            numeroDia = itemView.findViewById(R.id.numero_dia);
            huecoBarras=itemView.findViewById(R.id.hueco_barras_eventos);
        }
    }

    private int obtenerColorDesdeNombre(String color) {

        switch (color) {
            case "ROJO":
                return Color.parseColor("#FF6B6B");
            case "VERDE":
                return Color.parseColor("#4CAF50");
            case "AMARILLO":
                return Color.parseColor("#FFD93D");
            case "NARANJA":
                return Color.parseColor("#FF9800");
            case "AZUL":
                return Color.parseColor("#4ECDC4");
            case "MORADO":
                return Color.parseColor("#7F00FF");
            case "ROSA":
                return Color.parseColor("#C11CB2");
            case "GRIS":
                return Color.parseColor("#9B9B9B");
            case "NEGRO":
                return Color.parseColor("#000000");
            case "BLANCO":
                return Color.WHITE;
            default:
                return Color.GRAY;
        }
    }
}


