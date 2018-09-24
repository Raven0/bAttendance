package com.birutekno.battendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    TextView object_id;
    TextView object_date;
    TextView object_hour;
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        object_id = findViewById(R.id.object_id);
        object_date = findViewById(R.id.object_date);
        object_hour = findViewById(R.id.object_hour);
        btn_scan = findViewById(R.id.btn_scan);

        checkPermission();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            try{
                JSONObject object = new JSONObject(extras.getString("result"));
                object_id.setText(object.getString("id"));
                object_date.setText(object.getString("date"));
                object_hour.setText(object.getString("hour"));
            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(this, "Unable to Parse", Toast.LENGTH_SHORT).show();
            }
        }

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanActivity = new Intent(ResultActivity.this, ScanActivity.class);
                scanActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(scanActivity);
            }
        });

    }

    private void checkPermission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(ResultActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}
