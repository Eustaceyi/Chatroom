import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final ConcurrentHashMap<String, PrintWriter> writers = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("The server is running...");
        try (ServerSocket listener = new ServerSocket(12000)) {
            ExecutorService pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    private static class Handler implements Runnable {
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private String name;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                
                name = null;
                while (name == null) {
                    out.println("Please input your name.");
                    if (in.hasNextLine()) {
                        name = in.nextLine();
                        if (writers.containsKey(name)) {
                            out.println("This name is taken, please try another!");
                            name = null;
                        } else {
                            for (PrintWriter p : writers.values()) {
                                p.println(name + " has joined the chatroom.");
                            }
                            writers.put(name, out);
                            out.println("You have joined the chatroom.");
                        }
                    }
                }

                while (true) {
                    if (in.hasNextLine()) {
                        String input = in.nextLine();
                        System.out.println(input);
                        if (input.toLowerCase().startsWith("/quit")) {
                            out.println("/quit");
                            break;
                        }
                        for (String n : writers.keySet()) {
                            PrintWriter p = writers.get(n);
                            p.println(name + ": " + input);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    writers.remove(name);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
