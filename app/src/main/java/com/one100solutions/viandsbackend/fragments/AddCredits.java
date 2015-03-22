package com.one100solutions.viandsbackend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonRectangle;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;
import com.rengwuxian.materialedittext.MaterialEditText;


public class AddCredits extends Fragment {

    private static final String LOG_TAG = "Add credits Fragment";

    private MaterialEditText edtUserPhone, edtAmount;
    private ButtonRectangle btAddCredits;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_add_credits, container, false);
        initView(view);

        return view;
    }

    public void initView(View view) {

        edtUserPhone = (MaterialEditText) view.findViewById(R.id.edtUserPhone);
        edtAmount = (MaterialEditText) view.findViewById(R.id.edtAmount);


        btAddCredits = (ButtonRectangle) view.findViewById(R.id.btAddCredits);
        btAddCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEntries()) {
                    verify();
                }

            }
        });


    }

    public boolean checkEntries() {

        String userPhone = edtUserPhone.getText().toString().trim();
        String amount = edtAmount.getText().toString().trim();

        if (userPhone.length() != 10 || !(TextUtils.isDigitsOnly(userPhone))) {
            edtUserPhone.requestFocus();
            showDialog(getString(R.string.dialog_invalid_entry));
            return false;
        } else if (amount.length() == 0) {
            edtAmount.requestFocus();
            showDialog(getString(R.string.dialog_invalid_entry));
            return false;
        }


        return true;
    }

    public void verify() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .content("Please verify the amount")
                .positiveText(R.string.dialog_ok)
                .negativeText(R.string.dialog_cancel)
                .cancelable(false)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        addCredits();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }


                }).build();

        dialog.show();
    }

    public void showDialog(String content) {
        new MaterialDialog.Builder(getActivity())
                .content(content)
                .positiveText(R.string.dialog_ok)
                .show();
    }


    public void addCredits() {

        String userPhone = edtUserPhone.getText().toString().trim();
        String amount = edtAmount.getText().toString().trim();

        ViandsApplication.addCredits(getActivity(), userPhone, amount, new OnJSONResponseCallback() {

            @Override
            public void onJSONResponse(boolean success) {
                if (success) {
                    //goToHome();
                    System.out.println("Adding credits successful");
                }
            }
        });
    }

}
