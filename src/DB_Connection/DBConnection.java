package DB_Connection;

import java.sql.*;

public class DBConnection {

    private static String url = "jdbc:mysql://localhost/upeksha_communication";
    private static String userName = "root";
    private static String password = "";
    private static DBConnection instance;
    private static Statement stm;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, userName, password);

            stm = connection.createStatement();

        } catch (Exception e) {
            System.out.println("JDBC Class not found ...!\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, userName, password);

            Statement stm = connection.createStatement();

            ResultSet reset = stm.executeQuery("SELECT * FROM items");

            connection.close();

        } catch (Exception e) {
            System.out.println("JDBC Class not found ...!");
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