/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2018, Aswa Paul
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

import prod.Gui.Buttons.CloseButton;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;

/**
 *
 * @author giantas
 */
public class Notifier extends JFrame {
        
    private final int windowWidth = 350;
    private final int windowHeight = 130;
    
    private Container contentPane; 
    private JPanel headerPanel, bodyPanel, mainPanel;
    private JLabel nTitle, iconLabel;
    private EditorPanel nBody;
    private GridBagConstraints c = new GridBagConstraints();
    private GridBagLayout gridBagLayout = new GridBagLayout();
    private int type = 0;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    private String title, message;    
    
    int duration = 3000;
    int position = NotifyPosition.BOTTOM_RIGHT;
    
    public static class NotifyType {
        public static final int INFORMATION = 0;
        public static final int SUCCESS = 1;
        public static final int ERROR = 2;
        public static final int WARNING = 3;
    }
    
    public static class NotifyPosition {
        public static final int TOP_RIGHT = 0;
        public static final int TOP_LEFT = 1;
        public static final int BOTTOM_RIGHT = 2;
        public static final int BOTTOM_LEFT = 3;
    }
    
    public Notifier () {}
    
    public Notifier (int duration) {
        this.duration = duration;
    }
    
    public Notifier (int duration, int position) {
        this.duration = duration;
        this.position = position;
    }
    
    private void setType(int type) {
        this.type = type;
    }
    
    private void setPosition(int position) {
        this.position = position;
    }
    
    private void setNotificationTitle(String title) {
        this.title = title;
    }
    
    private void setNotificationMessage(String message) {
        this.message = message;
    }
    
    private String getNotificationTitle() {
        return this.title;
    }
    
    private String getNotificationMessage() {
        return this.message;
    }
    
    private int getPosition() {
        return position;
    }
    
    public void showAndDismiss(String title, String message) {
        setNotificationTitle(title);
        setNotificationMessage(message);
        SwingUtilities.invokeLater(() -> {
            invokerTimer();
        });
    }
    
    public void showAndDismiss(String title, String message, int notificationType) {
        setNotificationTitle(title);
        setNotificationMessage(message);
        setType(notificationType);
        SwingUtilities.invokeLater(() -> {
            invokerTimer();
        });
    }
    
    public void showAndDismiss(String title, String message, int notificationType, int position) {
        setNotificationTitle(title);
        setNotificationMessage(message);
        setType(notificationType);
        setPosition(position);
        SwingUtilities.invokeLater(() -> {
            invokerTimer();
        });
    }
    
    public void showAndWait(String title, String message) {
        setNotificationTitle(title);
        setNotificationMessage(message);
        populateGui();
    }
    
    public void showAndWait(String title, String message, int notificationType) {
        setNotificationTitle(title);
        setNotificationMessage(message);
        setType(notificationType);
        populateGui();
    }
    
    public void showAndWait(String title, String message, int notificationType, int position) {
        setNotificationTitle(title);
        setNotificationMessage(message);
        setType(notificationType);
        setPosition(position);
        populateGui();
    }
    
    public void schedule(String title, String message, Date date) {
        long delay = date.getTime() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            showAndDismiss(title, message);
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    private void invokerTimer() {
        Timer timer = new Timer(duration, (ActionEvent e) -> {
            dispose();
        });
        populateGui();
        timer.start();
    }
    
    private void populateGui() {
        create();
        nTitle.setText(getNotificationTitle());
        nBody.setDocumentText(getNotificationMessage());
    }
    
    private int getScreenWorkingWidth() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds().width;
    }

    private int getScreenWorkingHeight() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds().height;
    }
    
    private Dimension getLocation(int choice) {
        Rectangle rect = new Rectangle(getScreenWorkingWidth(), getScreenWorkingHeight());
        int maxX = (int) rect.getMaxX();
        int maxY = (int) rect.getMaxY();
        int minX = (int) rect.getMinX();
        int minY = (int) rect.getMinY();
        int diff = 5;
        int x, y;
        
        switch (choice) {
            case NotifyPosition.TOP_RIGHT:
                x = maxX - windowWidth - diff;
                y = minY + diff;
                break;
            case NotifyPosition.TOP_LEFT:
                x = minX + diff;
                y = minY + diff;
                break;
            case NotifyPosition.BOTTOM_RIGHT:
                x = maxX - windowWidth - diff;
                y = maxY - windowHeight - diff;
                break;
            case NotifyPosition.BOTTOM_LEFT:
                x = minX + diff;
                y = maxY - windowHeight - diff;
                break;
            default:
                // Bottom Left
                x = minX;
                y = maxY - windowHeight;
        }
        
        return new Dimension(x,y);
    }

    public void create() {
        setUndecorated(true);
        setAlwaysOnTop(true);
        
        Dimension d = getLocation(getPosition());
        int x = d.width;
        int y = d.height;
        this.setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(windowWidth, windowHeight);
        setVisible(true);
        
        contentPane = getContentPane();
        
        mainPanel = new JPanel(gridBagLayout);
        mainPanel.setBorder(new CompoundBorder(
                new RoundedBorder(8), BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainPanel);
        
        GridBagConstraints bc = new GridBagConstraints();
        bc.fill = GridBagConstraints.BOTH;
        bc.gridx = bc.gridy = 0;
        bc.weightx = 1;
        bc.weighty = 0.02;
        
        headerPanel = createHeaderPanel();
        bodyPanel = createBodyPanel();
        
        mainPanel.add(headerPanel, bc);
        bc.weighty = 0.98;
        bc.gridy = 1;
        bc.weightx = 0;
        mainPanel.add(bodyPanel, bc);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(gridBagLayout);
        
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = GridBagConstraints.BOTH;
        cc.gridx = cc.gridy = 0;
        cc.weightx = 0.05;
        cc.weighty = 1;
        
        iconLabel = new JLabel();
        iconLabel.setIcon(getTypeIcon());
        iconLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        panel.add(iconLabel, cc);
        
        cc.gridx = 1;
        cc.weightx = 0.75;
        nTitle = new JLabel("title");
        nTitle.setVerticalAlignment(SwingConstants.BOTTOM);
        panel.add(nTitle, cc);
        
        cc.gridx = 2;
        cc.weightx = 0.2;
        cc.weighty = 0;
        cc.fill = GridBagConstraints.NONE;
        cc.anchor = GridBagConstraints.NORTHEAST;
        CloseButton close = new CloseButton(
                "close-circle-outline_black.png", "Dismiss", this);
        close.setScaledHeight(18);
        close.setScaledWidth(18);
        close.create();
        panel.add(close, cc);
        
        return panel;
    }

    private JPanel createBodyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        nBody = new EditorPanel();
        nBody.setBackground(contentPane.getBackground());
        nBody.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        JScrollPane scroller = nBody.getScrollPane();
        scroller.setViewportBorder(null);
        scroller.setBorder(null);
        scroller.setBackground(contentPane.getBackground());
        panel.add(scroller, BorderLayout.CENTER);
        return panel;
    }
    
    private ImageIcon getTypeIcon() {
        String fileName;
        switch (type) {
            case NotifyType.INFORMATION:
                fileName = "information-outline_black.png";
                break;
            case NotifyType.SUCCESS:
                fileName = "check-circle-outline_black.png";
                break;
            case NotifyType.ERROR:
                fileName = "alert-circle-outline_black.png";
                break;
            case NotifyType.WARNING:
                fileName = "warning.png";
                break;
            default:
                fileName = "close-circle-outline_black.png";
        }
        ImageScaler icon = new ImageScaler(fileName);
        icon.setScaledHeight(25);
        icon.setScaledWidth(25);
        return icon.getScaledImage();
    }
    
    public static void main(String[] args) {
        Notifier notifier = new Notifier();
        notifier.showAndDismiss("Title", "Message", 
                NotifyType.ERROR, NotifyPosition.BOTTOM_LEFT);
    }
}
