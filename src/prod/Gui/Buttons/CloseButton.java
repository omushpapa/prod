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
package prod.Gui.Buttons;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import prod.Gui.ImageScaler;
import prod.Gui.RoundedBorder;

/**
 *
 * @author giantas
 */


public class CloseButton extends JButton implements MouseListener {
    private static String iconFile;
    private int scaledWidth = 10;
    private int scaledHeight = 10;
    private Component component;
    
    public CloseButton(String iconFile, String buttonName, Component c) {
        this.iconFile = iconFile;
        this.setToolTipText(buttonName);
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
        ImageScaler scaler = new ImageScaler(iconFile, 
                getScaledWidth(), getScaledHeight());
        setIcon(scaler.getScaledImage());
        setBorder(new RoundedBorder(5));
        setContentAreaFilled(false);
        addMouseListener(this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        JFrame f = (JFrame) component;
        f.dispose();
        component.setVisible(false);
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
}
