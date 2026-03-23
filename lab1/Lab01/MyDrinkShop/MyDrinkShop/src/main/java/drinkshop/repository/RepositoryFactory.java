package drinkshop.repository;

import drinkshop.domain.*;
import drinkshop.repository.file.*;

/**
 * Factory for creating repository instances.
 * Decouples service layer from specific repository implementations.
 * Supports adding different persistence strategies (database, XML, etc.) without changing services.
 */
public class RepositoryFactory {

    public static Repository<Integer, Product> createProductRepository(String filePath) {
        return new FileProductRepository(filePath);
    }

    public static Repository<Integer, Order> createOrderRepository(String filePath, Repository<Integer, Product> productRepo) {
        return new FileOrderRepository(filePath, productRepo);
    }

    public static Repository<Integer, Reteta> createRetetaRepository(String filePath) {
        return new FileRetetaRepository(filePath);
    }

    public static Repository<Integer, Stoc> createStocRepository(String filePath) {
        return new FileStocRepository(filePath);
    }
}
