package com.example.planify.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.Tarea;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class FragmentMainCalendario extends Fragment {

    private RecyclerView recyclerViewCalendar;
    private CalendarAdapter calendarAdapter;
    private List<CalendarDay> calendarDays;


    private RecyclerView recyclerViewTareas;
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

        recyclerViewCalendar = view.findViewById(R.id.recycler_Calendar);

        recyclerViewCalendar.setLayoutManager(
                new GridLayoutManager(getContext(), 7)
        );

        calendarDays = generarDiasDelMes(2026, 3);

        calendarAdapter = new CalendarAdapter(calendarDays);

        recyclerViewCalendar.setAdapter(calendarAdapter);


        // 2️⃣ Encontramos el RecyclerView
        recyclerViewTareas = view.findViewById(R.id.barra_eventos);

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

        tareas.add(t1);
        tareas.add(t2);
        tareas.add(t3);
        tareas.add(t4);

        // 4️⃣ Creamos el adapter
        eventAdapter = new EventAdapter(tareas);

        // 5️⃣ LayoutManager
        recyclerViewTareas.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );

        // 6️⃣ Conectamos adapter
        recyclerViewTareas.setAdapter(eventAdapter);

        return view;

    }

    private List<CalendarDay> generarDiasDelMes(int year, int month) {

        List<CalendarDay> days = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        // Calendar.MONTH va de 0 a 11
        calendar.set(year, month - 1, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Convertimos para que lunes = 1
        int adjustedFirstDay =
                (firstDayOfWeek == Calendar.SUNDAY) ? 7 : firstDayOfWeek - 1;

        // Huecos antes del día 1
        for (int i = 1; i < adjustedFirstDay; i++) {
            days.add(new CalendarDay(null));
        }

        // Días reales
        for (int day = 1; day <= daysInMonth; day++) {
            days.add(new CalendarDay(day));
        }

        return days;
    }

}
