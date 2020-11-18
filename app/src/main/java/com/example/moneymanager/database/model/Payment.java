package com.example.moneymanager.database.model;

public class Payment {
    public static final String TABLE_NAME = "payments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DETAILS = "details";
    //<COLUMN_TYPE> takes values <DatabaseHelper.TYPE_ONE | DatabaseHelper.TYPE_TWO | DatabaseHelper.TYPE_THREE>.
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    private int id;
    private String name;
    private String price;
    private String details;
    private String type;
    private String timestamp;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PRICE + " TEXT,"
                    + COLUMN_DETAILS + " TEXT,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";


    public Payment(int id,  String name, String price, String details, String type, String timestamp) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.type = type;
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
