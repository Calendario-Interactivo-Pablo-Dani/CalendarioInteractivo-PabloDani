package com.example.planify.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // 1️⃣ Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.ver_miembros, container, false);


        // 3️⃣ Inicializamos las vistas
        inicializarVistas(view);

        btnAtras=view.findViewById(R.id.btnAtrasMiembros);
        btnAtras.setOnClickListener(listenerAtras);


        // 4️⃣ Configuramos el RecyclerView
        configurarRecycler();

        // 5️⃣ Cargamos las tareas del día (de momento mock)
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

        // TODO: llamada a backend / ViewModel

        Miembro m1=new Miembro();
        Miembro m2=new Miembro();
        Miembro m3=new Miembro();

        m1.setUsername("danielito");
        m2.setUsername("pablito");
        m3.setUsername("h4astyy");
        listaMiembros.add(m1);
        listaMiembros.add(m2);
        listaMiembros.add(m3);

        miembroAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener listenerAtras = view -> {
        getParentFragmentManager().popBackStack();
    };

}



