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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.POJOs.CalendarioSeleccionado;
import com.example.planify.data.POJOs.Tarea;
import com.example.planify.data.dto.DiasConTareaDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.dto.TareaNuevaResponseDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.CalendarioApi;

import com.example.planify.data.network.TareaApi;
import com.example.planify.ui.Adapter.CalendarAdapter;
import com.example.planify.ui.Adapter.EventAdapter;
import com.example.planify.data.POJOs.CalendarDay;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * FRAGMENT MAIN CALENDARIO
 *
 * Este fragment representa la vista PRINCIPAL del calendario seleccionado.
 * Vive dentro de MarcoGeneral y se carga en su fragment_container.
 *
 * Responsabilidades:
 *  - Mostrar el nombre del calendario activo (CalendarioSeleccionado)
 *  - Gestionar el mes actual (anterior / siguiente)
 *  - Generar los días reales del mes (con huecos)
 *  - Mostrar el calendario mensual en forma de grid
 *  - Detectar el click sobre un día y navegar a la vista diaria (VentanaDia)
 *
 * NO se encarga de:
 *  - navegación global (eso es MarcoGeneral)
 *  - decidir qué calendario está activo (eso se hace antes, en el adapter)
 */
public class FragmentMainCalendario extends Fragment {

    // --------------------- CALENDARIO MENSUAL ---------------------
    private Map<LocalDate, List<String>> coloresPorDia = new HashMap<>();
    private RecyclerView recyclerViewCalendar;
    private CalendarAdapter calendarAdapter;
    private List<CalendarDay> calendarDays;

    // Estado del mes actual que se está mostrando
    private Calendar calendarioActual;

    // UI del mes
    private TextView textoMes;
    private Button btnMesAnterior;
    private Button btnMesSiguiente;


    // --------------------- LISTA DE TAREAS (SECUNDARIA) ---------------------

    private RecyclerView recyclerViewTareas;
    private TextView txtEmptyTareas;
    private EventAdapter eventAdapter;
    private List<TareaNuevaResponseDTO> tareas;


    // --------------------- CICLO DE VIDA ---------------------

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_main_calendario, container, false);

        // Mostramos el nombre del calendario seleccionado
        View tituloView = view.findViewById(R.id.titulo);
        TextView titulo = tituloView.findViewById(R.id.textNombreCalendario);
        titulo.setText(CalendarioSeleccionado.nombre);

        // Inicializamos la parte visual del mes (texto + botones)
        inicializarMes(view);

        // Configuramos el RecyclerView del calendario mensual
        configurarCalendarioMensual(view);

        //cargamos las barras
        cargarEventosDelMes();

        // Configuramos la lista de tareas (de momento mock)
        configurarListaTareas(view);

        return view;
    }


    // --------------------- INICIALIZACIÓN DEL MES ---------------------

    /*
     * Inicializa los botones de mes anterior/siguiente
     * y el texto del mes actual.
     */
    private void inicializarMes(View view) {

        View mesView = view.findViewById(R.id.mes);

        textoMes = mesView.findViewById(R.id.nombre_mes);
        btnMesAnterior = mesView.findViewById(R.id.boton_mes_anterior);
        btnMesSiguiente = mesView.findViewById(R.id.boton_mes_siguiente);

        // Estado inicial del calendario: primer día del mes actual
        calendarioActual = Calendar.getInstance();
        calendarioActual.set(Calendar.DAY_OF_MONTH, 1);

        actualizarTituloMes();

        // Cambiar al mes anterior
        btnMesAnterior.setOnClickListener(v -> {
            calendarioActual.add(Calendar.MONTH, -1);
            refrescarCalendario();
        });

        // Cambiar al mes siguiente
        btnMesSiguiente.setOnClickListener(v -> {
            calendarioActual.add(Calendar.MONTH, 1);
            refrescarCalendario();
        });
    }


    // --------------------- CALENDARIO MENSUAL ---------------------

    /*
     * Configura el RecyclerView que representa el calendario mensual.
     * Se usa un GridLayout de 7 columnas (días de la semana).
     */
    private void configurarCalendarioMensual(View view) {

        recyclerViewCalendar = view.findViewById(R.id.recycler_Calendar);

        recyclerViewCalendar.setLayoutManager(
                new GridLayoutManager(getContext(), 7)
        );

        // Generamos los días del mes actual
        calendarDays = generarDiasDelMes(calendarioActual);

        // Creamos el adapter del calendario
        calendarAdapter = new CalendarAdapter(
                calendarDays,
                coloresPorDia,
                v -> {
                    int dayNumber = (int) v.getTag();
                    abrirVentanaDia(dayNumber);
                }
        );

        recyclerViewCalendar.setAdapter(calendarAdapter);
    }


    /*
     *Explicado detalladamente, con nuestro calendario temp, lo que hacemos es crear una clonación
     * del calendario que le estamos pasando(to-do esto antes de pintar nada), se setea la fecha
     * forzada al dia 1, y luego con firstDayofWeek se coge el dia de la semana en que empieza
     *
     * Genera la lista de días del mes:
     *  - añade huecos antes del día 1
     *  - añade los días reales del mes
     *
     * El adapter NO calcula nada, solo pinta lo que recibe.
     */
    private List<CalendarDay> generarDiasDelMes(Calendar calendar) {

        List<CalendarDay> days = new ArrayList<>();

        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Ajuste para que lunes sea 1 y domingo 7
        int adjustedFirstDay =
                (firstDayOfWeek == Calendar.SUNDAY) ? 7 : firstDayOfWeek - 1;

        // Año y mes actuales (necesarios para LocalDate)
        int year = temp.get(Calendar.YEAR);
        int month = temp.get(Calendar.MONTH) + 1; // Calendar empieza en 0

        // Huecos antes del día 1
        for (int i = 1; i < adjustedFirstDay; i++) {
            days.add(new CalendarDay(null, null));
        }

        // Días reales
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate fecha = LocalDate.of(year, month, day);
            days.add(new CalendarDay(day, fecha));
        }

        return days;
    }


    /*
     * Actualiza el texto del mes que se muestra arriba.
     */
    private void actualizarTituloMes() {
        SimpleDateFormat sdf =
                new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        textoMes.setText(sdf.format(calendarioActual.getTime()));
    }


    /*
     * Se llama al cambiar de mes.
     * Regenera los días y notifica al adapter.
     */
    private void refrescarCalendario() {

        calendarDays.clear();
        calendarDays.addAll(generarDiasDelMes(calendarioActual));

        calendarAdapter.notifyDataSetChanged();
        actualizarTituloMes();

        cargarEventosDelMes();
    }


    // --------------------- NAVEGACIÓN A VISTA DIARIA ---------------------

    /*
     * Abre la vista diaria (VentanaDia) para el día seleccionado.
     * La navegación se hace por fragments, no por activities.
     */
    private void abrirVentanaDia(int dayNumber) {

        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.YEAR, calendarioActual.get(Calendar.YEAR));
        fecha.set(Calendar.MONTH, calendarioActual.get(Calendar.MONTH));
        fecha.set(Calendar.DAY_OF_MONTH, dayNumber);

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String fechaString = sdf.format(fecha.getTime());

        // Datos que necesitará el fragment diario
        Bundle bundle = new Bundle();
        bundle.putString("FECHA", fechaString);
        bundle.putInt("ID_CAL", CalendarioSeleccionado.idCal);

        VentanaDia ventanaDia = new VentanaDia();
        ventanaDia.setArguments(bundle);

        // Reemplazamos el fragment actual y lo añadimos al backstack
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ventanaDia)
                .addToBackStack(null)
                .commit();
    }


    // --------------------- LISTA DE TAREAS (MOCK) ---------------------

    /*
     * Configura la lista de tareas del día.
     */
    private void configurarListaTareas(View view) {

        recyclerViewTareas = view.findViewById(R.id.barra_eventos);
        txtEmptyTareas = view.findViewById(R.id.txtEmptyTareas);

        tareas = new ArrayList<>(); // empieza vacío

        eventAdapter = new EventAdapter(tareas);

        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTareas.setAdapter(eventAdapter);

        int idCal = CalendarioSeleccionado.idCal;
        cargarTareasDesdeApi(idCal);

    }
    private void cargarTareasDesdeApi(int idCal) {

        TareaApi api = ApiCliente.getRetrofit().create(TareaApi.class);

        api.verTareas(idCal).enqueue(new Callback<List<TareaNuevaResponseDTO>>() {

            @Override
            public void onResponse(
                    Call<List<TareaNuevaResponseDTO>> call,
                    Response<List<TareaNuevaResponseDTO>> response
            ) {

                if (!response.isSuccessful() || response.body() == null) {
                    recyclerViewTareas.setVisibility(View.GONE);
                    txtEmptyTareas.setVisibility(View.VISIBLE);
                    return;
                }

                List<TareaNuevaResponseDTO> lista = response.body();

                if (lista.isEmpty()) {
                    recyclerViewTareas.setVisibility(View.GONE);
                    txtEmptyTareas.setVisibility(View.VISIBLE);
                } else {
                    txtEmptyTareas.setVisibility(View.GONE);
                    recyclerViewTareas.setVisibility(View.VISIBLE);
                    eventAdapter.setTareas(lista);
                }
            }

            @Override
            public void onFailure(
                    Call<List<TareaNuevaResponseDTO>> call,
                    Throwable t
            ) {
                Toast.makeText(
                        getContext(),
                        "Error al cargar tareas",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void cargarEventosDelMes() {

        int anioActual = calendarioActual.get(Calendar.YEAR);
        int mesActual = calendarioActual.get(Calendar.MONTH) + 1;

        TareaApi tareaApi =
                ApiCliente.getRetrofit().create(TareaApi.class);

        tareaApi.verEventosMes(
                CalendarioSeleccionado.idCal,
                anioActual,
                mesActual
        ).enqueue(new Callback<List<DiasConTareaDTO>>() {

            @Override
            public void onResponse(
                    Call<List<DiasConTareaDTO>> call,
                    Response<List<DiasConTareaDTO>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {

                    coloresPorDia.clear();

                    for (DiasConTareaDTO dto : response.body()) {
                        LocalDate fecha = LocalDate.parse(dto.getFecha());
                        coloresPorDia.put(fecha, dto.getColores());
                    }

                    calendarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(
                    Call<List<DiasConTareaDTO>> call,
                    Throwable t
            ) {
                Toast.makeText(
                        getContext(),
                        "Error al cargar eventos del mes",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
