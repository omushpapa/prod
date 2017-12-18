/* 
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Aswa Paul
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE*  ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    
    private String title = "Sample Title";
    private String body = "Sample Body";
    private String date = null;
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