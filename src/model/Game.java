package model;

import java.util.Date;
import java.util.Map;


public class Game {

    private Date startOfGame;
    private String homeTeam,guestTeam;
    private Map<Condition,Float> conditions;
    
    
    public boolean started(){
    	
    	Date d = new Date(System.currentTimeMillis());
    	return d.after(startOfGame);
    }
    
    public void Game(Date start,String homeTeam,String guestTeam){
    	this.startOfGame=start;
    	this.homeTeam=homeTeam;
    	this.guestTeam=guestTeam;
    }
    
    public float getOddByCondition(Condition c){
    	float f = 0;
    	if(conditions.containsKey(c))
    	f=conditions.get(c);
    	return f;
    }
    
    public void placeOdds(Condition c,Float f){
    	if(!started()){
    		conditions.put(c, f);
    	}
    }
}
