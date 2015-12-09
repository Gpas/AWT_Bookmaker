package bookmaker;

import model.Condition;

import java.text.Format;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility class for fetching the appropriate text for conditions and teams from the properties file.
 */
public final class PropertiesUtil {

    private static ResourceBundle bundle;
    private static String[] conditions, teams;

    private PropertiesUtil(){

    }

    public enum Type {
            LEAD_TIME,
            LEAD_AMOUNT,
            LEAD_TIME_AMOUNT;

    };

    /**
     * Reads the properties file for the corresponding language and saves the conditions and teams in arrays
     * Needs to be called once for proper initialization and each time the language changes
     * @param language the desired language
     */
    public static void readProperties(String language){
        bundle = ResourceBundle.getBundle("lang", new Locale(language));
        conditions = bundle.getStringArray("conditions");
        teams = bundle.getStringArray("teams");
    }

    /**
     * For a draw or properties where no team leads
     * @param id condition-ID
     * @return condition text in String
     */
    public static String getConditionPerId(int id){
        return conditions[id];
    }

    /**
     * When one team leads without specific parameters
     * @param id condition-ID
     * @param leadingTeam the leading team
     * @return condition text in String
     */
    public static String getConditionPerId(int id, String leadingTeam){
        return String.format(conditions[id], leadingTeam);
    }

    /**
     * For the situations where one team leads at a specific time and/or amount of goals
     * Gets the appropriate condition text with variables for the corresponding id
     * Possible type values are: LEAD_TIME, LEAD_AMOUNT, LEAD_TIME_AMOUNT
     * LEAD_TIME and LEAD_AMOUNT accept one param and LEAD_TIME_AMOUNT needs first the time and second the amount
     * @param id condition-ID
     * @param leadingTeam the leading team
     * @param params if type is LEAD_TIME_AMOUNT, time and amount are needed, else only one int
     * @param type see above
     * @return condition text in String
     */
    public static String getConditionPerId(int id, String leadingTeam, int[] params, Type type){
        if (type == Type.LEAD_TIME || type == Type.LEAD_AMOUNT){
            return String.format(conditions[id], leadingTeam, params[0]);
        }
        else if(type == Type.LEAD_TIME_AMOUNT){
            return String.format(conditions[id], leadingTeam, params[0], params[1]);
        }
        else{
            return "No such condition";
        }
    }

    /**
     * Gets the team name in relation to the id
     * @param id team-ID
     * @return Teamname in String
     */
    public static String getTeamPerId(int id){
        return teams[id];
    }

    /**
     * Gets a list of all teams within the properties file
     * Used for rendering the team select options
     * @return Stringarray with all existing teams
     */
    public static String[] getTeamList(){
        return teams;
    }

}
