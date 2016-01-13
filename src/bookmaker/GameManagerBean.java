package bookmaker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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

/**
 * This bean manages the game creation process and the game listing on the home page.
 */
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

	/**
	 * Lists all running games. (starttime bigger than actual time)
	 * @return List of games
	 */
	public List<Game> listRunningGames(){
		List<Game> games = new ArrayList<>();
		Session hibernateSession = session.getSessionFactory().openSession();
		String hql = "FROM Game game WHERE game.startTime > :time";
		Query query = hibernateSession.createQuery(hql);
		query.setParameter("time",	new Date(System.currentTimeMillis()));
		List result = query.list();
		games.addAll(result);
		hibernateSession.close();
		return games;
	}

	/**
	 * Next State for game creation form
	 */
	public void nextState(){
		//User goes to the condition form
		if(state == 0){
			//Create game, so we can add conditions
			newGame = new Game(startTime,session.getUser(),Integer.parseInt(homeTeam),Integer.parseInt(guestTeam));
		}
		this.state+=1;
	}
	
	//-------------- section for game creation

	/**
	 * Creates a game and saves it in the DB
	 * Takes the values from the bean variables.
	 */
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
		this.conditions = new ArrayList<>();
	}

	/**
	 * Sets the amount of param fields to be rendered.
	 * Based on the properties file.
	 * Gets called with a ajax request.
	 * @param e
	 */
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

	/**
	 * Remove a condition from the condition list
	 * @param cond condition to remove
	 */
	public void removeCondition(Condition cond){
		if(conditions.contains(cond))
			conditions.remove(cond);
	}

	/**
	 * Add a condition to the condition list.
	 * @param e
	 */
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
			conditions.add(new Condition(newGame, condId, Integer.parseInt(winteam) , odd, values));
		}
		else{
			conditions.add(new Condition(newGame, condId, Integer.parseInt(winteam) , odd));
		}
	}
	
}
