package com.birutekno.battendance.helper;

import com.birutekno.battendance.model.DataHistory;

public class HistoryResponse {
    private DataHistory[] data;

    public DataHistory[] getData ()
    {
        return data;
    }

    public void setData (DataHistory[] data)
    {
        this.data = data;
    }
}
