package com.one100solutions.viandsbackend.fragments;

/**
 * Created by sujith on 18/4/15.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.gc.materialdesign.widgets.SnackBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.cards.AccountCard;
import com.one100solutions.viandsbackend.objects.AccountHeaderObject;
import com.one100solutions.viandsbackend.objects.AccountObject;
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


/**
 * Created by sujith on 7/3/15.
 */
public class MainAccountFragment extends Fragment {

    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private CardListView clvAccounts;

    private ArrayList<AccountObject> accountList = new ArrayList<>();
    private ProgressBarIndeterminate progressBarIndeterminate;

    private AccountHeaderObject accountHeaderObject = null;
    private TextView tvTotalOrdered, tvTotalCredited, tvCredited, tvOrdered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_refresh:
                updateAccounts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_accounts, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initView(View view) {
        cards.clear();
        accountList.clear();

        tvTotalCredited = (TextView) view.findViewById(R.id.tvTotalCredited);
        tvTotalOrdered = (TextView) view.findViewById(R.id.tvTotalOrdered);
        tvCredited = (TextView) view.findViewById(R.id.tvCredited);
        tvOrdered = (TextView) view.findViewById(R.id.tvOrdered);

        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        clvAccounts = (CardListView) view.findViewById(R.id.clvAccounts);
        if (clvAccounts != null) {
            clvAccounts.setAdapter(mCardArrayAdapter);
        }
        progressBarIndeterminate = (ProgressBarIndeterminate) view.findViewById(R.id.progressBarIndeterminate);
        progressBarIndeterminate.setVisibility(View.GONE);

        updateAccounts();
    }

    public void updateAccounts() {

        accountList.clear();
        cards.clear();
        setListViewHeightBasedOnChildren(clvAccounts);
        fetchAccounts(getActivity(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {
                if (success) {
                    showCards();
                }
            }
        });

    }

    public void showCards() {

        if (accountHeaderObject != null) {
            tvTotalCredited.setText("TOTAL CREDITED : " + "\u20B9 " + String.valueOf(accountHeaderObject.getTotalCredited()));
            tvTotalOrdered.setText("TOTAL ORDERED : " + "\u20B9 " + String.valueOf(accountHeaderObject.getTotalOrdered()));
            tvCredited.setText("CREDITED : " + "\u20B9 " + String.valueOf(accountHeaderObject.getCredited()));
            tvOrdered.setText("ORDERED : " + "\u20B9 " + String.valueOf(accountHeaderObject.getOrdered()));
        }

        cards.clear();
        for (int i = 0; i < accountList.size(); i++) {
            AccountCard accountCard = new AccountCard(getActivity(), accountList.get(i));
            cards.add(accountCard);
        }
        mCardArrayAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(clvAccounts);
    }

    public void fetchAccounts(final Context context, final OnJSONResponseCallback callback) {

        if (!ConnectionDetector.isConnectingToInternet(context)) {
            showInternetConnectionDialog(context);
            callback.onJSONResponse(false);
            return;
        }

        SessionManager sessionManager = new SessionManager(context);
        UserObject userObject = sessionManager.getUserDetails();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("token", userObject.getToken());
        RequestParams params = new RequestParams(paramMap);

        ViandsRestClient.post(ViandsRestClient.ACCOUNTS, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressBarIndeterminate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                progressBarIndeterminate.setVisibility(View.GONE);
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
                        JSONObject accountJSONObject = response.getJSONObject("account");
                        accountHeaderObject = new AccountHeaderObject();
                        accountHeaderObject.setTotalOrdered(accountJSONObject.getInt("totalOrdered"));
                        accountHeaderObject.setTotalCredited(accountJSONObject.getInt("totalCredited"));
                        accountHeaderObject.setCredited(accountJSONObject.getInt("credited"));
                        accountHeaderObject.setOrdered(accountJSONObject.getInt("ordered"));

                        JSONArray recordsJSONArray = accountJSONObject.getJSONArray("records");
                        for (int i = 0; i < recordsJSONArray.length(); i++) {
                            JSONObject item = recordsJSONArray.getJSONObject(i);
                            AccountObject accountObject = new AccountObject();
                            accountObject.setId(item.getString("id"));
                            accountObject.setName(item.getString("name"));
                            accountObject.setPhone(item.getLong("phone"));
                            accountObject.setCredits(item.getInt("credits"));
                            accountObject.setTotalCredited(item.getInt("totalCredited"));
                            accountObject.setTotalOrdered(item.getInt("totalOrdered"));

                            accountList.add(accountObject);
                        }
                        callback.onJSONResponse(true);

                    }
                } catch (Exception e) {

                    new SnackBar((Activity) context, e.getMessage(), null, null).show();
                    callback.onJSONResponse(false);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                new SnackBar((Activity) context, ViandsRestClient.FAILURE).show();
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

    /**
     * Expand a list view
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
