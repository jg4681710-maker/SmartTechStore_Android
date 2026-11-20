package com.smarttech.store;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Button btnCreateAccount;
    private Button btnBackLogin;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etRegisterName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnBackLogin = findViewById(R.id.btnBackLogin);

        btnCreateAccount.setOnClickListener(v -> registerUser());
        btnBackLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmation = etConfirmPassword.getText().toString();

        if (name.isEmpty()) {
            etName.setError("Ingresa tu nombre");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingresa un correo válido");
            etEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Utiliza al menos 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmation)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return;
        }

        btnCreateAccount.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (!task.isSuccessful()) {

                        btnCreateAccount.setEnabled(true);

                        String error = task.getException() != null
                                ? task.getException().getMessage()
                                : "Error desconocido";

                        showMessage("No se pudo crear la cuenta: " + error);
                        return;
                    }

                    if (auth.getCurrentUser() == null) {
                        btnCreateAccount.setEnabled(true);
                        showMessage("No se pudo obtener el usuario registrado.");
                        return;
                    }

                    String uid = auth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("uid", uid);
                    user.put("name", name);
                    user.put("email", email);
                    user.put("role", "COMPRADOR");
                    user.put("createdAt", FieldValue.serverTimestamp());

                    saveUserProfile(uid, user);
                });
    }

    private void saveUserProfile(
            String uid,
            Map<String, Object> user
    ) {

        firestore.collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(unused -> {

                    auth.signOut();

                    showMessage(
                            "Cuenta creada correctamente. Ya puedes iniciar sesión."
                    );

                    Intent intent = new Intent(
                            RegisterActivity.this,
                            MainActivity.class
                    );

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );

                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(error -> {

                    // Si falla Firestore, intenta revertir la cuenta recién creada.
                    if (auth.getCurrentUser() != null) {

                        auth.getCurrentUser()
                                .delete()
                                .addOnCompleteListener(deleteTask -> {

                                    auth.signOut();
                                    btnCreateAccount.setEnabled(true);

                                    if (deleteTask.isSuccessful()) {

                                        showMessage(
                                                "No se pudo guardar el perfil. "
                                                        + "La cuenta fue revertida. "
                                                        + "Intenta registrarte nuevamente."
                                        );

                                    } else {

                                        showMessage(
                                                "Falló el guardado del perfil y "
                                                        + "no se pudo eliminar la cuenta. "
                                                        + "Contacta al administrador."
                                        );
                                    }
                                });

                    } else {

                        btnCreateAccount.setEnabled(true);

                        showMessage(
                                "No se pudo guardar el perfil: "
                                        + error.getMessage()
                        );
                    }
                });
    }

    private void showMessage(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
