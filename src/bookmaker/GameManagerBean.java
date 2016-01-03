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
import model.User;

import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class GameManagerBean implements Serializable{
	
	
	private Date startTime;
	private int condToRemove, state=0;
	private List<Condition> conditions=new ArrayList<>();
	private String[] conditionValues = null;
	private String winteam, chooseCond, odd, homeTeam, guestTeam;
	private Game newGame;

	private String debugMessage = "Test";

	
	public void setStartTime(Date startTime){this.startTime=startTime;}
	public void setHomeTeam(String homeTeam){this.homeTeam=homeTeam;}
	public void setGuestTeam(String guestTeam){this.guestTeam=guestTeam;}
	public void setCondToRemove(int condToRemove){this.condToRemove=condToRemove;}
	public void setState(int state){this.state = state;}
	public void setConditions(List<Condition> conditions){this.conditions=conditions;}
	public void setWinteam(String winteam){this.winteam = winteam;}
	public void setChooseCond(String chooseCond){this.chooseCond =chooseCond;}

	public String getOdd() {
		return odd;
	}

	public void setOdd(String odd) {
		this.odd = odd;
	}

	public String[] getConditionValues() {
		return conditionValues;
	}

	public void setConditionValues(String[] conditionValues) {
		this.conditionValues = conditionValues;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public Date getStartTime(){return this.startTime;}
	public String getHomeTeam(){return this.homeTeam;}
	public String getGuestTeam(){return this.guestTeam;}
	public int getCondToRemove(){return this.condToRemove;}
	public int getState(){return this.state;}
	public List<Condition> getConditions(){return this.conditions;}
    public String getWinteam(){return this.winteam;}
	public String getChooseCond(){return this.chooseCond;}
    
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public GameManagerBean(){}
	
	public List<Game> listUserGames(){
		List<Game> games = new ArrayList<>();
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
			if(!user.changeBalance(-amount)){
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
	
	//-------------- 
	
	public void nextState(){
		//User goes to the condition form
		if(state == 0){
			//Create game, so we can add conditions
			newGame = new Game(startTime,session.getUser(),Integer.parseInt(homeTeam),Integer.parseInt(guestTeam));
		}
		this.state+=1;
	}
	
	//-------------- section for game creation
	
	public void createNewGame(){
		Game g = new Game(startTime,session.getUser(),Integer.parseInt(homeTeam),Integer.parseInt(guestTeam));
		saveGame(g);
	}

	public void setParamFields(AjaxBehaviorEvent e){
		// One parameter
		if(chooseCond.equals("2") || chooseCond.equals("3")){
			conditionValues = new String[1];
		}
		// Two parameters
		else if(chooseCond.equals("4")){
			conditionValues = new String[2];
		}
		// No parameters
		else{
			conditionValues = null;
		}
	}
	
	//-------------- manipulate condition list for game creation
	public String removeCondition(){
		if(conditions.contains(condToRemove))
			conditions.remove(condToRemove);
		return null;
	}
	
	public void addCondition(AjaxBehaviorEvent e){
		if(chooseCond.equals("2") || chooseCond.equals("3") || chooseCond.equals("4")){
			String values = "";
			if(conditionValues != null){
				for(String item : conditionValues){
					values += item +",";
				}
				//Delete last char
				values = values.substring(0, values.length()-1);
			}
			conditions.add(new Condition(newGame, Integer.parseInt(chooseCond), Integer.parseInt(winteam) , Integer.parseInt(odd), values));
			debugMessage = "Game " +newGame.getId() + ", cond "+chooseCond + ", winteam " + winteam + ", odd " + odd + ", values " + values;
		}
		else{
			conditions.add(new Condition(newGame, Integer.parseInt(chooseCond), Integer.parseInt(winteam) , Integer.parseInt(odd)));
			debugMessage = "Game " +newGame.getId() + ", cond "+chooseCond + ", winteam " + winteam + ", odd " + odd;
		}
		debugMessage = "Test Test";
	}
	
}
