package ru.job4j.pooh;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoohServer {
    private final HashMap<String, Service> modes = new HashMap<>();

    public void start() {
        modes.put("queue", new QueueService());
        modes.put("topic", new TopicService());
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(() -> {
                    try (OutputStream out = socket.getOutputStream();
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream())) {
                        byte[] buff = new byte[1_000_000];
                        int total = input.read(buff);
                        String text = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                        Req req = new Req(text);
                        Resp resp = modes.get(req.getMode()).process(req);
                        System.out.println(resp.getText());
                        out.write(("HTTP/1.1 " + resp.getStatus() + System.lineSeparator()
                                + System.lineSeparator() + System.lineSeparator()).getBytes());
                        out.write((resp.getText() + System.lineSeparator()).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PoohServer().start();
    }
}
