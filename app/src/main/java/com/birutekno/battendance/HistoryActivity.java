package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.birutekno.battendance.adapter.HistoryAdapter;
import com.birutekno.battendance.helper.AttendanceApi;
import com.birutekno.battendance.model.Data;
import com.birutekno.battendance.model.DataHistory;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressDialog progress_dialog;

    private ArrayList<Data> pojo;
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
        progress_dialog = new ProgressDialog(HistoryActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<DataHistory> call = AttendanceApi.getAPIService().history();
        call.enqueue(new Callback<DataHistory>() {
            @Override
            public void onResponse(Call<DataHistory> call, Response<DataHistory> response) {
                progress_dialog.dismiss();

                if(response.isSuccessful()){
                    DataHistory jsonResponse = response.body();
                    pojo = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                    mAdapter = new HistoryAdapter(pojo, getBaseContext());
                    recyclerView.setAdapter(mAdapter);
                }else {
                    Log.d("ERROR CODE" , String.valueOf(response.code()));
                    Log.d("ERROR BODY" , response.errorBody().toString());

                }
            }

            @Override
            public void onFailure(Call<DataHistory> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(HistoryActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }
}
