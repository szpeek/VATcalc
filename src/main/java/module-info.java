module com.example.vatcalc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.vatcalc to javafx.fxml;
    exports com.example.vatcalc;
}