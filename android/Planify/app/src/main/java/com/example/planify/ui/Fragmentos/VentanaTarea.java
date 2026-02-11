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

import androidx.appcompat.app.AlertDialog;
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
import java.util.Locale;

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
    private AppCompatButton btnEliminarTarea;

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

        // ===============================
        // REFERENCIAS UI
        // ===============================
        etNombre = view.findViewById(R.id.etNombre);
        etHora = view.findViewById(R.id.etHora);

        rgTipo = view.findViewById(R.id.rgTipo);
        rgEstado = view.findViewById(R.id.rgEstado);

        tvColorCasilla = view.findViewById(R.id.tvColorCasilla);
        btnConfirmar = view.findViewById(R.id.btnConfirmarCrearTarea);
        btnEliminarTarea = view.findViewById(R.id.btnEliminarTarea);

        btnEliminarTarea.setOnClickListener(v -> borrarTarea());

        tvColorCasilla.setOnClickListener(v -> mostrarSelectorColor(v));


        // ===============================
        // ESTADO INICIAL
        // ===============================
        fechaSeleccionada = null;
        horaSeleccionada = null;
        esEdicion = false;
        idTareaEdicion = -1;

        // ===============================
        // ARGUMENTOS
        // ===============================
        Bundle args = getArguments();
        if (args != null) {

            esEdicion = args.getBoolean("ES_EDICION", false);

            if (esEdicion) {
                idTareaEdicion = args.getInt("ID_TAREA", -1);
            }

            // --------
            // FECHA
            // --------

            // Crear tarea desde día concreto
            if (args.containsKey("FECHA_DIA") && args.getString("FECHA_DIA") != null) {
                fechaSeleccionada = LocalDate.parse(args.getString("FECHA_DIA"));
            }

            // Editar tarea existente
            else if (args.containsKey("FECHA_LIM") && args.getString("FECHA_LIM") != null) {
                LocalDateTime fechaLim =
                        LocalDateTime.parse(args.getString("FECHA_LIM"));

                fechaSeleccionada = fechaLim.toLocalDate();
                horaSeleccionada = fechaLim.toLocalTime();

                etHora.setText(
                        String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                horaSeleccionada.getHour(),
                                horaSeleccionada.getMinute()
                        )
                );
            }

            // --------
            // CAMPOS DE EDICIÓN
            // --------
            if (esEdicion) {

                etNombre.setText(args.getString("NOMBRE"));

                // TIPO
                String tipo = args.getString("TIPO");
                if ("tarea".equalsIgnoreCase(tipo)) {
                    rgTipo.check(R.id.rbTarea);
                } else if ("evento".equalsIgnoreCase(tipo)) {
                    rgTipo.check(R.id.rbEvento);
                }

                // ESTADO
                String estado = args.getString("ESTADO");
                if ("pendiente".equalsIgnoreCase(estado)) {
                    rgEstado.check(R.id.rbPendiente);
                } else if ("finalizada".equalsIgnoreCase(estado)) {
                    rgEstado.check(R.id.rbFinalizada);
                }

                // COLOR
                String color = args.getString("COLOR");
                if (color != null) {
                    aplicarColor(color);
                }

                btnConfirmar.setText("Modificar tarea");
            }
        }

        // ===============================
        // CONFIGURACIONES
        // ===============================
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
                finalizarTarea();
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
        popupView.findViewById(R.id.color_naranja).setOnClickListener(v -> {
            aplicarColor("NARANJA");
            popupWindow.dismiss();
        });
        popupView.findViewById(R.id.color_negro).setOnClickListener(v -> {
            aplicarColor("NEGRO");
            popupWindow.dismiss();
        });
        popupView.findViewById(R.id.color_rosa).setOnClickListener(v -> {
            aplicarColor("ROSA");
            popupWindow.dismiss();
        });
        popupView.findViewById(R.id.color_gris).setOnClickListener(v -> {
            aplicarColor("GRIS");
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.color_blanco).setOnClickListener(v -> {
            aplicarColor("BLANCO");
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
            case "NARANJA":
                colorInt = Color.parseColor("#FF8000");
                break;
            case "NEGRO":
                colorInt = Color.parseColor("#000000");
                break;
            case "ROSA":
                colorInt = Color.parseColor("#C11CB2");
                break;
            case "GRIS":
                colorInt = Color.parseColor("#9B9B9B");
                break;
            case "BLANCO":
                colorInt = Color.parseColor("#FFFFFF");
                break;

            default:
                colorInt = Color.WHITE;
        }

        tvColorCasilla.setBackgroundTintList(
                ColorStateList.valueOf(colorInt)
        );
    }
    public void borrarTarea(){
        // ===============================
        // CASO 1: NO ES EDICIÓN
        // ===============================
        if (!esEdicion) {
            Toast.makeText(
                    getContext(),
                    "Primero tienes que crear la tarea para poder eliminarla",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Seguridad extra
        if (idTareaEdicion == -1) {
            Toast.makeText(
                    getContext(),
                    "No se puede eliminar esta tarea",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // ===============================
        // CONFIRMACIÓN
        // ===============================
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás seguro de que quieres eliminar esta tarea?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    // ===============================
                    // LLAMADA A SPRING
                    // ===============================
                    TareaApi tareaApi =
                            ApiCliente.getRetrofit().create(TareaApi.class);

                    tareaApi.eliminarTarea(idTareaEdicion)
                            .enqueue(new Callback<Void>() {

                                @Override
                                public void onResponse(
                                        Call<Void> call,
                                        Response<Void> response
                                ) {
                                    if (response.isSuccessful()) {

                                        Toast.makeText(
                                                getContext(),
                                                "Tarea eliminada correctamente",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        getParentFragmentManager().popBackStack();

                                    } else {
                                        Toast.makeText(
                                                getContext(),
                                                "Error al eliminar la tarea",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }

                                @Override
                                public void onFailure(
                                        Call<Void> call,
                                        Throwable t
                                ) {
                                    Toast.makeText(
                                            getContext(),
                                            "Error de conexión",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void finalizarTarea(){
        // ===============================
        // CASO 1: NO ES EDICIÓN
        // ===============================
        if (!esEdicion) {
            Toast.makeText(
                    getContext(),
                    "Primero tienes que crear la tarea para poder finalizarla",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // Seguridad extra
        if (idTareaEdicion == -1) {
            Toast.makeText(
                    getContext(),
                    "No se puede finalizar esta tarea",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // ===============================
        // CONFIRMACIÓN
        // ===============================
        new AlertDialog.Builder(requireContext())
                .setTitle("Finalizar tarea")
                .setMessage("¿Estás seguro de que quieres finalizar esta tarea?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    // ===============================
                    // LLAMADA A SPRING
                    // ===============================
                    TareaApi tareaApi =
                            ApiCliente.getRetrofit().create(TareaApi.class);

                    tareaApi.eliminarTarea(idTareaEdicion)
                            .enqueue(new Callback<Void>() {

                                @Override
                                public void onResponse(
                                        Call<Void> call,
                                        Response<Void> response
                                ) {
                                    if (response.isSuccessful()) {

                                        Toast.makeText(
                                                getContext(),
                                                "Tarea finalizada correctamente",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        getParentFragmentManager().popBackStack();

                                    } else {
                                        Toast.makeText(
                                                getContext(),
                                                "Error al finalizar la tarea",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }

                                @Override
                                public void onFailure(
                                        Call<Void> call,
                                        Throwable t
                                ) {
                                    Toast.makeText(
                                            getContext(),
                                            "Error de conexión",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}
