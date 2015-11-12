package bookmaker;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean
@SessionScoped
public class RegistrationBean implements Serializable {
	
	//FacesContext context = FacesContext.getCurrentInstance();
   // ResourceBundle bundle = ResourceBundle.getBundle("lang", context.getViewRoot().getLocale());

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
        return "hello "+firstname+" "+lastname;
    }
    
    public RegistrationBean(){
        init();
    }
    
    
    private void init(){
    	
    }
    
    public void register(){
    	
    }
    
}
