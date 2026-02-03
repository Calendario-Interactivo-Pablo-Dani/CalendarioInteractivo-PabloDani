package com.example.planify.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.dto.CalendarioResponseDTO;
import com.example.planify.data.dto.CalendarioRequestDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.CalendarioApi;
import com.example.planify.data.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentanaGeneral extends AppCompatActivity {
    private RecyclerView recyclerCalendarios;
    private ListaCalendariosAdapter ListaCalendariosAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ventana_general);
        TextView txtBienvenida = findViewById(R.id.txtBienvenida);
        SessionManager sessionManager = new SessionManager(this);
        String nombre = sessionManager.getName();
        txtBienvenida.setText("¡Bienvenido " + nombre +  "!");


        inicializarVistas();
        cargarCalendarios();
        //Button btnCrearCalendario = findViewById(R.id.btnCrearCalendario);
        FloatingActionButton btnCrearCalendario = findViewById(R.id.btnCrearCalendario);
        btnCrearCalendario.setOnClickListener(v -> {
            mostrarDialogCrearCalendario();
        });
    }

    //NOS MUESTRA EL DIALOGO DE CREAR CALENDARIO
    private void mostrarDialogCrearCalendario() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater()
                .inflate(R.layout.dialogo_crear_calendario, null);

        // 1) Referencias a los EditText del diálogo (IMPORTANTE: usando "view")
        EditText etNombre = view.findViewById(R.id.etNombreCalendario);
        EditText etCodigo = view.findViewById(R.id.etCodigoCalendario);

        // 2) Referencia al botón "Crear" que está dentro del diálogo
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarCrearCalendario);

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        // 3) Click del botón "Crear" (dentro del diálogo)
        btnConfirmar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String codigoTexto = etCodigo.getText().toString().trim();

            // Validación básica
            if (nombre.isEmpty() || codigoTexto.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int codigo;
            try {
                codigo = Integer.parseInt(codigoTexto);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El código debe ser un número", Toast.LENGTH_SHORT).show();
                return;
            }
            añadirCalendarioBD(nombre, codigo);
            // (Opcional) Cerrar el diálogo después de pulsar crear
            dialog.dismiss();
        });
    }
    //Llamamos a la consulta del API para crear el calendario
    private void añadirCalendarioBD(String nombre, int codigo) {
        // DTO request
        CalendarioRequestDTO request =
                new CalendarioRequestDTO(nombre, codigo);

        // Usuario logueado
        SessionManager sessionManager = new SessionManager(this);
        int idUser = sessionManager.getIdUser();

        // API
        CalendarioApi calendarioApi =
                ApiCliente.getRetrofit().create(CalendarioApi.class);

        Call<CalendarioResponseDTO> call =
                calendarioApi.crearCalendario(idUser, request);

        call.enqueue(new Callback<CalendarioResponseDTO>() {
            @Override
            public void onResponse(Call<CalendarioResponseDTO> call,
                                   Response<CalendarioResponseDTO> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(VentanaGeneral.this,
                            "Calendario creado correctamente",
                            Toast.LENGTH_SHORT).show();
                    cargarCalendarios();
                } else {
                    Toast.makeText(VentanaGeneral.this,
                            "Error al crear calendario",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalendarioResponseDTO> call, Throwable t) {
                Toast.makeText(VentanaGeneral.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void inicializarVistas() {
        recyclerCalendarios = findViewById(R.id.recyclerCalendarios);
        recyclerCalendarios.setLayoutManager(new LinearLayoutManager(this));
    }
    private void cargarCalendarios() {

        // Sesión
        SessionManager sessionManager = new SessionManager(this);
        int idUser = sessionManager.getIdUser();

        // API
        CalendarioApi calendarioApi =
                ApiCliente.getRetrofit().create(CalendarioApi.class);

        Call<List<CalendarioResponseDTO>> call =
                calendarioApi.getMisCalendarios(idUser);

        call.enqueue(new Callback<List<CalendarioResponseDTO>>() {

            @Override
            public void onResponse(Call<List<CalendarioResponseDTO>> call,
                                   Response<List<CalendarioResponseDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<CalendarioResponseDTO> calendarios = response.body();
                    mostrarCalendarios(calendarios);

                } else {
                    Log.e("CALENDARIO", "Error response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CalendarioResponseDTO>> call, Throwable t) {
                Log.e("CALENDARIO", "Error conexión", t);
                Toast.makeText(VentanaGeneral.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarCalendarios(List<CalendarioResponseDTO> calendarios) {
        ListaCalendariosAdapter = new ListaCalendariosAdapter(calendarios);
        recyclerCalendarios.setAdapter(ListaCalendariosAdapter);
    }

}
