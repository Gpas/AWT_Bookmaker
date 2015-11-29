package bookmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Game;
import model.User;
import model.User_Game;

@ManagedBean
@SessionScoped
public class GameManagerBean implements Serializable{

	@ManagedProperty(value = "#{sessionBean.user}")
	private User user;	
	public void setUser(User user){this.user = user;}
	
	
	public GameManagerBean(){}
	
	public List<Game> getGamblerGames(){
		 
		List<Game> l=new ArrayList<>(); 
		//for(User_Game g : user.getGames())
		//	l.add(g.getGame());
		return l;
	}
	
	public void ListGames(){
		
	}
	
}
