package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.birutekno.battendance.helper.AttendanceApi;
import com.birutekno.battendance.model.AuthModel;
import com.birutekno.battendance.model.Karyawan;
import com.birutekno.battendance.model.Response;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class InfoActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AUTH";

    TextView tv_nik;
    TextView tv_nama;
    TextView tv_divisi;
    Button btn_login;
    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tv_nik = findViewById(R.id.tv_nik);
        tv_nama = findViewById(R.id.tv_nama);
        tv_divisi = findViewById(R.id.tv_divisi);
        btn_login = findViewById(R.id.login);

        getBundle(tv_nik, tv_nama, tv_divisi);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(tv_nik);
            }
        });
    }

    private void getBundle(TextView tv_nik, TextView tv_nama, TextView tv_divisi){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            tv_nik.setText(extras.getString("nik"));
            tv_nama.setText(extras.getString("nama"));
            tv_divisi.setText(extras.getString("divisi"));
        }
    }

    private void login(TextView tv_nik){
        // TODO: pake sistem LOGIN
        // TODO: masukkan RESPONSE ke SharedPreference
        String nik = tv_nik.getText().toString().trim();
        Intent intent = new Intent(InfoActivity.this, PinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        prosesValidasi(nik, intent);
    }

    private void prosesValidasi(String nik, Intent intent){
        HashMap<String, String> params = new HashMap<>();
        params.put("nik", nik);

        progress_dialog = new ProgressDialog(InfoActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<AuthModel> result = AttendanceApi.getAPIService().register(params);
        result.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, retrofit2.Response<AuthModel> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body().getResponse();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            Karyawan karyawan = response.body().getKaryawan();
                            String id = karyawan.getId();
                            String nik = karyawan.getNik();
                            String pin = karyawan.getPin();
                            String stat = karyawan.getStatus();
                            setSharedPref(id,nik,pin,stat);
                            startActivity(intent);
                        }else if(status.equals("failed")){
                            Toasty.warning(InfoActivity.this, "Verifikasi Gagal, Hubungi HRD", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(InfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(InfoActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void setSharedPref(String id, String nik, String pin, String status){
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.putString("nik", nik);
        editor.putString("pin", pin);
        editor.putString("status", status);
        editor.apply();
    }

}
