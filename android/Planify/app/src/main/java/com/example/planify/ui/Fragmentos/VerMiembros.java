package com.example.planify.ui.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.CalendarioSeleccionado;
import com.example.planify.data.POJOs.Miembro;
import com.example.planify.data.dto.UsuarioCalendarioDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.CalendarioApi;
import com.example.planify.ui.Adapter.MiembroAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerMiembros  extends Fragment {

    private RecyclerView recyclerViewMiembros;
    private MiembroAdapter miembroAdapter;
    private List<Miembro> listaMiembros;

    private Button btnAtras;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        //Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.ver_miembros, container, false);


        //Inicializamos las vistas
        inicializarVistas(view);

        btnAtras=view.findViewById(R.id.btnAtrasMiembros);
        btnAtras.setOnClickListener(listenerAtras);


        //Configuramos el RecyclerView
        configurarRecycler();

        //Cargamos las tareas del día (de momento mock)
        cargarMiembros();



        return view;
    }

    // --------------------------------------------------------------------------------------


    /*
     * Inicializa todas las vistas del layout.
     * Aquí NO se mete lógica, solo findViewById.
     */
    private void inicializarVistas(View view) {

        recyclerViewMiembros = view.findViewById(R.id.recycler_miembros);

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

        recyclerViewMiembros.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        listaMiembros = new ArrayList<>();

        miembroAdapter = new MiembroAdapter(listaMiembros);

        recyclerViewMiembros.setAdapter(miembroAdapter);
    }

    /*
     * Carga las tareas correspondientes al día.
     * De momento son datos falsos.
     * Más adelante vendrá del backend.
     */
    private void cargarMiembros() {

        int idCal = CalendarioSeleccionado.idCal;

        CalendarioApi calendarioApi =
                ApiCliente.getRetrofit().create(CalendarioApi.class);

        calendarioApi.obtenerMiembros(idCal)
                .enqueue(new Callback<List<UsuarioCalendarioDTO>>() {
                    @Override
                    public void onResponse(Call<List<UsuarioCalendarioDTO>> call,
                                           Response<List<UsuarioCalendarioDTO>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            //limpiamos lista por si ya había datos
                            listaMiembros.clear();

                            //convertimos DTO -> Miembro (modelo de Recycler)
                            for (UsuarioCalendarioDTO dto : response.body()) {
                                Miembro m = new Miembro();

                                m.setUsername(dto.getUsername());

                                listaMiembros.add(m);
                            }

                            // 3) avisamos al adapter
                            miembroAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getContext(),
                                    "Error al cargar miembros",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UsuarioCalendarioDTO>> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "Error de conexión",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private View.OnClickListener listenerAtras = view -> {
        getParentFragmentManager().popBackStack();
    };

}



