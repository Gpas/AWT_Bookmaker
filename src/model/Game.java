package model;

import java.util.Date;
import java.util.Map;


public class Game {

    private Date startOfGame;
    private String name;
    private Map<Condition,Float> conditions;
    
    
    public float getOddByCondition(Condition c){
    	float f = 0;
    	if(conditions.containsKey(c))
    	f=conditions.get(c);
    	return f;
    }
    
}
