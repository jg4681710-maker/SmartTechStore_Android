package com.smarttech.store.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarttech.store.R;
import com.smarttech.store.models.Product;
import com.smarttech.store.cart.CartManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends ArrayAdapter<Product> {

    private final Activity context;
    private final ArrayList<Product> products;

    public ProductAdapter(Activity context, ArrayList<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_product, parent, false);
        }

        Product product = products.get(position);

        ImageView image = view.findViewById(R.id.imgProduct);
        TextView name = view.findViewById(R.id.tvProductName);
        TextView description = view.findViewById(R.id.tvProductDescription);
        TextView price = view.findViewById(R.id.tvProductPrice);
        Button addButton = view.findViewById(R.id.btnAddProduct);

        image.setImageResource(product.getImageResource());
        name.setText(product.getName());
        description.setText(product.getDescription());

        NumberFormat currency = NumberFormat.getCurrencyInstance(
                new Locale("es", "CO")
        );

        price.setText(currency.format(product.getPrice()));

        addButton.setOnClickListener(v -> {

            CartManager.addProduct(product);

            Toast.makeText(
                    context,
                    product.getName() + " agregado al carrito",
                    Toast.LENGTH_SHORT
            ).show();

        });

        return view;
    }
}

