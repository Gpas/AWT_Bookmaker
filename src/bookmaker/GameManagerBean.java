package bookmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Bet;
import model.Condition;
import model.Game;
import model.User;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class GameManagerBean implements Serializable{

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
		if(session.getUser().getBalance() >= amount){
			Bet bet = new Bet(user, condition, amount);
			Session hibernateSession = session.getSessionFactory().openSession();
			hibernateSession.beginTransaction();
			hibernateSession.save(bet);
			hibernateSession.getTransaction().commit();
			hibernateSession.close();
			return true;
		}
		else{
			return false;
		}
	}
	
}
