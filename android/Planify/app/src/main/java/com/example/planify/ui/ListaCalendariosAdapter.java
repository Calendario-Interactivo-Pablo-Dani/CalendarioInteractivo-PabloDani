package com.example.planify.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.dto.CalendarioResponseDTO;

import java.util.List;

public class ListaCalendariosAdapter  extends RecyclerView.Adapter<ListaCalendariosAdapter.CalendarioViewHolder> {
    // 1️⃣ Lista de calendarios que vienen del backend
    private List<CalendarioResponseDTO> calendarios;

    // 2️⃣ Constructor: recibe la lista
    public ListaCalendariosAdapter(List<CalendarioResponseDTO> calendarios) {
        this.calendarios = calendarios;
    }

    // 3️⃣ Se llama cuando Android necesita crear una "fila"
    @NonNull
    @Override
    public CalendarioViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        // Inflamos el XML item_calendario.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendario, parent, false);

        return new CalendarioViewHolder(view);
    }

    // 4️⃣ Se llama para rellenar cada fila con datos
    @Override
    public void onBindViewHolder(
            @NonNull CalendarioViewHolder holder, int position) {

        CalendarioResponseDTO calendario = calendarios.get(position);

        // Texto del botón: Nombre (rol)
        String texto = calendario.getNombre()
                + " (" + calendario.getRol() + ")";

        holder.btnCalendario.setText(texto);
    }

    // 5️⃣ Cuántos elementos hay
    @Override
    public int getItemCount() {
        return calendarios.size();
    }

    // 6️⃣ ViewHolder: referencia al botón
    static class CalendarioViewHolder extends RecyclerView.ViewHolder {

        Button btnCalendario;

        public CalendarioViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCalendario = itemView.findViewById(R.id.btnCalendario);
        }
    }
}
