package drinkshop.domain;

import java.io.Serializable;

/**
 * Represents a beverage product in the DrinkShop system.
 * 
 * A product is characterized by:
 * - an identifier (unique)
 * - a name/description
 * - a price
 * - a category (e.g., HOT, COLD)
 * - a type (e.g., COFFEE, TEA)
 * 
 * Association: Product --"belongs to"--> CategorieBautura
 * Association: Product --"is of type"--> TipBautura
 * Association: Product --"uses recipe"--> Reteta (implicit through id matching)
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;
    private int id;
    private String nume;
    private double pret;
    private CategorieBautura categorie;
    private TipBautura tip;

    public Product(int id, String nume, double pret,
                  CategorieBautura categorie,
                  TipBautura tip) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.categorie = categorie;
        this.tip = tip;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public double getPret() { return pret; }
    public CategorieBautura getCategorie() { return categorie; }

    public void setCategorie(CategorieBautura categorie) {
        this.categorie = categorie;
    }

    public TipBautura getTip() { return tip; }

    public void setTip(TipBautura tip) {
        this.tip = tip;
    }
    public void setNume(String nume) { this.nume = nume; }
    public void setPret(double pret) { this.pret = pret; }

    @Override
    public String toString() {
        return nume + " (" + categorie + ", " + tip + ") - " + pret + " lei";
    }
}