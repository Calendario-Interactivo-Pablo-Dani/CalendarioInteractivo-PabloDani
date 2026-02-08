package com.example.planify.data.network;

import com.example.planify.data.dto.CalendarioRequestDTO;
import com.example.planify.data.dto.CalendarioResponseDTO;
import com.example.planify.data.dto.UsuarioCalendarioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CalendarioApi {
    //Ver mis calendarios
    @GET("calendario/mios/{idUser}")
    Call<List<CalendarioResponseDTO>> getMisCalendarios(@Path("idUser") int idUser);
    //Crear calendario
    @POST("calendario/crear/{idUser}")
    Call<CalendarioResponseDTO> crearCalendario(@Path("idUser") int idUser, @Body CalendarioRequestDTO request);
    @DELETE("calendario/eliminar/{idCal}/{idUser}")
    Call<Void> eliminarCalendario(@Path("idCal") int idCal, @Path("idUser") int idUser);

    @GET("calendario/verMiembros/{idCal}")
    Call<List<UsuarioCalendarioDTO>> obtenerMiembros(@Path("idCal") int idCal);
    @POST("calendario/unirse/{Codigo}/{idUser}")
    Call<CalendarioResponseDTO> unirseCalendario(@Path("Codigo") String Codigo, @Path("idUser") int idUser);


}
