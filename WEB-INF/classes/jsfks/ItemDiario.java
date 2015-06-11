package jsfks;
import javax.faces.event.ActionEvent; 
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException; 

public class ItemDiario implements Serializable {
    private Date fecha;
    private String texto; 
 
    public ItemDiario() {
    }

    public ItemDiario(String fecha, String texto) {
       //this.fecha=new Date();
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
       Date date1=new Date();
       try {
          date1 = formatter.parse(fecha);
       } catch (ParseException e) {
            e.printStackTrace();
       }
       this.fecha=date1;
       this.texto=texto;
    }

    public String getFechad() {
       if(this.fecha==null) {
         return "";
       }
       else {
         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
         return formatter.format(this.fecha);
       }
    }
    public Date getFecha() {
        return this.fecha;
    }

    public String getTexto() {
        return texto;
    }
 
    public void setFecha(Date fecha) {
    //    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.fecha = fecha;
    }

    public void setTexto(String texto) {
       this.texto = texto;
    }
}
