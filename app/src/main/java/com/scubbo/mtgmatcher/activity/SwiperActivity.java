package com.scubbo.mtgmatcher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scubbo.mtgmatcher.Constants;
import com.scubbo.mtgmatcher.R;
import com.scubbo.mtgmatcher.adapter.TabsAdapter;
import com.scubbo.mtgmatcher.enrollment.EnrollmentHelper;
import com.scubbo.mtgmatcher.fragments.AboutFragment;
import com.scubbo.mtgmatcher.fragments.MainPageFragment;
import com.scubbo.mtgmatcher.fragments.PlayerDetailsFragment;
import com.scubbo.mtgmatcher.fragments.TournamentEnrollmentFragment;
import com.scubbo.mtgmatcher.registration.GCMRegistrationHelper;

import java.net.MalformedURLException;
import java.net.URL;

public class SwiperActivity extends FragmentActivity {
    private ViewPager mViewPager;

    private Context context;

    private GoogleCloudMessaging gcm;
    private SharedPreferences prefs;
    private EnrollmentHelper mEnrollmentHelper;

    private String regid;

    private static final String TAG = "SwiperActivityTAG";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        context = getApplicationContext();

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            GCMRegistrationHelper gcmRegistrationHelper = new GCMRegistrationHelper(gcm, context);
            regid = gcmRegistrationHelper.getGCMRegistrationId();
        } else {
            Toast.makeText(context, R.string.noPlayServicesWarning, Toast.LENGTH_SHORT);
        }

        prefs = context.getSharedPreferences(SwiperActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        mEnrollmentHelper = new EnrollmentHelper(context);

        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        TabsAdapter tabsAdapter = new TabsAdapter(getFragmentManager());
        tabsAdapter.addFragment(new PlayerDetailsFragment());
        tabsAdapter.addFragment(new TournamentEnrollmentFragment());
        tabsAdapter.addFragment(new MainPageFragment());
        tabsAdapter.addFragment(new AboutFragment());
        mViewPager.setAdapter(tabsAdapter);


        if (savedInstanceState != null) {
//            bar.setSelectedNavigationItem(savedInstanceState.getInt("MainPage",0));
        }

    }

    public void updatePlayerDetails(View v) {
        EditText nameEntry = (EditText) findViewById(R.id.nameEntry);
        EditText dciEntry = (EditText) findViewById(R.id.dciEntry);

        String playerName = nameEntry.getText().toString();
        String dciNumber = dciEntry.getText().toString();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFS_PROPERTY_PLAYER_NAME, playerName);
        editor.putString(Constants.PREFS_PROPERTY_DCI_NUMBER, dciNumber);
        editor.commit();

        Toast.makeText(context, "Details saved", Toast.LENGTH_SHORT).show();
    }

    public void startQRScanner(View v) {
        //TODO: Check for registered player
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //TODO: Refactor this out
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.i(TAG, "scanResult was " + scanResult.toString());
            Log.i(TAG, "scanResult contents are " + scanResult.getContents());
            handleEnrollment(scanResult.getContents());
        }
    }

    public void enrollByTextEntry(View v) {
        EditText editText = (EditText) findViewById(R.id.enterEnrolledTournamentEditText);
        String editTextContents = editText.getText().toString();
        editText.setText("");

        handleEnrollment(editTextContents);
    }

    public void handleEnrollment(String stringUrl) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFS_PROPERTY_REGISTERED_TOURNAMENT_URL, stringUrl);
        editor.commit();

        if (mEnrollmentHelper.isEnrolled()) {
            //TODO: Make this friendlier and allow overriding
            Toast.makeText(context, "Sorry, you're already enrolled, we only allow one enrollment right now!",
                    Toast.LENGTH_SHORT);
            return;
        }

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO: Handle this - VERY IMPORTANT! This is protection against malicious QR codes!
        }

        //TODO: Check that player is actually registered
        String name = prefs.getString(Constants.PREFS_PROPERTY_PLAYER_NAME, "");
        String dciNumber = prefs.getString(Constants.PREFS_PROPERTY_DCI_NUMBER,"");
        String regId = "foobarbooey"; //TODO: Set and fetch this in prefs

        mEnrollmentHelper.enroll(url, name, dciNumber, regId);

    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        if (!(checkPlayServices())) {
            Toast.makeText(context, R.string.noPlayServicesWarning, Toast.LENGTH_SHORT);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                String TAG="THIS IS A TAG";
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
