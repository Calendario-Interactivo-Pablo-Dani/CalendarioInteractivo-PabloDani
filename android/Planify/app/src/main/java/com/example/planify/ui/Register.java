package com.example.planify.ui;

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
import com.example.planify.data.dto.RegisterRequestDTO;
import com.example.planify.data.dto.RegisterResponseDTO;
import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.AuthApi;
import com.example.planify.data.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    /*Datos registro*/
    private EditText txtNombre;
    private EditText txtEmail;

    private EditText txtTelefono;
    private EditText txtUsername;
    private EditText txtPassword;

    //API
    private AuthApi authApi;


    Button bRegister;
    Button bLogin;
    private View.OnClickListener listenerLogin=new View.OnClickListener() {

        public void onClick(View view) {
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };


    private View.OnClickListener listenerEntrar=new View.OnClickListener() {

        public void onClick(View view) {
            String nombre = txtNombre.getText().toString();
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            String telefono = txtTelefono.getText().toString();
            String username = txtUsername.getText().toString();
            /*COMPROBAMOS QUE LOS CAMPOS NO ESTAN VACIOS*/
            if (nombre.isEmpty() || username.isEmpty() || email.isEmpty()
                    || password.isEmpty() || telefono.isEmpty()) {

                Toast.makeText(Register.this,
                        "Rellena todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // COMPROBAMOS FORMATO DE EMAIL VALIDO
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(Register.this,
                        "Email no válido",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            //CONTRASEÑA MINIMA
            if (password.length() < 3) {
                Toast.makeText(Register.this,
                        "La contraseña debe tener al menos 3 caracteres",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            //TELEFONO SOLO NUMEROS
            if (!telefono.matches("\\d+")) {
                Toast.makeText(Register.this,
                        "El teléfono solo debe contener números",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            RegisterRequestDTO request = new RegisterRequestDTO(nombre, username, email, password, telefono);

            authApi.register(request).enqueue(new Callback<RegisterResponseDTO>() {
                @Override
                public void onResponse(Call<RegisterResponseDTO> call, Response<RegisterResponseDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        //CREAMOS DTO
                        RegisterResponseDTO usuario = response.body();
                        //GUARDAR SESIÓN
                        SessionManager session = new SessionManager(Register.this);
                        session.saveSession(
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getUsername(),
                                usuario.getEmail()
                        );
                        //TEXTO CONFIRMACIÓN
                        Toast.makeText(Register.this,
                                "Bienvenido " + usuario.getNombre(),
                                Toast.LENGTH_SHORT).show();
                        //PASAMOS A LA PANTALLA GENERAL
                        Intent i = new Intent(getApplicationContext(), MarcoGeneral.class);
                        startActivity(i);
                        finish();
                    } else{
                        try {
                            /*Vamos a pillar el mensaje de error que nos muestra el
                            * backend y lo mostramos por pantalla
                            * Asi el usuario sabe lo que ha pasado*/
                            /*Pillamos el texto que nos manda el backend*/
                            String errorJson = response.errorBody().string();
                            /*Lo convertimos en un objeto JSON*/
                            JSONObject jsonObject = new JSONObject(errorJson);
                            /*Sacamos exactamente el mensaje de error que esta
                            * despues del "message:"*/
                            String message = jsonObject.getString("message");

                            Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(Register.this,
                                    "Error al registrar el usuario",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<RegisterResponseDTO> call, Throwable t) {
                    Toast.makeText(Register.this,
                            "Error de conexión",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro);

        bRegister=(Button) findViewById(R.id.button_registro);
        bRegister.setOnClickListener(listenerEntrar);

        bLogin=(Button) findViewById(R.id.button_login);
        bLogin.setOnClickListener(listenerLogin);
        /*Datos registro*/
        txtNombre=findViewById(R.id.user_nombre);
        txtEmail=findViewById(R.id.user_email);
        txtTelefono=findViewById(R.id.user_telefono);
        txtUsername=findViewById(R.id.user);
        txtPassword=findViewById(R.id.password);
        /*Iniciamos el retrofit*/
        authApi = ApiCliente.getRetrofit().create(AuthApi.class);
    }
}
