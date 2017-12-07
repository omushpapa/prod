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
     */
    @Test
    public void testGetMonthInt() {
        Reminder instance = new Reminder("Sample");
        Integer expResult = null;
        Integer result = instance.getMonthInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLongMonthName method, of class Reminder.
     */
    @Test
    public void testGetLongMonthName() {
        Reminder instance = new Reminder("Sample");
        String expResult = null;
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
     * Test of getYear method, of class Reminder.
     */
    @Test
    public void testGetYear() {
        Reminder instance = new Reminder("Sample");
        Integer expResult = null;
        Integer result = instance.getYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of getShortYear method, of class Reminder.
     */
    @Test
    public void testGetShortYear() {
        Reminder instance = new Reminder("Sample");
        Integer expResult = null;
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
