package com.campus.lostfound.model;

import java.time.LocalDate;

public class FoundItem extends Item {
    public FoundItem(
         String itemId,
        String reporterId,
        String name,
        String category,
        String location,
        LocalDate date,
        String description
    ){
        super(
            itemId, 
            reporterId, 
            name, 
            category, 
            location, 
            date, 
            description, 
            ItemStatus.ACTIVE);
    }
    @Override
    public String getType() {
        return "FOUND";
    }
    
    
}
