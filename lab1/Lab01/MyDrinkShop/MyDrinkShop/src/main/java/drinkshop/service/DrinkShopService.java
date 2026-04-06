package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.reports.DailyReportService;
import drinkshop.repository.Repository;

import java.util.List;

/**
 * Application Controller (Façade) for the DrinkShop system.
 * Manages the business logic and coordinates all services.
 * 
 * Responsibilities:
 * - Product management (CRUD operations)
 * - Current order state management
 * - Order processing and checkout
 * - Recipe and stock management
 * - Report generation and data export
 */
public class DrinkShopService {

    private final ProductService productService;
    private final OrderService orderService;
    private final RetetaService retetaService;
    private final StocService stocService;
    private final DailyReportService report;
    
    // Current session order - managed by the application controller
    private Order currentOrder;

    public DrinkShopService(
            Repository<Integer, Product> productRepo,
            Repository<Integer, Order> orderRepo,
            Repository<Integer, Reteta> retetaRepo,
            Repository<Integer, Stoc> stocService
    ) {
        this.productService = new ProductService(productRepo);
        this.orderService = new OrderService(orderRepo, productRepo);
        this.retetaService = new RetetaService(retetaRepo);
        this.stocService = new StocService(stocService);
        this.report = new DailyReportService(orderRepo);
        this.currentOrder = new Order(1);
    }

    // ---------- PRODUCT MANAGEMENT ----------
    /**
     * Adds a new product to the system.
     */
    public void addProduct(Product p) {
        productService.addProduct(p);
    }

    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        productService.updateProduct(id, name, price, categorie, tip);
    }

    public void deleteProduct(int id) {
        productService.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Filters products by category (association: Product --"belongs to"--> CategorieBautura).
     */
    public List<Product> filtreazaDupaCategorie(CategorieBautura categorie) {
        return productService.filterByCategorie(categorie);
    }

    public List<Product> filtreazaDupaTip(TipBautura tip) {
        return productService.filterByTip(tip);
    }

    // ---------- CURRENT ORDER MANAGEMENT ----------
    /**
     * Gets the current session order.
     * Association: DrinkShopService --"manages"--> Order
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Adds an item to the current order.
     */
    public void addItemToCurrentOrder(OrderItem item) {
        currentOrder.addItem(item);
    }

    /**
     * Removes an item from the current order.
     */
    public void removeItemFromCurrentOrder(OrderItem item) {
        currentOrder.removeItem(item);
    }

    /**
     * Clears all items from the current order.
     */
    public void clearCurrentOrder() {
        currentOrder.getItems().clear();
    }

    /**
     * Computes the total price for the current order.
     */
    public double computeCurrentOrderTotal() {
        return orderService.computeTotal(currentOrder);
    }

    /**
     * Finalizes the current order and starts a new one.
     */
    public void checkoutCurrentOrder() {
        orderService.addOrder(currentOrder);
        currentOrder = new Order(1);
    }

    // ---------- PERSISTENT ORDER OPERATIONS ----------
    public void addOrder(Order o) {
        orderService.addOrder(o);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public double computeTotal(Order o) {
        return orderService.computeTotal(o);
    }

    public String generateReceipt(Order o) {
        return ReceiptGenerator.generate(o, productService.getAllProducts());
    }

    public double getDailyRevenue() {
        return report.getTotalRevenue();
    }

    public void exportCsv(String path) {
        CsvExporter.exportOrders(productService.getAllProducts(), orderService.getAllOrders(), path);
    }

    // ---------- RECIPE + STOCK MANAGEMENT ----------
    /**
     * Checks stock availability and consumes ingredients for a product.
     * Association: Product --"uses recipe"--> Reteta --"contains ingredients"--> IngredientReteta
     */
    public void comandaProdus(Product produs) {
        Reteta reteta = retetaService.findById(produs.getId());

        if (!stocService.areSuficient(reteta)) {
            throw new IllegalStateException("Stoc insuficient pentru produsul: " + produs.getNume());
        }
        stocService.consuma(reteta);
    }

    public List<Reteta> getAllRetete() {
        return retetaService.getAll();
    }

    public void addReteta(Reteta r) {
        retetaService.addReteta(r);
    }

    public void updateReteta(Reteta r) {
        retetaService.updateReteta(r);
    }

    public void deleteReteta(int id) {
        retetaService.deleteReteta(id);
    }
}