package com.scubbo.mtgmatcher.enrollment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.scubbo.mtgmatcher.R;
import com.scubbo.mtgmatcher.http.Callbackable;
import com.scubbo.mtgmatcher.http.HttpWrapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EnrollmentHelper {
    private final Context mContext;
    private boolean mIsEnrolled = false;
    private View mProgressBar = null;

    private static final String TAG = "EnrollmentHelperTAG";

    public EnrollmentHelper(Context context) {
        mContext = context;
    }

    public boolean isEnrolled() {
        return mIsEnrolled;
    }

    public void enroll(URL url, String name, String dciNumber, String regId, View progressBar) {
        HttpWrapper httpWrapper = new HttpWrapper();

        Map<String, String> options = new HashMap<String, String>();
        options.put("name",name);
        options.put("dciNumber",dciNumber);
        options.put("regId",regId);

        Callbackable callbackable = new Callbackable<String,Void>() {
            protected Void operate(String val) {
                callbackRegistration(val);
                return null;
            }
        };

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            mProgressBar = progressBar;
        }
        httpWrapper.sendPost("http://scubbo.org:2020/actions/public/registerPlayer.py",options, callbackable);
        //TODO: Change the name of registerPlayer to enrollPlayer
    }

    public void enroll(URL url, String name, String dciNumber, String regId) {
        enroll(url, name, dciNumber, null);
    }

    private void callbackRegistration(String backendResponse) {
        //TODO: Toasts for all these failure cases
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar = null;
        }

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(backendResponse);
            if (jsonResponse.getString("status").equals("failure")) { //TODO: Boooo, you should classify this
                if (jsonResponse.getString("code").equals("alreadyRegistered")) {
                    //TODO: allow a force-override
                    //logView.setText("Sorry, you're already registered as " + jsonResponse.getJSONObject("data").get("name") + " with DCI Number " + jsonResponse.getJSONObject("data").get("dciNumber"));
                } else {
                    //logView.setText("An unknown error occured. Response dump: " + jsonResponse.toString());
                }
            } else if (jsonResponse.getString("status").equals("success")) {
                //TODO: Make this message more friendly (return a tournament name in server response)
                Toast.makeText(mContext, "Congratulations! You are now enrolled", Toast.LENGTH_LONG);
                mIsEnrolled = true;
                //TODO: save these into prefs
                //logView.setText("Success! You are now registered");
            }
        } catch (JSONException e) {
            Log.i(TAG,"Something went wrong in parsing the backendResponse");
//            logView.setText(e.getMessage());
            e.printStackTrace();
        }


    }
}
