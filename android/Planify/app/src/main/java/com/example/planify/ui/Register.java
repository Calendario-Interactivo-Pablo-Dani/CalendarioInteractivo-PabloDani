package com.example.planify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planify.R;

public class Register extends AppCompatActivity {


    Button bRegister;


    private View.OnClickListener listenerEntrar=new View.OnClickListener() {


        public void onClick(View view) {
            Intent i=new Intent(getApplicationContext(), MarcoGeneral.class);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro);

        bRegister=(Button) findViewById(R.id.button_registro);
        bRegister.setOnClickListener(listenerEntrar);
    }
}
