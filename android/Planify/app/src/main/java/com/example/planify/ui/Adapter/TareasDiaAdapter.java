package com.example.planify.ui.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;
import com.example.planify.ui.Fragmentos.VentanaTarea;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class TareasDiaAdapter
        extends RecyclerView.Adapter<TareasDiaAdapter.TareaDiaViewHolder> {

    private List<Tarea> tareas;

    public TareasDiaAdapter(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    // --------------------- ON CREATE VIEW HOLDER -------------------------------------------
    /*
     * Este método se encarga de CREAR la vista de cada item del RecyclerView
     * que aparece en la ventana del día.
     *
     * Aquí se infla el XML correspondiente a una tarea del día y se crea
     * el ViewHolder que mantendrá las referencias a sus vistas.
     *
     * Este método NO rellena datos, solo crea la estructura visual.
     */
    @NonNull
    @Override
    public TareaDiaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarea_dia, parent, false);

        return new TareaDiaViewHolder(view);
    }
    // ---------------------------------------------------------------------------------------


    // --------------------- ON BIND VIEW HOLDER ----------------------------------------------
    /*
     * Este método se encarga de RELLENAR cada tarjeta con la información
     * de la tarea correspondiente a esa posición.
     *
     * El RecyclerView reutiliza los ViewHolder, por lo que este método
     * se ejecuta muchas veces al hacer scroll.
     *
     * Aquí se asignan los datos reales de la Tarea a los TextView.
     */
    @Override
    public void onBindViewHolder(@NonNull TareaDiaViewHolder holder, int position) {

        Tarea tarea = tareas.get(position);

        holder.tituloTarea.setText(tarea.getNombre());


        if (tarea.getFechaLim() != null) {

            LocalDateTime fechaLim =
                    LocalDateTime.parse(tarea.getFechaLim());

            holder.horaTarea.setText(
                    String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            fechaLim.getHour(),
                            fechaLim.getMinute()
                    )
            );

        } else {
            holder.horaTarea.setText("--:--");
        }

        holder.indicadorColor.setBackgroundTintList(
                ColorStateList.valueOf(getColorFromTarea(tarea))
        );

        holder.cardTarea.setOnClickListener(v -> {
            abrirEditarTarea(v, tarea);
        });
    }
    // ---------------------------------------------------------------------------------------


    // Número total de tareas del día
    @Override
    public int getItemCount() {
        return tareas.size();
    }

    // --------------------- VIEW HOLDER -----------------------------------------------------
    /*
     * El ViewHolder guarda las referencias a las vistas del item_tarea_dia.xml
     * para evitar hacer findViewById cada vez que se rellena un item.
     */
    static class TareaDiaViewHolder extends RecyclerView.ViewHolder {

        TextView tituloTarea;
        TextView horaTarea;
        View indicadorColor;

        MaterialCardView cardTarea;
        TareaDiaViewHolder(View itemView) {
            super(itemView);

            tituloTarea = itemView.findViewById(R.id.titulo_tarea_dia);
            horaTarea = itemView.findViewById(R.id.hora_tarea_dia);
            indicadorColor = itemView.findViewById(R.id.indicador_color);
            cardTarea = itemView.findViewById(R.id.btnTareaInspection);
        }
    }


    //Este metodo sirve para segun que color tuviese la tarea(ya seleccionado)
    //esto es para casos como volver a entrar a la app despues de cerrarla, se repinta del color
    //que estuviese seleccionado

    //*Hay que añadir el enum a la BD, con algunos colores, 6 por lo menos*
    private int getColorFromTarea(Tarea tarea) {

        if (tarea.getColor() == null) return Color.parseColor("#FFFFFF");

        switch (tarea.getColor()) {
            case "ROJO":
                return Color.parseColor("#E53935");
            case "AZUL":
                return Color.parseColor("#1E88E5");
            case "VERDE":
                return Color.parseColor("#43A047");
            case "AMARILLO":
                return Color.parseColor("#FDD835");
            case "MORADO":
                return Color.parseColor("#8E24AA");
            default:
                return Color.parseColor("#FFFFFF");
        }
    }
    // ---------------------------------------------------------------------------------------


    private void abrirEditarTarea(View view, Tarea tarea) {

        if (tarea.getId() == null) {
            Toast.makeText(
                    view.getContext(),
                    "Error: tarea sin ID",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        VentanaTarea fragmentEditarTarea = new VentanaTarea();

        Bundle bundle = new Bundle();

        // marcar que es edición
        bundle.putBoolean("ES_EDICION", true);

        // datos actuales de la tarea
        bundle.putInt("ID_TAREA", tarea.getId());
        bundle.putString("NOMBRE", tarea.getNombre());
        bundle.putString("TIPO", tarea.getTipo());
        bundle.putString("ESTADO", tarea.getEstado());
        bundle.putString("COLOR", tarea.getColor());
        bundle.putString("FECHA_LIM", tarea.getFechaLim());

        fragmentEditarTarea.setArguments(bundle);

        FragmentActivity activity = (FragmentActivity) view.getContext();

        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentEditarTarea)
                .addToBackStack(null)
                .commit();
    }




}

