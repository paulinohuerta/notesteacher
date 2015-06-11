package jsfks;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemEva implements Serializable{
        private int percent;
        private String descri="";  
        private List<Valor> valores;
        private Double media;  
        private int sumpeso;  
        private Double mediaItem;
        boolean valida=false; 

        public ItemEva(String descri, int percent, List<Valor> valores)   {
          this.valores = new ArrayList<Valor>();
          this.descri = descri;
          this.percent = percent;
          this.valores = valores;
          this.sumpeso = 0;
          this.media = 0.0;
          this.mediaItem = 0.0;
          this.valida=validaValores();
          if(valida) {
            obtenerMedia();
          }    
          else {
            media=0.0;
          }
          if(Double.isNaN(media)) {
           media=0.0;
          }
        }

        public void setValida(boolean valida) {
             //if(valida) {}
             //this.valida=validaValores();
             this.valida=valida;
        }

        public boolean getValida() {
            return this.valida;
        }

        public void setSumpeso(int sumpeso) {
            this.sumpeso=sumpeso;
        }

        public int getSumpeso() {
            return this.sumpeso;
        }

        public String getDescri() {
	       return descri;
        }

        public void setDescri(String descri) {
		this.descri = descri;
        }

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public List<Valor> getValores() {
		return valores;
	}

	public void setValores(List<Valor> valores) {
		this.valores = valores;
                this.valida=validaValores();
	}

	public String getMediaItemfmt() {
              if(mediaItem==0.0) {
                 return "";
              }
              return String.valueOf(mediaItem);
        }

	public void setMediaItem(Double mediaItem) {
              this.mediaItem=mediaItem;
        }

	public Double getMediaItem() {
		return mediaItem;
        }

	public String getMediafmt() {
              
              if(media==0.0) {
                 return "";
              }
              double nota= Math.round(media * 1000);
              nota = nota / 1000;
              return String.valueOf(nota);
        }
	
        public Double getMedia() {
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

        public void obtenerMedia() {
            Double sum=0.0;
            int sumpeso=0;
            int n=0;
            if(validaValores()) {
                for(Valor v : valores) {
                  Double vn = v.getNota(); 
                  int pe = v.getTrabajo().getPeso();
                  sum+=vn*pe/100;
                  sumpeso+=pe;
                  n++; 
                }
                //this.media=sum/n;
                this.media=sum;
                this.sumpeso=sumpeso;
                //this.mediaItem=(this.media * percent) / 100;
                  // Para tres decimales
                double nota=(this.media * percent) / 100;
                nota = Math.round(nota * 1000);
                this.mediaItem = nota / 1000;
             }
        }

        private boolean validaValores() {
                int sumPeso=0;
                for(Valor v : valores) {
                  if(!(v.getTrabajo().getPeso() >=0 && v.getTrabajo().getPeso() <= 100)) {
                    return false;
                  }
                  sumPeso+=v.getTrabajo().getPeso();
                }
                if(sumPeso > 100) {
                    return false;
                }
                return true;
        }
    
        @Override
        public String toString() {
           return "ItemEva{" + "descri=" + descri + ", percent=" + percent + ", valores= muchos, media=" + media + '}';
        }
}

