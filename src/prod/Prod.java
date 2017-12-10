/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;

/**
 *
 * @author giantas
 */
public class Prod {
    
    static Input input = new Input();
    static final DatabaseHandler dbHandler = new DatabaseHandler("prod.db");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean success = dbHandler.createTable();
        
        if (success) {
            Reminder reminder = new Reminder("This is a reminder");

            System.out.println("Reminder: " + reminder.getTitle());

            boolean proceed = true;
            final String options = "1. Add reminder\n" + 
                    "2. List reminders\n" + 
                    "3. Edit reminder\n" + 
                    "4. Search reminder\n" +
                    "5. List reminders of date\n" +
                    "6. List reminders of month\n" +
                    "7. List reminders of year\n" +
                    "8. Exit\n" +
                    "Choice: ";
            
            while (proceed) {
                System.out.println("\nChoose action:");
                int choice = input.getInt(options);
                
                switch (choice) {
                    case 1:
                        addReminder();
                        break;
                    case 2:
                        listReminders();
                        break;
                    case 3:
                        editReminder();
                        break;
                    case 4:
                        searchReminders();
                        break;
                    case 5:
                        listRemindersOfDate();
                        break;
                    case 6:
                        listRemindersOfMonth();
                        break;
                    case 7:
                        listRemindersOfYear();
                        break;
                    case 8:
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }            
        }
    }
    
    public static void addReminder() {
        String title = input.getLine("\nNew reminder:");
        String date = input.getLine("Date ("+ Reminder.dateFormat + "): ");
        String body = input.getLine("More info: ");
        dbHandler.insertReminder(new Reminder(title, body, date));
    }
    
    public static void listReminders() {
        List<Reminder> reminders = dbHandler.getReminders();
        System.out.println("Displaying " + reminders.size() + " reminders.");
        reminders.forEach((item) -> {
            displayReminderData(item);
        });
    }
    
    public static void searchReminders() {
        String searchQuery = input.getLine("Search: ");
        List<Reminder> reminders = dbHandler.searchReminders(searchQuery);
        System.out.println("Displaying " + reminders.size() + " reminders.");
        reminders.forEach((item) -> {
            displayReminderData(item);
        });
    }
    
    public static void listRemindersOfDate() {
        String date = input.getLine(
                "Enter search date [" + Reminder.dateFormat + "]: ");
        List<Reminder> reminders = dbHandler.getRemindersOfDate(date);
        System.out.println("Displaying " + reminders.size() + 
                " reminders of " + date + ".");
        reminders.forEach((item) -> {
            displayReminderData(item);
        });
    }
    
    public static void listRemindersOfMonth() {
        String date = input.getLine(
                "Enter search month [" + Reminder.monthFormat + "]: ");
        List<Reminder> reminders = dbHandler.getRemindersOfMonth(date);
        System.out.println("Displaying " + reminders.size() + 
                " reminders of " + date + ".");
        reminders.forEach((item) -> {
            displayReminderData(item);
        });
    }
    
    public static void listRemindersOfYear() {
        String date = input.getLine(
                "Enter search year [" + Reminder.yearFormat + "]: ");
        List<Reminder> reminders = dbHandler.getRemindersOfYear(date);
        System.out.println("Displaying " + reminders.size() + 
                " reminders of " + date + ".");
        reminders.forEach((item) -> {
            displayReminderData(item);
        });
    }
    
    private static void displayReminderData(Reminder reminder) {
        System.out.println("\nYour reminder: " + reminder.getTitle());
        System.out.println("Date: " + reminder.getDate());
        System.out.println("Date (String): " + reminder.getStringDate());
        System.out.println("Month (long): " + reminder.getLongMonthName());
        System.out.println("Month (short): " + reminder.getShortMonthName());
        System.out.println("Month (int): " + reminder.getMonthInt());
        System.out.println("Year: " + reminder.getYear());
        System.out.println("Year (Short): " + reminder.getShortYear());
        System.out.println("Body: " + reminder.getBody());
        System.out.println();
    }
    
    private static void editReminder() {
        List<Reminder> reminders = displayReminderTitles();
        int reminderCount = reminders.size();
        System.out.println("Size: " + reminderCount);
        if (reminderCount > 0) {
            int choice = input.getInt("Choose [1-" + reminderCount + "]: ");
            System.out.println("Choice: " + choice);
            if (choice <= reminderCount) {
                Reminder reminder = reminders.get(choice - 1);
                System.out.println("[Press Enter to retain]");
                String title = input.getLine("\nNew title: ");
                if (!title.isEmpty()) {
                    reminder.setTitle(title);
                }
                String date = input.getLine("\nDate ("+ Reminder.dateFormat + "): ");
                if (!date.isEmpty()) {
                    reminder.setDate(date);
                }
                String body = input.getLine("More info: ");
                if (!body.isEmpty()) {
                    reminder.setBody(body);
                }
                int success = dbHandler.updateReminder(reminder);
                if (success > 0) {
                    System.out.println("Reminder updated successfully");
                    displayReminderData(reminder);
                } else {
                    System.out.println("Update failed");
                }
            } else {
                System.out.println("Reminder not found.");
            }
            
        } else {
            System.out.println("No reminder found");
        }
                
    }
    
    private static List<Reminder> displayReminderTitles() {
        List<Reminder> reminders = dbHandler.getRemindersWithID();
        int counter = 1;
        for (Reminder reminder: reminders) {
            System.out.println("\n-- " + counter + ": " + reminder.getTitle());
            System.out.println();
            counter++;
        }
        return reminders;
        
    }
    
    private static Date toDate(String date) {
        Date dateObj = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateObj = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        
        return dateObj;
    }
    
}

