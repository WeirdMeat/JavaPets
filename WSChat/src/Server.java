import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    static final String ERASE_LINE = "\u001B[2K";
    static final String CURSOR_UP_ONE = "\u001B[1A";
    static int colorNumber = 0;
    static final String ANSI_RESET = "\u001B[0m";
    static ArrayList<String> msgs = new ArrayList<>();
    static int endPointer = 0;
    static final ReentrantLock lock = new ReentrantLock();

    static String getColor() {
        colorNumber++;
        return String.format("\u001B[3%dm", colorNumber);
    }

    static class ClientRunnable implements Runnable  {
        private final Socket client;
        private final String color;
        private int pointer;
        private String name;
        ClientRunnable (Socket client) {
            this.client = client;
            this.pointer = 0;
            this.color = getColor();
        }
        @Override
        public void run() {
            try {
                System.out.println("A client connected. It's port is " + client.getPort());
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                name = reader.readLine();
                writer.println("This is a message sent to " + name);
                new Thread(() -> {
                    while (true) {
                        String message = null;
                        try {
                            message = reader.readLine();
                            lock.lock();
                            try {
                                endPointer += 1;
                                this.pointer += 1;
                                msgs.add(color + "[" + name + "]: " + message + ANSI_RESET);
                                System.out.println(color + "[" + name + "]: " + message + ANSI_RESET);
                                writer.println(CURSOR_UP_ONE + ERASE_LINE + color + "[ME]: " + message + ANSI_RESET);
                            } finally {
                                lock.unlock();
                            }


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
                while (true) {
                    if (!lock.isLocked()) {
                        if (this.pointer < endPointer) {
                            writer.println(msgs.get(this.pointer));
                            this.pointer += 1;
                        }
                    }
                }
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