package com.birutekno.battendance.helper;

import com.birutekno.battendance.model.AuthModel;
import com.birutekno.battendance.model.HistoryModel;
import com.birutekno.battendance.model.Response;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    @FormUrlEncoded
    @POST("masuk")
    Call<Response> masuk(@FieldMap HashMap<String, String> params);

    @DELETE("absen/{id}/delete")
    Call<Response> cancelAbsensi(@Path("id") String id);

    @FormUrlEncoded
    @PUT("absen/{id}/edit")
    Call<Response> editAbsensi(@Path("id") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @PUT("verifikasi/{id}/edit")
    Call<Response> trigger(@Path("id") String id, @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @PUT("telat/{id}/alasan")
    Call<Response> editTelat(@Path("id") String id, @FieldMap HashMap<String, String> params);

    //ABSEN PULANG API
    @GET("pulang")
    Call<Response> pulang();

    @FormUrlEncoded
    @POST("keluar")
    Call<Response> keluar(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("mabal")
    Call<Response> mabal(@FieldMap HashMap<String, String> params);

    //LEMBUR API
    @GET("over")
    Call<Response> over();

    @FormUrlEncoded
    @POST("lembur")
    Call<Response> lembur(@FieldMap HashMap<String, String> params);

    //DATA HISTORY
    @GET("history")
    Call<HistoryModel> history();

    @GET("myhistory/{id}/show")
    Call<HistoryModel> myHistory(@Path("id") String id);
}
