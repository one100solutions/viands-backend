package com.one100solutions.viandsbackend.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.cards.OrderCard;
import com.one100solutions.viandsbackend.objects.CartObject;
import com.one100solutions.viandsbackend.objects.OrderObject;
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

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.dismiss.DefaultDismissableManager;

/**
 * Created by sujith on 20/3/15.
 */
public class ProcessOrdersFragment extends Fragment {

    private ArrayList<OrderObject> ordersList = new ArrayList<>();
    //private ProgressBarIndeterminate progressBarIndeterminate;

    private CardArrayAdapter mCardArrayAdapter1;
    private ArrayList<Card> cards1 = new ArrayList<Card>();
    private CardListView clv1;

    private CardArrayAdapter mCardArrayAdapter2;
    private ArrayList<Card> cards2 = new ArrayList<Card>();
    private CardListView clv2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_process_orders, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initView(View view) {

        cards1.clear();
        mCardArrayAdapter1 = new CardArrayAdapter(getActivity(), cards1);
        mCardArrayAdapter1.setDismissable(new RightDismissableManager());

        clv1 = (CardListView) view.findViewById(R.id.clvUnprocessedOrders);
        if (clv1 != null) {
            clv1.setAdapter(mCardArrayAdapter1);
        }


        cards2.clear();
        mCardArrayAdapter2 = new CardArrayAdapter(getActivity(), cards2);
        mCardArrayAdapter2.setDismissable(new RightDismissableManager());
        clv2 = (CardListView) view.findViewById(R.id.clvProcessingOrders);
        if (clv2 != null) {
            clv2.setAdapter(mCardArrayAdapter2);
        }


        ViandsApplication.getMenu(getActivity(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {
                if (success) {
                    fetchOrders(getActivity(), new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success) {
                            if (success) {
                                showCards();
                            }
                        }
                    });
                }
            }
        });


    }

    public void showCards() {
        cards1.clear();

        for (int i = 0; i < ordersList.size(); i++) {
            System.out.println(ordersList.get(i));

            OrderCard orderCard = new OrderCard(getActivity(), ordersList.get(i));
            orderCard.init();
            orderCard.setSwipeable(true);
            orderCard.setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    //Do something
                    new SnackBar(getActivity(), "Moved to processing").show();

                    OrderObject swipedObject = ((OrderCard) card).getOrderObject();
                    addToProcessingList(swipedObject);

                    ViandsApplication.orderComplete(getActivity(), swipedObject.getId(), new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success) {
                            if (success) {
                                System.out.println("Order completed successfully");
                            }
                        }
                    });

                }
            });
            cards1.add(orderCard);
        }
        mCardArrayAdapter1.notifyDataSetChanged();
    }

    public void addToProcessingList(OrderObject orderObject) {
        OrderCard newCard = new OrderCard(getActivity(), orderObject);
        newCard.init();
        newCard.setSwipeable(true);
        newCard.setOnSwipeListener(new Card.OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                //Do something
                new SnackBar(getActivity(), "Processing completed").show();
            }
        });
        cards2.add(newCard);
        mCardArrayAdapter2.notifyDataSetChanged();
    }

    public class RightDismissableManager extends DefaultDismissableManager {

        @Override
        public SwipeDirection getSwipeDirectionAllowed() {
            return SwipeDirection.RIGHT;
        }
    }

    static MaterialDialog progress;

    public void fetchOrders(final Context context, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        UserObject userObject = sessionManager.getUserDetails();

        String token = userObject.getToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("token", token);
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.GET_ORDERS, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                //       progressBarIndeterminate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                //      progressBarIndeterminate.setVisibility(View.GONE);
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
                    } else {
                        JSONArray orderJSONArray = response.getJSONArray("orders");
                        for (int i = 0; i < orderJSONArray.length(); i++) {
                            JSONObject orderJSONObject = orderJSONArray.getJSONObject(i);
                            OrderObject orderObject = new OrderObject();

                            orderObject.setRestaurantId(orderJSONObject.getString("restaurant_id"));
                            orderObject.setUserId(orderJSONObject.getString("user_id"));
                            orderObject.setTime(orderJSONObject.getString("time"));
                            orderObject.setType(orderJSONObject.getString("type"));
                            orderObject.setId(orderJSONObject.getString("id"));
                            //System.out.println(orderObject);

                            orderObject.setComplete(orderJSONObject.getBoolean("complete"));

                            System.out.println(orderObject);
                            System.out.println("DOne");

                            ArrayList<CartObject> itemsList = new ArrayList<CartObject>();
                            JSONArray itemsJSONArray = orderJSONObject.getJSONArray("items");
                            for (int j = 0; j < itemsJSONArray.length(); j++) {
                                JSONObject itemJSONObject = itemsJSONArray.getJSONObject(j);
                                CartObject cartObject = new CartObject();

                                cartObject.setId(itemJSONObject.getString("id"));
                                cartObject.setComplete(itemJSONObject.getBoolean("complete"));
                                cartObject.setQuantity(itemJSONObject.getInt("quantity"));

                                itemsList.add(cartObject);

                            }
                            orderObject.setItems(itemsList);
                            ordersList.add(orderObject);
                        }
                        callback.onJSONResponse(true);
                    }
                } catch (Exception e) {
                    new SnackBar((Activity) context, ViandsRestClient.FAILURE + " " + e.getMessage(), null, null).show();
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
}
