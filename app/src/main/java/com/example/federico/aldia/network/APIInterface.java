package com.example.federico.aldia.network;

import com.example.federico.aldia.model.AllLiquidaciones;
import com.example.federico.aldia.model.Comercio;
import com.example.federico.aldia.model.Empleado;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.TokenQR;
import com.example.federico.aldia.model.TokenRetro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @Headers("Liquidacion-Type: application/json")
    @POST(URLs.AUTHENTICATE)
    Call<String> loginUser(@Body TokenRetro token);

    @GET(URLs.COMERCIOS)
    Call<List<Comercio>>getComercios();

    @GET(URLs.LIQUIDACIONES + "/" + URLs.ONE + "/{userId}")
    Call<Liquidacion>getUltimaLiquidacion(@Path("userId") int userId);

    @GET(URLs.LIQUIDACIONES + "/" + URLs.ALL + "/{userId}")
    Call<AllLiquidaciones>getAllLiquidaciones(@Path("userId") int userId);

    @Headers("Liquidacion-Type: application/json")
    @POST(URLs.PERIODOS + "/" + URLs.NEW)
    Call<Periodo> newPeriodo(@Body TokenQR tokenQR);

    @GET(URLs.PERIODOS + "/{userId}")
    Call<Periodo>getPeriodos(@Path("userId") int userId);

    @GET(URLs.EMPLEADOS + "/" + URLs.GET_EMPLEADO)
    Call<Empleado>getDatosEmpleado();









}
