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
    private ObservableList<String> tasks = FXCollections.observableArrayList();
    private String selectedTask;

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
        if (addButton.getText().equals("Додати")) {
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
        } else {
            update(); // виклик методу оновлення запису
        }

    }

    // Видалити задачу з бази даних
    private void deleteTask() throws SQLException, ClassNotFoundException {
        selectedTask = tasksListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Видалення задачі");
            alert.setHeaderText(selectedTask);
            alert.setContentText("Ви дійсно бажаєте видалити цю задачу?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.deleteTask(selectedTask);
                alert = new Alert(Alert.AlertType.INFORMATION, "Задача успішно видалена!", ButtonType.CLOSE);
                alert.setTitle("Видалення задачі");
                alert.setHeaderText("Задачу видалено!");
                alert.showAndWait();
                populateTaskList();
            } else {
                return;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Спочатку оберіть задачу для видалення", ButtonType.OK);
            alert.setTitle("Помилка видалення задачі");
            alert.setHeaderText("Не обрано задачу для видалення!");
            alert.showAndWait();
        }
    }

    // Обробка натискання на кнопку "Оновити"
    private void updateTask() throws SQLException, ClassNotFoundException {
        selectedTask = tasksListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskTextField.setText(selectedTask);
            addButton.setText("Оновити");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Спочатку оберіть задачу для редагування", ButtonType.OK);
            alert.setTitle("Помилка редагування задачі");
            alert.setHeaderText("Не обрано задачу для редагування!");
            alert.showAndWait();
        }
    }

    // Оновлення задачі в базі даних
    private void update() throws SQLException, ClassNotFoundException {
        String task = taskTextField.getText();
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.updateTask(selectedTask, task);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Задача успішно змінена!", ButtonType.CLOSE);
        alert.setTitle("Релагування задачі");
        alert.setHeaderText("Задачу змінено!");
        alert.showAndWait();
        addButton.setText("Додати");
        taskTextField.setText("");
        populateTaskList();
    }
}
