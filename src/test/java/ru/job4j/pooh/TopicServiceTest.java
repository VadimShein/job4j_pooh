package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenPostAndGetMessagesFromEmptyTopic() {
        TopicService topicService = new TopicService();
        topicService.process(
                new Req("POST", "topic", "topic1", "test message1")
        );

        Resp result1 = topicService.process(
                new Req("GET", "topic", "topic1", "")
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "topic1", "")
        );
        assertThat(result1.getText(), is("test message1"));
        assertThat(result2.getText(), is("Value not found"));
    }

    @Test
    public void whenPostAndGetTopic() {
        TopicService topicService = new TopicService();
        topicService.process(
                new Req("POST", "topic", "topic1", "test message")
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "topic1", "")
        );
        assertThat(result1.getText(), is("test message"));
    }

    @Test
    public void whenTopicIsNotFound() {
        TopicService topicService = new TopicService();

        topicService.process(
                new Req("POST", "topic", "topic1", "test message")
        );

        Resp result1 = topicService.process(
                new Req("GET", "topic", "anotherTopic", "")
        );
        assertThat(result1.getText(), is("Value not found"));
    }
}
