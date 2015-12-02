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
import java.util.ArrayList;
import javax.swing.SwingUtilities;


public class ChatClient implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    OutputStream os = null;
    ObjectOutputStream oos = null;
    private Payload pay = null;
    private ChatClientThread client = null;
    private Payload payReturn = null;
    // Flag to determine whether this application is connected to someone
    private boolean isConnected = false;
    
    GUI g;

    public ChatClient(){};
    public ChatClient(String serverName, int serverPort, GUI gui) {
        this.g = gui;
        System.out.println("Establishing connection. Please wait ...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.setTextMessage("Establishing connection. Please wait ...");
            }
        });
 
       try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            isConnected = true;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Connected: " + socket);
                }
            });
            start();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
            
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Host unknown: " + uhe.getMessage());
                }
            });

        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Unexpected exception: " + ioe.getMessage());
                }
            });

        }
    }

    public void send(Payload pay) {
        try {
            oos.writeObject(pay);
            oos.flush();
            System.out.println("Sent Bloom Filter Successfully.");
            if (pay.filter != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        g.setTextMessage("Sent " + (pay.filter.size() / 8000) + " kilo bytes of data.");
                    }
                });
            }
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Establishing connection. Please wait ...");
                }
            });
        }
    }
    
    @Override
    public void run() {
        
    }

    public void handle(Payload msg) {
        
        System.out.println("Received payload with\n" + 
                           "\t ID: " + msg.id);
        
        if (msg.id == 2) {
            System.out.println("Recieved ArrayList of Strings and Bloom Filter");
            System.out.println("Number of strings recieved: " + msg.strings.size());
            // Determine which strings the client needs
            ArrayList<String> stringsToSend = Initialize.getStrings(msg.filter, msg.keySize, msg.numberOfElements);
            pay = new Payload(3, null, stringsToSend);
            send(pay);
            Initialize.addStrings(msg.strings);
        }
    }

    public void start() throws IOException {
        os=socket.getOutputStream();
        oos = new ObjectOutputStream(os);
        if (thread == null) {
            client = new ChatClientThread(this, socket, this.g);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            client.close();
            thread = null;
            isConnected = false;
        }
        try {
            if (os != null) {
                os.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Error closing ...");
                }
            });
        }
        if(client!=null){
        client.close();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
    
    public static void main(String args[]) {
        GUI gui = new GUI();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);

            }
        });

    }
}
