package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topicMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("Bad Request", 400);
        if (req.getMethod().equals("POST")) {
            topicMap.putIfAbsent(req.getTopic(), new ConcurrentLinkedQueue<>());
            topicMap.get(req.getTopic()).add(req.getMessage());
            result = new Resp("Created", 201);

        }
        if (req.getMethod().equals("GET")) {
            userMap.putIfAbsent(req.getTopic(), new ConcurrentLinkedQueue<>());
            userMap.put(req.getTopic(), topicMap.getOrDefault(req.getTopic(), new ConcurrentLinkedQueue<>()));
            String text = userMap.getOrDefault(req.getTopic(), new ConcurrentLinkedQueue<>()).poll();
            if (text != null) {
                result = new Resp(text, 200);
            } else {
                result = new Resp("Value not found", 404);
            }
        }
        return result;
    }
}