package jsfks;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class AlumnoCuaderno implements Serializable {

	private Alumno alu;
        private List<Eva> listEva;
        private ItemPie pie;
        private String observaciones="";

        public AlumnoCuaderno() {
	  this.alu = new Alumno();
	  this.listEva = new ArrayList<Eva>();
	  this.pie = new ItemPie();
        }

        public AlumnoCuaderno(String nombre, List<Eva> listEva) {
	  alu=new Alumno(nombre);
	  this.listEva = new ArrayList<Eva>();
          this.listEva=listEva;
	  this.pie = new ItemPie();
          obtenerPie();
        }

        public void obtenerPie() {
           // suma las medias de los items 
           // suma los porcentajes de los items 
          double sumM=0.0;
          int sumMenos=0;
          int sumMenosX=0;
          int sumP=0;
          int nitem=0;
          double sumaPesoValores;
          double sumX=0.00;
          for(ItemEva ie : listEva.get(0).getListItem()) {
            sumaPesoValores = 0.0;
            sumM+=ie.getMediaItem();
            sumP+=ie.getPercent();

            if(ie.getMediaItem()==0.0) {
              sumX+=0.0;
            }
            else {
             // sumX+=(ie.getPercent()*ie.getSumpeso())/100;
                // ver por nota (clase Valor), las notas > 0.0 interesa sumar su peso
              for(Valor val : ie.getValores() ) {
                if(val.getNota() > 0.0) {
                  sumaPesoValores+= val.getTrabajo().getPeso(); 
                }
              }
              if(sumaPesoValores > 100) {
               nitem++;
              }
              sumX+=(ie.getPercent()*sumaPesoValores)/100;
            }

            if(ie.getMediaItem()==0.0) {
              sumMenos+=ie.getPercent();
            }
          }
          sumM = Math.round(sumM * 100);
          sumM = sumM / 100;
          sumMenos = sumP - sumMenos;
          pie.setMedia(sumM);
          pie.setPercent(sumP);
          sumM=sumMenos/10.00;
          sumX=sumX/10;
          sumX = (double)Math.round(sumX * 100)/100;
          pie.setSobre(sumX);
          if(nitem > 0) {
            pie.setValida(" - No validados (" + nitem + ") items");
          }
        }

	public void setObservaciones(String observaciones) {
           this.observaciones=observaciones;
        }

	public void setPie(ItemPie pie) {
           this.pie=pie;
        }

	public void setAlu(Alumno alu) {
	  this.alu=alu;
	}

	public ItemPie getPie() {
          return this.pie;
        }

	public String getObservaciones() {
	   return this.observaciones;
        }

	public Alumno getAlu() {
	   return alu;
	}

	public void setListEva(List<Eva> listEva) {
	  this.listEva=listEva;
	}

	public List<Eva> getListEva() {
	  return listEva;
	}
        
	public boolean addItemEva(int num_eva, ItemEva ie) {
          Eva nueva_eva = new Eva();
          nueva_eva=listEva.get(num_eva);
          nueva_eva.addItem(ie);
	  listEva.set(num_eva,nueva_eva);
          return true;
        }

	public Eva getPrimera() {
	  return listEva.get(0);
	}
}
