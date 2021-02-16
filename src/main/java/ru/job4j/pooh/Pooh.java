package ru.job4j.pooh;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class Pooh {
    private static final ConcurrentHashMap<String, BlockingQueue<String>> map = new ConcurrentHashMap<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    private static String postQueue(String queue, String text) {
        if (!map.containsKey(queue)) {
            BlockingQueue<String> qu = new LinkedBlockingQueue<>();
            qu.add(text);
            map.put(queue, qu);
        } else {
            BlockingQueue<String> qu = new LinkedBlockingQueue<>(map.get(queue));
            qu.add(text);
            map.put(queue, qu);
        }
        return text;
    }

    private static String getQueue(String queue) {
        AtomicReference<String> rsl = null;
        BlockingQueue<String> qu = map.get(queue);
        pool.submit(() -> {
            if (qu.size() > 0) {
                rsl.set(qu.poll());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        if (qu.size() != 0) {
            map.put(queue, qu);
        } else {
            map.remove(queue);
        }
        return rsl.get();
    }

    private static String postTopic(String topic, String text) {
        if (!map.containsKey(topic)) {
            BlockingQueue<String> qu = new LinkedBlockingQueue<>();
            qu.add(text);
            map.put(topic, qu);
        } else {
            BlockingQueue<String> qu = new LinkedBlockingQueue<>(map.get(topic));
            qu.add(text);
            map.put(topic, qu);
        }
        return text;
    }

    private static String getTopic(String topic) {
        AtomicReference<String> rsl = null;
        BlockingQueue<String> qu = map.get(topic);
        pool.submit(() -> {
            if (qu.size() > 0) {
                rsl.set(qu.poll());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return rsl.get();
    }

//http://localhost:9000/queue?post={"queue":"weather","text":"temperature +18 C"}
//http://localhost:9000/queue?post={%22queue%22:%22weather%22,%22text%22:%22temperature%20+18%20C%22}

    public static void main(String[] args) {
        final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(socket.getInputStream()))) {
                    String str;
                    String answer = "null";
                    String[] strSplit;
                    String[] paramSplit;

                    while (!(str = in.readLine()).isEmpty()) {
                        if (str.contains("queue?")) {
                            strSplit = str.split(" ");
                            for (String temp : strSplit) {
                                if (temp.contains("post=")) {
                                    paramSplit = temp.split("post=", 2);
                                    Task task = GSON.fromJson(paramSplit[1], Task.class);
                                    answer = postQueue(task.getQueue(), task.getText());
                                } else if (temp.contains("get")) {
                                    paramSplit = temp.split("get=", 2);
                                    Task task = GSON.fromJson(paramSplit[1], Task.class);
                                    answer = getQueue(task.getQueue());
                                }
                            }
                        }
                        if (str.contains("topic?")) {
                            strSplit = str.split(" ");
                            for (String temp : strSplit) {
                                if (temp.contains("topic=")) {
                                    paramSplit = temp.split("topic=", 2);
                                    Task task = GSON.fromJson(paramSplit[1], Task.class);
                                    answer = postTopic(task.getQueue(), task.getText());
                                } else if (temp.contains("get=")) {
                                    paramSplit = temp.split("get=", 2);
                                    Task task = GSON.fromJson(paramSplit[1], Task.class);
                                    answer = getTopic(task.getQueue());
                                }
                            }
                        }
                    }
                    if (!server.isClosed()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        out.write((answer).getBytes());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

























