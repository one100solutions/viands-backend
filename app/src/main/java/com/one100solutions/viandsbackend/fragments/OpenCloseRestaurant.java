package com.one100solutions.viandsbackend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;


/**
 * Created by sujith on 7/3/15.
 */
public class OpenCloseRestaurant extends Fragment {

    private boolean close;
    private TextView tvOpenClose;
    private ButtonRectangle btOpenClose;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        close = ViandsApplication.restaurantList.get(0).isClosed();

        View view = inflater.inflate(R.layout.fragment_open_close, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void initView(View view) {

        tvOpenClose = (TextView) view.findViewById(R.id.tvOpenClose);

        btOpenClose = (ButtonRectangle) view.findViewById(R.id.btOpenClose);
        btOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
        setText();

    }

    public void setText() {
        if (!close) {
            tvOpenClose.setText(getActivity().getString(R.string.close_restaurant_msg));
            btOpenClose.setText(getActivity().getString(R.string.close_restaurant));
        } else {
            tvOpenClose.setText(getActivity().getString(R.string.open_restaurant_msg));
            btOpenClose.setText(getActivity().getString(R.string.open_restaurant));
        }
    }

    public void updateStatus() {
        ViandsApplication.openCloseRestaurant(getActivity(), close, new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {
                if (success) {


                    // update menu
                    ViandsApplication.getMenu(getActivity(), new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success) {
                            close = ViandsApplication.restaurantList.get(0).isClosed();
                            setText();
                        }
                    });
                }
            }
        });
    }
}
