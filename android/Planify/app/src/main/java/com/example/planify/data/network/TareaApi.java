package com.example.planify.data.network;

import com.example.planify.data.dto.TareaNuevaRequestDTO;
import com.example.planify.data.dto.TareaNuevaResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TareaApi {

    @POST("tarea/crearTarea")
    Call<TareaNuevaResponseDTO> crearTarea(
            @Body TareaNuevaRequestDTO request
    );
}