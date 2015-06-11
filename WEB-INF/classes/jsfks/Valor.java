package jsfks;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Valor implements Serializable{
        private Trabajo trabajo;  
        private Double nota;  
 
        public Valor(Trabajo trabajo, Double nota) {
          this.trabajo = trabajo;
          this.nota = nota;
        }

	public void setTrabajo(Trabajo trabajo) {
          this.trabajo=trabajo;
        }

	public Trabajo getTrabajo() {
          return trabajo; 
        }

	public String getNotafmt() {
            if(nota==0.0) {
               return "";
            }
            return String.valueOf(nota);
        }
	
        public Double getNota() {
		return nota;
        }

	public void setNota(Double nota) {
            if(nota >= 0.0 && nota <= 10.0) {
                this.nota = nota;
            }
	}
    
}

