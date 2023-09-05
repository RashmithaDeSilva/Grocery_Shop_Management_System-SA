package DB_Connection;

import java.sql.*;

public class DBConnection {

    private static String url = "jdbc:mysql://localhost/upeksha_communication";
    private static String userName = "root";
    private static String password = "";


    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(url, userName, password);

            Statement stm = connection.createStatement();

            ResultSet reset = stm.executeQuery("SELECT * FROM items");

//            while (reset.next()) {
//                System.out.println("ID: " + reset.getInt(1) +
//                        "\nItem name: " + reset.getString(2) +
//                        "\nItem price: " + reset.getDouble(3) +
//                        "\nItem selling price: " + reset.getDouble(4));
//            }

            connection.close();



        } catch (Exception e) {
            System.out.println("JDBC Class not found ...!");
        }
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