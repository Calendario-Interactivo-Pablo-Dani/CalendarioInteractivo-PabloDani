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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragmentMainCalendario extends Fragment {

    private RecyclerView recyclerViewCalendar;
    private CalendarAdapter calendarAdapter;
    private List<CalendarDay> calendarDays;


    private RecyclerView recyclerViewTareas;
    private EventAdapter eventAdapter;
    private List<Tarea> tareas;


    private Calendar calendarioActual;
    private TextView textoMes;
    private Button btnMesAnterior;
    private Button btnMesSiguiente;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // 1️⃣ Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_main_calendario, container, false);

        View mesView = view.findViewById(R.id.mes);

        textoMes = mesView.findViewById(R.id.nombre_mes);
        btnMesAnterior = mesView.findViewById(R.id.boton_mes_anterior);
        btnMesSiguiente = mesView.findViewById(R.id.boton_mes_siguiente);

        btnMesAnterior.setOnClickListener(v -> {
            calendarioActual.add(Calendar.MONTH, -1);
            refrescarCalendario();
        });

        btnMesSiguiente.setOnClickListener(v -> {
            calendarioActual.add(Calendar.MONTH, 1);
            refrescarCalendario();
        });

        recyclerViewCalendar = view.findViewById(R.id.recycler_Calendar);

        recyclerViewCalendar.setLayoutManager(
                new GridLayoutManager(getContext(), 7)
        );

        calendarioActual = Calendar.getInstance();
        calendarioActual.set(Calendar.DAY_OF_MONTH, 1);
        actualizarTituloMes();

        // generar días del mes actual
        calendarDays = generarDiasDelMes(calendarioActual);

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

    private List<CalendarDay> generarDiasDelMes(Calendar calendar) {

        List<CalendarDay> days = new ArrayList<>();

        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        int adjustedFirstDay =
                (firstDayOfWeek == Calendar.SUNDAY) ? 7 : firstDayOfWeek - 1;

        for (int i = 1; i < adjustedFirstDay; i++) {
            days.add(new CalendarDay(null));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            days.add(new CalendarDay(day));
        }

        return days;
    }

    private void actualizarTituloMes() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        textoMes.setText(sdf.format(calendarioActual.getTime()));
    }

    private void refrescarCalendario() {

        calendarDays.clear();
        calendarDays.addAll(generarDiasDelMes(calendarioActual));

        calendarAdapter.notifyDataSetChanged();
        actualizarTituloMes();
    }


}
