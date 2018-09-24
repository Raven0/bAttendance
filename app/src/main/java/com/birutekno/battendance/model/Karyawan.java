package com.birutekno.battendance.model;

public class Karyawan {
    private String id;

    private String updated_at;

    private String status;

    private String pin;

    private String jenis_kelamin;

    private String created_at;

    private String divisi;

    private String nama;

    private String nik;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getPin ()
    {
        return pin;
    }

    public void setPin (String pin)
    {
        this.pin = pin;
    }

    public String getJenis_kelamin ()
    {
        return jenis_kelamin;
    }

    public void setJenis_kelamin (String jenis_kelamin)
    {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getDivisi ()
    {
        return divisi;
    }

    public void setDivisi (String divisi)
    {
        this.divisi = divisi;
    }

    public String getNama ()
    {
        return nama;
    }

    public void setNama (String nama)
    {
        this.nama = nama;
    }

    public String getNik ()
    {
        return nik;
    }

    public void setNik (String nik)
    {
        this.nik = nik;
    }
}
