package com.one100solutions.viandsbackend.activities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.fragments.AddCredits;
import com.one100solutions.viandsbackend.fragments.MainAccountFragment;
import com.one100solutions.viandsbackend.fragments.NotificationFragment;
import com.one100solutions.viandsbackend.fragments.OpenCloseRestaurant;
import com.one100solutions.viandsbackend.fragments.ProcessOrdersFragment;
import com.one100solutions.viandsbackend.fragments.UpdateMenuFragment;
import com.one100solutions.viandsbackend.objects.UserObject;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;
import com.one100solutions.viandsbackend.utils.SessionManager;
import com.one100solutions.viandsbackend.utils.ViandsRestClient;

import java.io.IOException;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class HomeActivity extends MaterialNavigationDrawer {

    @Override
    protected void onStart() {
        super.onStart();

        // set the indicator for child fragments
        // N.B. call this method AFTER the init() to leave the time to instantiate the ActionBarDrawerToggle
        this.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    public void onHomeAsUpSelected() {
        // when the back arrow is selected this method is called


    }

    MaterialSection accountSection;

    @Override
    public void init(Bundle savedInstanceState) {

        final SessionManager sessionManager = new SessionManager(this);
        UserObject user = sessionManager.getUserDetails();

        MaterialAccount account = new MaterialAccount(this.getResources(), user.getName(), null, null, R.drawable.header_image);
        this.addAccount(account);

        MaterialSection processOrder = newSection(getString(R.string.fragment_process_orders), R.drawable.ic_local_restaurant_white_24dp, new ProcessOrdersFragment());
        this.addSection(processOrder);

        MaterialSection addCredits = newSection(getString(R.string.fragment_credits), R.drawable.ic_credit_card_white_24dp, new AddCredits());
        this.addSection(addCredits);

        MaterialSection accounts = newSection(getString(R.string.fragment_accounts), R.drawable.ic_description_white_24dp, new MainAccountFragment());
        this.addSection(accounts);

        //MaterialSection refundCredits = newSection(getString(R.string.fragment_refund_credits), R.drawable.ic_assignment_return_white_24dp, new RefundCredits());
        //this.addSection(refundCredits);

        MaterialSection updateMenu = newSection(getString(R.string.fragment_update_menu), R.drawable.ic_menu_white_24dp, new UpdateMenuFragment());
        this.addSection(updateMenu);


        MaterialSection notification = newSection(getString(R.string.fragment_notifications), R.drawable.ic_notifications_white_24dp, new NotificationFragment());
        this.addSection(notification);

        MaterialSection openClose = newSection(getString(R.string.fragment_open_close_restaurant), R.drawable.ic_lock_white_24dp, new OpenCloseRestaurant());
        this.addSection(openClose);
//
//
//        MaterialSection aboutSection = newSection(getString(R.string.fragment_about), R.drawable.ic_info_white_24dp, new AboutFragment());
//        this.addSection(aboutSection);
//
//        MaterialSection signOutSection = newSection(getString(R.string.fragment_signout), R.drawable.ic_settings_power_white_24dp, new SignOutFragment());
//        this.addSection(signOutSection);

        this.setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        context = this;

        ViandsApplication.getMenu(context, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {

            }
        });


        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId(this);
            if (regId.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.e("Tag", "No valid Google Play services found");
        }

    }

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "Tag";

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = ViandsRestClient.GCM_SENDER_ID;

    GoogleCloudMessaging gcm;
    Context context;
    String regId;
    String result;

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String message = "";

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    regId = gcm.register(SENDER_ID);
                    message = "Device registered with reg id " + regId;

                    //Send registration Id to server
                    sendRegistrationId(regId);

                    //Store registration Id so need to register again
                    storeRegistrationId(context, regId);

                } catch (IOException e) {
                    message = "Error:" + e.getMessage();
                }

                return message;
            }

            @Override
            protected void onPostExecute(String s) {
                result = s;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationId(String regId) {

        Log.e("Tag", "Registration Id is " + regId);
        ViandsApplication.sendGCMSenderId(context, regId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
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
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    protected String getRegistrationId(Context context) {

        SessionManager sessionManager = new SessionManager(context);
        String registrationId = sessionManager.getGCMSenderId();

        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = sessionManager.getAppVersion();
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package Name" + e);
        }
    }


    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {

        SessionManager sessionManager = new SessionManager(context);
        sessionManager.storeGCMSenderId(regId);
        sessionManager.storeAppVersion(getAppVersion(context));

    }

}
