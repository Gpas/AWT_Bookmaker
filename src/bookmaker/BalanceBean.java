package bookmaker;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.hibernate.Session;

import model.User;

@ManagedBean
@SessionScoped
public class BalanceBean implements Serializable{
	
	private static final int CARD_NUM = 1234;
	private static final int VALIDATION = 234;
	
	private int cardinputA,
		cardinputB,
	    cardinputC,
	    cardinputD,
	    validation;
	private String msg="validateCard";
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
	
	private boolean clear = false;
	
	public boolean isClear(){return this.clear;}
	
	 public void setSession(SessionBean session) {
	        this.session = session;
	 }
	 
	 public String getCurrentBalance(){
		 User u = session.getUser();
		 Session hibernateSession = session.getSessionFactory().openSession();
		 u = hibernateSession.load(User.class, u.getId());
		 String s = String.format("%.2f Fr",u.getBalance());
		 return s;
	 }
	 
	 public void addToBalance(){
		 
		 if(clear && amount.intValue() > 0){
			 
			 User u = session.getUser();
			 u.changeBalance(amount, true);
			 Session hibernateSession = session.getSessionFactory().openSession();
			 hibernateSession.beginTransaction();
		     hibernateSession.update(u);
			 hibernateSession.getTransaction().commit();
			 hibernateSession.close();
			 amount = BigDecimal.ZERO;
			 clear = false;
			 msg="transactionComplete";
		 }else{
			 msg="transactionFailed";
		 }
	 }
	 
	 public void validateCreditCard(){
		 
		 if(CARD_NUM == cardinputA &&
		    CARD_NUM == cardinputB &&
		    CARD_NUM == cardinputC &&
		    CARD_NUM == cardinputD &&
	        VALIDATION == validation){
			 
	    	 clear = true;
	    	 msg="valSuc";
	    	 cardinputA=0;
	    	 cardinputB=0;
	    	 cardinputC=0;
	    	 cardinputD=0;
	    	 validation=0;
	     }else{ 
		   msg="valError";
		   clear = false;
	     }
	 }
	 
	 public boolean numberValidation(){
		 
		 return false;
	 }

	 
	 
}
