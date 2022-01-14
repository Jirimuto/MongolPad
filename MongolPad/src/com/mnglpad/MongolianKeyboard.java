package com.mnglpad;

import java.awt.event.KeyEvent;

public class MongolianKeyboard {

	public static KeyEvent convert ( KeyEvent keyEvent ) {
		
        char keyChar = keyEvent.getKeyChar();
        char wchm = 0;
        
        switch ( keyChar ) {
			
			// B
			case 'z' : wchm = 0x183D; break; // 01
			case 'Z' : wchm = 0x1841; break; // 01
			case 'x' : wchm = 0x1831; break; // 02
			case 'c' : wchm = 0x1834; break; // 03
			case 'C' : wchm = 0x183C; break; // 03
			case 'v' : wchm = 0x1824; break; // 04
			case 'b' : wchm = 0x182A; break; // 05
			case 'n' : wchm = 0x1828; break; // 06
			case 'N' : wchm = 0x1829; break; // 06
			case 'm' : wchm = 0x182E; break; // 07
			case ',' : wchm = 0x1802; break; // 08
			case '<' : wchm = 0xFE3D; break; // 08
			case '.' : wchm = 0x1803; break; // 09
			case '>' : wchm = 0xFE3E; break; // 09
			case '/' : wchm = 0x00B7; break; // 10
			case '?' : wchm = 0xFE16; break; // 10
			// C
			case 'a' : wchm = 0x1820; break; // 01
			case 'A' : wchm = 0x180E; break; // 01
			case 's' : wchm = 0x1830; break; // 02
			case 'S' : wchm = 0x202F; break; // 02
			case 'd' : wchm = 0x1833; break; // 03
			case 'D' : wchm = 0x180B; break; // 03
			case 'f' : wchm = 0x1839; break; // 04
			case 'F' : wchm = 0x180C; break; // 04
			case 'g' : wchm = 0x182D; break; // 05
			case 'G' : wchm = 0x180D; break; // 05
			case 'h' : wchm = 0x182C; break; // 06
			case 'H' : wchm = 0x183E; break; // 06
			case 'j' : wchm = 0x1835; break; // 07
			case 'k' : wchm = 0x183A; break; // 08
			case 'K' : wchm = 0x183B; break; // 08
			case 'l' : wchm = 0x182F; break; // 09
			case 'L' : wchm = 0x1840; break; // 09
			case ';' : wchm = 0xFE14; break; // 10
			case ':' : wchm = 0x1804; break; // 10
			case '\'': wchm = 0x180B; break; // 11
			case '"' : wchm = 0x180C; break; // 11
			// D
			case 'q' : wchm = 0x1823; break; // 01
			case 'Q' : wchm = 0x1842; break; // 01
			case 'w' : wchm = 0x1838; break; // 02
			case 'e' : wchm = 0x1821; break; // 03
			case 'E' : wchm = 0x1827; break; // 03
			case 'r' : wchm = 0x1837; break; // 04
			case 'R' : wchm = 0x183F; break; // 04
			case 't' : wchm = 0x1832; break; // 05
			case 'y' : wchm = 0x1836; break; // 06
			case 'u' : wchm = 0x1826; break; // 07
			case 'i' : wchm = 0x1822; break; // 08
			case 'I' : wchm = 0x180A; break; // 08
			case 'o' : wchm = 0x1825; break; // 09
			case 'p' : wchm = 0x182B; break; // 10
			case '[' : wchm = 0xFE47; break; // 11
			case '{' : wchm = 0xFE3F; break; // 11
			case ']' : wchm = 0xFE48; break; // 12
			case '}' : wchm = 0xFE40; break; // 12
			case '\\': wchm = 0x1801; break; // 13
			case '|' : wchm = 0xFE31; break; // 13
			// E
			case '`' : wchm = 0x180D; break; // 00
			case '!' : wchm = 0xFE15; break; // 01
			case '@' : wchm = 0x2048; break; // 02
			case '#' : wchm = 0x2049; break; // 03
			case '^' : wchm = 0x200C; break; // 06
			case '&' : wchm = 0x180A; break; // 07
			case '*' : wchm = 0x200D; break; // 08
			case 'P' : wchm = 0x200D; break; // 08
			case '(' : wchm = 0xFE35; break; // 09
			case ')' : wchm = 0xFE36; break; // 10
			case '-' : wchm = 0x202F; break; // 11
			case '_' : wchm = 0x180E; break; // 11
			
			// case '1' : wchm = 0x1811; break; // 01
			// case '2' : wchm = 0x1812; break; // 02
			// case '3' : wchm = 0x1813; break; // 03
			// case '4' : wchm = 0x1814; break; // 04
			// case '5' : wchm = 0x1815; break; // 05
			// case '6' : wchm = 0x1816; break; // 06
			// case '7' : wchm = 0x1817; break; // 07
			// case '8' : wchm = 0x1818; break; // 08
			// case '9' : wchm = 0x1819; break; // 09
			// case '0' : wchm = 0x1810; break; // 10
			default :	break;
			
        }
        
        if( wchm != 0 ){
        	keyEvent.setKeyChar(wchm);
        }        
		return keyEvent;
	}

    public static void printIt(String title, KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);
        char keyChar = keyEvent.getKeyChar();
        System.out.println(title + " : " + keyText + " / " + keyChar );
      }

}
