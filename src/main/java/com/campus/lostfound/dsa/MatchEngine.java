package com.campus.lostfound.dsa;
 import com.campus.lostfound.model.Item;
 import com.campus.lostfound.model.LostItem;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.PriorityQueue;

public class MatchEngine {
    public static class MatchResult implements Comparable<MatchResult>{
        private Item item;
        private int score;

        public MatchResult(Item item,int score){
            this.item=item;
            this.score=score;
        }
        public Item getItem(){
            return item;
        }
        public int getScore(){
            return score;
        }
        @Override 
        public int compareTo(MatchResult other){
            return other.score=this.score;
        }
    }
    public List<MatchResult> findMatches(LostItem lostItem,List<Item>foundItems){
        PriorityQueue<MatchResult>queue=new PriorityQueue<>();
        for(Item item: foundItems){
            if(!item.getType().equals("FOUND")){
                continue;
            }
            int score=0;
            if(lostItem.getName().equalsIgnoreCase(item.getName())){
                score+=40;
            }
            if(lostItem.getCategory().equalsIgnoreCase(item.getCategory())){
                score+=30;
            }
            if(lostItem.getLocation().equalsIgnoreCase(item.getLocation())){
                score+=30;
            }
            if(score>=40){
                queue.add(new MatchResult(item, score));
            }
        }
        List<MatchResult>results=new ArrayList<>();
        while(!queue.isEmpty()){
            results.add(queue.poll());
        }
        return results;
    }
    
}
