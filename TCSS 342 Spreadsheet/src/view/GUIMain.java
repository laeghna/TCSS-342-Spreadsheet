/*
 * Lisa Taylor
 * Jonah Howard
 * Henry Lai
 * John Bui
 * 
 * TCSS 342 - Spring 2016
 * Assignment 6B
 */

package view;

import java.awt.EventQueue;

/**
 * Runs Spreadsheet app by instantiating and starting the SpreadsheetGUI.
 * 
 * @author Lisa Taylor
 * @version 4 March 2016
 */
public final class GUIMain {
    
    /**
     * Prevents instantiation of this class.
     */
    private GUIMain() {
        throw new IllegalStateException();
    }

    /**
     * Invokes the SpreadsheetGUI. Command line arguments
     * are ignored.
     * 
     * @param theArgs Command line arguments.
     */
    public static void main(final String[] theArgs) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().run();
            }
        });
    }

}
