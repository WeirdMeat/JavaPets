
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws RuntimeException {
        try {
            Socket client = new Socket("localhost", 80);
            Scanner reader = new Scanner(System.in);
            String name = reader.nextLine();
            System.out.println(String.format("%s connected", name));
            Scanner readerFromServer = new Scanner(client.getInputStream());
            System.out.println("The answer from server is: " + readerFromServer.nextLine());
            PrintWriter writerToServer = new PrintWriter(client.getOutputStream(), true);
            String message = reader.nextLine();
            writerToServer.println(message);
            System.out.println(readerFromServer.nextLine());
            client.close();
        }
        catch (IOException e) {
            System.err.println("You got IOException: Wrong Input:  " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}