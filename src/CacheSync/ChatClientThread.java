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
import javax.swing.SwingUtilities;

public class ChatClientThread extends Thread {

    private Socket socket = null;
    private ChatClient client = null;
    private DataInputStream streamIn = null;
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
            streamIn = new DataInputStream(socket.getInputStream());
            listen = true;
        } catch (IOException ioe) {

            System.out.println("Error getting input stream: " + ioe);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Error getting input stream: " + ioe);
                }
            });
            client.stop();
        }
    }

    public void close() {
        try {
            if (streamIn != null) {
                streamIn.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing input stream: " + ioe);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Error closing input stream: " + ioe);
                }
            });
        }
    }

    @Override
    public void run() {
        listen();
    }

    public void listen() {
        while (true) {
            while (listen) {
                try {
                    client.handle(streamIn.readUTF());
                } catch (IOException ioe) {
                    System.out.println("Listening error: " + ioe.getMessage());

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            g.setTextMessage("Listening error: " + ioe.getMessage());
                        }
                    });
                    client.stop();
                    close();
                    listen = false;
                }
            }
        }
    }

}
