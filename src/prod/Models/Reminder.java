/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author giantas
 */
public class Reminder {
    
    public String title = "Sample Title";
    public String body = "Sample Body";
    public String date = null;
    private Integer rowid = null;
    public static String dateFormat = "dd/MM/yyyy";
    public static String monthFormat = "MM/yyyy";
    public static String yearFormat = "yyyy";
    
    public Reminder (String title) {
        this.title = title;
    }
    
    public Reminder (String title, String body) {
        this.title = title;
        this.body = body;
    }
    
    public Reminder (String title, String body, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }
    
    public Reminder (String title, String body, Date date) {
        this.title = title;
        this.body = body;
        if (date != null) {
            DateFormat df = new SimpleDateFormat(dateFormat);
            this.date = df.format(date);
        } else {
            this.date = null;
        }
    }
    
    public int getRowID() {
        return this.rowid;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getBody() {
        return this.body;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public String getStringDate() {
        Date dateObj = getDateObj();
        if (dateObj != null) {
            return getDateObj().toString();
        } else {
            return null;
        }
    }
    
    public Date getDateObj() {
        Date dateObj = null;
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            dateObj = df.parse(date);
        } catch (ParseException e) {
            //e.printStackTrace();
        } catch (NullPointerException n) {
            //n.printStackTrace();
        }
        
        return dateObj;
    }
    
    public Integer getMonthInt() {
        Integer month = null;
        Date dateObj = getDateObj();
        if (dateObj != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObj);
            month = cal.get(Calendar.MONTH);
        }
        
        return month;
    }
    
    private String getMonthName(String monthFormat) {
        Calendar calendar = Calendar.getInstance();
        Date dateObj = getDateObj();
        if (dateObj != null) {
            calendar.setTime(dateObj);
            SimpleDateFormat monthDate = new SimpleDateFormat(monthFormat);
            return monthDate.format(calendar.getTime());
        } else {
            return null;
        }
    }
    
    public String getLongMonthName() {
        return getMonthName("MMMM");
    }
    
    public String getShortMonthName() {
        return getMonthName("MMM");
    }
    
    public Integer getYear() {
        Date dateObj = getDateObj();
        if (dateObj != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObj);
            return calendar.get(Calendar.YEAR);
        } else {
            return null;
        }        
    }
    
    public Integer getShortYear() {
        Date dateObj = getDateObj();
        if (dateObj != null) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yy");
            calendar.setTime(dateObj);
            return Integer.valueOf(df.format(calendar.getTime()));
        } else {
            return null;
        } 
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Reminder setBody(String body) {
        this.body = body;
        return this;
    }
    
    public Reminder setDate(String date) {
        this.date = date;
        return this;
    }
    
    public Reminder setRowID(Integer rowid) {
        this.rowid = rowid;
        return this;
    }
    
}