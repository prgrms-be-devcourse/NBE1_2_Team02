package com.example.book_your_seat.common.service;

import com.example.book_your_seat.common.entity.color.Color;

import java.util.LinkedHashMap;

public interface SlackService {

     void setMessage(String title, LinkedHashMap<String, String> data, Color color);


}
