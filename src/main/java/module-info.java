module com.example.frogger {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.frogger to javafx.fxml;
    exports com.example.frogger;
}