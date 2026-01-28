package com.example.planify.data.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.planify.BuildConfig;


public class ApiCliente {
    /*Esta clase se encarga de:
    -Crea el objeto authApi
    -Indica el url con el que vamos a mandar las peticiones a spring
    -Convierte JSON en Java*/
    private static final String BASE_URL = BuildConfig.BASE_URL;/*CAMBIAR IP DESPUES*/
    private static Retrofit retrofit;
    public static Retrofit getRetrofit() {
        /*Si no he creado retrofit lo creo ahora*/
        if (retrofit == null) {
            /*HttpLoggingInterceptor es un componente de OkHttp
            *su funcion es mirar las peticiones HTTP que salen y entran
            * y las imprime por el Logcat para que nosotros podamos ver que
            * pasa en to-do momento por la red*/
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            /*Aqui le indicas lo que quiere que te enseñe, en este caso nos enseña
            * la url + headers + body(JSON) de las peticiones que salen y entran*/
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            /*Esto es lo que realmente va  a hacer la peticion http
            * No creamos un cliente directamente, sino que lo creamos con un builder
            * y le añadimos el interceptor que queremos que haga*/
            OkHttpClient cliente = new OkHttpClient.Builder()
                    /*Le indicamos que antes y despues de cada peticion
                    * queremos que se ejecute el interceptor para imprimir las cosas*/
                    .addInterceptor(logging)
                    .build();


            /*Retrofit se encarga de:
            * -montar la url
            * -convertit nuestros objetos en JSON
            * enviar la peticion
            * convertir la respuesta JSON a nuestro DTO*/
            retrofit = new Retrofit.Builder()
                    /*Indicamos la dirección del servidor*/
                    .baseUrl(BASE_URL)
                    /*Vamos a usar Gson para convertir JSON en Java*/
                    .addConverterFactory(GsonConverterFactory.create())
                    /*Le indicamos el motor que debe usar para lanzar las peticiones*/
                    .client(cliente)
                    .build();
        }
        return retrofit;
    }
}

