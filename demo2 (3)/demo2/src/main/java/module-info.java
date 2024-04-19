module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires jdk.jpackage;
    requires org.apache.poi.poi;
    requires org.apache.commons.io;
    requires org.apache.poi.ooxml;

    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
}


