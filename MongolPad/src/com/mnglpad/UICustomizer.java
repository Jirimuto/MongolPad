package com.mnglpad;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class UICustomizer {

	private static HashMap<String, FontUIResource> fontMap = new HashMap<String, FontUIResource>();
	
    public UICustomizer()
    {
    	initFontUIResource();
    }

    public void initFontUIResource()
    {
        UIDefaults defaultTable = UIManager.getLookAndFeelDefaults();
        for(Object o: defaultTable.keySet() )
        {
            if(o.toString().toLowerCase().endsWith("font") )
            {
                FontUIResource font = (FontUIResource)UIManager.get(o);
                fontMap.put(o.toString(), font);
            }
        }
    }

    public void setMongolianUIFont(final int fontSizeInc )
    {
        UIDefaults defaultTable = UIManager.getLookAndFeelDefaults();
        for(Object o: defaultTable.keySet() )
        {
            if(o.toString().toLowerCase().endsWith("font") )
            {
                FontUIResource font = (FontUIResource)UIManager.get(o);
                if( fontMap.get(o.toString())==null )
                    fontMap.put(o.toString(), font);
                // System.out.println("ResourceName: " + o.toString() );
                if( "Menu.font".equalsIgnoreCase(o.toString()) 
                		|| "MenuItem.font".equalsIgnoreCase(o.toString()) 
                		|| "CheckBoxMenuItem.font".equalsIgnoreCase(o.toString()) 
                		|| "RadioButtonMenuItem.font".equalsIgnoreCase(o.toString()) 
                		|| "ToolBar.font".equalsIgnoreCase(o.toString()) 
                		|| "MenuBar.font".equalsIgnoreCase(o.toString()) 
                		|| "Label.font".equalsIgnoreCase(o.toString()) 
                		|| "ToolTip.font".equalsIgnoreCase(o.toString()) ){
                	font = new FontUIResource("Mongolian White Pua", font.getStyle(), font.getSize()+fontSizeInc);
                }
                UIManager.put(o, font);
            }
        }
    }

    public void setDefaultUIFont()
    {
        UIDefaults defaultTable = UIManager.getLookAndFeelDefaults();
        for(Object o: defaultTable.keySet() )
        {
            if(o.toString().toLowerCase().endsWith("font") )
            {
                FontUIResource font = fontMap.get(o.toString());
                if( font !=null )
                	UIManager.put(o, font);
            }
        }
    }
    
    public static void setFontSize(final int fontSize)
    {
        UIDefaults defaultTable = UIManager.getLookAndFeelDefaults();
        for(Object o: defaultTable.keySet() )
        {
            if(o.toString().toLowerCase().endsWith("font") )
            {
                FontUIResource font = (FontUIResource)UIManager.get(o);
                font = new FontUIResource(font.getName(), font.getStyle(), fontSize);
                UIManager.put(o, font);
            }
        }
    }

    public static void setScrollBarWidth(final int width)
    {
        UIManager.put("ScrollBar.width", width);
    }

    public static void updateUiAll(JComponent component)
    {
    	SwingUtilities.updateComponentTreeUI(component);
    }
}