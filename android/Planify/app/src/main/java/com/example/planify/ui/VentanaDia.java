package com.example.planify.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class VentanaDia extends Fragment {

    // --------------------- ATRIBUTOS ------------------------------------------------------

    // Recycler de tareas del día
    private RecyclerView recyclerViewTareasDia;
    private TareasDiaAdapter tareasDiaAdapter;
    private List<Tarea> listaTareasDia;


    private TextView fecha_calendario_dia;

    private Button btnAtrasDia;

    private FloatingActionButton btnCrearTarea;


    private String fechaDia;

    // --------------------- CICLO DE VIDA --------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // 1️⃣ Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.view_dia, container, false);


        // 3️⃣ Inicializamos las vistas
        inicializarVistas(view);




        fecha_calendario_dia=view.findViewById(R.id.textoFechaCalendarioDia);
        fecha_calendario_dia.setText("TAREAS DEL DÍA");


        btnAtrasDia=view.findViewById(R.id.btnatrasDia);
        btnAtrasDia.setOnClickListener(listenerAtras);


        btnCrearTarea=view.findViewById(R.id.btnCrearTarea);
        btnCrearTarea.setOnClickListener(listenerCrearTarea);



        Bundle args = getArguments();
        if (args != null) {

            fechaDia = args.getString("FECHA"); // "2026-03-05"

            try {
                LocalDate fecha = LocalDate.parse(fechaDia);

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "EEEE d 'de' MMMM",
                                new Locale("es", "ES")
                        );

                String fechaFormateada =
                        fecha.format(formatter).toUpperCase();

                fecha_calendario_dia.setText(fechaFormateada);

            } catch (Exception e) {
                fecha_calendario_dia.setText("TAREAS DEL DÍA");
            }
        }

        // 4️⃣ Configuramos el RecyclerView
        configurarRecycler();

        // 5️⃣ Cargamos las tareas del día (de momento mock)
        cargarTareasDelDia();

        return view;
    }

    // --------------------------------------------------------------------------------------

    /*
     * Recoge los datos que llegan a este fragment.
     * Normalmente vendrán del calendario (día pulsado).
     */
    /*private void recogerDatos() {

        if (getArguments() != null) {

            String fecha = getArguments().getString("FECHA");
            idCalendario = getArguments().getInt("ID_CAL");

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            fechaSeleccionada = LocalDate.parse(fecha, formatter);
        }
    }*/

    /*
     * Inicializa todas las vistas del layout.
     * Aquí NO se mete lógica, solo findViewById.
     */
    private void inicializarVistas(View view) {

        recyclerViewTareasDia = view.findViewById(R.id.recycler_tareas_dia);

        // TODO FUTURO:
        // TextView fecha
        // EditText nueva tarea
        // Botón añadir tarea
        // etc.
    }

    /*
     * Configura el RecyclerView:
     * - LayoutManager
     * - Adapter
     */
    private void configurarRecycler() {

        recyclerViewTareasDia.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        listaTareasDia = new ArrayList<>();

        tareasDiaAdapter = new TareasDiaAdapter(listaTareasDia);

        recyclerViewTareasDia.setAdapter(tareasDiaAdapter);
    }

    /*
     * Carga las tareas correspondientes al día.
     * De momento son datos falsos.
     * Más adelante vendrá del backend.
     */
    private void cargarTareasDelDia() {

        // TODO: llamada a backend / ViewModel

        // Datos de prueba

        Tarea t1 = new Tarea();
        t1.setFechaLim("2026-02-09T18:30");
        t1.setNombre("Presentación proyecto");
        t1.setId(1);

        Tarea t2 = new Tarea();
        t2.setNombre("Entrenar");
        t2.setColor("VERDE");
        t2.setId(2);

        listaTareasDia.add(t1);
        listaTareasDia.add(t2);

        tareasDiaAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener listenerAtras = view -> {
        getParentFragmentManager().popBackStack();
    };

    private View.OnClickListener listenerCrearTarea = view -> {

        Bundle bundle = new Bundle();
        bundle.putString("FECHA_DIA", fechaDia);
        bundle.putInt("ID_CAL", CalendarioSeleccionado.idCal);

        VentanaTarea fragmentTarea = new VentanaTarea();
        fragmentTarea.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentTarea)
                .addToBackStack(null)
                .commit();
    };



}
