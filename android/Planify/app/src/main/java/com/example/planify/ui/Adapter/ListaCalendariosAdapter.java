package com.example.planify.ui.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.CalendarioSeleccionado;
import com.example.planify.data.dto.CalendarioResponseDTO;
import com.example.planify.ui.Activities.MarcoGeneral;

import java.util.List;

/**/
public class ListaCalendariosAdapter  extends RecyclerView.Adapter<ListaCalendariosAdapter.CalendarioViewHolder> {
    // Lista de calendarios que vienen del backend
    private List<CalendarioResponseDTO> calendarios;

    public ListaCalendariosAdapter(List<CalendarioResponseDTO> calendarios) {
        this.calendarios = calendarios;
    }

    //Se llama cuando Android necesita crear una "fila"
    @NonNull
    @Override
    public CalendarioViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        // Inflamos el XML item_calendario.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendario, parent, false);

        return new CalendarioViewHolder(view);
    }

    //Se llama para rellenar cada fila con datos
    @Override
    public void onBindViewHolder(
            @NonNull CalendarioViewHolder holder, int position) {

        CalendarioResponseDTO calendario = calendarios.get(position);

        // Texto del botón: Nombre (rol)
        String texto = calendario.getNombre()
                + " (" + calendario.getRol() + ")";
        //tras la referencia al boton, le damos un valor, el texto, que será el nombre del calendar
        holder.btnCalendario.setText(texto);
        //Le damos también un listener que abrirá el activity marcoGeneral
        holder.btnCalendario.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    MarcoGeneral.class
            );

            //Crear y establecer los daros del calendario seleccionado awuí permite sber exactamente
            //el calendario que se ha pulsado, actua como un calendario "activo"
            CalendarioSeleccionado.idCal = calendario.getIdCal();
            CalendarioSeleccionado.nombre = calendario.getNombre();
            CalendarioSeleccionado.rol = calendario.getRol();
            CalendarioSeleccionado.codigo = calendario.getCodigo();

            v.getContext().startActivity(intent);
        });
    }

    //  Cuántos elementos hay
    @Override
    public int getItemCount() {
        return calendarios.size();
    }

    //  ViewHolder: referencia al botón
    static class CalendarioViewHolder extends RecyclerView.ViewHolder {

        Button btnCalendario;

        public CalendarioViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCalendario = itemView.findViewById(R.id.btnCalendario);
        }
    }
}
