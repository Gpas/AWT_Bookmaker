package model;

import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Element;


public class Bet {
	
	private Game game;
	private Map<Condition,Float> placeOnOdds;
	
	public void betOnCondition(Condition c,float f){
		if(!game.started()){
			placeOnOdds.put(c, f);
		}
			
	}
	
	public float getWinnings(){
		
		float f=0;
		for(Entry<Condition, Float> e : placeOnOdds.entrySet()){
			
			Condition c=e.getKey();
			float val=e.getValue();
			
			if(c.getState()==Condition.State.YES){
				f+=game.getOddByCondition(c)*val;
			}
		}
		return f;
	}
	
}
