package com.example.book_your_seat.common.service;

import com.example.book_your_seat.common.entity.color.Color;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

    @Service
    public class SlackServiceImpl implements SlackService {

        @Value("${webhook.slack.url}")
        private String slackUrl;

        private final Slack slackClient = Slack.getInstance();

        @Override
        public void setErrorMessage(String title, LinkedHashMap<String, String> data) {
            try{
                slackClient.send(slackUrl, payload(p -> p
                        .text(title)
                        .attachments(List.of(
                                Attachment.builder().color(Color.RED.getCode())
                                        .fields(
                                                data.keySet().stream().map(key -> generateSlackField(key, data.get(key))).collect(Collectors.toList())
                                        ).build())))
                );
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setPaymentMessage(String title, LinkedHashMap<String, String> data) {
            try{
                slackClient.send(slackUrl, payload(p -> p
                        .text(title)
                        .attachments(List.of(
                                Attachment.builder().color(Color.BLUE.getCode())
                                        .fields(
                                                data.keySet().stream().map(key -> generateSlackField(key, data.get(key))).collect(Collectors.toList())
                                        ).build())))
                );
            }catch (IOException e) {
                e.printStackTrace();
            }

    }


    private Field generateSlackField(String key, String data) {
        return Field.builder()
                .title(key)  // 필드 제목
                .value(data)  // 필드 내용
                .valueShortEnough(false)  // Slack 메시지 필드에 Slack의 메시지가 짧은지여부
                .build();

    }


    }




