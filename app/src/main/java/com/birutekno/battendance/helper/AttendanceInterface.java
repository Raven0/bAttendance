package com.birutekno.battendance.helper;

import com.birutekno.battendance.model.AuthModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AttendanceInterface {
    //LOGIN API
    @FormUrlEncoded
    @POST("login")
    Call<AuthModel> login(@FieldMap HashMap<String, String> params);

    //REGISTER API
    @FormUrlEncoded
    @POST("validasi")
    Call<AuthModel> validasi(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("register")
    Call<AuthModel> register(@FieldMap HashMap<String, String> params);

    //PIN API
    @FormUrlEncoded
    @POST("register/pin")
    Call<AuthModel> pin(@FieldMap HashMap<String, String> params);

    //ABSEN MASUK API
    //ABSEN PULANG API
    //LEMBUR API
    //DATA HISTORY
}
