module org.museum_app.museum {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires mysql.connector.j;
    requires java.sql;
    requires java.naming;
    requires java.xml;

    opens org.museum_app.museum to javafx.fxml;
    opens org.museum_app.museum.view to javafx.fxml;
    opens org.museum_app.museum.presenter to javafx.fxml;
    opens org.museum_app.museum.model to org.hibernate.orm.core;
    opens org.museum_app.museum.model.repository to org.hibernate.orm.core;
    opens org.museum_app.museum.fxml to javafx.fxml;

    exports org.museum_app.museum;
    exports org.museum_app.museum.view;
    exports org.museum_app.museum.presenter;

}