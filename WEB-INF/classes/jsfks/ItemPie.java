package jsfks;
import java.io.Serializable;
import java.util.UUID;

public class ItemPie implements Serializable{
        private int percent;
        private Double media;  
        private Double sobre;  
        private String valida;  

        public ItemPie()   {
          percent=0;
          media=0.0;
          sobre=0.0;
          valida="";
        }

        public ItemPie(int percent, Double media)   {
          this.percent = percent;
          this.media = media;
          this.sobre=0.0;
          this.valida="";
        }

        public Double getSobre() {
          return this.sobre;
        }

        public void setSobre(Double sobre) {
          this.sobre=sobre;
        }
        public void setValida(String valida) {
          this.valida=valida;
        }
        public String getValida() {
          return valida;
        }

        public void setMedia(Double val) {
          this.media=val;
        }

        public Double getMedia() {
            return this.media;
        }

	public int getPercent() {
            return percent;
	}

	public void setPercent(int percent) {
	    this.percent = percent;
	}

	public String getMediafmt() {
              if(media==0.0) {
                 return "";
              }
              return String.valueOf(media);
        }
}

