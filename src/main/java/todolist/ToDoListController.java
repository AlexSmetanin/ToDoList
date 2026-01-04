package todolist;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ToDoListController {
    ObservableList<String> tasks = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField taskTextField;

    @FXML
    private ListView<String> tasksListView;

    @FXML
    private Button updateButton;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        populateTaskList(); // заповнити список задач з БД

        addButton.setOnAction(actionEvent -> {
            try {
                addTask();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        
        deleteButton.setOnAction(actionEvent -> {
            try {
                deleteTask();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        
        updateButton.setOnAction(actionEvent -> {
            try {
                updateTask();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Заповнення списку задач з бази даних
    private void populateTaskList() throws SQLException, ClassNotFoundException {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet result = databaseHandler.getAllTasks();
        tasks.clear();
        while (result.next()) {
            String task = result.getString("task_name");
            tasks.add(task);
        }
        tasksListView.setItems(tasks);
    }

    // Додавання нової задачі в базу даних
    private void addTask() throws SQLException, ClassNotFoundException {
        String task_name = taskTextField.getText().trim();
        if (task_name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Введіть назву задачі, будь ласка.", ButtonType.OK);
            alert.setTitle("Помилка при додаванні задачі");
            alert.setHeaderText("Назва задачі пуста!");
            alert.showAndWait();
        } else {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            Task task = new Task(task_name);
            databaseHandler.addTask(task);
            taskTextField.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Нова задача успішно додана!", ButtonType.CLOSE);
            alert.setTitle("Додавання задачі");
            alert.setHeaderText("Задачу додано!");
            alert.showAndWait();
            populateTaskList();
        }
    }

    // Видалити задачу з бази даних
    private void deleteTask() throws SQLException, ClassNotFoundException {
        String selectedTask = tasksListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.deleteTask(selectedTask);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Задача успішно видалена!", ButtonType.CLOSE);
            alert.setTitle("Видалення задачі");
            alert.setHeaderText("Задачу видалено!");
            alert.showAndWait();
            populateTaskList();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Спочатку оберіть задачу для видалення", ButtonType.OK);
            alert.setTitle("Помилка видалення задачі");
            alert.setHeaderText("Не обрано задачу для видалення!");
            alert.showAndWait();
        }
    }

    private void updateTask() throws SQLException, ClassNotFoundException {
        String selectedTask = tasksListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskTextField.setText(selectedTask);
            addButton.setText("Оновити");

            DatabaseHandler databaseHandler = new DatabaseHandler();
            databaseHandler.updateTask(selectedTask, taskTextField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Задача успішно змінена!", ButtonType.CLOSE);
            alert.setTitle("Релагування задачі");
            alert.setHeaderText("Задачу змінено!");
            alert.showAndWait();
            populateTaskList();
            addButton.setText("Додати");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Спочатку оберіть задачу для редагування", ButtonType.OK);
            alert.setTitle("Помилка редагування задачі");
            alert.setHeaderText("Не обрано задачу для редагування!");
            alert.showAndWait();
        }
    }

}
