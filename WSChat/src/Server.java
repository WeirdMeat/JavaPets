import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws RuntimeException {
        try {
            ServerSocket server = new ServerSocket(80);
            try {
                System.out.println("Server has started on 127.0.0.1:80.\r\nWaiting for a connection…");
                while (true) {
                    Socket client = server.accept();
                    System.out.println("A client connected. It's port is " + client.getPort());
                    OutputStream output = client.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println("This is a message sent to Mark");
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}