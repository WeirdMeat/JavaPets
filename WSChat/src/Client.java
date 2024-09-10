
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws RuntimeException {

        try {
            Scanner reader = new Scanner(System.in);
            System.out.println("Who");
            String name = reader.nextLine();
            Socket client = new Socket("localhost", 80);
            BufferedReader readerFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writerToServer = new PrintWriter(client.getOutputStream(), true);
            writerToServer.println(name);
            System.out.println("You are in");
            Runnable readMsg = () -> {
                while (true) {
                    try {
                        System.out.println(readerFromServer.readLine());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            new Thread(readMsg).start();

            while (reader.hasNextLine()) {
                String message = reader.nextLine();
                writerToServer.println(message);
            }
        }
        catch (IOException e) {
            System.err.println("You got IOException: Wrong Input:  " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}