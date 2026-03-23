package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.RepositoryFactory;
import drinkshop.service.DrinkShopService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the DrinkShop system.
 * Manages initialization using the Factory pattern for repository creation.
 */
public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ---------- Initializare Repository-uri prin Factory (Design Pattern: Factory Method) ----------
        Repository<Integer, Product> productRepo = RepositoryFactory.createProductRepository("data/products.txt");
        Repository<Integer, Order> orderRepo = RepositoryFactory.createOrderRepository("data/orders.txt", productRepo);
        Repository<Integer, Reteta> retetaRepo = RepositoryFactory.createRetetaRepository("data/retete.txt");
        Repository<Integer, Stoc> stocRepo = RepositoryFactory.createStocRepository("data/stocuri.txt");

        // ---------- Initializare Service (Façade Pattern) ----------
        DrinkShopService service = new DrinkShopService(productRepo, orderRepo, retetaRepo, stocRepo);

        // ---------- Incarcare FXML ----------

        FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
        Scene scene = new Scene(loader.load());

        // ---------- Setare Service in Controller ----------
        DrinkShopController controller = loader.getController();
        controller.setService(service);

        // ---------- Afisare Fereastra ----------
        stage.setTitle("Coffee Shop Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}