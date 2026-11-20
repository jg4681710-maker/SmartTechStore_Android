
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

    private static final String DATABASE_NAME =
            "smarttech_purchases.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PURCHASES =
            "purchases";

    private static final String TABLE_ITEMS =
            "purchase_items";

    public PurchaseDatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // CONFIGURACIÓN DE LA BASE DE DATOS
    @Override
    public void onConfigure(SQLiteDatabase db) {

        super.onConfigure(db);

        // Activa la comprobación de claves foráneas.
        db.setForeignKeyConstraintsEnabled(true);
    }

    // CREACIÓN DE TABLAS
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPurchases =
                "CREATE TABLE " + TABLE_PURCHASES + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "email TEXT NOT NULL, " +
                        "date TEXT NOT NULL, " +
                        "total REAL NOT NULL CHECK(total > 0)" +
                        ")";

        String createItems =
                "CREATE TABLE " + TABLE_ITEMS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "purchase_id INTEGER NOT NULL, " +
                        "product_id INTEGER NOT NULL, " +
                        "product_name TEXT NOT NULL, " +
                        "price REAL NOT NULL CHECK(price > 0), " +
                        "quantity INTEGER NOT NULL CHECK(quantity > 0), " +
                        "subtotal REAL NOT NULL CHECK(subtotal > 0), " +
                        "FOREIGN KEY(purchase_id) " +
                        "REFERENCES purchases(id) ON DELETE CASCADE" +
                        ")";

        db.execSQL(createPurchases);
        db.execSQL(createItems);
    }

    // ACTUALIZACIÓN DE LA BASE DE DATOS
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // Se conservan los registros existentes.
        // No se eliminan tablas ni compras anteriores.
    }

    // REGISTRAR COMPRA
    public long savePurchase(
            String email,
            double total
    ) {

        if (email == null
                || email.trim().isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email.trim())
                .matches()
                || !Double.isFinite(total)
                || total <= 0) {

            return -1;
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("email", email.trim());
        values.put("date", getCurrentDate());
        values.put("total", total);

        return db.insert(
                TABLE_PURCHASES,
                null,
                values
        );
    }

    // REGISTRAR PRODUCTO DE UNA COMPRA
    public boolean savePurchaseItem(
            long purchaseId,
            int productId,
            String productName,
            double price,
            int quantity
    ) {

        if (purchaseId <= 0
                || productName == null
                || productName.trim().isEmpty()
                || !Double.isFinite(price)
                || price <= 0
                || quantity <= 0) {

            return false;
        }

        double subtotal = price * quantity;

        if (!Double.isFinite(subtotal) || subtotal <= 0) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("purchase_id", purchaseId);
        values.put("product_id", productId);
        values.put("product_name", productName.trim());
        values.put("price", price);
        values.put("quantity", quantity);
        values.put("subtotal", subtotal);

        return db.insert(
                TABLE_ITEMS,
                null,
                values
        ) != -1;
    }

    // CONSULTAR COMPRAS DE UN USUARIO
    public Cursor getPurchases(String email) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_PURCHASES,
                null,
                "email = ?",
                new String[]{email.trim()},
                null,
                null,
                "id DESC"
        );
    }

    // CONSULTAR PRODUCTOS DE UNA COMPRA
    public Cursor getPurchaseItems(long purchaseId) {

        if (purchaseId <= 0) {
            return null;
        }

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_ITEMS,
                null,
                "purchase_id = ?",
                new String[]{
                        String.valueOf(purchaseId)
                },
                null,
                null,
                "id ASC"
        );
    }

    // CONTAR COMPRAS DE UN USUARIO
    public int getPurchaseCount(String email) {

        if (email == null || email.trim().isEmpty()) {
            return 0;
        }

        SQLiteDatabase db = getReadableDatabase();

        int count = 0;

        try (
                Cursor cursor = db.rawQuery(
                        "SELECT COUNT(*) FROM "
                                + TABLE_PURCHASES
                                + " WHERE email = ?",
                        new String[]{email.trim()}
                )
        ) {

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }

        return count;
    }

    // OBTENER FECHA ACTUAL
    private String getCurrentDate() {

        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                );

        return format.format(new Date());
    }
}
