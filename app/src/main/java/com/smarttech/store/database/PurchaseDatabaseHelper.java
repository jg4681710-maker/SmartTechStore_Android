
package com.smarttech.store.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PurchaseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smarttech_purchases.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PURCHASES = "purchases";
    private static final String TABLE_ITEMS = "purchase_items";

    public PurchaseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPurchases = "CREATE TABLE " + TABLE_PURCHASES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "total REAL NOT NULL)";

        String createItems = "CREATE TABLE " + TABLE_ITEMS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "purchase_id INTEGER NOT NULL, " +
                "product_id INTEGER NOT NULL, " +
                "product_name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "subtotal REAL NOT NULL, " +
                "FOREIGN KEY(purchase_id) REFERENCES purchases(id))";

        db.execSQL(createPurchases);
        db.execSQL(createItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No eliminar compras existentes.
    }

    public long savePurchase(String email, double total) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("date", getCurrentDate());
        values.put("total", total);

        return db.insert(TABLE_PURCHASES, null, values);
    }

    public boolean savePurchaseItem(
            long purchaseId,
            int productId,
            String productName,
            double price,
            int quantity) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("purchase_id", purchaseId);
        values.put("product_id", productId);
        values.put("product_name", productName);
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("subtotal", price * quantity);

        return db.insert(TABLE_ITEMS, null, values) != -1;
    }

    public Cursor getPurchases(String email) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_PURCHASES,
                null,
                "email = ?",
                new String[]{email},
                null,
                null,
                "id DESC"
        );
    }

    public Cursor getPurchaseItems(long purchaseId) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_ITEMS,
                null,
                "purchase_id = ?",
                new String[]{String.valueOf(purchaseId)},
                null,
                null,
                "id ASC"
        );
    }

    public int getPurchaseCount(String email) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_PURCHASES +
                        " WHERE email = ?",
                new String[]{email}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }

    private String getCurrentDate() {

        SimpleDateFormat format = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        );

        return format.format(new Date());
    }
}

