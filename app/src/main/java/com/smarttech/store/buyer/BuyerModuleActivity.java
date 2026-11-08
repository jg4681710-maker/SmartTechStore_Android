
package com.smarttech.store.buyer;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smarttech.store.cart.CartManager;
import com.smarttech.store.database.PurchaseDatabaseHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class BuyerModuleActivity extends Activity {

    private LinearLayout container;
    private String module;
    private String email;

    private PurchaseDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        module = getIntent().getStringExtra("MODULE_NAME");
        email = getIntent().getStringExtra("USER_EMAIL");

        if (module == null) {
            module = "Módulo del comprador";
        }

        if (email == null) {
            email = "usuario@smarttech.com";
        }

        databaseHelper = new PurchaseDatabaseHelper(this);

        ScrollView scrollView = new ScrollView(this);

        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(24, 24, 24, 24);
        container.setBackgroundColor(Color.rgb(245, 247, 250));

        scrollView.addView(container);

        setContentView(scrollView);

        createHeader();

        if (module.equalsIgnoreCase("Perfil del comprador")) {
            showProfile();

        } else if (module.equalsIgnoreCase("Historial de compras")) {
            showPurchaseHistory();

        } else {
            showInformation();
        }
    }

    private void createHeader() {

        TextView title = new TextView(this);

        title.setText(module);
        title.setTextSize(25);
        title.setTextColor(Color.rgb(20, 45, 75));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 20);

        container.addView(title);

        TextView subtitle = new TextView(this);

        subtitle.setText("SmartTech Store");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.DKGRAY);

        container.addView(subtitle);
    }

    private void showProfile() {

        addSection("Información personal");

        addInfo("Correo electrónico", email);
        addInfo("Rol", "Comprador");
        addInfo("Estado de cuenta", "Activo");

        addInfo(
                "Productos en carrito",
                String.valueOf(CartManager.getCount())
        );

        addSection("Resumen");

        addInfo(
                "Total del carrito",
                formatCurrency(CartManager.getTotal())
        );

        addInfo(
                "Compras registradas",
                String.valueOf(databaseHelper.getPurchaseCount(email))
        );

        addInfo(
                "Tipo de aplicación",
                "Prototipo académico Android"
        );
    }

    private void showPurchaseHistory() {

        addSection("Historial de compras");

        int purchaseCount = databaseHelper.getPurchaseCount(email);

        addInfo(
                "Compras registradas",
                String.valueOf(purchaseCount)
        );

        if (purchaseCount == 0) {

            addInfo(
                    "Estado",
                    "No existen compras confirmadas."
            );

        } else {

            addInfo(
                    "Estado",
                    "Compras confirmadas y guardadas localmente."
            );

            Cursor purchases = databaseHelper.getPurchases(email);

            if (purchases != null) {

                try {

                    while (purchases.moveToNext()) {

                        long purchaseId = purchases.getLong(
                                purchases.getColumnIndexOrThrow("id")
                        );

                        String date = purchases.getString(
                                purchases.getColumnIndexOrThrow("date")
                        );

                        double total = purchases.getDouble(
                                purchases.getColumnIndexOrThrow("total")
                        );

                        addSection("Compra #" + purchaseId);

                        addInfo("Fecha", date);

                        addInfo(
                                "Total de compra",
                                formatCurrency(total)
                        );

                        showPurchaseItems(purchaseId);
                    }

                } finally {

                    purchases.close();
                }
            }
        }

        addSection("Carrito actual");

        addInfo(
                "Productos agregados",
                String.valueOf(CartManager.getCount())
        );

        addInfo(
                "Total actual",
                formatCurrency(CartManager.getTotal())
        );
    }

    private void showPurchaseItems(long purchaseId) {

        Cursor items = databaseHelper.getPurchaseItems(purchaseId);

        if (items == null) {
            return;
        }

        try {

            while (items.moveToNext()) {

                String name = items.getString(
                        items.getColumnIndexOrThrow("product_name")
                );

                double price = items.getDouble(
                        items.getColumnIndexOrThrow("price")
                );

                int quantity = items.getInt(
                        items.getColumnIndexOrThrow("quantity")
                );

                double subtotal = items.getDouble(
                        items.getColumnIndexOrThrow("subtotal")
                );

                addInfo("Producto", name);

                addInfo(
                        "Precio unitario",
                        formatCurrency(price)
                );

                addInfo(
                        "Cantidad",
                        String.valueOf(quantity)
                );

                addInfo(
                        "Subtotal",
                        formatCurrency(subtotal)
                );
            }

        } finally {

            items.close();
        }
    }

    private void showInformation() {

        addSection("Información del módulo");

        addInfo("Usuario", email);

        addInfo(
                "Descripción",
                "Módulo de información del comprador."
        );

        addInfo("Estado", "Disponible");
    }

    private void addSection(String text) {

        TextView title = new TextView(this);

        title.setText(text);
        title.setTextSize(20);
        title.setTextColor(Color.rgb(25, 90, 140));
        title.setPadding(0, 24, 0, 10);

        container.addView(title);
    }

    private void addInfo(String title, String value) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 14, 18, 14);
        card.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 0, 0, 12);

        card.setLayoutParams(params);

        TextView label = new TextView(this);

        label.setText(title);
        label.setTextSize(14);
        label.setTextColor(Color.GRAY);

        TextView content = new TextView(this);

        content.setText(value);
        content.setTextSize(17);
        content.setTextColor(Color.rgb(30, 40, 50));
        content.setPadding(0, 5, 0, 0);

        card.addView(label);
        card.addView(content);

        container.addView(card);
    }

    private String formatCurrency(double amount) {

        NumberFormat format =
                NumberFormat.getCurrencyInstance(
                        new Locale("es", "CO")
                );

        return format.format(amount);
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}




