package todolist;

import java.sql.*;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    // Створити підключення до бази даних
    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,
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

    public ResultSet getAllTasks() throws SQLException, ClassNotFoundException {
        String select = "SELECT task_name FROM " + Const.TASK_TABLE;
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
