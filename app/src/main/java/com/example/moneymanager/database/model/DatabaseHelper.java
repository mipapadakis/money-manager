package com.example.moneymanager.database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TYPE_ONE = "1";
    public static final String TYPE_TWO = "2";
    public static final String TYPE_THREE = "3";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "money_manager_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create payments table
        db.execSQL(Payment.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Payment.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertPayment(String name, String price, String details, String type) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        values.put(Payment.COLUMN_NAME, name);
        values.put(Payment.COLUMN_PRICE, price);
        values.put(Payment.COLUMN_DETAILS, details);
        values.put(Payment.COLUMN_TYPE, type);

        // insert row
        long id = db.insert(Payment.TABLE_NAME, null, values); //TODO

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Payment getPayment(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Payment.TABLE_NAME,
                new String[]{Payment.COLUMN_ID, Payment.COLUMN_NAME, Payment.COLUMN_PRICE, Payment.COLUMN_DETAILS, Payment.COLUMN_TYPE, Payment.COLUMN_TIMESTAMP},
                Payment.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare payment object
        Payment payment = new Payment(
                cursor.getInt(cursor.getColumnIndex(Payment.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_DETAILS)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TYPE)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return payment;
    }

    public List<Payment> getAllPayments(String type) {
        List<Payment> payments = new ArrayList<>();

        // Select All Query
        //String selectQuery = "SELECT  * FROM " + Payment.TABLE_NAME + " ORDER BY " + Payment.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(selectQuery, null);

        Cursor cursor = db.query(Payment.TABLE_NAME,
                new String[]{Payment.COLUMN_ID, Payment.COLUMN_NAME, Payment.COLUMN_PRICE, Payment.COLUMN_DETAILS, Payment.COLUMN_TYPE, Payment.COLUMN_TIMESTAMP},
                Payment.COLUMN_TYPE + "=?", new String[]{type}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setId(cursor.getInt(cursor.getColumnIndex(Payment.COLUMN_ID)));
                payment.setName(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_NAME)));
                payment.setPrice(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_PRICE)));
                payment.setDetails(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_DETAILS)));
                payment.setType(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TYPE)));
                payment.setTimestamp(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

                payments.add(payment);
            } while (cursor.moveToNext());
            cursor.close();
        }


        // close the db connection
        db.close();

        return payments;
    }

    public int getPaymentsCount(String type) {
        //String countQuery = "SELECT  * FROM " + Payment.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Payment.TABLE_NAME,
                new String[]{Payment.COLUMN_ID, Payment.COLUMN_NAME, Payment.COLUMN_PRICE, Payment.COLUMN_DETAILS, Payment.COLUMN_TYPE, Payment.COLUMN_TIMESTAMP},
                Payment.COLUMN_TYPE + "=?", new String[]{type}, null, null, null, null);

        int count = cursor.getCount(); //TODO
        cursor.close();

        // return count
        return count;
    }

    public void updatePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Payment.COLUMN_NAME, payment.getName());
        values.put(Payment.COLUMN_PRICE, payment.getPrice());
        values.put(Payment.COLUMN_DETAILS, payment.getDetails());
        values.put(Payment.COLUMN_TYPE, payment.getType());

        // updating row
        db.update(Payment.TABLE_NAME, values, Payment.COLUMN_ID + " = ?",
                new String[]{String.valueOf(payment.getId())});
        db.close();
    }

    public void deletePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Payment.TABLE_NAME, Payment.COLUMN_ID + " = ?",
                new String[]{String.valueOf(payment.getId())});
        db.close();
    }
}