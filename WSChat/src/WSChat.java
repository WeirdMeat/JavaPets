import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class WSChat {

    static class ClientRunnable implements Runnable  {
        private final String name;
        ClientRunnable (String name) {
            this.name = name;
        }
        @Override
        public void run() {
            try {
                Socket client = new Socket("localhost", 80);
                System.out.println(String.format("%s connected", name));
                client.close();
            }
            catch (IOException e) {
                System.err.println("You got IOException: Wrong Input:  " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    static class ServerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                ServerSocket server = new ServerSocket(80);
                try {
                    System.out.println("Server has started on 127.0.0.1:80.\r\nWaiting for a connection…");
                    server.accept();
                    System.out.println("A client connected.");
                    server.accept();
                    System.out.println("Another client connected.");
                }
                finally {
                    server.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Thread serverThread = new Thread(new ServerRunnable());
        Thread clientThread = new Thread(new ClientRunnable("Josh"));
        Thread clientSecondThread = new Thread(new ClientRunnable("Mark"));
        serverThread.start();
        clientThread.start();
        clientSecondThread.start();
    }
}