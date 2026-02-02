package com.example.planify.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planify.R;
import com.example.planify.data.dto.CalendarioResponseDTO;
import com.example.planify.data.dto.CalendarioRequestDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.CalendarioApi;
import com.example.planify.data.session.SessionManager;

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
        inicializarVistas();
        cargarCalendarios();
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
