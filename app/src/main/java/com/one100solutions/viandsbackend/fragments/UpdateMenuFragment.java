package com.one100solutions.viandsbackend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.cards.UpdateMenuCard;
import com.one100solutions.viandsbackend.objects.DishObject;
import com.one100solutions.viandsbackend.utils.OnJSONResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayMultiChoiceAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.base.CardViewWrapper;

/**
 * Created by sujith on 22/3/15.
 */
public class UpdateMenuFragment extends Fragment {

    private MyCardArrayMultiChoiceAdapter mCardArrayAdapter;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private CardListView listView;

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

        listView = (CardListView) view.findViewById(R.id.clvUpdateMenu);
        cards.clear();
        mCardArrayAdapter = new MyCardArrayMultiChoiceAdapter(getActivity(), cards);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        }

        showCards();
    }

    public void showCards() {

        ArrayList<DishObject> menu = ViandsApplication.restaurantList.get(0).getMenu();

        for (int i = 0; i < menu.size(); i++) {
            UpdateMenuCard updateMenuCard = new UpdateMenuCard(getActivity(), menu.get(i));
            updateMenuCard.setOnLongClickListener(new Card.OnLongCardClickListener() {
                @Override
                public boolean onLongClick(Card card, View view) {
                    return mCardArrayAdapter.startActionMode(getActivity());

                }
            });
            cards.add(updateMenuCard);
        }

        mCardArrayAdapter.notifyDataSetChanged();

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
                menuItem.put("available", listView.isItemChecked(i));
                menuItem.put("_id", mainMenu.get(i).getId());
                menu.put(menuItem);

            }

        } catch (JSONException e) {

        }


        for (int i = 0; i < cards.size(); i++) {
            listView.setItemChecked(i, false);
        }

        ViandsApplication.updateMenu(getActivity(), menu.toString(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success) {
                if (success) {
                    System.out.println("Menu updated successfully");
                }
            }
        });

    }

    public class MyCardArrayMultiChoiceAdapter extends CardArrayMultiChoiceAdapter {

        public MyCardArrayMultiChoiceAdapter(Context context, List<Card> cards) {
            super(context, cards);
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //It is very important to call the super method
            super.onCreateActionMode(mode, menu);

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked, CardViewWrapper cardViewWrapper, Card card) {
            Toast.makeText(getContext(), "Click;" + position + " - " + checked, Toast.LENGTH_SHORT).show();
        }
    }
}
