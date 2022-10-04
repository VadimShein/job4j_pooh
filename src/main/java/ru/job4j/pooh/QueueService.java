package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queueMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("Bad Request", 400);
        if (req.getMethod().equals("POST")) {
            queueMap.putIfAbsent(req.getTopic(), new ConcurrentLinkedQueue<>());
            queueMap.get(req.getTopic()).add(req.getMessage());
            result = new Resp("Created", 201);

        }
        if (req.getMethod().equals("GET")) {
            String text = queueMap.getOrDefault(req.getTopic(), new ConcurrentLinkedQueue<>()).poll();
            if (text != null) {
                result = new Resp(text, 200);
            } else {
                result = new Resp("Value not found", 404);
            }
        }
        return result;
    }
}