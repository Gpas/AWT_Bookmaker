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

    public static void readProperties(String language){
        bundle = ResourceBundle.getBundle("lang", new Locale(language));
        conditions = bundle.getStringArray("conditions");
        teams = bundle.getStringArray("teams");
    }

    // For a draw or properties where no team leads
    public static String getConditionPerId(int id){
        return conditions[id];
    }

    // When one team leads without specific parameters
    public static String getConditionPerId(int id, String leadingTeam){
        return String.format(conditions[id], leadingTeam);
    }

    // For the situations where one team leads at a specific time and/or amount of goals
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

    // Get the corresponding team name
    public static String getTeamPerId(int id){
        return teams[id];
    }

}
