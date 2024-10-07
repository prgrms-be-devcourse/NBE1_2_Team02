package com.example.book_your_seat.common.service;

import java.util.LinkedHashMap;

public interface SlackService {

     void setErrorMessage(String title, LinkedHashMap<String, String> data);

}
