package com.one100solutions.viandsbackend.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.one100solutions.viandsbackend.objects.OrderObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujith on 19/3/15.
 */
public class OrderSQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "OrderSQLiteHelper";

    private Context mContext;

    // Database Version
    private static final int DATABASE_VERSION = DatabaseConstants.DATABASE_VERSION;
    // Database Name
    private static final String DATABASE_NAME = DatabaseConstants.DATABASE_ORDER;
    // Table Name
    private static final String TABLE_NAME = DatabaseConstants.TABLE_ORDER;

    private static final String TAG_ORDER_ID = "id";
    private static final String TAG_ORDER_MESSAGE = "ordermessage";
    private static final String TAG_ORDER_TYPE = "ordertype";


    public OrderSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        String CREATE_CHANNEL_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
                + "id TEXT PRIMARY KEY , " + "ordermessage TEXT, "
                + "ordertype TEXT )";

        db.execSQL(CREATE_CHANNEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

        // create fresh table
        this.onCreate(db);
    }

    public void addOrder(OrderObject orderObject) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TAG_ORDER_ID, orderObject.getId());
        values.put(TAG_ORDER_MESSAGE, orderObject.getMessage());
        values.put(TAG_ORDER_TYPE, orderObject.getType());

        db.insert(TABLE_NAME, null, values);

        System.out.println("Added order !");
        db.close();

    }

    public List<OrderObject> getAllOrders() {
        List<OrderObject> allOrders = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        OrderObject orderObject = null;
        if (cursor.moveToFirst()) {
            do {
                orderObject = new OrderObject();
                orderObject.setId(cursor.getString(0));
                orderObject.setMessage(cursor.getString(1));
                orderObject.setType(cursor.getString(2));

                allOrders.add(orderObject);
            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.d("Get all order()", allOrders.toString());

        return allOrders;
    }

}
