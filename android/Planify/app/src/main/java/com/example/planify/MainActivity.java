package com.example.planify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private View.OnClickListener listenerRegistro=new View.OnClickListener() {

        public void onClick(View view) {
            Intent i=new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }
    };

    //esto aun esta sin flitros(que haya campos escritos) es solo para ir probando
    private View.OnClickListener listenerLogin=new View.OnClickListener() {

        public void onClick(View view) {
            Intent i=new Intent(getApplicationContext(), MarcoGeneral.class);
            startActivity(i);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bRegister=(Button) findViewById(R.id.button_registro);
        bRegister.setOnClickListener(listenerRegistro);
        Button bLogin=(Button) findViewById(R.id.button_login);
        bLogin.setOnClickListener(listenerLogin);
    }
}