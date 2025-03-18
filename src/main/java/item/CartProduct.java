package item;

public class CartProduct extends Product {
    private int cart_quantity;

    public CartProduct() {
    }

    public CartProduct(int id, String name, String description, double price, String imageUrl, int quantity, int cart_quantity) {
        super(id, name, description, price, imageUrl, quantity);
        this.cart_quantity = cart_quantity;
    }

    public int getCartQuantity() {
        return cart_quantity;
    }

    public void setCartQuantity(int cart_quantity) {
        this.cart_quantity = cart_quantity;
    }
}
