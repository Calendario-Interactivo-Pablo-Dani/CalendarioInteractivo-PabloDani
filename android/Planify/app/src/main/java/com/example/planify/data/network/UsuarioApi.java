package com.example.planify.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioApi {
    @GET("usuario/totalCalendarios/{idUser}")
    Call<Integer> obtenerTotalCalendarios(@Path("idUser") int idUser);
}
