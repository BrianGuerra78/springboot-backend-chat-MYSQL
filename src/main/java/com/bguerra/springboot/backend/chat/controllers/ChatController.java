package com.bguerra.springboot.backend.chat.controllers;

import com.bguerra.springboot.backend.chat.model.Message;
import com.bguerra.springboot.backend.chat.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
public class ChatController {

    private String[] colors = {"red", "blue", "green", "magenta", "orange", "purple", "yellow"};

    private final MessageService service;

    @Autowired
    private SimpMessagingTemplate websocket;

    public ChatController(MessageService service) {
        this.service = service;
    }

    @MessageMapping("/message")
    @SendTo("/chat/message")
    public Message reciveMessage(Message message){
        message.setDate(new Date().getTime());
        //message.setText("Recibido por el broker: " + message.getText());
        if (message.getType().equals("NEW_USER")){
            message.setColor(this.colors[new Random().nextInt(colors.length)]);
            message.setText("nuevo usuario");
        } else {
            service.save(message);
        }
        return message;
    }

    @MessageMapping("/writing")
    @SendTo("/chat/writing")
    public String isWriting(String username){
        return username.concat(" esta escribiendo ...");
    }

    @MessageMapping("/history")
    //@SendTo("/chat/history/{clientId}")
    //public List<Message> getHistoryMessages(@DestinationVariable String clientId){
    public void getHistoryMessages(@DestinationVariable String clientId){
        //return service.findAll();
        websocket.convertAndSend("/chat/history/".concat(clientId), service.findAll());
    }
}
