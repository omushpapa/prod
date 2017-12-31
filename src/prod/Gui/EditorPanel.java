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
import java.awt.Cursor;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;
import org.jsoup.Jsoup;

/**
 *
 * @author giantas
 */
public class EditorPanel extends JTextPane {
    
    public HTMLEditorKit htmlKit = new HTMLEditorKit();
    public StyledDocument styledDoc;
    private JScrollPane textScrollPane = new JScrollPane();
    private final UndoManager undoManager = new UndoManager();
    private AbstractAction undoAction;
    private AbstractAction redoAction;
    
    public EditorPanel() {        
        setDocument(new DefaultStyledDocument());
        htmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
        setEditorKit(htmlKit);
        styledDoc = getStyledDocument();
        setAttributes();      
        
        textScrollPane.getVerticalScrollBar().setUnitIncrement(3);
        textScrollPane.setViewportView(this);
        textScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);        
    }
    
    public JScrollPane getScrollPane() {
        return textScrollPane;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }
    
    public void setUndoAction(AbstractAction a) {
        this.undoAction = a;
    }
    
    private AbstractAction getUndoAction() {
        return undoAction;
    }
    
    public void setRedoAction(AbstractAction a) {
        this.redoAction = a;
    }
    
    private AbstractAction getRedoAction() {
        return redoAction;
    }
    
    public void setDocumentText(String text) {
        emptyDocument();
        StringReader reader = new StringReader(text);
        try {
            htmlKit.read(reader, styledDoc, 0);
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(ItemEditor.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    public void emptyDocument() {
        try {
            styledDoc.remove(0, styledDoc.getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String readDocumentContent() {
        StringWriter writer = new StringWriter();
        try {
            htmlKit.write(writer, styledDoc, 0, styledDoc.getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(ItemEditor.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return writer.toString();
    }
    
    private String html2Text(String html) {
        return Jsoup.parse(html).text();
    }
    
    public String readDocumentText() {
        return html2Text(readDocumentContent());
    }
    
    public String readDocumentText(String htmlContent) {
        return html2Text(htmlContent);
    }
    
    private void setAttributes() {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, Color.BLACK);
        StyleConstants.setFontFamily(attributeSet, "Arial");
        StyleConstants.setFontSize(attributeSet, 14);

        styledDoc.setCharacterAttributes(0, 
                styledDoc.getLength(), attributeSet, false);
    }
    
    public void setFontTo(String font) {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet, font);

        styledDoc.setCharacterAttributes(0, 
                styledDoc.getLength(), attributeSet, false);
    }

    public void addMappings() {
        // Always place after updating document with text
        styledDoc.addUndoableEditListener((UndoableEditEvent e) -> {
            undoManager.addEdit(e.getEdit());
        });
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, Event.CTRL_MASK);
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, Event.CTRL_MASK);
        KeyStroke boldKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_B, Event.CTRL_MASK);
        KeyStroke italicKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_I, Event.CTRL_MASK);
        KeyStroke underlineKeyStroke = KeyStroke.getKeyStroke(
                KeyEvent.VK_U, Event.CTRL_MASK);
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(boldKeyStroke, "boldKeyStroke");
        getActionMap().put(
                "boldKeyStroke", new StyledEditorKit.BoldAction());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(italicKeyStroke, "italicKeyStroke");
        getActionMap().put(
                "italicKeyStroke", new StyledEditorKit.ItalicAction());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(underlineKeyStroke, "underlineKeyStroke");
        getActionMap().put(
                "underlineKeyStroke", new StyledEditorKit.UnderlineAction());
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(undoKeyStroke, "undoKeyStroke");
        getActionMap().put("undoKeyStroke", getUndoAction());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(redoKeyStroke, "redoKeyStroke");
        getActionMap().put("redoKeyStroke", getRedoAction());
    }
    
    
}
