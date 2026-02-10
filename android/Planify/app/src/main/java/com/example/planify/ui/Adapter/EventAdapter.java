package com.example.planify.ui.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;
import com.example.planify.data.dto.TareaNuevaResponseDTO;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<TareaNuevaResponseDTO> tareas;

    public EventAdapter(List<TareaNuevaResponseDTO> tareas) {
        this.tareas = tareas;
    }
    public void setTareas(List<TareaNuevaResponseDTO> nuevasTareas) {
        this.tareas.clear();
        this.tareas.addAll(nuevasTareas);
        notifyDataSetChanged();
    }



    //---------------------ON CREATE VIEW HOLDER----------------------------------------------------------
    /*
     * Para entender este méto-do hay que tener muy presente la clase estática
     * EventViewHolder que tenemos debajo, ya que es aquí donde se ejecuta su constructor.
     *
     * ¿Qué hace este méto-do?
     *
     * Este méto-do se encarga de CREAR la vista de cada item del RecyclerView,
     * es decir, de cada tarjeta (tarea/evento) que aparece dentro de la barra con scroll.
     *
     * La anotación @NonNull indica que este méto-do nunca debe devolver null
     * (evita bugs y ayuda al compilador).
     *
     * En la línea donde se hace:
     * View view = LayoutInflater...
     * lo que ocurre es que se “infla” el layout, es decir:
     * se coge el XML del layout de cada tarjeta y se convierte en una View real.
     *
     * Una vez tenemos esta View (la tarjeta ya creada), llamamos inmediatamente
     * al constructor del ViewHolder con:
     * return new EventViewHolder(view);
     *
     * En ese constructor, el ViewHolder localiza los TextView del XML usando findViewById
     * y guarda las referencias, de forma que aprende qué componentes tiene esa tarjeta.
     *
     * Salidos del constructor, este ViewHolder ya preparado se devuelve al RecyclerView,
     * que lo reutilizará al hacer scroll. Es como una rueda:
     * las tarjetas que salen de pantalla se reutilizan para mostrar nuevas tareas,
     * por lo que solo existen a la vez las necesarias para lo que se ve en pantalla, osea como si
     * fuese un jodido molino que solo lleva agua en las cubas q salen del agua.
     */

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //creamos la vista real, a partir del xml itemsbarraeventos, que se infla.
        // el context no es solo "config", es la referencia a la Activity
        // que permite inflar el layout con el tema, tamaños y recursos correctos

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_barra_eventos, parent, false);

        // devolvemos al RecyclerView (el molino) el ViewHolder ya preparado,
        // con la tarjeta creada y sus TextView localizados,
        // para que lo guarde y lo reutilice cuando haga falta al girar la rueda

        return new EventViewHolder(view);
    }
    //----------------------------------------------------------------------------------------------------



    // Aquí se rellena cada item con datos distintos
    //------------------------------------ON BIND VIEW HOLDER---------------------------------------------

    /*
     * Este méto-do se encarga de RELLENAR cada tarjeta con la información
     * correspondiente a una posición de la lista de tareas.
     *
     * Aquí NO se crean vistas ni se hace findViewById.
     * Se utilizan ViewHolder ya creados y preparados previamente, por el metodo onCreateViewHolder
     *
     * El parámetro "position" indica qué elemento de la lista de tareas
     * corresponde a esta tarjeta en ese momento.
     *
     * El RecyclerView puede reutilizar un mismo ViewHolder para distintas
     * posiciones al hacer scroll, por lo que este méto-do se ejecuta muchas veces.
     *
     * En este méto-do se asignan los datos reales (nombre, fecha, hora, etc.)
     * a los TextView de la tarjeta.
     */

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        TareaNuevaResponseDTO  tarea = tareas.get(position);

        holder.tituloEvento.setText(tarea.getNombre());

        String fecha = tarea.getFechaLim();

        if (fecha != null && fecha.length() >= 16) {

            String dia = fecha.substring(8, 10);
            String mesNum = fecha.substring(5, 7);
            String hora = fecha.substring(11, 16);

            holder.diaEvento.setText(dia);
            holder.mesEvento.setText(mesATexto(mesNum));
            holder.horaEvento.setText(hora);

        } else {
            holder.diaEvento.setText("--");
            holder.mesEvento.setText("---");
            holder.horaEvento.setText("--:--");
        }
        holder.indicadorColor.setBackgroundTintList(
                ColorStateList.valueOf(
                        colorDesdeEnum(tarea.getColor())
                )
        );

    }
    private String mesATexto(String mesNum) {
        switch (mesNum) {
            case "01": return "ENE";
            case "02": return "FEB";
            case "03": return "MAR";
            case "04": return "ABR";
            case "05": return "MAY";
            case "06": return "JUN";
            case "07": return "JUL";
            case "08": return "AGO";
            case "09": return "SEP";
            case "10": return "OCT";
            case "11": return "NOV";
            case "12": return "DIC";
            default: return "---";
        }
    }
    private int colorDesdeEnum(String color) {

        if (color == null) return Color.GRAY;

        switch (color) {
            case "ROJO":
                return Color.parseColor("#FF6B6B");
            case "AZUL":
                return Color.parseColor("#4D96FF");
            case "AMARILLO":
                return Color.parseColor("#FFD93D");
            case "NARANJA":
                return Color.parseColor("#FF9F43");
            case "VERDE":
                return Color.parseColor("#6BCF63");
            case "NEGRO":
                return Color.parseColor("#2E2E2E");
            case "MORADO":
                return Color.parseColor("#9B59B6");
            case "ROSA":
                return Color.parseColor("#FF6F91");
            case "BLANCO":
                return Color.WHITE;
            case "GRIS":
                return Color.GRAY;
            default:
                return Color.GRAY;
        }
    }



    // Cuántos items hay
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // ViewHolder: guarda las vistas del item_event.xml
    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tituloEvento, diaEvento, mesEvento, horaEvento;
        View indicadorColor;

        EventViewHolder(View itemView) {
            super(itemView);

            indicadorColor = itemView.findViewById(R.id.indicador_color);

            tituloEvento = itemView.findViewById(R.id.titulo_evento);
            diaEvento = itemView.findViewById(R.id.dia_evento);
            mesEvento = itemView.findViewById(R.id.mes_evento);
            horaEvento = itemView.findViewById(R.id.hora_evento);
        }
    }
}