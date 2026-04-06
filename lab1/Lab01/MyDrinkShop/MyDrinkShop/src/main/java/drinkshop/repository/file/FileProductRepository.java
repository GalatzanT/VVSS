package drinkshop.repository.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import drinkshop.domain.Product;
import drinkshop.domain.CategorieBautura;
import drinkshop.domain.TipBautura;

public class FileProductRepository
        extends FileAbstractRepository<Integer, Product> {

    public FileProductRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }

    @Override
    protected Product extractEntity(String line) {

        String[] elems = line.split(",");

        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        double price = Double.parseDouble(elems[2]);
        CategorieBautura categorie = CategorieBautura.valueOf(elems[3]);
        TipBautura tip = TipBautura.valueOf(elems[4]);

        return new Product(id, name, price, categorie, tip);
    }

    @Override
    public Product save(Product entity) {
        if (entity == null) { // Nod 2
            throw new IllegalArgumentException("Entity cannot be null"); // Nod 3
        }

        // Nod 4: Condiție multiplă
        if (entity.getPret() <= 0 || entity.getNume() == null || entity.getNume().trim().isEmpty()) { // Nod 5, 6, 7
            throw new IllegalArgumentException("Invalid product data"); // Nod 8
        }

        Product saved = super.save(entity); // Nod 9

        // Conținutul metodei writeToFile() integrat/apelat aici:
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) { // Nod 10
            for (Product p : entities.values()) { // Nod 11
                bw.write(createEntityAsString(p)); // Nod 12
                bw.newLine();
            }
        } catch (IOException e) { // Nod 13
            return null;
        }

        return saved; // Nod 15
    }

    @Override
    protected String createEntityAsString(Product entity) {
        return entity.getId() + "," +
                entity.getNume() + "," +
                entity.getPret() + "," +
                entity.getCategorie() + "," +
                entity.getTip();
    }
}
