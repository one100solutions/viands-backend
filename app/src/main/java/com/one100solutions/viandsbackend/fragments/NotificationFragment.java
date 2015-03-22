package com.one100solutions.viandsbackend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonRectangle;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;
import com.rengwuxian.materialedittext.MaterialEditText;


public class NotificationFragment extends Fragment {

    private static final String LOG_TAG = "Add credits Fragment";

    private MaterialEditText edtTitle, edtMessage;
    private ButtonRectangle btSendNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater
                .inflate(R.layout.fragment_send_notification, container, false);
        initView(view);

        return view;
    }

    public void initView(View view) {

        edtTitle = (MaterialEditText) view.findViewById(R.id.edtTitle);
        edtMessage = (MaterialEditText) view.findViewById(R.id.edtMessage);


        btSendNotification = (ButtonRectangle) view.findViewById(R.id.btSendNotification);
        btSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEntries()) {
                    verify();
                }

            }
        });


    }

    public boolean checkEntries() {

        String title = edtTitle.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();

        if (title.length() == 0) {
            edtTitle.requestFocus();
            showDialog(getString(R.string.dialog_invalid_entry));
            return false;
        } else if (message.length() == 0) {
            edtMessage.requestFocus();
            showDialog(getString(R.string.dialog_invalid_entry));
            return false;
        }

        return true;
    }

    public void verify() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .content("Send notification??")
                .positiveText(R.string.dialog_ok)
                .negativeText(R.string.dialog_cancel)
                .cancelable(false)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        sendNotification();
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


    public void sendNotification() {

        String title = edtTitle.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();

        ViandsApplication.sendNotification(getActivity(), title, message, new OnJSONResponseCallback() {

            @Override
            public void onJSONResponse(boolean success) {
                if (success) {
                    //goToHome();
                    System.out.println("Notification sent successfully");
                }
            }
        });
    }

}
