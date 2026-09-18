package com.campus.lostfound.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Security {
    private Security(){

    }
    
public static String hashPassword(String password){
    Validate.requiredtext(password);
    try{
        MessageDigest md=MessageDigest.getInstance("SHA-256");
        byte[]digest=md.digest(password.getBytes(StandardCharsets.UTF_16));
        StringBuilder result=new StringBuilder();
        for(byte b: digest){
            result.append(String.format("%02x",b));
        }
        return result.toString();
    }catch(NoSuchAlgorithmException e){
        throw new IllegalStateException("SHA-256 is unavailable",e);
    }
}
}
