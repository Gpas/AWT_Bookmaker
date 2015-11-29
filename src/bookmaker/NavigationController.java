package bookmaker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

@ManagedBean(name = "navigationController", eager = true)
@RequestScoped
public class NavigationController implements Serializable {

    //this managed property will read value from request parameter pageId
    @ManagedProperty(value="#{param.pageId}")
    private String pageId;

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    //condional navigation based on pageId
    public String showPage(){
        if(pageId == null){
            return "home";
        }
        if(pageId.equals("1")){
            return "registration";
        }else if(pageId.equals("2")){
            return "listGames";
        }else{
            return "home";
        }
    }
}
