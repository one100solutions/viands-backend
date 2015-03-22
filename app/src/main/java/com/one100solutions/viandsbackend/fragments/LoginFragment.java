package com.one100solutions.viandsbackend.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.views.ButtonRectangle;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.HomeActivity;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;
import com.rengwuxian.materialedittext.MaterialEditText;


public class LoginFragment extends Fragment {

    private final int logoStayDuration = 1500;

    private ImageView ivAppIcon;
    private TextView tvAppTag;

    private MaterialEditText edtUserName, edtUserPassword;
    private ButtonRectangle btLogin;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         * Exit on back press on login fragment
         */
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }


    public void initView(View view) {

        final LinearLayout llLoginBox1 = (LinearLayout) view
                .findViewById(R.id.llLoginBox1);
        final LinearLayout llLoginBox2 = (LinearLayout) view
                .findViewById(R.id.llLoginBox2);

        ivAppIcon = (ImageView) view.findViewById(R.id.ivAppIcon);
        tvAppTag = (TextView) view.findViewById(R.id.tvAppTag);

        edtUserName = (MaterialEditText) llLoginBox1.findViewById(R.id.edtUserName);
        edtUserPassword = (MaterialEditText) llLoginBox1.findViewById(R.id.edtUserPassword);
        btLogin = (ButtonRectangle) llLoginBox2.findViewById(R.id.btLogin);


        ivAppIcon.setVisibility(View.VISIBLE);
        tvAppTag.setVisibility(View.VISIBLE);

        llLoginBox1.setVisibility(View.GONE);
        llLoginBox2.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animTranslate = AnimationUtils.loadAnimation(
                        getActivity(), R.anim.translate);
                animTranslate.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        llLoginBox1.setVisibility(View.VISIBLE);
                        llLoginBox2.setVisibility(View.VISIBLE);

                        ivAppIcon.setVisibility(View.GONE);
                        tvAppTag.setVisibility(View.GONE);

                        Animation animFade = AnimationUtils.loadAnimation(
                                getActivity(), R.anim.fade);
                        llLoginBox1.startAnimation(animFade);
                        llLoginBox2.startAnimation(animFade);
                    }
                });

                ivAppIcon.startAnimation(animTranslate);
            }
        }, logoStayDuration);



        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtUserName.getText().toString();
                String password = edtUserPassword.getText().toString();

                // check if fields are empty
                if (email.trim().length() > 0 && password.trim().length() > 0) {

                    doLogin();

                } else {
                    // fields are empty. Display dialog
                    showInvalidLoginDialog(getActivity().getString(R.string.dialog_invalid_login));
                }


            }
        });
    }

    /**
     * Invalid login dialog
     */
    private void showInvalidLoginDialog(String content) {
        new MaterialDialog.Builder(getActivity())
                .content(content)
                .positiveText(R.string.dialog_ok)
                .show();
    }

    MaterialDialog progress;

    public void doLogin() {
        String userPhone = edtUserName.getText().toString().trim();
        String userPassword = edtUserPassword.getText().toString().trim();
        ViandsApplication.doLogin(getActivity(), userPhone, userPassword, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {
                //do something with the JSON
                if (success) {
                    goToHome();
                }
            }
        });
    }

    public void showDialog(String content) {
        new MaterialDialog.Builder(getActivity())
                .content(content)
                .positiveText(R.string.dialog_ok)
                .show();
    }


    public void goToHome() {
        // call HomeActivity
        Intent intent = new Intent(getActivity(),
                HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
