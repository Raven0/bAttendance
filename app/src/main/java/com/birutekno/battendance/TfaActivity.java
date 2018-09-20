package com.birutekno.battendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import es.dmoral.toasty.Toasty;

public class TfaActivity extends AppCompatActivity implements View.OnClickListener{

    String[] pinArray = new String[4];
    String pinCheck;

    TextView tv_clock;
    TextView tv_one;
    TextView tv_two;
    TextView tv_three;
    TextView tv_four;

    Button btn_send;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfa);

        //Component Initialization
        tv_clock = findViewById(R.id.tv_clock);
        tv_one = findViewById(R.id.tv_one);
        tv_two = findViewById(R.id.tv_two);
        tv_three = findViewById(R.id.tv_three);
        tv_four = findViewById(R.id.tv_four);
        btn_send = findViewById(R.id.btn_send);
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
        getBundle();

        //Realtime Clock function call
        realtimeClock(tv_clock);

        //Button set Pn Click Listener
        btn_send.setOnClickListener(this);
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
            clearPin();
        }else if (view == btn_del){
            delPin();
        }else if (view == btn_send){
            send();
        }
    }

    private void getBundle(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            try{
                //{"id":"1","pin":"1809"}
                JSONObject object = new JSONObject(extras.getString("result"));
                pinCheck = object.getString("pin");
            }catch (JSONException e){
                e.printStackTrace();
                Toasty.error(this, "Gagal memparse data!", Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(TfaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void realtimeClock(final TextView tv_clock){
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
                                SimpleDateFormat clockFormat = new SimpleDateFormat("hh:mm");
                                String clockString = clockFormat.format(date);
                                tv_clock.setText(clockString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    private void assignPin(int number){
        String val = String.valueOf(number);
        if (TextUtils.isEmpty(String.valueOf(pinArray[0]))){
            pinArray[0] = val;
            tv_one.setText(val);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[1]))){
            pinArray[1] = val;
            tv_two.setText(val);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[2]))){
            pinArray[2] = val;
            tv_three.setText(val);
        }else if (TextUtils.isEmpty(String.valueOf(pinArray[3]))){
            pinArray[3] = val;
            tv_four.setText(val);
            send();
        }
    }

    private void clearPin(){
        pinArray[0] = "";
        pinArray[1] = "";
        pinArray[2] = "";
        pinArray[3] = "";
        tv_one.setText("");
        tv_two.setText("");
        tv_three.setText("");
        tv_four.setText("");
    }

    private void delPin(){
        if (!TextUtils.isEmpty(String.valueOf(pinArray[3]))){
            pinArray[3] = "";
            tv_four.setText("");
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[2]))){
            pinArray[2] = "";
            tv_three.setText("");
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[1]))){
            pinArray[1] = "";
            tv_two.setText("");
        }else if (!TextUtils.isEmpty(String.valueOf(pinArray[0]))){
            pinArray[0] = "";
            tv_one.setText("");
        }
    }

    private void send(){
        String pin = pinArray[0] + pinArray[1] + pinArray[2] + pinArray[3];

        if (pinCheck.equals(pin)){
            //TODO : buat sistem untuk UPDATE status pin di tabel verifikasi
            //TODO : buat sistem untuk UPDATE verifikasi_id di tabel Absensi
            Intent intent = new Intent(TfaActivity.this, MainActivity.class);
            Toasty.success(this, "Berhasil!", Toast.LENGTH_SHORT, true).show();
            startActivity(intent);
        }else {
            Toasty.info(this, "Pin 2FA salah!", Toast.LENGTH_SHORT, true).show();
        }

    }
}
