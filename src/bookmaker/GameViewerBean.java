package bookmaker;


import model.Bet;
import model.Condition;
import model.Game;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@SessionScoped
public class GameViewerBean implements Serializable {

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean session;

    public void setSession(SessionBean session) {
        this.session = session;
    }

    private Game activGame;
    private int activGameId = -1;
    private List<Condition> conditions;
    private float betAmount;
    private String message;
    private Map<String, Bet> bets;
    private User gameowner;


    public GameViewerBean(){

    }

    public List<Game> listUserGames(){
        User user = session.getUser();
        if(user.getIsBookmaker()){
            List<Game> games = new ArrayList<>();
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
            Set<Game> gamesSet = new HashSet<>();
            for(Bet bet : result){
                gamesSet.add(bet.getCondition().getGame());
            }
            hibernateSession.close();
            return new ArrayList<>(gamesSet);
        }
    }

    public String resetGameId(){
        activGameId = -1;
        return "games";
    }

    public void loadGameDetails(){
        message = "";
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
            hql = "FROM Game game left join fetch game.owner user WHERE game.id = :gameId";
            query = hibernateSession.createQuery(hql);
            query.setParameter("gameId", activGameId);
            activGame = (Game) query.list().get(0);
            activGame.setConditions(conditions);
            this.gameowner = activGame.getOwner();
            // Load the saved bets for this game if user is gambler
            User user = session.getUser();
            if(!user.getIsBookmaker()){
                this.bets = new HashMap<>();
                hql = "FROM Bet bet left join fetch bet.condition cond WHERE bet.user.id = :userId AND cond.game.id = :gameId";
                query = hibernateSession.createQuery(hql);
                query.setParameter("gameId", activGameId);
                query.setParameter("userId", user.getId());
                List<Bet> betsList = query.list();
                for(Bet bet : betsList){
                    this.bets.put(Integer.toString(bet.getCondition().getId()),bet);
                }
            }
            hibernateSession.close();
        }
    }

    public float getSavedBetAmount(String condId){
        if(bets.containsKey(condId)){
            return bets.get(condId).getAmount();
        }
        else{
            return 0;
        }
    }

    public void betOnCondition(Condition condition){
        Session hibernateSession = session.getSessionFactory().openSession();
        User user = hibernateSession.get(User.class, session.getUser().getId());
        if(user.getBalance() >= betAmount){
            // Create new bet
            Bet bet = new Bet(user, condition, betAmount);
            // Move the money from user to gameowner
            if(!user.changeBalance(betAmount, false)){
                message = "Not enough balance!";
                return;
            }
            gameowner.changeBalance(betAmount, true);
            hibernateSession.beginTransaction();
            hibernateSession.save(bet);
            hibernateSession.save(user);
            session.setUser(user);
            hibernateSession.save(gameowner);
            hibernateSession.getTransaction().commit();
            hibernateSession.close();
            // Put it in the bet map, so the ui gets refreshed
            this.bets.put(Integer.toString(condition.getId()),bet);
            message = "Successfully placed a bet";
            return;
        }
        else{
            message = "Not enough balance!";
            return;
        }
    }

    public void undoBet(Condition condition){
        Session hibernateSession = session.getSessionFactory().openSession();
        Bet bet = this.bets.get(Integer.toString(condition.getId()));
        //  delete Bet object
        String sql = "DELETE FROM bet WHERE id = :betId";
        Query query = hibernateSession.createSQLQuery(sql);
        query.setParameter("betId", bet.getId());
        query.executeUpdate();
        User user = hibernateSession.get(User.class, session.getUser().getId());
        User gameowner = hibernateSession.get(User.class, this.gameowner.getId());
        if(!gameowner.changeBalance(bet.getAmount(),false)){
            message = "Gameowner has not enough balance!";
            return;
        }
        user.changeBalance(bet.getAmount(),true);
        hibernateSession.beginTransaction();
        hibernateSession.save(user);
        session.setUser(user);
        hibernateSession.save(gameowner);
        this.gameowner = gameowner;
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
        return;
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

    public float getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(float betAmount) {
        this.betAmount = betAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
