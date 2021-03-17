package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topicMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = null;
        int status = 404;

        if (req.method().equals("POST")) {
            topicMap.putIfAbsent(req.key(), new ConcurrentLinkedQueue<>());
            topicMap.get(req.key()).add(req.message());
            text = req.message();
            status = 200;
        }
        if (req.method().equals("GET")) {
            userMap.putIfAbsent(req.id(), new ConcurrentLinkedQueue<>());
            userMap.put(req.id(), topicMap.getOrDefault(req.key(), new ConcurrentLinkedQueue<>()));
            text = userMap.getOrDefault(req.id(), new ConcurrentLinkedQueue<>()).poll();
            status = 200;
        }
        return new Resp(text, status);
    }
}