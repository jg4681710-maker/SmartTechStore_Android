
package com.smarttech.store.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.smarttech.store.R;
import com.smarttech.store.ProductDetailActivity;
import com.smarttech.store.adapters.ProductAdapter;
import com.smarttech.store.models.Product;

import java.util.ArrayList;

public class CatalogActivity extends Activity {

    private ListView listProducts;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        listProducts = findViewById(R.id.listProducts);

        loadProducts();

        ProductAdapter adapter = new ProductAdapter(this, products);
        listProducts.setAdapter(adapter);

        // Abrir el detalle del producto seleccionado
        listProducts.setOnItemClickListener((parent, view, position, id) -> {

            Product product = products.get(position);

            Intent intent = new Intent(
                    CatalogActivity.this,
                    ProductDetailActivity.class
            );

            intent.putExtra("product_id", product.getId());
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", product.getImageResource());

            startActivity(intent);
        });
    }

    private void loadProducts() {

        products = new ArrayList<>();

        // Producto 1: Audífonos Bluetooth
        products.add(new Product(
                1,
                "Audífonos Bluetooth",
                "Audífonos inalámbricos para música y llamadas.",
                89900,
                R.drawable.headphones
        ));

        // Producto 2: Mouse inalámbrico
        products.add(new Product(
                2,
                "Mouse inalámbrico",
                "Mouse inalámbrico para oficina y estudio.",
                59900,
                R.drawable.wireless_mouse
        ));

        // Producto 3: Teclado mecánico
        products.add(new Product(
                3,
                "Teclado mecánico",
                "Teclado para trabajo y gaming.",
                159900,
                R.drawable.mechanical_keyboard
        ));

        // Producto 4: Parlante Bluetooth
        products.add(new Product(
                4,
                "Parlante Bluetooth",
                "Parlante portátil con conexión Bluetooth.",
                129900,
                R.drawable.bluetooth_speaker
        ));

        // Producto 5: Cargador USB
        products.add(new Product(
                5,
                "Cargador USB",
                "Cargador para dispositivos compatibles.",
                39900,
                R.drawable.usb_charger
        ));
    }
}
