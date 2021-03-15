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
            ConcurrentLinkedQueue<String> qu = new ConcurrentLinkedQueue<>(queueMap.get(req.key()));
            qu.add(req.message());
            queueMap.put(req.key(), qu);
            text = "POST: " + req.message();
            status = 200;
        }
        if (req.method().equals("GET")) {
            ConcurrentLinkedQueue<String> qu = queueMap.getOrDefault(req.key(), new ConcurrentLinkedQueue<>());
            text = "GET: " + qu.poll();
            if (qu.size() > 0) {
                queueMap.put(req.key(), qu);
            } else {
                queueMap.remove(req.key());
            }
            status = 200;
        }
        return new Resp(text, status);
    }
}