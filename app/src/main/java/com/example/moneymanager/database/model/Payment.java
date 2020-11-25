package com.example.moneymanager.database.model;

public class Payment {
    public static final String TABLE_ONE_NAME = "expenses";
    public static final String TABLE_TWO_NAME = "i_owe";
    public static final String TABLE_THREE_NAME = "owe_me";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DETAILS = "details";
    //<COLUMN_TYPE> takes values <DatabaseHelper.TYPE_ONE | DatabaseHelper.TYPE_TWO | DatabaseHelper.TYPE_THREE>.
    public static final String COLUMN_TIMESTAMP = "timestamp";
    private int id;
    private String name;
    private String price;
    private String details;
    private String timestamp;

    // Create table SQL query
    public static final String CREATE_TABLE_ONE =
            "CREATE TABLE " + TABLE_ONE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_DETAILS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_TABLE_TWO =
            "CREATE TABLE " + TABLE_TWO_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_DETAILS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_TABLE_THREE =
            "CREATE TABLE " + TABLE_THREE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_DETAILS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";


    public Payment(int id,  String name, String price, String details, String timestamp) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.timestamp = timestamp;
    }

    public Payment() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice(){ return price; }
    public void setPrice(String price) { this.price = price; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
