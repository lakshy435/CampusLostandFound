package com.campus.lostfound.dsa;
import com.campus.lostfound.model.Item;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemSearch {
    private Map<String,Item>itemIndex;
    public ItemSearch(){
        itemIndex=new HashMap<>();
    }

        public void buildIndex(List<Item>items){
            itemIndex.clear();
            
        
        for(Item item:items){
            itemIndex.put(
                item.getItemId(),item
            );
        }
    }
    public Item searchbyId(String itemId){
        return itemIndex.get(itemId);
    }
    public List<Item> searchbyKeyword(
        List<Item> items,
        String keyword
    ){
        List<Item>result=new ArrayList<>();
        keyword=keyword.trim().toLowerCase();
        for(Item item:items){
            String name=item.getName().toLowerCase();
            String category=item.getCategory().toLowerCase();
            String location=item.getLocation().toLowerCase();
            String description=item.getDescription().toLowerCase();
            boolean found=false;
            if(name.contains(keyword)){
                found=true;
            }
            if(category.contains(keyword)){
                found=true;
            }
            if(location.contains(keyword)){
                found=true;
            }
            if(description.contains(keyword)){
                found=true;
            }
            if(found){
                result.add(item);
            }
        }
        return result;
    }
    
}
