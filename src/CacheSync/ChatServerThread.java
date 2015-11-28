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

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private InputStream is=null;
    private ObjectInputStream ois=null;
//    private DataInputStream streamIn = null;
//    private DataOutputStream streamOut = null;
    private GUI g;
    

    public ChatServerThread(ChatServer _server, Socket _socket, GUI gui) throws IOException, ClassNotFoundException {
        
      
        super();
          this.g=gui;
        server = _server;
        socket = _socket;
        ID = socket.getPort();

    }

//    public void send(String msg) throws IOException {
//        try {
//            streamOut.writeUTF(msg);
//            streamOut.flush();
//        } catch (IOException ioe) {
//            SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                g.setTextMessage(ID + " ERROR sending: " + ioe.getMessage());
//            }
//        });
//            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
//            server.remove(ID);
//            stop();
//        }
//    }

    public int getID() {
        return ID;
    }

    public void run() {
        System.out.println("Server Thread " + ID + " running.");
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Server Thread " + ID + " running.");
            }
        });
        
        
        
        while (true) {
            try {
                server.handle(ID, (Payload)ois.readObject());
            } catch (IOException ioe) {
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                
                SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage(ID + " ERROR reading: " + ioe.getMessage());
            }
        });
                try {
                    server.remove(ID);
                } catch (IOException ex) {
                    Logger.getLogger(ChatServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                stop();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChatServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void open() throws IOException {
         is = socket.getInputStream();
         ois = new ObjectInputStream(is);
//        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
         if (is != null) {
            is.close();
        }
        if (ois != null) {
            ois.close();
        }
//        if (streamIn != null) {
//            streamIn.close();
//        }
//        if (streamOut != null) {
//            streamOut.close();
//        }
    }
}
