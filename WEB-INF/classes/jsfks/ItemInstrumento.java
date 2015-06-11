package jsfks;
import java.io.Serializable;
import java.util.UUID;

public class ItemInstrumento implements Serializable{
        private String codigo;
        private int orden;
        private String descri;
        private int peso;  

        public ItemInstrumento() {
        }

        public ItemInstrumento(String codigo, int orden, String descri, int peso)   {
          this.codigo = codigo;
          this.orden = orden;
          this.descri = descri;
          this.peso = peso;
        }

        public void setCodigo(String codigo) {
	    this.codigo = codigo;
        }

        public void setDescri(String descri) {
	    this.descri = descri;
        }

        public void setOrden(int orden) {
	    this.orden = orden;
        }

        public String getCodigo() {
            return this.codigo;
        }

        public int getOrden() {
            return this.orden;
        }

        public String getDescri() {
            return this.descri;
        }

	public int getPeso() {
            return peso;
	}

	public void setPeso(int peso) {
	    this.peso = peso;
	}
}
