package com.birutekno.battendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.birutekno.battendance.adapter.HistoryAdapter;
import com.birutekno.battendance.helper.HistoryResponse;
import com.birutekno.battendance.model.History;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    private ArrayList<History> pojo;
    private HistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.drawable_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });

        initViews();
        loadJSON();
    }

    private void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void loadJSON(){
        //TODO: Buat sistem untuk menerima RESPONSE History
        History[] data = new History[6];
        data[0] = new History();
        data[0].setId("1");
        data[0].setId_karyawan("1");
        data[0].setNama("Dede Heri");
        data[0].setJam("07:43");
        data[0].setTanggal("18-09-2018");
        data[0].setAction("IN");

        data[1] = new History();
        data[1].setId("2");
        data[1].setId_karyawan("1");
        data[1].setNama("Dede Heri");
        data[1].setJam("17:40");
        data[1].setTanggal("18-09-2018");
        data[1].setAction("OUT");

        data[2] = new History();
        data[2].setId("3");
        data[2].setId_karyawan("1");
        data[2].setNama("Dede Heri");
        data[2].setJam("07:26");
        data[2].setTanggal("17-09-2018");
        data[2].setAction("IN");

        data[3] = new History();
        data[3].setId("4");
        data[3].setId_karyawan("1");
        data[3].setNama("Dede Heri");
        data[3].setJam("17:21");
        data[3].setTanggal("17-09-2018");
        data[3].setAction("OUT");

        data[4] = new History();
        data[4].setId("5");
        data[4].setId_karyawan("1");
        data[4].setNama("Dede Heri");
        data[4].setJam("08:03");
        data[4].setTanggal("16-09-2018");
        data[4].setAction("IN");

        data[5] = new History();
        data[5].setId("6");
        data[5].setId_karyawan("1");
        data[5].setNama("Dede Heri");
        data[5].setJam("17:01");
        data[5].setTanggal("16-09-2018");
        data[5].setAction("OUT");

        HistoryResponse historyResponse = new HistoryResponse();
        historyResponse.setData(data);
        pojo = new ArrayList<>(Arrays.asList(historyResponse.getData()));

//        Toast.makeText(this, pojo.toString(), Toast.LENGTH_SHORT).show();
        mAdapter = new HistoryAdapter(pojo, getBaseContext());
        recyclerView.setAdapter(mAdapter);
    }
}
