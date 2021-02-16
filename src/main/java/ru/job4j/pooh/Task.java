package ru.job4j.pooh;

public class Task {
    private String queue;
    private String text;

    public Task(String queue, String text) {
        this.queue = queue;
        this.text = text;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
