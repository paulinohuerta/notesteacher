package jsfks;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

 
public class Notas implements Serializable {

    private String clave; 
    private String title; 
    private String file;
    private String author;
    private String texto;
    private Date fecha;
 
    public Notas() {
    }

    public Notas(String clave, String title, String file, String author, String texto, Date fecha) {
       this.clave=clave;
       this.title=title;
       this.file=file;
       this.author=author;
       this.texto=texto;
       this.fecha=fecha;
    }

    public String getFile() {
       return this.file;
    }

    public String getAuthor() {
       return this.author;
    }
    public String getClave() {
        return this.clave;
    }

    public String getTitle() {
        return title;
    }
 
    public Date getFecha() {
        return fecha;
    }

    public void setFile(String file) {
        this.file = file;
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
         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
         return formatter.format(this.fecha);
       }
    }

    public void setFecha(Date fecha) {
       this.fecha = fecha;
    }

    public void setTitle(String title) {
        this.title = title;
    }
 
    public void setTexto(String texto) {
         this.texto=texto;
    }

    public String getTexto() {
        return texto;
    }
}
