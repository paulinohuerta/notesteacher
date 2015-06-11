package jsfks;
import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="unusuario")
@SessionScoped
public class User implements Serializable{
   private String name;

   public String getName() {return name;}
   public void setName(String name) { this.name=name;}
   public String accion() { 
     return "index";
   }
}
