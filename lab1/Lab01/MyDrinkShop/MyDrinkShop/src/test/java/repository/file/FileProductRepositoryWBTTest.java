package repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tehnici folosite: White-Box Testing (WBT)
 * Criterii: SC, DC, MCC, APC, LC.
 * * CFG (Control Flow Graph) rezumat pentru F02.CFG-Paths:
 * - 1-2-3: Validare entity == null -> Exception
 * - 1-2-4-5/6/7-8: Validare date invalide (pret, nume) -> Exception
 * - 1-2-4-9-10-11-12...15: Salvare cu succes (bucla for rulează)
 * - 1...10-13-14: Eroare la deschidere/scriere fișier -> IOException
 */
@DisplayName("Teste WBT pentru Cerința save() și writeToFile()")
class FileProductRepositoryWBTTest {

    private FileProductRepository repository;
    private final String testFile = "products_wbt_test.txt";

    @BeforeEach
    void setUp() {
        repository = new FileProductRepository(testFile);
    }

    @AfterEach
    void tearDown() {
        File file = new File(testFile);
        if (file.exists() && file.canWrite()) {
            file.delete();
        }
    }

    // --- 2. DECISION / CONDITION COVERAGE (DC) ---
    // Scop: Fiecare condiție a evaluat atât TRUE cât și FALSE.
    @Test
    @Tag("DC")
    @DisplayName("DC: Acoperire ramuri True/False")
    void testDecisionCoverage() {
        // T1: (entity == null) -> True
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));

        // T2: (entity == null) -> False, dar intra în (pret <= 0) -> True
        Product negativePriceProd = new Product(3, "Espresso", -5.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
        assertThrows(IllegalArgumentException.class, () -> repository.save(negativePriceProd));

        // T3: Toate condițiile False -> Ajunge la bucla FOR. Bucla evaluează True (are elemente), apoi False (se termină)
        Product validProd = new Product(4, "Matcha", 20.0, CategorieBautura.TEA, TipBautura.PLANT_BASED);
        Product saved = repository.save(validProd);
        assertNotNull(saved);
    }

    // --- 3. MULTIPLE CONDITION COVERAGE (MCC) ---
    // Scop: Toate combinațiile T/F din: (pret <= 0 || nume == null || nume.isEmpty())
    @Test
    @Tag("MCC")
    @DisplayName("MCC: Testarea combinațiilor logice pentru datele produsului")
    void testMultipleConditionCoverage() {
        // C1: T, C2: X, C3: X (Pret invalid)
        assertThrows(IllegalArgumentException.class, () ->
            repository.save(new Product(5, "Ceai", 0, CategorieBautura.TEA, TipBautura.WATER_BASED)));

        // C1: F, C2: T, C3: X (Nume null)
        assertThrows(IllegalArgumentException.class, () ->
            repository.save(new Product(6, null, 10.0, CategorieBautura.TEA, TipBautura.WATER_BASED)));

        // C1: F, C2: F, C3: T (Nume empty)
        assertThrows(IllegalArgumentException.class, () ->
            repository.save(new Product(7, "   ", 10.0, CategorieBautura.TEA, TipBautura.WATER_BASED)));

        // C1: F, C2: F, C3: F (Valid - Totul e OK)
        assertDoesNotThrow(() ->
            repository.save(new Product(8, "Cappuccino", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY)));
    }

    // --- 4. ALL PATH COVERAGE (APC) ---
    // Scop: Se testează un drum cap-coadă independent (ex. de la nod 1 la 15) fără întreruperi de excepții.
    @Test
    @Tag("APC")
    @DisplayName("APC: Drum independent complet (Succes Workflow)")
    void testAllPathCoverage() {
        // Path: Validare succes -> Apel super.save() -> Deschidere fisier -> For Loop (executat) -> Close fisier -> Return
        Product validPathProduct = new Product(9, "Frappe", 25.0, CategorieBautura.ICED_COFFEE, TipBautura.DAIRY);

        Product result = repository.save(validPathProduct);
        assertEquals(9, result.getId());

        // Verificăm dacă fișierul a fost creat pe acest drum (dovedește trecerea prin try-with-resources)
        File f = new File(testFile);
        assertTrue(f.exists() && f.length() > 0);
    }

    // --- 5. SIMPLE LOOP COVERAGE (LC) ---
    // Scop: Testarea buclei for (0 execuții, 1 execuție, 2 execuții, multe execuții)
    @Test
    @Tag("LC")
    @DisplayName("LC: Acoperirea ciclului simplu (bucla FOR din writeToFile)")
    void testLoopCoverage() {
        // Notă: "0 execuții" (bypassing) nu este posibil strict prin save(),
        // deoarece entitatea tocmai a fost adăugată, deci `entities.values()` are cel puțin 1 element.

        // Cazul 1 execuție: O singură entitate în map (Cea adăugată acum)
        Product prod1 = new Product(10, "Ceai", 5.0, CategorieBautura.TEA, TipBautura.WATER_BASED);
        repository.save(prod1); // For loop execută o iterație
        assertEquals(1, repository.findAll().size()); // Verificare implicită din memorie

        // Cazul 2 execuții: Adăugăm a doua entitate
        Product prod2 = new Product(11, "Suc", 12.0, CategorieBautura.JUICE, TipBautura.PLANT_BASED);
        repository.save(prod2); // For loop execută două iterații (rescrie ambele elemente)

        // Cazul "multe" execuții (ex: 3)
        Product prod3 = new Product(12, "Cafea", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.WATER_BASED);
        repository.save(prod3); // For loop execută 3 iterații

        assertEquals(3, repository.findAll().size());
    }
}
