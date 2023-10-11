package DB_Connection;

import javafx.scene.control.Alert;
import model.*;
import model.staticType.IncomeDayTypes;
import model.staticType.IncomeOrExpensesTypes;
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
            getInstance().getItemTableWithStockAvailable(25);

            connection.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    // Create

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (item_name, user_id, set_or_reset_date, set_or_reset_time) VALUES (?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setString(1, item.getItemName());
            preparedStatement.setInt(2, item.getUserId());
            preparedStatement.setDate(3, item.getDate());
            preparedStatement.setTime(4, item.getTime());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addStock(Stock stock) {
        String sql = "INSERT INTO stock (user_id, item_id, quantity, refill_quantity, price, selling_price, " +
                "refill_date, refill_time) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setInt(1, stock.getUserId());
            preparedStatement.setInt(2, stock.getItemId());
            preparedStatement.setInt(3, stock.getQuantity());
            preparedStatement.setInt(4, stock.getRefillQuantity());
            preparedStatement.setDouble(5, stock.getPrice());
            preparedStatement.setDouble(6, stock.getSellingPrice());
            preparedStatement.setDate(7, stock.getLastRefillDate());
            preparedStatement.setTime(8, stock.getLastRefillTime());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addLog(Log log) {
        String sql = "INSERT INTO log (user_id, log_name, log_date, log_time, amount, log_type, " +
                "income_and_expenses_type) VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setInt(1, log.getUserId());
            preparedStatement.setString(2, log.getLogName());
            preparedStatement.setDate(3, log.getDate());
            preparedStatement.setTime(4, log.getTime());
            preparedStatement.setDouble(5, log.getAmount());
            preparedStatement.setInt(6, log.getLogType());
            preparedStatement.setInt(7, log.getIncomeOrExpenses());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addSell(Sell sell) {
        String sql = "INSERT INTO sells (user_id, item_id, sale_date, sale_time, discount, sale_amount, " +
                "quantity, edit) VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setInt(1, sell.getUserId());
            preparedStatement.setInt(2, sell.getItemId());
            preparedStatement.setDate(3, sell.getSellDate());
            preparedStatement.setTime(4, sell.getSellTime());
            preparedStatement.setDouble(5, sell.getDiscount());
            preparedStatement.setDouble(6, sell.getPrice());
            preparedStatement.setInt(7, sell.getQuantity());
            preparedStatement.setBoolean(8, sell.isEdited());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addBill(Bill bill) {
        String sql = "INSERT INTO bills (user_id, discount, sale_amount, order_date, order_time) " +
                "VALUES (?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            preparedStatement.setInt(1, bill.getUserId());
            preparedStatement.setDouble(2, bill.getDiscount());
            preparedStatement.setDouble(3, bill.getPrice());
            preparedStatement.setDate(4, bill.getDate());
            preparedStatement.setTime(5, bill.getTime());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }


    // Update

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET item_name = ?, user_id = ?, set_or_reset_date = ?, set_or_reset_time = ? " +
                "WHERE item_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            // Set the values for the placeholders
            preparedStatement.setString(1, item.getItemName());
            preparedStatement.setInt(2, item.getUserId());
            preparedStatement.setDate(3, item.getDate());
            preparedStatement.setTime(4, item.getTime());
            preparedStatement.setInt(5, item.getItemId());

            // Execute the query
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateStock(Stock stock) {
        String sql = "UPDATE stock " +
                "SET user_id = ?, item_id = ?, quantity = ?, refill_quantity = ?, price = ?, " +
                "selling_price = ?, refill_date = ?, refill_time = ? " +
                "WHERE stock_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, stock.getUserId());
            preparedStatement.setInt(2, stock.getItemId());
            preparedStatement.setInt(3, stock.getQuantity());
            preparedStatement.setInt(4, stock.getRefillQuantity());
            preparedStatement.setDouble(5, stock.getPrice());
            preparedStatement.setDouble(6, stock.getSellingPrice());
            preparedStatement.setDate(7, stock.getLastRefillDate());
            preparedStatement.setTime(8, stock.getLastRefillTime());
            preparedStatement.setInt(9, stock.getStockId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }


    // Retrieve

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public int getUserRoll(String userName) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT title FROM users WHERE user_name = '" + userName +"';");
        return reset.next() ? reset.getInt("title") : -1;
    }

    public int getTableRowCount(TableTypes type) throws SQLException {
        ResultSet reset = null;

        switch (type) {
            case STOCK_REFILL_TABLE:
                reset = stm.executeQuery("SELECT COUNT(*) FROM stock WHERE quantity <= refill_quantity;");
                break;

            case STOCK_TABLE:
                reset = stm.executeQuery("SELECT COUNT(*) FROM stock;");
                break;

            case ITEM_TABLE:
                reset = stm.executeQuery("SELECT COUNT(*) FROM items;");
                break;

            case STOCK_AVAILABLE_ITEM_TABLE:
                reset = stm.executeQuery("SELECT COUNT(DISTINCT item_id) AS unique_item_count FROM stock;");
                break;
        }

        return (reset != null && reset.next()) ? reset.getInt(1) : -1;
    }

    public ArrayList<Item> getItemTableWithStockAvailable(int itemCount) throws SQLException {
        ResultSet reset;

        if(itemCount > 0) {
            reset = stm.executeQuery("SELECT DISTINCT item_id FROM stock LIMIT 25 OFFSET " + itemCount +";");
        } else {
            reset = stm.executeQuery("SELECT DISTINCT item_id FROM stock LIMIT 25;");
        }

        ArrayList<Item> items = new ArrayList<>();

        while(reset.next()) {
            items.add(new Item(reset.getInt("item_id")));
        }

        for (Item i : items) {
            i.setItemName(getItemName(i.getItemId()));
            ArrayList<Stock> stocks = getStocks(i.getItemId());

            for (Stock s : stocks) {
                i.addStock(new Stock(s.getStockId(), s.getItemId(), s.getQuantity(),
                        s.getPrice(), s.getSellingPrice()));
            }
        }

        return items;
    }

    private ArrayList<Stock> getStocks(int itemId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT * FROM stock WHERE item_id = " + itemId +";");

        ArrayList<Stock> stocks = new ArrayList<>();

        while (reset.next()) {
            stocks.add(new Stock(reset.getInt("stock_id"), reset.getInt("item_id"),
                    reset.getInt("quantity"), reset.getDouble("price"),
                    reset.getDouble("selling_price")));
        }
        return stocks;
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
            items.add(new Item(reset.getInt("item_id"), reset.getString("item_name"),
                    reset.getInt("user_id"), reset.getDate("set_or_reset_date"),
                    reset.getTime("set_or_reset_time")));
        }

        return items;
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

    public int getUserId(String userName) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT user_id FROM users WHERE user_name = '" + userName +"';");

        if(reset.next()) {
            return reset.getInt("user_id");
        }
        return -1;
    }

    public String getItemName(int itemId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT item_name FROM items WHERE item_id = " + itemId +";");

        if(reset.next()) {
            return reset.getString("item_name");
        }
        return null;
    }

    public int getStockId(int itemId, double price) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT stock_id FROM stock WHERE item_id = " + itemId +" " +
                "&& price = " + price +";");

        if(reset.next()) {
            return reset.getInt("stock_id");
        }
        return -1;
    }

    public double getIncome(String date) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT SUM(sale_amount) - SUM(discount) AS net_sale_amount " +
                "FROM sells WHERE sale_date = '" + date + "';");

        if(reset.next()) {
            return reset.getDouble("net_sale_amount");
        }
        return -1;
    }

    public double getAllStockadeValue() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT SUM(quantity * price) AS total_stock_value FROM stock;");

        if(reset.next()) {
            return reset.getDouble("total_stock_value");
        }
        return -1;
    }

    public double getLockerMoney() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT " +
                "    (SUM(CASE WHEN income_and_expenses_type IN (1, 2) THEN amount ELSE 0 END) - " +
                "     SUM(CASE WHEN income_and_expenses_type IN (3, 4, 5) THEN amount ELSE 0 END)) AS result " +
                "FROM log;");

        if(reset.next()) {
            return reset.getDouble("result");
        }
        return -1;
    }

    public int getLastBillNumber() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT bill_number FROM bills ORDER BY bill_number DESC LIMIT 1;");

        if(reset.next()) {
            return reset.getInt("bill_number");
        }
        return -1;
    }

    public double getIncome(IncomeDayTypes incomeDayTypes, IncomeOrExpensesTypes incomeOrExpensesTypes) throws SQLException {
        String sql = "SELECT ";

        switch (incomeOrExpensesTypes) {
            case ALL:
                sql += "    (SUM(CASE WHEN income_and_expenses_type IN (1, 2) THEN amount ELSE 0 END) - " +
                        "     SUM(CASE WHEN income_and_expenses_type IN (3, 4, 5) THEN amount ELSE 0 END))";
                break;

            case INCOME:
                sql += "SUM(CASE WHEN income_and_expenses_type IN (1, 2) THEN amount ELSE 0 END)";
                break;

            case EXPENSES:
                sql += "SUM(CASE WHEN income_and_expenses_type IN (3, 4, 5) THEN amount ELSE 0 END)";
                break;
        }

        sql += "AS result FROM log ";

        switch (incomeDayTypes) {
            case TODAY:
                sql += "WHERE log_date = CURDATE()";
                break;

            case YESTERDAY:
                sql += "WHERE log_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY);";
                break;

            case THIS_WEEK:
                sql += "WHERE log_date >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY) " +
                        "AND log_date <= DATE_ADD(CURDATE(), INTERVAL 6 - WEEKDAY(CURDATE()) DAY);";
                break;

            case LAST_WEEK:
                sql += "WHERE log_date >= DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 7) DAY) " +
                        "AND log_date <= DATE_SUB(CURDATE(), INTERVAL (WEEKDAY(CURDATE()) + 1) DAY);";
                break;

            case THIS_MONTH:
                sql += "WHERE log_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01') AND log_date <= LAST_DAY(CURDATE());";
                break;

            case LAST_MONTH:
                sql += "WHERE log_date >= DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y-%m-01') " +
                        "AND log_date <= DATE_SUB(DATE_FORMAT(CURDATE(), '%Y-%m-01'), INTERVAL 1 DAY);";
                break;
        }

        ResultSet reset = stm.executeQuery(sql);

        if(reset.next()) {
            return reset.getDouble("result");
        }
        return -1;
    }


    // Delete

    public boolean deleteStock(int stockId) {
        String sql = "DELETE FROM stock WHERE stock_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, stockId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteItem(int itemId) {
        String sql = "DELETE FROM items WHERE item_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, itemId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            return false;
        }
    }


    // Check
    public boolean checkUserLogin(String userName, String password) {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE user_name = ? AND password = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                if(resultSet.getInt("count") == 1) {
                    return true;
                }
            }

        } catch (SQLException e) {
            return false;
        }

        return false;
    }

    public boolean checkItemAndPrice(int itemId, double price) {
        String sql = "SELECT COUNT(*) AS count FROM stock WHERE item_id = ? AND price = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.setDouble(2, price);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                if(resultSet.getInt("count") == 0) {
                    return true;
                }
            }

        } catch (SQLException e) {
            return false;
        }

        return false;
    }


    // Close connection

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