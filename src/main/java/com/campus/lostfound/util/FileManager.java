package com.campus.lostfound.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {
    private FileManager(){

    }
    
public static void save(Path filepath,Object saveddata){
    try{
        if(filepath.getParent()!=null){
            Files.createDirectories(filepath.getParent());
        }
        FileOutputStream file= new FileOutputStream(filepath.toFile());
        ObjectOutputStream output=new ObjectOutputStream(file);
        output.writeObject(saveddata);
        
        output.close();
        file.close();
    }catch(IOException e){
        throw new IllegalStateException("unable tostore the required data");
    }
}
public static Object load( Path filepath,
    Object defaultData){try{
        FileInputStream file=new FileInputStream(filepath.toFile());
        ObjectInputStream input=new ObjectInputStream(file);
        Object saveddata=input.readObject();
        input.close();
        file.close();
        return saveddata;

    }catch(IOException|ClassNotFoundException e){
        System.out.println("unable to load saved data ");
        return defaultData;
    }}
    
}
