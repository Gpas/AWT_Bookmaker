package bookmaker;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.User;

@ManagedBean
@SessionScoped
public class BalanceBean implements Serializable{
	
	private static final int CARD_NUM = 1234;
	private static final int VALIDATION = 234;
	
	private int card_input0,
		card_input1,
	    card_input2,
	    card_input3,
	    validation_input;
	
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;
	
	private boolean clear = false;
	
	 public void setSession(SessionBean session) {
	        this.session = session;
	 }
	 
	 public String getCurrentBalance(){
		 User u = session.getUser();
		 String s = String.format("%.2f Fr",u.getBalance());
		 return s;
	 }
	 
	 public void addToBalance(float f){
		 
		 if(clear){
			 User u = session.getUser();
			 u.changeBalance(f, true);
		 }
	 }
	 
	 public boolean validateCreditCard(){
		 
		 if(CARD_NUM == card_input0)
		 if(CARD_NUM == card_input1)
		 if(CARD_NUM == card_input2)
		 if(CARD_NUM == card_input3)
	     if(VALIDATION == validation_input){
	    	 clear = true;
	    	 return true;
	     }
	    	 
		 return false;
	 }
	 
	 
}
