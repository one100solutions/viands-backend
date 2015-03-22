package com.one100solutions.viandsbackend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.UserObject;
import com.one100solutions.viandsbackend.utils.SessionManager;

/**
 * Created by sujith on 10/3/15.
 */
public class AccountFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initView(View view) {

        TextView tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        TextView tvCredits = (TextView) view.findViewById(R.id.tvCredits);

        ImageView ivPhone = (ImageView) view.findViewById(R.id.ivPhone);
        ImageView ivEmail = (ImageView) view.findViewById(R.id.ivEmail);
        ImageView ivCredits = (ImageView) view.findViewById(R.id.ivCredits);

        ivPhone.setColorFilter(getActivity().getResources().getColor(R.color.gray2));
        ivEmail.setColorFilter(getActivity().getResources().getColor(R.color.gray2));
        ivCredits.setColorFilter(getActivity().getResources().getColor(R.color.gray2));

        // get user data
        SessionManager sessionManager = new SessionManager(getActivity());
        UserObject user = sessionManager.getUserDetails();

        // set data
        tvUserName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(String.valueOf(user.getPhone()));
        tvCredits.setText("\u20B9 " + String.valueOf(user.getCredits()));

    }
}
