package DB_Connection;

import javafx.scene.control.Alert;
import model.Item;
import model.Stock;
import model.staticType.TableTypes;

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
            new Alert(Alert.AlertType.ERROR, "Database Connection Error !\n(" + e.getMessage() + ")").show();
//            System.out.println("JDBC Class not found ...!\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, userName, password);

            stm = connection.createStatement();

            //ResultSet reset = stm.executeQuery("SELECT * FROM items;");

//            while(reset.next()) {
//                System.out.println(reset.getRow());
//            }
            getInstance().getItemTable(0);

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

    public int getUserRoll(String userName) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT title FROM users WHERE user_name = '" + userName +"';");
        return reset.next() ? reset.getInt("title") : -1;
    }

    public int getTableRowCount(TableTypes type) throws SQLException {
        ResultSet reset = null;

        switch (type) {
            case StockRefillTable:
                reset = stm.executeQuery("SELECT COUNT(*) FROM stock WHERE quantity <= refill_quantity;");
                break;

            case StockTable:
                reset = stm.executeQuery("SELECT COUNT(*) FROM stock;");
                break;

            case ItemTable:
                reset = stm.executeQuery("SELECT COUNT(*) FROM items;");
                break;
        }

        return (reset != null && reset.next()) ? reset.getInt(1) : -1;
    }

    public ArrayList<Item> getItemTable() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT * FROM items;");

        ArrayList<Item> items = new ArrayList<>();

        while(reset.next()) {
            items.add(new Item(reset.getInt("item_id"), reset.getString("item_name")));
        }

        ArrayList<Stock> stocks = getStockTable();

        for (Item i : items) {
            for (Stock s : stocks) {
                if(i.getItemId() == s.getItemId()) {
                    i.addStock(new Stock(s.getStockId(), s.getItemId(), s.getQuantity(), s.getPrice(), s.getSellingPrice()));
                }
            }
        }

        // reset = stm.executeQuery("SELECT quantity FROM stock WHERE item_id;");
        return items;
    }

    public ArrayList<Item> getItemTable(int itemCount) throws SQLException {
        ResultSet reset;

        if(itemCount > 0) {
            reset = stm.executeQuery("SELECT * FROM items LIMIT 25 OFFSET " + itemCount +";");
        } else {
            reset = stm.executeQuery("SELECT * FROM items LIMIT 25;");
        }

        ArrayList<Item> items = new ArrayList<>();

        while(reset.next()) {
            items.add(new Item(reset.getInt("item_id"), reset.getString("item_name")));
        }

        return items;
    }

    public ArrayList<Stock> getStockTable() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT * FROM stock;");

        ArrayList<Stock> stocks = new ArrayList<>();

        while(reset.next()) {
            stocks.add(new Stock(reset.getInt("stock_id"), reset.getInt("item_id"),
                    reset.getInt("quantity"), reset.getDouble("price"),
                    reset.getDouble("selling_price")));
        }

        return stocks;
    }

    public ArrayList<Stock> getStockTable(int stockCount) throws SQLException {
        ResultSet reset;

        if(stockCount > 0) {
            reset = stm.executeQuery("SELECT * FROM stock LIMIT 25 OFFSET " + stockCount +";");
        } else {
            reset = stm.executeQuery("SELECT * FROM stock LIMIT 25;");
        }

        ArrayList<Stock> stocks = new ArrayList<>();

        while(reset.next()) {
            stocks.add(new Stock(reset.getInt("stock_id"), reset.getInt("user_id"),
                    reset.getInt("item_id"), reset.getInt("quantity"),
                    reset.getInt("refill_quantity"), reset.getDouble("price"),
                    reset.getDouble("selling_price"), reset.getDate("refill_date"),
                    reset.getTime("refill_time")));
        }

        return stocks;
    }

    public ArrayList<Stock> getRefillStockTable(int stockCount) throws SQLException {
        ResultSet reset;

        if(stockCount > 0) {
            String sql = "SELECT stock_id, item_id, quantity, price " +
                    "FROM stock WHERE quantity <= refill_quantity LIMIT 25 OFFSET " + stockCount + ";";
            reset = stm.executeQuery(sql);
        } else {
            reset = stm.executeQuery("SELECT stock_id, item_id, quantity, price " +
                    "FROM stock WHERE quantity <= refill_quantity LIMIT 25;");
        }

        ArrayList<Stock> stocks = new ArrayList<>();

        while(reset.next()) {
            stocks.add(new Stock(reset.getInt("stock_id"), reset.getInt("item_id"),
                    reset.getInt("quantity"), reset.getDouble("price")));
        }

        return stocks;
    }

    public Stock getRefillQuantityAndSellingPrice(Stock stock) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT selling_price, refill_quantity FROM stock WHERE stock_id = "
                + stock.getStockId() +";");

        if(reset.next()) {
            stock.setSellingPrice(reset.getDouble("selling_price"));
            stock.setRefillQuantity(reset.getInt("refill_quantity"));
        }

        return stock;
    }

    public String getUserName(int userId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT user_name FROM users WHERE user_id = " + userId +";");

        if(reset.next()) {
            return reset.getString("user_name");
        }
        return null;
    }

    public String getItemName(int itemId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT item_name FROM items WHERE item_id = " + itemId +";");

        if(reset.next()) {
            return reset.getString("item_name");
        }
        return null;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (item_name) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setString(1, item.getItemName());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_name = ? WHERE item_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            // Set the values for the placeholders
            preparedStatement.setString(1, item.getItemName());
            preparedStatement.setInt(2, item.getItemId());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

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