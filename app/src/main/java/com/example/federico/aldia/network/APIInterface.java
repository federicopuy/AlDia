package com.example.federico.aldia.network;

import com.example.federico.aldia.data.Comercio;
import com.example.federico.aldia.data.TokenRetro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @Headers("Content-Type: application/json")
    @POST(URLs.AUTHENTICATE)
    Call<String> loginUser(@Body TokenRetro token);


    @GET(URLs.COMERCIOS)
    Call<List<Comercio>>getComercios(@Header("authorization") String token);






}
