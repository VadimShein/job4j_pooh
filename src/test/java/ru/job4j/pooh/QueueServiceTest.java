package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueueServiceTest {
    @Test
    public void whenPostAndGetMessagesFromEmptyQueue() {
        TopicService topicService = new TopicService();
        topicService.process(
                new Req("POST", "queue", "topic1", "test message1")
        );

        Resp result1 = topicService.process(
                new Req("GET", "queue", "topic1", "")
        );
        Resp result2 = topicService.process(
                new Req("GET", "queue", "topic1", "")
        );
        assertThat(result1.getText(), is("test message1"));
        assertThat(result2.getText(), is("Value not found"));
    }

    @Test
    public void whenPostAndGetQueue() {
        QueueService queueService = new QueueService();

        queueService.process(
                new Req("POST", "queue", "topic1", "test message1")
        );

        Resp result = queueService.process(
                new Req("GET", "queue", "topic1", "")
        );
        assertThat(result.getText(), is("test message1"));
    }

    @Test
    public void whenTopicIsNotFound() {
        TopicService topicService = new TopicService();

        topicService.process(
                new Req("POST", "queue", "topic1", "test message")
        );

        Resp result1 = topicService.process(
                new Req("GET", "queue", "anotherTopic", "")
        );
        assertThat(result1.getText(), is("Value not found"));
    }
}