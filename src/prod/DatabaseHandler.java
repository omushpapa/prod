/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giantas
 */
public class DatabaseHandler {
    
    public static String DB_NAME;
    public static String DB_PATH = "";
    
    public DatabaseHandler (String databaseName, String databasePath) {
        DatabaseHandler.DB_NAME = databaseName;
        this.DB_PATH = databasePath;
    }
    
    public DatabaseHandler (String databaseName) {
        this.DB_NAME = databaseName;
    }
    
    private Connection getConnection() {
        Connection connection;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + DB_PATH + DB_NAME;
            // create a connection to the database
            connection = DriverManager.getConnection(url);
            
            System.out.println("Database connection established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection = null;
        } 
        
        return connection;
    }
    
    public boolean createTable() {
        boolean success = false;
        try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
            // create a new table
            statement.execute(Tables.Prod.CREATE_TABLE_QUERY);
            success = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }
    
    public int insert(String title, String body, Date date) {
        java.sql.Date sqlDate = null;
        if (date != null) {
            sqlDate = new java.sql.Date(date.getTime());
        } 
        
        int success = 0;
        String sql = "INSERT INTO "+ Tables.Prod.TABLE_NAME + "(" + 
                Tables.Prod.COLUMN_TITLE + ", " +
                Tables.Prod.COLUMN_BODY + ", " + 
                Tables.Prod.COLUMN_DATE + ", " +
                Tables.Prod.COLUMN_CREATE_DATE + ") VALUES (?,?,?,?)";
 
        try (Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, body);
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setDate(4, new java.sql.Date(new Date().getTime()));
            success = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }
    
    public int insertTitle(String title) {
        int success = 0;
        String sql = "INSERT INTO "+ Tables.Prod.TABLE_NAME + "(" + 
                Tables.Prod.COLUMN_TITLE + ", " +
                Tables.Prod.COLUMN_CREATE_DATE + ") VALUES (?,?)";
 
        try (Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, new java.sql.Date(new Date().getTime()));
            success = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }
    
    public int insertReminder(Reminder reminder) {
        Date date = reminder.getDateObj();
        java.sql.Date sqlDate = null;
        if (date != null) {
            sqlDate = new java.sql.Date(date.getTime());
        } 
        
        int success = 0;
        String sql = "INSERT INTO "+ Tables.Prod.TABLE_NAME + "(" + 
                Tables.Prod.COLUMN_TITLE + ", " +
                Tables.Prod.COLUMN_BODY + ", " + 
                Tables.Prod.COLUMN_DATE + ", " +
                Tables.Prod.COLUMN_CREATE_DATE + ") VALUES (?,?,?,?)";
 
        try (Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, reminder.getTitle());
            preparedStatement.setString(2, reminder.getBody());
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setDate(4, new java.sql.Date(new Date().getTime()));
            success = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }
    
    public List<Reminder> getReminders() {
        List<Reminder> reminders = new ArrayList<>();
        
        String sql = "SELECT * FROM " + Tables.Prod.TABLE_NAME;
        
        try (Connection conn = getConnection();
             Statement statement  = conn.createStatement();
                ResultSet resultSet    = statement.executeQuery(sql)){
            
            // loop through the result set
            while (resultSet.next()) {
                String title = resultSet.getString(Tables.Prod.COLUMN_TITLE);
                String body = resultSet.getString(Tables.Prod.COLUMN_BODY);
                Date date = resultSet.getDate(Tables.Prod.COLUMN_DATE);
                reminders.add(new Reminder(title, body, date));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    public List<Reminder> getRemindersWithID() {
        List<Reminder> reminders = new ArrayList<>();
        
        String sql = "SELECT rowid,* FROM " + Tables.Prod.TABLE_NAME;
        
        try (Connection conn = getConnection();
             Statement statement  = conn.createStatement();
                ResultSet resultSet    = statement.executeQuery(sql)){
            
            // loop through the result set
            while (resultSet.next()) {
                Integer rowid = resultSet.getInt("rowid");
                String title = resultSet.getString(Tables.Prod.COLUMN_TITLE);
                String body = resultSet.getString(Tables.Prod.COLUMN_BODY);
                Date date = resultSet.getDate(Tables.Prod.COLUMN_DATE);
                Reminder reminder = new Reminder(title, body, date);
                reminder.setRowID(rowid);
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    public List<Reminder> getRemindersOfDate(String stringDate) {
        List<Reminder> reminders = new ArrayList<>();
        
        String sql = "SELECT rowid,* FROM " + Tables.Prod.TABLE_NAME + 
                " WHERE STRFTIME('%d/%m/%Y', " + Tables.Prod.COLUMN_DATE + 
                "/1000, 'unixepoch', 'localtime') = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement statement  = conn.prepareStatement(sql);){
            
            statement.setString(1, stringDate);
            ResultSet resultSet = statement.executeQuery();
            
            // loop through the result set
            while (resultSet.next()) {
                Integer rowid = resultSet.getInt("rowid");
                String title = resultSet.getString(Tables.Prod.COLUMN_TITLE);
                String body = resultSet.getString(Tables.Prod.COLUMN_BODY);
                Date date = resultSet.getDate(Tables.Prod.COLUMN_DATE);
                Reminder reminder = new Reminder(title, body, date);
                reminder.setRowID(rowid);
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    public List<Reminder> getRemindersOfMonth(String stringDate) {
        List<Reminder> reminders = new ArrayList<>();
        
        String sql = "SELECT rowid,* FROM " + Tables.Prod.TABLE_NAME + 
                " WHERE STRFTIME('%m/%Y', " + Tables.Prod.COLUMN_DATE + 
                "/1000, 'unixepoch', 'localtime') = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement statement  = conn.prepareStatement(sql);){
            
            statement.setString(1, stringDate);
            ResultSet resultSet = statement.executeQuery();
            
            // loop through the result set
            while (resultSet.next()) {
                Integer rowid = resultSet.getInt("rowid");
                String title = resultSet.getString(Tables.Prod.COLUMN_TITLE);
                String body = resultSet.getString(Tables.Prod.COLUMN_BODY);
                Date date = resultSet.getDate(Tables.Prod.COLUMN_DATE);
                Reminder reminder = new Reminder(title, body, date);
                reminder.setRowID(rowid);
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    public List<Reminder> getRemindersOfYear(String stringDate) {
        List<Reminder> reminders = new ArrayList<>();
        
        String sql = "SELECT rowid,* FROM " + Tables.Prod.TABLE_NAME + 
                " WHERE STRFTIME('%Y', " + Tables.Prod.COLUMN_DATE + 
                "/1000, 'unixepoch', 'localtime') = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement statement  = conn.prepareStatement(sql);){
            
            statement.setString(1, stringDate);
            ResultSet resultSet = statement.executeQuery();
            
            // loop through the result set
            while (resultSet.next()) {
                Integer rowid = resultSet.getInt("rowid");
                String title = resultSet.getString(Tables.Prod.COLUMN_TITLE);
                String body = resultSet.getString(Tables.Prod.COLUMN_BODY);
                Date date = resultSet.getDate(Tables.Prod.COLUMN_DATE);
                Reminder reminder = new Reminder(title, body, date);
                reminder.setRowID(rowid);
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    public List<Reminder> searchReminders(String searchQuery) {
        List<Reminder> reminders = new ArrayList<>();
        
        String sql = "SELECT rowid,* FROM " + Tables.Prod.TABLE_NAME + 
                " WHERE " + Tables.Prod.COLUMN_TITLE + 
                " LIKE ? OR " + Tables.Prod.COLUMN_BODY + " LIKE ?";
        String finalSearchQuery = "%" + searchQuery + "%";
        
        try (Connection conn = getConnection();
             PreparedStatement statement  = conn.prepareStatement(sql);){
            
            statement.setString(1, finalSearchQuery);
            statement.setString(2, finalSearchQuery);
            ResultSet resultSet = statement.executeQuery();
            
            // loop through the result set
            while (resultSet.next()) {
                Integer rowid = resultSet.getInt("rowid");
                String title = resultSet.getString(Tables.Prod.COLUMN_TITLE);
                String body = resultSet.getString(Tables.Prod.COLUMN_BODY);
                Date date = resultSet.getDate(Tables.Prod.COLUMN_DATE);
                Reminder reminder = new Reminder(title, body, date);
                reminder.setRowID(rowid);
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return reminders;
    }
    
    public int updateReminder(Reminder reminder) {
        int success = 0;
        java.sql.Date sqlDate = null;
        Date date = reminder.getDateObj();
        if (date != null) {
            sqlDate = new java.sql.Date(date.getTime());
        }
        
        String sql = "UPDATE " + Tables.Prod.TABLE_NAME + " " +
                "SET " + Tables.Prod.COLUMN_TITLE + " = ? , " +
                Tables.Prod.COLUMN_BODY + " = ? , " +
                Tables.Prod.COLUMN_DATE + " = ? , " +
                Tables.Prod.COLUMN_CREATE_DATE + " = ? WHERE rowid = ?";
 
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = 
                        connection.prepareStatement(sql)) {
 
            // set the corresponding param
            preparedStatement.setString(1, reminder.getTitle());
            preparedStatement.setString(2, reminder.getBody());
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setDate(4, new java.sql.Date(
                    new Date().getTime()));
            preparedStatement.setInt(5, reminder.getRowID());
            // update 
            success = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return success;
    }
    
}
