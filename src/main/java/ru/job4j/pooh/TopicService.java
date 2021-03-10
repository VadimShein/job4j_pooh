package ru.job4j.pooh;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, BlockingQueue<String>> topicMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BlockingQueue<String>> userMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = "null";
        Resp response = null;

        if (req.method().equals("POST")) {
            if (req.mode().equals("topic")) {
                if (!topicMap.containsKey(req.key())) {
                    BlockingQueue<String> bq = new LinkedBlockingQueue<>();
                    bq.add(req.message());
                    topicMap.put(req.key(), bq);
                } else {
                    BlockingQueue<String> qu = new LinkedBlockingQueue<>(topicMap.get(req.key()));
                    qu.add(req.message());
                    topicMap.put(req.key(), qu);
                }
            }
            response = new Resp(req.message(), 200);
        }
        if (req.method().equals("GET")) {
            if (topicMap.containsKey(req.key())) {
                userMap.put(req.id(), topicMap.get(req.key()));
                BlockingQueue<String> qu = userMap.get(req.id());
                if (qu.size() > 0) {
                    text = qu.poll();
                    userMap.put(req.id(), qu);
                } else {
                    userMap.remove(req.key());
                }
            }
            response = new Resp(text, 200);
        }
        return response;
    }
}