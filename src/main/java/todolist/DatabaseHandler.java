package todolist;

import java.sql.*;

public class DatabaseHandler extends Configs {

    // Створити підключення до бази даних
    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);

        return dbConnection;
    }

    // Додати нову задачу
    public void addTask(Task task) {
        String insert = "INSERT INTO " + Const.TASK_TABLE + "(" +
                Const.TASK_NAME + ")" + "VALUES(?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Отримати список всіх задач з бази даних
    public ResultSet getAllTasks() throws SQLException, ClassNotFoundException {
        String select = "SELECT " + Const.TASK_NAME + " FROM " + Const.TASK_TABLE;
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    // Видалити задачу з бази даних
    public void deleteTask(String selectedTask) {
        String delete = "DELETE FROM " + Const.TASK_TABLE + " WHERE + " + Const.TASK_NAME + " = ('" + selectedTask + "')" ;
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Оновити задачу в базі даних
    public void updateTask(String selectedTask, String newTask) {
        String upd = "UPDATE todo SET task_name = '" + newTask + "' WHERE task_name = '" + selectedTask + "'";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(upd);
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
