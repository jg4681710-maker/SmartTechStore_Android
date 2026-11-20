
package com.smarttech.store.data;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.smarttech.store.R;
import com.smarttech.store.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class ProductStore {

    private static final String COLLECTION = "products";

    private final FirebaseFirestore firestore;

    public interface ProductsCallback {
        void onSuccess(ArrayList<Product> products);
        void onError(String message);
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String message);
    }

    public ProductStore() {
        firestore = FirebaseFirestore.getInstance();
    }

    // ASIGNAR IMAGEN LOCAL SEGUN EL NOMBRE
    private int getImageResource(String name) {

        if (name == null) {
            return R.drawable.ic_launcher_foreground;
        }

        String productName = name.toLowerCase(Locale.ROOT);

        if (productName.contains("hp")) {
            return R.drawable.img_hp;
        }

        if (productName.contains("jbl")) {
            return R.drawable.img_jbl;
        }

        if (productName.contains("lenovo")) {
            return R.drawable.img_lenovo;
        }

        if (productName.contains("tablet")) {
            return R.drawable.img_tablet;
        }

        if (productName.contains("samsung")) {
            return R.drawable.img_samsung;
        }

        return R.drawable.ic_launcher_foreground;
    }

    // CONSULTAR PRODUCTOS ACTIVOS
    public void getAll(ProductsCallback callback) {

        if (callback == null) {
            return;
        }

        firestore.collection(COLLECTION)
                .whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(snapshot -> {

                    ArrayList<Product> products = new ArrayList<>();

                    for (DocumentSnapshot document : snapshot.getDocuments()) {

                        String name = document.getString("name");
                        String description = document.getString("description");

                        Object priceObject = document.get("price");
                        Object idObject = document.get("id");

                        if (name == null || name.trim().isEmpty()
                                || !(priceObject instanceof Number)) {
                            continue;
                        }

                        double price = ((Number) priceObject).doubleValue();

                        if (!Double.isFinite(price) || price <= 0) {
                            continue;
                        }

                        int id = idObject instanceof Number
                                ? ((Number) idObject).intValue()
                                : (document.getId().hashCode() & 0x7fffffff);

                        int image = getImageResource(name);

                        Product product = new Product(
                                id,
                                document.getId(),
                                name,
                                description == null ? "" : description,
                                price,
                                image,
                                ""
                        );

                        products.add(product);
                    }

                    callback.onSuccess(products);
                })
                .addOnFailureListener(error ->
                        callback.onError(
                                "No se pudieron consultar los productos: "
                                        + getErrorMessage(error)
                        )
                );
    }

    // REGISTRAR UN PRODUCTO
    public void add(
            String name,
            String description,
            double price,
            OperationCallback callback
    ) {

        if (callback == null) {
            return;
        }

        if (!isValid(name, description, price)) {
            callback.onError(
                    "Verifica el nombre, la descripción y el precio."
            );
            return;
        }

        String documentId = firestore.collection(COLLECTION)
                .document()
                .getId();

        int newId = (int) (System.currentTimeMillis() & 0x7fffffff);

        Map<String, Object> product = new HashMap<>();

        product.put("id", newId);
        product.put("name", name.trim());
        product.put("description", description.trim());
        product.put("price", price);
        product.put("active", true);
        product.put("createdAt", FieldValue.serverTimestamp());

        firestore.collection(COLLECTION)
                .document(documentId)
                .set(product)
                .addOnSuccessListener(unused ->
                        callback.onSuccess()
                )
                .addOnFailureListener(error ->
                        callback.onError(
                                "No se pudo registrar el producto: "
                                        + getErrorMessage(error)
                        )
                );
    }

    // ACTUALIZAR UN PRODUCTO
    public void update(
            String documentId,
            String name,
            String description,
            double price,
            OperationCallback callback
    ) {

        if (callback == null) {
            return;
        }

        if (documentId == null || documentId.trim().isEmpty()) {
            callback.onError(
                    "El identificador del producto no es válido."
            );
            return;
        }

        if (!isValid(name, description, price)) {
            callback.onError(
                    "Verifica el nombre, la descripción y el precio."
            );
            return;
        }

        Map<String, Object> updates = new HashMap<>();

        updates.put("name", name.trim());
        updates.put("description", description.trim());
        updates.put("price", price);

        firestore.collection(COLLECTION)
                .document(documentId)
                .update(updates)
                .addOnSuccessListener(unused ->
                        callback.onSuccess()
                )
                .addOnFailureListener(error ->
                        callback.onError(
                                "No se pudo actualizar el producto: "
                                        + getErrorMessage(error)
                        )
                );
    }

    // ELIMINAR UN PRODUCTO
    public void delete(
            String documentId,
            OperationCallback callback
    ) {

        if (callback == null) {
            return;
        }

        if (documentId == null || documentId.trim().isEmpty()) {
            callback.onError(
                    "El identificador del producto no es válido."
            );
            return;
        }

        firestore.collection(COLLECTION)
                .document(documentId)
                .delete()
                .addOnSuccessListener(unused ->
                        callback.onSuccess()
                )
                .addOnFailureListener(error ->
                        callback.onError(
                                "No se pudo eliminar el producto: "
                                        + getErrorMessage(error)
                        )
                );
    }

    // VALIDAR LOS DATOS
    private boolean isValid(
            String name,
            String description,
            double price
    ) {

        return name != null
                && !name.trim().isEmpty()
                && description != null
                && !description.trim().isEmpty()
                && Double.isFinite(price)
                && price > 0;
    }

    // OBTENER MENSAJE DE ERROR
    private String getErrorMessage(Exception error) {

        if (error == null || error.getMessage() == null) {
            return "Error desconocido.";
        }

        return error.getMessage();
    }
}
