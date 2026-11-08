
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

        if (email == null || email.trim().isEmpty()) {
            email = "usuario@smarttech.com";
        }

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
                    .append(currency.format(
                            product.getPrice()
                    ))
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

            summary.append(
                    "--------------------------------\n"
            );

            summary.append(
                    "Unidades totales: "
            );

            summary.append(
                    CartManager.getCount()
            );

            tvCheckoutProducts.setText(
                    summary.toString()
            );

            btnConfirmPurchase.setEnabled(true);
        }

        tvCheckoutTotal.setText(
                "Total: " + currency.format(
                        CartManager.getTotal()
                )
        );
    }

    private void confirmPurchase() {

        if (CartManager.getCount() == 0) {

            Toast.makeText(
                    this,
                    "El carrito está vacío",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        products = CartManager.getProducts();

        double total = CartManager.getTotal();

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
                            "No se pudo guardar un producto."
                    );
                }
            }

            db.setTransactionSuccessful();

            success = true;

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Error al guardar la compra: "
                            + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {

            db.endTransaction();
        }

        if (!success) {
            return;
        }

        String confirmation =
                "Compra académica simulada.\n"
                        + "Total: "
                        + currency.format(total)
                        + "\nCompra registrada correctamente.";

        CartManager.clearCart();

        Toast.makeText(
                this,
                confirmation,
                Toast.LENGTH_LONG
        ).show();

        finish();
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
