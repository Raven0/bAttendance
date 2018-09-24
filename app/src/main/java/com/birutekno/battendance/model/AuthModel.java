package com.birutekno.battendance.model;

public class AuthModel {
    private Response response;

    private Karyawan karyawan;

    public Response getResponse ()
    {
        return response;
    }

    public void setResponse (Response response)
    {
        this.response = response;
    }

    public Karyawan getKaryawan ()
    {
        return karyawan;
    }

    public void setKaryawan (Karyawan karyawan)
    {
        this.karyawan = karyawan;
    }
}
