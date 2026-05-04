package drinkshop.service.integration;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Integration_SRETest {

    private FileProductRepository realRepo;
    private ProductService productService;
    private final String TEST_FILE = "test_step3_sre.txt";

    @BeforeEach
    void setUp() throws IOException {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        file.createNewFile();

        // Tot fluxul S -> R -> E este real
        realRepo = new FileProductRepository(TEST_FILE);
        productService = new ProductService(realRepo);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    @DisplayName("Step 3: Integrare S+R+E ")
    void testFullIntegration_AddAndRetrieveEntities() {
        Product p1 = new Product(1, "FlatWhite", 18.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);

        productService.addProduct(p1);

        assertEquals(1, productService.getAllProducts().size());
        assertEquals("FlatWhite", productService.findById(1).getNume());
    }

    @Test
    @DisplayName("Step 3: Integrare S+R+E ")
    void testFullIntegration_FilterByTip() {
        productService.addProduct(new Product(1, "FlatWhite", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY));
        productService.addProduct(new Product(2, "Ceai", 12.0, CategorieBautura.JUICE, TipBautura.WATER_BASED));

        List<Product> waterBasedDrinks = productService.filterByTip(TipBautura.WATER_BASED);

        assertEquals(1, waterBasedDrinks.size(), "Exista doar o bautura pe baza de apa");
        assertEquals("Ceai", waterBasedDrinks.get(0).getNume(), "Eroare la filtrare");
    }

    @Test
    @DisplayName("Step 3: Integrare S+R+E")
    void testFullIntegration_DeleteRealProduct() {
        Product p = new Product(1, "Iced Latte", 18.0, CategorieBautura.ICED_COFFEE, TipBautura.DAIRY);
        productService.addProduct(p);

        assertEquals(1, productService.getAllProducts().size());
        assertNotNull(productService.findById(1));
        productService.deleteProduct(1);

        assertEquals(0, productService.getAllProducts().size(), "Stergerea produsului a esuat");
        assertNull(productService.findById(1), "Stergerea produsului a esuat");
    }
}
