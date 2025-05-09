package com.bguerra.springboot.backend.chat.services;

import com.bguerra.springboot.backend.chat.model.Message;

import java.util.List;

public interface MessageService {

    List<Message> findAll();

    void save(Message message);
}
