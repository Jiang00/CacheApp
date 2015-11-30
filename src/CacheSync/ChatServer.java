/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author shanliang
 */
public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    public Payload load=null;
    GUI g;

    public ChatServer(int port, GUI gui) {
        try {
            this.g=gui;
             SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Binding to port " + port + ", please wait  ...");
            }
        });
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
             SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Server started: " + server);
            }
        });
            System.out.println("Server started: " + server);
            start();
        } catch (IOException ioe) {
             SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Can not bind to port " + port + ": " + ioe.getMessage());
            }
        });
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (thread != null) {
            try {
                SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Waiting for a client ...");
            }
        });
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                
                SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Server accept error: " + ioe);
            }
        });
                System.out.println("Server accept error: " + ioe);
                try {
                    stop();
                } catch (IOException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() throws IOException {
        if (thread != null) {
            server.close();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(int ID, Payload pl) throws IOException {
        this.load =pl;
        
           SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               
                g.setTextMessage("Payload ID: "+(load.id));
                g.setTextMessage("Payload Value: "+(load.value));
                
                for (Integer i : pl.keyList) {
                    System.out.println(i);
                }
                
            }
        });

    }

    public synchronized void remove(int ID) throws IOException {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
             SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Removing client thread " + ID + " at " + pos);
            }
        });
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            toTerminate.close();
            toTerminate.stop();
            
        }
    }

    private void addThread(Socket socket) throws IOException, ClassNotFoundException {
        if (clientCount < clients.length) {
            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Client accepted: " + socket);
                
            }
        });
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ChatServerThread(this, socket, this.g);
            clients[clientCount].open();
            clients[clientCount].start();
            clientCount++;
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
                 SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Client refused: maximum " + clients.length + " reached.");
            }
        });
        
        }
    }

}

