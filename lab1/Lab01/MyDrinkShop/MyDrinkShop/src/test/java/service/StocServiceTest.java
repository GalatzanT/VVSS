package service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StocServiceTest {
    
    private StocService stocService;
    private Repository<Integer, Stoc> stocRepository;
    
    @BeforeEach
    void setUp() {
        stocRepository = mock(Repository.class);
        stocService = new StocService(stocRepository);
    }

    // --- WBT (White Box Testing) ---
    @Test
    public void TC01_ValidOrder_addStoc() {
        // Test adding a valid stock entry
        Stoc stoc = new Stoc(1, "Cafea", 100, 10);
        
        stocService.add(stoc);
        
        verify(stocRepository, times(1)).save(stoc);
    }

    @Test
    void TC05_oneIngredient_sufficientStock_returnsTrue() {
        // Setup: Create stock for one ingredient
        List<Stoc> stocks = new ArrayList<>();
        stocks.add(new Stoc(1, "Lapte", 500, 50));
        
        when(stocRepository.findAll()).thenReturn(stocks);
        
        // Create recipe that needs 300 units of milk
        List<IngredientReteta> ingrediente = Arrays.asList(
            new IngredientReteta("Lapte", 300)
        );
        Reteta reteta = new Reteta(1, ingrediente);
        
        // Verify sufficient stock
        assertTrue(stocService.areSuficient(reteta));
    }

    // --- Integration Tests ---
    @Test
    void TC1_add_product_stub() {
        // Test add operation with stub
        Stoc stoc = new Stoc(2, "Zahar", 200, 20);
        
        stocService.add(stoc);
        
        verify(stocRepository).save(stoc);
    }

    @Test
    void TC2_add_and_update_repo() {
        // Test add and update operations
        Stoc stoc = new Stoc(3, "Ciocolata", 150, 15);
        
        stocService.add(stoc);
        
        stoc.setCantitate(100);
        stocService.update(stoc);
        
        verify(stocRepository).save(stoc);
        verify(stocRepository).update(stoc);
    }

    @Test
    void TC3_delete_product_repo() {
        // Test delete operation
        int stocId = 4;
        
        stocService.delete(stocId);
        
        verify(stocRepository).delete(stocId);
    }

    // --- Boundary Value Analysis (BVA) ---
    @Test
    void BVA_Valid_AddProduct() {
        // BVA: Minimum valid boundary - stock with minimum quantity
        Stoc stoc1 = new Stoc(5, "Cafea", 1, 0);
        
        stocService.add(stoc1);
        
        verify(stocRepository).save(stoc1);
        
        // BVA: Maximum valid boundary - stock with large quantity
        Stoc stoc2 = new Stoc(6, "Apa", 10000, 100);
        
        stocService.add(stoc2);
        
        verify(stocRepository).save(stoc2);
    }

    @Test
    void BVA_Invalid_AddProduct() {
        // BVA: Boundary condition - stock quantity equals minimum stock
        Stoc stocAtBoundary = new Stoc(9, "Zahar", 50, 50);
        
        stocService.add(stocAtBoundary);
        
        verify(stocRepository).save(stocAtBoundary);
        
        // BVA: Just below minimum stock
        Stoc stocBelowMinimum = new Stoc(10, "Lapte", 49, 50);
        
        stocService.add(stocBelowMinimum);
        
        verify(stocRepository).save(stocBelowMinimum);
    }

    // --- Equivalence Class Partitioning (ECP) ---
    @Test
    void ECP_Valid_AddProduct() {
        // ECP: Valid stock entry with normal values
        Stoc stoc = new Stoc(7, "Ciocolata", 250, 25);
        
        stocService.add(stoc);
        
        verify(stocRepository).save(stoc);
    }

    @Test
    void testAreSuficient_ECP_sufficientStock() {
        // ECP: Sufficient stock case
        List<Stoc> stocks = Arrays.asList(
            new Stoc(1, "Cafea", 500, 10),
            new Stoc(2, "Lapte", 400, 20)
        );
        when(stocRepository.findAll()).thenReturn(stocks);
        
        List<IngredientReteta> ingrediente = Arrays.asList(
            new IngredientReteta("Cafea", 100),
            new IngredientReteta("Lapte", 150)
        );
        Reteta reteta = new Reteta(1, ingrediente);
        
        assertTrue(stocService.areSuficient(reteta));
    }

    @Test
    void ECP_Invalid_AddProduct() {
        // ECP: Invalid case - test with boundary conditions
        Stoc stoc = new Stoc(8, "Zahar", 0, 10);
        
        stocService.add(stoc);
        
        verify(stocRepository).save(stoc);
    }

    @Test
    void testAreSuficient_ECP_insufficientStock() {
        // ECP: Insufficient stock case
        List<Stoc> stocks = Arrays.asList(
            new Stoc(1, "Cafea", 50, 10)
        );
        when(stocRepository.findAll()).thenReturn(stocks);
        
        List<IngredientReteta> ingrediente = Arrays.asList(
            new IngredientReteta("Cafea", 100)
        );
        Reteta reteta = new Reteta(1, ingrediente);
        
        assertFalse(stocService.areSuficient(reteta));
    }

    @Test
    void testConsuma_validRecipe() {
        // Test consuming stock for a valid recipe
        List<Stoc> stocks = new ArrayList<>();
        stocks.add(new Stoc(1, "Zahar", 500, 50));
        stocks.add(new Stoc(2, "Cafea", 300, 30));
        
        when(stocRepository.findAll()).thenReturn(stocks);
        
        List<IngredientReteta> ingrediente = Arrays.asList(
            new IngredientReteta("Zahar", 100),
            new IngredientReteta("Cafea", 50)
        );
        Reteta reteta = new Reteta(1, ingrediente);
        
        stocService.consuma(reteta);
        
        verify(stocRepository, atLeast(1)).update(any(Stoc.class));
    }

    @Test
    void testConsuma_insufficientStock_throwsException() {
        // Test consuming with insufficient stock throws exception
        List<Stoc> stocks = Arrays.asList(
            new Stoc(1, "Cafea", 30, 10)
        );
        when(stocRepository.findAll()).thenReturn(stocks);
        
        List<IngredientReteta> ingrediente = Arrays.asList(
            new IngredientReteta("Cafea", 100)
        );
        Reteta reteta = new Reteta(1, ingrediente);
        
        assertThrows(IllegalStateException.class, () -> stocService.consuma(reteta));
    }

    @Test
    void testGetAll() {
        // Test retrieving all stock entries
        List<Stoc> stocks = Arrays.asList(
            new Stoc(1, "Cafea", 100, 10),
            new Stoc(2, "Lapte", 200, 20)
        );
        when(stocRepository.findAll()).thenReturn(stocks);
        
        List<Stoc> result = stocService.getAll();
        
        assertEquals(2, result.size());
        verify(stocRepository).findAll();
    }
}