package com.birutekno.battendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    TextView tv_nik;
    TextView tv_nama;
    TextView tv_divisi;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tv_nik = findViewById(R.id.tv_nik);
        tv_nama = findViewById(R.id.tv_nama);
        tv_divisi = findViewById(R.id.tv_divisi);
        btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(tv_nik);
            }
        });
    }

    private void login(TextView tv_nik){
        // TODO: pake sistem LOGIN
        // TODO: masukkan RESPONSE ke SharedPreference
        String nik = tv_nik.getText().toString().trim();
        Intent intent = new Intent(InfoActivity.this, PinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
