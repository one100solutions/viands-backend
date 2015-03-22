package com.one100solutions.viandsbackend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.fragments.LoginFragment;
import com.one100solutions.viandsbackend.utils.SessionManager;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            nextFragment(new LoginFragment(), getString(R.string.fragment_login));
        }
    }

    public void nextFragment(Fragment frag, String fragName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.addToBackStack(fragName);
        fragmentTransaction.add(R.id.fragment_container, frag);
        fragmentTransaction.commit();
    }

}
