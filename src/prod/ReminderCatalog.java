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
package prod;

import prod.Models.Reminder;
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
