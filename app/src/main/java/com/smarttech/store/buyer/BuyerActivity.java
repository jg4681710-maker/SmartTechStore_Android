
package com.smarttech.store.buyer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.smarttech.store.R;

public class BuyerActivity extends Activity {

    private TextView tvWelcome;
    private Button btnCatalog;
    private Button btnCart;
    private Button btnProfile;
    private Button btnOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        tvWelcome = findViewById(R.id.tvBuyerWelcome);
        btnCatalog = findViewById(R.id.btnCatalog);
        btnCart = findViewById(R.id.btnCart);
        btnProfile = findViewById(R.id.btnBuyerProfile);
        btnOrders = findViewById(R.id.btnBuyerOrders);

        String email = getIntent().getStringExtra("USER_EMAIL");

        if (email != null) {
            tvWelcome.setText("Hola, " + email);
        }

        btnCatalog.setOnClickListener(v -> {

            Intent intent = new Intent(
                    BuyerActivity.this,
                    CatalogActivity.class
            );

            startActivity(intent);

        });

        btnCart.setOnClickListener(v -> {

            Intent intent = new Intent(
                    BuyerActivity.this,
                    CartActivity.class
            );

            startActivity(intent);

        });

        btnProfile.setOnClickListener(v ->
                openModule("Perfil del comprador")
        );

        btnOrders.setOnClickListener(v ->
                openModule("Historial de compras")
        );
    }

    private void openModule(String module) {

        Intent intent = new Intent(
                BuyerActivity.this,
                BuyerModuleActivity.class
        );

        intent.putExtra("MODULE_NAME", module);

        startActivity(intent);
    }
}

