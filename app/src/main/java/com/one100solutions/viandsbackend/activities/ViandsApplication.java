package com.one100solutions.viandsbackend.activities;

/**
 * Created by sujith on 14/3/15.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.DishObject;
import com.one100solutions.viandsbackend.objects.RestaurantObject;
import com.one100solutions.viandsbackend.objects.UserObject;
import com.one100solutions.viandsbackend.utils.ConnectionDetector;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;
import com.one100solutions.viandsbackend.utils.SessionManager;
import com.one100solutions.viandsbackend.utils.ViandsRestClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ViandsApplication extends Application {

    public static boolean tabletMode = false;
    public static boolean landscapeMode = false;
    public static boolean isAppOpen = false;

    public static ArrayList<RestaurantObject> restaurantList = new ArrayList<>();

    private static Context context;
    private static ViandsApplication instance;

    private static String LOG_TAG = "Viands Application Class";

    public static Context getAppContext() {
        return context;
    }

    public static ViandsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();

//        landscapeMode = getResources().getBoolean(R.bool.landscapeMode);
//        tabletMode = getResources().getBoolean(R.bool.tabletMode);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static void getMenu(final Context context, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        ViandsRestClient.get(ViandsRestClient.RESTAURANTS, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                    } else {

                        ViandsApplication.restaurantList = new ArrayList<RestaurantObject>();

                        JSONArray restaurantsJSON = response.getJSONArray("restaurants");
                        for (int i = 0; i < restaurantsJSON.length(); i++) {
                            JSONObject obj = restaurantsJSON.getJSONObject(i);

                            RestaurantObject restaurant = new RestaurantObject();
                            restaurant.setId(obj.getString("_id"));
                            restaurant.setName(obj.getString("name"));
                            restaurant.setLocation(obj.getString("location"));
                            restaurant.setClose(obj.getBoolean("close"));

                            ArrayList<DishObject> menu = new ArrayList<DishObject>();
                            JSONArray menuJSON = obj.getJSONArray("menu");
                            for (int j = 0; j < menuJSON.length(); j++) {
                                JSONObject item = menuJSON.getJSONObject(j);

                                DishObject dish = new DishObject();
                                dish.setName(item.getString("name"));
                                dish.setId(item.getString("_id"));
                                dish.setAvailable(item.getBoolean("available"));
                                dish.setCategory(item.getString("category"));
                                try {
                                    dish.setCost(item.getInt("cost"));
                                } catch (Exception e) {
                                    dish.setCost(0);
                                }

                                dish.setSno(item.getInt("sno"));

                                menu.add(dish);

                            }

                            restaurant.setMenu(menu);
                            ViandsApplication.restaurantList.add(restaurant);

                            callback.onJSONResponse(true);

                        }
                    }


                } catch (Exception e) {

                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }

        });

    }

    static MaterialDialog progress;


    public static void showInternetConnectionDialog(Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_error)
                .content(R.string.dialog_network_connection)
                .positiveText(R.string.dialog_ok)
                .iconRes(R.drawable.ic_error_red_24dp)
                .show();
    }

    public static void showDialog(Context context, String content) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(R.string.dialog_ok)
                .show();
    }


    /**
     * Logining API
     */
    public static void doLogin(final Context context, final String userName, final String userPassword, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username", userName);
        paramMap.put("password", userPassword);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progress = new MaterialDialog.Builder(context)
                        .title(R.string.dialog_loggin_in)
                        .content(R.string.dialog_please_wait)
                        .progress(true, 0).show();
            }

            @Override
            public void onFinish() {
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        JSONObject restaurantJSON = response.getJSONObject("Restaurant");
                        UserObject userObject = new UserObject();
                        userObject.setName(restaurantJSON.getString("username"));
                        userObject.setToken(restaurantJSON.getString("token"));

                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.createLoginSession(userObject);

                        callback.onJSONResponse(true);

                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }


    /**
     * SEND GCM ID ---- NOTE SyncHttpClient used
     */
    public static void sendGCMSenderId(final Context context, String senderId) {

        SessionManager sessionManager = new SessionManager(context);
        UserObject userObject = sessionManager.getUserDetails();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("token", userObject.getToken());
        paramMap.put("gcm_id", senderId);
        // 0 - restaurant, 1 - user
        paramMap.put("mode", "0");

        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.syncPost(ViandsRestClient.REGISTER_GCM, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("ResponseGCM:" + response);

            }


        });
    }

    /**
     * Add credits API
     */
    public static void addCredits(final Context context, final String userPhone, final String amount, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();
        String userName = sessionManager.getUserDetails().getName();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username", userName);
        paramMap.put("tar_phone", userPhone);
        paramMap.put("amount", amount);
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.ADD_CREDITS, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progress = new MaterialDialog.Builder(context)
                        .title(R.string.dialog_add_credits)
                        .content(R.string.dialog_please_wait)
                        .progress(true, 0).show();
            }

            @Override
            public void onFinish() {
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        String message = response.getString("message");
                        showDialog(context, message);

                        callback.onJSONResponse(true);

                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }

    /**
     * Refund credits API
     */
    public static void refundCredits(final Context context, final String userPhone, final String amount, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();
        String userName = sessionManager.getUserDetails().getName();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username", userName);
        paramMap.put("tar_phone", userPhone);
        paramMap.put("amount", amount);
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.REFUND_CREDITS, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progress = new MaterialDialog.Builder(context)
                        .title(R.string.dialog_refund_credits)
                        .content(R.string.dialog_please_wait)
                        .progress(true, 0).show();
            }

            @Override
            public void onFinish() {
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        String message = response.getString("message");
                        showDialog(context, message);

                        callback.onJSONResponse(true);

                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }


    /**
     * Update menu API
     */
    public static void updateMenu(final Context context, final String menu, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("menu", menu);
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.UPDATE_MENU, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progress = new MaterialDialog.Builder(context)
                        .title(R.string.dialog_update_menu)
                        .content(R.string.dialog_please_wait)
                        .progress(true, 0).show();
            }

            @Override
            public void onFinish() {
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(true);
                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }

    /**
     * Update menu API
     */
    public static void sendNotification(final Context context, final String title, final String message, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("title", title);
        paramMap.put("message", message);
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.ADD_NOTIFICATIONS, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progress = new MaterialDialog.Builder(context)
                        .title(R.string.dialog_send_notification)
                        .content(R.string.dialog_please_wait)
                        .progress(true, 0).show();
            }

            @Override
            public void onFinish() {
                progress.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(true);
                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }

    /**
     * Order complete
     */
    public static void orderComplete(final Context context, final String orderId, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("order_id", orderId);
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.ORDER_COMPLETE, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        // String message = response.getString("message");
                        //showDialog(context, message);
                        callback.onJSONResponse(true);
                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }

    /**
     * Order complete
     */
    public static void orderDelivered(final Context context, final String orderId, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("order_id", orderId);
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.ORDER_DELIVERED, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(true);
                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }

    /**
     * Open Close
     */
    public static void openCloseRestaurant(final Context context, final boolean close, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getUserDetails().getToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("restaurant_id", ViandsApplication.restaurantList.get(0).getId());
        paramMap.put("close", String.valueOf(!close));
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.CLOSE_RESTAURANT, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Response:" + response);
                try {
                    boolean error = response.getBoolean("err");
                    if (error) {
                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(false);

                    } else {

                        String message = response.getString("message");
                        showDialog(context, message);
                        callback.onJSONResponse(true);
                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                    callback.onJSONResponse(false);

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
                callback.onJSONResponse(false);
            }


        });
    }

}
