package ru.job4j.pooh;

public class Req {
    private String text;
    private String[] parseText;
    private String[] parseMessageData;
    private String[] parseData;

    public Req(String text) {
        this.text = text;
        this.parseText = text.split("\n");
        this.parseMessageData = parseText[0].split(" ");
        this.parseData = parseMessageData[1].split("/");
    }

    public String valueOf(String key) {
        return null;
    }

    public String method() {    // post/get
        return parseMessageData[0];
    }

    public String mode() {  //queue/topic
        return parseData[1];
    }

    public String key() {   //weather
        return parseData[2];
    }

    public String message() {   //text
        return parseText[7];
    }

    public String id() {   //id
        return parseData[3];
    }
}