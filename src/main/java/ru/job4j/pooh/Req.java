package ru.job4j.pooh;

import java.util.Objects;

public class Req {
    private final String method;
    private final String mode;
    private final String topic;
    private final String message;

    public Req(String text) {
        String[] lines = text.split("\n");
        String[] data = lines[0].split(" ");
        String[] parameters = data[1].split("/");
        this.message = lines[lines.length - 1];
        this.method = data[0];
        this.mode = parameters[1];
        this.topic = parameters[2];
    }

    public Req(String method, String mode, String topic, String message) {
        this.method = method;
        this.mode = mode;
        this.topic = topic;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public String getMethod() {
        return method;
    }

    public String getMode() {
        return mode;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Req req = (Req) o;
        return Objects.equals(method, req.method)
                && Objects.equals(mode, req.mode)
                && Objects.equals(topic, req.topic)
                && Objects.equals(message, req.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, mode, topic, message);
    }

    @Override
    public String toString() {
        return "Req{"
                + "method='" + method + '\''
                + ", mode='" + mode + '\''
                + ", topic='" + topic + '\''
                + ", message='" + message + '\'' + '}';
    }
}