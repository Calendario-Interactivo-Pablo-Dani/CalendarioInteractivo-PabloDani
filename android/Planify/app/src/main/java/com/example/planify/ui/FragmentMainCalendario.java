package com.example.planify.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;

import java.util.ArrayList;
import java.util.List;
public class FragmentMainCalendario extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Tarea> tareas;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // 1️⃣ Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_main_calendario, container, false);

        // 2️⃣ Encontramos el RecyclerView
        recyclerView = view.findViewById(R.id.barra_eventos);

        // 3️⃣ Creamos datos de prueba
        tareas = new ArrayList<>();

        Tarea t1 = new Tarea();
        t1.setNombre("Estudiar PDM");

        Tarea t2 = new Tarea();
        t2.setNombre("Avanzar TFG");


        Tarea t3 = new Tarea();
        t1.setNombre("Estudiar PDM");

        Tarea t4 = new Tarea();
        t2.setNombre("Avanzar TFG");
        Tarea t5 = new Tarea();
        t1.setNombre("Estudiar PDM");

        Tarea t6 = new Tarea();
        t2.setNombre("Avanzar TFG");
        Tarea t7 = new Tarea();
        t1.setNombre("Estudiar PDM");

        Tarea t8 = new Tarea();
        t2.setNombre("Avanzar TFG");

        tareas.add(t1);
        tareas.add(t2);
        tareas.add(t3);
        tareas.add(t4);
        tareas.add(t5);
        tareas.add(t6);
        tareas.add(t7);
        tareas.add(t8);

        // 4️⃣ Creamos el adapter
        eventAdapter = new EventAdapter(tareas);

        // 5️⃣ LayoutManager
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );

        // 6️⃣ Conectamos adapter
        recyclerView.setAdapter(eventAdapter);

        return view;
    }

}
