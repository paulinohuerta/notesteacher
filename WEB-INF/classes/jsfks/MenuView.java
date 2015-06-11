package jsfks;
 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import javax.annotation.PostConstruct;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuElement;
import javax.faces.bean.ManagedProperty;
import org.primefaces.context.RequestContext;
import java.util.HashMap;
import java.util.Map;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import org.primefaces.event.MenuActionEvent;
import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.ArrayList;
 
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class MenuView implements Serializable  {

    private MenuModel model;
    
    @ManagedProperty("#{userLoginView}")
    private UserLoginView user;
 
    @PostConstruct
    public void init() {
        List<String> gru = new ArrayList<String>();
        gru=user.getGrupos();

        model = new DefaultMenuModel();
         
        //First submenu
        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Dynamic Submenu");
        DefaultMenuItem item = new DefaultMenuItem("External");
        item.setUrl("http://www.primefaces.org");
        item.setIcon("ui-icon-home");
        firstSubmenu.addElement(item);
         
        model.addElement(firstSubmenu);
         
        //Second submenu
        DefaultSubMenu secondSubmenu = new DefaultSubMenu("Grupos");
        //Map<String, List<String>> map = new HashMap<String,List<String>>(); 
        //List<String> val=new ArrayList<String>();
        //val.add("manzana");
        //val.add("collar");
        for (String st : gru) { 
          item = new DefaultMenuItem(st);
           //item.setIcon("ui-icon-disk");
          //item.setCommand(String.format("#{menuView.save(%s)}", st));
          item.setCommand("#{menuView.save}");
          item.setUpdate("messages");
          //map.put(st, val);
          //item.setParams(map);
          secondSubmenu.addElement(item);
        }
         
        model.addElement(secondSubmenu);
    }

    public void setUser(UserLoginView user) {
        this.user=user;
    }

    public UserLoginView getUser() {
          return this.user;
    }
    
    public void displayList(ActionEvent event) {
      MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();
      Long id = Long.parseLong(menuItem.getParams().get("listId").get(0));
    }
     
    public MenuModel getModel() {
        return model;
    }   
     
    public void save() {
        addMessage("Success", "Data saved");
    }
     
    public void update() {
        addMessage("Success", "Data updated");
    }
     
    public void delete() {
        addMessage("Success", "Data deleted");
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
