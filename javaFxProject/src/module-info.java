module javaFxProject {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;  // Make sure you include javafx.fxml if you're using FXML.

    opens application to javafx.graphics, javafx.fxml;
}
