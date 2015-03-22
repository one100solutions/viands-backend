package com.one100solutions.viandsbackend.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.DishObject;
import com.one100solutions.viandsbackend.utils.ViandsRestClient;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by sujith on 19/3/15.
 */
public class UpdateMenuCard extends Card {

    public DishObject getDishObject() {
        return dishObject;
    }

    public void setDishObject(DishObject dishObject) {
        this.dishObject = dishObject;
    }

    private DishObject dishObject;

    public UpdateMenuCard(Context context) {
        this(context, R.layout.card_update_menu_layout);
    }

    public UpdateMenuCard(Context context, DishObject dishObject) {
        this(context, R.layout.card_update_menu_layout);
        this.dishObject = dishObject;
    }

    public UpdateMenuCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        CircleImageView ivDishImage = (CircleImageView) parent.findViewById(R.id.ivDishImage);
        TextView tvDishName = (TextView) parent.findViewById(R.id.tvDishName);
        TextView tvDishCategory = (TextView) parent.findViewById(R.id.tvDishCategory);
        TextView tvDishCost = (TextView) parent.findViewById(R.id.tvDishCost);

        tvDishName.setText(dishObject.getName());

        if (dishObject.getCategory().equals("1"))
            tvDishCategory.setText("South Indian");
        else if (dishObject.getCategory().equals("2"))
            tvDishCategory.setText("North Indian");
        else if (dishObject.getCategory().equals("3"))
            tvDishCategory.setText("Chinese");
        else if (dishObject.getCategory().equals("4"))
            tvDishCategory.setText("Snacks");
        else if (dishObject.getCategory().equals("5"))
            tvDishCategory.setText("Juices");

        tvDishCost.setText("\u20B9 " + String.valueOf(dishObject.getCost()));

        String imageUrl = ViandsRestClient.IMAGE_URL + dishObject.getCategory() + "/c" + dishObject.getCategory() + "." + dishObject.getSno() + ".jpg";

        Picasso.with(getContext()).load(imageUrl)
                .into(ivDishImage);

    }
}
