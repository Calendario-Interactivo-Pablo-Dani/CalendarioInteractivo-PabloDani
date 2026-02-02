package com.example.planify.data.network;

import com.example.planify.data.dto.CalendarioRequestDTO;
import com.example.planify.data.dto.CalendarioResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
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

}
