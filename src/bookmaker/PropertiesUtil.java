package bookmaker;

import model.Condition;

import java.text.Format;
import java.util.*;

/**
 * Utility class for fetching the appropriate text for conditions and teams from the properties file.
 */
public final class PropertiesUtil {

    private static ResourceBundle langBundle, condBundle, teamBundle;
    private static Map<String, String> conditions, teams;

    private PropertiesUtil(){

    }


    /**
     * Reads the properties file for the corresponding language and saves the conditions and teams in maps
     * Needs to be called once for proper initialization and each time the language changes
     * @param language the desired language
     */
    public static void readProperties(String language){
        langBundle = ResourceBundle.getBundle("lang", new Locale(language));
        condBundle = ResourceBundle.getBundle("conditions", new Locale(language));
        teamBundle = ResourceBundle.getBundle("teams", new Locale(language));

        conditions = bundleToMap(condBundle);
        teams = bundleToMap(teamBundle);
    }

    private static Map<String, String> bundleToMap(ResourceBundle bundle){
        // get the keys
        Enumeration<String> enumeration = bundle.getKeys();
        Map<String, String> tempMap = new HashMap<String, String>();
        String key = "";
        while (enumeration.hasMoreElements()) {
            key = enumeration.nextElement();
            tempMap.put(key,bundle.getString(key));
        }

        return tempMap;
    }

    /**
     * For a draw or properties where no team leads
     * @param id condition-ID
     * @return condition text in String
     */
    public static String getConditionPerId(String id){
        return conditions.get(id);
    }

    /**
     * When one team leads without specific parameters
     * @param id condition-ID
     * @param leadingTeam the leading team
     * @return condition text in String
     */
    public static String getConditionPerId(String id, String leadingTeam){
        return String.format(conditions.get(id), leadingTeam);
    }

    /**
     * For the situations where one team leads at a specific time and/or amount of goals
     * Gets the appropriate condition text with variables for the corresponding id
     * @param id condition-ID
     * @param leadingTeam the leading team
     * @param params if type is LEAD_TIME_AMOUNT, time and amount are needed, else only one int
     * @return condition text in String
     */
    public static String getConditionPerId(String id, String leadingTeam, int[] params){
        return String.format(conditions.get(id), leadingTeam, params);
    }

    /**
     * Gets the team name in relation to the id
     * @param id team-ID
     * @return Teamname in String
     */
    public static String getTeamPerId(String id){
        return teams.get(id);
    }

    /**
     * Gets a list of all teams within the properties file
     * Used for rendering the team select options
     * @return Map<String, String>
     */
    public static Map<String, String> getTeamList(){
        return teams;
    }

    /**
     * Gets a list of all conditions within the properties file
     * Used for rendering the condition select options
     * @return Map<String, String>
     */
    public static Map<String, String> getConditionList(){
        return conditions;
    }

}
