/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prod;

import prod.Reminder;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author giantas
 */
public class ReminderTest {
    
    public ReminderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getTitle method, of class Reminder.
     */
    @Test
    public void testGetTitle() {
        Reminder instance = new Reminder("Sample");
        String expResult = "Sample";
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getTitle method, of class Reminder.
     * Test should return empty title given empty title.
     */
    @Test
    public void testShouldReturnEmptyTitleGivenEmptyTitle() {
        Reminder instance = new Reminder("");
        String expResult = "";
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getTitle method, of class Reminder.
     * Test should return null given if title is null.
     */
    @Test
    public void testShouldReturnNullTitleGivenNullTitle() {
        Reminder instance = new Reminder("");
        String expResult = null;
        instance.setTitle(null);
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBody method, of class Reminder.
     */
    @Test
    public void testGetBody() {
        Reminder instance = new Reminder("Sample");
        String expResult = "Sample Body";
        String result = instance.getBody();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBody method, of class Reminder.
     * Test should return empty title given empty title.
     */
    @Test
    public void testShouldReturnEmptyBodyGivenEmptyBody() {
        Reminder instance = new Reminder("");
        String expResult = "";
        instance.setBody("");
        String result = instance.getBody();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getBody method, of class Reminder.
     * Test should return null if body is null.
     */
    @Test
    public void testShouldReturnNullBodyGivenNullBody() {
        Reminder instance = new Reminder("");
        String expResult = null;
        instance.setBody(null);
        String result = instance.getBody();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDate method, of class Reminder.
     */
    @Test
    public void testGetDate() {
        Reminder instance = new Reminder("Sample");
        String expResult = null;
        String result = instance.getDate();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDateObj method, of class Reminder.
     * Test returns correct string-formatted date object 
     * given day having a leading zero
     */
    @Test
    public void testShouldReturnDateAsStringWithLeadingZero() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("01/12/2017");
        String expResult = "Fri Dec 01 00:00:00 EAT 2017";
        Date result = instance.getDateObj();
        assertEquals(expResult, result.toString());
    }
    
    /**
     * Test of getDateObj method, of class Reminder.
     * Test returns correct string-formatted date object
     * given day having NO leading zero
     */
    @Test
    public void testShouldReturnDateAsStringWithoutLeadingZero() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("1/12/2017");
        String expResult = "Fri Dec 01 00:00:00 EAT 2017";
        Date result = instance.getDateObj();
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of getStringDate method, of class Reminder.
     */
    @Test
    public void testGetStringDate() {
        Reminder instance = new Reminder("Sample");
        String expResult = null;
        String result = instance.getStringDate();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDateObj method, of class Reminder.
     */
    @Test
    public void testGetDateObj() {
        Reminder instance = new Reminder("Sample");
        Date expResult = null;
        Date result = instance.getDateObj();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMonthInt method, of class Reminder.
     * Test should return null if date not set
     */
    @Test
    public void testGetMonthIntGivenEmptyDate() {
        Reminder instance = new Reminder("Sample");
        Integer expResult = null;
        Integer result = instance.getMonthInt();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getMonthInt method, of class Reminder.
     * Test should return zero-index month int
     */
    @Test
    public void testGetMonthInt() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("12/03/2017");
        Integer expResult = 2;
        Integer result = instance.getMonthInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLongMonthName method, of class Reminder.
     * Test should return null given null date
     */
    @Test
    public void testShouldReturnNullGivenNullDate() {
        Reminder instance = new Reminder("Sample");
        String expResult = null;
        String result = instance.getLongMonthName();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getLongMonthName method, of class Reminder.
     * Test should return month String in full
     */
    @Test
    public void testGetLongMonthName() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("2/04/1920");
        String expResult = "April";
        String result = instance.getLongMonthName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getShortMonthName method, of class Reminder.
     */
    @Test
    public void testGetShortMonthName() {
        Reminder instance = new Reminder("Sample");
        String expResult = null;
        String result = instance.getShortMonthName();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getShortMonthName method, of class Reminder.
     * Test should return month String in short
     */
    @Test
    public void testGetLongMonthNameInShort() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("2/04/1920");
        String expResult = "Apr";
        String result = instance.getShortMonthName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getYear method, of class Reminder.
     * Test should return null if date is null
     */
    @Test
    public void testGetYearGivenNullDate() {
        Reminder instance = new Reminder("Sample");
        Integer expResult = null;
        Integer result = instance.getYear();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getYear method, of class Reminder.
     * Test should return year (Integer) in full if date not null
     */
    @Test
    public void testGetYear() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("8/6/1856");
        Integer expResult = 1856;
        Integer result = instance.getYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of getShortYear method, of class Reminder.
     * Test should return null if date is null
     */
    @Test
    public void testGetShortYearGivenNullDate() {
        Reminder instance = new Reminder("Sample");
        Integer expResult = null;
        Integer result = instance.getShortYear();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getShortYear method, of class Reminder.
     * Test should return 2-digit year (Integer) if date not null
     */
    @Test
    public void testGetShortYear() {
        Reminder instance = new Reminder("Sample");
        instance.setDate("8/6/1856");
        Integer expResult = 56;
        Integer result = instance.getShortYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTitle method, of class Reminder.
     */
    @Test
    public void testSetTitle() {
        String expResult = "This is a sample title";
        String title = "This is a sample title";
        Reminder instance = new Reminder("Sample");
        instance.setTitle(title);
        assertEquals(expResult, instance.getTitle());
    }

    /**
     * Test of setBody method, of class Reminder.
     */
    @Test
    public void testSetBody() {
        String body = "";
        Reminder instance = new Reminder("Sample");
        String expResult = "";
        instance.setBody(body);
        assertEquals(expResult, instance.getBody());
    }

    /**
     * Test of setDate method, of class Reminder.
     */
    @Test
    public void testSetDate() {
        String date = "01/10/2017";
        Reminder instance = new Reminder("Sample");
        String expResult = "01/10/2017";
        instance.setDate(date);
        assertEquals(expResult, instance.getDate());
    }
    
}
