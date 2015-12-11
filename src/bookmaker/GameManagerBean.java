package bookmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import model.Bet;
import model.Condition;
import model.Game;
import model.ProtoCondition;
import model.User;

import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class GameManagerBean implements Serializable{
	
	
	private Date startTime;
	private int homeTeam, guestTeam,
		condToRemove,
		chooscond;
	private List<ProtoCondition> conditions=new ArrayList<>();
	private String winteam;
	
	public void setStartTime(Date startTime){this.startTime=startTime;}
	public void setHomeTeam(int homeTeam){this.homeTeam=homeTeam;}
	public void setGuestTeam(int guestTeam){this.guestTeam=guestTeam;}
	public void setCondToRemove(int condToRemove){this.condToRemove=condToRemove;}
	public void setConditions(List<ProtoCondition> conditions){this.conditions=conditions;}
	public void setWinteam(String winteam){this.winteam = winteam;}
	public void setChooscond(int choosCond){this.chooscond=choosCond;}
	
	public Date getStartTime(){return this.startTime;}
	public int getHomeTeam(){return this.homeTeam;}
	public int getGuestTeam(){return this.guestTeam;}
	public int getCondToRemove(){return this.condToRemove;}
	public List<ProtoCondition> getConditions(){return this.conditions;}
    public String getWinteam(){return this.winteam;}
	public int getChooscond(){return this.chooscond;}
    
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

	public boolean betOnCondition(Condition condition, User user, float amount){
		if(user.getBalance() >= amount){
			// Create new bet
			Bet bet = new Bet(user, condition, amount);
			// Get the gameowner
			User gameowner = condition.getGame().getOwner();
			// Move the money from user to gameowner
			if(user.changeBalance(-amount)){
				return false;
			}
			gameowner.changeBalance(amount);
			Session hibernateSession = session.getSessionFactory().openSession();
			hibernateSession.beginTransaction();
			hibernateSession.save(bet);
			hibernateSession.save(user);
			hibernateSession.save(gameowner);
			hibernateSession.getTransaction().commit();
			hibernateSession.close();
			return true;
		}
		else{
			return false;
		}
	}

	public boolean undoBet(Bet bet){
		User user = bet.getUser();
		User gameowner = bet.getCondition().getGame().getOwner();
		if(gameowner.changeBalance(-bet.getAmount())){
			return false;
		}
		user.changeBalance(bet.getAmount());
		Session hibernateSession = session.getSessionFactory().openSession();
		hibernateSession.beginTransaction();
		hibernateSession.save(user);
		hibernateSession.save(gameowner);
		hibernateSession.delete(bet);
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		return true;
	}
	
	//-------------- section for game creation
	
	public void createNewGame(){
		User u = session.getUser();
		Game g = new Game(startTime,u,homeTeam,guestTeam);
		saveGame(g);
	}
	
	//-------------- manipulate condition list for game creation
	public void removeCondition(){
		if(condToRemove < conditions.size())
			conditions.remove(condToRemove);
	}
	
	public void addCondition(AjaxBehaviorEvent e){
		conditions.add(new ProtoCondition(winteam, chooscond,0,0));
	}
	
}
