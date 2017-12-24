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
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package prod.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import prod.Database.DatabaseHandler;
import prod.Models.Reminder;
import static prod.Models.Reminder.dateFormat;

/**
 *
 * @author giantas
 */


public class Gui extends JFrame {
    
    private final DatabaseHandler dbHandler = new DatabaseHandler("prod.db");
    private final int windowWidth = 700;
    private final int windowHeight = 550;    
    private final Container contentPane;
    private final JMenuBar menuBar = new JMenuBar();
    private final GridBagLayout gridBagLayout = new GridBagLayout();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private JPanel panelLeft, panelRight, 
            panelLeftTop, panelLeftDown,
            panelRightTop, panelRightDown,
            datePanel, controlsPanel;
    private final JPanel tabOne = new JPanel();
    private final JPanel tabTwo = new JPanel();
    private JTabbedPane tabbedPane;
    private GridLayout gridLayout = new GridLayout();
    private JTextArea displayItem = new JTextArea("Hello, "
            + "selected reminder will be shown here!");
    private final Border lowerEtchedBorder = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    private JScrollPane topScrollPane = new JScrollPane();
    private JScrollPane scrollTabOne, scrollTabTwo;
    private BorderLayout borderLayout = new BorderLayout();
    private JButton selectDateButton = null;
    
    // Date variables
    protected Calendar currentDisplayDate;
    protected final int currentDayOfMonth;

    protected JButton prevMonth;
    protected JButton nextMonth;
    protected JButton prevYear;
    protected JButton nextYear;

    protected JTextField textField;
    protected SimpleDateFormat dayName   = new SimpleDateFormat("d");
    protected SimpleDateFormat monthName = new SimpleDateFormat("MMMM");

    protected String[] weekdayNames = 
        {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
    public Gui(String title) {
        currentDisplayDate   = Calendar.getInstance();
        currentDayOfMonth = currentDisplayDate.get(Calendar.DAY_OF_MONTH);
        contentPane = getContentPane();
        contentPane.setLayout(gridBagLayout);
        
        createMenuBar();
        createMainWindow();
        createStatusBar();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(title); 
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new Terminator());
    }
    
    public void setDate(String date) {
        currentDisplayDate = Calendar.getInstance();
        editDate(date);
    }

    public void setDate(Calendar date) {
        currentDisplayDate = date;
        validate();
        repaint();
    }

    public void setDate(int month, int day, int year) {
        currentDisplayDate = Calendar.getInstance();
        currentDisplayDate.set(expandYear(year), month - 1, day);
        validate();
        repaint();
    }

    protected int expandYear(int year) {
        if (year < 100) {                   // 2 digit year
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int current2DigitYear = currentYear % 100;
            int currentCentury    = currentYear / 100 * 100;
            // set 2 digit year range +20 / -80 from current year
            int high2DigitYear    = (current2DigitYear + 20) % 100;
            if (year <= high2DigitYear) {
                year += currentCentury;
            }
            else {
                year += (currentCentury - 100);
            }
        }
        return year;
    }
    
    public Calendar getCalendarDate() {
        return currentDisplayDate;
    }

    public Date getDate() {
        return currentDisplayDate.getTime();
    }

    public String getFormattedDate() {
        return  Integer.toString(getDay()) + "/" +
            Integer.toString(getMonth()) + "/" +
            Integer.toString(getYear());
    }

    public int getMonth() {
        return currentDisplayDate.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return currentDisplayDate.get(Calendar.DAY_OF_MONTH);
    }

    public int getYear() {
        return currentDisplayDate.get(Calendar.YEAR);
    }
    
    protected JPanel createControls() {        
        JPanel c = new JPanel();
        c.setBorder(BorderFactory.createRaisedBevelBorder());
        c.setFocusable(true);
        c.setLayout(new FlowLayout(FlowLayout.CENTER));

        prevYear = new JButton("<<");
        c.add(prevYear);
        prevYear.setMargin(new Insets(0,0,0,0));
        prevYear.addActionListener((ActionEvent arg0) -> {
            addYear(-1);
        });

        prevMonth = new JButton("<");
        c.add(prevMonth);
        prevMonth.setMargin(new Insets(0,0,0,0));
        prevMonth.addActionListener((ActionEvent arg0) -> {
            addMonth(-1);
        });


        textField = new JTextField(getFormattedDate(), 10);
        c.add(textField);
        textField.setEnabled(true);
        textField.setColumns(10);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.addActionListener((ActionEvent e) -> {
            editDate(textField.getText());
        });
      

        nextMonth = new JButton(">");
        c.add(nextMonth);
        nextMonth.setMargin(new Insets(0,0,0,0));
        nextMonth.addActionListener((ActionEvent arg0) -> {
            addMonth(+1);
        });

        nextYear = new JButton(">>");
        c.add(nextYear);
        nextYear.setMargin(new Insets(0,0,0,0));
        nextYear.addActionListener((ActionEvent arg0) -> {
            addYear(+1);
        });

        return c;
    }
    
    protected JPanel createDatePicker() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c  = new GridBagConstraints();
        
        datePanel = new JPanel();
        datePanel.setFocusable(true);
        datePanel.setLayout(gridbag);

        String month = monthName.format(currentDisplayDate.getTime());
        String year  = Integer.toString(getYear());

        c.gridx      = 0;
        c.gridy      = 0;
        c.gridwidth  = 7;
        c.gridheight = 1;
        c.weightx    = 0.1428571428571429;      // 1/7 columns
        c.weighty    = 0.125;                   // 1/8 rows
        JLabel title = new JLabel(month + " " + year);
        datePanel.add(title, c);
        Font font      = title.getFont();
        Font weekFont = new Font(font.getName(), font.getStyle(),
                font.getSize() - 2);
        title.setFont(font);

        c.gridy      = 1;
        c.gridwidth  = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        for (c.gridx = 0; c.gridx < 7; c.gridx++) {
            JLabel label = new JLabel(
                    weekdayNames[c.gridx], SwingConstants.CENTER);
            datePanel.add(label, c);
            label.setFont(weekFont);
        }

        Calendar calendar = (Calendar) currentDisplayDate.clone();
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK) + 1);
        int monthInt = currentDisplayDate.get(Calendar.MONTH);

        c.gridwidth  = 1;
        c.gridheight = 1;

        for (c.gridy = 2; c.gridy < 8; c.gridy++) {
            for (c.gridx = 0; c.gridx < 7; c.gridx++) {
                JButton dayButton = new JButton();;
                JLabel dayLabel;
                
                if (calendar.get(Calendar.MONTH) == monthInt) {
                    String dayString = dayName.format(calendar.getTime());
                    if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                        dayString = " " + dayString;
                    }
                    
                    dayButton.setText(dayString);
                    dayButton.setBorderPainted(true);
                    String formattedDate = calendar.get(
                            Calendar.DAY_OF_MONTH) + "/" +
                            (calendar.get(Calendar.MONTH) + 1) + "/" +
                            calendar.get(Calendar.YEAR);
                    dayLabel = new JLabel(
                            dbHandler.getRemindersCountOfDate(formattedDate),
                            SwingConstants.TRAILING);
                } else {
                    dayButton.setEnabled(false);
                    dayButton.setBorderPainted(false);
                    dayLabel = new JLabel(" ",
                            SwingConstants.TRAILING);
                    
                }
                
                dayLabel.setFont(new Font("Serif", Font.PLAIN, 8));
                dayLabel.setForeground(Color.red);
                datePanel.add(dayButton, c);
                Color color = dayButton.getBackground();
                if ((calendar.get(Calendar.DAY_OF_MONTH) == getDay()) &&
                        (calendar.get(Calendar.MONTH) == monthInt)) {
                    dayButton.setContentAreaFilled(true);
                    dayButton.setBackground(Color.yellow);
                } else {
                    dayButton.setContentAreaFilled(false);
                    dayButton.setBackground(color);
                }
                dayButton.add(dayLabel);
                dayButton.setLayout(new GridLayout(2, 1));
                dayButton.setFocusPainted(false);
                dayButton.setFont(weekFont);
                dayButton.setFocusable(true);
                dayButton.setMargin(new Insets(5,5,5,5));
                dayButton.addActionListener((ActionEvent e) -> {
                    if (selectDateButton != null) {
                        if (selectDateButton.getText().equals(
                                String.valueOf(currentDayOfMonth))) {
                            selectDateButton.setBackground(Color.yellow);
                        } else {
                            selectDateButton.setContentAreaFilled(false);
                        }
                    }
                    selectDateButton = dayButton;
                    dayButton.setContentAreaFilled(true);
                    dayButton.setBackground(Color.CYAN);
                    changeDay(e.getActionCommand());
                    tabTwo.removeAll();
                    listReminders(true);
                    validate();
                    repaint();
                    tabbedPane.setSelectedComponent(scrollTabTwo);
                });
                calendar.add(Calendar.DATE, +1);
            }
        }
        return datePanel;
    }
    
    public void addMonth(int month) {
        currentDisplayDate.add(Calendar.MONTH, month);
        
        refreshPanelRightDown();
    }

    public void addYear(int year) {
        currentDisplayDate.add(Calendar.YEAR, year);
        
        refreshPanelRightDown();
    }

    public void editDate(String date) {
        parseDate(date);
        
        refreshPanelRightDown();
    }

    protected void parseDate(String date) {
        String[] parts = date.split("/");
        switch (parts.length) {
            case 3:
                currentDisplayDate.set(Calendar.DAY_OF_MONTH,
                        Integer.valueOf(parts[0]));
                currentDisplayDate.set(Calendar.MONTH,
                        Integer.valueOf(parts[1]) - 1);
                currentDisplayDate.set(Calendar.YEAR,
                        expandYear(Integer.valueOf(parts[2])));
                break;
            case 2:
                currentDisplayDate = Calendar.getInstance();
                currentDisplayDate.set(Calendar.DAY_OF_MONTH,
                        Integer.valueOf(parts[0]));
                currentDisplayDate.set(Calendar.MONTH,
                        Integer.valueOf(parts[1]) - 1);
                break;
            default:
                // invalid date
                currentDisplayDate = Calendar.getInstance();
                break;
        }
    }

    public void changeDay(String day) {
        currentDisplayDate.set(Calendar.DAY_OF_MONTH, 
                Integer.valueOf(day.trim()));
        textField.setText(getFormattedDate());
    }
    
    private void createMenuBar() {
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        file.add(eMenuItem);

        menuBar.add(file);

        setJMenuBar(menuBar);
    }

    private void createStatusBar() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.001;
        
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        contentPane.add(statusPanel, gbc);
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }

    private void createMainWindow() {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.999;
        gbc.weightx = 1.0;
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(gridBagLayout);
        mainPanel.setBorder(
                BorderFactory.createMatteBorder(
                        1, 1, 1, 1, Color.BLACK));
        
        contentPane.add(mainPanel, gbc);
        
        panelLeft = new JPanel(gridBagLayout);
        panelRight = new JPanel(gridBagLayout);
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.weightx = 0.25;
        mainPanel.add(panelLeft, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        mainPanel.add(panelRight, gbc);
        
        configurePanelLeft();
        configurePanelRight();
    }

    private void configurePanelLeft() {
        panelLeftTop = new JPanel(gridLayout);
        panelLeftDown = new JPanel(gridLayout);
        tabbedPane = new JTabbedPane();
        panelLeftTop.setBorder(
                BorderFactory.createMatteBorder(
                        1, 1, 1, 1, Color.YELLOW));
        panelLeftDown.setBorder(
                BorderFactory.createMatteBorder(
                        1, 1, 1, 1, Color.BLUE));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.3;
        c.weightx = 1.0;
        panelLeft.add(panelLeftTop, c);
        c.gridy = 1;
        c.weighty = 0.7;
        panelLeft.add(panelLeftDown, c);
        
        // Display Area
        displayItem.setBorder(lowerEtchedBorder);
        displayItem.setLineWrap(true);
        displayItem.setBackground(contentPane.getBackground());
        displayItem.setWrapStyleWord(true);
        displayItem.setPreferredSize(panelLeftTop.getSize());
        // topScrollPane
        topScrollPane.getVerticalScrollBar().setUnitIncrement(3);
        topScrollPane.setViewportView(displayItem);
        topScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelLeftTop.add(topScrollPane);
        
        // Bottom Panel
        tabOne.setLayout(gridBagLayout);
        tabTwo.setLayout(gridBagLayout);
        panelLeftDown.add(tabbedPane);
        
        // ScrollPanes
        scrollTabOne = new JScrollPane(tabOne);
        scrollTabOne.setPreferredSize(
                new Dimension(
                        panelLeftDown.getWidth(),
                        panelLeftDown.getHeight())
        );
        scrollTabTwo = new JScrollPane(tabTwo);
        scrollTabTwo.setPreferredSize(
                new Dimension(
                        panelLeftDown.getWidth(),
                        panelLeftDown.getHeight())
        );
        tabbedPane.add(scrollTabOne, "All");
        tabbedPane.add(scrollTabTwo, "Selected Date");
        
        listReminders(false);
        
        listReminders(true);
    }

    private void configurePanelRight() {
        panelRightTop = new JPanel();
        panelRightDown = new JPanel(gridBagLayout);
        panelRightTop.setBorder(
                BorderFactory.createMatteBorder(
                        1, 1, 1, 1, Color.YELLOW));
        panelRightDown.setBorder(
                BorderFactory.createMatteBorder(
                        1, 1, 1, 1, Color.BLUE));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.weightx = 1.0;
        panelRight.add(panelRightTop, c);
        
        c.gridy = 1;
        c.weighty = 0.9;
        panelRight.add(panelRightDown, c);
        
        // Date picker
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.1;
        
        datePanel = createDatePicker();
        controlsPanel = createControls();
        populatePanelRightDown(controlsPanel, datePanel);
    }
    
    private void refreshPanelRightDown() {        
        datePanel = createDatePicker();
        controlsPanel = createControls();
        
        panelRightDown.removeAll();
        populatePanelRightDown(controlsPanel, datePanel);
        validate();
        repaint();
    }
    
    private void populatePanelRightDown(JPanel controlsPanel, JPanel datePanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        
        panelRightDown.add(controlsPanel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 0.9;
        panelRightDown.add(datePanel, gbc);
    }
    
    private void listReminders(Boolean useSelectDate) {
        GridBagConstraints c = new GridBagConstraints();
        DateFormat df = new SimpleDateFormat(dateFormat);
        String date = df.format(getDate());
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.weightx = 0.3;
        c.weighty = 1;
        
        List<Reminder> reminders;
        if (useSelectDate) {
            reminders = dbHandler.getRemindersOfDate(date);
        } else {
            reminders = dbHandler.getRemindersWithID();
        }
        
        for (int i = 0; i < reminders.size(); i++) {
            c.gridy = i;
            
            Reminder reminder = reminders.get(i);
            String textValue = reminder.getTitle() + ": " + reminder.getBody();
            JTextArea textArea = new JTextArea(textValue);
            textArea.setBorder(lowerEtchedBorder);
            textArea.setLineWrap(true);
            textArea.setColumns(2);
            textArea.setBackground(contentPane.getBackground());
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayItem.setText(textValue);
                    SwingUtilities.invokeLater(() -> {
                        topScrollPane.getVerticalScrollBar().setValue(0);
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // Do nothing
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // Do nothing
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Do nothing
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Do nothing
                }
            });
            if (useSelectDate) {
                tabTwo.add(textArea, c);
            } else {
                tabOne.add(textArea, c);
            }
        }
        
        SwingUtilities.invokeLater(() -> {
                        scrollTabOne.getVerticalScrollBar().setValue(0);
                    });
        SwingUtilities.invokeLater(() -> {
                        scrollTabTwo.getVerticalScrollBar().setValue(0);
                    });
    }
    
}
