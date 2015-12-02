/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

import javax.swing.SwingUtilities;

/**
 *
 * @author shanliang
 */
public class SwingWorker {
    
    private static GUI g;
    
    public static void getGUI (GUI gui) {
        g = gui;
    }
    
    public static void setGUIText(String s){
    SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage(s);
                }
            });
    }
    }
