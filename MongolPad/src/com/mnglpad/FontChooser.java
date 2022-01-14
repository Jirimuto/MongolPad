package com.mnglpad;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mongol.encode.MongolianFontUtil;

/**
 *
 * @author Burhan
 */
public class FontChooser implements ActionListener, ListSelectionListener{
    static JFrame window = new JFrame("Font Chooser");
    MongolPad mnglpad;
    
    JLabel flist_label, fsize_label, fstyle_label, fprev_label, preview;
    JList flist, fsize, fstyle;
    ScrollPane flist_sc, fstyle_sc, fsize_sc;
    JButton ok, cancel;
    
    GraphicsEnvironment ge; // graphics env
    String font_names[]; // font names array
    Font selected;
    
    String font_name;
    int font_size, font_style;
    
    public FontChooser(MongolPad ref)
    {
        mnglpad  = ref;
        window.setLayout(null);
       
        MongolianFontUtil.init();
        // creating font name list
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        font_names = ge.getAvailableFontFamilyNames();
        flist = new JList(font_names);
        flist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flist_label  = new JLabel("Font: ");        
        window.add(flist_label);
        flist_label.setBounds(10, 10, 240, 20);        
        flist_sc = new ScrollPane();
        flist_sc.add(flist);
        flist_sc.setBounds(10, 30, 240, 400);
        window.add(flist_sc);
        flist.addListSelectionListener(this);
                
        // font style box
        String styles[] = {"Regular", "Bold", "Italic", "Bold Italic"};
        fstyle = new JList(styles);
        fstyle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fstyle_label = new JLabel("Style: ");
        window.add(fstyle_label);
        fstyle_label.setBounds(260, 10, 80, 20);
        fstyle_sc = new ScrollPane();
        fstyle_sc.add(fstyle);
        fstyle_sc.setBounds(260, 30, 80, 120);
        window.add(fstyle_sc);
        fstyle.addListSelectionListener(this);
        
        // font size box
        Vector<String> a = new Vector<String>(40, 1);
        for (int i = 8; i <= 100; i += 2)
            a.addElement(String.valueOf(i));
        fsize = new JList(a);
        fsize.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fsize_label = new JLabel("Size: ");
        fsize_label.setBounds(260, 160, 80, 20);
        window.add(fsize_label);
        fsize_sc = new ScrollPane();
        fsize_sc.add(fsize);
        fsize_sc.setBounds(260, 190, 80, 240);
        window.add(fsize_sc);
        fsize.addListSelectionListener(this);
        
        // ok and cancel button
        ok = new JButton("OK");
        ok.setBounds(350, 30, 75, 20);
        ok.addActionListener(this);
        window.add(ok);
        
        cancel = new JButton("Cancel");
        cancel.setBounds(350, 60, 75, 20);
        cancel.addActionListener(this);
        window.add(cancel);
        
        
        fprev_label  = new JLabel("Preview: ");
        fprev_label.setBounds(10, 230, 420, 20);
        window.add(fprev_label);
        
        // preview
        preview = new JLabel("Sample Text");
        preview.setBounds(10, 440, 410, 100);
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        preview.setOpaque(true);
        preview.setBackground(Color.white);
        preview.setBorder(new LineBorder(Color.black, 1));
        window.add(preview);
                
        int w = 540;
        int h = 600;
        window.setSize(w,h);
        // set window position
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        window.setLocation(center.x-w/2, center.y-h/2+25);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setVisible(false);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==ok)
            ok();
        else if(e.getSource()==cancel)
            cancel();
    }
    
    public void valueChanged(ListSelectionEvent l)
    {
        if(l.getSource()==flist)
        {
            preview.setText( flist.getSelectedValue().toString() );
            changeFontSample();
        }
        else if(l.getSource()==fsize)
        {
            changeFontSample();
        }
        else if(l.getSource()==fstyle)
        {
            changeFontSample();
        }
    }
    
    private void changeFontSample()
    {

        try
        {
        font_name = flist.getSelectedValue().toString();
        }
        catch(NullPointerException npe)
        {
            font_name = "Verdana";
        }
        try
        {
            font_style = getStyle();
        }
        catch(NullPointerException npe)
        {
            font_style = Font.PLAIN;
        }
        try
        {
            font_size = Integer.parseInt(fsize.getSelectedValue().toString());
        }
        catch(NullPointerException npe)
        {
            font_size = 18;
        }
        selected = new Font(font_name, font_style, font_size);
        preview.setFont(selected);
    }
    
    private int getStyle()
    {
        if( fstyle.getSelectedValue().toString().equals("Bold") )
            return Font.BOLD;
        if(fstyle.getSelectedValue().toString().equals("Italic") )
            return Font.ITALIC;
        if(fstyle.getSelectedValue().toString().equals("Bold Italic")) 
            return Font.BOLD+Font.ITALIC;
        
        return Font.PLAIN;	
    }
    
    private void ok()
    {
        try
        {
        	mnglpad.editor.setFont(selected);
        	mnglpad.editor_resize();
        }
        catch(NullPointerException npe){}
        this.window.setVisible(false);
    }
    
    private void cancel()
    {
        this.window.setVisible(false);
    }

}
