package drinkshop.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer order in the DrinkShop system.
 * 
 * Responsibilities:
 * - maintains the ordered items
 * - computes and stores the total price
 * - provides order identification
 * 
 * Aggregation: Order --"contains"-> OrderItem (0..*) [composition]
 * Aggregation: OrderItem --"references"-> Product
 * 
 * Note: This entity could be extended to include customer information and timestamps.
 */
public class Order implements Serializable {

    private int id;
    private List<OrderItem> items;
    private double totalPrice;

    public Order(int id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Order(int id, List<OrderItem> items, double totalPrice) {
        this.id = id;
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public double getTotal() {
        return totalPrice;
    }

    public void computeTotalPrice() {
        this.totalPrice=items.stream().mapToDouble(OrderItem::getTotal).sum();
    }
}