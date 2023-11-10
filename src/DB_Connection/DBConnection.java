package DB_Connection;

import javafx.scene.control.Alert;
import model.*;
import model.staticType.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DBConnection {

    private static String url = "jdbc:mysql://localhost:3307/upeksha_communication";
    private static String userName = "root";
    private static String password = "12345";
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

//            int userId, String userName, String email, String password, int title, boolean banded
//            System.out.println(getInstance().addUser(new User("nipun", "nipun@gmail.com",
//                    "12345", 2, false)));

            connection.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    // Create ----------------------------------------------------------------------------------------------------------
    public boolean addItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (item_name, user_id, set_or_reset_date, set_or_reset_time, stop_selling) " +
                "VALUES (?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // Set the values for the placeholders
        preparedStatement.setString(1, item.getItemName());
        preparedStatement.setInt(2, item.getUserId());
        preparedStatement.setDate(3, item.getDate());
        preparedStatement.setTime(4, item.getTime());
        preparedStatement.setBoolean(5, item.isStopSelling());

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean addStock(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock (user_id, item_id, quantity, refill_quantity, price, selling_price, " +
                "refill_date, refill_time) VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
    }

    public boolean addReturnStock(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock (stock_id, user_id, item_id, quantity, refill_quantity, " +
                "price, selling_price, refill_date, refill_time) VALUES (?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // Set the values for the placeholders
        preparedStatement.setInt(1, stock.getStockId());
        preparedStatement.setInt(2, stock.getUserId());
        preparedStatement.setInt(3, stock.getItemId());
        preparedStatement.setInt(4, stock.getQuantity());
        preparedStatement.setInt(5, stock.getRefillQuantity());
        preparedStatement.setDouble(6, stock.getPrice());
        preparedStatement.setDouble(7, stock.getSellingPrice());
        preparedStatement.setDate(8, stock.getLastRefillDate());
        preparedStatement.setTime(9, stock.getLastRefillTime());

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean addLog(Log log) throws SQLException {
        String sql = "INSERT INTO log (user_id, log_name, log_date, log_time, amount, log_type, " +
                "income_and_expenses_type) VALUES (?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // Set the values for the placeholders
        preparedStatement.setInt(1, log.getUserId());
        preparedStatement.setString(2, log.getLogName());
        preparedStatement.setDate(3, log.getDate());
        preparedStatement.setTime(4, log.getTime());
        preparedStatement.setDouble(5, log.getAmount());
        preparedStatement.setInt(6, getLogTypes(log.getLogType()));
        preparedStatement.setInt(7, getIncomeOrExpense(log.getIncomeOrExpensesType()));

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean addSell(Sell sell) throws SQLException {
        String sql = "INSERT INTO sells (sale_id, bill_number, item_id, stock_id, discount, " +
                "sale_amount, profit, quantity, edit, returns) VALUES (?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, sell.getSellId());
        preparedStatement.setString(2, sell.getBillNumber());
        preparedStatement.setInt(3, sell.getItemId());
        preparedStatement.setInt(4, sell.getStockId());
        preparedStatement.setDouble(5, sell.getDiscount());
        preparedStatement.setDouble(6, sell.getPrice());
        preparedStatement.setDouble(7, sell.getProfit());
        preparedStatement.setInt(8, sell.getQuantity());
        preparedStatement.setBoolean(9, sell.isEdited());
        preparedStatement.setBoolean(10, sell.isReturns());

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean addBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (bill_number, user_id, discount, total_price, order_date, order_time, returns) " +
                "VALUES (?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // Set the values for the placeholders
        preparedStatement.setString(1, bill.getBillNumber());
        preparedStatement.setInt(2, bill.getUserId());
        preparedStatement.setDouble(3, bill.getDiscount());
        preparedStatement.setDouble(4, bill.getPrice());
        preparedStatement.setDate(5, bill.getDate());
        preparedStatement.setTime(6, bill.getTime());
        preparedStatement.setBoolean(7, bill.isReturns());

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (user_name, email, password, title, banded) VALUES (?,?,?,?,?);";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // Set the values for the placeholders
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setInt(4, user.getTitle());
        preparedStatement.setBoolean(5, user.isBanded());

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }


    // Update ----------------------------------------------------------------------------------------------------------
    public boolean updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET item_name = ?, user_id = ?, set_or_reset_date = ?, set_or_reset_time = ?, " +
                "stop_selling = ? WHERE item_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // Set the values for the placeholders
        preparedStatement.setString(1, item.getItemName());
        preparedStatement.setInt(2, item.getUserId());
        preparedStatement.setDate(3, item.getDate());
        preparedStatement.setTime(4, item.getTime());
        preparedStatement.setBoolean(5, item.isStopSelling());
        preparedStatement.setInt(6, item.getItemId());

        // Execute the query
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateStock(Stock stock) throws SQLException {
        String sql = "UPDATE stock " +
                "SET user_id = ?, item_id = ?, quantity = ?, refill_quantity = ?, price = ?, " +
                "selling_price = ?, refill_date = ?, refill_time = ? " +
                "WHERE stock_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
    }

    public boolean updateStock(Sell sell, String bill_number) throws SQLException {
        String sql = "UPDATE stock SET quantity = quantity + ?, WHERE stock_id = ? " +
                "AND item_id = ? AND price = ?;";

        double price = (sell.getProfit() - (sell.getPrice() + sell.getDiscount()));

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sell.getQuantity());
        preparedStatement.setInt(2, sell.getStockId());
        preparedStatement.setInt(3, sell.getItemId());
        preparedStatement.setDouble(4, price);

        if(preparedStatement.executeUpdate() > 0) {
            return true;

        } else {
            Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            Time time = new Time(Calendar.getInstance().getTime().getTime());

            if(addReturnStock(new Stock(sell.getStockId(), getBillAddedUserId(bill_number), sell.getItemId(),
                    sell.getQuantity(), 0, price, sell.getPrice() +
                    sell.getDiscount(), date, time))) {
                return true;

            } else {
                return addStock(new Stock(0, getBillAddedUserId(bill_number), sell.getItemId(),
                        sell.getQuantity(), 0, price, sell.getPrice() +
                        sell.getDiscount(), date, time));
            }
        }
    }

    public boolean updateRemoveStock(int stockId, int quantity) throws SQLException {
        String sql = "UPDATE stock SET quantity = quantity - ? WHERE stock_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, quantity);
        preparedStatement.setInt(2, stockId);

        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateAddStock(int stockId, int quantity) throws SQLException {
        String sql = "UPDATE stock SET quantity = quantity + ? WHERE stock_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, quantity);
        preparedStatement.setInt(2, stockId);

        return preparedStatement.executeUpdate() > 0;
    }

    public boolean changeUserPassword(int userId, String password) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, password);
        preparedStatement.setInt(2, userId);

        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateUserDetails(User user) throws SQLException {
        String sql = "UPDATE users SET user_name = ?, email = ?, password = ? WHERE user_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setInt(4, user.getUserId());

        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateAllUserDetails(User user) throws SQLException {
        String sql = "UPDATE users SET user_name = ?, email = ?, password = ?, title = ? WHERE user_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setInt(4, user.getTitle());
        preparedStatement.setInt(5, user.getUserId());

        return preparedStatement.executeUpdate() > 0;
    }

    public boolean updateSellingItemStopOrNot(int itemId, boolean value) throws SQLException {
        String sql = "UPDATE items SET stop_selling = ? WHERE item_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, value);
        preparedStatement.setInt(2, itemId);
        return preparedStatement.executeUpdate() > 0;
    }

    public boolean bandedUser(int userId) throws SQLException {
        return stm.executeUpdate("UPDATE users SET banded = true WHERE user_id = " + userId + ";") > 0;
    }

    public boolean unbrandedUser(int userId) throws SQLException {
        return stm.executeUpdate("UPDATE users SET banded = false WHERE user_id = " + userId + ";") > 0;
    }


    // Retrieve --------------------------------------------------------------------------------------------------------
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private int getBillAddedUserId(String billNumber) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT user_id FROM bills WHERE bill_number = " + billNumber +";");

        if(reset.next()) {
            return reset.getInt("user_id");
        }
        return -1;
    }

    public int getUserRoll(String userName, String password) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT title FROM users WHERE user_name = '" + userName +"' " +
                "AND password = '" + password +"';");
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
                reset = stm.executeQuery("SELECT COUNT(DISTINCT s.item_id) AS item_count FROM stock s " +
                        "INNER JOIN items i ON s.item_id = i.item_id WHERE s.quantity > 0 AND i.stop_selling = 0;");
                break;

            case AVAILABLE_ITEM_TABLE:
                reset = stm.executeQuery("SELECT COUNT(*) FROM items WHERE stop_selling <> true;");
                break;

            case USER_TABLE:
                reset = stm.executeQuery("SELECT COUNT(*) FROM users;");
                break;

            case BILL_TABLE:
                reset = stm.executeQuery("SELECT COUNT(*) FROM bills;");
                break;
        }

        return (reset != null && reset.next()) ? reset.getInt(1) : 0;
    }

    public ArrayList<Item> getItemTableWithStockAvailable(int itemCount) throws SQLException {
        ResultSet reset;

        if(itemCount > 0) {
            reset = stm.executeQuery("SELECT DISTINCT s.item_id FROM stock s INNER JOIN items i ON " +
                    "s.item_id = i.item_id WHERE s.quantity > 0 AND i.stop_selling = 0 LIMIT 25 OFFSET"
                    + itemCount +";");
        } else {
            reset = stm.executeQuery("SELECT DISTINCT s.item_id FROM stock s INNER JOIN items i ON " +
                    "s.item_id = i.item_id WHERE s.quantity > 0 AND i.stop_selling = 0 LIMIT 25;");
        }

        ArrayList<Item> items = new ArrayList<>();

        while(reset.next()) {
            items.add(new Item(reset.getInt("item_id")));
        }

        for (Item i : items) {
            i.setItemName(getItemName(i.getItemId()));
            ArrayList<Stock> stocks = getStocks(i.getItemId());

            for (Stock s : stocks) {
                if(s.getQuantity() > 0) {
                    i.addStock(new Stock(s.getStockId(), s.getItemId(), s.getQuantity(),
                            s.getPrice(), s.getSellingPrice()));
                }
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
                    reset.getTime("set_or_reset_time"), reset.getBoolean("stop_selling")));
        }

        return items;
    }

    public ArrayList<Item> getAvailableItemTable(int availableItemCount) throws SQLException {
        ResultSet reset;

        if(availableItemCount > 0) {
            reset = stm.executeQuery("SELECT * FROM items WHERE stop_selling <> true LIMIT 25 OFFSET " +
                    availableItemCount +";");
        } else {
            reset = stm.executeQuery("SELECT * FROM items WHERE stop_selling <> true LIMIT 25;");
        }

        ArrayList<Item> items = new ArrayList<>();

        while(reset.next()) {
            items.add(new Item(reset.getInt("item_id"), reset.getString("item_name"),
                    reset.getInt("user_id"), reset.getDate("set_or_reset_date"),
                    reset.getTime("set_or_reset_time"), reset.getBoolean("stop_selling")));
        }

        return items;
    }

    public ArrayList<User> getUsersTable(int userCount) throws SQLException {
        ResultSet reset;

        if(userCount > 0) {
            reset = stm.executeQuery("SELECT * FROM users LIMIT 25 OFFSET " + userCount +";");
        } else {
            reset = stm.executeQuery("SELECT * FROM users LIMIT 25;");
        }

        ArrayList<User> users = new ArrayList<>();

        while(reset.next()) {
            users.add(new User(reset.getInt("user_id"), reset.getString("user_name"),
                    reset.getString("email"), reset.getInt("title"),
                    reset.getBoolean("banded")));
        }

        return users;
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

    public ArrayList<Bill> getBillTableDesc(int billCount) throws SQLException {
        ResultSet reset;

        if(billCount > 0) {
            reset = stm.executeQuery("SELECT * FROM bills ORDER BY bill_number DESC LIMIT 25 OFFSET "
                    + billCount +";");
        } else {
            reset = stm.executeQuery("SELECT * FROM bills ORDER BY bill_number DESC LIMIT 25;");
        }

        ArrayList<Bill> bills = new ArrayList<>();

        while(reset.next()) {
            bills.add(new Bill(reset.getString("bill_number"), reset.getInt("user_id"),
                    reset.getDouble("total_price"), reset.getDouble("discount"),
                    reset.getDate("order_date"), reset.getTime("order_time"),
                    reset.getBoolean("returns")));
        }

        return bills;
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
        ResultSet reset = stm.executeQuery("SELECT stock_id FROM stock WHERE item_id = " + itemId + " " +
                "&& price = " + price + ";");

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

    public String getLastBillNumber() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT MAX(CAST(SUBSTRING(bill_number, 2) AS UNSIGNED)) " +
                "AS last_id_number FROM bills WHERE bill_number LIKE 'B%';");

        if(reset.next()) {
            return "B" + reset.getString("last_id_number");
        }
        return "B";
    }

    public String getLastSellID() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT MAX(CAST(SUBSTRING(sale_id, 2) AS UNSIGNED)) " +
                "AS last_id_number FROM sells WHERE sale_id LIKE 'S%';");

        if(reset.next()) {
            return "S" + reset.getString("last_id_number");
        }
        return "S";
    }

    public int getLastStockID() throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT stock_id FROM stock ORDER BY stock_id DESC LIMIT 1;");

        if(reset.next()) {
            return reset.getInt("stock_id");
        }
        return -1;
    }

    public String getUserEmail(int userId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT email FROM users WHERE user_id=" + userId +";");

        if(reset.next()) {
            return reset.getString("email");
        }
        return null;
    }

    public int getStockQuantity(int stockId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT quantity FROM stock WHERE stock_id=" + stockId + ";");

        if(reset.next()) {
            return reset.getInt("quantity");
        }
        return -1;
    }

    public String getUserPassword(int userId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT password FROM users WHERE user_id=" + userId + ";");

        if(reset.next()) {
            return reset.getString("password");
        }
        return null;
    }

    public ArrayList<Sell> getSells(String billNumber) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT * FROM sells WHERE bill_number = " + billNumber + ";");
        ArrayList<Sell> sells = new ArrayList<>();

        while(reset.next()) {
            sells.add(new Sell(reset.getString("sale_id"), billNumber, reset.getInt("item_id"),
                    reset.getInt("stock_id"), reset.getDouble("discount"),
                    reset.getDouble("sale_amount"), reset.getDouble("profit"),
                    reset.getInt("quantity"), reset.getBoolean("edit"),
                    reset.getBoolean("returns")));
        }

        return sells;
    }

    public double getIncome(IncomeDayTypes incomeDayTypes, IncomeOrExpensesTypes incomeOrExpensesTypes) throws SQLException {
        String sql = "SELECT ";

        switch (incomeOrExpensesTypes) {
            case ALL:
                sql += "    (SUM(CASE WHEN income_and_expenses_type IN (1, 2) THEN amount ELSE 0 END) - " +
                        "     SUM(CASE WHEN income_and_expenses_type IN (3, 4, 5, 6) THEN amount ELSE 0 END))";
                break;

            case INCOME:
                sql += "SUM(CASE WHEN income_and_expenses_type IN (1, 2) THEN amount ELSE 0 END)";
                break;

            case EXPENSES:
                sql += "SUM(CASE WHEN income_and_expenses_type IN (3, 4, 5, 6) THEN amount ELSE 0 END)";
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


    // Delete ----------------------------------------------------------------------------------------------------------
    public boolean deleteStock(int stockId) throws SQLException {
        String sql = "DELETE FROM stock WHERE stock_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, stockId);
        return preparedStatement.executeUpdate() > 0;
    }

    public void deleteLastSells(int itemCount) throws SQLException {
        stm.executeUpdate("DELETE FROM sells ORDER BY sale_id DESC LIMIT " + itemCount + ";");
    }

    public void deleteLastBill() throws SQLException {
        stm.executeUpdate("DELETE FROM bills ORDER BY bill_number DESC LIMIT 1;");
    }

    public boolean deleteSellEdit(String sellId) throws SQLException {
        String sql = "DELETE FROM sell_edits WHERE sale_id = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, sellId);

        return preparedStatement.executeUpdate() > 0;
    }

    public void deleteIllegalUsers(int maximumUserCount) throws SQLException {
        stm.executeUpdate("DELETE FROM users WHERE user_id NOT IN (" +
                "    SELECT user_id FROM (" +
                "             SELECT user_id FROM users ORDER BY user_id LIMIT " + maximumUserCount +
                "    ) AS banned" + ");");
    }


    // Returns ---------------------------------------------------------------------------------------------------------
    public boolean setReturnBill(String billNumber) throws SQLException {
        if(setReturnAllSells(billNumber)) {
            String sql = "UPDATE bills SET returns = 1 WHERE bill_number = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, billNumber);
            return preparedStatement.executeUpdate() > 0;
        }

        return false;
    }

    public boolean setReturnAllSells(String billNumber) throws SQLException {
        ArrayList<Sell> sells = getSells(billNumber);

        for (Sell s : sells) {
            if(!deleteSellEdit(s.getSellId()) || !updateStock(s, billNumber)) {
                return false;
            }
        }

        String sql = "UPDATE sells SET returns = 1 WHERE bill_number = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, billNumber);
        return preparedStatement.executeUpdate() > 0;
    }


    // Check -----------------------------------------------------------------------------------------------------------
    public boolean checkUserLogin(String userName, String password) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE user_name = ? AND password = ? AND banded <> true";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("count") == 1;
        }
        return false;
    }

    public boolean checkItemAndPrice(int itemId, double price) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM stock WHERE item_id = ? AND price = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, itemId);
        preparedStatement.setDouble(2, price);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("count") == 0;
        }

        return false;
    }

    public boolean userNameIsAvailable(String userName, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE user_name = ? AND user_id <> ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);
        preparedStatement.setInt(2, userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return !(resultSet.getInt("count") == 0);
        }

        return true;
    }

    public boolean userNameIsAvailable(String userName) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE user_name = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userName);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return !(resultSet.getInt("count") == 0);
        }

        return true;
    }

    public boolean mailIsAvailable(String mail, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE email = ? AND user_id <> ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, mail);
        preparedStatement.setInt(2, userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return !(resultSet.getInt("count") == 0);
        }

        return true;
    }

    public boolean mailIsAvailable(String mail) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE email = ?;";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, mail);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()) {
            return !(resultSet.getInt("count") == 0);
        }

        return true;
    }

    public boolean isBandedUser(int userId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT banded FROM users WHERE user_id = " + userId +";");
        return reset.next() && reset.getBoolean("banded");
    }

    public boolean userIdIsAvailable(int userId) throws SQLException {
        ResultSet reset = stm.executeQuery("SELECT COUNT(*) AS count FROM users WHERE user_id = " + userId +";");

        if(reset.next()) {
            return reset.getInt("count") > 0;
        }
        return false;
    }


    // Close connection ------------------------------------------------------------------------------------------------
    public void closeConnection() throws SQLException {
        connection.close();
    }


    // Getters ---------------------------------------------------------------------------------------------------------
    public static String getUrl() {
        return url;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public int getLogTypes(LogTypes log) {
        switch (log) {
            case ERROR:
                return 1;

            case WARNING:
                return 2;

            case INFORMATION:
                return 3;
        }

        return -1;
    }

    public int getIncomeOrExpense(IncomeOrExpenseLogTypes type) {
        switch (type) {
            case SELL_INCOME:
                return 1;

            case ADD_MONEY:
                return 2;

            case RETURNS:
                return 3;

            case BUY_ITEMS:
                return 4;

            case WITHDRAW_MONEY:
                return 5;

            case PAY_BILLS:
                return 6;

            case DAMAGE:
                return 7;

            case ERRORS:
                return 8;
        }

        return -1;
    }


    // Setters ---------------------------------------------------------------------------------------------------------
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