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
package prod.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.Popup;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import prod.Database.DatabaseHandler;
import prod.Models.Reminder;
import static prod.Models.Reminder.dateFormat;

/**
 *
 * @author giantas
 */
// A Swing GUI application inherits from top-level container javax.swing.JFrame
public class Gui extends JFrame {
    private static final DatabaseHandler dbHandler = new DatabaseHandler("prod.db");
    private final int windowWidth = 700;
    private final int windowHeight = 600;
    private final Container container;
    private final JPanel panelLeft = new JPanel();
    private final JPanel panelRight = new JPanel();
    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();
    private final int verticalSpacerSize = 8;
    private final int horizontalSpacerSize = 15;
    private Border lowerEtchedBorder = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    
    // -----
    protected boolean controlsOnTop;

    protected Calendar currentDisplayDate;

    protected JButton prevMonth;
    protected JButton nextMonth;
    protected JButton prevYear;
    protected JButton nextYear;

    protected JTextField textField;

    protected List<ActionListener> popupListeners = 
        new ArrayList<ActionListener>();

    protected Popup popup;

    protected SimpleDateFormat dayName   = new SimpleDateFormat("d");
    protected SimpleDateFormat monthName = new SimpleDateFormat("MMMM");

    protected String iconFile = "datepicker.gif";
    protected String[] weekdayNames = 
        {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    JPanel datePanel = new JPanel();
    JPanel panelRightDown = new JPanel();
    JPanel controlsPanel;
    JPanel panelLeftDown = new JPanel();
    JTextArea displayItem = new JTextArea("Hello, left top!");
    JScrollPane topScrollPane = new JScrollPane();
    // -----
 
    // Constructor to setup the GUI components and event handlers
    public Gui() {
        //-----
        currentDisplayDate   = Calendar.getInstance();
        controlsOnTop        = true;
       
        // Retrieve the content-pane of the top-level container JFrame
        // All operations done on the content-pane
        
        container = getContentPane();
        container.setLayout(new BorderLayout());
        createMenuBar();
        createStatusBar();
        
        JPanel fullPane = new JPanel();
        fullPane.setLayout(new GridBagLayout());
        // Spacer
        container.add(Box.createVerticalStrut(
                4), BorderLayout.NORTH);
        container.add(fullPane, BorderLayout.CENTER);
        fullPane.setMinimumSize(new Dimension(windowWidth, windowHeight));
        
        // Position Left and Right panels
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        fullPane.add(panelLeft, gridBagConstraints);
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 0;
        fullPane.add(Box.createHorizontalStrut(
                15), gridBagConstraints);
        
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.weightx = 0.9;
        fullPane.add(panelRight, gridBagConstraints);
        
        
        configurePanelLeft(panelLeft);
        configurePanelRight(panelRight);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Prod - Reminder"); 
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
        //createPanel();
        validate();
        repaint();
    }

    public void setDate(int month, int day, int year) {
        currentDisplayDate = Calendar.getInstance();
        currentDisplayDate.set(expandYear(year), month - 1, day);
        //createPanel();
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
    
    public void setControlsOnTop(boolean flag) {
        controlsOnTop = flag; 
        repaint();
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
        prevYear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                addYear(-1);        
            }
        });

        prevMonth = new JButton("<");
        c.add(prevMonth);
        prevMonth.setMargin(new Insets(0,0,0,0));
        prevMonth.addActionListener((ActionEvent arg0) -> {
            addMonth(-1);
        });


        textField = new JTextField(getFormattedDate(), 10);
        c.add(textField);
        //textField.setEditable(false);
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

        datePanel.setFocusable(true);
        datePanel.setLayout(gridbag);

        String month = monthName.format(currentDisplayDate.getTime());
        String year  = Integer.toString(getYear());

        c.gridx      = 0;
        c.gridy      = 0;
        c.gridwidth  = 7;
        c.gridheight = 1;
        JLabel title = new JLabel(month + " " + year);
        datePanel.add(title, c);
        Font font      = title.getFont();
//        Font titleFont = new Font(font.getName(), font.getStyle(),
//              font.getSize() + 2);
        Font weekFont = new Font(font.getName(), font.getStyle(),
                font.getSize() - 2);
        title.setFont(font);

        c.gridy      = 1;
        c.weightx    = 0.1;
        c.weighty    = 0.1;
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
//      monthInt = 0;

        c.gridwidth  = 1;
        c.gridheight = 1;

        for (c.gridy = 2; c.gridy < 8; c.gridy++) {
            for (c.gridx = 0; c.gridx < 7; c.gridx++) {
                JButton dayButton;
                
                if (calendar.get(Calendar.MONTH) == monthInt) {
                    String dayString = dayName.format(calendar.getTime());
                    if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                        dayString = " " + dayString;
                    }
                        
                    dayButton = new JButton(dayString);
                } else {
                    dayButton = new JButton();
                    dayButton.setEnabled(false);
                }
                
                datePanel.add(dayButton, c);
                Color color = dayButton.getBackground();
                if ((calendar.get(Calendar.DAY_OF_MONTH) == getDay()) &&
                        (calendar.get(Calendar.MONTH) == monthInt)) { 
                    dayButton.setBackground(Color.yellow);
                //  dayButton.setFocusPainted(true);
                //  dayButton.setSelected(true);
                } else
                    dayButton.setBackground(color);
                dayButton.setFont(weekFont);
                dayButton.setFocusable(true);
                //dayButton.setPreferredSize(new Dimension(width, height));
                dayButton.setMargin(new Insets(5,5,5,5));
                dayButton.addActionListener((ActionEvent e) -> {
                    changeDay(e.getActionCommand());
                    panelLeftDown.removeAll();
                    listReminders(panelLeft);
                    validate();
                    repaint();
                    System.out.println("The date is " + getFormattedDate());
                    System.out.println("Date is " + currentDisplayDate.getTime().toString());
                });
                calendar.add(Calendar.DATE, +1);
            }
         // if (draw.get(Calendar.MONTH) != monthInt) break;
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
                currentDisplayDate.set(Calendar.MONTH,
                        Integer.valueOf(parts[0]) - 1);
                currentDisplayDate.set(Calendar.DAY_OF_MONTH,
                        Integer.valueOf(parts[1]));
                currentDisplayDate.set(Calendar.YEAR,
                        expandYear(Integer.valueOf(parts[2])));
                break;
            case 2:
                currentDisplayDate = Calendar.getInstance();
                currentDisplayDate.set(Calendar.MONTH,
                        Integer.valueOf(parts[0]) - 1);
                currentDisplayDate.set(Calendar.DAY_OF_MONTH,
                        Integer.valueOf(parts[1]));
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
    
    private void refreshPanelRightDown() {
        datePanel = new JPanel();
        controlsPanel = new JPanel();
        
        datePanel = createDatePicker();
        controlsPanel = createControls();
        
        panelRightDown.removeAll();
        panelRightDown.add(controlsPanel, BorderLayout.NORTH);
        panelRightDown.add(datePanel, BorderLayout.CENTER);
        validate();
        repaint();
    }
    
    private void createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        file.add(eMenuItem);

        menubar.add(file);

        setJMenuBar(menubar);
    }
    
    private void createStatusBar() {
        // create the status bar panel and shove it down the bottom of the frame
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        container.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(container.getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }
    
    public void configurePanelLeft(JPanel panel) {
        // Configure Left-side panels
        // Top panel
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        JPanel panelLeftTop = new JPanel();
        panelLeftTop.setBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.YELLOW));
        
        panel.setLayout(new GridBagLayout());
        panel.add(panelLeftTop, gridBagConstraints);
        
        // Display Area
        //JTextArea displayItem = new JTextArea("Hello, left top!");
        displayItem.setBorder(lowerEtchedBorder);
        displayItem.setLineWrap(true);
        displayItem.setBackground(panel.getBackground());
        displayItem.setWrapStyleWord(true);
        //displayItem.setEditable(false);
        panelLeftTop.setLayout(new BorderLayout());
        
        //JScrollPane topScrollPane = new JScrollPane();
        topScrollPane.getVerticalScrollBar().setUnitIncrement(3);
        topScrollPane.setViewportView(displayItem);
        topScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panelLeftTop.add(topScrollPane);
        
        // Spacer
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0;
        panel.add(Box.createVerticalStrut(
                verticalSpacerSize), gridBagConstraints);
        
        // Bottom panel
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.7;
        //JPanel panelLeftDown = new JPanel();
        panelLeftDown.setBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
        panelLeftDown.setLayout(new GridBagLayout());
        
        listReminders(panel);
        
        JScrollPane scrollPane = new JScrollPane(panelLeftDown,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        panelLeftDown.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(300,800));
        scrollPane.getVerticalScrollBar().setUnitIncrement(3);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                scrollPane.getVerticalScrollBar().setValue(0);
            }
         });
        panel.add(scrollPane, gridBagConstraints);

    }
    
    public void configurePanelRight(JPanel panel) {
        // Configure right-side panels   
        // Top panel
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        JPanel panelRightTop = new JPanel();
        panelRightTop.setBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.YELLOW));
        panelRightTop.add(new JLabel("Hello, right top!"));
        panel.setLayout(new GridBagLayout());
        panel.add(panelRightTop, gridBagConstraints);
        
        // Spacer
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0;
        panel.add(Box.createVerticalStrut(
                verticalSpacerSize), gridBagConstraints);
        
        // Bottom panel
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        
        panelRightDown.setBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
        //panelRightDown.add(new JLabel("Hello, right down!"));
        panel.add(panelRightDown, gridBagConstraints);
        panel.add(panelRightDown, gridBagConstraints);
        
        // Date Picker
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        panelRightDown.setLayout(new BorderLayout());
        datePanel = createDatePicker();
        controlsPanel = createControls();
        panelRightDown.add(controlsPanel, BorderLayout.NORTH);
        panelRightDown.add(datePanel, BorderLayout.CENTER);
    }
    
    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel listCellRendererComponent = 
                        (JLabel) super.getListCellRendererComponent(
                                list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(
                        lowerEtchedBorder);
                listCellRendererComponent.setEnabled(true);
                return listCellRendererComponent;
            }
        };
    }

    public static void main(String[] args) {
        // Run the GUI construction in the Event-Dispatching thread for thread-safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui(); // Let the constructor do the job
            }
        });
    }

    private void listReminders(JPanel parentPanel) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String date = df.format(getDate());
        
        List<Reminder> reminders = dbHandler.getRemindersOfDate(date);
        
        for (int i = 0; i < reminders.size(); i++) {
            reminders.get(i);
        }
        
        for (int i = 0; i < reminders.size(); i++) {
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            gridBagConstraints.weightx = 0.3;
            //gridBagConstraints.weighty = 1.0;
            
            Reminder reminder = reminders.get(i);
            String textValue = reminder.getTitle() + ": " + reminder.getBody();
            JTextArea textArea = new JTextArea(textValue);
            textArea.setBorder(lowerEtchedBorder);
            textArea.setLineWrap(true);
            textArea.setBackground(parentPanel.getBackground());
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayItem.setText(textValue);
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() { 
                            topScrollPane.getVerticalScrollBar().setValue(0);
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            panelLeftDown.add(textArea, gridBagConstraints);
        }
    }
    
}
