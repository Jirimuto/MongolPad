package com.mnglpad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.mongol.encode.MongolianConverter;
import com.mongol.encode.MongolianFontUtil;
import com.mongol.swing.MSwingRotateUtilities;
import com.mongol.swing.MTextArea;

/**
 * Mongolian NotePad simple text editor
 * supports only one font.
 *
 * @author  jrmt@Almas Inc.
 * @version 1.00 2011/11/05
 */
public class MongolPad extends JFrame implements ActionListener, Printable
{
    Container container;
    public JScrollPane scrollpane;
    public MTextArea editor;

    private static final InputContext context = InputContext.getInstance();
    private static UICustomizer mnglpadUI;

    private int WINDOW_WIDTH = 1024;
    private int WINDOW_HEIGHT = 748;

    private boolean mongolian_keyboard_enable = false;

	/**
	 * Suffix applied to the key used in resource file
	 * lookups for an image.
	 */
	public static final String imageSuffix = "Image";

	/**
	 * Suffix applied to the key used in resource file
	 * lookups for an action.
	 */
	public static final String actionSuffix = "Action";

	/**
	 * Suffix applied to the key used in resource file
	 * lookups for tooltip text.
	 */
	public static final String tipSuffix = "Tooltip";

    private JMenuBar menubar;
	private JToolBar toolbar;
	private JComponent status;

    private JMenu file;
    private JMenuItem file_new;
    private JMenuItem file_open;
    private JSeparator file_sep1;
    private JMenuItem file_save;
    private JMenuItem file_save_as;
    private JSeparator file_sep2;
    private JMenuItem file_print;
    private JSeparator file_sep3;
    private JMenuItem file_close;
    private JMenuItem file_exit;

    private JMenu edit;
    private JMenuItem edit_undo;
    private JMenuItem edit_redo;
    private JSeparator edit_sep1;
    private JMenuItem edit_copy;
    private JMenuItem edit_cut;
    private JMenuItem edit_paste;
    private JMenuItem edit_delete;
    private JSeparator edit_sep2;
    private JMenuItem edit_find;
    private JMenuItem edit_find_next;
    private JMenuItem edit_replace;
    private JSeparator edit_sep3;
    private JMenuItem edit_selectall;
    private JMenuItem edit_timedate;

    private JMenu format;
    private JMenuItem format_font;
    private JSeparator format_sep1;
    private JMenu keyboard;
    private JCheckBoxMenuItem keyboard_mn;
    private JMenuItem keyborad_layout_mn;
    private JSeparator format_sep2;
    private JMenu language;
    private JCheckBoxMenuItem language_en;
    private JCheckBoxMenuItem language_cn;
    private JCheckBoxMenuItem language_ja;
    private JCheckBoxMenuItem language_mn;
    private JSeparator format_sep3;
    private JMenu convert;
    private JMenuItem str2uppr, str2lwr;
    private JCheckBoxMenuItem format_wordwarp;

    private JMenu help;
    private JMenuItem help_about;

    private JButton button_about;

    private JButton button_new;
    private JButton button_open;
    private JButton button_save;
    private JButton button_save_as;

    private JButton button_copy;
    private JButton button_cut;
    private JButton button_paste;
    private JButton button_delete;

    private JButton button_find;
    private JButton button_find_next;
    private JButton button_replace;

    private JButton button_font;
    private JButton button_print;
    private JButton button_exit;
    Dimension winSize;
    Rectangle editorBounds;

    UndoManager undo = new UndoManager();
    UIManager.LookAndFeelInfo lnf[];

    Find finder;
    FontChooser fontchooser;
    MnKeyboardLayout mnKeyboardLayout;
    About about;

    String path, content;

	private static Locale locale;

	private static ResourceBundle resources;

	static {
			locale = new Locale(Locale.CHINESE.getLanguage());
			locale = Locale.getDefault();
			resources = getResouceBundle( locale );
			context.selectInputMethod(locale);
	}

	public static  ResourceBundle getResouceBundle( Locale locale)  {

		ResourceBundle result = null;
		try {
			result = ResourceBundle.getBundle("resources.MongolPad", locale);
		} catch (MissingResourceException mre) {
			try {
				Locale defaultlocale = Locale.getDefault();
				result = ResourceBundle.getBundle("resources.MongolPad", defaultlocale);
			} catch (MissingResourceException mre1) {
				System.err.println("resources/MongolPad.properties not found");
				System.exit(1);
			}
		}
		return result;
	}

    public MongolPad(String filename){

        super("Untitled - Mongolpad");
        try
        {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        MongolianFontUtil.init();

        Container c = getContentPane();
        editor = new MTextArea() {

        	@Override
            public InputContext getInputContext() {
                 return context;
            }
        };
        Character.Subset[] subsets = new Character.Subset[] {java.awt.im.InputSubset.KANJI};
        context.setCharacterSubsets(subsets);

		editor.setRotateDirection(MSwingRotateUtilities.ROTATE_VERTICAL);
		editor.setRotateHint(MSwingRotateUtilities.ROTATE_LEFTTORIGHT);
		editor.setDragEnabled(true);
		editor.setLineWrap(true);
		editor.setWrapStyleWord(true);
        Font defaultFont = new Font("Mongolian White", Font.PLAIN, 24);
		editor.setFont(defaultFont);
        scrollpane = new JScrollPane(editor, scrollpane.VERTICAL_SCROLLBAR_AS_NEEDED, scrollpane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //adding scrollbar to text area;
        c.add(scrollpane);

        KeyListener keyListener = new KeyListener() {

            public void keyPressed(KeyEvent keyEvent) {
            	;
            }

            public void keyReleased(KeyEvent keyEvent) {
            	;
            }

            public void keyTyped(KeyEvent keyEvent) {
            	if( mongolian_keyboard_enable ) {
            		keyEvent = MongolianKeyboard.convert(keyEvent);
            	}
            }

          };
          editor.addKeyListener(keyListener);


        menubar = createMenubar();
        setJMenuBar(menubar);

        toolbar = createToolbar();
        c.add(toolbar, BorderLayout.NORTH);

        mnglpadUI = new UICustomizer();

        this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentResized(ComponentEvent evt) {
        		Component c = (Component)evt.getSource();
        		// Get new size
        		Dimension newSize = c.getSize();
        		if( winSize != null ){
	        		if( newSize.height != winSize.height ){
	        			int scrollHight = editor.getParent().getHeight();
	        			if( scrollpane.getHorizontalScrollBar().isShowing() )
	        				editor.setSize(Integer.MAX_VALUE,  scrollHight-20 );
	        			else
	        				editor.setSize(Integer.MAX_VALUE,  scrollHight-20 );
	        		}
        		}

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
			}
		});

		add(createStatusbar(), BorderLayout.SOUTH);

        // undo manager
        Document doc= editor.getDocument();
        doc.addUndoableEditListener(
                new UndoableEditListener( )
                {
                    public void undoableEditHappened( UndoableEditEvent event )
                    {
                        undo.addEdit(event.getEdit());
                    }
                }
        );

        // find_window
        finder = new Find(this);
        finder.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // font chooser
        fontchooser = new FontChooser(this);
        mnKeyboardLayout = new MnKeyboardLayout(this);
        about = new About(this);

        // set window size
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        // set window position
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setLocation(center.x-WINDOW_WIDTH/2, center.y-WINDOW_HEIGHT/2);
        this.setVisible(true);
        path = "";
        winSize = this.getSize();
        editorBounds = editor.getBounds();
        editor_resize();

        if( filename != null ){
        	file_load(filename);
        }

    }

    public void editor_resize() {

		Dimension newSize = getSize();
		if( winSize != null ){
    		if( newSize.height != winSize.height ){
    			int scrollHight = editor.getParent().getHeight();
    			if( scrollpane.getHorizontalScrollBar().isShowing() )
    				editor.setSize(Integer.MAX_VALUE,  scrollHight-20 );
    			else
    				editor.setSize(Integer.MAX_VALUE,  scrollHight-20 );
    		}
		}

    }
	/**
	 * Create the menubar for the app.  By default this pulls the
	 * definition of the menu from the associated resource file.
	 */
	protected JMenuBar createMenubar() {

		JMenuBar mb = new JMenuBar();

        file = new JMenu(getResourceString("FileLabel"));
        file_new = new JMenuItem(getResourceString("NewLabel"));
		URL url = getResource("New" + imageSuffix);
		if (url != null) {
			file_new.setHorizontalTextPosition(JButton.RIGHT);
			file_new.setIcon(new ImageIcon(url));
		}
        file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        file_new.addActionListener(this);
        file.add(file_new);
        file_open = new JMenuItem(getResourceString("OpenLabel"));
		url = getResource("Open" + imageSuffix);
		if (url != null) {
			file_open.setHorizontalTextPosition(JButton.RIGHT);
			file_open.setIcon(new ImageIcon(url));
		}
        file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        file_open.addActionListener(this);
        file.add(file_open);
        file_sep1 = new JSeparator();
        file.add(file_sep1);
        file_save = new JMenuItem(getResourceString("SaveLabel"));
		url = getResource("Save" + imageSuffix);
		if (url != null) {
			file_save.setHorizontalTextPosition(JButton.RIGHT);
			file_save.setIcon(new ImageIcon(url));
		}
        file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        file_save.addActionListener(this);
        file.add(file_save);
        file_save_as = new JMenuItem(getResourceString("SaveAsLabel"));
		url = getResource("SaveAs" + imageSuffix);
		if (url != null) {
			file_save_as.setHorizontalTextPosition(JButton.RIGHT);
			file_save_as.setIcon(new ImageIcon(url));
		}
        file_save_as.addActionListener(this);
        file.add(file_save_as);
        file_sep2 = new JSeparator();
        file.add(file_sep2);

        file_print = new JMenuItem(getResourceString("PrintLabel"));
		url = getResource("Print" + imageSuffix);
		if (url != null) {
			file_print.setHorizontalTextPosition(JButton.RIGHT);
			file_print.setIcon(new ImageIcon(url));
		}
        file_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        file_print.addActionListener(this);
        file.add(file_print);
        file_sep3 = new JSeparator();
        file.add(file_sep3);

        file_close = new JMenuItem(getResourceString("CloseLabel"));
		url = getResource("Close" + imageSuffix);
		if (url != null) {
			file_close.setHorizontalTextPosition(JButton.RIGHT);
			file_close.setIcon(new ImageIcon(url));
		}
        file_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
        file_close.addActionListener(this);
        file.add(file_close);
        file_exit = new JMenuItem(getResourceString("ExitLabel"));
		url = getResource("Exit" + imageSuffix);
		if (url != null) {
			file_close.setHorizontalTextPosition(JButton.RIGHT);
			file_close.setIcon(new ImageIcon(url));
		}
        file_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        file_exit.addActionListener(this);
        file.add(file_exit);
        mb.add(file);

        edit = new JMenu(getResourceString("EditLabel"));
        edit_undo = new JMenuItem(getResourceString("UndoLabel"));
		url = getResource("Undo" + imageSuffix);
		if (url != null) {
			edit_undo.setHorizontalTextPosition(JButton.RIGHT);
			edit_undo.setIcon(new ImageIcon(url));
		}
        edit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        edit_undo.addActionListener(this);
        edit.add(edit_undo);
        edit_redo = new JMenuItem(getResourceString("RedoLabel"));
		url = getResource("Redo" + imageSuffix);
		if (url != null) {
			edit_redo.setHorizontalTextPosition(JButton.RIGHT);
			edit_redo.setIcon(new ImageIcon(url));
		}
        edit_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        edit_redo.addActionListener(this);
        edit.add(edit_redo);
        edit_sep1 = new JSeparator();
        edit.add(edit_sep1);
        edit_copy = new JMenuItem(getResourceString("CopyLabel"));
		url = getResource("Copy" + imageSuffix);
		if (url != null) {
			edit_copy.setHorizontalTextPosition(JButton.RIGHT);
			edit_copy.setIcon(new ImageIcon(url));
		}
        edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        edit_copy.addActionListener(this);
        edit.add(edit_copy);
        edit_cut = new JMenuItem(getResourceString("CutLabel"));
		url = getResource("Cut" + imageSuffix);
		if (url != null) {
			edit_cut.setHorizontalTextPosition(JButton.RIGHT);
			edit_cut.setIcon(new ImageIcon(url));
		}
        edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        edit_cut.addActionListener(this);
        edit.add(edit_cut);
        edit_paste = new JMenuItem(getResourceString("PasteLabel"));
		url = getResource("Paste" + imageSuffix);
		if (url != null) {
			edit_paste.setHorizontalTextPosition(JButton.RIGHT);
			edit_paste.setIcon(new ImageIcon(url));
		}
        edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        edit_paste.addActionListener(this);
        edit.add(edit_paste);
        edit_delete = new JMenuItem(getResourceString("DeleteLabel"));
		url = getResource("Delete" + imageSuffix);
		if (url != null) {
			edit_delete.setHorizontalTextPosition(JButton.RIGHT);
			edit_delete.setIcon(new ImageIcon(url));
		}
        edit_delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        edit_delete.addActionListener(this);
        edit.add(edit_delete);
        edit_sep2 = new JSeparator();
        edit.add(edit_sep2);
        edit_find = new JMenuItem(getResourceString("FindLabel"));
		url = getResource("Find" + imageSuffix);
		if (url != null) {
			edit_find.setHorizontalTextPosition(JButton.RIGHT);
			edit_find.setIcon(new ImageIcon(url));
		}
        edit_find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        edit_find.addActionListener(this);
        edit.add(edit_find);
        edit_find_next = new JMenuItem(getResourceString("FindNextLabel"));
		url = getResource("FindNext" + imageSuffix);
		if (url != null) {
			edit_find_next.setHorizontalTextPosition(JButton.RIGHT);
			edit_find_next.setIcon(new ImageIcon(url));
		}
        edit_find_next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        edit_find_next.addActionListener(this);
        edit.add(edit_find_next);
        edit_replace = new JMenuItem(getResourceString("ReplaceLabel"));
		url = getResource("Replace" + imageSuffix);
		if (url != null) {
			edit_replace.setHorizontalTextPosition(JButton.RIGHT);
			edit_replace.setIcon(new ImageIcon(url));
		}
        edit_replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        edit_replace.addActionListener(this);
        edit.add(edit_replace);
        edit_sep3 = new JSeparator();
        edit.add(edit_sep3);
        edit_selectall = new JMenuItem(getResourceString("SelectAllLabel"));
        edit_selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        edit_selectall.addActionListener(this);
        edit.add(edit_selectall);
        edit_timedate = new JMenuItem(getResourceString("DateTimeLabel"));
		url = getResource("DateTime" + imageSuffix);
		if (url != null) {
			edit_timedate.setHorizontalTextPosition(JButton.RIGHT);
			edit_timedate.setIcon(new ImageIcon(url));
		}
        edit_timedate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        edit_timedate.addActionListener(this);
        edit.add(edit_timedate);
        mb.add(edit);

        format = new JMenu(getResourceString("FormatLabel"));
        format_font = new JMenuItem(getResourceString("FontLabel"));
		url = getResource("Font" + imageSuffix);
		if (url != null) {
			format_font.setHorizontalTextPosition(JButton.RIGHT);
			format_font.setIcon(new ImageIcon(url));
		}
        format_font.addActionListener(this);
        format.add(format_font);
        format_sep1 = new JSeparator();
        format.add(format_sep1);

        keyboard = new JMenu(getResourceString("KeyboardLabel"));
        keyboard_mn = new JCheckBoxMenuItem(getResourceString("KeyboardMongolianLabel"));
        keyboard_mn.addActionListener(this);
        keyboard_mn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
       	keyboard_mn.setSelected(mongolian_keyboard_enable);
        keyboard.add(keyboard_mn);
        keyborad_layout_mn = new JMenuItem(getResourceString("KeyboardLayoutMongolianLabel"));
        keyborad_layout_mn.addActionListener(this);
        keyboard.add(keyborad_layout_mn);
        format.add(keyboard);

        format_sep2 = new JSeparator();
        format.add(format_sep2);
        language = new JMenu(getResourceString("LanguageLabel"));
        language_mn = new JCheckBoxMenuItem(getResourceString("LanguageMongolianLabel"));
        language_mn.addActionListener(this);
        language.add(language_mn);
        language_cn = new JCheckBoxMenuItem(getResourceString("LanguageChineseLabel"));
        language_cn.addActionListener(this);
        language.add(language_cn);
        language_en = new JCheckBoxMenuItem(getResourceString("LanguageEnglishLabel"));
        language_en.addActionListener(this);
        language.add(language_en);
        language_ja = new JCheckBoxMenuItem(getResourceString("LanguageJapaneseLabel"));
        language_ja.addActionListener(this);
        language.add(language_ja);
        format.add(language);

        format_sep3 = new JSeparator();
        format.add(format_sep3);
        convert = new JMenu(getResourceString("ConvertLabel"));
        str2uppr = new JMenuItem(getResourceString("ToUppercaseLabel"));
        str2uppr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK));
        str2uppr.addActionListener(this);
        convert.add(str2uppr);
        str2lwr = new JMenuItem(getResourceString("ToLowercaseLabel"));
        str2lwr.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK));
        str2lwr.addActionListener(this);
        convert.add(str2lwr);
        format.add(convert);

        format_wordwarp = new JCheckBoxMenuItem(getResourceString("WordWrapLabel"));
        format_wordwarp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        format_wordwarp.addActionListener(this);
        format_wordwarp.setState(editor.getLineWrap());
        format.add(format_wordwarp);
        mb.add(format);

        help = new JMenu(getResourceString("HelpLabel"));
        help_about = new JMenuItem(getResourceString("AboutLabel"));
		url = getResource("About" + imageSuffix);
		if (url != null) {
			help_about.setHorizontalTextPosition(JButton.RIGHT);
			help_about.setIcon(new ImageIcon(url));
		}
        help_about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        help_about.addActionListener(this);
        help.add(help_about);
        mb.add(help);

        if( locale != null ) {
	    	if( "CN".equalsIgnoreCase(locale.getLanguage()))
	        	language_cn.setState(true);
	    	if( "EN".equalsIgnoreCase(locale.getLanguage()))
	        	language_en.setState(true);
	    	if( "MN".equalsIgnoreCase(locale.getLanguage()))
	        	language_mn.setState(true);
	    	if( "JA".equalsIgnoreCase(locale.getLanguage()))
	        	language_ja.setState(true);
        }

		return mb;
	}

	/**
	 * Exchange MenuBar Text. Using predefined Locale based resource file
	 */
	private void exchangeMenubarText() {

        file.setText(getResourceString("FileLabel"));
        file_new.setText(getResourceString("NewLabel"));
        file_open.setText(getResourceString("OpenLabel"));
        file_save.setText(getResourceString("SaveLabel"));
        file_save_as.setText(getResourceString("SaveAsLabel"));
        file_print.setText(getResourceString("PrintLabel"));
        file_close.setText(getResourceString("CloseLabel"));
        file_exit.setText(getResourceString("ExitLabel"));

        edit.setText(getResourceString("EditLabel"));
        edit_undo.setText(getResourceString("UndoLabel"));
        edit_redo.setText(getResourceString("RedoLabel"));
        edit_copy.setText(getResourceString("CopyLabel"));
        edit_cut.setText(getResourceString("CutLabel"));
        edit_paste.setText(getResourceString("PasteLabel"));
        edit_delete.setText(getResourceString("DeleteLabel"));
        edit_find.setText(getResourceString("FindLabel"));
        edit_find_next.setText(getResourceString("FindNextLabel"));
        edit_replace.setText(getResourceString("ReplaceLabel"));
        edit_selectall.setText(getResourceString("SelectAllLabel"));
        edit_timedate.setText(getResourceString("DateTimeLabel"));

        format.setText(getResourceString("FormatLabel"));
        format_font.setText(getResourceString("FontLabel"));

        keyboard.setText(getResourceString("KeyboardLabel"));
        keyboard_mn.setText(getResourceString("KeyboardMongolianLabel"));
        keyborad_layout_mn.setText(getResourceString("KeyboardLayoutMongolianLabel"));

        language.setText(getResourceString("LanguageLabel"));
        language_mn.setText(getResourceString("LanguageMongolianLabel"));
        language_cn.setText(getResourceString("LanguageChineseLabel"));
        language_en.setText(getResourceString("LanguageEnglishLabel"));
        language_ja.setText(getResourceString("LanguageJapaneseLabel"));

        convert.setText(getResourceString("ConvertLabel"));
        str2uppr.setText(getResourceString("ToUppercaseLabel"));
        str2lwr.setText(getResourceString("ToLowercaseLabel"));

        format_wordwarp.setText(getResourceString("WordWrapLabel"));

        help.setText(getResourceString("HelpLabel"));
        help_about.setText(getResourceString("AboutLabel"));

	}

	/**
	 * Create the toolbar.  By default this reads the
	 * resource file for the definition of the toolbar.
	 */
	private JToolBar createToolbar() {

		JToolBar tb = new JToolBar();

		button_new = createTool("New");
		tb.add(button_new);
		button_open = createTool("Open");
		tb.add(button_open);
		button_save = createTool("Save");
		tb.add(button_save);
		button_save_as = createTool("SaveAs");
		tb.add(button_save_as);

		tb.add(Box.createHorizontalStrut(5));

		button_copy = createTool("Copy");
		tb.add(button_copy);
		button_cut = createTool("Cut");
		tb.add(button_cut);
		button_paste = createTool("Paste");
		tb.add(button_paste);
		button_delete = createTool("Delete");
		tb.add(button_delete);

		tb.add(Box.createHorizontalStrut(5));

		button_find = createTool("Find");
		tb.add(button_find);
		button_find_next = createTool("FindNext");
		tb.add(button_find_next);
		button_replace = createTool("Replace");
		tb.add(button_replace);

		tb.add(Box.createHorizontalStrut(5));

		button_font = createTool("Font");
		tb.add(button_font);
		button_print = createTool("Print");
		tb.add(button_print);
		button_about = createTool("About");
		tb.add(button_about);

		tb.add(Box.createHorizontalStrut(5));

		button_exit = createTool("Exit");
		tb.add(button_exit);

		tb.add(Box.createHorizontalGlue());
		return tb;
	}

	/**
	 * Exchange the toolbar text using predefined resource file
	 */
	private void exchangeToolbartext() {

		button_new.setToolTipText(getResourceString("New" + tipSuffix));
		button_open.setToolTipText(getResourceString("Open" + tipSuffix));
		button_save.setToolTipText(getResourceString("Save" + tipSuffix));
		button_save_as.setToolTipText(getResourceString("SaveAs" + tipSuffix));

		button_copy.setToolTipText(getResourceString("Copy" + tipSuffix));
		button_cut.setToolTipText(getResourceString("Cut" + tipSuffix));
		button_paste.setToolTipText(getResourceString("Paste" + tipSuffix));
		button_delete.setToolTipText(getResourceString("Delete" + tipSuffix));

		button_find.setToolTipText(getResourceString("Find" + tipSuffix));
		button_find_next.setToolTipText(getResourceString("FindNext" + tipSuffix));
		button_replace.setToolTipText(getResourceString("Replace" + tipSuffix));

		button_font.setToolTipText(getResourceString("Font" + tipSuffix));
		button_print.setToolTipText(getResourceString("Print" + tipSuffix));
		button_about.setToolTipText(getResourceString("About" + tipSuffix));

		button_exit.setToolTipText(getResourceString("Exit" + tipSuffix));

	}


	/**
	 * Hook through which every toolbar item is created.
	 */
	protected JButton createTool(String key) {
		return createToolbarButton(key);
	}

	/**
	 * Create a button to go inside of the toolbar.  By default this
	 * will load an image resource.  The image filename is relative to
	 * the classpath (including the '.' directory if its a part of the
	 * classpath), and may either be in a JAR file or a separate file.
	 *
	 * @param key The key in the resource file to serve as the basis
	 *  of lookups.
	 */
	protected JButton createToolbarButton(String key) {

		URL url = getResource(key + imageSuffix);
		JButton b = new JButton(new ImageIcon(url)) {
			public float getAlignmentY() { return 0.5f; }
		};
		b.setRequestFocusEnabled(false);
		b.setMargin(new Insets(1,1,1,1));

		String astr = getResourceString(key + actionSuffix);
		if (astr == null) {
			astr = key;
		}

		b.setActionCommand(astr);
		b.addActionListener(this);

		String tip = getResourceString(key + tipSuffix);
		if (tip != null) {
			b.setToolTipText(tip);
		}

		return b;
	}

	/**
	 * Create a status bar
	 */
	protected Component createStatusbar() {
		// need to do something reasonable here
		status = new StatusBar();
		return status;
	}

	public String getResourceString(String nm) {
		String str;
		try {
			str = resources.getString(nm);
			// Jirimuto added --2017/08/22
			if( "MN".equalsIgnoreCase(locale.getLanguage())){
				MongolianConverter converter = new MongolianConverter();
				str = converter.convert(str);
			}
		} catch (MissingResourceException mre) {
			str = null;
		}
		return str;
	}

	protected URL getResource(String key) {
		String name = getResourceString(key);
		if (name != null) {
			URL url = this.getClass().getResource(name);
			return url;
		}
		return null;
	}

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==file_new || e.getSource()==button_new )
            file_new();
        else if(e.getSource()==file_open || e.getSource()==button_open )
            file_open();
        else if(e.getSource()==file_save || e.getSource()==button_save )
            file_save();
        else if(e.getSource()==file_save_as || e.getSource()==button_save_as )
            file_save_as();
        else if(e.getSource()==file_print  || e.getSource()==button_print )
            file_print();
        else if(e.getSource()==file_close )
            file_close();
        else if(e.getSource()==file_exit  || e.getSource()==button_exit )
            file_exit();


        else if(e.getSource()==edit_undo)
            edit_undo();
        else if(e.getSource()==edit_redo)
            edit_redo();
        else if(e.getSource()==edit_cut || e.getSource()==button_cut)
            edit_cut();
        else if(e.getSource()==edit_copy || e.getSource()==button_copy)
            edit_copy();
        else if(e.getSource()==edit_paste || e.getSource()==button_paste)
            edit_paste();
        else if(e.getSource()==edit_delete  || e.getSource()==button_delete )
            edit_delete();
        else if(e.getSource()==edit_find  || e.getSource()==button_find )
            edit_find();
        else if(e.getSource()==edit_find_next  || e.getSource()==button_find_next )
            edit_find_next();
        else if(e.getSource()==edit_replace  || e.getSource()==button_replace )
            edit_replace();
        else if(e.getSource()==edit_selectall )
            edit_selectall();
        else if(e.getSource()==edit_timedate )
            edit_timedate();


        else if(e.getSource()==format_font  || e.getSource()==button_font )
            format_font();
        else if(e.getSource()==keyboard_mn  )
            mn_keyboard_toggle();
        else if(e.getSource()==keyborad_layout_mn  )
            mn_keyboard_layout();
        else if(e.getSource()==language_en  )
            language_select("EN");
        else if(e.getSource()==language_cn  )
            language_select("CN");
        else if(e.getSource()==language_ja  )
            language_select("JA");
        else if(e.getSource()==language_mn  )
            language_select("MN");
        else if(e.getSource()==str2uppr)
            str2uppr();
        else if(e.getSource()==str2lwr)
            str2lwr();
        else if(e.getSource()==format_wordwarp)
            format_wordwarp();


        else if(e.getSource()==help_about  || e.getSource()==button_about )
            help_about();
    }

    public void file_new(){
        if(editor.getText().equals("") || editor.getText().equals(content))
        {
            editor.setText("");
            content = "";
            path = "";
            setTitle("untitled - MongolPad");
        }
        else
        {
            int a = JOptionPane.showConfirmDialog(null, "The text has been changed\nDo you want to save the changes?");
            if(a==0)
                file_save();
            else if(a==1)
            {
                editor.setText("");
                path = "";
                setTitle("untitled - MongolPad");
            }
            else if(a==2)
                return;
        }
    }

    public void file_load(String filename){

        File myfile = new File(filename);
        if(myfile == null || myfile.getName().equals(""))
        {
            path = filename;
            setTitle(filename+" - MongolPad");
            return;
        }
        try
        {
        	InputStreamReader isr = new InputStreamReader(new FileInputStream(myfile), "UTF-8");
            BufferedReader input = new BufferedReader(isr);
            StringBuffer str = new StringBuffer();
            String line;
            while((line = input.readLine()) != null) // st is declared as string avobe
                str.append(line+"\n");
            editor.setText(str.toString());
            content = editor.getText();
            path = myfile.toString();
            setTitle(myfile.getName()+" - MongolPad");
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "File not found: "+e);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "IO ERROR: "+e);
        }
    }

    public void file_open(){

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int r=fc.showOpenDialog(this);
        if(r==fc.CANCEL_OPTION)
            return;

        File myfile = fc.getSelectedFile();
        if(myfile == null || myfile.getName().equals(""))
        {
            JOptionPane.showMessageDialog(this, "Select a file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try
        {
        	InputStreamReader isr = new InputStreamReader(new FileInputStream(myfile), "UTF-8");
            BufferedReader input = new BufferedReader(isr);
            StringBuffer str = new StringBuffer();
            String line;
            while((line = input.readLine()) != null) // st is declared as string avobe
                str.append(line+"\n");
            editor.setText(str.toString());
            content = editor.getText();
            editor.setCaretPosition(0);
            path = myfile.toString();
            setTitle(myfile.getName()+" - MongolPad");
    		Dimension newSize = getSize();
    		newSize.height++;
    		setSize( newSize);
            editor_resize() ;
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "File not found: "+e);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "IO ERROR: "+e);
        }
    }

    public void file_save(){
        if(path.equals(""))
        {
            file_save_as();
            return;
        }
        try
        {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            BufferedWriter output = new BufferedWriter(osw);
            StringBuffer str = new StringBuffer(editor.getText());

            int i = 0;
            while (i < str.length() ){
            	char c = str.charAt(i);
            	int ic = c;
            	if( c != '\n' )
            		output.append(c);
            	else
            		output.append("\r\n");
            	i++;
            }
            output.flush();
            content = editor.getText();
            osw.close();
        }
        catch(IOException i)
        {
            JOptionPane.showMessageDialog(this,"Failed to save the file","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void file_save_as(){

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int r = fc.showSaveDialog(this);
        if(r==fc.CANCEL_OPTION)
            return;
        File myfile = fc.getSelectedFile();
        if(myfile==null || myfile.getName().equals(""))
        {
            JOptionPane.showMessageDialog(this,"Please enter a file name!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(myfile.exists())
        {
            r = JOptionPane.showConfirmDialog(this, "A file with same name already exists!\nAre you sure want to overwrite?");
            if(r != 0)
                return;
        }
        try
        {

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(myfile), "UTF-8");
            BufferedWriter output = new BufferedWriter(osw);
            StringBuffer str = new StringBuffer(editor.getText());

            int i = 0;
            while (i < str.length() ){
            	char c = str.charAt(i);
            	int ic = c;
            	if( c != '\n' )
            		output.append(c);
            	else
            		output.append("\r\n");
            	i++;
            }
            output.flush();
            content = editor.getText();
            setTitle(myfile.getName()+" - MongolPad");
            path = myfile.toString();
            osw.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(this,"Failed to save the file","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void file_print() {

        MPrintIt printIt = new MPrintIt( path );
        printIt.print( editor );

    }

    public void file_close(){
        if(editor.getText().equals("") || editor.getText().equals(content))
        {
            editor.setText("");
            path = "";
            setTitle("untitled - MongolPad");
        }
        else
        {
            int a = JOptionPane.showConfirmDialog(null, "The text has been changed\nDo you want to save the changes?");
            if(a==0)
                file_save();
            else if(a==1)
            {
                editor.setText("");
                path = "";
                setTitle("untitled - MongolPad");
            }
            else if(a==2)
                return;
        }

    }

    public void file_exit(){

        if(editor.getText().equals("") || editor.getText().equals(content))
            System.exit(0);
    	else
    	{
            int b = JOptionPane.showConfirmDialog(null, "The text has been changed.\nDo you want to save the changes?");

            if(b==0)
                    file_save();
            else if(b==1)
                    System.exit(0);
            else if(b==2)
                    return;
    	}
    }

    public void edit_undo() {
        if( undo.canUndo())
        {
            try
            {
                undo.undo();
            }
            catch(CannotUndoException e)
            {
            }
        }
    }

    public void edit_redo(){
        if( undo.canRedo())
        {
            try
            {
                undo.redo();
            }
            catch(CannotRedoException e)
            {
            }
        }
    }

    public void edit_cut(){
        editor.cut();
    }

    public void edit_copy(){
        editor.copy();
    }

    public void edit_paste(){
        editor.paste();
    }

    public void edit_delete(){
        String temp = editor.getText();
        editor.setText(temp.substring(0, editor.getSelectionStart())+temp.substring(editor.getSelectionEnd()));
    }

    public void edit_find(){
        finder.setVisible(true);
    }

    public void edit_find_next(){
        finder.find_next();
    }

    public void edit_replace(){
        finder.setVisible(true);
    }

    public void edit_selectall(){
        editor.selectAll();
    }

    public void edit_timedate(){

        try
        {
        int start = editor.getSelectionStart();
        int end   = editor.getSelectionEnd();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String now = sdf.format(cal.getTime());
        String temp1 = editor.getText().substring(0,start);
        String temp2 = editor.getText().substring(end);
        editor.setText(temp1+" "+now+" "+temp2);
        editor.select(start+1, start+1+now.length());
        }
        catch(NullPointerException e){}
    }

    public void format_font(){
    	fontchooser.window.show();
        fontchooser.window.setVisible(true);
    }

    public void mn_keyboard_toggle(){

        if( mongolian_keyboard_enable ){
        	mongolian_keyboard_enable = false;
        }
        else {
        	mongolian_keyboard_enable = true;
        }
       	keyboard_mn.setSelected(mongolian_keyboard_enable);
    }

    public void mn_keyboard_layout(){
    	mnKeyboardLayout.window.setVisible(true);
    }


    public void language_select(String lang){

    	language_en.setState(false);
    	language_cn.setState(false);
    	language_mn.setState(false);
    	language_ja.setState(false);

		locale = new Locale(lang);

    	if( "CN".equalsIgnoreCase(locale.getLanguage()))
        	language_cn.setState(true);
    	if( "EN".equalsIgnoreCase(locale.getLanguage()))
        	language_en.setState(true);
    	if( "MN".equalsIgnoreCase(locale.getLanguage()))
        	language_mn.setState(true);
    	if( "JA".equalsIgnoreCase(locale.getLanguage()))
        	language_ja.setState(true);

		resources = getResouceBundle( locale );

		exchangeMenubarText();

		exchangeToolbartext();

    	if( "MN".equalsIgnoreCase(locale.getLanguage()))
    		mnglpadUI.setMongolianUIFont( 6 );
    	else
    		mnglpadUI.setDefaultUIFont();

    	SwingUtilities.updateComponentTreeUI(menubar);
    	SwingUtilities.updateComponentTreeUI(toolbar);

    }

    public void str2uppr(){
        try
        {
        int start = editor.getSelectionStart();
        int end   = editor.getSelectionEnd();
        String temp1 = editor.getText().substring(0,start);
        String temp2 = editor.getText().substring(end);
        String conv  = editor.getSelectedText().toUpperCase();
        editor.setText(temp1+conv+temp2);
        editor.select(start, end);
        }
        catch(NullPointerException e){}
    }

    public void str2lwr(){
        try
        {
        int start = editor.getSelectionStart();
        int end   = editor.getSelectionEnd();
        String temp1 = editor.getText().substring(0,start);
        String temp2 = editor.getText().substring(end);
        String conv  = editor.getSelectedText().toLowerCase();
        editor.setText(temp1+conv+temp2);
        editor.select(start, end);
        }
        catch(NullPointerException e){}
    }

    public void format_wordwarp(){
        if(editor.getLineWrap()==false){
            editor.setLineWrap(true);
        }
        else {
            editor.setLineWrap(false);
        }
        editor_resize();
    }

    public void help_about(){
        about.window.setVisible(true);
    }


	/**
	 * Find the hosting frame, for the file-chooser dialog.
	 */
	protected Frame getFrame() {
		for (Container p = getParent(); p != null; p = p.getParent()) {
			if (p instanceof Frame) {
				return (Frame) p;
			}
		}
		return null;
	}

	/**
	 * Fetch the editor contained in this panel
	 */
	protected JTextComponent getEditor() {
		return editor;
	}

	/**
	 * FIXME - I'm not very useful yet
	 */
	class StatusBar extends JComponent {

		public StatusBar() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		}

		public void paint(Graphics g) {
			super.paint(g);
		}

	}

	  public int print(Graphics g, PageFormat pf, int pageIndex) {
		    if (pageIndex != 0)
		      return NO_SUCH_PAGE;
		    Graphics2D g2 = (Graphics2D) g;
		    g2.setFont(new Font("Serif", Font.PLAIN, 36));
		    g2.setPaint(Color.black);

		    g2.translate(pf.getImageableX(), pf.getImageableY());
		    editor.paint(g2);

		    return PAGE_EXISTS;
		  }

}
