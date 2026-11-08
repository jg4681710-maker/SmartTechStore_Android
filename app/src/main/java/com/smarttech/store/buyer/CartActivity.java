
package com.smarttech.store.buyer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarttech.store.R;
import com.smarttech.store.CheckoutActivity;
import com.smarttech.store.cart.CartManager;
import com.smarttech.store.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends Activity {

    private ListView listCart;
    private TextView tvCartCount;
    private TextView tvCartTotal;
    private Button btnClearCart;
    private Button btnCheckout;

    private ArrayList<Product> products;
    private ArrayAdapter<String> adapter;

    private NumberFormat currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        currency = NumberFormat.getCurrencyInstance(
                new Locale("es", "CO")
        );

        listCart = findViewById(R.id.listCart);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnClearCart = findViewById(R.id.btnClearCart);
        btnCheckout = findViewById(R.id.btnCheckout);

        configureCartActions();

        loadCart();
    }

    private void configureCartActions() {

        btnCheckout.setOnClickListener(view -> {

            if (CartManager.getCount() == 0) {

                Toast.makeText(
                        this,
                        "Agrega productos antes de continuar",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent = new Intent(
                    CartActivity.this,
                    CheckoutActivity.class
            );

            startActivity(intent);
        });

        btnClearCart.setOnClickListener(view -> {

            CartManager.clearCart();

            loadCart();

            Toast.makeText(
                    this,
                    "Carrito vacío",
                    Toast.LENGTH_SHORT
            ).show();
        });

        listCart.setOnItemClickListener(
                (parent, view, position, id) -> {

                    if (position < 0 || position >= products.size()) {
                        return;
                    }

                    Product product = products.get(position);

                    CartManager.increaseQuantity(
                            product.getId()
                    );

                    loadCart();

                    Toast.makeText(
                            this,
                            "Cantidad actualizada",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        listCart.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    if (position < 0 || position >= products.size()) {
                        return true;
                    }

                    Product product = products.get(position);

                    CartManager.removeProductById(
                            product.getId()
                    );

                    loadCart();

                    Toast.makeText(
                            this,
                            "Producto eliminado",
                            Toast.LENGTH_SHORT
                    ).show();

                    return true;
                }
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadCart();
    }

    private void loadCart() {

        products = CartManager.getProducts();

        ArrayList<String> productNames = new ArrayList<>();

        for (Product product : products) {

            int quantity = CartManager.getQuantity(
                    product.getId()
            );

            double subtotal = product.getPrice() * quantity;

            String itemText =
                    product.getName()
                            + "\nPrecio unitario: "
                            + currency.format(product.getPrice())
                            + "\nCantidad: "
                            + quantity
                            + "\nSubtotal: "
                            + currency.format(subtotal)
                            + "\nToca para aumentar cantidad";

            productNames.add(itemText);
        }

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productNames
        );

        listCart.setAdapter(adapter);

        tvCartCount.setText(
                "Unidades: " + CartManager.getCount()
                        + " | Productos diferentes: "
                        + CartManager.getDistinctProductCount()
        );

        tvCartTotal.setText(
                "Total: " + currency.format(
                        CartManager.getTotal()
                )
        );

        boolean empty = products.isEmpty();

        btnCheckout.setEnabled(!empty);
        btnClearCart.setEnabled(!empty);
    }
}
