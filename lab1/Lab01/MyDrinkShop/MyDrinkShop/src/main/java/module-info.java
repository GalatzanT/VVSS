module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;

    requires org.controlsfx.controls;

    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;
    exports drinkshop.service;

    opens drinkshop.domain to  javafx.base;
    exports drinkshop.domain;
}