package com.birutekno.battendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrasiActivity extends AppCompatActivity {

    EditText et_nik;
    TextView tv_regist;
    Button btn_login;

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
        startActivity(intent);
    }

    private void login(){
        Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
