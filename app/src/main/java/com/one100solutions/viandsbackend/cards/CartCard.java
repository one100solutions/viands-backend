package com.one100solutions.viandsbackend.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.CartObject;
import com.one100solutions.viandsbackend.utils.ViandsRestClient;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by sujith on 19/3/15.
 */
public class CartCard extends Card {

    public CartObject getCartObject() {
        return cartObject;
    }

    public void setCartObject(CartObject cartObject) {
        this.cartObject = cartObject;
    }

    private CartObject cartObject;

    public CartCard(Context context) {
        this(context, R.layout.card_cart_layout);
    }

    public CartCard(Context context, CartObject cartObject) {
        this(context, R.layout.card_cart_layout);
        this.cartObject = cartObject;
    }

    public CartCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //  ImageView ivDishImage = (ImageView) parent.findViewById(R.id.ivDishImage);

        CircleImageView ivDishImage = (CircleImageView) parent.findViewById(R.id.ivDishImage);
        TextView tvDishName = (TextView) parent.findViewById(R.id.tvDishName);
        TextView tvDishQuantity = (TextView) parent.findViewById(R.id.tvDishQuantity);
        TextView tvDishCost = (TextView) parent.findViewById(R.id.tvDishCost);

        tvDishName.setText(cartObject.getName());
        tvDishQuantity.setText("Quantity : " + String.valueOf(cartObject.getQuantity()));
        tvDishCost.setText(String.valueOf(cartObject.getCost()));

        String imageUrl = ViandsRestClient.IMAGE_URL + cartObject.getCategory() + "/c" + cartObject.getCategory() + "." + cartObject.getSno() + ".jpg";

        Picasso.with(getContext()).load(imageUrl)
                .into(ivDishImage);



    }
}
