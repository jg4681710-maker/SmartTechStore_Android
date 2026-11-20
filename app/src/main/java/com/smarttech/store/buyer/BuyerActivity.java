
package com.smarttech.store.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.smarttech.store.MainActivity;
import com.smarttech.store.R;

public class BuyerActivity extends Activity {

    private TextView tvWelcome;

    private Button btnCatalog;
    private Button btnCart;
    private Button btnProfile;
    private Button btnOrders;

    private String userEmail;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        tvWelcome = findViewById(R.id.tvBuyerWelcome);
        btnCatalog = findViewById(R.id.btnCatalog);
        btnCart = findViewById(R.id.btnCart);
        btnProfile = findViewById(R.id.btnBuyerProfile);
        btnOrders = findViewById(R.id.btnBuyerOrders);

        // Obtener el usuario autenticado desde Firebase.
        FirebaseUser currentUser =
                FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            returnToLogin(
                    "La sesión no está activa. Inicia sesión nuevamente."
            );
            return;
        }

        userEmail = currentUser.getEmail();
        userUid = currentUser.getUid();

        if (userEmail == null || userEmail.trim().isEmpty()) {
            returnToLogin(
                    "No se pudo identificar la cuenta del comprador."
            );
            return;
        }

        tvWelcome.setText("Hola, " + userEmail);

        configureNavigation();
    }

    // Configurar la navegación del módulo comprador.
    private void configureNavigation() {

        btnCatalog.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BuyerActivity.this,
                    CatalogActivity.class
            );

            addUserData(intent);
            startActivity(intent);
        });

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(
                    BuyerActivity.this,
                    CartActivity.class
            );

            addUserData(intent);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v ->
                openModule("Perfil del comprador")
        );

        btnOrders.setOnClickListener(v ->
                openModule("Historial de compras")
        );
    }

    // Abrir las secciones del perfil y del historial.
    private void openModule(String module) {
        Intent intent = new Intent(
                BuyerActivity.this,
                BuyerModuleActivity.class
        );

        intent.putExtra("MODULE_NAME", module);

        addUserData(intent);
        startActivity(intent);
    }

    // Compartir los datos de sesión entre pantallas.
    private void addUserData(Intent intent) {
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("USER_UID", userUid);
        intent.putExtra("USER_ROLE", "COMPRADOR");
    }

    // Cerrar la sesión y regresar a la pantalla de acceso.
    private void returnToLogin(String message) {
        FirebaseAuth.getInstance().signOut();

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();

        Intent intent = new Intent(
                BuyerActivity.this,
                MainActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    // Comprobar que la sesión continúe activa.
    @Override
    protected void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser() == null
                && userEmail != null) {
            returnToLogin(
                    "Tu sesión ha finalizado. Inicia sesión nuevamente."
            );
        }
    }
}
