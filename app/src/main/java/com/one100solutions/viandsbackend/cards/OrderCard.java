package com.one100solutions.viandsbackend.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.activities.ViandsApplication;
import com.one100solutions.viandsbackend.objects.CartObject;
import com.one100solutions.viandsbackend.objects.DishObject;
import com.one100solutions.viandsbackend.objects.OrderObject;
import com.one100solutions.viandsbackend.utils.ViandsRestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;

/**
 * Created by sujith on 19/3/15.
 */
public class OrderCard extends CardWithList {

    public OrderObject getOrderObject() {
        return orderObject;
    }

    public void setOrderObject(OrderObject orderObject) {
        this.orderObject = orderObject;
    }

    private OrderObject orderObject;

    public OrderCard(Context context) {
        super(context);
    }

    public OrderCard(Context context, OrderObject orderObject) {
        this(context);
        this.orderObject = orderObject;
    }

    @Override
    protected CardHeader initCardHeader() {

        return new OrderCardHeader(getContext(), orderObject);
    }

    @Override
    protected void initCard() {

        //Set the whole card as swipeable
//        setSwipeable(true);
//        setOnSwipeListener(new OnSwipeListener() {
//            @Override
//            public void onSwipe(Card card) {
//                Toast.makeText(getContext(), "Swipe on " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    protected List<ListObject> initChildren() {

        //Init the list
        List<ListObject> mObjects = new ArrayList<ListObject>();

        ArrayList<CartObject> items = orderObject.getItems();
        for (CartObject item : items) {
            ItemObject itemObject = new ItemObject(this);
            itemObject.setCartObject(item);
            mObjects.add(itemObject);
        }

        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {
        CircleImageView ivDishImage = (CircleImageView) convertView.findViewById(R.id.ivDishImage);
        TextView tvDishName = (TextView) convertView.findViewById(R.id.tvDishName);
        TextView tvDishQuantity = (TextView) convertView.findViewById(R.id.tvDishQuantity);

        ItemObject itemObject = (ItemObject) object;
        CartObject cartObject = itemObject.getCartObject();

        boolean found = false;
        DishObject dishObject = null;
        String id = cartObject.getId();
        if (ViandsApplication.restaurantList != null && ViandsApplication.restaurantList.size() != 0) {

            for (DishObject menu : ViandsApplication.restaurantList.get(0).getMenu()) {
                if (menu.getId().equals(id)) {
                    dishObject = menu;

                    tvDishName.setText(dishObject.getName());
                    tvDishQuantity.setText(String.valueOf(cartObject.getQuantity()));
                    String imageUrl = ViandsRestClient.IMAGE_URL + dishObject.getCategory() + "/c" + dishObject.getCategory() + "." + dishObject.getSno() + ".jpg";

                    Picasso.with(getContext()).load(imageUrl)
                            .into(ivDishImage);
                    found = true;

                    break;
                }
            }

        }

        if (!found) {
            tvDishName.setText(cartObject.getId());
            tvDishQuantity.setText(String.valueOf(cartObject.getQuantity()));
        }

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.card_order_inner_layout;
    }

    public class ItemObject extends DefaultListObject {

        public CartObject getCartObject() {
            return cartObject;
        }

        public void setCartObject(CartObject cartObject) {
            this.cartObject = cartObject;
        }

        private CartObject cartObject;

        public ItemObject(Card parentCard) {
            super(parentCard);
        }
    }


    public class OrderCardHeader extends CardHeader {

        private OrderObject orderObject;

        public OrderCardHeader(Context context) {
            super(context, R.layout.card_order_header_layout);
        }

        public OrderCardHeader(Context context, OrderObject orderObject) {
            this(context);
            this.orderObject = orderObject;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {


            TextView tvOrderId = (TextView) view.findViewById(R.id.tvOrderId);
            TextView tvOrderTime = (TextView) view.findViewById(R.id.tvOrderTime);
            TextView tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);
            TextView tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);

            TextView tvOrderUserName = (TextView) view.findViewById(R.id.tvOrderUserName);


            tvOrderUserName.setText(orderObject.getName());

            tvOrderId.setText(orderObject.getId());
            tvOrderTime.setText("Time: " + orderObject.getTime());
            tvTotalAmount.setText("\u20B9 " + String.valueOf(orderObject.getTotalAmount()));

            if (orderObject.isComplete() && orderObject.isDelivered()) {
                tvOrderStatus.setText("Status: Delivered");
            } else if (orderObject.isComplete()) {
                tvOrderStatus.setText("Status: Complete");
            } else {
                tvOrderStatus.setText("Status: Processing");
            }

        }
    }

}
