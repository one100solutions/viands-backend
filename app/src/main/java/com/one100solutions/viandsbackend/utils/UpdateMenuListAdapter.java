package com.one100solutions.viandsbackend.utils;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.one100solutions.viandsbackend.R;
import com.one100solutions.viandsbackend.objects.DishObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sujith on 18/4/15.
 */
public class UpdateMenuListAdapter extends ArrayAdapter<DishObject> implements CompoundButton.OnCheckedChangeListener {
    SparseBooleanArray mCheckStates;

    Context context;
    int layoutResourceId;
    ArrayList<DishObject> data = null;

    public UpdateMenuListAdapter(Context context, int layoutResourceId, ArrayList<DishObject> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        mCheckStates = new SparseBooleanArray(data.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AppInfoHolder holder = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppInfoHolder();

            holder.ivDishImage = (CircleImageView) row.findViewById(R.id.ivDishImage);
            holder.tvDishName = (TextView) row.findViewById(R.id.tvDishName);
            holder.tvDishCategory = (TextView) row.findViewById(R.id.tvDishCategory);
            holder.tvDishCost = (TextView) row.findViewById(R.id.tvDishCost);
            holder.chkSelect = (CheckBox) row.findViewById(R.id.cbSelect);

            row.setTag(holder);

        } else {
            holder = (AppInfoHolder) row.getTag();
        }


        DishObject dishObject = data.get(position);

        holder.tvDishName.setText(dishObject.getName());

        if (dishObject.getCategory().equals("1"))
            holder.tvDishCategory.setText("South Indian");
        else if (dishObject.getCategory().equals("2"))
            holder.tvDishCategory.setText("North Indian");
        else if (dishObject.getCategory().equals("3"))
            holder.tvDishCategory.setText("Chinese");
        else if (dishObject.getCategory().equals("4"))
            holder.tvDishCategory.setText("Snacks");
        else if (dishObject.getCategory().equals("5"))
            holder.tvDishCategory.setText("Juices");

        holder.tvDishCost.setText("\u20B9 " + String.valueOf(dishObject.getCost()));

        String imageUrl = ViandsRestClient.IMAGE_URL + dishObject.getCategory() + "/c" + dishObject.getCategory() + "." + dishObject.getSno() + ".jpg";

        Picasso.with(getContext()).load(imageUrl)
                .into(holder.ivDishImage);

        holder.chkSelect.setTag(position);
        holder.chkSelect.setChecked(mCheckStates.get(position, dishObject.isAvailable()));
        holder.chkSelect.setOnCheckedChangeListener(this);
        return row;

    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }

    static class AppInfoHolder {

        CheckBox chkSelect;
        CircleImageView ivDishImage;
        TextView tvDishName;
        TextView tvDishCategory;
        TextView tvDishCost;

    }
}
