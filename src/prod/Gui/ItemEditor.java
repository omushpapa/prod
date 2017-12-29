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
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import prod.Database.DatabaseHandler;
import prod.Models.Reminder;

/**
 *
 * @author giantas
 */
public class ItemEditor extends JDialog {
    private static DatabaseHandler dbHandler;
    private static final int windowWidth = 700;
    private static final int windowHeight = 550;
    private final Reminder reminder;
    private JPanel controlsPanel, bottomPanel;
    private EditorPanel textPanel;
    private FormatButton cut, copy, paste, bold, italic, 
            underline, undo, redo, save;
    private final Container contentPane;
    private JScrollPane textScrollPane;
    private JComboBox comboBox = new JComboBox();
    
    private final GridBagLayout gridBagLayout = new GridBagLayout();
    private UndoManager undoManager;
    private StyledDocument styledDoc;
    private HTMLEditorKit htmlKit;
    private String selectFont = "Arial";
    private JTextField title = new JTextField();
    private JTextField date = new JTextField();
    
    public ItemEditor(Reminder reminder, DatabaseHandler handler) {
        this.contentPane = getContentPane();
        this.reminder = reminder;
        this.dbHandler = handler;
        setTitle("title"); 
        setSize(windowWidth, windowHeight);
        setVisible(true);
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(new JPanel());
        
        createMainPanel();
        
    }

    private void createMainPanel() {
        contentPane.setLayout(gridBagLayout);
        controlsPanel = new JPanel(gridBagLayout);
        textPanel = new EditorPanel();
        htmlKit = (HTMLEditorKit) textPanel.getEditorKit();
        styledDoc = textPanel.getStyledDocument();
        undoManager = textPanel.getUndoManager();
        textPanel.setUndoAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                    updateButtons();
                } catch (CannotUndoException cre) {}
            }
        });
        textPanel.setRedoAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                    updateButtons();
                } catch (CannotRedoException cre) {}
            }
        });
        // Load text to TextPane
        textPanel.setDocumentText(reminder.getBody());
        
        // Add title and Date
        Font f = new Font(selectFont, Font.BOLD, 15);
        JPanel titlePanel = new JPanel(gridBagLayout);
        titlePanel.setBackground(Color.GRAY);
        title.setText(reminder.getTitle());
        title.setFont(f);
        date.setText(reminder.getDate());
        date.setFont(f);
        
        GridBagConstraints bcc = new GridBagConstraints();
        bcc.fill = GridBagConstraints.BOTH;
        bcc.weightx = 0.7;
        bcc.weighty = 1.0;
        bcc.gridy = 0;
        bcc.gridx = 0;
        titlePanel.add(title, bcc);
        bcc.weightx = 0.3;
        bcc.gridx = 1;
        titlePanel.add(date, bcc);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.1;
        
        contentPane.add(controlsPanel, c);
        c.weighty = 0.9;
        c.gridy = 1;
        
        bottomPanel = new JPanel(gridBagLayout);
        contentPane.add(bottomPanel, c);
        
        bcc = new GridBagConstraints();
        bcc.fill = GridBagConstraints.BOTH;
        bcc.weightx = 1.0;
        bcc.weighty = 0.03;
        bcc.gridx = 0;
        bcc.gridy = 0;
        bottomPanel.add(titlePanel, bcc);
        
        bcc.weighty = 0.97;
        bcc.gridy = 1;
        textScrollPane = textPanel.getScrollPane();
        bottomPanel.add(textScrollPane, bcc);
        
        addControls();
        addHandlers();
        updateButtons();
        
        textPanel.addMappings();
    }

    private void addControls() {
        cut = new FormatButton(
                "content-cut_black.png", new DefaultEditorKit.CutAction());
        cut.setMnemonic(KeyEvent.VK_X);
        copy = new FormatButton(
                "content-copy_black.png", new DefaultEditorKit.CopyAction());
        copy.setMnemonic(KeyEvent.VK_C);
        paste = new FormatButton(
                "content-paste_black.png", new DefaultEditorKit.PasteAction());
        undo =  new FormatButton("undo_black.png");
        redo = new FormatButton("redo_black.png");
        underline = new FormatButton(
                "format-underline_black.png", new StyledEditorKit.UnderlineAction());
        bold = new FormatButton(
                "format-bold_black.png", new StyledEditorKit.BoldAction());
        italic = new FormatButton(
                "format-italic_black.png", new StyledEditorKit.ItalicAction());
        save = new FormatButton("content-save_black.png");
        
        save.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveReminderChanges();
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 0.1;
        
        controlsPanel.add(cut, c);
        controlsPanel.add(copy, c);
        controlsPanel.add(paste, c);
        controlsPanel.add(undo, c);
        controlsPanel.add(redo, c);
        controlsPanel.add(underline, c);
        controlsPanel.add(bold, c);
        controlsPanel.add(italic, c);
        controlsPanel.add(save, c);
        
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        for ( int i = 0; i < fonts.length; i++ ) {
          comboBox.addItem(fonts[i++]);
        }
        comboBox.addActionListener((ActionEvent e) -> {
            selectFont = ((JComboBox) e.getSource()).getSelectedItem().toString();
            textPanel.setFontTo(selectFont);
        });
        controlsPanel.add(comboBox, c);
    }    
    
    public static void main(String[] args) {
        // For testing this window solo :- Do not hang me :)
        SwingUtilities.invokeLater(() -> {
            new ItemEditor(new Reminder("Sample reminder"), dbHandler);
        });
    }

    private void addHandlers() {
        styledDoc.addUndoableEditListener((UndoableEditEvent e) -> {
            updateButtons();
        });
        
        undo.addActionListener((ActionEvent e) -> {
            try {
                undoManager.undo();
                updateButtons();
            } catch (CannotUndoException cre) {}
        });
        redo.addActionListener((ActionEvent e) -> {
            try {
                undoManager.redo();
                updateButtons();
            } catch (CannotRedoException cre) {}
        });
    }
    
    private void updateButtons() {
        redo.setEnabled(undoManager.canRedo());
        undo.setEnabled(undoManager.canUndo());
    }
    
    private boolean saveReminderChanges() {
        String htmlContent = textPanel.readDocumentContent();
        
        String t = title.getText();
        String d = date.getText();
        reminder.setTitle(t);
        reminder.setBody(htmlContent);
        reminder.setDate(d);
        
        int result = dbHandler.updateReminder(reminder);
        return (result > 0);
    }
}
