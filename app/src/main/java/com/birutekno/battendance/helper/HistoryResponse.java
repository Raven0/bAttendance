package com.birutekno.battendance.helper;

import com.birutekno.battendance.model.History;

public class HistoryResponse {
    private History[] data;

    public History[] getData ()
    {
        return data;
    }

    public void setData (History[] data)
    {
        this.data = data;
    }
}
