package drinkshop.service.integration;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class Integration_SREVTest {

    private FileProductRepository realRepo;
    private ProductService productService;
    private final String TEST_FILE = "test_step4_srev.txt";

    @BeforeEach
    void setUp() throws IOException {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        file.createNewFile();

        realRepo = new FileProductRepository(TEST_FILE);
        productService = new ProductService(realRepo);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    @DisplayName("Step 4: Integrare S+R+E+V")
    void testUpdateProduct_ValidData_IntegratesWithValidator() {
        Product p = new Product(1, "FlatWhite", 8.0, CategorieBautura.TEA, TipBautura.WATER_BASED);
        productService.addProduct(p); // addProduct nu validează, doar pune în repo

        productService.updateProduct(1, "Cappucino", 10.5, CategorieBautura.CLASSIC_COFFEE, TipBautura.DAIRY);

        Product updatedProduct = productService.findById(1);
        assertEquals("Cappucino", updatedProduct.getNume());
        assertEquals(10.5, updatedProduct.getPret());
    }

    @Test
    @DisplayName("Step 4: Integrare S+R+E+V")
    void testUpdateProduct_InvalidData_ThrowsValidationException() {
        Product p = new Product(1, "FlatWhite", 8.0, CategorieBautura.TEA, TipBautura.WATER_BASED);
        productService.addProduct(p);
        ValidationException exception = assertThrows(ValidationException.class, () ->
                productService.updateProduct(1, "FlatWhite", -5.0, CategorieBautura.TEA, TipBautura.WATER_BASED)
        );
        assertTrue(exception.getMessage().contains("Pret invalid"), "Mesajul de eroare nu a apărut");
        Product unchangedProduct = productService.findById(1);
        assertEquals(8.0, unchangedProduct.getPret(), "Prețul a fost modificat");
    }
}
