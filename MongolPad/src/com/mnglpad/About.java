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
public class About{
    static JFrame window = new JFrame("About Mongolian Notepad");
    MongolPad samp;
    JButton btn;

    public About(MongolPad ref)
    {
        samp  = ref;
        Container c = window.getContentPane();
        c.setLayout(new FlowLayout());

        String about = "<html>" +
                "<body>" +
                "Traditional Mongolian Notepad <br/>" +
                "<br/>" +
                "Created by Jirimutu@Dayaar Tech Co. Ltd & Almas Inc. <br>" +
                "All Rights Reserved by Dayaar Tech Co. Ltd & Almas Inc.<br/>" +
                "<br/><br/>" +
                "Contact: Dayaar Tech Co. Ltd & Almas Inc.<br>" +
                "E-Mail: mongol@almas.co.jp<br>" +
                "Product Web: http://www.mongolfont.com/<br/>" +
                "English: http://www.mongolfont.com/en/<br/>" +
                "Chinese: http://www.mongolfont.com/cn/<br/>" +
                "Japanese: http://www.mongolfont.com/ja/<br/><br/>" +
                "Version: 1.1.0.1<br/>" +
                "Built Date: Augest 28, 2017<br/><br/>" +
                "Dayaar Tech Co. Ltd Web.: http://www.dayaar.com.cn/<br/>" +
                "Almas Web: http://www.almas.co.jp/<br/><br/><br/>" +
                "</body>" +
                "</html>";

		URL url = this.getClass().getResource("/resources/mnglpad.png");
		if( url != null ){
	        ImageIcon icon = new ImageIcon(url);
	        JLabel l = new JLabel("", icon, SwingConstants.LEFT);
	        l.setText(about);
	        l.setVerticalTextPosition(SwingConstants.TOP);
	        l.setIconTextGap(20);
	        c.add(l);
		} else {
	        JLabel l = new JLabel("", null, SwingConstants.LEFT);
	        l.setText(about);
	        l.setVerticalTextPosition(SwingConstants.TOP);
	        l.setIconTextGap(20);
	        c.add(l);
		}

        int w = 720;
        int h = 480;
        window.setSize(w,h);
        // set window position
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        window.setLocation(center.x-w/2, center.y-h/2+25);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setVisible(false);


    }
}
