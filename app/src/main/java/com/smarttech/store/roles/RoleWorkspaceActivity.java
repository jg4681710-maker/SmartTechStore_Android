
package com.smarttech.store.roles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.smarttech.store.MainActivity;
import com.smarttech.store.data.ProductStore;
import com.smarttech.store.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RoleWorkspaceActivity extends Activity {

    private String role;
    private String email;

    private LinearLayout root;
    private ProductStore productStore;
    private NumberFormat currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        role = getIntent().getStringExtra("USER_ROLE");
        email = getIntent().getStringExtra("USER_EMAIL");

        if (role == null) {
            role = "";
        }

        if (email == null) {
            email = "";
        }

        productStore = new ProductStore();

        currency = NumberFormat.getCurrencyInstance(
                new Locale("es", "CO")
        );

        createInterface();
    }

    // Construye el panel de acuerdo con el rol recibido.
    private void createInterface() {

        ScrollView scroll = new ScrollView(this);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 32, 28, 28);
        root.setBackgroundColor(Color.rgb(245, 247, 250));

        scroll.addView(root);

        TextView title = new TextView(this);
        title.setText("SMARTTECH STORE\n" + role.toUpperCase());
        title.setTextSize(25);
        title.setTextColor(Color.rgb(20, 45, 75));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 20);

        root.addView(title);

        TextView welcome = new TextView(this);
        welcome.setText("Sesión: " + email + "\nPanel de gestión");
        welcome.setTextSize(15);
        welcome.setTextColor(Color.DKGRAY);

        root.addView(welcome);

        if (role.equalsIgnoreCase("Administrador")) {

            createAdminPanel();

        } else if (role.equalsIgnoreCase("Vendedor")) {

            createSellerPanel();

        } else {

            addSection("Acceso no autorizado");

            addAction(
                    "Cerrar sesión",
                    "Regresar al inicio de sesión",
                    this::logout
            );
        }

        addSection("Cuenta");

        addAction(
                "Cerrar sesión",
                "Salir de SmartTech Store",
                this::logout
        );

        setContentView(scroll);
    }

    // Opciones del administrador.
    private void createAdminPanel() {

        addSection("Gestión de productos");

        addAction(
                "Registrar producto",
                "Crear un producto nuevo",
                this::showAddProductDialog
        );

        addAction(
                "Consultar productos",
                "Ver los productos registrados",
                this::showProducts
        );

        addAction(
                "Editar producto",
                "Modificar nombre, descripción y precio",
                this::showEditProductDialog
        );

        addAction(
                "Eliminar producto",
                "Retirar un producto del catálogo",
                this::showDeleteProductDialog
        );

        addSection("Administración");

        addAction(
                "Gestión de usuarios",
                "Administrar las cuentas de la aplicación",
                () -> showInformation(
                        "Gestión de usuarios",
                        "Este módulo requiere un servicio administrativo "
                                + "seguro para consultar y modificar cuentas."
                )
        );

        addAction(
                "Reporte de ventas",
                "Consultar el estado de las ventas",
                () -> showInformation(
                        "Reporte de ventas",
                        "El reporte compartido de ventas está pendiente "
                                + "de conectar con los registros de pedidos."
                )
        );
    }

    // Opciones del vendedor.
    private void createSellerPanel() {

        addSection("Zona de vendedor");

        addAction(
                "Consultar productos",
                "Ver los productos disponibles",
                this::showProducts
        );

        addAction(
                "Mi perfil",
                "Consultar los datos de la sesión",
                () -> showInformation(
                        "Perfil del vendedor",
                        "Cuenta activa:\n" + email
                )
        );

        addAction(
                "Pedidos recibidos",
                "Consultar el estado de los pedidos",
                () -> showInformation(
                        "Pedidos recibidos",
                        "La consulta compartida de pedidos está pendiente "
                                + "de implementar."
                )
        );
    }

    private void addSection(String text) {

        TextView title = new TextView(this);
        title.setText(text);
        title.setTextSize(19);
        title.setTextColor(Color.rgb(25, 90, 140));
        title.setPadding(0, 25, 0, 10);

        root.addView(title);
    }

    private void addAction(
            String title,
            String description,
            Runnable action
    ) {

        Button button = new Button(this);

        button.setText(title + "\n" + description);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.bottomMargin = 10;

        root.addView(button, params);

        button.setOnClickListener(view -> action.run());
    }

    // Formulario de registro.
    private void showAddProductDialog() {

        LinearLayout form = createForm();

        EditText name = createInput("Nombre del producto");
        EditText description = createInput("Descripción");
        EditText price = createPriceInput();

        form.addView(name);
        form.addView(description);
        form.addView(price);

        new AlertDialog.Builder(this)
                .setTitle("Registrar producto")
                .setView(form)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    String productName =
                            name.getText().toString().trim();

                    String productDescription =
                            description.getText().toString().trim();

                    double value;

                    try {
                        value = parsePrice(price.getText().toString());

                    } catch (NumberFormatException e) {
                        showMessage("Ingresa un precio válido.");
                        return;
                    }

                    if (productName.isEmpty()
                            || productDescription.isEmpty()
                            || !Double.isFinite(value)
                            || value <= 0) {

                        showMessage(
                                "Completa los datos y verifica el precio."
                        );
                        return;
                    }

                    productStore.add(
                            productName,
                            productDescription,
                            value,
                            new ProductStore.OperationCallback() {

                                @Override
                                public void onSuccess() {
                                    showMessage(
                                            "Producto registrado correctamente."
                                    );
                                }

                                @Override
                                public void onError(String message) {
                                    showMessage(message);
                                }
                            }
                    );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Consulta de productos.
    private void showProducts() {

        productStore.getAll(new ProductStore.ProductsCallback() {

            @Override
            public void onSuccess(ArrayList<Product> products) {

                if (isFinishing() || isDestroyed()) {
                    return;
                }

                if (products.isEmpty()) {
                    showMessage(
                            "No hay productos registrados en Firestore."
                    );
                    return;
                }

                String[] items = new String[products.size()];

                for (int i = 0; i < products.size(); i++) {

                    Product product = products.get(i);

                    items[i] = product.getName()
                            + "\nCódigo: " + product.getId()
                            + "\nPrecio: "
                            + currency.format(product.getPrice())
                            + "\n" + product.getDescription();
                }

                new AlertDialog.Builder(RoleWorkspaceActivity.this)
                        .setTitle("Productos registrados")
                        .setItems(items, (dialog, position) -> {

                            Product selected = products.get(position);

                            showInformation(
                                    selected.getName(),
                                    "Código: " + selected.getId()
                                            + "\nDescripción: "
                                            + selected.getDescription()
                                            + "\nPrecio: "
                                            + currency.format(
                                            selected.getPrice()
                                    )
                            );
                        })
                        .setPositiveButton("Cerrar", null)
                        .show();
            }

            @Override
            public void onError(String message) {

                if (!isFinishing() && !isDestroyed()) {
                    showMessage(message);
                }
            }
        });
    }

    // Seleccionar producto para editar.
    private void showEditProductDialog() {

        productStore.getAll(new ProductStore.ProductsCallback() {

            @Override
            public void onSuccess(ArrayList<Product> products) {

                if (isFinishing() || isDestroyed()) {
                    return;
                }

                if (products.isEmpty()) {
                    showMessage("No hay productos para editar.");
                    return;
                }

                String[] names = new String[products.size()];

                for (int i = 0; i < products.size(); i++) {
                    names[i] = products.get(i).getName();
                }

                new AlertDialog.Builder(RoleWorkspaceActivity.this)
                        .setTitle("Seleccionar producto")
                        .setItems(names, (dialog, position) ->
                                showProductEditForm(products.get(position))
                        )
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onError(String message) {

                if (!isFinishing() && !isDestroyed()) {
                    showMessage(message);
                }
            }
        });
    }

    // Formulario de edición.
    private void showProductEditForm(Product product) {

        LinearLayout form = createForm();

        EditText name = createInput("Nombre");
        EditText description = createInput("Descripción");
        EditText price = createPriceInput();

        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(String.valueOf(product.getPrice()));

        form.addView(name);
        form.addView(description);
        form.addView(price);

        new AlertDialog.Builder(this)
                .setTitle("Editar producto")
                .setView(form)
                .setPositiveButton("Actualizar", (dialog, which) -> {

                    String productName =
                            name.getText().toString().trim();

                    String productDescription =
                            description.getText().toString().trim();

                    double value;

                    try {
                        value = parsePrice(price.getText().toString());

                    } catch (NumberFormatException e) {
                        showMessage("Ingresa un precio válido.");
                        return;
                    }

                    if (productName.isEmpty()
                            || productDescription.isEmpty()
                            || !Double.isFinite(value)
                            || value <= 0) {

                        showMessage(
                                "Completa los datos y verifica el precio."
                        );
                        return;
                    }

                    productStore.update(
                            product.getDocumentId(),
                            productName,
                            productDescription,
                            value,
                            new ProductStore.OperationCallback() {

                                @Override
                                public void onSuccess() {
                                    showMessage(
                                            "Producto actualizado correctamente."
                                    );
                                }

                                @Override
                                public void onError(String message) {
                                    showMessage(message);
                                }
                            }
                    );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Seleccionar producto para eliminar.
    private void showDeleteProductDialog() {

        productStore.getAll(new ProductStore.ProductsCallback() {

            @Override
            public void onSuccess(ArrayList<Product> products) {

                if (isFinishing() || isDestroyed()) {
                    return;
                }

                if (products.isEmpty()) {
                    showMessage("No hay productos para eliminar.");
                    return;
                }

                String[] names = new String[products.size()];

                for (int i = 0; i < products.size(); i++) {
                    names[i] = products.get(i).getName();
                }

                new AlertDialog.Builder(RoleWorkspaceActivity.this)
                        .setTitle("Seleccionar producto")
                        .setItems(names, (dialog, position) -> {

                            Product selected = products.get(position);

                            new AlertDialog.Builder(
                                    RoleWorkspaceActivity.this
                            )
                                    .setTitle("Confirmar eliminación")
                                    .setMessage(
                                            "¿Deseas eliminar "
                                                    + selected.getName()
                                                    + " del catálogo?"
                                    )
                                    .setPositiveButton(
                                            "Eliminar",
                                            (d, w) -> {

                                                productStore.delete(
                                                        selected.getDocumentId(),
                                                        new ProductStore.OperationCallback() {

                                                            @Override
                                                            public void onSuccess() {
                                                                showMessage(
                                                                        "Producto eliminado correctamente."
                                                                );
                                                            }

                                                            @Override
                                                            public void onError(String message) {
                                                                showMessage(message);
                                                            }
                                                        }
                                                );
                                            }
                                    )
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }

            @Override
            public void onError(String message) {

                if (!isFinishing() && !isDestroyed()) {
                    showMessage(message);
                }
            }
        });
    }

    private LinearLayout createForm() {

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(35, 10, 35, 5);

        return form;
    }

    private EditText createInput(String hint) {

        EditText input = new EditText(this);
        input.setHint(hint);

        return input;
    }

    private EditText createPriceInput() {

        EditText input = new EditText(this);
        input.setHint("Precio en pesos");
        input.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        return input;
    }

    // Acepta punto o coma como separador decimal.
    private double parsePrice(String value) {

        return Double.parseDouble(
                value.trim().replace(",", ".")
        );
    }

    private void showInformation(String title, String message) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void showMessage(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    // Cerrar sesión.
    private void logout() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(
                RoleWorkspaceActivity.this,
                MainActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}
