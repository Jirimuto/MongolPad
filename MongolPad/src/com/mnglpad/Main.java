package com.mnglpad;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Burhan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JFrame;

public class Main {
    public static void main(String args[]){

    MongolPad mynote;
    String filename = null;
    for(int i=0; i<args.length; i++) {
    		if( !args[i].startsWith("-") )
    			filename = args[i];
    }
    	if( filename != null )
    		mynote = new MongolPad( filename );
    	else
    		mynote = new MongolPad(null);

    mynote.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
