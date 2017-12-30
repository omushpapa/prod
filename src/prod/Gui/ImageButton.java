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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import prod.Database.DatabaseHandler;
import static prod.Gui.Gui.RESOURCES_PATH;
import prod.Models.Reminder;
import prod.Prod;

/**
 *
 * @author giantas
 */


public class ImageButton extends JButton implements MouseListener {
    public static final String SAVE = "save";
    public static final String QUICK_EDIT = "quickEdit";
    public static final String EDIT = "edit";
    public static final String NEW = "new";
    private static String iconFile;
    private static DatabaseHandler dbHandler;
    public Component component;
    private int scaledWidth = 15;
    private int scaledHeight = 15;
    
    public ImageButton(String iconFile, 
            String buttonName, DatabaseHandler dbHandler) {
        this.iconFile = iconFile;
        this.dbHandler = dbHandler;
        setName(buttonName);
    }
    
    public void setComponent(Component c) {
        this.component = c;
    }
    
    public void setScaledWidth(int width) {
        this.scaledWidth = width;
    }
    
    public void setScaledHeight(int height) {
        this.scaledHeight = height;
    }
    
    public int getScaledWidth() {
        return this.scaledWidth;
    }
    
    public int getScaledHeight() {
        return this.scaledHeight;
    }
    
    public void create() {
        setIcon(getScaledIcon());
        setBorder(new RoundedBorder(5));
        setContentAreaFilled(false);
        addMouseListener(this);
    }
    
    private ImageIcon getScaledIcon() {
        ImageIcon icon;
        
        try {
            Image img = ImageIO.read(
                    getClass().getResource(RESOURCES_PATH + iconFile));
            Image newimg = img.getScaledInstance(
                    getScaledWidth(), getScaledHeight(), Image.SCALE_FAST) ; 
            icon = new ImageIcon(newimg);
        } catch (IOException ex) {
            icon = null;
        }
        return icon;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        String rowString = component.getName();
        if (rowString != null) {
            int rowId = Integer.valueOf(rowString.trim());
            Reminder reminder = dbHandler.getReminderWithRowID(rowId);
            applyAction(reminder);
        } else if (getName().equals(NEW)) {
            Reminder newReminder = new Reminder(
                    "Title goes here", "More information here", 
                    "Date here (" + Reminder.dateFormat.toLowerCase() + ")");
            applyAction(newReminder);
        } else {
            JOptionPane.showMessageDialog(
                    component.getParent(), "Select a reminder first");
        }
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
        setContentAreaFilled(true);
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setContentAreaFilled(false);
    }

    private void applyAction(Reminder reminder) {
        
        switch(getName()) {
            case QUICK_EDIT:
                component.setEnabled(true);
                break;
            case SAVE:
                if (component instanceof EditorPanel) {
                    EditorPanel textPane = (EditorPanel) component;
                    String body = textPane.readDocumentContent();
                    reminder.setBody(body);
                    int result = dbHandler.updateReminder(reminder);
                    
                    String message = null;
                    if (result > 0) { 
                        message = "Changes saved successfully!";
                        
                    } else {
                        message = "Changes could NOT be saved!";
                    }
                    JOptionPane.showMessageDialog(component.getParent(), message);
                }
                break;
            case EDIT:
                ItemEditor editItem = new ItemEditor(reminder, dbHandler);
                editItem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                editItem.addWindowListener(getWindowListener());
                break;
            case NEW:
                ItemEditor newItem = new ItemEditor(reminder, dbHandler);
                newItem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                newItem.addWindowListener(getWindowListener());                
                break;
            default:
                //
                     
        }
    }
    
    private WindowListener getWindowListener() {
        JFrame frame = (JFrame) SwingUtilities.getRoot(component);
        return new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
                frame.dispose();
                new Gui(Prod.TITLE, dbHandler, Prod.config);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        };
    }
    
    private static class RoundedBorder implements Border {

        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
    
}
