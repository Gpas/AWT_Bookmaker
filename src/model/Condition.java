package model;

public class Condition {

	public enum State{
	    PENDING,
	    YES,
	    NO
	}
	
	private State state = State.PENDING; 
	private String description;
	
	public Condition(String description){
		this.description=description;
	}
	
	public State getState(){
		return state;
	}
	
	public void setState(State s){
		if(this.state==State.PENDING)
			this.state=s;
	}
}
