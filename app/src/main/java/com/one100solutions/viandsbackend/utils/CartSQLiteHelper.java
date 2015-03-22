package com.one100solutions.viandsbackend.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.one100solutions.viandsbackend.objects.CartObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujith on 19/3/15.
 */
public class CartSQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "CartSQLiteHelper";

    private Context mContext;

    // Database Version
    private static final int DATABASE_VERSION = DatabaseConstants.DATABASE_VERSION;
    // Database Name
    private static final String DATABASE_NAME = DatabaseConstants.DATABASE_CART;
    // Table Name
    private static final String TABLE_NAME = DatabaseConstants.TABLE_CART;

    private static final String TAG_DISH_ID = "id";
    private static final String TAG_DISH_NAME = "name";
    private static final String TAG_DISH_CATEGORY = "category";
    private static final String TAG_DISH_COST = "cost";
    private static final String TAG_DISH_SNO = "sno";
    private static final String TAG_DISH_QUANTITY = "quantity";

    public CartSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        String CREATE_CHANNEL_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
                + "id TEXT PRIMARY KEY , " + "name TEXT, "
                + "category TEXT, " + "cost INTEGER, " + "sno INTEGER, "
                + "quantity INTEGER" + ")";

        db.execSQL(CREATE_CHANNEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

        // create fresh table
        this.onCreate(db);
    }

    public void addItemToCart(CartObject cartObject) {

        CartObject obj = getCartItem(cartObject.getId());
        if (obj == null) {
            // item not in cart

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();


            values.put(TAG_DISH_ID, cartObject.getId());
            values.put(TAG_DISH_NAME, cartObject.getName());
            values.put(TAG_DISH_CATEGORY, cartObject.getCategory());
            values.put(TAG_DISH_COST, cartObject.getCost());
            values.put(TAG_DISH_SNO, cartObject.getSno());
            values.put(TAG_DISH_QUANTITY, cartObject.getQuantity());

            db.insert(TABLE_NAME, null, values);

            System.out.println("Added Dish to cart!");
            db.close();
        } else {
            // item in cart but quantity needs to be updated
            int newQuantity = obj.getQuantity() + cartObject.getQuantity();
            System.out.println("NEW QUANTITY  : " + newQuantity);

            String query = "UPDATE " + TABLE_NAME + " SET " + TAG_DISH_QUANTITY + "=" + newQuantity +
                    " WHERE " + TAG_DISH_ID + "=" + "'" + cartObject.getId() + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);
        }
    }

    public List<CartObject> getAllCartItems() {
        List<CartObject> allItems = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        CartObject cartObject = null;
        if (cursor.moveToFirst()) {
            do {
                cartObject = new CartObject();
                cartObject.setId(cursor.getString(0));
                cartObject.setName(cursor.getString(1));
                cartObject.setCategory(cursor.getString(2));
                cartObject.setCost(cursor.getInt(3));
                cartObject.setSno(cursor.getInt(4));
                cartObject.setQuantity(cursor.getInt(5));

                allItems.add(cartObject);
            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.d("Get all cart items()", allItems.toString());

        return allItems;
    }

    public CartObject getCartItem(String id) {

        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + TAG_DISH_ID + " = " + "'" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        CartObject cartObject = null;
        if (cursor.moveToFirst()) {
            cartObject = new CartObject();
            cartObject.setId(cursor.getString(0));
            cartObject.setName(cursor.getString(1));
            cartObject.setCategory(cursor.getString(2));
            cartObject.setCost(cursor.getInt(3));
            cartObject.setSno(cursor.getInt(4));
            cartObject.setQuantity(cursor.getInt(5));
        }
        cursor.close();

        if (cartObject != null)
            Log.d("getCartItem()", cartObject.toString());

        return cartObject;
    }

    public void deleteCartItem(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        boolean check = db.delete(TABLE_NAME, TAG_DISH_ID + "='"
                + id + "'", null) > 0;
        db.close();

        if (check)
            Log.d("deletion success", "");
        else
            Log.d("deletion failed ", "");
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

}
