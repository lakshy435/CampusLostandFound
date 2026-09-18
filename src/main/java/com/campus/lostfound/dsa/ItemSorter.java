package com.campus.lostfound.dsa;

import com.campus.lostfound.model.Item;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class ItemSorter {
    public enum SortType{
        DATE,NAME,CATEGORY,LOCATION
    }
    public static List<Item>sort(
        List<Item> items,
        SortType type
    ){
        List<Item>sortedItems=new ArrayList<>(items);
        if(type==SortType.DATE){
            sortedItems.sort(Comparator.comparing(
                Item::getDate
            ).reversed());
        }else if(type==SortType.NAME){
            sortedItems.sort(Comparator.comparing(
                item ->item.getName().toLowerCase()
            ));
        }else if(type==SortType.CATEGORY){
            sortedItems.sort(Comparator.comparing(
                item -> item.getCategory().toLowerCase()
            ));
        }else if(type==SortType.LOCATION){
            sortedItems.sort(Comparator.comparing(
                item -> item.getLocation().toLowerCase()
            ));
        }
        return sortedItems;
    }
    
}
