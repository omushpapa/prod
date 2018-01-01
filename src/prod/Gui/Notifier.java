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
import java.awt.event.ActionListener;
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
    
    public static final int INFORMATION = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int WARNING = 3;
    
    public static final int TOP_RIGHT = 0;
    public static final int TOP_LEFT = 1;
    public static final int BOTTOM_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    
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
    
    
    int duration = 3000;
    int position = BOTTOM_RIGHT;
    
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
    
    private int getPosition() {
        return position;
    }
    
    public void send(String title, String message) {
        SwingUtilities.invokeLater(() -> {            
            invoker(title, message);
        });
    }
    
    public void send(String title, String message, int notificationType) {
        setType(notificationType);
        SwingUtilities.invokeLater(() -> {
            invoker(title, message);
        });
    }
    
    public void send(String title, String message, int notificationType, int position) {
        setType(notificationType);
        setPosition(position);
        SwingUtilities.invokeLater(() -> {
            invoker(title, message);
        });
    }
    
    private void invoker(String title, String message) {
        Timer timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        create();
        nTitle.setText(title);
        nBody.setDocumentText(message);
        timer.start();
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
        int diff = 35;
        int x, y;
        
        switch (choice) {
            case TOP_RIGHT:
                x = maxX - diff;
                y = minY;
                break;
            case TOP_LEFT:
                x = minX;
                y = minY;
                break;
            case BOTTOM_RIGHT:
                x = maxX - diff;
                y = maxY - diff;
                break;
            case BOTTOM_LEFT:
                x = minX;
                y = maxY - diff;
                break;
            default:
                // Bottom Left
                x = minX;
                y = maxY - diff;
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
                "close-circle-outline_black.png", "Close", this);
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
            case INFORMATION:
                fileName = "close-circle-outline_black.png";
                break;
            case SUCCESS:
                fileName = "check-circle-outline_black.png";
                break;
            case ERROR:
                fileName = "alert-circle-outline_black.png";
                break;
            case WARNING:
                fileName = "warning.png";
                break;
            default:
                fileName = "close-circle-outline_black.png";
        }
        ScaledIcon icon = new ScaledIcon(fileName);
        icon.setScaledHeight(25);
        icon.setScaledWidth(25);
        return icon.getScaledIcon();
    }    
}
