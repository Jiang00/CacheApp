/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    private static final int k = 13;
    private static final int[] prime1 = {11, 17, 23, 31, 41, 47, 59, 67, 73, 83, 97, 103, 109};
    private static final int[] prime2 = {13, 19, 29, 37, 43, 53, 61, 71, 79, 89, 101, 107, 113};
    
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
                
                ArrayList toGet = new ArrayList();
                ArrayList toSend = new ArrayList();
                
                if (pl.value == 1) {
                    System.out.println("Recieved Bloom Filter");
                    ArrayList<String> stringsToSend = Initialize.getStrings(pl.filter);
                    
                    try {
                        for (int i = 0; i < clientCount; i++) {
                            clients[i].send(load);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if (pl.value == 2) {
                    System.out.println("Recieved HashTables");
                    /////////////////////////////////////////
                    /* Need to compare the hashtables here */
                    /////////////////////////////////////////
                    ArrayList itemsToGet = new ArrayList();
                    for (ArrayList l : SetInterface.hashTables) {
                        itemsToGet.add(Comparator.compareHashTables(SetInterface.setKeyList, l));
                    }
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

