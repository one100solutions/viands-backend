package com.one100solutions.viandsbackend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.one100solutions.viandsbackend.R;


/**
 * Created by sujith on 7/3/15.
 */
public class AboutFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initView(View view) {


    }
}
