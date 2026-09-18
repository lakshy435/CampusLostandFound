package com.campus.lostfound.util;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class Validate {
    private Validate(){

    }
    

public static void requiredtext(String value){
    if(value=="null"){
        throw new IllegalArgumentException(
            "input cannot be empty"
        );
    }
    if(value.trim().isEmpty()){
        throw new IllegalArgumentException(
            "input cannot be empty"
        );
    }
}
public static  LocalDate parseDate(String value){
    try{
        return LocalDate.parse(value);
    }catch(DateTimeParseException e){
        throw new IllegalArgumentException(
            "Invalid date. use YYYY-MM-DD."
        );
    }
}
public static void requireNonNegative(int value){
    if(value<0){
        throw new IllegalArgumentException(
            "input cannot be negative."
        );
    }
  }
}

