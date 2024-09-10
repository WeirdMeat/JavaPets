import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    static ArrayList<String> msgs = new ArrayList<>();
    static int endPointer = 0;
    static final ReentrantLock lock = new ReentrantLock();

    static class ClientRunnable implements Runnable  {
        private final Socket client;
        private int pointer;
        private String name;
        ClientRunnable (Socket client) {
            this.client = client;
            this.pointer = 0;
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
                                msgs.add("[" + name + "]: " + message);
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