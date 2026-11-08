
package com.smarttech.store;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarttech.store.cart.CartManager;
import com.smarttech.store.models.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends Activity {

    private ImageView imgProduct;
    private TextView txtName;
    private TextView txtDescription;
    private TextView txtPrice;
    private Button btnAddCart;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProductDetail);
        txtName = findViewById(R.id.txtProductName);
        txtDescription = findViewById(R.id.txtProductDescription);
        txtPrice = findViewById(R.id.txtProductPrice);
        btnAddCart = findViewById(R.id.btnAddCartDetail);

        int id = getIntent().getIntExtra("product_id", -1);
        String name = getIntent().getStringExtra("product_name");
        String description = getIntent().getStringExtra("product_description");
        double price = getIntent().getDoubleExtra("product_price", 0);
        int imageResource = getIntent().getIntExtra("product_image", 0);

        product = new Product(
                id,
                name,
                description,
                price,
                imageResource
        );

        txtName.setText(product.getName());
        txtDescription.setText(product.getDescription());

        NumberFormat format =
                NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

        txtPrice.setText(format.format(product.getPrice()));

        if (product.getImageResource() != 0) {
            imgProduct.setImageResource(product.getImageResource());
        }

        btnAddCart.setOnClickListener(v -> {
            CartManager.addProduct(product);

            Toast.makeText(
                    ProductDetailActivity.this,
                    "Producto agregado al carrito",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}
