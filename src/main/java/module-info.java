module com.anywherecreative.intellisweeper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.anywherecreative.intellisweeper to javafx.fxml;
    exports com.anywherecreative.intellisweeper;
}