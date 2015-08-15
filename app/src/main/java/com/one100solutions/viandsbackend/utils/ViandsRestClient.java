package com.one100solutions.viandsbackend.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by sujith on 10/3/15.
 */
public class ViandsRestClient {

    // Main
    //private static final String BASE_URL = "http://54.152.155.94:3000/";

    // local
    //private static final String BASE_URL = "http://192.168.1.115:3000/";

    // Test
    private static final String BASE_URL = "http://52.2.11.50:3000/";

    public static final String LOGIN = "login_restaurant";
    public static final String RESTAURANTS = "restaurants";
    public static final String GET_ORDERS = "get_order";
    public static final String ADD_CREDITS = "add_credits";
    public static final String REFUND_CREDITS = "refund_credits";
    public static final String IMAGE_URL = "http://d3g1clmq5hrgtm.cloudfront.net/category%20";
    public static final String REGISTER_GCM = "register_gcm";
    public static final String GCM_SENDER_ID = "1079726291627";
    public static final String UPDATE_MENU = "change_menu";
    public static final String ORDER_COMPLETE = "order_complete";
    public static final String ORDER_DELIVERED = "order_delivered";
    public static final String CLOSE_RESTAURANT = "close_restaurant";
    public static final String ACCOUNTS = "account_info";
    public static final String ADD_NOTIFICATIONS = "add_notification";


    public static final String FAILURE = "Oops! Something went wrong!";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static SyncHttpClient syncHttpClient = new SyncHttpClient();

    // async
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // async
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    // sync
    public static void syncGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        syncHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    // sync
    public static void syncPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        syncHttpClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
