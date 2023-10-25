module programm.sandwich.calculater {
    requires javafx.controls;
    requires javafx.fxml;


    opens programm.sandwich.calculater to javafx.fxml;
    exports programm.sandwich.calculater;
}