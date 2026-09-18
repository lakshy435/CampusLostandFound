package com.campus.lostfound.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
public class AppLogger {
    private static Logger logger=Logger.getLogger("campus not found sorry ");
    static{
        try{
            FileHandler pathfile=new FileHandler("data application.log",true);
            pathfile.setFormatter(new SimpleFormatter());
            logger.addHandler(pathfile);
            logger.setUseParentHandlers(false);


        }catch(IOException e){
            System.out.println("unable to load your file try later");
        }
    }
    private AppLogger(){

    }
    public static void info(String message){
        logger.info(message);
    }
    public static void warning(String message){
        logger.warning(message);
    }
    
}
