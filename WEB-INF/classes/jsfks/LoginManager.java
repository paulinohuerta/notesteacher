package jsfks;
import javax.servlet.http.HttpSession; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import redis.clients.jedis.Jedis;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import org.primefaces.context.RequestContext;

@ManagedBean(eager=true)
@ApplicationScoped
public class LoginManager implements Serializable {

    private Set<String> users = new HashSet<String>();

    public Set<String> getUsers() {
        return users;
    }
    public LoginManager() {
      // System.out.println("\\\\\\ Creado Login Manager ////////\n");
    }
}
