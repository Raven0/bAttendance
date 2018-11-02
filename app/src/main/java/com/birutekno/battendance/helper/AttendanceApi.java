package com.birutekno.battendance.helper;

public class  AttendanceApi {
    public static final String BASE_URL_API = "http://attendance.birutekno.com/public/api/";

    public static AttendanceInterface getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(AttendanceInterface.class);
    }
}
