package jsfks;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Collections;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams; 
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
 
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean(name = "alum_cua_serv")
@SessionScoped
public class AlumnoCuadernoService implements Serializable {
     
    private final static String[] tipoFaq = {"problema","alta","sugerencia"};
    private final static String[] admNotas = {"crear una nueva","editar una existentente","ver todas"};
    private final static String[] generalDescri = {"aaaaaa","bbbbbb","cccccc","dddddd","eeeeee","ffffff","gggggg","hhhhhh","iiiiii","jjjjjj","kkkkkk","llllll"};
    private final static int[] generalOrden = {1,2,3,4,5,6,7,8,9,10,11,12};
    private final static String[] iDescri = {"Pruebas escritas","Pruebas prácticas","Actividades en clase","Entrega de trabajos","Realización de tareas en Casa","Cumplimiento de las normas","Respeto a profesorado y compañeros/as","Asistencia y puntualidad","Otros 1","Otros 2","Otros 3","Otros 4"};
    private final static String[] generalEval = { "Primera","Segunda","Tercera" };
    private final static int[] percent;
    private String[] descri = new String[10];  
    private List<Valor> valores = new ArrayList<Valor>();
    private Double[] media = new Double[10];
    private List<Trabajo> updateTrabajos;
    private List<ItemInstrumento> items;
    private Jedis conn;
    
    private String[] nombre  = new String[5];

    static {

        percent = new int[29];
        percent[0] = 0;
        percent[1] = 1;
        percent[2] = 2;
        percent[3] = 3;
        percent[4] = 4;
        percent[5] = 5;
        percent[6] = 6;
        percent[7] = 7;
        percent[8] = 8;
        percent[9] = 9;
        percent[10] = 10;
        percent[11] = 15;
        percent[12] = 20;
        percent[13] = 25;
        percent[14] = 30;
        percent[15] = 35;
        percent[16] = 40;
        percent[17] = 45;
        percent[18] = 50;
        percent[19] = 55;
        percent[20] = 60;
        percent[21] = 65;
        percent[22] = 70;
        percent[23] = 75;
        percent[24] = 80;
        percent[25] = 85;
        percent[26] = 90;
        percent[27] = 95;
        percent[28] = 100;
    }
     
    public AlumnoCuadernoService() {
        conn = new Jedis("localhost");
        conn.select(11);
        int i;
        items=new ArrayList<ItemInstrumento>();
        for(i=0; i < generalDescri.length; i++) {
            if(i<=7) {
              items.add(new ItemInstrumento(generalDescri[i],i+1,iDescri[i],5));
            }
            else {
              items.add(new ItemInstrumento(generalDescri[i],i+1,iDescri[i],0));
            }
        } 
    }
    public List<Sugerencia> cargarSugerencia() {
        List<String> list=new ArrayList<String>();
        Set<String> claves=new HashSet<String>();
        claves=conn.hkeys("cuaderno:faq:preguntas");
        for(String st : claves) {
          if(!conn.sismember("cuaderno:faqrespuestas",st)) {
            list.add(st);
            list.add(conn.hget("cuaderno:faq:preguntas",st));
          }
        }
        List<Sugerencia> lsugerencia= new ArrayList<Sugerencia>();
        lsugerencia=getListSugerencia(list);
        return lsugerencia;
    }
    public List<Sugerencia> cargarSugerenciaHist(String profe) {
        List<String> list=new ArrayList<String>();
        Set<String> claves=new HashSet<String>();
        claves=conn.hkeys("cuaderno:faq:preguntas");
        for(String st : claves) {
           list.add(st);
           list.add(conn.hget("cuaderno:faq:preguntas",st));
        }
        List<Sugerencia> lsugerencia= new ArrayList<Sugerencia>();
        lsugerencia=getListSugerenciaUsuario(profe,list);
        return lsugerencia;
    }
    private List<Sugerencia> getListSugerencia(List<String> list) {
        List<Sugerencia> lisSug = new ArrayList<Sugerencia>();
        int i,index_field1,index_field2,index_fecha;
        String clave,valor;
        Date date1 = new Date();
        for(i=0;i<list.size();i=i+2) {
           clave=list.get(i);
           valor=list.get(i+1);
           index_field1=clave.indexOf(":",0);
           index_field2=clave.indexOf(":",index_field1+1);
           index_fecha=valor.indexOf(" ",0);
           String f1=clave.substring(0,index_field1);
           String f2=clave.substring(index_field1+1,index_field2);
           String f3=clave.substring(index_field2+1);
           String d1=valor.substring(0,index_fecha);
           SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:H:mm");
           try {
              date1 = formatter.parse(d1);
           } catch (ParseException e) {
                e.printStackTrace();
           }
           Sugerencia unaSugerencia=new Sugerencia(clave,valor.substring(index_fecha+1),date1,f2,f3);
           lisSug.add(unaSugerencia);
        }
        return lisSug;
     }

    private List<Sugerencia> getListSugerenciaUsuario(String profe, List<String> list) {
        List<Sugerencia> lisSug = new ArrayList<Sugerencia>();
        int i,index_field1,index_field2,index_fecha;
        String clave,valor;
        Date date1 = new Date();
        for(i=0;i<list.size();i=i+2) {
           clave=list.get(i);
           index_field1=clave.indexOf(":",0);
           index_field2=clave.indexOf(":",index_field1+1);
           String f1=clave.substring(0,index_field1);
           String f2=clave.substring(index_field1+1,index_field2);
           String f3=clave.substring(index_field2+1);
           if(!f3.equals(profe)) {
            continue;
           }
           valor=list.get(i+1);
           index_fecha=valor.indexOf(" ",0);
           String d1=valor.substring(0,index_fecha);
           SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:H:mm");
           try {
                date1 = formatter.parse(d1);
           } catch (ParseException e) {
                e.printStackTrace();
           }
           Sugerencia unaSugerencia=new Sugerencia(clave,valor.substring(index_fecha+1),date1,f2,f3);
           lisSug.add(unaSugerencia);
        }
        return lisSug;
     }
    public void grabarFaq(String profe, Sugerencia unafaq) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:H:mm");
        String fecha=formatter.format(unafaq.getFecha());
        Long key=conn.incr("cuaderno:faq");
        String keySt=Long.toString(key);       
        conn.hset("cuaderno:faq:preguntas",keySt + ":" + unafaq.getTipo() + ":" + profe,fecha + " " + unafaq.getDescri());
    }

    public void grabarItemDiario(String profe, String gr, ItemDiario item, boolean editado, ItemDiario itemEdit) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fecha=formatter.format(item.getFecha()); 
        //String fechaEdit=formatter.format(itemEdit.getFecha()); 
        boolean modificado=false;
        boolean alta=false;
        List<String> list=new ArrayList<String>();
        if(editado) {
          list=conn.lrange("cuaderno:profe:" + profe + ":" + gr + ":diario:nombres",0, -1);
          for(String key : list){
            String f1=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":diario:" + key, "fecha");
            if(f1.equals(fecha)) {
              conn.hset("cuaderno:profe:" + profe + ":" + gr + ":diario:" + key, "texto",item.getTexto());
              modificado=true;
              break;
            }
          }
          if(!modificado) {
            // cambiar de fecha al item (dar de baja y producir alta)
            String fechaEdit=formatter.format(itemEdit.getFecha()); 
            for(String key : list){
               String f1=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":diario:" + key, "fecha");
               if(f1.equals(fechaEdit)) {
                 // se elimina el item
                 conn.srem("cuaderno:profe:" + profe + ":" + gr + ":diario:sfechas",f1);
                 conn.del("cuaderno:profe:" + profe + ":" + gr + ":diario:" + key);
                 long delete=conn.lrem("cuaderno:profe:" + profe + ":" + gr + ":diario:nombres",1,key);
                 if(delete==1) {
                   alta=true;
                   break;
                 }
               }
            }
           }
          }
          if(alta || !editado) {
        conn.sadd("cuaderno:profe:" + profe + ":" + gr + ":diario:sfechas",fecha);
        Long key=conn.incr("cuaderno:profe:" + profe + ":" + gr + ":diario");
        String keySt=Long.toString(key);
        conn.rpush("cuaderno:profe:" + profe + ":" + gr + ":diario:nombres", keySt);
        conn.hset("cuaderno:profe:" + profe + ":" + gr + ":diario:" + key, "fecha",fecha);
        conn.hset("cuaderno:profe:" + profe + ":" + gr + ":diario:" + key, "texto",item.getTexto());
        }
    }
    
    public Diario cargarDiario(String profe,String gr) {
        Diario diario = new Diario();
        if(conn.exists("cuaderno:profe:" + profe + ":" + gr + ":diario")) {
           List<String> listItemDiario=conn.lrange("cuaderno:profe:" + profe + ":" + gr + ":diario:nombres", 0, -1);
           for(String st : listItemDiario) {
             String texto = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":diario:" + st , "texto");
             String fech1  = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":diario:" + st , "fecha");
             ItemDiario unItem = new ItemDiario(fech1,texto);
             diario.addItem(unItem);
	   }
        }
        return diario;
    }
    public ScheduleView  cargarAgenda(String profe,String gr) {
        ScheduleView agenda = new ScheduleView();
        if(conn.exists("cuaderno:profe:" + profe + ":" + gr + ":agenda")) {
           List<String> listItemAgenda=conn.lrange("cuaderno:profe:" + profe + ":" + gr + ":agenda:nombres", 0, -1);
           for(String st : listItemAgenda) {
             String nombre = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + st , "nombre");
             String fech1  = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + st , "fecha");
             agenda.setDatos(nombre,fech1);
	   }
        }
        return agenda;
    }

    private void  cargarItems(String profe,String gr,List<String> itemsPersis) {
        int n=0;
        int i;
        String unPercent, laDescri;
        items=new ArrayList<ItemInstrumento>();
        if(itemsPersis!=null) {
          for(String st : itemsPersis) {
            i=getUnaDescriItem(st);
            String descriPersis=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":items:mombre",st);
            if(descriPersis==null) {
              laDescri=iDescri[i];
            }
            else {
              laDescri=descriPersis;
            }
            unPercent=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":items",st); 
            items.add(new ItemInstrumento(st,++n,laDescri,Integer.parseInt(unPercent)));
          }
        }
        else {
          for(i=0; i < generalDescri.length; i++) {
            if(i<=7) {
              items.add(new ItemInstrumento(generalDescri[i],i+1,iDescri[i],5));
            }
            else {
              items.add(new ItemInstrumento(generalDescri[i],i+1,iDescri[i],0));
            }
          }
        }
    }

    public void grabarAgenda(String profe, String gr, ScheduleView agenda) {
      //Date date = new Date(); 
      List<String> list=new ArrayList<String>();
      SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
      List<String> listItemAgenda=conn.lrange("cuaderno:profe:" + profe + ":" + gr + ":agenda:nombres", 0, -1);
      for(String st : listItemAgenda) {
         //Date fecha=new Date();
         String nombre = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + st , "nombre");
         String fech1  = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + st , "fecha");
     //        agenda.setDatos(nombre,fech1);
         boolean existe=false;
         
         for(ScheduleEvent ev : agenda.getEventModel().getEvents()) {
            String fecha = sdf.format(ev.getStartDate());
            String title = ev.getTitle();
            if(fecha.equals(fech1) && title.equals(nombre)) {
               existe=true;
               list.add(ev.getId());
            }
         }
         if(!existe) {
           // baja de la BD
             long cuantos=conn.lrem("cuaderno:profe:" + profe + ":" + gr + ":agenda:nombres",1,st);
             if(cuantos==1) {
               cuantos=conn.del("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + st);
             }
         }
       }
       for(ScheduleEvent ev : agenda.getEventModel().getEvents()) {
           if(list.contains(ev.getId())) {
              continue;
           }
           else {
          // alta en base de datos
            String f1 = sdf.format(ev.getStartDate());
            Long key=conn.incr("cuaderno:profe:" + profe + ":" + gr + ":agenda");
            String keySt=Long.toString(key); 
            conn.rpush("cuaderno:profe:" + profe + ":" + gr + ":agenda:nombres", keySt);

            conn.hset("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + key, "nombre",ev.getTitle());
            conn.hset("cuaderno:profe:" + profe + ":" + gr + ":agenda:" + key, "fecha", f1);
           }
       }
  }

    private int getUnaDescriItem(String name) {
        int i;
        for(i=0; i < generalDescri.length; i++) {
            if(name.equals(generalDescri[i])) {
               return i;
            }
        }
        return -1;
    }

    public void updatingTrabajos(String profe, String gr, String ev) {
    }
    
    public void createTrabajos(String profe, String gr, List<Trabajo> trabajos) {
       for(Trabajo t : trabajos) {
         //conn.rpush("cuaderno:profe:" + profe + ":" + gr + ":" + t.getDescri() + ":nombres:" + t.getEval(), t.getClave());
         Long key=conn.incr("cuaderno:profe:" + profe + ":" + gr + ":" + t.getDescri() + ":valores:" + gettingEval(t.getEval()));
         String keySt=Long.toString(key); 
         t.setClave(keySt); 
         conn.rpush("cuaderno:profe:" + profe + ":" + gr + ":" + t.getDescri() + ":nombres:" + gettingEval(t.getEval()), t.getClave());
	 //conn.hset("cuaderno:profe:" + profe + ":" + gr + ":" + t.getDescri() + ":nombre:" + t.getEval() + ":" + t.getTitle(), "peso", Integer.toString(t.getPeso()));
	 conn.hset("cuaderno:profe:" + profe + ":" + gr + ":" + t.getDescri() + ":nombres:" + gettingEval(t.getEval()) + ":" + t.getClave(), "peso", Integer.toString(t.getPeso()));
	 conn.hset("cuaderno:profe:" + profe + ":" + gr + ":" + t.getDescri() + ":nombres:" + gettingEval(t.getEval()) + ":" + t.getClave(), "nombre", t.getTitle());
       }
    }

    public void setItems(List<ItemInstrumento> it) {
        this.items=it;
    }
    
    public List<ItemInstrumento> getItems() {
        return this.items;
    }

    public void updatingT1(String profe, String gr, String eval, String descri, String t, int peso, String nombre) {
	   String peso_antes_string = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":" + descri + ":nombres:" + gettingEval(eval) + ":" + t, "peso");
           int  peso_antes_int = Integer.parseInt(peso_antes_string);
           int  peso_actual_int = peso;
           String peso_actual=Integer.toString(peso_actual_int);
           
           if(peso_antes_int!=peso_actual_int) {
	     conn.hset("cuaderno:profe:" + profe + ":" + gr + ":" + descri + ":nombres:" + gettingEval(eval) + ":" + t, "peso",peso_actual);
           }
            // procedemos con cambio de nombre
	   String nombre_antes = conn.hget("cuaderno:profe:" + profe + ":" + gr + ":" + descri + ":nombres:" + gettingEval(eval) + ":" + t, "nombre");
           String nombre_actual = nombre;
           if(!nombre_antes.equals(nombre_actual)) {
	     conn.hset("cuaderno:profe:" + profe + ":" + gr + ":" + descri + ":nombres:" + gettingEval(eval) + ":" + t, "nombre",nombre_actual);
           }

    }

    public boolean validaTotalPercentItems() {
        int sum=0;
        for(ItemInstrumento itv : items) {
           sum+=itv.getPeso();
        }
        if(sum > 100) {
          return false;
        }
        return true;
    }

    public boolean validaAltaDiarioFecha(String profe,String gr,String st) {
        return !conn.sismember("cuaderno:profe:" + profe + ":" + gr + ":diario:sfechas", st);
    }

    public boolean validaItemsOrden() {
        HashSet<Integer> set = new HashSet<Integer>();
        for(ItemInstrumento itv : items) {
          if(set.contains(itv.getOrden())) return false;
          set.add(itv.getOrden());
        }
        return true;
    }

    public void updateItems(String profe,String gr,String ev) {
        ItemInstrumento itv = new ItemInstrumento(); 
        // comprobar si hay un cambio repecto a la BD ******
          // ¿cambio en la description?
        for(int j = 0 ; j < items.size() ; j++) {
          itv=getUnItem(j+1);
          String sc=itv.getCodigo();
          String sd=itv.getDescri();
          if(obtenerCodigoItem(sd) == null) {
           if(sc.equals("iiiiii")||sc.equals("jjjjjj")||sc.equals("kkkkkk")||sc.equals("llllll")){
              conn.hset("cuaderno:profe:" + profe + ":" + gr + ":items:mombre",sc,sd);
           }
          }
        }
        conn.del("cuaderno:profe:" + profe + ":" + gr + ":items:orden");
        for(int j = 0 ; j < items.size() ; j++) {
           itv=getUnItem(j+1);
           conn.rpush("cuaderno:profe:" + profe + ":" + gr + ":items:orden",itv.getCodigo());
           conn.hset("cuaderno:profe:" + profe + ":" + gr + ":items",itv.getCodigo(),Integer.toString(itv.getPeso()));
        }
    }

    public List<AlumnoCuaderno> createAluGru(String profe,String gr,String ev) {
           
        updateTrabajos=new ArrayList<Trabajo>();
        updateTrabajos.clear(); 

        List<String> alumnos;
        alumnos=new ArrayList<String>();
        //alumnos = conn.lrange("cuaderno:grupo:" + gr, 0, -1);
   alumnos = conn.sort("cuaderno:grupo:" + gr, new SortingParams().alpha());

        List<AlumnoCuaderno> list = new ArrayList<AlumnoCuaderno>();
        if(gr==null) {
          return list;
        }

        List<String> itemsPersis=null;
        if(conn.exists("cuaderno:profe:" + profe + ":" + gr + ":items:orden")) {
          itemsPersis=new ArrayList<String>();
          itemsPersis=conn.lrange("cuaderno:profe:" + profe + ":" + gr + ":items:orden", 0, -1); 
        }
        cargarItems(profe,gr,itemsPersis);
 
        ItemInstrumento itv = new ItemInstrumento(); 
        List<String> lostrabajos = new ArrayList<String>();
        for(int i = 0 ; i < alumnos.size() ; i++) {
           List<ItemEva> lIEva = new ArrayList<ItemEva>();
           for(int j = 0 ; j < items.size() ; j++) {
             itv=getUnItem(j+1);
             if(itv.getPeso()==0) continue;
             lostrabajos=conn.lrange("cuaderno:profe:" + profe + ":" + gr + ":" + itv.getCodigo() + ":nombres:" + gettingEval(ev), 0, -1);
             if(lostrabajos!=null) {
               List<Valor>  arr = new ArrayList<Valor>();
               for(String untra : lostrabajos) {
                 String pesotra=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":" + itv.getCodigo() + ":nombres:" + gettingEval(ev) + ":" + untra,"peso");
                 if(pesotra.equals("0") && i!=0) continue;
                 if(pesotra.equals("0") && i==0) {
                    String nombretra=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":" + itv.getCodigo() + ":nombres:" + gettingEval(ev) + ":" + untra,"nombre");
                    Trabajo t = new Trabajo(itv.getCodigo(),ev,untra,nombretra,Integer.parseInt(pesotra));
                    updateTrabajos.add(t);
                    continue;
                 }
                 String nombretra=conn.hget("cuaderno:profe:" + profe + ":" + gr + ":" + itv.getCodigo() + ":nombres:" + gettingEval(ev) + ":" + untra,"nombre");
                 String notatra=conn.get("cuaderno:profe:" + profe + ":" + gr + ":" + itv.getCodigo() + ":" + gettingEval(ev) + ":" + alumnos.get(i) + ":" + untra);
                 if(notatra==null) {
                   notatra="0.0";                   
                 }
                 Trabajo t = new Trabajo(itv.getCodigo(),ev,untra,nombretra,Integer.parseInt(pesotra));
                 if(i==0) {
                   updateTrabajos.add(t);
                 }
                 
                 // obtener valores
                 Valor a1 = new Valor(t,Double.parseDouble(notatra));
                 arr.add(a1);
               }
               lIEva.add(new ItemEva(itv.getCodigo(),itv.getPeso(),arr));
               //lIEva.add(new ItemEva(generalDescri[j],items.get(j).getPeso(),arr));
             }
             else { // lostrabajos nulos
               itv=items.get(j);
               lIEva.add(new ItemEva(itv.getCodigo(),itv.getPeso(),null));
               //lIEva.add(new ItemEva(generalDescri[j],items.get(j).getPeso(),null));
             }
           }
           List<Eva> listEva = new ArrayList<Eva>();
           listEva.add(new Eva(lIEva));
          
           list.add(new AlumnoCuaderno(alumnos.get(i),listEva));
        }
        return list;
    }

    public void editandoTable(String profe,String gr,String descri,String eval,String nombre_alu, List<Valor> lv) {
        for(Valor valor : lv) {
             //cuaderno:profe:antonio:SMR2:aaaaaa:0:Enrique:
             conn.set("cuaderno:profe:" + profe + ":" + gr + ":" + descri + ":" + gettingEval(eval) + ":" + nombre_alu + ":" + valor.getTrabajo().getClave() , Double.toString(valor.getNota()));
        }
    }

    private ItemInstrumento getUnItem(int j) {
        for(ItemInstrumento itv : items) {
          if(itv.getOrden()==j) {
            return itv;
          }
        }
        return null;
    }

    public void setUpdateTrabajos(List<Trabajo> ut) {
        this.updateTrabajos = ut;
    }

    public List<Trabajo> getUpdateTrabajos() {
        return this.updateTrabajos;
    }

    public String[] getTipoFaq(){
        return tipoFaq;
    }
    public String[] getIDescri(){
        return iDescri;
    }

    public String[] getGeneralEval(){
        return generalEval;
    }

    public String[] getGeneralDescri(){
        return generalDescri;
    }
    
    public int[] getGeneralOrden(){
        return generalOrden;
    }

    public int[] getPercent(){
        return percent;
    }

    public String obtenerCodigoItem(String descriItem) {
       int i=0;
       for(String st : iDescri) {
            if(st.equals(descriItem)) {
              return generalDescri[i];
            }
            i++;
       }
       return null;
    }

    public String obtenerDescriItem(String codigoItem) {
       int i=0;
       for(String st : generalDescri) {
            if(st.equals(codigoItem)) {
              return iDescri[i];
            }
            i++;
       }
       return null;
    }
    
    public boolean existeGrupo(String gr) {
      return conn.exists("cuaderno:grupo:" + gr);
    }
    public void altaGrupoProfe(List<String> grupos,String pr) {
      for(String st : grupos){
        conn.lrem("cuaderno:profe:" + pr, 0, st);
        conn.rpush("cuaderno:profe:" + pr, st);
      }
    }
    public void altaGrupo(List<String> alumnos,String gr) {
      conn.sadd("cuaderno:grupos", gr);
      java.util.Collections.sort(alumnos);
      for(String st : alumnos){
        conn.rpush("cuaderno:grupo:" + gr, st);
      }
    }
    public List<Profe> listprofe() {
      List<Profe> list = new ArrayList<Profe>();
      for(String pr : conn.smembers("cuaderno:profes")) {
        list.add(new Profe(pr,pr));
      }
      return list;
    }
    public List<Grupo> listgrup() {
      List<Grupo> list = new ArrayList<Grupo>();
      for(String gr : conn.smembers("cuaderno:grupos")) {
        list.add(new Grupo(gr,gr));
      }
      return list;
    }
    public List<String> profeGrupo(String st) {
      List<String> list = new ArrayList<String>();
      list=conn.lrange("cuaderno:profe:" + st,0, -1);
      return list;
    }
    public List<String> grupoAlumno(String st) {
      List<String> list = new ArrayList<String>();
      list=conn.lrange("cuaderno:grupo:" + st,0, -1);
      return list;
    }
    public List<Sugerencia> cargarHistSuger(String profe) {
      List<Sugerencia> list = new ArrayList<Sugerencia>();
      list=cargarSugerenciaHist(profe);
      return list;
    } 
    public void grabarRespFaq(Sugerencia resp) {
      String key = resp.getClave();
      conn.sadd("cuaderno:faqrespuestas", key);
      conn.hset("cuaderno:faq:preguntas",key,resp.getDescri());
    }
    public boolean respondidaFaq(String key) {
      return conn.sismember("cuaderno:faqrespuestas", key);
    }
    public void altaUnAlumno(String grupo, String alum) {
      conn.rpush("cuaderno:grupo:" + grupo, alum);
    }
    public void altaUnProfe(String st) {
      conn.sadd("cuaderno:profes", st);
    }
    public void altaUnGrupo(String st) {
      conn.sadd("cuaderno:grupos", st);
    }
    public List<String> getAdmNotasTitles(String user) {
      List<String> admNotasTitles = new ArrayList<String>();
      for(String st : conn.smembers("cuaderno:profe:" + user + ":notas")) {
        admNotasTitles.add(st);
      }
      return admNotasTitles;
    }
    public String[] getAdmNotas() {
      return admNotas;
    }
    public void grabarTextoNota(String profe,String nameNota, String eltexto) {
      conn.sadd("cuaderno:profe:" + profe + ":notas", nameNota);
      conn.hset("cuaderno:profe:" + profe + ":notas:" + nameNota,"texto",eltexto);
    }
    public String textoNota(String profe,String nameNota) {
      String texto=conn.hget("cuaderno:profe:" + profe + ":notas:" + nameNota,"texto");
      return texto;
    }
    public String gettingEval(String eval) {
      if(eval.equals("Primera")) {
        return "0";
      }
      if(eval.equals("Segunda")) {
        return "1";
      }
      if(eval.equals("Tercera")) {
        return "2";
      }
      return null;
    }
}
