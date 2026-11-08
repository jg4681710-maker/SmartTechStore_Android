
package com.smarttech.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvRole = findViewById(R.id.tvRole);
        Button btnLogout = findViewById(R.id.btnLogout);

        String email = getIntent().getStringExtra("USER_EMAIL");
        String role = getIntent().getStringExtra("USER_ROLE");

        tvWelcome.setText("Bienvenido\n" + email);
        tvRole.setText("Perfil: " + role);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    MainActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK
            );

            startActivity(intent);
            finish();
        });
    }
}
