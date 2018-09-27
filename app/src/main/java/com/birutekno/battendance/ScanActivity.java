package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.birutekno.battendance.helper.AttendanceApi;
import com.birutekno.battendance.model.Response;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class ScanActivity extends AppCompatActivity {

    Toolbar toolbar;
    CodeScannerView scannerView;
    CodeScanner mCodeScanner;
    String absen;

    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        toolbar = findViewById(R.id.toolbar);
        scannerView = findViewById(R.id.scanner_view);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.drawable_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });

        getBundleAbsen();

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ScanActivity.this, TfaActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("result", result.toString());
                        intent.putExtra("absen", absen);
                        startActivity(intent);

                        try {
                            JSONObject object = new JSONObject(result.toString());
                            String id = object.getString("id");
                            setTrigger(id,"1", intent);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toasty.error(ScanActivity.this, "Gagal memparse data!", Toast.LENGTH_SHORT, true).show();
                            Intent redirect = new Intent(ScanActivity.this, MainActivity.class);
                            startActivity(redirect);
                        }
                    }
                });
            }
        });
        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.error(ScanActivity.this, "Camera initialization error!", Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void getBundleAbsen(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            try{
                absen = extras.getString("absen");
            }catch (Exception e){
                e.printStackTrace();
                Toasty.error(this, "Gagal memparse data!", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void setTrigger(String id, String trigger, Intent intent){
        HashMap<String, String> params = new HashMap<>();
        params.put("status", trigger);

        progress_dialog = new ProgressDialog(ScanActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<Response> result = AttendanceApi.getAPIService().trigger(id, params);
        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            startActivity(intent);
                        }else if(status.equals("failed")){
                            Toasty.warning(ScanActivity.this, "Update trigger gagal", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ScanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(ScanActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void cancelAbsen(){
        progress_dialog = new ProgressDialog(ScanActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<Response> result = AttendanceApi.getAPIService().cancelAbsensi(absen);
        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            ScanActivity.super.onBackPressed();
                            Toasty.info(ScanActivity.this, "Absensi dibatalkan!", Toast.LENGTH_SHORT,true).show();
                        }else if(status.equals("failed")){
                            Toasty.warning(ScanActivity.this, "Absensi tidak bisa dibatalkan!", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ScanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(ScanActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        cancelAbsen();
    }
}
