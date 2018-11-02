package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.birutekno.battendance.adapter.HistoryAdapter;
import com.birutekno.battendance.helper.AttendanceApi;
import com.birutekno.battendance.model.DataHistory;
import com.birutekno.battendance.model.HistoryModel;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String PREFS_NAME = "AUTH";

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressDialog progress_dialog;

    private ArrayList<DataHistory> pojo;
    private HistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerview);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.drawable_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        initViews();
        loadJSON();
    }

    private void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private String getSharedPrefId(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("id", null);
    }

    private void loadJSON(){
        progress_dialog = new ProgressDialog(HistoryActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<HistoryModel> call = AttendanceApi.getAPIService().myHistory(getSharedPrefId());
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                progress_dialog.dismiss();

                if(response.isSuccessful()){
                    HistoryModel jsonResponse = response.body();
                    pojo = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                    mAdapter = new HistoryAdapter(pojo, getBaseContext());
                    recyclerView.setAdapter(mAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    Toasty.success(HistoryActivity.this, "Data berhasil dimuat!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Log.d("ERROR CODE" , String.valueOf(response.code()));
                    Log.d("ERROR BODY" , response.errorBody().toString());

                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(HistoryActivity.this, "Database Attendance timeout, hubungi staff IT!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(HistoryActivity.this, "Server sedang dalam pemeliharaan!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }
}
