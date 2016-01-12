package bookmaker;

import model.User;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


/**
 * Bean for registering a new user.
 */
@ManagedBean
@SessionScoped
public class RegistrationBean implements Serializable {
	
	/**
	 * form evaluation setting min length
	 */
	private final static int MIN_FIRSTNAME=3,
			MIN_LASTNAME=3,
			MIN_PW=6;
	
	/**
	 * Simple Email regex and regex to
	 * check Names for unallowed characters name starts with alphabetical character
	 */
	private final Pattern EmailPattern = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9-._]{1,15}@[a-zA-Z0-9]{1,15}.[a-zA-Z]{3}");
	private final Pattern NamePattern = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9-._]{2,15}");
	
	/**
	 * error messages and field labels
	 */
	private final static String FIRSTNAME ="firstname",
			LASTNAME="lastname",
			EMAIL="email",
			PW0="password",
			PW1="passwordrepeat",
			FIELD_EMTY="fieldEmpty",
			TOO_SHORT="tooShort",
			PW_MISMATCH="pwMismatch",
			NOT_NAME="notName",
			NOT_MAIL="notEmail";

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }

    private String firstname,lastname,email,pw0,pw1,msg="nan",param0="",param1="";
    
    public String getFirstname(){return firstname;}
    public String getLastname(){return lastname;}
    public String getEmail(){return email;}
    public String getPw0(){return pw0;}
    public String getPw1(){return pw1;}
    
    public void setFirstname(String firstname){this.firstname=firstname;}
    public void setLastname(String lastname){this.lastname=lastname;}
    public void setEmail(String email){this.email=email;}
    public void setPw0(String pw0){this.pw0=pw0;}
    public void setPw1(String pw1){this.pw1=pw1;}
    
    
    public String getMsg() {return msg;}
    public void setMsg(String msg){ this.msg = msg;}
    public String getParam0() {return param0;}
    public void setParam0(String param){ this.param0 = param;}
    public String getParam1() {return param1;}
    public void setParam1(String param){ this.param1 = param;}
    
    public RegistrationBean(){

    }

    /**
     * Registers a new user and the sets this user as active.
     * Uses the PasswordManager for hashing.
     * @return the next view to render
     */
    public String register(){
        try{
            //procide if input is valid
            if(validateFields()){
            // hash the password
            String hashedPW = PasswordManager.hashPassword(this.getPw0());
            User user = new User(this.getEmail(), this.getFirstname(), this.getLastname(), hashedPW, false);
            
            //save the newly generated user
            Session hibernateSession = session.getSessionFactory().openSession();
            hibernateSession.beginTransaction();
            hibernateSession.save(user);
            hibernateSession.getTransaction().commit();
            hibernateSession.close();
            
            //log in the new user
            session.loginNewUser(user);
            msg="regSuc";
            return "home";
            }else{return "registration";}
        }
        catch(Exception e){
            msg = e.getMessage();
            return "registration";
        }


    }
    
    public String list(){
    	return "listGames";
    }
    
    /**
     * validate the input of registaration form
     * and set error message in case of invalid inputs
     * @return boolean if input was correct
     */
    private boolean validateFields(){

    	//test some length constraints
    	if(firstname == null){
    		msg=FIELD_EMTY;
    		param0=FIRSTNAME;
    		return false;
    	}
    	if(lastname == null){
    		msg=FIELD_EMTY;
    		param0=LASTNAME;
    		return false;
    	}
    	if(email == null){
    		msg=FIELD_EMTY;
    		param0=EMAIL;
    		return false;
    	}	
    	if(pw1 == null){
    		msg=FIELD_EMTY;
    		param0=PW0;
    		return false;
    	}
    	if(pw0 == null){
    		msg=FIELD_EMTY;
    		param0=PW1;
    		return false;
    	}
    	if(firstname.length() < MIN_FIRSTNAME){
    		msg=TOO_SHORT;
    		param0=FIRSTNAME;
    		param1=""+MIN_FIRSTNAME;
    		return false;
    	}
        if(lastname.length() < MIN_LASTNAME){
        	msg=TOO_SHORT;
        	param0=LASTNAME;
        	param1=""+MIN_LASTNAME;
        	return false;
        }
        if(pw0.length() < MIN_PW){
        	msg=TOO_SHORT;
        	param0=PW0;
        	param1=""+MIN_PW;
        	return false;
        }
        //both pw must match
        if(!pw0.equals(pw1)){
            msg=PW_MISMATCH;
            return false;
        }
        
        //test with regex
        Matcher matchMail = EmailPattern.matcher(email);
        if(!matchMail.matches() ){
        	msg=NOT_MAIL;
        	param0=EMAIL;
        	return false;
        }
        Matcher matchFirstname = NamePattern.matcher(firstname);
        if(!matchFirstname.matches() ){
        	msg=NOT_NAME;
        	param0=FIRSTNAME;
        	return false;
        }
        Matcher matchLastme = NamePattern.matcher(lastname);
        if(!matchLastme.matches() ){
        	msg=NOT_NAME;
        	param0=LASTNAME;
        	return false;
        }
        
    	return true;
    }
}
