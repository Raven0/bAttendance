package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistrasiActivity extends AppCompatActivity {

    EditText et_nik;
    TextView tv_regist;
    Button btn_login;
    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        et_nik = findViewById(R.id.nik);
        tv_regist = findViewById(R.id.registrasi);
        btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrasi(et_nik);
            }
        });
        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void registrasi(EditText et_nik){
        // TODO: buat sistem registrasi parameter NIK dengan RESPONSE ID/NIK karyawan, NAMA, dan DIVISI
        String nik = et_nik.getText().toString().trim();
        Intent intent = new Intent(RegistrasiActivity.this, InfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        prosesValidasi(nik, intent);
    }

    private void login(){
        Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void prosesValidasi(String nik, Intent intent){
        HashMap<String, String> params = new HashMap<>();
        params.put("nik", nik);

        progress_dialog = new ProgressDialog(RegistrasiActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<AuthModel> result = AttendanceApi.getAPIService().validasi(params);
        result.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, retrofit2.Response<AuthModel> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body().getResponse();
                        String status = responses.getMessage();
                        if (status.equals("exist")) {
                            Karyawan karyawan = response.body().getKaryawan();
                            String nik = karyawan.getNik();
                            String nama = karyawan.getNama();
                            String divisi = karyawan.getDivisi();
                            intent.putExtra("nik", nik);
                            intent.putExtra("nama", nama);
                            intent.putExtra("divisi", divisi);
                            startActivity(intent);
                        }else if(status.equals("empty")){
                            Toasty.warning(RegistrasiActivity.this, "NIK anda tidak terdaftar", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RegistrasiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(RegistrasiActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }
}
