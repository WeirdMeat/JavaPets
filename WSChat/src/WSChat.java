import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class WSChat {

    static class ClientRunnable implements Runnable  {
        private final String name;
        ClientRunnable (String name) {
            this.name = name;
        }
        @Override
        public void run() {
            try (Socket client = new Socket("localhost", 80)) {
                    System.out.println(String.format("%s connected", name));
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
                try (ServerSocket server = new ServerSocket(80)) {
                    System.out.println("Server has started on 127.0.0.1:80.\r\nWaiting for a connection…");
                    while (true) {
                        Socket client = server.accept();
                        System.out.println("A client connected. It's port is " + client.getPort());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws RuntimeException {

        Thread serverThread = new Thread(new ServerRunnable());
        serverThread.start();
        try (Scanner reader = new Scanner(System.in)) {
            while (true) {
                String name = reader.nextLine();
                Thread clientThread = new Thread(new ClientRunnable(name));
                clientThread.start();
            }
        }
    }
}