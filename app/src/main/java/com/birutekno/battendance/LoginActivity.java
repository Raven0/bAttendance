package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AUTH";

    EditText et_nik;
    TextView tv_regist;
    Button btn_login;
    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_nik = findViewById(R.id.nik);
        tv_regist = findViewById(R.id.registrasi);
        btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(et_nik);
            }
        });

        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrasi();
            }
        });
    }

    private void login(EditText et_nik){
        String nik = et_nik.getText().toString().trim();
        Intent intent = new Intent(LoginActivity.this, PinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        prosesLogin(nik, intent);
    }

    private void registrasi(){
        Intent intent = new Intent(LoginActivity.this, RegistrasiActivity.class);
        startActivity(intent);
    }

    private void prosesLogin(String nik, Intent intent){
        HashMap<String, String> params = new HashMap<>();
        params.put("nik", nik);

        progress_dialog = new ProgressDialog(LoginActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<AuthModel> result = AttendanceApi.getAPIService().login(params);
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
                        }else if(status.equals("unauthorized")){
                            Toasty.info(LoginActivity.this, "Akun Anda belum di Verifikasi, Silahkan Registrasi", Toast.LENGTH_SHORT,true).show();
                        }else if(status.equals("failed")){
                            Toasty.warning(LoginActivity.this, "NIK anda tidak terdaftar", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(LoginActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(LoginActivity.this, "Server sedang dalam pemeliharaan!", Toast.LENGTH_SHORT, true).show();
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
