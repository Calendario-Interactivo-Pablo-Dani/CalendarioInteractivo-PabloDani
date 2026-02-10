package com.example.planify.ui.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.CalendarioSeleccionado;
import com.example.planify.data.POJOs.Tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.TareaApi;
import com.example.planify.ui.Adapter.TareasDiaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

            String fechaStr = args.getString("FECHA");
            if (fechaStr != null) {

                fechaDia = fechaStr; // ✅ CLAVE: guardar la fecha para pasarla a VentanaTarea

                try {
                    LocalDate fecha = LocalDate.parse(fechaStr);

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

            } else {
                fecha_calendario_dia.setText("TAREAS DEL DÍA");
                fechaDia = null; // explícito
            }
        }

        // 4️⃣ Configuramos el RecyclerView
        configurarRecycler();

        // 5️⃣ Cargamos las tareas del día (de momento mock)
        cargarTareasDelDia();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fechaDia != null) {
            cargarTareasDelDia();
        }
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

        if (fechaDia == null) {
            return;
        }
        TareaApi tareaApi =
                ApiCliente.getRetrofit().create(TareaApi.class);

        tareaApi.verTareasDia(
                CalendarioSeleccionado.idCal,
                fechaDia // yyyy-MM-dd
        ).enqueue(new Callback<List<Tarea>>() {
            @Override
            public void onResponse(
                    Call<List<Tarea>> call,
                    Response<List<Tarea>> response
            ) {
                listaTareasDia.clear();

                if (response.isSuccessful() && response.body() != null) {
                    listaTareasDia.addAll(response.body());
                }

                tareasDiaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Tarea>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Error al cargar tareas del día",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });




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
