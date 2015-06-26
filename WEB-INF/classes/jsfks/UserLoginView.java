package jsfks;
import javax.servlet.http.HttpSession; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import redis.clients.jedis.Jedis;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletContext;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
 
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class UserLoginView implements Serializable  {
     
    private String username;
     
    private String password;
 
    private boolean session=false;

    private Jedis conn;
    
    private List<String> grupos;
    
    private Set<String> losprofes;
    private List<String> lprofes;
    private List<List<String>> profes;
    private List<String> gruposBlanco;
    private String backurl;
    private boolean back=false;

    public String getUsername() {
        return username;
    }
 
    public boolean isSession() {
        return this.session;
    }
    public void setSession(boolean session) {
        this.session=session;
    }

    public boolean getSession() {
        return session;
    }
    public void setUsername(String username) {
        this.username = username;
    }
 
    public List<List<String>> getProfes() {
         return this.profes;
    }

    public List<String> getLprofes() {
        return this.lprofes;
    }

    public Set<String> getLosprofes() {
        return this.losprofes;
    }
    public void setLosprofes(Set<String> losprofes) {
        this.losprofes=losprofes;
    }
    public void setGruposBlanco(List<String> grupos) {
        this.gruposBlanco=grupos;
    }

    public void setGrupos(List<String> grupos) {
        this.grupos=grupos;
    }

    public List<String> getGruposBlanco() {
        return this.gruposBlanco;
    }
    
    public List<String> getGrupos() {
        return this.grupos;
    }

    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
   
    public String action() {
       if(this.session) {
          return "index51-19";
       } 
       return null;
    }

    public void logout(ActionEvent event) {
       FacesContext facesContext = FacesContext.getCurrentInstance();
       HttpSession sess = (HttpSession) facesContext.getExternalContext().getSession(true);
       sess = (HttpSession) facesContext.getExternalContext().getSession(false);
       sess.invalidate();
       session=false;
       
       username="";
    }

    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = null;
        boolean loggedIn = false;
        boolean next=false;
         
        if(username != null && username.equals("paulino") && password != null && password.equals("paulino")) {
          next=true;
        }
        if(username != null && username.equals("pedro") && password != null && password.equals("pedro")) {
          next=true;
        }
        if(username != null && username.equals("juan") && password != null && password.equals("juan")) {
          next=true;
        }
        if(username != null && username.equals("jestudios") && password != null && password.equals("jestudios")) {
          next=true;
        }
        if(next) {
            loggedIn = true;
            this.session = true;
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", username);
            conn = new Jedis("localhost");
            conn.select(10);
          
            //
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            HttpSession sess = (HttpSession) facesContext.getExternalContext().getSession(true);
            sess.setAttribute("username", username);
            LoginManager loginManager = (LoginManager) servletContext.getAttribute("loginManager");
            loginManager.getUsers().add(username);
            gruposBlanco=new ArrayList<String>();
            gruposBlanco.add("");
            
            grupos=new ArrayList<String>();
            grupos = conn.lrange("cuaderno:profe:" + username, 0, -1);

            if(username.equals("jestudios")) {
              losprofes=conn.smembers("cuaderno:profes"); 
              profes = new ArrayList<List<String>>();
              lprofes = new ArrayList<String>();
              lprofes.addAll(losprofes);
              List<String> grupInfo;
              for(String st : lprofes) {
                 grupInfo = new ArrayList<String>();
                 grupInfo = conn.lrange("cuaderno:profe:" + st, 0, -1);
                 profes.add(grupInfo);
              }
            }
         }
         else {
            loggedIn = false;
            this.session=false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
    }   
    public String getBackurl() {
      return this.backurl;
    }
    public void setBackurl(String backurl) {
      this.backurl=backurl;
    }
    public String actionBackurl2(String backPage) {
      if(!backPage.equals(this.backurl)) {
        this.backurl=backPage;
        back=true;
        return "notas";
      }
      return "notas";
      //return null;
    }
    public String actionBackurl1(String backPage) {
      if(!backPage.equals(this.backurl)) {
        this.backurl=backPage;
        back=true;
        return "sugerencias";
      }
      return "sugerencias";
      //return null;
    }
    public boolean isBack() {
      return back;
    }
}
