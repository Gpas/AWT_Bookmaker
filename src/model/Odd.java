package model;

public class Odd {
	
	private float odd0,odd1;
	
	public Odd(float odd0,float odd1){
		this.odd0=odd0;
		this.odd0=odd1;
	}
	
	public String toString(){
		return odd0+"/"+odd1;
	}
	
	public float calculateWinnings(float f){
		float odd = odd0/odd1;
		return odd*f;
	}
	

}
