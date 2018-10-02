package com.birutekno.battendance;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.birutekno.battendance.helper.AttendanceApi;
import com.birutekno.battendance.model.Response;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AUTH";
    // TODO: buat gradien instagram untuk header
    LinearLayout layoutHeader;
    TextView tv_clock;
    TextView tv_date;
    ImageView img_history;
    ImageView card_absen;
    ImageView card_pulang;
    ImageView card_lembur;
    Button btn_logout;
    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check permission
        checkPermission();

        //Component Initialization
        layoutHeader = findViewById(R.id.layoutHeader);
        tv_clock = findViewById(R.id.tv_clock);
        tv_date = findViewById(R.id.tv_date);
        img_history = findViewById(R.id.img_history);
        card_absen = findViewById(R.id.absenCard);
        card_pulang = findViewById(R.id.pulangCard);
        card_lembur = findViewById(R.id.lemburCard);
        btn_logout = findViewById(R.id.logout);

        //Realtime Clock function call
        realtimeClock(tv_clock, tv_date, layoutHeader);

        //OnClickAction
        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history();
            }
        });
        card_absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absen();
            }
        });
        card_pulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long date = System.currentTimeMillis();
                SimpleDateFormat clockFormat = new SimpleDateFormat("h");
                SimpleDateFormat pmFormat = new SimpleDateFormat("a");
                int currentClock = Integer.parseInt(clockFormat.format(date));
                int startPulangClock = 5;
                String pm = pmFormat.format(date);
                if (currentClock == startPulangClock && pm.equals("PM")) {
                    pulang();
                }else {
                    formPulang(savedInstanceState);
                }
            }
        });
        card_lembur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long date = System.currentTimeMillis();
                SimpleDateFormat clockFormat = new SimpleDateFormat("h");
                SimpleDateFormat pmFormat = new SimpleDateFormat("a");
                int currentClock = Integer.parseInt(clockFormat.format(date));
                int startLemburClock = 5;
                String pm = pmFormat.format(date);
                if (currentClock >= startLemburClock && pm.equals("PM")) {
                    formLembur(savedInstanceState);
                }else {
                    Toasty.info(MainActivity.this, "Waktu lembur belum dimulai", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void realtimeClock(final TextView tv_clock, final TextView tv_date, final LinearLayout layoutHeader){
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat clockFormat = new SimpleDateFormat("hh:mm:ss a");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                                String clockString = clockFormat.format(date);
                                String dateString = dateFormat.format(date);
                                tv_clock.setText(clockString);
                                tv_date.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    private String getSharedPrefId(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("id", null);
    }

    private void history(){
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    private void absen(){
        HashMap<String, String> params = new HashMap<>();
        params.put("karyawan_id", getSharedPrefId());

        progress_dialog = new ProgressDialog(MainActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<Response> result = AttendanceApi.getAPIService().masuk(params);
        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                            intent.putExtra("absen", responses.getId());
                            startActivity(intent);
                        }else if(status.equals("failed")){
                            Toasty.warning(MainActivity.this, "Anda sudah melakukan absen masuk!", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(MainActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(MainActivity.this, "Server sedang dalam pemeliharaan!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void pulang(){
        HashMap<String, String> params = new HashMap<>();
        params.put("karyawan_id", getSharedPrefId());

        progress_dialog = new ProgressDialog(MainActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<Response> result = AttendanceApi.getAPIService().pulang(params);
        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            Toasty.success(MainActivity.this, "Absensi berhasil dilakukan!", Toast.LENGTH_SHORT,true).show();
                        }else if(status.equals("failed")){
                            Toasty.warning(MainActivity.this, "Anda sudah melakukan absen pulang!", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(MainActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(MainActivity.this, "Server sedang dalam pemeliharaan!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void mabal(String alasan){
        HashMap<String, String> params = new HashMap<>();
        params.put("karyawan_id", getSharedPrefId());
        params.put("alasan", alasan);

        progress_dialog = new ProgressDialog(MainActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<Response> result = AttendanceApi.getAPIService().mabal(params);
        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            Toasty.success(MainActivity.this, "Absensi berhasil dilakukan!", Toast.LENGTH_SHORT,true).show();
                        }else if(status.equals("failed")){
                            Toasty.warning(MainActivity.this, "Anda sudah melakukan absen pulang!", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(MainActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(MainActivity.this, "Server sedang dalam pemeliharaan!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void lembur(String alasan, String durasi){
        HashMap<String, String> params = new HashMap<>();
        params.put("karyawan_id", getSharedPrefId());
        params.put("alasan", alasan);
        params.put("durasi", durasi);

        progress_dialog = new ProgressDialog(MainActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<Response> result = AttendanceApi.getAPIService().lembur(params);
        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Response responses = response.body();
                        String status = responses.getMessage();
                        if (status.equals("success")) {
                            Toasty.success(MainActivity.this, "Anda tercatat Lembur " + durasi + " Jam", Toast.LENGTH_SHORT,true).show();
                        }else if(status.equals("failed")){
                            Toasty.warning(MainActivity.this, "Anda sudah tercatat Lembur hari ini!", Toast.LENGTH_SHORT,true).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(MainActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }else {
                    Toasty.error(MainActivity.this, "Server sedang dalam pemeliharaan!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void checkPermission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void sendLembur(String args){
        String[] strings = args.split(",");
        String durasi = strings[0];
        String alasan = strings[1];
        lembur(alasan,durasi);
    }

    private void formPulang(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle("Form Pulang")
                .setMessage("Masukkan alasan anda")
                .setHint("Contoh : izin ke bank")
                .setIcon(R.drawable.ic_assignment_white_36dp)
                .setConfirmButton(android.R.string.ok, text -> mabal(text))
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
                .show();
    }

    private void formLembur(Bundle savedInstanceState) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle("Form Lembur")
                .setMessage("Isi durasi (jam) lembur anda dan alasan lembur")
                .setHint("Contoh : 3, Kejar deadline besok pagi")
                .setIcon(R.drawable.ic_assignment_white_36dp)
                .setInputFilter("Masukkan seperti di contoh", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\d,.*");
                    }
                })
                .setConfirmButton(android.R.string.ok, text -> sendLembur(text))
                .setNegativeButton(android.R.string.no, null)
                .setSavedInstanceState(savedInstanceState)
                .configureEditText(editText -> editText.setMaxLines(1))
                .show();
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder ask = new AlertDialog.Builder(MainActivity.this);
        ask.setTitle("Apakah Anda yakin akan keluar?");
        ask.setMessage("Tekan tombol Ya jika anda ingin logout dari aplikasi ini");
        ask.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ask.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ask.show();
    }
}
