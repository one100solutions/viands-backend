package com.one100solutions.viandsbackend.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.NotificationObject;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by sujith on 7/3/15.
 */
public class NotificationCard extends Card {

    private NotificationObject notificationObject;

    public NotificationCard(Context context) {
        this(context, R.layout.card_notification_layout);
    }

    public NotificationCard(Context context, NotificationObject notificationObject) {
        this(context, R.layout.card_notification_layout);
        this.notificationObject = notificationObject;
    }

    public NotificationCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ImageView ivOfferIcon = (ImageView) parent.findViewById(R.id.ivNotification);
        ivOfferIcon.setColorFilter(getContext().getResources().getColor(R.color.red));

        TextView tvNotificationTitle = (TextView) parent.findViewById(R.id.tvNotificationTitle);
        tvNotificationTitle.setText(notificationObject.getTitle());

        TextView tvNotificationMessage = (TextView) parent.findViewById(R.id.tvNotificationMessage);
        tvNotificationMessage.setText(notificationObject.getMessage());

    }
}
