package com.smarttech.store.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.smarttech.store.R;
import com.smarttech.store.ProductDetailActivity;
import com.smarttech.store.adapters.ProductAdapter;
import com.smarttech.store.data.ProductStore;
import com.smarttech.store.models.Product;

import java.util.ArrayList;

public class CatalogActivity extends Activity {

    private ListView listProducts;
    private ArrayList<Product> products;
    private ProductAdapter adapter;
    private ProductStore productStore;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        listProducts = findViewById(R.id.listProducts);

        productStore = new ProductStore();

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        adapter = new ProductAdapter(
                this,
                new ArrayList<>()
        );

        listProducts.setAdapter(adapter);

        listProducts.setOnItemClickListener(
                (parent, view, position, id) -> {

                    if (products == null
                            || position < 0
                            || position >= products.size()) {
                        return;
                    }

                    Product product = products.get(position);

                    Intent intent = new Intent(
                            CatalogActivity.this,
                            ProductDetailActivity.class
                    );

                    intent.putExtra("product_id", product.getId());

                    intent.putExtra(
                            "product_document_id",
                            product.getDocumentId()
                    );

                    intent.putExtra(
                            "product_name",
                            product.getName()
                    );

                    intent.putExtra(
                            "product_description",
                            product.getDescription()
                    );

                    intent.putExtra(
                            "product_price",
                            product.getPrice()
                    );

                    intent.putExtra(
                            "product_image",
                            product.getImageResource()
                    );

                    intent.putExtra("USER_EMAIL", userEmail);

                    startActivity(intent);
                }
        );
    }

    // Consulta los productos activos de Firestore.
    private void loadProducts() {

        productStore.getAll(new ProductStore.ProductsCallback() {

            @Override
            public void onSuccess(ArrayList<Product> result) {

                if (isFinishing() || isDestroyed()) {
                    return;
                }

                products = result;

                adapter.clear();
                adapter.addAll(products);
                adapter.notifyDataSetChanged();

                if (products.isEmpty()) {

                    Toast.makeText(
                            CatalogActivity.this,
                            "No hay productos disponibles en el catálogo.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String message) {

                if (isFinishing() || isDestroyed()) {
                    return;
                }

                Toast.makeText(
                        CatalogActivity.this,
                        message,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (productStore != null && adapter != null) {
            loadProducts();
        }
    }
}
