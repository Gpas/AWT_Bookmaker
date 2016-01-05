package bookmaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.hibernate.Query;
import org.hibernate.Session;

import model.Condition;
import model.Game;

@ManagedBean
@SessionScoped
public class GameCloseBean implements Serializable{
	
	private static final long MIN90 = (long) 54E5;
	
	public GameCloseBean(){
	}
	
	private int closeGameId = -1;
	private Game closeGame;
	private List<Condition> conditions;
	private List<Integer> checkBoxes=new ArrayList<>();
	private String msg="";
	private List<Game> gamesToClose;

	public int getCloseGameId() {
		return closeGameId;
	}
	public void setCloseGameId(int closeGameId) {
		this.closeGameId = closeGameId;
	}
	public Game getCloseGame() {
		return closeGame;
	}
	public void setCloseGame(Game closeGame) {
		this.closeGame = closeGame;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public List<Integer> getCheckBoxes() {
		return checkBoxes;
	}
	public void setCheckBoxes(List<Integer> checkBoxes) {
		this.checkBoxes = checkBoxes;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }
	

	public void loadGameToClose(){
		if(closeGameId != -1){
			// Load a List of Games not jet closed
			Session hibernateSession = session.getSessionFactory().openSession();
			String hql = "FROM Game game "
					+ "WHERE game.startTime after :time "
					+ "AND game.closed = :state";
			Query query = hibernateSession.createQuery(hql);
			query.setParameter("time", new Date(System.currentTimeMillis()-MIN90));
			query.setParameter("state", false);
			this.gamesToClose = query.list();
			hibernateSession.close();
		}
	}
    
    
	public void loadGameDetails(){
		if(closeGameId != -1){
			// Load Conditions
			Session hibernateSession = session.getSessionFactory().openSession();
			String hql = "FROM Condition cond WHERE cond.game.id = :gameId";
			Query query = hibernateSession.createQuery(hql);
			query.setParameter("gameId", closeGameId);
			// Add conditions to list for displaying in datatable
			this.conditions = query.list();
			Set<Condition> conditions = new HashSet<>(this.conditions);
			// Load Game and add Conditions
			hql = "FROM Game game WHERE game.id = :gameId";
			query = hibernateSession.createQuery(hql);
			query.setParameter("gameId", closeGameId);
			closeGame = (Game) query.list().get(0);
			closeGame.setConditions(conditions);
			hibernateSession.close();
		}
	}
	
	public void closeGame(){
		
		msg="";
		for(int i: checkBoxes)
			for(Condition c: conditions)
				if(c.getId() == i){
					c.setOccurred(true);
					//msg+=" -"+i+"-";
				}
		
			
		closeGame.setClosed(true);
		
		Session hibernateSession = session.getSessionFactory().openSession();
		hibernateSession.beginTransaction();
		hibernateSession.save(closeGame);
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
	}



	
	
	
}
