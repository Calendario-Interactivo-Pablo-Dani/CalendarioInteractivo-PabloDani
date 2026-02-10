package com.example.planify.ui.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Tarea> tareas;

    public EventAdapter(List<Tarea> tareas) {
        this.tareas = tareas;
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

        Tarea tarea = tareas.get(position);

        holder.tituloEvento.setText(tarea.getNombre());

        // De momento fijo hasta que parsees fechas
        holder.diaEvento.setText("27");
        holder.mesEvento.setText("ENE");
        holder.horaEvento.setText("18:30");
    }
    //-----------------------------------------------------------------------------------------------------



    // Cuántos items hay
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // ViewHolder: guarda las vistas del item_event.xml
    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tituloEvento, diaEvento, mesEvento, horaEvento;

        EventViewHolder(View itemView) {
            super(itemView);
            tituloEvento = itemView.findViewById(R.id.titulo_evento);
            diaEvento = itemView.findViewById(R.id.dia_evento);
            mesEvento = itemView.findViewById(R.id.mes_evento);
            horaEvento=itemView.findViewById(R.id.hora_evento);
        }
    }
}