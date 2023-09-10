package DB_Connection;

import javafx.scene.control.Alert;
import model.Item;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection {

    private static String url = "jdbc:mysql://localhost/upeksha_communication";
    private static String userName = "root";
    private static String password = "1234";
    private static DBConnection instance;
    private static Connection connection;
    private static Statement stm;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, userName, password);

            stm = connection.createStatement();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database Connection Error !\n(" + e.getMessage() + ")" ).show();
//            System.out.println("JDBC Class not found ...!\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, userName, password);

            Statement stm = connection.createStatement();

            ResultSet reset = stm.executeQuery("SELECT * FROM items;");

            connection.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }


    public boolean checkUserLogin(String userName, String password) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT password FROM users WHERE user_name = '" + userName + "';");

        if (reset.next()) {
            return reset.getString(1).equals(password);
        }

        return false;
    }

    public String getItemName(String itemID) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT item_name FROM items WHERE item_id = '" + itemID + "';");

        if (reset.next()) {
            return reset.getString(1);
        }

        return "";
    }

    public ArrayList<Item> getItemTable() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT * FROM items;");

        ArrayList<Item> items = new ArrayList<>();

        while(reset.next()) {
            items.add(new Item(reset.getInt("item_id"), reset.getString("item_name"),
                    10, reset.getDouble("price"),
                    reset.getDouble("selling_price")));
        }

        // reset = stm.executeQuery("SELECT quantity FROM stock WHERE item_id;");
        return items;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (item_name, price, selling_price) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setString(1, item.getItemName());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(item.getPrice()));
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(item.getSellingPrice()));

            // Execute the query
            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException e) {
            return false;
        }

    }

    public void closeConnection() throws SQLException {
        connection.close();
    }


    // Getters
    public static String getUrl() {
        return url;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }


    // Setters
    public static void setUrl(String url) {
        DBConnection.url = url;
    }

    public static void setUserName(String userName) {
        DBConnection.userName = userName;
    }

    public static void setPassword(String password) {
        DBConnection.password = password;
    }

}