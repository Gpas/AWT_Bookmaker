package bookmaker;

import model.Condition;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.Format;
import java.text.MessageFormat;
import java.util.*;

/**
 * Utility class for fetching the appropriate text for conditions and teams from the properties file.
 */
public class PropertiesUtil {

    private ResourceBundle langBundle, condBundle, teamBundle;
    private Map<String, String> conditions, teams, conditionsSwapped, teamsSwapped, tempMap, tempMapSwapped;

    public PropertiesUtil(){

    }


    /**
     * Reads the properties file for the corresponding language and saves the conditions and teams in maps
     * Needs to be called once for proper initialization and each time the language changes
     * @param locale the desired language
     */
    public void readProperties(Locale locale){
        langBundle = ResourceBundle.getBundle("lang", locale);
        condBundle = ResourceBundle.getBundle("conditions", locale);
        teamBundle = ResourceBundle.getBundle("teams", locale);

        bundleToMap(condBundle);
        conditions = tempMap;
        conditionsSwapped = tempMapSwapped;
        bundleToMap(teamBundle);
        teams = tempMap;
        teamsSwapped = tempMapSwapped;
    }

    private void bundleToMap(ResourceBundle bundle) {
        // get the keys
        Enumeration<String> enumeration = bundle.getKeys();
        tempMap = new TreeMap<>();
        tempMapSwapped = new TreeMap<>();
        String key = "";
        while (enumeration.hasMoreElements()) {
            key = enumeration.nextElement();
            tempMap.put(bundle.getString(key), key);
            tempMapSwapped.put(key, bundle.getString(key));
        }

    }

    /**
     * For the situations where one team leads at a specific time and/or amount of goals
     * Gets the appropriate condition text with variables for the corresponding id
     * @param id conditionTextId
     * @param leadingTeam the leading team, else empty string
     * @param params additional params when needed, else empty string
     * @return condition text in String
     */
    public String getConditionPerId(String id, String leadingTeam, String params){
        int[] paramsInt = new int[0];
        if(params != null && !params.isEmpty() && params.contains(",")){
            String temp[] = params.split(",");
            paramsInt = new int[temp.length];
            for( int i = 0; i < temp.length; i++){
                paramsInt[i] = Integer.parseInt(temp[i]);
            }
        }
        else if(params != null && !params.isEmpty()){
            paramsInt = new int[1];
            paramsInt[0] = Integer.parseInt(params);
        }
        if(paramsInt.length == 2){
            return MessageFormat.format(conditionsSwapped.get(id), leadingTeam, paramsInt[0], paramsInt[1]);
        }
        else if(paramsInt.length == 1){
            return MessageFormat.format(conditionsSwapped.get(id), leadingTeam, paramsInt[0]);
        }
        else if(leadingTeam != null && !leadingTeam.isEmpty())
        {
            return MessageFormat.format(conditionsSwapped.get(id), leadingTeam);
        }
        else{
            return conditionsSwapped.get(id);
        }
    }

    /**
     * Gets the team name in relation to the id
     * @param id team-ID
     * @return Teamname in String
     */
    public String getTeamPerId(String id){
        return teamsSwapped.get(id);
    }

    /**
     * Gets a list of all teams within the properties file
     * Used for rendering the team select options
     * @return Map<String, String>
     */
    public Map<String, String> getTeamList(){
        return teams;
    }

    /**
     * Gets a list of all conditions within the properties file
     * Used for rendering the condition select options
     * @return Map<String, String>
     */
    public Map<String, String> getConditionList(){
        return conditions;
    }

}
