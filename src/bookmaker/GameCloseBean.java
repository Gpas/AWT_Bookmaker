package bookmaker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Bet;
import model.User;
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
	private Map<Long, Boolean> checked = new HashMap<>();

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
	public List<Game> getGamesToClose() {
		return gamesToClose;
	}
	public void setGamesToClose(List<Game> gamesToClose) {
		this.gamesToClose = gamesToClose;
	}

	public Map<Long, Boolean> getChecked() {
		return checked;
	}

	public void setChecked(Map<Long, Boolean> checked) {
		this.checked = checked;
	}

	@ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }
	

	public void loadGamesToClose(){
		
			// Load a List of Games not jet closed
			Session hibernateSession = session.getSessionFactory().openSession();
			String hql = "FROM Game game "
					+ "WHERE game.startTime < :time "
					+ "AND game.closed = :state";
			
			Date d= new Date(System.currentTimeMillis()-MIN90);
			String timestring= d.toString();
			msg = timestring;
			
			Query query = hibernateSession.createQuery(hql);
			query.setParameter("time", d);
			query.setParameter("state", false);
			this.setGamesToClose(query.list());
			hibernateSession.close();
	}
    
	public void loadGameDetails(){
		if(closeGameId != -1){
			// Load Conditions
			Session hibernateSession = session.getSessionFactory().openSession();
			closeGame = hibernateSession.load(Game.class, closeGameId);
			/*String hql = "FROM Condition cond left join fetch Bet WHERE cond.game.id = :gameId";
			Query query = hibernateSession.createQuery(hql);
			query.setParameter("gameId", closeGameId);
			// Add conditions to list for displaying
			this.conditions = query.list();
			Set<Condition> conditions = new HashSet<>(this.conditions);
			// Load Game and add Conditions
			hql = "FROM Game game left join fetch game.owner user WHERE game.id = :gameId";
			query = hibernateSession.createQuery(hql);
			query.setParameter("gameId", closeGameId);
			
			 closeGame = (Game) query.list().get(0);
			 closeGame.setConditions(conditions);*/
			// this.gameowner = closeGame.getOwner();
			this.conditions = new ArrayList<>(closeGame.getConditions());
	
			hibernateSession.close();
		}
	}
	
	public void closeGame(){
		Session hibernateSession;
		hibernateSession = session.getSessionFactory().openSession();
		closeGame.setClosed(true);
		hibernateSession.beginTransaction();
		hibernateSession.saveOrUpdate(closeGame);
		hibernateSession.getTransaction().commit();
		hibernateSession.close();
			for(Condition condition: this.conditions){
				if(checked.get(condition.getId())){
					hibernateSession = session.getSessionFactory().openSession();
					condition.setOccurred(true);
					String hql = "FROM Bet bet left join fetch bet.user user WHERE bet.condition.id = :condId";
					Query query = hibernateSession.createQuery(hql);
					query.setParameter("condId", condition.getId());
					List<Bet> bets = query.list();
					for(Bet bet : bets){
						BigDecimal amount = bet.getAmount().multiply(BigDecimal.valueOf(condition.getOdd()));
						if(!closeGame.getOwner().changeBalance(amount, false)){
							// When the gameowner has not enough balance
							msg = "Gameowner has not enough balance";
							return;
						}
						bet.getUser().changeBalance(amount, true);
						hibernateSession.beginTransaction();
						hibernateSession.saveOrUpdate(bet.getUser());
						hibernateSession.getTransaction().commit();
						hibernateSession.close();
					}
					hibernateSession = session.getSessionFactory().openSession();
					hql = "UPDATE Condition cond SET cond.occurred = 1 WHERE cond.id = :condID";
					query = hibernateSession.createQuery(hql);
					query.setParameter("condID", condition.getId());
					query.executeUpdate();
					hibernateSession.close();
					msg += " ," +condition.getId();
				}
			}
		checked.clear();
		closeGameId = -1;
	}
}
