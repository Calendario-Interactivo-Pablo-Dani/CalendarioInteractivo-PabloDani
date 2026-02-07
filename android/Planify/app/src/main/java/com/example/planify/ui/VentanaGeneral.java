package com.example.planify.ui;

import android.content.Intent;
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


/*
* CLASE VENTANA GENERAL
*
* En esta clase se representa el HOME del usuario, la ventana que se muestra justo después del Login
* Es un puente entre la sesión, el backend y la navegación.
*
* Las funciones de esta clase son ver la lista de calendarios en los que está incluido el usuario,
* crear sus propios calendarios y unirse a otros.
*
* Se delega la navegacion al Adapter
* */
public class VentanaGeneral extends AppCompatActivity {
    private RecyclerView recyclerCalendarios;
    private ListaCalendariosAdapter ListaCalendariosAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*cargamos el layout de ventanaGneral que tendrá el textView de bienvenida con el nombre del
        * usuario, el recyclerView de todos los calendarios en los que está incluido y los botones
        * de unirse y crear el calendario.
        * */

        setContentView(R.layout.ventana_general);
        TextView txtBienvenida = findViewById(R.id.txtBienvenida);


        /*
        * Declaramos un sessionManager para que nos recoja la información de session del usuario que ç
        * está activo, porque el usuario creado en el login, se mantiene.
        *
        * Aqui cogemos su nombre para poner el mensaje de bienvenida.
        *
        * La Activity no crea ni decide el usuario activo,
        * solo lo consulta a través del SessionManager.
        * */
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
        /*
        * En estas lineas lo que se hace es crear un dialogo que actuará rapidamente como un layout
        * independiente al que podremos hacer así referencia a sus componentes, los edit text y el boton
        * de confirmar
        * */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater()
                .inflate(R.layout.dialogo_crear_calendario, null);

        // 1) Referencias a los EditText del diálogo (IMPORTANTE: usando "view")
        EditText etNombre = view.findViewById(R.id.etNombreCalendario);


        // 2) Referencia al botón "Crear" que está dentro del diálogo
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarCrearCalendario);

        //se le pone la vista al alterdialog
        builder.setView(view);
        //se crea
        AlertDialog dialog = builder.create();
        //se pinta
        dialog.show();

        // 3) Click del botón "Crear" (dentro del diálogo)
        btnConfirmar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();

            // Validación básica
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }


            añadirCalendarioBD(nombre);
            // (Opcional) Cerrar el diálogo después de pulsar crear
            dialog.dismiss();
        });
    }
    //Llamamos a la consulta del API para crear el calendario
    private void añadirCalendarioBD(String nombre) {
        // DTO request
        /*
        * Por q va el request solo con los datos del calendario y sin el id del usuario?-->
        * pues por q así separamos el body que tiene solo datos del calendario del idUser que va
        * como parametro
        *
        * Es como crear un calendario desde el contexto del Usuario de la sesion
        *
        * -Se hace la request
        * */
        CalendarioRequestDTO request =
                new CalendarioRequestDTO(nombre);

        // Usuario logueado, se coge el id
        SessionManager sessionManager = new SessionManager(this);
        int idUser = sessionManager.getIdUser();

        // API
        CalendarioApi calendarioApi =
                ApiCliente.getRetrofit().create(CalendarioApi.class);

        //aquí finalmente se crear el calendario con el idUser y la request que tiene en el body
        // el nombre y codigo
        Call<CalendarioResponseDTO> call =
                calendarioApi.crearCalendario(idUser, request);

        // a la cola, hacemos la peticion
        call.enqueue(new Callback<CalendarioResponseDTO>() {
            @Override
            public void onResponse(Call<CalendarioResponseDTO> call,
                                   Response<CalendarioResponseDTO> response) {

                //si fue exitosa, sale un popUp de creado correctamente
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

    /*
    * Aqui sacamos las referencia al recyclerView q esta en el layout ya inflado.
    *
    * Seguidamente le damos un layout, una orientación, por q sino el recycler no sabe si es
    * vertical, horizontal, grid o lo q sea. En este caso le ponemos linearLayout q por defecto es v
    * */
    private void inicializarVistas() {

        recyclerCalendarios = findViewById(R.id.recyclerCalendarios);
        recyclerCalendarios.setLayoutManager(new LinearLayoutManager(this));
    }


    /*
    *
    * */
    private void cargarCalendarios() {

        // Sesión
        SessionManager sessionManager = new SessionManager(this);
        int idUser = sessionManager.getIdUser();

        // API
        CalendarioApi calendarioApi =
                ApiCliente.getRetrofit().create(CalendarioApi.class);


        //aqui le estamos diciendo que la request de esta llamada, la vamos a
        //usar de manera que será una pedida de los calendarios del usuario
        Call<List<CalendarioResponseDTO>> call =
                calendarioApi.getMisCalendarios(idUser);

        /*
        * Aqui es como si dijesemos que pongamos en cola la pregunta de los calendarios del usuario
        * y que nos haga un callback
        * */
        call.enqueue(new Callback<List<CalendarioResponseDTO>>() {

            //onResponse quiere decir q llegó respuesta, que la comunicación fue correcta.
            @Override
            public void onResponse(Call<List<CalendarioResponseDTO>> call,
                                   Response<List<CalendarioResponseDTO>> response) {

                //ahora si la respuesta fue correcta, pero esto no implica que haya calendarios.
                if (response.isSuccessful() && response.body() != null) {
                    //mostramos los calendarios que tienen los datos de ese response
                    List<CalendarioResponseDTO> calendarios = response.body();
                    mostrarCalendarios(calendarios);

                } else {
                    Log.e("CALENDARIO", "Error response: " + response.code());
                }
            }

            //el onFailure es que la comunicación ha fallado, el servidor no existe, no respondió...
            @Override
            public void onFailure(Call<List<CalendarioResponseDTO>> call, Throwable t) {
                Log.e("CALENDARIO", "Error conexión", t);
                Toast.makeText(VentanaGeneral.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*En este metodo pasamos por parametro la responseDTO, que viene con los datos necesarios del
    * calendario.
    *
    * Creamos un adapter con esos datos ya listos.
    *
    * le setteamos el adapter al recyclerCalendarios, que es el scroll de todos los calendarios del
    * usuario. La secuencia interna que seguirá esto será:
    *   -preguntar cuantos items hay
    *   -creará celdas
    *   -las rellenará
    *
    * */
    private void mostrarCalendarios(List<CalendarioResponseDTO> calendarios) {
        ListaCalendariosAdapter = new ListaCalendariosAdapter(calendarios);
        recyclerCalendarios.setAdapter(ListaCalendariosAdapter);
    }
    public void menu(View view){
        Intent i= new Intent(getApplicationContext(), PerfilUsuario.class);
        startActivity(i);
    }
    public void crearUnirCalendario(View view){

    }

}
