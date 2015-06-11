package jsfks;
 
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.ServletContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
public class SessionCounterListener implements HttpSessionListener {
 
  private static int totalActiveSessions;
 
  public static int getTotalActiveSession(){
	return totalActiveSessions;
  }
 
  @Override
  public void sessionCreated(HttpSessionEvent arg0) {
	totalActiveSessions++;
	//System.out.println("sessionCreated - add one session into counter");
  }
 
  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    totalActiveSessions--;
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession sess = (HttpSession) facesContext.getExternalContext().getSession(true);
    String st= (String) sess.getAttribute("username");
    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    LoginManager loginManager = (LoginManager) servletContext.getAttribute("loginManager");
    loginManager.getUsers().remove(st);
    //System.out.println("sessionDestroyed - deduct one session from counter *** " + " " + " ***\n");
  }	
}
