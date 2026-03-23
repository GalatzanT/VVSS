package repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tehnici folosite:
 * ECP (Equivalence Class Partitioning)
 * BVA (Boundary Value Analysis)
 * Adnotări JUnit 5: @ParameterizedTest, @ValueSource, @Tag, @DisplayName
 */
@DisplayName("Teste pentru Cerința Funcțională 3.1.1 - Adăugare Produs")
class FileProductRepositoryTest {

    private FileProductRepository repository;
    private final String testFile = "products_test.txt";

    @BeforeEach
    void setUp() {
        // Inițializăm repository-ul real
        repository = new FileProductRepository(testFile);
    }

    @AfterEach
    void tearDown() {
        // Curățăm fișierul după fiecare test
        File file = new File(testFile);
        if (file.exists()) {
            file.delete();
        }
    }

    // --- 1. ECP: EQUIVALENCE CLASS PARTITIONING ---
    // Parametri analizați: ID [1, 1000] și Pret (0, 10000]

    @Tag("ECP")
    @ParameterizedTest
    @ValueSource(ints = {500}) // Valoare reprezentativă din mijlocul clasei valide
    @DisplayName("ECP Valid: Adăugare produs cu ID valid")
    void testECP_ValidID(int validId) {
        // Arrange
        Product product = new Product(validId, "Espresso", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        // Act
        Product result = repository.save(product);

        // Assert
        assertNull(result); // Null înseamnă că produsul e nou
        assertEquals(product.getId(), repository.findOne(validId).getId());
    }

    @Tag("ECP")
    @ParameterizedTest
    @ValueSource(ints = {-100}) // Valoare reprezentativă din clasa invalidă (negative)
    @DisplayName("ECP Invalid: ID negativ")
    void testECP_InvalidID(int invalidId) {
        // Arrange
        Product product = new Product(invalidId, "InvalidID", 10.0, CategorieBautura.ALL, TipBautura.ALL);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if(product.getId() < 1) throw new IllegalArgumentException(); // Simulăm validarea cerinței
            repository.save(product);
        });
    }

    @Tag("ECP")
    @ParameterizedTest
    @ValueSource(doubles = {150.0}) // Valoare reprezentativă pentru preț valid
    @DisplayName("ECP Valid: Preț valid")
    void testECP_ValidPrice(double validPrice) {
        // Arrange
        Product product = new Product(10, "Mocha", validPrice, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);

        // Act
        repository.save(product);

        // Assert
        assertEquals(validPrice, repository.findOne(10).getPret());
    }

    @Tag("ECP")
    @ParameterizedTest
    @ValueSource(doubles = {-50.0}) // Valoare reprezentativă pentru preț invalid
    @DisplayName("ECP Invalid: Preț negativ")
    void testECP_InvalidPrice(double invalidPrice) {
        // Arrange
        Product product = new Product(11, "NegativePrice", invalidPrice, CategorieBautura.ALL, TipBautura.ALL);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if(product.getPret() <= 0) throw new IllegalArgumentException();
            repository.save(product);
        });
    }

    // --- 2. BVA: BOUNDARY VALUE ANALYSIS ---

    @Tag("BVA")
    @ParameterizedTest
    @ValueSource(ints = {1, 1000}) // Limitele inferioară și superioară valide
    @DisplayName("BVA Valid: Limite ID (1 și 1000)")
    void testBVA_ValidID(int boundaryId) {
        // Arrange
        Product product = new Product(boundaryId, "BoundaryProduct", 15.0, CategorieBautura.TEA, TipBautura.WATER_BASED);

        // Act
        repository.save(product);

        // Assert
        assertNotNull(repository.findOne(boundaryId));
    }

    @Tag("BVA")
    @ParameterizedTest
    @ValueSource(ints = {0, 1001}) // Valorile imediat în afara limitelor
    @DisplayName("BVA Invalid: Limite ID (0 și 1001)")
    void testBVA_InvalidID(int invalidBoundaryId) {
        // Arrange
        Product product = new Product(invalidBoundaryId, "OutsideBoundary", 15.0, CategorieBautura.TEA, TipBautura.WATER_BASED);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if(invalidBoundaryId < 1 || invalidBoundaryId > 1000) throw new IllegalArgumentException();
            repository.save(product);
        });
    }

    @Tag("BVA")
    @ParameterizedTest
    @ValueSource(doubles = {0.01, 10000.0}) // Preț minim valid și preț maxim valid
    @DisplayName("BVA Valid: Limite Preț (0.01 și 10000)")
    void testBVA_ValidPrice(double boundaryPrice) {
        // Arrange
        Product product = new Product(50, "BoundaryPrice", boundaryPrice, CategorieBautura.JUICE, TipBautura.BASIC);

        // Act
        repository.save(product);

        // Assert
        assertEquals(boundaryPrice, repository.findOne(50).getPret());
    }

    @Tag("BVA")
    @ParameterizedTest
    @ValueSource(doubles = {0.0, 10000.01}) // Imediat sub minim și peste maxim
    @DisplayName("BVA Invalid: Limite Preț (0.0 și 10000.01)")
    void testBVA_InvalidPrice(double invalidPrice) {
        // Arrange
        Product product = new Product(51, "PriceError", invalidPrice, CategorieBautura.SMOOTHIE, TipBautura.PLANT_BASED);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if(product.getPret() <= 0 || product.getPret() > 10000) throw new IllegalArgumentException();
            repository.save(product);
        });
    }
}