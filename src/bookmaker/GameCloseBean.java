package bookmaker;

import java.io.Serializable;
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
	
	public GameCloseBean(){
	}
	
	private int closeGameId = 1;
	private Game closeGame;
	private List<Condition> conditions;
	private List<Condition> checkBoxes;

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
	public List<Condition> getCheckBoxes() {
		return checkBoxes;
	}
	public void setCheckBoxes(List<Condition> checkBoxes) {
		this.checkBoxes = checkBoxes;
	}
	
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
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
		closeGame.setClosed(true);
		for(Condition cond : checkBoxes)
			cond.setOccurred(true);
		
		Session hibernateSession = session.getSessionFactory().openSession();
		hibernateSession.beginTransaction();
		hibernateSession.save(closeGame);
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
	}


	
	
	
}
