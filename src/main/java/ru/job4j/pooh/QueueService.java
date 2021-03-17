package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queueMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = null;
        int status = 404;

        if (req.method().equals("POST")) {
            queueMap.putIfAbsent(req.key(), new ConcurrentLinkedQueue<>());
            queueMap.get(req.key()).add(req.message());
            text = req.message();
            status = 200;
        }
        if (req.method().equals("GET")) {
            text = queueMap.getOrDefault(req.key(), new ConcurrentLinkedQueue<>()).poll();
            status = 200;
        }
        return new Resp(text, status);
    }
}