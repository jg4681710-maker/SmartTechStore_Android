
package com.smarttech.store.buyer;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
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

    private NumberFormat currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        module = getIntent().getStringExtra("MODULE_NAME");
        email = getIntent().getStringExtra("USER_EMAIL");

        if (module == null || module.trim().isEmpty()) {
            module = "Módulo del comprador";
        }

        if (email == null || email.trim().isEmpty()) {
            email = "No disponible";
        }

        currency = NumberFormat.getCurrencyInstance(
                new Locale("es", "CO")
        );

        databaseHelper = new PurchaseDatabaseHelper(this);

        createInterface();
    }

    // CREAR INTERFAZ
    private void createInterface() {

        ScrollView scrollView = new ScrollView(this);

        container = new LinearLayout(this);

        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(24, 24, 24, 30);
        container.setBackgroundColor(
                Color.rgb(245, 247, 250)
        );

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

    // ENCABEZADO
    private void createHeader() {

        TextView title = new TextView(this);

        title.setText(module);
        title.setTextSize(25);
        title.setTextColor(Color.rgb(20, 45, 75));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 16);

        container.addView(title);

        TextView subtitle = new TextView(this);

        subtitle.setText("SMARTTECH STORE");
        subtitle.setTextSize(15);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 0, 0, 20);

        container.addView(subtitle);
    }

    // PERFIL DEL COMPRADOR
    private void showProfile() {

        addSection("Información personal");

        addInfo("Correo electrónico", email);
        addInfo("Rol", "Comprador");
        addInfo("Estado de sesión", "Información de cuenta");

        addSection("Resumen de actividad");

        addInfo(
                "Productos en carrito",
                String.valueOf(CartManager.getCount())
        );

        addInfo(
                "Productos diferentes",
                String.valueOf(
                        CartManager.getDistinctProductCount()
                )
        );

        addInfo(
                "Total del carrito",
                formatCurrency(CartManager.getTotal())
        );

        int purchases = databaseHelper.getPurchaseCount(email);

        addInfo(
                "Compras registradas",
                String.valueOf(purchases)
        );

        addSection("Información de la aplicación");

        addInfo(
                "Plataforma",
                "SmartTech Store Android"
        );

        addInfo(
                "Almacenamiento de compras",
                "Base de datos local SQLite"
        );

        addInfo(
                "Tipo de aplicación",
                "Prototipo académico"
        );
    }

    // HISTORIAL DE COMPRAS
    private void showPurchaseHistory() {

        addSection("Historial de compras");

        int purchaseCount =
                databaseHelper.getPurchaseCount(email);

        addInfo(
                "Total de compras",
                String.valueOf(purchaseCount)
        );

        if (purchaseCount == 0) {

            showEmptyHistory();

        } else {

            addInfo(
                    "Estado",
                    "Registros almacenados localmente."
            );

            Cursor purchases =
                    databaseHelper.getPurchases(email);

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

                        showPurchaseCard(
                                purchaseId,
                                date,
                                total
                        );
                    }

                } catch (Exception error) {

                    addInfo(
                            "Error",
                            "No fue posible mostrar todos los registros."
                    );

                } finally {

                    purchases.close();
                }

            } else {

                addInfo(
                        "Información",
                        "No fue posible consultar los registros."
                );
            }
        }

        showCurrentCart();
    }

    // TARJETA DE CADA COMPRA
    private void showPurchaseCard(
            long purchaseId,
            String date,
            double total
    ) {

        addSection("Compra #" + purchaseId);

        addInfo(
                "Fecha de compra",
                date == null ? "No disponible" : date
        );

        addInfo(
                "Total de compra",
                formatCurrency(total)
        );

        addSection("Productos de la compra");

        showPurchaseItems(purchaseId);
    }

    // PRODUCTOS DE CADA COMPRA
    private void showPurchaseItems(long purchaseId) {

        Cursor items =
                databaseHelper.getPurchaseItems(purchaseId);

        if (items == null) {

            addInfo(
                    "Productos",
                    "No fue posible consultar los productos."
            );

            return;
        }

        boolean hasItems = false;

        try {

            while (items.moveToNext()) {

                hasItems = true;

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

                addInfo(
                        "Producto",
                        name == null ? "Sin nombre" : name
                );

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

        } catch (Exception error) {

            addInfo(
                    "Información",
                    "No se pudieron leer todos los productos."
            );

        } finally {

            items.close();
        }

        if (!hasItems) {

            addInfo(
                    "Productos",
                    "Esta compra no tiene productos registrados."
            );
        }
    }

    // HISTORIAL VACÍO
    private void showEmptyHistory() {

        addSection("Sin compras registradas");

        addInfo(
                "Información",
                "Todavía no existen compras registradas para esta cuenta."
        );

        addInfo(
                "Siguiente paso",
                "Explora el catálogo, agrega productos al carrito "
                        + "y realiza una compra académica simulada."
        );
    }

    // RESUMEN DEL CARRITO
    private void showCurrentCart() {

        addSection("Carrito actual");

        addInfo(
                "Unidades",
                String.valueOf(CartManager.getCount())
        );

        addInfo(
                "Productos diferentes",
                String.valueOf(
                        CartManager.getDistinctProductCount()
                )
        );

        addInfo(
                "Total actual",
                formatCurrency(CartManager.getTotal())
        );
    }

    // INFORMACIÓN GENERAL
    private void showInformation() {

        addSection("Información del módulo");

        addInfo("Usuario", email);

        addInfo(
                "Descripción",
                "Módulo de información del comprador."
        );

        addInfo(
                "Aplicación",
                "SmartTech Store Android"
        );
    }

    // CREAR TÍTULOS DE SECCIÓN
    private void addSection(String text) {

        TextView title = new TextView(this);

        title.setText(text);
        title.setTextSize(20);
        title.setTextColor(Color.rgb(25, 90, 140));
        title.setPadding(0, 24, 0, 10);

        container.addView(title);
    }

    // CREAR TARJETAS INFORMATIVAS
    private void addInfo(String title, String value) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 14, 18, 14);
        card.setBackgroundColor(Color.WHITE);
        card.setElevation(2);

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
        content.setTextSize(16);
        content.setTextColor(Color.rgb(30, 40, 50));
        content.setPadding(0, 5, 0, 0);

        card.addView(label);
        card.addView(content);

        container.addView(card);
    }

    // FORMATO MONETARIO COLOMBIANO
    private String formatCurrency(double amount) {

        return currency.format(amount);
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
