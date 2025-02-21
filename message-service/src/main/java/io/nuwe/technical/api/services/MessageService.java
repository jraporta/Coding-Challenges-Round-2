package io.nuwe.technical.api.services;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
	this.messageRepository= messageRepository;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesByUserReceiverId(long userReceiverId){
        return messageRepository.findAllByUserReceiverId(userReceiverId);
    }

    public List<Message> getAllMessagesByUserSenderId(long userSenderId){
        return messageRepository.findAllByUserSenderId(userSenderId);
    }

    public List<Message> getAllMessagesByUserSenderIdAndUserReceiverId(long userSenderId, long userReceiverId){
        return messageRepository.findAllByUserSenderIdAndUserReceiverId(userSenderId, userReceiverId);
    }

    public Optional<Message> getMessageById(int id){
        return messageRepository.findById(id);
    }

    public Message createMessage(Message u){
	messageRepository.save(u);
	return u;
    }

    public void saveMessage(Message t){
	messageRepository.save(t);
    }

    public Message deleteMessage(Message message){
	messageRepository.delete(message);
	return message;
    }

    public void deleteAll(){
	messageRepository.deleteAll();
    }
}
