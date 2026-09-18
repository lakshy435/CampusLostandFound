package com.campus.lostfound.model;
import java.io.Serializable;

public class User implements Serializable {

    private final String userId;
    private String name;
    private final String passwordHash;
    private final Role role;

    public User(String userId,String name,String passwordHash,Role role){
        this.userId = userId;
        this.name = name;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    public String getUserId(){
 return userId;
    }
    public String getName(){
        return name;
    }
    public String getPasswordHash(){
        return passwordHash;
    }
    public Role getRole(){
        return role;
    }
   public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return userId + " | " + name + " | " + role;
    }

    
}
