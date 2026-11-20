
package com.smarttech.store.cart;

import com.smarttech.store.models.Product;

import java.util.ArrayList;

public final class CartManager {

    private static final ArrayList<Product> cart = new ArrayList<>();
    private static final ArrayList<Integer> quantities = new ArrayList<>();

    private static final int MAX_QUANTITY = 99;

    private CartManager() {
        // Evita crear instancias de esta clase.
    }

    // AGREGAR PRODUCTO
    public static synchronized void addProduct(Product product) {

        if (!isValidProduct(product)) {
            return;
        }

        int index = findProduct(product.getId());

        if (index >= 0) {

            int currentQuantity = quantities.get(index);

            if (currentQuantity < MAX_QUANTITY) {
                quantities.set(index, currentQuantity + 1);
            }

        } else {

            cart.add(product);
            quantities.add(1);
        }
    }

    // OBTENER PRODUCTOS
    public static synchronized ArrayList<Product> getProducts() {

        return new ArrayList<>(cart);
    }

    // OBTENER CANTIDAD POR ID
    public static synchronized int getQuantity(int productId) {

        int index = findProduct(productId);

        if (index < 0) {
            return 0;
        }

        return quantities.get(index);
    }

    // OBTENER CANTIDAD POR POSICIÓN
    public static synchronized int getQuantityAt(int position) {

        if (position < 0 || position >= quantities.size()) {
            return 0;
        }

        return quantities.get(position);
    }

    // AUMENTAR CANTIDAD
    public static synchronized void increaseQuantity(int productId) {

        int index = findProduct(productId);

        if (index < 0) {
            return;
        }

        int quantity = quantities.get(index);

        if (quantity < MAX_QUANTITY) {
            quantities.set(index, quantity + 1);
        }
    }

    // DISMINUIR CANTIDAD
    public static synchronized void decreaseQuantity(int productId) {

        int index = findProduct(productId);

        if (index < 0) {
            return;
        }

        int quantity = quantities.get(index);

        if (quantity > 1) {

            quantities.set(index, quantity - 1);

        } else {

            removeAt(index);
        }
    }

    // ELIMINAR PRODUCTO POR ID
    public static synchronized void removeProductById(int productId) {

        int index = findProduct(productId);

        if (index >= 0) {
            removeAt(index);
        }
    }

    // ELIMINAR PRODUCTO POR POSICIÓN
    public static synchronized void removeProduct(int position) {

        if (position >= 0 && position < cart.size()) {
            removeAt(position);
        }
    }

    // CALCULAR TOTAL
    public static synchronized double getTotal() {

        double total = 0;

        for (int i = 0; i < cart.size(); i++) {

            Product product = cart.get(i);

            int quantity = quantities.get(i);

            double subtotal = product.getPrice() * quantity;

            if (!Double.isFinite(subtotal)) {
                return 0;
            }

            total += subtotal;
        }

        return Double.isFinite(total) ? total : 0;
    }

    // CONTAR UNIDADES
    public static synchronized int getCount() {

        int count = 0;

        for (int quantity : quantities) {
            count += quantity;
        }

        return count;
    }

    // CONTAR PRODUCTOS DIFERENTES
    public static synchronized int getDistinctProductCount() {

        return cart.size();
    }

    // VERIFICAR SI ESTÁ VACÍO
    public static synchronized boolean isEmpty() {

        return cart.isEmpty();
    }

    // VACIAR CARRITO
    public static synchronized void clearCart() {

        cart.clear();
        quantities.clear();
    }

    // BUSCAR PRODUCTO
    private static int findProduct(int productId) {

        for (int i = 0; i < cart.size(); i++) {

            if (cart.get(i).getId() == productId) {
                return i;
            }
        }

        return -1;
    }

    // ELIMINAR ELEMENTOS MANTENIENDO LAS LISTAS SINCRONIZADAS
    private static void removeAt(int index) {

        if (index >= 0 && index < cart.size()) {

            cart.remove(index);
            quantities.remove(index);
        }
    }

    // VALIDAR PRODUCTO
    private static boolean isValidProduct(Product product) {

        return product != null
                && product.getId() >= 0
                && product.getName() != null
                && !product.getName().trim().isEmpty()
                && Double.isFinite(product.getPrice())
                && product.getPrice() > 0;
    }
}
