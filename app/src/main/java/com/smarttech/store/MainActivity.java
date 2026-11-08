package com.smarttech.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText etEmail;
    private EditText etPassword;
    private Spinner spinnerRole;
    private Button btnLogin;
    private TextView tvForgotPassword;

    private final String[] roles = {
            "Comprador",
            "Vendedor",
            "Administrador"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerRole.setAdapter(adapter);

        btnLogin.setOnClickListener(v -> login());

        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Recuperación académica: contacta al administrador para restablecer la contraseña.",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void login() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (email.isEmpty()) {
            etEmail.setError("Ingresa tu correo electrónico");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa tu contraseña");
            etPassword.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingresa un correo válido");
            etEmail.requestFocus();
            return;
        }


        Intent intent;

        if (role.equals("Comprador")) {

            intent = new Intent(
                    MainActivity.this,
                    com.smarttech.store.buyer.BuyerActivity.class
            );

        } else {

            intent = new Intent(
                    MainActivity.this,
                    com.smarttech.store.roles.RoleWorkspaceActivity.class
            );

        }

        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("USER_ROLE", role);

        startActivity(intent);
    }
}