import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static class ClientRunnable implements Runnable  {
        private final Socket client;
        ClientRunnable (Socket client) {
            this.client = client;
        }
        @Override
        public void run() {
            try {
                System.out.println("A client connected. It's port is " + client.getPort());
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                writer.println("This is a message sent to Mark");
                String message = reader.readLine();
                writer.println("You wrote " + message + ". What are you doing with your life. ");
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