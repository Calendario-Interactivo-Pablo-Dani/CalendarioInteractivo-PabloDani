package com.example.planify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planify.R;
import com.example.planify.data.dto.LoginRequestDTO;
import com.example.planify.data.dto.LoginResponseDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.AuthApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText txEmail;
    private  EditText txPassword;
    private  AuthApi authApi;
    private View.OnClickListener listenerRegistro=new View.OnClickListener() {


        public void onClick(View view) {
            Intent i=new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }
    };

    //esto aun esta sin flitros(que haya campos escritos) es solo para ir probando
    private View.OnClickListener listenerLogin=new View.OnClickListener() {
        public void onClick(View view) {
//            Intent i=new Intent(getApplicationContext(), MarcoGeneral.class);
//            startActivity(i);
            String email = txEmail.getText().toString();
            String password = txPassword.getText().toString();

            LoginRequestDTO request = new LoginRequestDTO(email, password);

            authApi.login(request).enqueue(new Callback<LoginResponseDTO>() {
                @Override
                public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponseDTO usuario = response.body();
                        Toast.makeText(MainActivity.this,
                                "Bienvenido " + usuario.getNombre(),
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MarcoGeneral.class);
                        startActivity(i);
                    } else if (response.code() == 401) {
                        Toast.makeText(MainActivity.this,
                                "Email o contraseña incorrectos",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                    Toast.makeText(MainActivity.this,
                            "Error de conexión",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Registro*/
        Button bRegister=(Button) findViewById(R.id.button_registro);
        bRegister.setOnClickListener(listenerRegistro);
        /*Login*/
        Button bLogin=(Button) findViewById(R.id.button_login);
        bLogin.setOnClickListener(listenerLogin);
        /*Email y Contraseña*/
        txEmail = findViewById(R.id.user_email);
        txPassword= findViewById(R.id.password);
        /*Api*/
        authApi = ApiCliente.getRetrofit().create(AuthApi.class);

    }
}