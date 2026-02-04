package com.example.planify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planify.R;
import com.example.planify.data.session.SessionManager;

public class PerfilUsuario extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuario);
        SessionManager sessionManager = new SessionManager(this);
        String nombre = sessionManager.getName();
        String email = sessionManager.getEmail();
        String usuario = sessionManager.getUsername();
        String telefono = sessionManager.getTelefono();
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtUsuario = findViewById(R.id.txtUsuario);
        TextView txtTelefono = findViewById(R.id.txtTelefono);
        txtNombre.setText("Nombre: " + nombre);
        txtEmail.setText("Email: " + email);
        txtUsuario.setText("Nombre de Usuario" + usuario);
        txtTelefono.setText("Teléfono: " + telefono);
    }
    public void logout(View view) {
        SessionManager session = new SessionManager(this);
        session.logout();
        Intent i= new Intent(getApplicationContext(), MainActivity.class);
        //esta linea borra todas las pestañas abiertas q hubiese de esa app y vuelves al punto
        //inicial, es decir, al login, sin ella si le dieses a la flecha de atrás despues de
        //logout, volverías a la app logueado.
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
    public void atras(View view) {
        finish();
    }


}
