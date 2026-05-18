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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class StocServiceTest {
    private StocService stocService;
    private Repository<Integer, Stoc> stocRepository;
    
    @BeforeEach
    void setUp() {
        stocRepository = mock(Repository.class);
        stocService = new StocService(stocRepository);
    }
    
    ///WBT
    @Test
    public void TC01_ValidOrder() {
        Stoc stoc = new Stoc(1, "Cafea", 100, 10);
        stocService.add(stoc);
        verify(stocRepository).save(stoc);
        assertTrue(true);
    }

    @Test
    void TC05_oneIngredient_sufficientStock_returnsTrue() {
        List<Stoc> stocks = new ArrayList<>();
        stocks.add(new Stoc(1, "Lapte", 500, 50));
        when(stocRepository.findAll()).thenReturn(stocks);
        
        List<IngredientReteta> ingrediente = Arrays.asList(
            new IngredientReteta("Lapte", 300)
        );
        Reteta reteta = new Reteta(1, ingrediente);
        
        assertTrue(stocService.areSuficient(reteta));
    }
    //intT
    @Test
    void TC1_add_product_stub() {
        Stoc stoc = new Stoc(2, "Zahar", 200, 20);
        stocService.add(stoc);
        verify(stocRepository).save(stoc);
        assertTrue(true);
    }
    @Test
    void TC2_add_product_repo() {
        Stoc stoc = new Stoc(3, "Ciocolata", 150, 15);
        stocService.add(stoc);
        stoc.setCantitate(100);
        stocService.update(stoc);
        verify(stocRepository).save(stoc);
        verify(stocRepository).update(stoc);
        assertTrue(true);
    }
    @Test
    void TC3_add_product_FullIntegration() {
        int stocId = 4;
        stocService.delete(stocId);
        verify(stocRepository).delete(stocId);
        assertTrue(true);
    }

    //bbt
    @Test
    void testAddProduct_ECP_Valid() {
        Stoc stoc = new Stoc(5, "Ciocolata", 250, 25);
        stocService.add(stoc);
        verify(stocRepository).save(stoc);
        assertTrue(true);
    }
    @Test
    void testAddProduct_ECP_Invalid() {
        Stoc stoc = new Stoc(6, "Zahar", 0, 10);
        stocService.add(stoc);
        verify(stocRepository).save(stoc);
        assertTrue(true);
    }
    @Test
    void testAddProduct_BVA_Valid() {
        Stoc stocMin = new Stoc(7, "Cafea", 1, 0);
        stocService.add(stocMin);
        verify(stocRepository).save(stocMin);
        
        Stoc stocMax = new Stoc(8, "Apa", 10000, 100);
        stocService.add(stocMax);
        verify(stocRepository).save(stocMax);
        assertTrue(true);
    }
    @Test
    void add_product_BVA_Invalid() {
        Stoc stocBoundary = new Stoc(9, "Zahar", 50, 50);
        stocService.add(stocBoundary);
        verify(stocRepository).save(stocBoundary);
        
        Stoc stocBelow = new Stoc(10, "Lapte", 49, 50);
        stocService.add(stocBelow);
        verify(stocRepository).save(stocBelow);
        assertTrue(true);
    }
}