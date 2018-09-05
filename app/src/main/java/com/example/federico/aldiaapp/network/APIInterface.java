package com.example.federico.aldiaapp.network;

import com.example.federico.aldiaapp.model.AllPayments;
import com.example.federico.aldiaapp.model.Business;
import com.example.federico.aldiaapp.model.Employee;
import com.example.federico.aldiaapp.model.FirebaseToken;
import com.example.federico.aldiaapp.model.Payment;
import com.example.federico.aldiaapp.model.QrToken;
import com.example.federico.aldiaapp.model.Shift;
import com.example.federico.aldiaapp.model.TokenQR;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @Headers("Payment-Type: application/json")
    @POST(URLs.AUTHENTICATE)
    Call<String> loginUser(@Body FirebaseToken token);

    @GET(URLs.BUSINESSES)
    Call<List<Business>> getBusinesses();

    @GET(URLs.PAYMENTS + "/" + URLs.ONE + "/{userId}")
    Call<Payment> getLastPayment(@Path("userId") long userId);

    @GET(URLs.PAYMENTS + "/" + URLs.ALL + "/{userId}")
    Call<AllPayments> getAllPayments(@Path("userId") long userId,
                                     @Query("page") long page,
                                     @Query("size") int size);

    @Headers("Payment-Type: application/json")
    @POST(URLs.SHIFTS + "/" + URLs.NEW)
    Call<Shift> newPeriodo(@Body TokenQR tokenQR);

    @GET(URLs.SHIFTS + "/{method}" + "/{id}")
    Call<List<Shift>> getShifts(@Path("method") String tipoBusqueda,
                                @Path("id") long id);

    @GET(URLs.EMPLOYEES + "/" + URLs.GET_EMPLOYEE)
    Call<Employee> getEmployeeData();

    @Headers("Payment-Type: application/json")
    @POST(URLs.SHIFTS + "/" + URLs.REGISTER_PERIOD_OFFLINE)
    Call<Shift> newShiftOffline(@Body QrToken qrToken);

}
