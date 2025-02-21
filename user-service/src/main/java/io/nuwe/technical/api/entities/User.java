package io.nuwe.technical.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="USERS")
public class User {

    /* TODO TASK 3: Subscription */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String email;
    private int age;
    @JsonIgnore
    private boolean isSubscribed;

    public User(){
	super();
    }

    public User(String name, String email, int age){
	super();
	this.name = name;
	this.email = email;
	this.age = age;
	this.isSubscribed = true;
    }

    public long getId() {
	return this.id;
    }

    public String getName() {
	return this.name;
    }

    public String getEmail() {
	return this.email;
    }

    public int getAge() {
	return this.age;
    }

    public boolean getIsSubscribed() {
	return this.isSubscribed;
    }

    public void setName(String name){
	this.name = name;
    }

    public void setEmail(String email){
	this.email = email;
    }

    public void setAge(int age){
	this.age = age;
    }

    public void setId(long id){
	this.id = id;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

}
