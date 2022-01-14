package com.mnglpad;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Jirimuto
 */
public class MnKeyboardLayout{
    static JFrame window = new JFrame("Mongolian Keyboard Layout");
    MongolPad mnglpad;
    JButton btn;

    public MnKeyboardLayout(MongolPad ref)
    {
        mnglpad  = ref;
        Container c = window.getContentPane();
        c.setLayout(new FlowLayout());

        String title = "<html>" +
                "<body>" +
                "<br/>" +
                "</body>" +
                "</html>";

		URL url = this.getClass().getResource("/resources/mkeyboard.png");
		if( url != null ){
	        ImageIcon icon = new ImageIcon(url);
	        JLabel l = new JLabel("", icon, SwingConstants.LEFT);
	        l.setText(title);
	        l.setVerticalTextPosition(SwingConstants.CENTER);
	        l.setIconTextGap(20);
	        c.add(l);
		} else {
	        JLabel l = new JLabel("", null, SwingConstants.LEFT);
	        l.setText(title);
	        l.setVerticalTextPosition(SwingConstants.CENTER);
	        l.setIconTextGap(20);
	        c.add(l);
		}

        int w = 1080;
        int h = 640;
        window.setSize(w,h);
        // set window position
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        window.setLocation(center.x-w/2, center.y-h/2+25);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setVisible(false);


    }
}
