package bookmaker;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.hibernate.Session;

import model.User;

/**
 * Bean for adding a amount of money to a users game credit
 */
@ManagedBean
@SessionScoped
public class BalanceBean implements Serializable{
	
	// example credidcard and validation code
	private static final int CARD_NUM = 1234;
	private static final int VALIDATION = 234;
	
	
	/**
	 * Form Fields
	 * 4 creditcard input and a input for validation code
	 */
	private int cardinputA,
		cardinputB,
	    cardinputC,
	    cardinputD,
	    validation;
	
	/**
	 * output messages
	 */
	private String msg="validateCard";
	
	/**
	 * amount of money that is transfered to an users game credit
	 */
	private BigDecimal amount = new BigDecimal("0.00");
	
	public int getCardinputA(){return this.cardinputA;}
	public int getCardinputB(){return this.cardinputB;}
	public int getCardinputC(){return this.cardinputC;}
	public int getCardinputD(){return this.cardinputD;}
	public void setCardinputA(int in){this.cardinputA=in;}
	public void setCardinputB(int in){this.cardinputB=in;}
	public void setCardinputC(int in){this.cardinputC=in;}
	public void setCardinputD(int in){this.cardinputD=in;}
	
	public int getValidation(){return this.validation;}
	public void setValidation(int v){this.validation=v;}
	
	public BigDecimal getAmount(){return this.amount;}
	public void setAmount(BigDecimal amount){this.amount = amount;}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;
	
	/**
	 * boolean indicates the clearanc of a creditcard
	 * is set to true if a card is accepted
	 */
	private boolean clear = false;
	
	public boolean isClear(){return this.clear;}
	
	 public void setSession(SessionBean session) {
	        this.session = session;
	 }
	 
	 
	 /**
	  * Querry the balance of the current users game credit with currency symbol (Fr) 
	  * @return a formated String that represents the current balance of a user
	  */
	 public String getCurrentBalance(){
		 User u = session.getUser();
		 Session hibernateSession = session.getSessionFactory().openSession();
		 u = hibernateSession.load(User.class, u.getId());
		 String s = String.format("%.2f Fr",u.getBalance());
		 return s;
	 }
	 
	 /**
	  * add a amount to a users game credit
	  * amount must be positive and credit card must be validated
	  * @return void
	  */
	 public void addToBalance(){
		 
		 //card must be validated and amount must be positive
		 if(clear && amount.intValue() > 0){
			 
			 User u = session.getUser();
			 u.changeBalance(amount, true);
			 
			 //update database
			 Session hibernateSession = session.getSessionFactory().openSession();
			 hibernateSession.beginTransaction();
		     hibernateSession.update(u);
			 hibernateSession.getTransaction().commit();
			 hibernateSession.close();
			 
			 //reset attributes
			 amount = BigDecimal.ZERO;
			 clear = false;
			 msg="transactionComplete";
		 }else{
			 //message if amount was not added to balance
			 msg="transactionFailed";
		 }
	 }
	 
	 /**
	  * test if input for credit card matches the example
	  * card number
	  * if it matches set clear flag to true
	  * @return void
	  */
	 public void validateCreditCard(){
		 
		 //check if inputfields are 1234
		 // and validation field 234
		 if(CARD_NUM == cardinputA &&
		    CARD_NUM == cardinputB &&
		    CARD_NUM == cardinputC &&
		    CARD_NUM == cardinputD &&
	        VALIDATION == validation){
			 
			 //set clearance to true
	    	 clear = true;
	    	 msg="valSuc";
	    	 
	    	 //reset everything
	    	 cardinputA=0;
	    	 cardinputB=0;
	    	 cardinputC=0;
	    	 cardinputD=0;
	    	 validation=0;
	     }else{ 
	    //error message if card number does not match
		   msg="valError";
		   clear = false;
	     }
	 }
}
