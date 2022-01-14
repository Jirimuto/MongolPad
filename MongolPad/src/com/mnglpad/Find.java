package com.mnglpad;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mongol.swing.MLabel;
import com.mongol.swing.MTextField;


public class Find extends JFrame implements ActionListener
{
    int startIndex=0;
    MLabel l1, l2;
    MTextField tf, tr;
    JButton find_btn, find_next, replace, replace_all, cancel;
    
    MongolPad samp;
    
    public Find(MongolPad mynote)
    {       
        super(mynote.getResourceString("FindReplaceLabel"));
        samp = mynote;
        
        l1 = new MLabel(mynote.getResourceString("FindWhatLabel"));
        l2 = new MLabel(mynote.getResourceString("ReplaceWithLabel"));
        tf = new MTextField(60);
        tr = new MTextField(60);
        Font font = new Font("Mongolian White Pua", Font.PLAIN, 18);
        tf.setFont(font);
        tr.setFont(font);
        
        find_btn = new JButton(mynote.getResourceString("FindLabel"));
        find_next = new JButton(mynote.getResourceString("FindNextLabel"));
        replace = new JButton(mynote.getResourceString("ReplaceLabel"));
        replace_all = new JButton(mynote.getResourceString("ReplaceAllLabel"));
        cancel = new JButton(mynote.getResourceString("CancelLabel"));
        
        setLayout(null);
        int label_w = 80;
        int label_h = 20;
        int tf_w    = 120;
        
        l1.setBounds(10,10, label_w, label_h);
        add(l1);
        tf.setBounds(10+label_w, 10, tf_w, 20);
        add(tf);
        l2.setBounds(10, 10+label_h+10, label_w, label_h);
        add(l2);
        tr.setBounds(10+label_w, 10+label_h+10, tf_w, 20);
        add(tr);
                
        find_btn.setBounds(220, 10, 100, 20);
        add(find_btn);
        find_btn.addActionListener(this);
        find_next.setBounds(220, 30, 100, 20);
        add(find_next);
        find_next.addActionListener(this);
        replace.setBounds(220, 50, 100, 20);
        add(replace);
        replace.addActionListener(this);
        replace_all.setBounds(220, 70, 100, 20);
        add(replace_all);
        replace_all.addActionListener(this);
        cancel.setBounds(220, 90, 100, 20);
        add(cancel);
        cancel.addActionListener(this);
        
        int w = 340;
        int h = 150;
        
        setSize(w,h);
        // set window position
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setLocation(center.x-w/2, center.y-h/2);
        setVisible(false);
    }
    
    public void actionPerformed(ActionEvent e)
    {
         if(e.getSource()==find_btn)
         {
            find();
         }
         else if(e.getSource() == find_next)
         {
            find_next();
         }
         else if(e.getSource() == replace)
         {
             replace();
         }
         else if(e.getSource() == replace_all)
         {
            replace_all();
         }
         else if(e.getSource() == cancel)
         {
            this.setVisible(false);
         }
    }
    
    public void find()
    {
        int select_start = samp.editor.getText().indexOf(tf.getText());        
        if(select_start == -1)
        {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, samp.getResourceString("CouldNotFind")+tf.getText()+"!");
            return;
        }
        if(select_start == samp.editor.getText().lastIndexOf(tf.getText()))
        {
            startIndex = 0;
        }        
        int select_end = select_start+tf.getText().length();
        samp.editor.select(select_start, select_end);
    }
    
    public void find_next()
    {
        
        String selection = samp.editor.getSelectedText();
        try
        {
            selection.equals("");
        }
        catch(NullPointerException e)
        {
            selection = tf.getText();
            try
            {
                selection.equals("");
            }
            catch(NullPointerException e2)
            {
                selection = JOptionPane.showInputDialog(samp.getResourceString("FindLabel")+" :");
                tf.setText(selection);
            }
        }
        try
        {
            int select_start = samp.editor.getText().indexOf(selection, startIndex);
            int select_end = select_start+selection.length();
            samp.editor.select(select_start, select_end);
            startIndex = select_end+1;
        
            if(select_start == samp.editor.getText().lastIndexOf(selection))
            {
                startIndex = 0;
            }
        }
        catch(NullPointerException e)
        {}
    }
    
    public void replace()
    {
        try
        {
        find();
        samp.editor.replaceSelection(tr.getText());
        }
        catch(NullPointerException e)
        {
            System.out.print("Null Pointer Exception: "+e);
        }        
    }
    
    public void replace_all()
    {
        
        samp.editor.setText(samp.editor.getText().replaceAll(tf.getText(), tr.getText()));
    }
}
