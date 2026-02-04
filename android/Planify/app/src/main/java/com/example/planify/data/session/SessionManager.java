package com.example.planify.data.session;

import android.content.Context;
import android.content.SharedPreferences;
/*La idea de esta clase es que una vez el usuario inicia sesion en su movil, no
* tenga que estar metiendo su cuenta cada vez que quiera entrar en la aplicacion,
* es decir que sus datos de sesión se guarden*/
public class SessionManager {
    //ATRIBUTOS QUE QUEMEROS TENER GUARDADOS PARA LA SESION
    private static final String KEY_IS_LOGGED = "is_logged";
    private static final String KEY_ID_USER = "id_user";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";

    private static final String KEY_EMAIL = "email";
    private static final String PREF_NAME = "planify_session";
    private static final String KEY_TELEFONO = "telefono";




    //Objeto para leer los atributos que guardemos en shared preferences
    private SharedPreferences prefs;
    //Objeto para escribir/modificar estos atributos
    private SharedPreferences.Editor editor;

    /*Constructor para inicializar el objeto en el:
    *le decimos a android que vamos a necesitar almacenamiento para guardar
    * estos datos(1ºlinea)
    *creamos un editor para poder modificar los atributos(2ºlinea)
    * */
    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    //GUARDAR SESIÓN
    /*Cuando el login sea correcto, guardamos los datos*/
    public void saveSession(int id,String nombre, String username, String email, String telefono) {
        /*Ponemos el is_logged como true y eso es lo que va ha hacer que
        * no tengamos que iniciar sesion cada vez que entremos*/
        editor.putBoolean(KEY_IS_LOGGED, true);
        editor.putInt(KEY_ID_USER, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, nombre);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TELEFONO, telefono);
        editor.apply();
    }
    public boolean isLogged() {
        return prefs.getBoolean(KEY_IS_LOGGED, false);
    }
    // Getters
    public int getIdUser() {
        return prefs.getInt(KEY_ID_USER, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }
    public String getName() {
        return prefs.getString(KEY_NAME, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }
    public String getTelefono() {
        return prefs.getString(KEY_TELEFONO, null);
    }
    // Cerrar sesión
    /*Esto nos servira cuando el usuario cierre sesion*/
    public void logout() {
        editor.clear();
        editor.apply();
    }








}
