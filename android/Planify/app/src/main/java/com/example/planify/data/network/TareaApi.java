package com.example.planify.data.network;

import com.example.planify.data.POJOs.Tarea;
import com.example.planify.data.dto.CalendarioRequestDTO;
import com.example.planify.data.dto.CalendarioResponseDTO;
import com.example.planify.data.dto.DiasConTareaDTO;
import com.example.planify.data.dto.TareaNuevaRequestDTO;
import com.example.planify.data.dto.TareaNuevaResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TareaApi {

    @POST("tarea/crearTarea")
    Call<TareaNuevaResponseDTO> crearTarea(
            @Body TareaNuevaRequestDTO request
    );
    @POST("tarea/modificarTarea/{idTarea}")
    Call<TareaNuevaResponseDTO> modificarTarea(@Path("idTarea") int idTarea, @Body TareaNuevaRequestDTO request);
    @DELETE("tarea/eliminarTarea/{idTarea}")
    Call<Void> eliminarTarea(@Path("idTarea") int idTarea);

    @GET("tarea/verTareasDia/{idCal}/{fecha}")
    Call<List<Tarea>> verTareasDia(@Path("idCal") int idCal, @Path("fecha") String fecha);

    @GET("tarea/verTareas/{idCal}")
    Call<List<TareaNuevaResponseDTO>> verTareas(@Path("idCal") int idCal);
    @GET("tarea/verEventosMes/{idCal}/{anio}/{mes}")
    Call<List<DiasConTareaDTO>> verEventosMes(@Path("idCal") int idCal, @Path("anio") int anio, @Path("mes") int mes);





}


