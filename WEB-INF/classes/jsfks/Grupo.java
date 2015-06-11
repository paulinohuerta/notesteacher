package jsfks;
import javax.faces.event.ActionEvent; 
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
 
public class Grupo implements Serializable {

    private String clave; 
    private String descri;
 
    public Grupo() {
    }

    public Grupo(String clave, String descri) {
       this.descri=descri;
       this.clave=clave;
    }

    public String getClave() {
        return clave;
    }

    public String getDescri() {
        return descri;
    }
}
