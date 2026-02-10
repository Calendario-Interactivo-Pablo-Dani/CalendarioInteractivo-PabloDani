package com.example.planify.ui.Fragmentos;

import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import com.example.planify.R;
import com.example.planify.data.POJOs.CalendarioSeleccionado;
import com.example.planify.data.dto.TareaNuevaRequestDTO;
import com.example.planify.data.dto.TareaNuevaResponseDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.TareaApi;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentanaTarea extends Fragment {
    private boolean esEdicion = false;
    private int idTareaEdicion = -1;


    private String colorSeleccionado = "BLANCO";
    // TEXTOS
    private EditText etNombre;
    private EditText etHora;

    // RADIO GROUPS
    private RadioGroup rgTipo;
    private RadioGroup rgEstado;

    // BOTÓN
    private AppCompatButton btnConfirmar;

    private String fechaDia;
    // COLOR
    private LocalDate fechaSeleccionada;
    private LocalTime horaSeleccionada;

    private TextView tvColorCasilla;

    public VentanaTarea() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.tarea_creacion_ventana, container, false);

        // REFERENCIAS
        etNombre = view.findViewById(R.id.etNombre);
        etHora = view.findViewById(R.id.etHora);

        rgTipo = view.findViewById(R.id.rgTipo);
        rgEstado = view.findViewById(R.id.rgEstado);

        tvColorCasilla = view.findViewById(R.id.tvColorCasilla);
        btnConfirmar = view.findViewById(R.id.btnConfirmarCrearTarea);

        tvColorCasilla.setOnClickListener(v -> {
            mostrarSelectorColor(v);
        });


        Bundle args = getArguments();
        fechaSeleccionada = null;
        if (args != null) {
            esEdicion = args.getBoolean("ES_EDICION", false);

            if (esEdicion) {
                idTareaEdicion = args.getInt("ID_TAREA", -1);
            }

            // Caso 1: crear tarea desde un día concreto
            if (args.containsKey("FECHA_DIA") && args.getString("FECHA_DIA") != null) {
                fechaSeleccionada = LocalDate.parse(args.getString("FECHA_DIA"));
            }

            // Caso 2: editar tarea existente
            else if (args.containsKey("FECHA_LIM") && args.getString("FECHA_LIM") != null) {
                LocalDateTime fechaLim =
                        LocalDateTime.parse(args.getString("FECHA_LIM"));
                fechaSeleccionada = fechaLim.toLocalDate();

                // además, precargar la hora
                horaSeleccionada = fechaLim.toLocalTime();
                etHora.setText(horaSeleccionada.toString());
            }

            // Campos comunes de edición
            if (args.getBoolean("ES_EDICION", false)) {
                etNombre.setText(args.getString("NOMBRE"));
            }
        }

        configurarHora();
        configurarEstado();
        configurarConfirmar();

        return view;
    }

    // =========================
    // HORA (TimePicker)
    // =========================
    private void configurarHora() {
        etHora.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
            int minutoActual = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(
                    getContext(),
                    (view, hourOfDay, minute) -> {
                        horaSeleccionada = LocalTime.of(hourOfDay, minute);
                        etHora.setText(horaSeleccionada.toString());
                    },
                    horaActual,
                    minutoActual,
                    true
            );

            dialog.show();
        });
    }

    // =========================
    // ESTADO (cerrar si finalizada)
    // =========================
    private void configurarEstado() {
        rgEstado.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbFinalizada) {
                // Cerrar el fragment
                getParentFragmentManager().popBackStack();
            }
        });
    }

    // =========================
    // CONFIRMAR
    // =========================
    private void configurarConfirmar() {

        btnConfirmar.setOnClickListener(v -> {

            // ===============================
            // VALIDACIONES
            // ===============================
            String nombre = etNombre.getText().toString().trim();

            int tipoSeleccionado = rgTipo.getCheckedRadioButtonId();
            int estadoSeleccionado = rgEstado.getCheckedRadioButtonId();

            if (nombre.isEmpty()) {
                etNombre.setError("Introduce un nombre");
                return;
            }

            if (horaSeleccionada == null) {
                etHora.setError("Selecciona una hora");
                return;
            }

            if (fechaSeleccionada == null) {
                Toast.makeText(
                        getContext(),
                        "Fecha no válida",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // ===============================
            // DTO PARA SPRING
            // ===============================
            TareaNuevaRequestDTO request = new TareaNuevaRequestDTO();
            request.setIdCal(CalendarioSeleccionado.idCal);
            request.setNombre(nombre);

            LocalDateTime fechaLim =
                    LocalDateTime.of(fechaSeleccionada, horaSeleccionada);

            request.setFechaLim(fechaLim.toString());

            // tipo
            if (tipoSeleccionado == R.id.rbTarea) {
                request.setTipo("tarea");
            } else if (tipoSeleccionado == R.id.rbEvento) {
                request.setTipo("evento");
            }

            // estado
            if (estadoSeleccionado == R.id.rbPendiente) {
                request.setEstado("pendiente");
            } else if (estadoSeleccionado == R.id.rbFinalizada) {
                request.setEstado("finalizada");
            }

            // color
            request.setColor(colorSeleccionado);

            // ===============================
            // LLAMADA A SPRING
            // ===============================
            TareaApi tareaApi =
                    ApiCliente.getRetrofit().create(TareaApi.class);

            Call<TareaNuevaResponseDTO> call;

            if (esEdicion) {

                if (idTareaEdicion == -1) {
                    Toast.makeText(
                            getContext(),
                            "ID de tarea no válido",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                call = tareaApi.modificarTarea(idTareaEdicion, request);

            } else {
                call = tareaApi.crearTarea(request);
            }

            call.enqueue(new Callback<TareaNuevaResponseDTO>() {

                @Override
                public void onResponse(
                        Call<TareaNuevaResponseDTO> call,
                        Response<TareaNuevaResponseDTO> response
                ) {
                    if (response.isSuccessful()) {

                        Toast.makeText(
                                getContext(),
                                esEdicion
                                        ? "Tarea modificada correctamente"
                                        : "Tarea creada correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                        getParentFragmentManager().popBackStack();

                    } else {
                        Toast.makeText(
                                getContext(),
                                esEdicion
                                        ? "Error al modificar la tarea"
                                        : "Error al crear la tarea",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<TareaNuevaResponseDTO> call,
                        Throwable t
                ) {
                    Toast.makeText(
                            getContext(),
                            "Error de conexión",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }


    private void mostrarSelectorColor(View anchor) {

        View popupView = LayoutInflater.from(getContext())
                .inflate(R.layout.popup_selector_color, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupView.findViewById(R.id.color_rojo).setOnClickListener(v -> {
            aplicarColor("ROJO");
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_azul).setOnClickListener(v -> {
            aplicarColor("AZUL");
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_verde).setOnClickListener(v -> {
            aplicarColor("VERDE");
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_amarillo).setOnClickListener(v -> {
            aplicarColor("AMARILLO");
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_morado).setOnClickListener(v -> {
            aplicarColor("MORADO");
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(anchor);
    }

    private void aplicarColor(String color) {

        colorSeleccionado = color;

        int colorInt;

        switch (color) {
            case "ROJO":
                colorInt = Color.parseColor("#E53935");
                break;
            case "AZUL":
                colorInt = Color.parseColor("#1E88E5");
                break;
            case "VERDE":
                colorInt = Color.parseColor("#43A047");
                break;
            case "AMARILLO":
                colorInt = Color.parseColor("#FDD835");
                break;
            case "MORADO":
                colorInt = Color.parseColor("#8E24AA");
                break;
            default:
                colorInt = Color.WHITE;
        }

        tvColorCasilla.setBackgroundTintList(
                ColorStateList.valueOf(colorInt)
        );
    }

}
