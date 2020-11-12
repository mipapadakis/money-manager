package com.example.moneymanager;

public class Payment {
    public static final String TABLE_NAME = "payments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    private int id;
    private String product;
    private String timestamp;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PRODUCT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";


    public Payment(int id, String product, String timestamp) {
        this.id = id;
        this.product = product;
        this.timestamp = timestamp;
    }

    public Payment() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }
    public void setProduct(String note) {
        this.product = product;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
