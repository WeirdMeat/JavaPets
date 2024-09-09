import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    static class ClientRunnable implements Runnable  {
        private final Socket client;
        private String name;
        ClientRunnable (Socket client) {
            this.client = client;
        }
        @Override
        public void run() {
            try {
                System.out.println("A client connected. It's port is " + client.getPort());
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                writer.println("This is a message sent to " + reader.readLine());
                new Thread(() -> {
                    while (true) {
                        String message = null;
                        try {
                            message = reader.readLine();
                            writer.println(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
            catch (IOException e) {
                System.err.println("You got IOException: Wrong Input:  " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws RuntimeException {
        try {
            ServerSocket server = new ServerSocket(80);
            try {
                System.out.println("Server has started on 127.0.0.1:80.\r\nWaiting for a connection…");
                while (true) {
                    Socket client = server.accept();
                    Thread clientThread = new Thread(new ClientRunnable(client));
                    clientThread.start();
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}