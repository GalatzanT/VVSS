
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Integration_SRTest {

    private FileProductRepository realRepo;
    private ProductService productService;
    private final String TEST_FILE = "test_step2_sr.txt";

    @Mock
    private Product mockProduct;

    @BeforeEach
    void setUp() throws IOException {
        // Setup fișier curat pentru Repo real
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        file.createNewFile();

        // Integrare reală S + R
        realRepo = new FileProductRepository(TEST_FILE);
        productService = new ProductService(realRepo);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    @DisplayName("Step 2: Integrare S+R")
    void testAddProduct_WithRealRepo_AndMockEntity() {
        when(mockProduct.getId()).thenReturn(54);
        when(mockProduct.getNume()).thenReturn("Mocked Coffee");
        when(mockProduct.getPret()).thenReturn(18.0);
        when(mockProduct.getCategorie()).thenReturn(CategorieBautura.SPECIAL_COFFEE);
        when(mockProduct.getTip()).thenReturn(TipBautura.BASIC);

        productService.addProduct(mockProduct);

        Product retrieved = productService.findById(54);
        assertNotNull(retrieved, "Produsul mock nu a fost gasit in fisier");
        assertEquals("Mocked Coffee", retrieved.getNume());
        assertEquals(18.0, retrieved.getPret());

        verify(mockProduct, atLeastOnce()).getId();
        verify(mockProduct, atLeastOnce()).getNume();
    }

    @Test
    @DisplayName("Step 2: Integrare S+R - deleteProduct șterge corect folosind Repo real")
    void testDeleteProduct_WithRealRepo() {
        when(mockProduct.getId()).thenReturn(1);
        when(mockProduct.getNume()).thenReturn("Delete");
        when(mockProduct.getPret()).thenReturn(87.0);
        when(mockProduct.getCategorie()).thenReturn(CategorieBautura.TEA);
        when(mockProduct.getTip()).thenReturn(TipBautura.WATER_BASED);

        productService.addProduct(mockProduct);
        assertEquals(1, productService.getAllProducts().size());

        productService.deleteProduct(1);

        assertEquals(0, productService.getAllProducts().size(), "Repository-ul nu e gol dupa stergere");
    }

    @Test
    @DisplayName("Step 2: Integrare S+R - getAllProducts returneaza corect mock-urile salvate")
    void testGetAllProducts_WithRealRepo_AndMockEntities() {
        Product mockProduct2 = org.mockito.Mockito.mock(Product.class);

        when(mockProduct.getId()).thenReturn(1);
        when(mockProduct.getNume()).thenReturn("Mock Cafea");
        when(mockProduct.getPret()).thenReturn(100.0);
        when(mockProduct.getCategorie()).thenReturn(CategorieBautura.CLASSIC_COFFEE);
        when(mockProduct.getTip()).thenReturn(TipBautura.BASIC);

        when(mockProduct2.getId()).thenReturn(2);
        when(mockProduct2.getNume()).thenReturn("Mock Ceai");
        when(mockProduct2.getPret()).thenReturn(12.0);
        when(mockProduct2.getCategorie()).thenReturn(CategorieBautura.TEA);
        when(mockProduct2.getTip()).thenReturn(TipBautura.WATER_BASED);

        productService.addProduct(mockProduct);
        productService.addProduct(mockProduct2);

        var allProducts = productService.getAllProducts();

        assertEquals(2, allProducts.size(), "Ar trebui sa gasim 2 produse in repo-ul real");

        verify(mockProduct, atLeastOnce()).getId();
        verify(mockProduct2, atLeastOnce()).getId();
    }
}
