
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws RuntimeException {
        try {
            Socket client = new Socket("localhost", 80);
            Scanner reader = new Scanner(System.in);
            String name = reader.nextLine();
            System.out.println(String.format("%s connected", name));
            client.close();
        }
        catch (IOException e) {
            System.err.println("You got IOException: Wrong Input:  " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}