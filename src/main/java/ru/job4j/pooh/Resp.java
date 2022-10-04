package ru.job4j.pooh;

public class Resp {
    private final String text;
    private final int status;

    public Resp(String text, int status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Resp{"
                + "text='" + text + '\''
                + ", status=" + status + '}';
    }
}