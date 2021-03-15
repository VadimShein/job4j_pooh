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
            ConcurrentLinkedQueue<String> qu = new ConcurrentLinkedQueue<>(topicMap.get(req.key()));
            qu.add(req.message());
            topicMap.put(req.key(), qu);
            text = "POST: " + req.message();
            status = 200;
        }
        if (req.method().equals("GET")) {
            ConcurrentLinkedQueue<String> qu = topicMap.getOrDefault(req.key(), new ConcurrentLinkedQueue<>());
            userMap.putIfAbsent(req.id(), new ConcurrentLinkedQueue<>());
            userMap.put(req.id(), qu);
            ConcurrentLinkedQueue<String> quId = userMap.getOrDefault(req.id(), new ConcurrentLinkedQueue<>());
            text = "GET: " + quId.poll();
            if (quId.size() > 0) {
                userMap.put(req.id(), quId);
            } else {
                userMap.remove(req.id());
            }
            status = 200;
        }
        return new Resp(text, status);
    }
}