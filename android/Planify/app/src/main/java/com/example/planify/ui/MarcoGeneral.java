package com.example.planify.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.planify.R;
import com.google.android.material.navigation.NavigationView;

public class MarcoGeneral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marco_general);

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
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);


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
        //aqui de momento ponemos esto pero podremos poner lo que queramos más adelante, osea pondremos
        //calendarios y demás
        if (id == R.id.nav_home) {
            // cargar fragment Home
        } else if (id == R.id.nav_logros) {
            Intent i= new Intent(getApplicationContext(), Logros.class);
            startActivity(i);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //----------------------------------------------------------------------------------------------------

}
