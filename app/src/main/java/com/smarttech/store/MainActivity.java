
package com.smarttech.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.smarttech.store.buyer.BuyerActivity;
import com.smarttech.store.roles.RoleWorkspaceActivity;

public class MainActivity extends Activity {

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;
    private Button btnRegister;
    private TextView tvForgotPassword;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private boolean loginInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> loginUser());

        btnRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(
                v -> recoverPassword()
        );
    }

    // INICIAR SESIÓN
    private void loginUser() {

        if (loginInProgress) {
            return;
        }

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (email.isEmpty()) {

            etEmail.setError("Ingresa tu correo electrónico");
            etEmail.requestFocus();

            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError("Ingresa un correo válido");
            etEmail.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            etPassword.setError("Ingresa tu contraseña");
            etPassword.requestFocus();

            return;
        }

        loginInProgress = true;
        btnLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (!task.isSuccessful()
                            || auth.getCurrentUser() == null) {

                        finishLoginWithError(
                                "No se pudo iniciar sesión. "
                                        + "Verifica tus credenciales."
                        );

                        return;
                    }

                    String uid = auth.getCurrentUser().getUid();

                    loadUserProfile(uid);
                });
    }

    // CONSULTAR PERFIL EN FIRESTORE
    private void loadUserProfile(String uid) {

        firestore.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(this, profileTask -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (!profileTask.isSuccessful()) {

                        finishLoginWithError(
                                "No se pudo consultar el perfil del usuario."
                        );

                        return;
                    }

                    // Verificar que la sesión siga perteneciendo
                    // al mismo usuario que inició sesión.
                    if (auth.getCurrentUser() == null
                            || !uid.equals(auth.getCurrentUser().getUid())) {

                        finishLoginWithError(
                                "La sesión cambió. Inicia sesión nuevamente."
                        );

                        return;
                    }

                    DocumentSnapshot document =
                            profileTask.getResult();

                    if (document == null || !document.exists()) {

                        finishLoginWithError(
                                "No existe un perfil asociado a esta cuenta."
                        );

                        return;
                    }

                    String role = document.getString("role");
                    String userEmail = auth.getCurrentUser().getEmail();

                    String profileEmail = document.getString("email");

                    if (role == null
                            || userEmail == null
                            || profileEmail == null
                            || !userEmail.equalsIgnoreCase(profileEmail)) {

                        finishLoginWithError(
                                "El perfil del usuario está incompleto "
                                        + "o no coincide con la cuenta."
                        );

                        return;
                    }

                    openUserModule(role, userEmail, uid);
                });
    }

    // REDIRECCIONAR SEGÚN EL ROL
    private void openUserModule(
            String role,
            String email,
            String uid
    ) {

        Intent intent;

        if ("COMPRADOR".equals(role)) {

            intent = new Intent(
                    MainActivity.this,
                    BuyerActivity.class
            );

            intent.putExtra("USER_ROLE", "Comprador");

        } else if ("VENDEDOR".equals(role)) {

            intent = new Intent(
                    MainActivity.this,
                    RoleWorkspaceActivity.class
            );

            intent.putExtra("USER_ROLE", "Vendedor");

        } else if ("ADMINISTRADOR".equals(role)) {

            intent = new Intent(
                    MainActivity.this,
                    RoleWorkspaceActivity.class
            );

            intent.putExtra("USER_ROLE", "Administrador");

        } else {

            finishLoginWithError(
                    "El rol de esta cuenta no está reconocido."
            );

            return;
        }

        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("USER_UID", uid);

        loginInProgress = false;

        startActivity(intent);
        finish();
    }

    // FINALIZAR UN INTENTO DE INICIO DE SESIÓN CON ERROR
    private void finishLoginWithError(String message) {

        auth.signOut();

        loginInProgress = false;

        if (!isFinishing() && !isDestroyed()) {

            btnLogin.setEnabled(true);

            Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // RECUPERAR CONTRASEÑA
    private void recoverPassword() {

        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError(
                    "Escribe un correo válido para recuperar la contraseña"
            );

            etEmail.requestFocus();

            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                this,
                                "Si la cuenta existe, recibirás "
                                        + "instrucciones para restablecer "
                                        + "tu contraseña.",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "No se pudo solicitar la recuperación. "
                                        + "Inténtalo nuevamente.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
