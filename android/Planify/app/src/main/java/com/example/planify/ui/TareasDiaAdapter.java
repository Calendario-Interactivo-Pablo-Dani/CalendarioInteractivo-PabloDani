package com.example.planify.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;

import java.util.List;

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
        holder.horaTarea.setText("18:30");

        holder.indicadorColor.setBackgroundTintList(
                ColorStateList.valueOf(getColorFromTarea(tarea))
        );

        holder.indicadorColor.setOnClickListener(v -> {
            mostrarSelectorColor(v, tarea, holder);
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

        TareaDiaViewHolder(View itemView) {
            super(itemView);

            tituloTarea = itemView.findViewById(R.id.titulo_tarea_dia);
            horaTarea = itemView.findViewById(R.id.hora_tarea_dia);
            indicadorColor = itemView.findViewById(R.id.indicador_color);
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

    private void mostrarSelectorColor(View anchor, Tarea tarea, TareaDiaViewHolder holder) {

        View popupView = LayoutInflater.from(anchor.getContext())
                .inflate(R.layout.popup_selector_color, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupView.findViewById(R.id.color_rojo).setOnClickListener(v -> {
            aplicarColor(tarea, "ROJO", holder);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_azul).setOnClickListener(v -> {
            aplicarColor(tarea, "AZUL", holder);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_verde).setOnClickListener(v -> {
            aplicarColor(tarea, "VERDE", holder);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_amarillo).setOnClickListener(v -> {
            aplicarColor(tarea, "AMARILLO", holder);
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_morado).setOnClickListener(v -> {
            aplicarColor(tarea, "MORADO", holder);
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(anchor);
    }

    private void aplicarColor(Tarea tarea, String color, TareaDiaViewHolder holder) {

        tarea.setColor(color); // por ahora String, luego enum

        holder.indicadorColor.setBackgroundTintList(
                ColorStateList.valueOf(getColorFromTarea(tarea))
        );

        // Aquí luego: llamada a backend para guardar
    }

}

