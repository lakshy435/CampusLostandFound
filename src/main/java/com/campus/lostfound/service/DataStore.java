package com.campus.lostfound.service;

import com.campus.lostfound.model.Claim;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.User;
import com.campus.lostfound.util.FileManager;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked") 
public class DataStore {
    private Map<String, User>users;
    private Map<String, Item>items;
    private Map<String, Claim>claims;

    public DataStore(){
        users=(Map<String, User>) FileManager.load(
            Path.of("data/users.dat"),
            new HashMap<String,User>()
        );
        items= (Map<String, Item>) FileManager.load(
            Path.of("data/items.dat"),
            new HashMap<String,Item>()
        );
        claims=(Map<String, Claim>) FileManager.load(
            Path.of("data/claims.dat"),
            new HashMap<String,Claim>()
        );
    }
    public Map<String, User> getUsers(){
        return users;
    } 
    public Map<String, Item> getItems(){
        return items;
    }
    public Map<String, Claim> getClaims(){
        return claims;
    }
    public void saveUsers(){
        FileManager.save(Path.of("data/users.dat"),
    users);
    }
    public void saveItems(){
        FileManager.save(Path.of("data/items.dat"),
        items);
    }
    public void saveClaims(){
        FileManager.save(Path.of("data/claims.dat"),
        claims);
    }
}