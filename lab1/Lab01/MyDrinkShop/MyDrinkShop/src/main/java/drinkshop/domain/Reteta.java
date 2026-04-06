package drinkshop.domain;

import java.util.List;

/**
 * Represents a recipe (Reteta) for a beverage product.
 * 
 * Responsibilities:
 * - defines the ingredients required to prepare a beverage
 * - maintains ingredient quantities for stock management
 * - supports customization through ingredient adjustment
 * 
 * Aggregation: Reteta --"is composed of"--> IngredientReteta (0..*) [composition]
 * Association: Reteta --"is used by"--> Product (implicit through id matching)
 * Association: IngredientReteta --"draws from"--> Stoc
 */
public class Reteta {

    private int id;
    private List<IngredientReteta> ingrediente;

    public Reteta(int id, List<IngredientReteta> ingrediente) {
        this.id = id;
        this.ingrediente = ingrediente;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public List<IngredientReteta> getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(List<IngredientReteta> ingrediente) {
        this.ingrediente = ingrediente;
    }

    @Override
    public String toString() {
        return "Reteta{" +
                "productId=" + id +
                ", ingrediente=" + ingrediente +
                '}';
    }
}

