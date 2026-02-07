package com.example.planify.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.planify.data.network.ApiCliente;
import com.example.planify.data.network.CalendarioApi;
import com.example.planify.data.session.SessionManager;


import com.example.planify.R;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarcoGeneral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    int idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marco_general);
        SessionManager session = new SessionManager(this);


        //ACCEDEMOS AL HEADER Y AHI PILLAMOS EL USERNAME Y LO PONEMOS
        /*Pillamos el xml de marco general y lo guardamos en una variable tipo
        * navigation view*/
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*Le decimos algo tipo “Dame el primer header que tengas dentro”
        * y como solo tenemos 1 siempre es 0*/
        View headerView = navigationView.getHeaderView(0);

        TextView tvUsername = headerView.findViewById(R.id.username);


         idUser = session.getIdUser();
        String username = session.getUsername();
        String email = session.getEmail();

        tvUsername.setText(username);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);





        //esto es para que la toolbar q acabamos de declarar android la tome como la suya, osea que
        //la use por defecto como si fuese la suya nativa
        setSupportActionBar(toolbar);


        //OCULTAR O ENSEÑAR items


        //vale, estas 3 lineas nos van a servir para si queremos ocultar alguna parte del drawer,
        //por ejemplo, si estamos verificados y hubiese un menu de verificación, pues ocultarlo.
        //las lineas de abajo son solo ejemplos
        Menu menu=navigationView.getMenu();
        menu.findItem(R.id.nav_salir).setVisible(true);
        menu.findItem(R.id.nav_miembros).setVisible(true);


        navigationView.bringToFront();//para q se superponga al frente
        //aqui lo que le decimos es que sincronice la barra superior, osea el toolbar con nuestro drawer,
        //osea con nuestro menú, de tal manera que al darle a las tres barritas del toolbar, abra el drawer.
        //las R.string y tal son para que salga el texto en personas o admins que usen lectores de pantalla
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //para que reciba eventos del toggle
        drawerLayout.addDrawerListener(toggle);
        //el toggle sync es nada más para q se sincronicen la animaciones
        toggle.syncState();

        //el listener para los items del drawer
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.setCheckedItem(R.id.nav_home);

        //este mét-odo entero sirve para que si estamos metidos en el menu lateral,
        //y el usuario le da a la flecha de atrás del móvil, no salga de la app o vuelva para
        //atrás sino que salga del menu lateral, y luego ya si le das otra vez pues pasará lo q sea
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false); // desactiva este callback
                    getOnBackPressedDispatcher().onBackPressed(); // comportamiento normal
                }
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentMainCalendario())
                    .commit();
        }

    }

    //--------------------------------ON NAVIGATION ITEM SELECTED-----------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        int id = menuItem.getItemId();
        CalendarioApi calendarioApi = ApiCliente.getRetrofit().create(CalendarioApi.class);

        //aqui de momento ponemos esto pero podremos poner lo que queramos más adelante, osea pondremos
        //calendarios y demás
        if (id == R.id.nav_home) {
            // cargar fragment Home
            Intent i = new Intent(MarcoGeneral.this, VentanaGeneral.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(i);
            finish();
        } else if (id == R.id.nav_logros) {
            Intent i= new Intent(getApplicationContext(), Logros.class);
            startActivity(i);
        }else if (id == R.id.nav_salir) {
            //SALIR CALENDARIO
            int idCal = CalendarioSeleccionado.idCal;
            Call<Void> call = calendarioApi.eliminarCalendario(idCal, idUser);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MarcoGeneral.this, "Calendario eliminado correctamente", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(getApplicationContext(), VentanaGeneral.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                    }else {
                        Toast.makeText(MarcoGeneral.this, "Error al eliminar calendario", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MarcoGeneral.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }else if(id == R.id.nav_perfil){
            Intent i= new Intent(getApplicationContext(), PerfilUsuario.class);
            startActivity(i);
        }else if(id == R.id.nav_miembros){
            Fragment fragment = new VerMiembros();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //----------------------------------------------------------------------------------------------------

}
