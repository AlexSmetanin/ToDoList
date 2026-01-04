package todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ToDoListApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoListApplication.class.getResource("ToDoListForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        //scene.getStylesheets().add("style.css");
        stage.setTitle("ToDoList Application");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
