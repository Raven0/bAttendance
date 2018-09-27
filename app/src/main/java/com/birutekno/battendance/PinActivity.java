package com.birutekno.battendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.birutekno.battendance.helper.AttendanceApi;
import com.birutekno.battendance.model.AuthModel;
import com.birutekno.battendance.model.Karyawan;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class PinActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String PREFS_PIN = "PIN";
    public static final String PREFS_NAME = "AUTH";

    String[] pinArray = new String[6];

    ImageView tv_one;
    ImageView tv_two;
    ImageView tv_three;
    ImageView tv_four;
    ImageView tv_five;
    ImageView tv_six;

    Button btn_one;
    Button btn_two;
    Button btn_three;
    Button btn_four;
    Button btn_five;
    Button btn_six;
    Button btn_seven;
    Button btn_eight;
    Button btn_nine;
    Button btn_zero;
    Button btn_clear;
    Button btn_del;

    ProgressDialog progress_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        //Component Initialization
        tv_one = findViewById(R.id.tv_one);
        tv_two = findViewById(R.id.tv_two);
        tv_three = findViewById(R.id.tv_three);
        tv_four = findViewById(R.id.tv_four);
        tv_five = findViewById(R.id.tv_five);
        tv_six = findViewById(R.id.tv_six);
        btn_one = findViewById(R.id.btn_one );
        btn_two = findViewById(R.id.btn_two );
        btn_three = findViewById(R.id.btn_three );
        btn_four = findViewById(R.id.btn_four );
        btn_five = findViewById(R.id.btn_five );
        btn_six = findViewById(R.id.btn_six );
        btn_seven = findViewById(R.id.btn_seven );
        btn_eight = findViewById(R.id.btn_eight );
        btn_nine = findViewById(R.id.btn_nine );
        btn_zero = findViewById(R.id.btn_zero );
        btn_clear = findViewById(R.id.btn_clear );
        btn_del = findViewById(R.id.btn_del );

        //Variable Initialization
        clearPin();

        //Button set Pn Click Listener
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_del.setOnClickListener(this);

        if (getSharedPrefPin() == null){
            //BUAT PIN
            if (getSharedPref() != null){
                Toasty.info(this, "Buat Pin terlebih dahulu!", Toast.LENGTH_SHORT, true).show();
                Toasty.warning(this, "Device diganti!", Toast.LENGTH_SHORT, true).show();
            }else{
                Toasty.info(this, "Buat Pin terlebih dahulu!", Toast.LENGTH_SHORT, true).show();
            }
        }else{
            //MASUKKAN PIN
            if (!getSharedPrefPin().equals(getSharedPref())){
                Toasty.info(this, "Masukkan Pin!", Toast.LENGTH_SHORT, true).show();
                Toasty.warning(this, "Device diganti!", Toast.LENGTH_SHORT, true).show();
            }else {
                Toasty.info(this, "Masukkan Pin!", Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    @Override
    public void onClick(View view){
        if (view == btn_one){
            assignPin(1);
        }else if (view == btn_two){
            assignPin(2);
        }else if (view == btn_three){
            assignPin(3);
        }else if (view == btn_four){
            assignPin(4);
        }else if (view == btn_five){
            assignPin(5);
        }else if (view == btn_six){
            assignPin(6);
        }else if (view == btn_seven){
            assignPin(7);
        }else if (view == btn_eight){
            assignPin(8);
        }else if (view == btn_nine){
            assignPin(9);
        }else if (view == btn_zero){
            assignPin(0);
        }else if (view == btn_clear){
            // TODO: buat action untuk lupa pin dengan klik tombol clear 5x
            clearPin();
        }else if (view == btn_del){
            delPin();
        }
    }

    private void assignPin(int number){
        String val = String.valueOf(number);
        if (TextUtils.isEmpty(String.valueOf(pinArray[0]))){
            pinArray[0] = val;
            tv_one.setImageResource(R.drawable.drawable_circle);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[1]))){
            pinArray[1] = val;
            tv_two.setImageResource(R.drawable.drawable_circle);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[2]))){
            pinArray[2] = val;
            tv_three.setImageResource(R.drawable.drawable_circle);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[3]))){
            pinArray[3] = val;
            tv_four.setImageResource(R.drawable.drawable_circle);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[4]))){
            pinArray[4] = val;
            tv_five.setImageResource(R.drawable.drawable_circle);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[5]))){
            pinArray[5] = val;
            tv_six.setImageResource(R.drawable.drawable_circle);
            send();
        }
    }

    private void clearPin(){
        pinArray[0] = "";
        pinArray[1] = "";
        pinArray[2] = "";
        pinArray[3] = "";
        pinArray[4] = "";
        pinArray[5] = "";
        tv_one.setImageResource(R.drawable.drawable_circle_empty);
        tv_two.setImageResource(R.drawable.drawable_circle_empty);
        tv_three.setImageResource(R.drawable.drawable_circle_empty);
        tv_four.setImageResource(R.drawable.drawable_circle_empty);
        tv_five.setImageResource(R.drawable.drawable_circle_empty);
        tv_six.setImageResource(R.drawable.drawable_circle_empty);
    }

    private void delPin(){
        if (!TextUtils.isEmpty(String.valueOf(pinArray[5]))){
            pinArray[5] = "";
            tv_six.setImageResource(R.drawable.drawable_circle_empty);
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[4]))){
            pinArray[4] = "";
            tv_five.setImageResource(R.drawable.drawable_circle_empty);
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[3]))){
            pinArray[3] = "";
            tv_four.setImageResource(R.drawable.drawable_circle_empty);
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[2]))){
            pinArray[2] = "";
            tv_three.setImageResource(R.drawable.drawable_circle_empty);
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[1]))){
            pinArray[1] = "";
            tv_two.setImageResource(R.drawable.drawable_circle_empty);
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[0]))){
            pinArray[0] = "";
            tv_one.setImageResource(R.drawable.drawable_circle_empty);
        }
    }

    private String getSharedPref(){
        SharedPreferences prefs = getSharedPreferences(PREFS_PIN, MODE_PRIVATE);
        return prefs.getString("pin", null);
    }

    private void setSharedPref(String pin){
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_PIN, MODE_PRIVATE).edit();
        editor.putString("pin", pin);
        editor.apply();
    }

    private String getSharedPrefPin(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("pin", null);
    }

    private void setSharedPrefPin(String pin){
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("pin", pin);
        editor.apply();
    }

    private String getSharedPrefNik(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("nik", null);
    }

    private void send(){
        String pin = pinArray[0] + pinArray[1] + pinArray[2] + pinArray[3] + pinArray[4] + pinArray[5];

        if (getSharedPrefPin() == null){
            Intent restartIntent = new Intent(PinActivity.this, PinActivity.class);
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setPin(getSharedPrefNik(), pin, restartIntent);
            setSharedPrefPin(pin);
            setSharedPref(pin);
        }else if (getSharedPrefPin().equals(pin) || getSharedPref().equals(pin)){
            if (getSharedPref() == null){
                Toasty.warning(this, "Device di set!", Toast.LENGTH_SHORT, true).show();
                setSharedPrefPin(pin);
                setSharedPref(pin);
            }
            Intent intent = new Intent(PinActivity.this, MainActivity.class);
            Toasty.success(this, "Berhasil!", Toast.LENGTH_SHORT, true).show();
            startActivity(intent);
        }else {
            Toasty.info(this, "Pin salah!", Toast.LENGTH_SHORT, true).show();
        }

    }

    private void setPin(String nik, String pin, Intent intent){
        HashMap<String, String> params = new HashMap<>();
        params.put("nik", nik);
        params.put("pin", pin);

        progress_dialog = new ProgressDialog(PinActivity.this);
        progress_dialog.setMessage("Harap tunggu...");
        progress_dialog.setCancelable(false);
        progress_dialog.show();

        Call<AuthModel> result = AttendanceApi.getAPIService().pin(params);
        result.enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, retrofit2.Response<AuthModel> response) {
                progress_dialog.dismiss();
                try {
                    if(response.body()!=null) {
                        Karyawan karyawan = response.body().getKaryawan();
                        if (karyawan.getPin().equals(pin)){
                            Toasty.success(PinActivity.this, "Pin Berhasil Dibuat!", Toast.LENGTH_SHORT, true).show();
                            startActivity(intent);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PinActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                progress_dialog.dismiss();
                t.printStackTrace();
                if (t.getMessage().equals("timeout")){
                    Toasty.error(PinActivity.this, "Database Attendance timeout, coba lagi!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }
}
