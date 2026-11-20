
package com.smarttech.store;

import android.app.Activity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smarttech.store.cart.CartManager;
import com.smarttech.store.database.PurchaseDatabaseHelper;
import com.smarttech.store.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CheckoutActivity extends Activity {

    private TextView tvCheckoutProducts;
    private TextView tvCheckoutTotal;
    private Button btnConfirmPurchase;

    private ArrayList<Product> products;

    private NumberFormat currency;

    private PurchaseDatabaseHelper databaseHelper;

    private String email;

    private boolean purchaseInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);

        tvCheckoutProducts = findViewById(
                R.id.tvCheckoutProducts
        );

        tvCheckoutTotal = findViewById(
                R.id.tvCheckoutTotal
        );

        btnConfirmPurchase = findViewById(
                R.id.btnConfirmPurchase
        );

        currency = NumberFormat.getCurrencyInstance(
                new Locale("es", "CO")
        );

        email = getIntent().getStringExtra("USER_EMAIL");

        databaseHelper = new PurchaseDatabaseHelper(this);

        loadCheckout();

        btnConfirmPurchase.setOnClickListener(
                view -> confirmPurchase()
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadCheckout();
    }

    // CARGAR RESUMEN DE LA COMPRA
    private void loadCheckout() {

        products = CartManager.getProducts();

        StringBuilder summary = new StringBuilder();

        for (Product product : products) {

            int quantity = CartManager.getQuantity(
                    product.getId()
            );

            double subtotal =
                    product.getPrice() * quantity;

            summary.append(product.getName())
                    .append("\n")
                    .append("Precio unitario: ")
                    .append(currency.format(product.getPrice()))
                    .append("\n")
                    .append("Cantidad: ")
                    .append(quantity)
                    .append("\n")
                    .append("Subtotal: ")
                    .append(currency.format(subtotal))
                    .append("\n\n");
        }

        if (products.isEmpty()) {

            tvCheckoutProducts.setText(
                    "No hay productos en el carrito."
            );

            btnConfirmPurchase.setEnabled(false);

        } else {

            summary.append("------------------------------\n");

            summary.append("Unidades totales: ")
                    .append(CartManager.getCount());

            tvCheckoutProducts.setText(summary.toString());

            btnConfirmPurchase.setEnabled(
                    !purchaseInProgress
            );
        }

        tvCheckoutTotal.setText(
                "Total: " + currency.format(
                        CartManager.getTotal()
                )
        );
    }

    // CONFIRMAR COMPRA ACADÉMICA
    private void confirmPurchase() {

        if (purchaseInProgress) {
            return;
        }

        if (email == null || email.trim().isEmpty()) {

            showMessage(
                    "No se identificó el usuario de la compra."
            );

            return;
        }

        if (CartManager.getCount() <= 0) {

            showMessage(
                    "El carrito está vacío."
            );

            loadCheckout();

            return;
        }

        products = CartManager.getProducts();

        double total = CartManager.getTotal();

        if (products.isEmpty()
                || !Double.isFinite(total)
                || total <= 0) {

            showMessage(
                    "No se puede procesar una compra con datos inválidos."
            );

            return;
        }

        for (Product product : products) {

            int quantity = CartManager.getQuantity(
                    product.getId()
            );

            if (product.getName() == null
                    || product.getName().trim().isEmpty()
                    || !Double.isFinite(product.getPrice())
                    || product.getPrice() <= 0
                    || quantity <= 0) {

                showMessage(
                        "Se encontró un producto con información inválida."
                );

                return;
            }
        }

        purchaseInProgress = true;
        btnConfirmPurchase.setEnabled(false);

        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();

        boolean success = false;

        db.beginTransaction();

        try {

            long purchaseId = databaseHelper.savePurchase(
                    email,
                    total
            );

            if (purchaseId == -1) {

                throw new Exception(
                        "No se pudo registrar la compra."
                );
            }

            for (Product product : products) {

                int quantity = CartManager.getQuantity(
                        product.getId()
                );

                boolean itemSaved =
                        databaseHelper.savePurchaseItem(
                                purchaseId,
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                quantity
                        );

                if (!itemSaved) {

                    throw new Exception(
                            "No se pudo guardar el producto: "
                                    + product.getName()
                    );
                }
            }

            db.setTransactionSuccessful();

            success = true;

        } catch (Exception error) {

            showMessage(
                    "Error al registrar la compra: "
                            + error.getMessage()
            );

        } finally {

            db.endTransaction();
        }

        if (!success) {

            purchaseInProgress = false;
            btnConfirmPurchase.setEnabled(true);

            return;
        }

        CartManager.clearCart();

        showMessage(
                "Compra académica simulada.\n"
                        + "Total: "
                        + currency.format(total)
                        + "\nRegistro guardado correctamente."
        );

        finish();
    }

    private void showMessage(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
