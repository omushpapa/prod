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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import prod.Config.ConfigKeys;
import prod.Config.ConfigHandler;
import prod.Database.DatabaseHandler;
import prod.Models.Reminder;
import static prod.Models.Reminder.dateFormat;
import prod.Prod;

/**
 *
 * @author giantas
 */

public class Gui extends JFrame {
    
    public static final String RESOURCES_PATH = "../resources/";
    private final DatabaseHandler dbHandler;
    private final ConfigHandler config;
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
    private EditorPanel displayItem = new EditorPanel();
    private final Border lowerEtchedBorder = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    private JScrollPane topScrollPane = new JScrollPane();
    private JScrollPane scrollTabOne, scrollTabTwo;
    private BorderLayout borderLayout = new BorderLayout();
    private JButton selectDateButton = null;
    private JTextArea selectListItem = null;
    private JPanel displayItemControls;
    
    private UndoManager undoManager;
    private StyledDocument styledDoc;
    private HTMLEditorKit htmlKit;
    
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
    
    public Gui(String title, DatabaseHandler handler, ConfigHandler manager) {
        this.dbHandler = handler;
        this.config = manager;
        applyLook();
        currentDisplayDate   = Calendar.getInstance();
        currentDayOfMonth = getCurrentDay();
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
    
    public int getCurrentDay() {
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Calendar ycalendar = Calendar.getInstance();
        ycalendar.get(Calendar.DATE);
        return Integer.valueOf(
                dateFormat.format(ycalendar.getTime()));
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
                if ((calendar.get(Calendar.DAY_OF_MONTH) == getCurrentDay()) &&
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
        
        JMenu options = new JMenu("Options");
        JMenu look = new JMenu("Look");
        
        JMenuItem nimbus = new JMenuItem("Nimbus");
        nimbus.addActionListener((ActionEvent e) -> {
            if (allowLookAndFeelChange("Nimbus")) {
                config.setProperty(ConfigKeys.LOOK, "Nimbus");
                restartGui();
            }
        });
        JMenuItem metal = new JMenuItem("Metal");
        metal.addActionListener((ActionEvent e) -> {
            if (allowLookAndFeelChange("Metal")) {
                config.setProperty(ConfigKeys.LOOK, "Metal");
                restartGui();
            }
        });
        JMenuItem gtk = new JMenuItem("GTK");
        gtk.addActionListener((ActionEvent e) -> {
            if (allowLookAndFeelChange("GTK")) {
                config.setProperty(ConfigKeys.LOOK, "GTK");
                restartGui();
            }
        });
        JMenuItem motif = new JMenuItem("Motif");
        motif.addActionListener((ActionEvent e) -> {
            if (allowLookAndFeelChange("Motif")) {
                config.setProperty(ConfigKeys.LOOK, "Motif");
                restartGui();
            }
        });
        JMenuItem windows = new JMenuItem("Classic Windows");
        windows.addActionListener((ActionEvent e) -> {
            if (allowLookAndFeelChange("Classic Windows")) {
                config.setProperty(ConfigKeys.LOOK, "Windows");
                restartGui();
            }
        });
        JMenuItem multi = new JMenuItem("Multi");
        multi.addActionListener((ActionEvent e) -> {
            if (allowLookAndFeelChange("Multi")) {
                config.setProperty(ConfigKeys.LOOK, "Synth");
                restartGui();
            }
        });
        
        String definedLook = config.getProperty(ConfigKeys.LOOK, "Metal");
        if (!definedLook.equals(nimbus.getText())) {
            look.add(nimbus);
        }
        if (!definedLook.equals(metal.getText())) {
            look.add(metal);
        }
        if (!definedLook.equals(gtk.getText())) {
            look.add(gtk);
        }
        if (!definedLook.equals(motif.getText())) {
            look.add(motif);
        }
        if (!definedLook.equals(windows.getText())) {
            look.add(windows);
        }
        if (!definedLook.equals(multi.getText())) {
            look.add(multi);
        }

        file.add(eMenuItem);
        options.add(look);

        menuBar.add(file);
        menuBar.add(options);

        setJMenuBar(menuBar);
    }
    
    private boolean allowLookAndFeelChange(String theme) {
        boolean close = false;
        String message = "Do you want to change to '" + theme + "' look?\n\n"
                + "Note: Changes may NOT be full applied until restart!\n"
                + "\tNot all Looks may be available for your system.";
        int reply = JOptionPane.showConfirmDialog(
                this, message, "Change Look", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
          close = true;
        }
        return close;
    }
    
    private void applyLook() {
        try {
            UIManager.setLookAndFeel(
                    getLookAndFeelClassName(
                            config.getProperty(ConfigKeys.LOOK, "Metal")));
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException | 
                NullPointerException ex) {
            Logger.getLogger(Prod.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        for(Window window : JFrame.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
    
    private static String getLookAndFeelClassName(String nameSnippet) {
        UIManager.LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info : plafs) {
            if (info.getName().contains(nameSnippet)) {
                return info.getClassName();
            }
        }
        return null;
    }
    
    public void restartGui() {
        dispose();
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        new Gui(this.getTitle(), this.dbHandler, this.config);
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
        panelLeftTop = new JPanel(gridBagLayout);
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
        displayItem.setDocumentText("Hello, selected reminder will be shown here!");
        displayItem.setDisabledTextColor(Color.BLACK);
        displayItem.setPreferredSize(panelLeftTop.getSize());
        displayItem.setEnabled(false);        
        htmlKit = (HTMLEditorKit) displayItem.getEditorKit();
        styledDoc = displayItem.getStyledDocument();
        undoManager = displayItem.getUndoManager();
        displayItem.setUndoAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                } catch (CannotUndoException cre) {}
            }
        });
        displayItem.setRedoAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                } catch (CannotRedoException cre) {}
            }
        });
        
        // topScrollPane
        topScrollPane = displayItem.getScrollPane();
        
        // Panel Left Top
        GridBagConstraints bc = new GridBagConstraints();
        bc.fill = GridBagConstraints.BOTH;
        bc.gridx = 0;
        bc.gridy = 0;
        bc.weightx = 0.95;
        bc.weighty = 1;
        panelLeftTop.add(topScrollPane, bc);
        
        displayItemControls = new JPanel(gridBagLayout);
        displayItemControls.setBorder(lowerEtchedBorder);
        bc.gridx = 1;
        bc.weightx = 0.05;
        panelLeftTop.add(displayItemControls, bc);
        
        addControlButtons();
        
        // Bottom Panel
        tabOne.setLayout(gridBagLayout);
        tabTwo.setLayout(gridBagLayout);
        panelLeftDown.add(tabbedPane);
        
        // ScrollPanes
        Dimension d = new Dimension(
                panelLeftDown.getWidth(), panelLeftDown.getHeight());
        scrollTabOne = new JScrollPane(tabOne);
        scrollTabOne.setPreferredSize(d);
        scrollTabTwo = new JScrollPane(tabTwo);
        scrollTabTwo.setPreferredSize(d);
        tabbedPane.add(scrollTabOne, "All");
        tabbedPane.add(scrollTabTwo, "Selected Date");
        
        listReminders(false);
        
        listReminders(true);
    }
    
    private void addControlButtons() {
        Dimension dimension = new Dimension(30, 25);
        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0;
        bc.gridy = 0;
        bc.weightx = 1;
        bc.weighty = 0.25;
        
        ImageButton editItem = new ImageButton(
                "pencil_black.png", ImageButton.EDIT, dbHandler);
        editItem.setPreferredSize(dimension);
        editItem.setToolTipText("Edit");
        editItem.setComponent(displayItem);
        editItem.create();
        
        ImageButton quickEditItem = new ImageButton(
                "instapaper_black.png", 
                ImageButton.QUICK_EDIT, dbHandler);
        quickEditItem.setPreferredSize(dimension);
        quickEditItem.setToolTipText("Quick Edit");
        quickEditItem.setComponent(displayItem);
        quickEditItem.create();
        
        ImageButton saveItem = new ImageButton(
                "content-save_black.png", ImageButton.SAVE, dbHandler);
        saveItem.setPreferredSize(dimension);
        saveItem.setToolTipText("Save Changes");
        saveItem.setComponent(displayItem);
        saveItem.create();
        
        displayItemControls.add(editItem, bc);
        bc.gridy = 1;
        displayItemControls.add(quickEditItem, bc);
        bc.gridy = 2;
        displayItemControls.add(saveItem, bc);
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
            JTextArea textArea = new JTextArea(reminder.getTitle());
            textArea.setBorder(lowerEtchedBorder);
            textArea.setLineWrap(true);
            textArea.setColumns(2);
            textArea.setBackground(contentPane.getBackground());
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayItem.setDocumentText(
                            dbHandler.getReminderWithRowID(
                                    reminder.getRowID()).getBody());
                    displayItem.setName(String.valueOf(reminder.getRowID()));
                    displayItem.setEnabled(false);
                    SwingUtilities.invokeLater(() -> {
                        topScrollPane.getVerticalScrollBar().setValue(0);
                    });
                    if (selectListItem != null) {
                        selectListItem.setBackground(contentPane.getBackground());
                    }
                    selectListItem = textArea;
                    textArea.setBackground(Color.CYAN);                    
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
