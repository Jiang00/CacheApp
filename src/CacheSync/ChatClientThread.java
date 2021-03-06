/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

/**
 *
 * @author shanliang
 */
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class ChatClientThread extends Thread {

    private Socket socket = null;
    private ChatClient client = null;
    //private DataInputStream streamIn = null;
    private InputStream is=null;
    private ObjectInputStream ois=null;
    
    
    boolean listen = true;
    GUI g;

    public ChatClientThread(ChatClient _client, Socket _socket, GUI g) {

        client = _client;
        socket = _socket;
        this.g = g;
        open();
        start();

    }

    public void open() {
        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            //streamIn = new DataInputStream(socket.getInputStream());
            listen = true;
        } catch (IOException ioe) {

            System.out.println("Error getting input stream: " + ioe);
            SwingWorker.setGUIText("Error getting input stream: " + ioe);

            client.stop();
        }
    }

    public void close() {
        try {
//            if (streamIn != null) {
//                streamIn.close();
//            }
        if (is != null) {
            is.close();
        }
        if (ois != null) {
            ois.close();
        }
        } catch (IOException ioe) {
            System.out.println("Error closing input stream: " + ioe);

                    SwingWorker.setGUIText("Error closing input stream: " + ioe);

        }
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listen() throws ClassNotFoundException {
        while (true) {
            while (listen) {
                try {
                    client.handle((Payload)ois.readObject());
                } catch (IOException ioe) {
                    System.out.println("Listening error: " + ioe.getMessage());

                    SwingWorker.setGUIText("Listening error: " + ioe.getMessage());

                    client.stop();
                    close();
                    listen = false;
                }
            }
        }
    }

}
