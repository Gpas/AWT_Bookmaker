package model;

public class Condition {

	public enum State{
	    PENDING,
	    YES,
	    NO
	}
	
	private State state = State.PENDING; 
	private String description;
	
	public State getState(){
		return state;
	}
	
}
