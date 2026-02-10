package com.example.planify.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.planify.data.session.SessionManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    /*Datos login*/
    private EditText txEmail;
    private  EditText txPassword;
    /*Api*/
    private  AuthApi authApi;
    private View.OnClickListener listenerRegistro=new View.OnClickListener() {


        public void onClick(View view) {
            Intent i=new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }
    };

    //esto aun esta sin flitros(que haya campos escritos) es solo para ir probandoa
    private View.OnClickListener listenerLogin=new View.OnClickListener() {
        public void onClick(View view) {
            String email = txEmail.getText().toString();
            String password = txPassword.getText().toString();
            /*COMPROBAMOS QUE LOS CAMPOS NO ESTAN VACIOS*/
            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(Login.this,
                        "Rellena todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // COMPROBAMOS FORMATO DE EMAIL VALIDO
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(Login.this,
                        "Email no válido",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            //CONTRASEÑA MINIMA
            if (password.length() < 3) {
                Toast.makeText(Login.this,
                        "La contraseña debe tener al menos 3 caracteres",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequestDTO request = new LoginRequestDTO(email, password);

            authApi.login(request).enqueue(new Callback<LoginResponseDTO>() {
                @Override
                public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        //CREAMOS DTO
                        LoginResponseDTO usuario = response.body();
                        //GUARDAR SESIÓN
                        SessionManager session = new SessionManager(Login.this);
                        session.saveSession(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getUsername(),
                                usuario.getEmail(),
                                usuario.getTelefono()
                        );
                        //TEXTO CONFIRMACIÓN
                        Toast.makeText(Login.this,
                                "Bienvenido " + usuario.getNombre(),
                                Toast.LENGTH_SHORT).show();
                        //PASAMOS A LA PANTALLA GENERAL
                        Intent i = new Intent(getApplicationContext(), Home.class);
                        startActivity(i);
                        finish();
                    } else if (response.code() == 401) {
                        Toast.makeText(Login.this,
                                "Email o contraseña incorrectos",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                    Toast.makeText(Login.this,
                            "Error de conexión",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*MANEJO DE SESION*/
        SessionManager session = new SessionManager(this);
        if (session.isLogged()) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        /*Registro*/
        Button bRegister=(Button) findViewById(R.id.button_registro);
        bRegister.setOnClickListener(listenerRegistro);
        /*Login*/
        Button bLogin=(Button) findViewById(R.id.button_login);
        bLogin.setOnClickListener(listenerLogin);
        /*Email y Contraseña(login)*/
        txEmail = findViewById(R.id.user_email);
        txPassword= findViewById(R.id.password);
        /*Api*/
        authApi = ApiCliente.getRetrofit().create(AuthApi.class);
    }
}