package com.birutekno.battendance.helper;

public class AttendanceApi {
//    public static final String BASE_URL_API = "http://test.heksasecurity.com/api/";
    public static final String BASE_URL_API = "http://battendance.com/api/";

    public static AttendanceInterface getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(AttendanceInterface.class);
    }
}
