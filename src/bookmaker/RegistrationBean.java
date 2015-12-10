package bookmaker;

import model.User;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class RegistrationBean implements Serializable {
	
	//FacesContext context = FacesContext.getCurrentInstance();
   // ResourceBundle bundle = ResourceBundle.getBundle("lang", context.getViewRoot().getLocale());

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }

    private String firstname,lastname,email,pw0,pw1,message;
    
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
    
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    
    public RegistrationBean(){
        init();
    }
    
    
    private void init(){
    	
    }
    
    public String register(){
        try{
            String hashedPW = PasswordManager.hashPassword(this.getPw0());
            User user = new User(this.getEmail(), this.getFirstname(), this.getLastname(), hashedPW, false);
            Session hibernateSession = session.getSessionFactory().openSession();
            hibernateSession.beginTransaction();
            hibernateSession.save(user);
            hibernateSession.getTransaction().commit();
            hibernateSession.close();
            session.setUser(user);
            return "home";
        }
        catch(Exception e){
            message = e.getMessage();
            return "registration";
        }


    }
    
    public String list(){
    	return "listGames";
    }
    
}
