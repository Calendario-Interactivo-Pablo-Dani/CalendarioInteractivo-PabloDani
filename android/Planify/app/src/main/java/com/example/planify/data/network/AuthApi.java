package com.example.planify.data.network;

import com.example.planify.data.dto.LoginRequestDTO;
import com.example.planify.data.dto.LoginResponseDTO;
import com.example.planify.data.dto.RegisterRequestDTO;
import com.example.planify.data.dto.RegisterResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
/*Va a definir los endpoints de spring para poder usarlos*/
public interface AuthApi {
    /*Mandamos en el body el dto con el usuario con el que vamos a hacer el login
    * EJEMPLO:
    * authApi.login(dto)                    (LO LLAMAS)
    * POST http://10.0.2.2:8080/auth/login
      Body: JSON (email + password)         (LO QUE HACE)*/
    @POST("/auth/login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO request);
    /*Lo mismo que el login*/
    @POST("/auth/register")
    Call<RegisterResponseDTO> register(@Body RegisterRequestDTO request);


}
