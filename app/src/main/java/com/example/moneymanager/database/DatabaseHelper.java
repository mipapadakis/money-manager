package com.example.moneymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moneymanager.database.model.Payment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    private final int type;
    private final String tableName;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "money_manager_db";
    private static final String ORDER = " DESC"; // " ASC"

    public DatabaseHelper(Context context, int type) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.type = type;
        if(type == TYPE_ONE) tableName = Payment.TABLE_ONE_NAME;
        else if(type == TYPE_TWO) tableName = Payment.TABLE_TWO_NAME;
        else tableName = Payment.TABLE_THREE_NAME;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL(Payment.CREATE_TABLE_ONE);
        db.execSQL(Payment.CREATE_TABLE_TWO);
        db.execSQL(Payment.CREATE_TABLE_THREE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + tableName);

        // Create tables again
        onCreate(db);
    }

    public long insertPayment(String name, String price, String details) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` will be inserted automatically.
        values.put(Payment.COLUMN_NAME, name);
        values.put(Payment.COLUMN_PRICE, price);
        values.put(Payment.COLUMN_DETAILS, details);
        values.put(Payment.COLUMN_TIMESTAMP, DateFormat.getDateTimeInstance().format(new Date()));

        // insert row
        long id = db.insert(tableName, null, values);//TODO

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Payment getPayment(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableName,
                new String[]{Payment.COLUMN_ID, Payment.COLUMN_NAME, Payment.COLUMN_PRICE, Payment.COLUMN_DETAILS, Payment.COLUMN_TIMESTAMP},
                Payment.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare payment object
        Payment payment = new Payment(
                cursor.getInt(cursor.getColumnIndex(Payment.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_PRICE)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_DETAILS)),
                cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return payment;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

//        Cursor cursor = db.query(tableName,
//                new String[]{Payment.COLUMN_ID, Payment.COLUMN_NAME, Payment.COLUMN_PRICE, Payment.COLUMN_DETAILS, Payment.COLUMN_TYPE, Payment.COLUMN_TIMESTAMP},
//                Payment.COLUMN_TYPE + "=?", new String[]{type}, null, null, null, null);

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tableName + " ORDER BY " + Payment.COLUMN_ID + ORDER;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setId(cursor.getInt(cursor.getColumnIndex(Payment.COLUMN_ID)));
                payment.setName(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_NAME)));
                payment.setPrice(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_PRICE)));
                payment.setDetails(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_DETAILS)));
                payment.setTimestamp(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

                payments.add(payment);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // close the db connection
        db.close();

        return payments;
    }

    public int getPaymentsCount() {
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(tableName,
//                new String[]{Payment.COLUMN_ID, Payment.COLUMN_NAME, Payment.COLUMN_PRICE, Payment.COLUMN_DETAILS, Payment.COLUMN_TYPE, Payment.COLUMN_TIMESTAMP},
//                Payment.COLUMN_TYPE + "=?", new String[]{type}, null, null, null, null);

        Cursor cursor = db.rawQuery("SELECT  * FROM " + tableName, null);
        int count = cursor.getCount();
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

        // updating row
        db.update(tableName, values, Payment.COLUMN_ID + " = ?",
                new String[]{String.valueOf(payment.getId())});
        db.close();
    }

    public void deletePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, Payment.COLUMN_ID + " = ?",
                new String[]{String.valueOf(payment.getId())});
        db.close();
    }

    public List<Payment> searchPaymentByName(String substring) {
        // get readable database as we are not inserting anything
        List<Payment> payments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName+
                " WHERE "+Payment.COLUMN_NAME+" LIKE '%"+ substring +"%'"
                + " ORDER BY " + Payment.COLUMN_ID + ORDER, null);

        // looping through all rows and adding to list
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setId(cursor.getInt(cursor.getColumnIndex(Payment.COLUMN_ID)));
                payment.setName(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_NAME)));
                payment.setPrice(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_PRICE)));
                payment.setDetails(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_DETAILS)));
                payment.setTimestamp(cursor.getString(cursor.getColumnIndex(Payment.COLUMN_TIMESTAMP)));

                payments.add(payment);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return payments;
    }
}