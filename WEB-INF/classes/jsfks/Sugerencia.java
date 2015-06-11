package jsfks;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

 
public class Sugerencia implements Serializable {

    private String clave; 
    private String descri;
    private Date fecha;
    private String tipo;
    private String author;
 
    public Sugerencia() {
    }

    public Sugerencia(String clave, String descri, Date fecha, String tipo, String author) {
       this.clave=clave;
       this.descri=descri;
       this.fecha=fecha;
       this.author=author;
       this.tipo=tipo;
    }

    public String getAuthor() {
       return this.author;
    }
    public String getClave() {
        return this.clave;
    }

    public String getTipo() {
        return tipo;
    }
 
    public Date getFecha() {
        return fecha;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public String getFechad() {
       if(this.fecha==null) {
         return "";
       }
       else {
         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:H:mm");
         return formatter.format(this.fecha);
       }
    }

    public void setFecha(Date fecha) {
       this.fecha = fecha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
 
    public void setDescri(String descri) {
         this.descri=descri;
    }

    public String getDescri() {
        return descri;
    }
}
