package bookmaker;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.hibernate.Query;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class GameManagerBean implements Serializable{
	
	
	private Date startTime;
	private int state=0;
	private List<Condition> conditions=new ArrayList<>();
	private BigDecimal[] conditionValues = null;
	private BigDecimal odd = BigDecimal.ZERO;
	private String winteam, chooseCond, homeTeam, guestTeam;
	private Game newGame;

	public void setStartTime(Date startTime){this.startTime=startTime;}
	public void setHomeTeam(String homeTeam){this.homeTeam=homeTeam;}
	public void setGuestTeam(String guestTeam){this.guestTeam=guestTeam;}
	public void setState(int state){this.state = state;}
	public void setConditions(List<Condition> conditions){this.conditions=conditions;}
	public void setWinteam(String winteam){this.winteam = winteam;}
	public void setChooseCond(String chooseCond){this.chooseCond =chooseCond;}

	public BigDecimal getOdd() {
		return odd;
	}

	public void setOdd(BigDecimal odd) {
		this.odd = odd;
	}

	public BigDecimal[] getConditionValues() {
		return conditionValues;
	}

	public void setConditionValues(BigDecimal[] conditionValues) {
		this.conditionValues = conditionValues;
	}

	public Date getStartTime(){return this.startTime;}
	public String getHomeTeam(){return this.homeTeam;}
	public String getGuestTeam(){return this.guestTeam;}
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
		User user = session.getUser();
		if(user.getIsBookmaker()){
			Session hibernateSession = session.getSessionFactory().openSession();
			String hql = "FROM Game game WHERE game.owner.id = :userId";
			Query query = hibernateSession.createQuery(hql);
			query.setParameter("userId", user.getId());
			List result = query.list();
			games.addAll(result);
			hibernateSession.close();
			return games;
		}
		else{
			Session hibernateSession = session.getSessionFactory().openSession();
			String hql = "FROM Bet bet " +
					"left join fetch bet.condition condition " +
					"left join fetch condition.game " +
					"WHERE bet.user.id = :userId";
			Query query = hibernateSession.createQuery(hql);
			query.setParameter("userId", user.getId());
			List<Bet> result = query.list();
			for(Bet bet : result){
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
	
	public void createGame(){
		Session hibernateSession = session.getSessionFactory().openSession();
		hibernateSession.beginTransaction();
		hibernateSession.saveOrUpdate(newGame);
		for(Condition cond : conditions){
			hibernateSession.saveOrUpdate(cond);
		}
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
		newGame = null;
		this.state=0;
	}

	public void setParamFields(AjaxBehaviorEvent e){
		// One parameter
		if(chooseCond.equals("2") || chooseCond.equals("3")){
			conditionValues = new BigDecimal[]{BigDecimal.ZERO};
		}
		// Two parameters
		else if(chooseCond.equals("4")){
			conditionValues = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
		}
		// No parameters
		else{
			conditionValues = null;
		}
	}
	
	//-------------- manipulate condition list for game creation
	public void removeCondition(Condition cond){
		if(conditions.contains(cond))
			conditions.remove(cond);
	}
	
	public void addCondition(AjaxBehaviorEvent e){
		int condId = Integer.parseInt(chooseCond);
		if(condId >= 2){
			String values = "";
			if(conditionValues != null && conditionValues.length > 0){
				for(BigDecimal item : conditionValues){
					if(item != null){
						values = values + "," + item.toString();
					}
				}
				//Delete first char
				values = values.substring(1, values.length());
			}
			conditions.add(new Condition(newGame, condId, Integer.parseInt(winteam) , odd.intValue(), values));
		}
		else{
			conditions.add(new Condition(newGame, condId, Integer.parseInt(winteam) , odd.intValue()));
		}
	}
	
}
