package bookmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Bet;
import model.Game;
import model.User;

import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class GameManagerBean implements Serializable{
	
	
	private Date startTime;
	private int homeTeam, guestTeam;
	
	public void setStartTime(Date startTime){this.startTime=startTime;}
	public void setHomeTeam(int homeTeam){this.homeTeam=homeTeam;}
	public void setGuestTeam(int guestTeam){this.guestTeam=guestTeam;}
	
	public Date getStartTime(){return this.startTime;}
	public int getHomeTeam(){return this.homeTeam;}
	public int getGuestTeam(){return this.guestTeam;}

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public GameManagerBean(){}
	
	public List<Game> getUserGames(){
		List<Game> games = new ArrayList<Game>();
		if(session.getUser().getIsBookmaker()){
			games.addAll(session.getUser().getGames());
			return games;
		}
		else{
			Set<Bet> bets = session.getUser().getBets();
			for(Bet bet : bets){
				games.add(bet.getCondition().getGame());
			}
			return games;
		}
	}
	
	public void ListGames(){
		
	}

	public void createNewGame(){
		
		User u = session.getUser();
		Game g = new Game(startTime,u,homeTeam,guestTeam);
		
		
		saveGame(g);
	}
	
	public void saveGame(Game game){
		Session hibernateSession = session.getSessionFactory().openSession();
		hibernateSession.beginTransaction();
		hibernateSession.save(game);
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
	}

	public Game loadGame(int id){
		Session hibernateSession = session.getSessionFactory().openSession();
		Game game = (Game) hibernateSession.get(Game.class, id);
		hibernateSession.close();
		return game;
	}
	
}
