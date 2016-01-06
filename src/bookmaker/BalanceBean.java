package bookmaker;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class BalanceBean implements Serializable{
	
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;
	
	 public void setSession(SessionBean session) {
	        this.session = session;
	 }
}
