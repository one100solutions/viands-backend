package com.one100solutions.viandsbackend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.objects.DishObject;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;
import com.one100solutions.viandsbackend.utils.UpdateMenuListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by sujith on 22/3/15.
 */
public class UpdateMenuFragment extends Fragment {

    private UpdateMenuListAdapter adapter;
    private ListView listView;

    private ButtonRectangle btUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_menu, container, false);
        initView(view);

        return view;
    }

    public void initView(View view) {
        btUpdate = (ButtonRectangle) view.findViewById(R.id.btUpdate);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListToBeUpdated();
            }
        });

        listView = (ListView) view.findViewById(R.id.lvUpdateMenu);

        adapter = new UpdateMenuListAdapter(getActivity(), R.layout.card_update_menu_layout, ViandsApplication.restaurantList.get(0).getMenu());
        if (listView != null) {
            listView.setAdapter(adapter);
        }

    }


    public void getListToBeUpdated() {

        ArrayList<DishObject> mainMenu = ViandsApplication.restaurantList.get(0).getMenu();

        JSONArray menu = new JSONArray();
        try {

            for (int i = 0; i < mainMenu.size(); i++) {
                JSONObject menuItem = new JSONObject();
                menuItem.put("category", mainMenu.get(i).getCategory());
                menuItem.put("sno", mainMenu.get(i).getSno());
                menuItem.put("name", mainMenu.get(i).getName());
                menuItem.put("cost", mainMenu.get(i).getCost());
                menuItem.put("available", adapter.isChecked(i));
                menuItem.put("_id", mainMenu.get(i).getId());
                menu.put(menuItem);

            }

        } catch (JSONException e) {

        }

        System.out.println("UPDATED MENU:" + menu.toString());


        ViandsApplication.updateMenu(getActivity(), menu.toString(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {
                if (success) {
                    System.out.println("Menu updated successfully");

                    ViandsApplication.getMenu(getActivity(), new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success) {

                        }
                    });
                }
            }
        });

    }

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
