package com.one100solutions.viandsbackend.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.AccountObject;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by sujith on 18/4/15.
 */
public class AccountCard extends Card {

    private AccountObject accountObject;

    public AccountCard(Context context) {
        this(context, R.layout.card_main_account_layout);
    }

    public AccountCard(Context context, AccountObject accountObject) {
        this(context, R.layout.card_main_account_layout);
        this.accountObject = accountObject;
    }

    public AccountCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView tvAccountId = (TextView) parent.findViewById(R.id.tvAccountId);
        tvAccountId.setText(accountObject.getId().substring(0, 5) + "...");

        TextView tvAccountName = (TextView) parent.findViewById(R.id.tvAccountName);
        tvAccountName.setText(accountObject.getName());

        TextView tvAccountPhone = (TextView) parent.findViewById(R.id.tvAccountPhone);
        tvAccountPhone.setText(String.valueOf(accountObject.getPhone()));

        TextView tvAccountRecharged = (TextView) parent.findViewById(R.id.tvAccountRecharged);
        tvAccountRecharged.setText("\u20B9 " + String.valueOf(accountObject.getTotalCredited()));

        TextView tvAccountSpent = (TextView) parent.findViewById(R.id.tvAccountSpent);
        tvAccountSpent.setText("\u20B9 " + String.valueOf(accountObject.getTotalOrdered()));

        TextView tvAccountBalance = (TextView) parent.findViewById(R.id.tvAccountBalance);
        tvAccountBalance.setText("\u20B9 " + String.valueOf(accountObject.getCredits()));

    }
}

