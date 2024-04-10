module ViewProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires ModelProject;
    requires java.logging;
    requires java.sql;

    opens pl.view to javafx.fxml;
    exports pl.view;
}