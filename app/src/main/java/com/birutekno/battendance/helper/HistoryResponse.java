package com.birutekno.battendance.helper;

import com.birutekno.battendance.model.Data;

public class HistoryResponse {
    private Data[] data;

    public Data[] getData ()
    {
        return data;
    }

    public void setData (Data[] data)
    {
        this.data = data;
    }
}
