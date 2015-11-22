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


public class ChatClient implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
   // private DataInputStream console = null;
   // private DataOutputStream streamOut = null;
    OutputStream os = null;
    ObjectOutputStream oos= null;
    private Payload pay=null;
    private ChatClientThread client = null;
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

    public void send(){
            try {
//                streamOut.writeUTF(g.getTextSend());
//                streamOut.flush();
                pay=new Payload(1, "Test");
                    oos.writeObject(pay);
                    oos.flush();
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

    public void handle(String msg) {
        if (msg.equals(".bye")) {
            System.out.println("Good bye. Press RETURN to exit ...");

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage("Good bye. Press RETURN to exit ...");
                }
            });
            stop();
        } else {
            System.out.println(msg);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    g.setTextMessage(msg);
                }
            });
        }
    }

    public void start() throws IOException {
       // console = new DataInputStream(System.in);
       // streamOut = new DataOutputStream(socket.getOutputStream());
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
        }
        try {
//            if (console != null) {
//                console.close();
//            }
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
        client.close();

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
