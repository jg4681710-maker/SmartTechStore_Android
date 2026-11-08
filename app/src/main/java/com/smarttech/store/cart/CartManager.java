
package com.smarttech.store.cart;

import com.smarttech.store.models.Product;

import java.util.ArrayList;

public final class CartManager {

    private static final ArrayList<Product> cart = new ArrayList<>();
    private static final ArrayList<Integer> quantities = new ArrayList<>();

    private CartManager() {
    }

    /**
     * Agrega un producto al carrito.
     * Si ya existe, incrementa su cantidad.
     */
    public static synchronized void addProduct(Product product) {

        if (product == null) {
            return;
        }

        int index = findProduct(product.getId());

        if (index >= 0) {
            quantities.set(index, quantities.get(index) + 1);
        } else {
            cart.add(product);
            quantities.add(1);
        }
    }

    /**
     * Devuelve una copia de los productos del carrito.
     */
    public static synchronized ArrayList<Product> getProducts() {
        return new ArrayList<>(cart);
    }

    /**
     * Devuelve la cantidad de unidades de un producto.
     */
    public static synchronized int getQuantity(int productId) {

        int index = findProduct(productId);

        if (index < 0) {
            return 0;
        }

        return quantities.get(index);
    }

    /**
     * Devuelve la cantidad de unidades por posición.
     */
    public static synchronized int getQuantityAt(int position) {

        if (position < 0 || position >= quantities.size()) {
            return 0;
        }

        return quantities.get(position);
    }

    /**
     * Incrementa la cantidad de un producto.
     */
    public static synchronized void increaseQuantity(int productId) {

        int index = findProduct(productId);

        if (index >= 0) {
            quantities.set(index, quantities.get(index) + 1);
        }
    }

    /**
     * Reduce la cantidad de un producto.
     * Si llega a cero, elimina el producto.
     */
    public static synchronized void decreaseQuantity(int productId) {

        int index = findProduct(productId);

        if (index < 0) {
            return;
        }

        int quantity = quantities.get(index);

        if (quantity > 1) {
            quantities.set(index, quantity - 1);
        } else {
            cart.remove(index);
            quantities.remove(index);
        }
    }

    /**
     * Elimina un producto por su identificador.
     */
    public static synchronized void removeProductById(int productId) {

        int index = findProduct(productId);

        if (index >= 0) {
            cart.remove(index);
            quantities.remove(index);
        }
    }

    /**
     * Elimina un producto por posición.
     * Conserva compatibilidad con CartActivity existente.
     */
    public static synchronized void removeProduct(int position) {

        if (position >= 0 && position < cart.size()) {
            cart.remove(position);
            quantities.remove(position);
        }
    }

    /**
     * Calcula el total considerando las cantidades.
     */
    public static synchronized double getTotal() {

        double total = 0;

        for (int i = 0; i < cart.size(); i++) {

            Product product = cart.get(i);
            int quantity = quantities.get(i);

            total += product.getPrice() * quantity;
        }

        return total;
    }

    /**
     * Cantidad total de unidades.
     */
    public static synchronized int getCount() {

        int count = 0;

        for (int quantity : quantities) {
            count += quantity;
        }

        return count;
    }

    /**
     * Cantidad de productos diferentes.
     */
    public static synchronized int getDistinctProductCount() {
        return cart.size();
    }

    /**
     * Vacía completamente el carrito.
     */
    public static synchronized void clearCart() {
        cart.clear();
        quantities.clear();
    }

    /**
     * Busca un producto por su ID.
     */
    private static int findProduct(int productId) {

        for (int i = 0; i < cart.size(); i++) {

            if (cart.get(i).getId() == productId) {
                return i;
            }
        }

        return -1;
    }
}
