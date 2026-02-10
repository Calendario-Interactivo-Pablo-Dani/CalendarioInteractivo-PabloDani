package com.example.planify.ui.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Miembro;

import java.util.List;

public class MiembroAdapter extends RecyclerView.Adapter<MiembroAdapter.MiembroViewHolder>{

    private List<Miembro> miembros;

    public MiembroAdapter(List<Miembro> miembros) {
        this.miembros = miembros;
    }

    @NonNull
    @Override
    public MiembroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //creamos la vista real, a partir del xml itemsbarraeventos, que se infla.
        // el context no es solo "config", es la referencia a la Activity
        // que permite inflar el layout con el tema, tamaños y recursos correctos

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_miembro, parent, false);

        // devolvemos al RecyclerView (el molino) el ViewHolder ya preparado,
        // con la tarjeta creada y sus TextView localizados,
        // para que lo guarde y lo reutilice cuando haga falta al girar la rueda

        return new MiembroViewHolder(view);
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
     * En este méto-do se asignan los datos reales (username)
     * a los TextView de la tarjeta.
     */

    @Override
    public void onBindViewHolder(@NonNull MiembroViewHolder holder, int position) {

        Miembro miembro = miembros.get(position);
        holder.nombreMiembro.setText(miembro.getUsername());
    }
    //-----------------------------------------------------------------------------------------------------



    // Cuántos items hay
    @Override
    public int getItemCount() {
        return miembros.size();
    }

    // ViewHolder: guarda las vistas del item_miembro.xml
    static class MiembroViewHolder extends RecyclerView.ViewHolder {

        TextView nombreMiembro;
        ImageView iconoMiembro;

        MiembroViewHolder(View itemView) {
            super(itemView);
            iconoMiembro=itemView.findViewById(R.id.tvIconoMiembro);
            nombreMiembro=itemView.findViewById(R.id.NombreMiembro);
        }
    }
}

