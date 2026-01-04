module todolist.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens todolist to javafx.fxml;
    exports todolist;
}