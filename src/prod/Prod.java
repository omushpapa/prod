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

/**
 *
 * @author giantas
 */
public class Prod {
    
    static Input input = new Input();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Reminder reminder = new Reminder("This is a reminder");
        
        System.out.println("Reminder: " + reminder.getTitle());
        
        boolean proceed = true;
        int counter = 0;
        ReminderCatalog catalog = new ReminderCatalog();
        
        while (proceed) {
            Reminder customReminder = new Reminder(
                    input.getLine("\nNew reminder: "));
            catalog.add(customReminder);
            customReminder.setDate(input.getLine("Date: "));
            
            //displayReminderData(customReminder);
            
            counter += 1;
            if (counter == 2){
                break;
            }
            
        }
        
        System.out.println("Displaying all reminders:");
        for (Iterator<Reminder> it = catalog.getReminders().iterator(); 
                it.hasNext();) {
            Reminder item = it.next();
            displayReminderData(item);
        }
        
        Date theDay = toDate("12/10/2017");
        System.out.println("Displaying all reminders of " + theDay.toString());
        for (Reminder item: catalog.getRemindersOfDay(theDay)) {
            displayReminderData(item);
        }
        System.out.println("Catalog length (with day): " + 
                catalog.getRemindersOfDay(theDay).size());
        System.out.println("Catalog length (all): " + 
                catalog.getReminders().size());
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

