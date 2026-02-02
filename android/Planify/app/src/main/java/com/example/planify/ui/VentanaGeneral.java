package com.example.planify.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ventana_general);
        // Sesión (para sacar el idUser)
        SessionManager sessionManager = new SessionManager(this);
        int idUser = sessionManager.getIdUser();

        // Crear la API
        CalendarioApi calendarioApi =
                ApiCliente.getRetrofit().create(CalendarioApi.class);

        // 🔹 PROBAR: obtener mis calendarios
        Call<List<CalendarioResponseDTO>> call = calendarioApi.getMisCalendarios(idUser);

        call.enqueue(new Callback<List<CalendarioResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CalendarioResponseDTO>> call,
                                   Response<List<CalendarioResponseDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<CalendarioResponseDTO> calendarios = response.body();

                    Log.d("CALENDARIO", "Calendarios recibidos: " + calendarios.size());

                    for (CalendarioResponseDTO c : calendarios) {
                        Log.d("CALENDARIO",
                                c.getIdCal() + " - " + c.getNombre() + " (" + c.getRol() + ")");
                    }
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

}
