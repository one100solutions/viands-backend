package com.one100solutions.viandsbackend.fragments;

/**
 * Created by sujith on 5/3/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.one100solutions.viandsbackend.utils.SessionManager;


public class SignOutFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.logoutUser();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
