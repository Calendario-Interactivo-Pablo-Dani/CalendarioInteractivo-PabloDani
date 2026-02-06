package com.example.planify.ui;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.planify.R;

import java.util.Calendar;
import java.util.Locale;

public class VentanaTarea extends Fragment {

    // TEXTOS
    private EditText etNombre;
    private EditText etDescripcion;
    private EditText etHora;

    // RADIO GROUPS
    private RadioGroup rgTipo;
    private RadioGroup rgEstado;

    // BOTÓN
    private AppCompatButton btnConfirmar;

    // COLOR
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
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etHora = view.findViewById(R.id.etHora);

        rgTipo = view.findViewById(R.id.rgTipo);
        rgEstado = view.findViewById(R.id.rgEstado);

        tvColorCasilla = view.findViewById(R.id.tvColorCasilla);
        btnConfirmar = view.findViewById(R.id.btnConfirmarCrearTarea);

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
                        String horaFormateada = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                hourOfDay,
                                minute
                        );
                        etHora.setText(horaFormateada);
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
                FragmentMainCalendario fragmentMainDesdeTarea = new FragmentMainCalendario();

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentMainDesdeTarea)
                        .commit();
            }
        });
    }

    // =========================
    // CONFIRMAR
    // =========================
    private void configurarConfirmar() {
        btnConfirmar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String hora = etHora.getText().toString().trim();

            int tipoSeleccionado = rgTipo.getCheckedRadioButtonId();
            int estadoSeleccionado = rgEstado.getCheckedRadioButtonId();

            if (nombre.isEmpty()) {
                etNombre.setError("Introduce un nombre");
                return;
            }

            if (hora.isEmpty()) {
                etHora.setError("Selecciona una hora");
                return;
            }

            // AQUÍ luego:
            // - crear objeto Tarea
            // - enviar al backend
            // - refrescar calendario

            Toast.makeText(
                    getContext(),
                    "Tarea creada correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            // cerrar ventana
            FragmentMainCalendario fragmentMainDesdeTarea = new FragmentMainCalendario();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentMainDesdeTarea)
                    .commit();
        });
    }
}
