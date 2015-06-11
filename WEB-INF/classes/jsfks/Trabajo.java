package jsfks;
import javax.faces.event.ActionEvent; 
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
 
public class Trabajo implements Serializable {

    private final static String[] generalDescri = {"aaaaaa","bbbbbb","cccccc","dddddd","eeeeee","ffffff","gggggg","hhhhhh","iiiiii","jjjjjj","kkkkkk","llllll"};
    private final static int[] generalOrden = {1,2,3,4,5,6,7,8,9,10,11,12};
    private final static String[] iDescri = {"Pruebas escritas","Pruebas prácticas","Actividades en clase","Entrega de trabajos","Realización de tareas en Casa","Cumplimiento de las normas","Respeto a profesorado y compañeros/as","Asistencia y puntualidad","Otros 1","Otros 2","Otros 3","Otros 4"};

    
    private String clave; 
    private String descri;
    private String eval;
    private String title;
    private int peso;
 
    public Trabajo() {
    }

    public Trabajo(String descri, String eval, String clave, String title, int peso) {
       if(validaDescri(descri)) {
         this.descri=descri;
       }
       else {
         this.descri=obtenerCodigoItem(descri);
       }
      /* 
       String ev=eval;
       if(eval.equals("Primera")) {
         ev="0";
       }
       if(eval.equals("Segunda")) {
         ev="1";
       }
       if(eval.equals("Tercera")) {
         ev="2";
       }

      // this.eval=eval;
       this.eval=ev;
      */
       this.eval=eval;
       this.clave=clave;
       this.title=title;
       this.peso=peso;
    }

    private String obtenerCodigoItem(String descri) {
       int i=0;
       if(descri==null) return generalDescri[0];
       for(String st : iDescri) {
            if(st.equals(descri)) {
              return generalDescri[i];
            }
            i++;
       }
       return descri;
    }

    private boolean validaDescri(String descri) {
       for(String st : generalDescri) {
          if(st.equals(descri))
            return true;
       }
       return false;
    }

    public String getClave() {
        return clave;
    }

    public String getTitle() {
        return title;
    }
 
    public String getEval() {
        return eval;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setEval(String eval) {
    /*   String ev=eval;
       if(eval.equals("Primera")) {
         ev="0";
       }
       if(eval.equals("Segunda")) {
         ev="1";
       }
       if(eval.equals("Tercera")) {
         ev="2";
       }
       this.eval = ev;
      */
       this.eval=eval;
    }

    public void setTitle(String title) {
        this.title = title;
    }
 
    public void setDescri(String descri) {
        if(validaDescri(descri)) {
         this.descri=descri;
       }
       else {
         this.descri=obtenerCodigoItem(descri);
       }
    }

    public String getDescri() {
        return descri;
    }

    public int getPeso() {
        return peso;
    }
 
    public void setPeso(int peso) {
        this.peso = peso;
    }
 
 
    public boolean equals(Object obj) {
        if(!(obj instanceof Trabajo))
            return false;
         
        Trabajo trabajo = (Trabajo) obj;
        boolean bo =  (trabajo.getTitle() != null && trabajo.getTitle().equals(title)) && (trabajo.getPeso() >= 0 && trabajo.getPeso() == peso) && (trabajo.getEval().equals(eval)) && (trabajo.getDescri().equals(descri));
        if(bo) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Duplicado. Este trabajo ya ha sido agregado",null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return bo;
    }

    public String reset() {
       peso=5;
       title="";
       descri="";
       eval="";
       return null;
    }
}
