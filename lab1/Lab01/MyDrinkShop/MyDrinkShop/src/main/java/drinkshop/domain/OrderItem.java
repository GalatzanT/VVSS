package drinkshop.domain;

import java.io.Serializable;

/**
 * Represents a line item in an order.
 * 
 * Responsibilities:
 * - associates a product with a quantity
 * - computes the subtotal for that item
 * 
 * Association: OrderItem --"contains"--> Product (multiplicity 1)
 * Role names: 
 *   - OrderItem.product: the product being ordered
 *   - OrderItem.quantity: the number of units ordered
 * 
 * Invariant: quantity >= 1
 */
public class OrderItem implements Serializable {

    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public double getTotal() {
        return product.getPret() * quantity;
    }
}
