package io.nuwe.technical.api.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userSenderId;
    private long userReceiverId;

    private String body;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentAt;

    public Message(){
	super();
	this.sentAt = LocalDateTime.now();
    }

    public Message(long userSenderId, long userReceiverId, String body){
	super();
	this.userSenderId = userSenderId;
	this.userReceiverId = userReceiverId;
	this.body = body;
	this.sentAt = LocalDateTime.now();
    }

    public long getId() {
	return this.id;
    }

    public long getUserSenderId() {
	return this.userSenderId;
    }

    public long getUserReceiverId() {
	return this.userReceiverId;
    }

    public String getBody() {
	return this.body;
    }

    public LocalDateTime getSentAt() {
	return this.sentAt;
    }

    public void setId(long id){
	this.id = id;
    }

    public void setUserSenderId(long userSenderId){
	this.userSenderId = userSenderId;
    }

    public void setUserReceiverId(long userReceiverId){
	this.userReceiverId = userReceiverId;
    }

    public void setBody(String body){
	this.body = body;
    }

    public void setSentAt(LocalDateTime sentAt){
	this.sentAt = sentAt;
    }

}
