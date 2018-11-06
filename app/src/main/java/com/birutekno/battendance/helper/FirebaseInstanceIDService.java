package com.birutekno.battendance.helper;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    public String refreshedToken;

    @Override
    public void onTokenRefresh() {

        //For registration of token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //To displaying token on logcat
        Log.d("TOKEN: ", refreshedToken);

    }

}
