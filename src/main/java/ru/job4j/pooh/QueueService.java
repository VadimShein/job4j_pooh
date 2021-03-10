package ru.job4j.pooh;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, BlockingQueue<String>> queueMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String text = "null";
        Resp response = null;

        if (req.method().equals("POST")) {
            if (req.mode().equals("queue")) {
                if (!queueMap.containsKey(req.key())) {
                    BlockingQueue<String> bq = new LinkedBlockingQueue<>();
                    bq.add(req.message());
                    queueMap.put(req.key(), bq);
                } else {
                    BlockingQueue<String> qu = new LinkedBlockingQueue<>(queueMap.get(req.key()));
                    qu.add(req.message());
                    queueMap.put(req.key(), qu);
                }
            }
            response = new Resp(req.message(), 200);
        }
        if (req.method().equals("GET")) {
            if (queueMap.containsKey(req.key())) {
                BlockingQueue<String> qu = queueMap.get(req.key());
                if (qu.size() > 0) {
                    text = qu.poll();
                    queueMap.put(req.key(), qu);
                } else {
                    queueMap.remove(req.key());
                }
            }
            response = new Resp(text, 200);
        }
        return response;
    }
}