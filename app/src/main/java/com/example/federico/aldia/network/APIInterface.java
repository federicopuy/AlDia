package com.example.federico.aldia.network;

import com.example.federico.aldia.model.AllPayments;
import com.example.federico.aldia.model.Business;
import com.example.federico.aldia.model.Employee;
import com.example.federico.aldia.model.FirebaseToken;
import com.example.federico.aldia.model.Liquidacion;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.TokenQR;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @Headers("Liquidacion-Type: application/json")
    @POST(URLs.AUTHENTICATE)
    Call<String> loginUser(@Body FirebaseToken token);

    @GET(URLs.BUSINESSES)
    Call<List<Business>> getBusinesses();

    @GET(URLs.PAYMENTS + "/" + URLs.ONE + "/{userId}")
   Call<Liquidacion> getLastPayment(@Path("userId") long userId);

    @GET(URLs.PAYMENTS + "/" + URLs.ALL + "/{userId}")
    Call<AllPayments> getAllPayments(@Path("userId") long userId,
                                     @Query("page") long page,
                                     @Query("size") int size);

    @Headers("Liquidacion-Type: application/json")
    @POST(URLs.SHIFTS + "/" + URLs.NEW)
    Call<Periodo> newPeriodo(@Body TokenQR tokenQR);

    @GET(URLs.SHIFTS + "/{method}" + "/{id}")
    Call<List<Periodo>> getShifts(@Path("method") String tipoBusqueda,
                                  @Path("id") long id);

    @GET(URLs.EMPLOYEES + "/" + URLs.GET_EMPLOYEE)
    Call<Employee> getEmployeeData();

    @Headers("Liquidacion-Type: application/json")
    @POST(URLs.SHIFTS + "/" + URLs.REGISTER_PERIOD_OFFLINE)
    Call<Periodo> newShiftOffline(@Body QrToken qrToken);

}
