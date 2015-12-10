package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;



@ManagedBean
@SessionScoped
public class Utility {
	
	public Set<Entry<Object, Object>> getTeams(){
		try {
		InputStream is = 
				getClass().getClassLoader().
				getResourceAsStream("teams_en.properties");
		Properties prop = new Properties();
		prop.load(is);
		return prop.entrySet();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Set<Entry<Object, Object>> getConditions(){
		try {
		InputStream is = 
				getClass().getClassLoader().
				getResourceAsStream("conditions_en.properties");
		Properties prop = new Properties();
		prop.load(is);
		return prop.entrySet();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
