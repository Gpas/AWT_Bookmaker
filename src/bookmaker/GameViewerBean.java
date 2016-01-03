package bookmaker;


import model.Bet;
import model.Condition;
import model.Game;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ManagedBean
@ViewScoped
public class GameViewerBean implements Serializable {

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }

    private Game activGame;
    private int activGameId = -1;
    private List<Condition> conditions;
    private int betAmount;


    public GameViewerBean(){

    }

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
            hibernateSession.close();
            return games;
        }
    }

    public void loadGameDetails(){
        if(activGameId != -1){
            // Load Conditions
            Session hibernateSession = session.getSessionFactory().openSession();
            String hql = "FROM Condition cond WHERE cond.game.id = :gameId";
            Query query = hibernateSession.createQuery(hql);
            query.setParameter("gameId", activGameId);
            // Add conditions to list for displaying in datatable
            this.conditions = query.list();
            Set<Condition> conditions = new HashSet<>(this.conditions);
            // Load Game and add Conditions
            hql = "FROM Game game WHERE game.id = :gameId";
            query = hibernateSession.createQuery(hql);
            query.setParameter("gameId", activGameId);
            activGame = (Game) query.list().get(0);
            activGame.setConditions(conditions);
            hibernateSession.close();
        }
    }

    public boolean betOnCondition(Condition condition){
        User user = session.getUser();
        if(user.getBalance() >= betAmount){
            // Create new bet
            Bet bet = new Bet(user, condition, betAmount);
            // Get the gameowner
            User gameowner = condition.getGame().getOwner();
            // Move the money from user to gameowner
            if(!user.changeBalance(-betAmount)){
                return false;
            }
            gameowner.changeBalance(betAmount);
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


    public Game getActivGame() {
        return activGame;
    }

    public void setActivGame(Game activGame) {
        this.activGame = activGame;
    }

    public int getActivGameId() {
        return activGameId;
    }

    public void setActivGameId(int activGameId) {
        this.activGameId = activGameId;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }
}
