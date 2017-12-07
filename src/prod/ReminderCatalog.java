/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author giantas
 */
public class ReminderCatalog {
    
    private List<Reminder> reminderList;
    
    public ReminderCatalog() {
        this.reminderList = new ArrayList<>();
    }
    
    public ReminderCatalog(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }
    
    public List<Reminder> getReminders() {
        return reminderList;
    }
    
    public List<Reminder> getRemindersOfDay(Date date) {
        List<Reminder> foundList = new ArrayList<>();
        System.out.println("Searching for date: " + date.toString());
        
        for (Reminder reminder: reminderList) {
            Date dateObj = reminder.getDateObj();
            if (dateObj != null && dateObj.compareTo(date) == 0 ) {
                foundList.add(reminder);
            } 
        }
        return foundList;
    }
    
    private Integer getMonthInt(String monthName) {
        Integer month = null;
        try {
            
            Date date = new SimpleDateFormat("MMMM").parse(monthName);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month;
    }
    
    public List<Reminder> getRemindersOfMonth(String month) {
        List<Reminder> foundList = new ArrayList<>();
        
        reminderList.stream().filter(
                (reminder) -> (reminder.getDate() != null && 
                               reminder.getMonthInt() != -1 && 
                               Objects.equals(
                                       reminder.getMonthInt(), getMonthInt(month))))
                .forEachOrdered((reminder) -> {
                    foundList.add(reminder);
                }
            );
        return foundList;
    }
    
    public List<Reminder> getRemindersOfYear(int year) {
        List<Reminder> foundList = new ArrayList<>();
        
        reminderList.stream().filter(
                (reminder) -> (reminder.getDate() != null && 
                               reminder.getYear() == year))
                .forEachOrdered((reminder) -> {
                    foundList.add(reminder);
                });
        ;
        return foundList;
    }
    
    public List<Reminder> add(Reminder reminder) {
        reminderList.add(reminder);
        return reminderList;
    }
    
}
