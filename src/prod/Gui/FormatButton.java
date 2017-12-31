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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import static prod.Gui.Gui.RESOURCES_PATH;

/**
 *
 * @author giantas
 */
public class FormatButton extends JButton implements MouseListener {
    
    private String iconFile;
    private int scaledWidth = 20;
    private int scaledHeight = 20;
    
    public FormatButton(String iconFile) {
        this.iconFile = iconFile;
        setDefaults();
    }
    
    public FormatButton(String iconFile, Action a) {
        this.iconFile = iconFile;
        setAction(a);
        addMouseListener(this);
        setDefaults();
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
    
    public void setDefaults() {
        setIcon(getScaledIcon());
        setBorder(new RoundedBorder(5));
        setContentAreaFilled(false);
        setText("");
        setEnabled(true);
    }
    
    private ImageIcon getScaledIcon() {
        ImageIcon icon;
        
        try {
            Image img = ImageIO.read(
                    getClass().getClassLoader().getResourceAsStream(
                            RESOURCES_PATH + iconFile));
            Image newimg = img.getScaledInstance(
                    getScaledWidth(), getScaledHeight(), Image.SCALE_FAST) ; 
            icon = new ImageIcon(newimg);
        } catch (IOException ex) {
            icon = null;
        }
        return icon;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        setContentAreaFilled(true);
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setContentAreaFilled(false);
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
