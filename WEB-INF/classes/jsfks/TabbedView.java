package jsfks;

import java.io.Serializable; 
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedProperty;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.context.RequestContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import java.text.SimpleDateFormat;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.event.ColumnResizeEvent;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

@ManagedBean
@ViewScoped
public class TabbedView implements Serializable {
     
     private List<AlumnoCuaderno> alumCua;
     private Trabajo trabajo;
     private List<Trabajo> trabajos;
     private List<Trabajo> tupdating;
     private List<ItemInstrumento> items;
      private List<String> listAluLoad;
      private List<String> listGrLoad;
      private List<String> listAluLoadTarget;
      private List<String> listGrLoadTarget;
      private String stCarga;
      private DualListModel<String> cargaAlum;
      private DualListModel<String> cargaGProfe;
      private String grupoEscogido;
      private List<Grupo> listgrup;
      private List<Profe> listprofe;
      private List<String> listP;
      private List<Sugerencia> listSugerencia;
      private Sugerencia sugerencia;
      private Sugerencia unafaq;
      private String selectedGr;
      private String selectedPr;
      private String selectedPr1;
      private Date unafecha;
      private List<Sugerencia> historicoSugerencia;
      private String nuevoTexto;
      private String newprofe="un profe";
      private String newgrupo="un grupo";
      private String newalumno="un alumno";
      private String backurl;
      private Notas unanota;
      private String actionNotas;
      private List<String> listaNotas;
      private List<String> grupoAlumno;
      private String cargaNota;
     private String option;
     private String eval;
     private ScheduleView agenda;
     private Diario diario;
     private ItemDiario diarioItem;
     private ItemDiario diarioItemEdit;
     private String grupoValidoAgenda="--no grupo válido--";
     private boolean editandoItemDiario=false;
     private boolean cuadernostandard=true;
     private boolean admin=false; 
     private boolean cuadernoadmin=false; 
     private boolean faq=false; 
     private boolean cargagrupoadmin=false; 
     private boolean cargapickadmin=false; 
     private boolean consultaG=false; 
     private boolean modificando=false; 
     private boolean agregando=false; 
     private boolean cerrando=false; 
     private boolean showagenda=false; 

     private TreeNode root;
     private TreeNode selectedNode;

     private String adminprofe;

     private UploadedFile file;

     //@ManagedProperty("#{scheduleView}")
     //private ScheduleView agenda;

     @ManagedProperty("#{alum_cua_serv}")
     private AlumnoCuadernoService service;
     
     @ManagedProperty("#{userLoginView}")
     private UserLoginView user;
     
     @ManagedProperty("#{infoService}")
     private InfoService infoservice;
     
     @PostConstruct
     public void init() {
         alumCua = service.createAluGru(null,null,null);
   //      tupdating = service.getUpdateTrabajos();
         agenda=new ScheduleView();
         diario=new Diario();
         diarioItem=new ItemDiario();
         diarioItemEdit=new ItemDiario();
         tupdating = new ArrayList<Trabajo>();
         items = new ArrayList<ItemInstrumento>();
         items=service.getItems();
         trabajo = new Trabajo();
         trabajos = new ArrayList<Trabajo>();
         if(user.getUsername().equals("jestudios")) {
            this.admin=true;
            root = infoservice.createInfo(user.getLprofes(),user.getProfes());
         }
         listAluLoad = new ArrayList<String>();
         listAluLoadTarget = new ArrayList<String>();
         listGrLoad = new ArrayList<String>();
         listGrLoadTarget = new ArrayList<String>();
         listSugerencia = new ArrayList<Sugerencia>();
         sugerencia = new Sugerencia();
         unafaq = new Sugerencia();
         grupoEscogido="--nombre nuevo grupo--";
         historicoSugerencia = new ArrayList<Sugerencia>();
         historicoSugerencia = service.cargarHistSuger(this.user.getUsername());
         unanota = new Notas();
         listaNotas = new ArrayList<String>();
         grupoAlumno = new ArrayList<String>();
         nuevoTexto="";
     }
     
     public void setInfoservice(InfoService infoservice) {
        this.infoservice = infoservice;
     }   
    
     public String itemDescri(String codigo) {
        for(ItemInstrumento itv : items) {
           if(codigo.equals(itv.getCodigo())) {
               return itv.getDescri();
           }
        }
        return null;
     }

     public void setItems(List<ItemInstrumento> it) {
        this.items = it;
     }

     public List<ItemInstrumento> getItems() {
        return this.items;
     }

     public TreeNode getRoot() {
        return root;
     }
  
     public TreeNode getSelectedNode() {
        return selectedNode;
     }
 
     public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
     }     
 
     public String getGrupoValidoAgenda() {
        return grupoValidoAgenda;
     }
     public void setGrupoValidoAgenda(String grupo) {
        this.grupoValidoAgenda=grupo;
     }
     public String reset() {
       trabajo.setTitle("");
       trabajo.setDescri("");
       trabajo.setEval("");
       trabajo.setPeso(5);
       return null;
     }

    public void setAlumCua(List<AlumnoCuaderno> alumCua) {
        this.alumCua=alumCua;
    }
    
    public void setLosprofes(Set<String> losprofes) {
        this.user.setLosprofes(losprofes);
    }
    public Set<String> getLosprofes() {
       return this.user.getLosprofes();
    }
    public String getEval() {
        return eval;
    }

    public void setGrupoEscogido(String grupoE) {
        this.grupoEscogido=grupoE;
    }
    public void setUnanota(Notas unanota) {
        this.unanota=unanota;
    }
    public Notas getUnanota() {
        return this.unanota;
    }
    public Date getUnafecha() {
        return this.unafecha;
    }
    public void setUnafecha(Date unafecha) {
        this.unafecha=unafecha;
    }
    public Sugerencia getUnafaq() {
        return this.unafaq;
    }
    public void setUnafaq(Sugerencia unafaq) {
        this.unafaq=unafaq;
    }
    public Sugerencia getSugerencia() {
        return this.sugerencia;
    }
    public void setSugerencia(Sugerencia sugerencia) {
        this.sugerencia=sugerencia;
    }
    public void setListSugerencia(List<Sugerencia> listSugerencia) {
        this.listSugerencia=listSugerencia;
    }
    public List<Sugerencia> getListSugerencia() {
        return listSugerencia;
    }
    public List<Profe> getListprofe() {
        return this.listprofe;
    }
    public List<Grupo> getListgrup() {
        for(Grupo g : listgrup) {
	}
        return this.listgrup;
    }
    public String getGrupoEscogido() {
        return grupoEscogido;
    }

    public String getOption() {
        return option;
    }
 
    public void setEval(String eval) {
        /*String ev=eval;
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
        showagenda=false;
        alumCua.clear();
        tupdating.clear();
        this.user.setGruposBlanco(user.getGrupos());
        this.option="";
        agenda=new ScheduleView();
        grupoValidoAgenda="--no grupo válido--";
        diario=new Diario();
        items.clear(); // new ArrayList<ItemInstrumento>();
    }
    
    public String getAdminprofe() {
        return adminprofe;
    }
    
    public void respuestaFaq(ActionEvent event) {
       String cabeceraRespFaq="\n>\nRespuesta >> ";
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:H:mm");
       Date f=new Date();
       String fecha=formatter.format(f);
       String fechaAnt=formatter.format(sugerencia.getFecha());
       cabeceraRespFaq=cabeceraRespFaq + fecha + " >> " + this.user.getUsername() + "\n"; 
       sugerencia.setDescri(fechaAnt + " " + sugerencia.getDescri() + cabeceraRespFaq + nuevoTexto);
        /* preparing mail to the user who made the suggestion
       String to = "xxxxxx@domain";
       String from = "yyy@domain";
       String host = "zzzz";
       Properties properties = System.getProperties();
       properties.setProperty("mail.smtp.host", host);
       Session session = Session.getDefaultInstance(properties);
       try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         message.setSubject("Respuesta a sugerencia");
         message.setText(sugerencia.getDescri());
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      } */
      //
      // It's necessary for this: javax.mail-1.5.3.jar and activation.jar
      //
    }
    public void actionInstrumento() {
      if(service.validaItemsOrden() && service.validaTotalPercentItems()) {
        service.updateItems(this.user.getUsername(),this.option,this.eval);
        alumCua=service.createAluGru(this.user.getUsername(),this.option,this.eval);
        items=service.getItems();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK",  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
      }
    }

    public void actionTupdating1() {
       if(this.tupdating.get(0)!=null) {
          String descri=tupdating.get(0).getDescri();
          if(descri!=null) {
             for(Trabajo t : tupdating) {
               int peso=t.getPeso();
               String tit=t.getTitle();
               String key=t.getClave();
               String descri1=t.getDescri();
               service.updatingT1(this.user.getUsername(),this.option,this.eval,descri1,key,peso,tit);
             }
          }
       }
       alumCua=service.createAluGru(this.user.getUsername(),this.option,this.eval);
       tupdating = service.getUpdateTrabajos();
    }

    public List<Trabajo> getTupdating() {
        return this.tupdating;
    }

    public void setTupdating(List<Trabajo> lt) {
        this.tupdating = lt;
    }

    public void setOption(String option) {
        this.option = option;
        showagenda=true;
        setGrupoValidoAgenda(option);
        alumCua.clear();
        tupdating.clear();
        diarioItem.setFecha(new Date());
        diarioItem.setTexto(" ");

    //    diarioItem.setFecha(null);
    //    diarioItem.setTexto("");
        diarioItemEdit.setFecha(null);
        diarioItemEdit.setTexto("");
        agenda = service.cargarAgenda(this.user.getUsername(),this.option);
        diario = service.cargarDiario(this.user.getUsername(),this.option);
        alumCua=service.createAluGru(this.user.getUsername(),this.option,this.eval);
        tupdating = service.getUpdateTrabajos();
        items=service.getItems();
    }
    
    public Diario getDiario() {
        return this.diario;
    }
    public void setDiario(Diario diario) {
        this.diario=diario;
    }
    public ScheduleView getAgenda() {
        return this.agenda;
    }
    public void setAgenda(ScheduleView agenda) {
        this.agenda=agenda;
    }
    public void setUser(UserLoginView user) {
        this.user=user;
    }

    public UserLoginView getUser() {
        return this.user;
    }

    public List<AlumnoCuaderno> getAlumCua() {
        return this.alumCua;
    }


    public void setService(AlumnoCuadernoService service) {
        this.service=service;
    }

    public AlumnoCuadernoService getService() {
        return this.service;
    }

    public String[] getGeneralEval() {
        return service.getGeneralEval();
    }

    public ItemDiario getDiarioItem() {
        return diarioItem;
    }

    public void setDiarioItem(ItemDiario item) {
        this.diarioItem=item;
    }

    public String[] getTipoFaq() {
        return service.getTipoFaq();
    }
    public String[] getIdescri() {
        return service.getIDescri();
    }

    public String[] getGeneralDescri() {
        return service.getGeneralDescri();
    }

    public int[] getGeneralOrden() {
        return service.getGeneralOrden();
    }

    public int[] getPercent() {
        return service.getPercent();
    }

    public void actionElimTrabajo(){
    }
    public void actionAgenda(){
       service.grabarAgenda(this.user.getUsername(),this.option,agenda);
    }
    public void actionDiario(){
       service.grabarItemDiario(this.user.getUsername(),this.option,this.diarioItem,editandoItemDiario,diarioItemEdit);
       editandoItemDiario=false;
       diario = service.cargarDiario(this.user.getUsername(),this.option);
       diarioItem.setTexto(""); 
       diarioItem.setFecha(null); 
    }

    public void actionRespFaq(){	
       service.grabarRespFaq(sugerencia);
       nuevoTexto="";
       sugerencia.setDescri("");
       sugerencia.setClave("");
       sugerencia.setFecha(null);
       listSugerencia=service.cargarSugerencia();
       
    }
    public String getActionNotas() {
       return actionNotas;
    }
    public void setActionNotas(String action) {
       this.actionNotas=action;
       if(this.actionNotas.contains("editar")) {
         listaNotas = new ArrayList<String>();
         listaNotas=service.getAdmNotasTitles(user.getUsername());
       }
       if(this.actionNotas.contains("crear")) {
       }
    }
    public List<String> getListaNotas() {
       return this.listaNotas;
    }
    public void setListaNotas(List<String> lista) {
       this.listaNotas=lista;
    }
    public List<String> getListadegrupos() {
       List<String> list = new ArrayList<String>();
       for(Grupo g : listgrup) {
         list.add(g.getClave());
       }
       return list;
    }
    public String[] getAdmNotas() {
       return service.getAdmNotas();
    }

    public void actionNotas(){
       service.grabarTextoNota(user.getUsername(),unanota.getTitle(), unanota.getTexto());
    }

    public String actionFaq(){
       SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy:H:mm");
       Date f=new Date();
       unafaq.setFecha(f);
       String fecha=formatter.format(f);
       unafaq.setAuthor(this.user.getUsername());
       service.grabarFaq(this.user.getUsername(),unafaq);
       return "index51-17";
    }
    public String actionTrabajo(){
       service.createTrabajos(this.user.getUsername(),this.option,trabajos);
       alumCua=service.createAluGru(this.user.getUsername(),this.option,this.eval);
       tupdating = service.getUpdateTrabajos();
       trabajos.clear();
       trabajo.setTitle("");
       trabajo.setDescri("");
       trabajo.setEval("");
       trabajo.setPeso(5);
       return null;
    }
    
    private String obtenerDescriItem(String codigoItem) {
       return service.obtenerDescriItem(codigoItem);
    }

    public void action(AlumnoCuaderno alc){
          for(Eva eva : alc.getListEva()) {
             List<ItemEva> lista_it=eva.getListItem();
             for( ItemEva ie : lista_it ) {
               String codigoItem=ie.getDescri();
                // obtenr codigo del item a partir de la descri
               //String descri=obtenerDescriItem(codigoItem);
               List<Valor> lv = ie.getValores();
               if(!lv.isEmpty()) {
                 service.editandoTable(this.user.getUsername(),this.option,codigoItem,this.eval,alc.getAlu().getNombre(),lv);
               }
             }
          }
          alc.obtenerPie();
    }
   
    public void displaySelectedSingle() {
        if(selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    public void displaySelectedSingle1() {
        if(selectedNode != null) {
           String eval=selectedNode.getData().toString();
           if(eval.contains("Ev")) {
             String grupo=selectedNode.getParent().getData().toString();
             String profe=selectedNode.getParent().getParent().getData().toString();
             String select=profe + ":" + grupo + ":" + eval;
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", select);
            // FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getParent().getData().toString());
             FacesContext.getCurrentInstance().addMessage(null, message);
             
             String ev;
             if(eval.equals("Primera Ev")) {
               ev="Primera"; 
             }
             else if(eval.equals("Segunda Ev")){
               ev="Segunda"; 
             }
             else {
               ev="Tercera"; 
             } 
             alumCua=service.createAluGru(profe,grupo,ev);
             this.adminprofe=profe + " - " + grupo + " - " + eval;
           }
        }
    }
 
  /*  public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }*/

    public boolean isAdmin(){
        return admin;
    }
    public boolean isCerrando(){
        return cerrando;
    }
    public boolean isCuadernostandard(){
        return cuadernostandard;
    }
    public boolean isShowagenda(){
        return showagenda;
    }
    public boolean isConsultaG(){
        return consultaG;
    }
    public boolean isCargapickadmin(){
        return cargapickadmin;
    }
    public boolean isFaq(){
        return faq;
    }
    public boolean isCargagrupoadmin(){
        return cargagrupoadmin;
    }
    public boolean isCuadernoadmin(){
        return cuadernoadmin;
    }
    public boolean isModificando(){
        return modificando;
    }
    public boolean isAgregando(){
        return agregando;
    }
    

    public String actionAgregando(){
        return null;
    }
    // moviendo eventos en agenda 
    public void onEventMove(ScheduleEntryMoveEvent event) {
        agenda.onEventMove(event);
        actionAgenda();
    }
 
    public void agregando(ActionEvent actionEvent) {
        agregando=true;
        cerrando=false;
        modificando=false;
    }
    public void cerrando(ActionEvent actionEvent) {
        agregando=false;
        modificando=false;
    }
    public void cuadernomio(ActionEvent actionEvent) {
        cuadernoadmin=false;
        cargagrupoadmin=false;
        faq=false;
        cargapickadmin=false;
        consultaG=false;
        cuadernostandard=true;
        adminprofe="";
        alumCua=service.createAluGru(this.user.getUsername(),this.option,this.eval);
    }
    public void altaDiario(ActionEvent event) {
      String mensa=null;
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      String st=formatter.format(diarioItem.getFecha());
      if(!editandoItemDiario) { 
       if(!service.validaAltaDiarioFecha(this.user.getUsername(),this.option,st))
       {
        mensa="Existente un item con fecha: " + st;
       }
       if(mensa!=null) {
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, mensa,  null);
         FacesContext.getCurrentInstance().addMessage(null, message);
       }
      }
    }

    public void updatingItems(ActionEvent event) {
      String mensa=null;
      if(!service.validaItemsOrden()) {
        mensa="número de orden repetido";
      }
      if(!service.validaTotalPercentItems()) {
        mensa="total de pesos supera el 100%";
      }
      if(mensa!=null) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, mensa,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
      }
       //List<ItemInstrumento> items = ((List<ItemInstrumento>) event.getObject()).getTitle();
    }
    
    public void eliminandoTrabajo(ActionEvent event) {
          String buttonId;
          buttonId = event.getComponent().getClientId();
         FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item A eliminar",buttonId );
      FacesContext.getCurrentInstance().addMessage(null, msg);
      //String tra = ((Trabajo) event.getObject()).getTitle();
      String param = (String)event.getComponent().getAttributes().get("valor");
      for(Trabajo tra : tupdating) {
         if(tra.getClave().equals(param)) {
            tupdating.remove(tra);
            break; 
         }
      }
    }
    
    public void consultaG(ActionEvent actionEvent) {
        consultaG=true;
        cuadernoadmin=false;
        cargagrupoadmin=false;
        cargapickadmin=false;
        cuadernostandard=false;
        faq=false;
        listgrup = new ArrayList<Grupo>();
        listgrup=service.listgrup();
        listP = new ArrayList<String>();
        listprofe = new ArrayList<Profe>();
        listprofe=service.listprofe();
        for(Profe pr : listprofe){
           listP.add(pr.getClave());
        }
        List<Grupo> listg=new ArrayList<Grupo>();
        listg=service.listgrup();
        listGrLoad.clear();
        for(Grupo gr : listg) {
           listGrLoad.add(gr.getClave());
        }
        cargaGProfe = new DualListModel<String>(listGrLoad, listGrLoadTarget);
    }
    public void faq(ActionEvent actionEvent) {
        faq=true;
        consultaG=false;
        cuadernoadmin=false;
        cargagrupoadmin=false;
        cargapickadmin=false;
        cuadernostandard=false;
        listSugerencia=service.cargarSugerencia();
        sugerencia=new Sugerencia();
    }
    public void cargagrupoadmin(ActionEvent actionEvent) {
        cargagrupoadmin=true;
        cuadernoadmin=false;
        cargapickadmin=false;
        cuadernostandard=false;
        faq=false;
        consultaG=false;
        alumCua = service.createAluGru(null,null,null);
    }

    public void cuadernoadmin(ActionEvent actionEvent) {
        cuadernoadmin=true;
        cuadernostandard=false;
        alumCua = service.createAluGru(null,null,null);
    }

    public void modificando(ActionEvent actionEvent) {
        modificando=true;
        cerrando=false;
        agregando=false;
    } 
    public void onTabChange(TabChangeEvent event) {
      /*
        FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());
        FacesContext.getCurrentInstance().addMessage(null, msg);
       */
        String st=event.getTab().getTitle();
        if(st.contains("Modificando")||st.contains("Eliminando") || st.contains("items") || st.contains("Diario")) {
          trabajo.setTitle("  ");
          trabajo.setDescri("Pruebas escritas");
          trabajo.setEval("0");
          trabajo.setPeso(5);
        }
        if(st.contains("Agregando")) {
          trabajo.setTitle("");
          trabajo.setDescri("");
          trabajo.setEval("Primera");
          trabajo.setPeso(5);
          diarioItem.setFecha(new Date());
          diarioItem.setTexto(" ");
        }
        if(st.contains("Diario")) {
          diarioItem.setFecha(null);
          diarioItem.setTexto("");
        }
       
    }
         
    public void onTabClose(TabCloseEvent event) {
        FacesMessage msg = new FacesMessage("Tab Closed", "Closed tab: " + event.getTab().getTitle());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void grupoChanged(final AjaxBehaviorEvent event)  {
    }

    public void asignarChanged1(final AjaxBehaviorEvent event)  {
    }
    public void grupoChanged1(final AjaxBehaviorEvent event)  {
    }

    public void onRowEdit(RowEditEvent event) {
      //FacesMessage msg = new FacesMessage("Item Editado", ((ItemEva) event.getObject()).getDescri());
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Editado",((ItemEva) event.getObject()).getDescri() );
      FacesContext.getCurrentInstance().addMessage(null, msg);
      ((ItemEva) event.getObject()).obtenerMedia();
    }

    public void sugerenciaOnRowSelect(SelectEvent event) {
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
       String clave=((Sugerencia) event.getObject()).getClave();
       String texto=((Sugerencia) event.getObject()).getDescri();
       Date f=((Sugerencia) event.getObject()).getFecha();
       String fecha = format.format(f);
       String author=((Sugerencia) event.getObject()).getAuthor();
       String tipo=((Sugerencia) event.getObject()).getTipo();
       FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Item seleccionado ", texto + " *** " +fecha + " ** " + author + " ** " + tipo + " ** "+ clave );
       FacesContext.getCurrentInstance().addMessage(null, msg);
       sugerencia.setClave(clave);
       sugerencia.setDescri(texto);
       sugerencia.setFecha(f);
       sugerencia.setTipo(tipo);
       sugerencia.setAuthor(author);
    }
    public void onRowSelect5(SelectEvent event) {
        nuevoTexto=((Sugerencia) event.getObject()).getDescri();
    }
    public void onRowSelect(SelectEvent event) {
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
       String texto=((ItemDiario) event.getObject()).getTexto();
       Date f=((ItemDiario) event.getObject()).getFecha();
       String fecha = format.format(f);
       FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Item seleccionado ", texto + " *** " +fecha);
       FacesContext.getCurrentInstance().addMessage(null, msg);
       editandoItemDiario=true;
       diarioItemEdit.setTexto(texto);
       diarioItemEdit.setFecha(f);
       diarioItem.setTexto(texto);
       diarioItem.setFecha(f);
    }
    public void sugerenciaOnRowUnSelect(UnselectEvent event) {
    }
    public void onRowUnSelect(UnselectEvent event) {
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
       String texto=((ItemDiario) event.getObject()).getTexto();
       Date f=((ItemDiario) event.getObject()).getFecha();
       String fecha = format.format(f);
       FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Item deseleccionado ", texto + " *//* " + fecha);
       FacesContext.getCurrentInstance().addMessage(null, msg);
       editandoItemDiario=false;
       diarioItem.setTexto("---");
       diarioItem.setFecha(new Date());
    }
    public void onCellEdit(CellEditEvent event) {

       // Object oldValue = event.getOldValue();
        //Object newValue = event.getNewValue();
        
         
        //if(newValue != null && !newValue.equals(oldValue)) {
            //FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            //FacesContext.getCurrentInstance().addMessage(null, msg);
        //}
        
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
       Object oldValue = event.getOldValue();
       Object newValue = event.getNewValue();
       ItemDiario item=new ItemDiario();
       item=(ItemDiario) oldValue;
       String texto=item.getTexto();
       String st = format.format(item.getFecha());
       FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Editado ",st + texto);
       FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowEdit3(RowEditEvent event) {
//      FacesMessage msg = new FacesMessage("Item Editado", "Fecha: " );
      //FacesMessage msg = new FacesMessage("Item Editado", "Fecha: " +((ItemDiario) event.getObject()).getFecha());
       FacesContext facesContext = FacesContext.getCurrentInstance();
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
       String st = format.format(((ItemDiario)event.getObject()).getFecha());
       facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se está editando fecha ", st));
        //
       String mensa=null;
       if(!service.validaAltaDiarioFecha(this.user.getUsername(),this.option,st))
        {
          mensa="Existente un item con fecha " + st;
          //diarioItem.setFecha(null);
        }
        if(mensa!=null) {
          facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,mensa, st));
        }
        else {
          facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item a salvar con fecha ", st));
        }

      /*
      FacesContext facesContext = FacesContext.getCurrentInstance();
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date to Save", format.format(((ItemDiario)event.getObject()).getFecha())));
      */
    }

    public void onRowEdit2(RowEditEvent event) {
      FacesMessage msg = new FacesMessage("Item Editado", ((ItemInstrumento) event.getObject()).getDescri());
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowEdit1(RowEditEvent event) {
      FacesMessage msg = new FacesMessage("Item Editado", ((Trabajo) event.getObject()).getTitle());
      FacesContext.getCurrentInstance().addMessage(null, msg);
//      ((ItemEva) event.getObject()).obtenerMedia();
    }

    public void onRowCancel(RowEditEvent event) {
      FacesMessage msg = new FacesMessage("Item Cancelado", ((ItemEva) event.getObject()).getDescri());
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel3(RowEditEvent event) {
      FacesMessage msg = new FacesMessage("Item Cancelado", "Fecha ");
      //FacesMessage msg = new FacesMessage("Item Cancelado", "Fecha: " + ((ItemDiario) event.getObject()).getFecha());
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel2(RowEditEvent event) {
      FacesMessage msg = new FacesMessage("Item Cancelado", ((ItemInstrumento) event.getObject()).getDescri());
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel1(RowEditEvent event) {
      FacesMessage msg = new FacesMessage("Item Cancelado", ((Trabajo) event.getObject()).getTitle());
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public String reinit() {
        // createNew();
        trabajo = new Trabajo();
        return null;
    }
 
    public Trabajo getTrabajo() {
        return trabajo;
    }

    public List<Trabajo> getTrabajos() {
        return trabajos;
    }

    public void sugerenciaOnDateSelect(SelectEvent event) {
    }
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //
        String mensa=null;
         String st = format.format(diarioItem.getFecha());
         if(!service.validaAltaDiarioFecha(this.user.getUsername(),this.option,st))
         {
          mensa="Existente un item con fecha " + st;
          diarioItem.setFecha(null);
         }
         if(mensa!=null) {
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, mensa,  null);
           FacesContext.getCurrentInstance().addMessage(null, message);
         }
         else {
          facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
         }
    }
     
    public void handleToggle(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Toggled", "Visibility:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    public void upload1()  throws IOException {
        if(file != null) {
            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        InputStream is;
        String str;
        int hasta;
        is=file.getInputstream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO8859-1"));
        // reading title -first record-
        listAluLoad.clear();
        str=br.readLine();
        while ((str = br.readLine()) != null) {
           hasta=str.indexOf("\"",1);
           listAluLoad.add(str.substring(1,hasta));
	}
        cargaAlum = new DualListModel<String>(listAluLoad, listAluLoadTarget);
        cargapickadmin=true;
        listgrup = new ArrayList<Grupo>();
        listgrup=service.listgrup();
    }
   
    public List<String> getListAluLoadTarget() {
        return listAluLoadTarget;
    }
    
    public List<String> getAlumnosCargados() {
        return listAluLoad;
    }
        
    public DualListModel<String> getCargaAlum() {
        return cargaAlum;
    }
 
    public void setCargaAlum(DualListModel<String> cargaAlum) {
        this.cargaAlum = cargaAlum;
    }

    public DualListModel<String> getCargaGProfe() {
        return cargaGProfe;
    }
 
    public void setCargaGProfe(DualListModel<String> cargaGProfe) {
        this.cargaGProfe = cargaGProfe;
    }

    public void actionAlumnosCargados() {
        if(!service.existeGrupo(grupoEscogido)) { 
          service.altaGrupo(cargaAlum.getTarget(),grupoEscogido);
          listgrup=new ArrayList<Grupo>();
          listgrup=service.listgrup();
        }
    }
    public void actionPG() {
        //if(!service.existeGrupo(selectedPr)) { 
               service.altaGrupoProfe(cargaGProfe.getTarget(),selectedPr1);
               //service.altaGrupoProfe(cargaGProfe.getTarget(),"paulino");
        //listgrup=new ArrayList<Grupo>();
        //listgrup=service.listgrup();
       //}
    }
    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            builder.append((String) item).append("<br />");
        }
         
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    } 
 
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
    public void onResize(ColumnResizeEvent event) {
        FacesMessage msg = new FacesMessage("Column " + event.getColumn().getClientId() + " resized", "W:" + event.getWidth() + ", H:" + event.getHeight());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    } 
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    } 
    
    public String getSelectedPr1() {
        return selectedPr1;
    }
    public String getSelectedPr() {
        return selectedPr;
    }
    public String getSelectedGr() {
        return selectedGr;
    }
 
    public void setSelectedPr1(String selectedPr1) {
        this.selectedPr1 = selectedPr1;
    }
    public void setSelectedPr(String selectedPr) {
        this.selectedPr = selectedPr;
    }
    public void setSelectedGr(String selectedGr) {
        grupoAlumno=service.grupoAlumno(selectedGr);
        this.selectedGr = selectedGr;
    }
    public void seleccionadoAction(ActionEvent actionEvent) {
        addMessage("Welcome to Primefaces!!");
    }
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }    
    public List<String> getProfeGrupo() {
      List<String> list = new ArrayList<String>();
      list=service.profeGrupo(selectedPr);
      return list;
    }
    public void setGrupoAlumno(List<String> grupoAlumno) {
      this.grupoAlumno=grupoAlumno;
    }
    public List<String> getGrupoAlumno() {
      //List<String> list = new ArrayList<String>();
      //list=service.grupoAlumno(selectedGr);
      //return list;
      return grupoAlumno;
    }
    public void setHistoricoSugerencia(List<Sugerencia> lista) {
      this.historicoSugerencia=lista;
    }
    public String getNuevoTexto() {
      return this.nuevoTexto;
    }
    public void setNuevoTexto(String nuevoT) {
      this.nuevoTexto=nuevoT;
    }
    public List<Sugerencia> getHistoricoSugerencia() {
      return historicoSugerencia;
    }
    public boolean respondidaFaq(Sugerencia sug) {
      return service.respondidaFaq(sug.getClave());
    }
    public void actionAsignarAlumno() {
      service.altaUnAlumno(selectedGr,newalumno);
      newalumno="un alumno";
      selectedGr="";
    }
    public void actionAltaGrupo() {
      service.altaUnGrupo(newgrupo);
      newgrupo="un grupo";
      listgrup = new ArrayList<Grupo>();
      listgrup=service.listgrup();
      listprofe = new ArrayList<Profe>();
      listprofe=service.listprofe();
      listP.clear();
      listGrLoadTarget.clear();
      listGrLoad.clear();
      for(Profe pr : listprofe){
           listP.add(pr.getClave());
      }
      for(Grupo gr : listgrup) {
        listGrLoad.add(gr.getClave());
      }
      cargaGProfe = new DualListModel<String>(listGrLoad, listGrLoadTarget);
    }
    public String getNewalumno() {
      return this.newalumno;
    }
    public String getNewgrupo() {
      return this.newgrupo;
    }
    public String getNewprofe() {
      return this.newprofe;
    }
    public void setNewalumno(String newalumno) {
      this.newalumno=newalumno;
    }
    public void setNewgrupo(String ungrupo) {
      this.newgrupo=ungrupo;
    }
    public void setNewprofe(String unprofe) {
      this.newprofe=unprofe;
    }
    public void actionAltaProfe() {
      service.altaUnProfe(newprofe);
      newprofe="un profe";
      listprofe = new ArrayList<Profe>();
      listprofe=service.listprofe();
      listP.clear();
      for(Profe pr : listprofe) {
        listP.add(pr.getClave());
      }
    }
    public List<String> getListP() {
      return this.listP;
    }
    public void setListP(List<String> lista) {
      this.listP=lista;
    }
    public String getBackurl() {
      return this.backurl;
    }
    public void setBackurl(String backurl) {
      this.backurl=backurl;
    }
    public void setCargaNota(String carganota) {
      this.cargaNota=carganota;
      unanota.setTitle(this.cargaNota);
      unanota.setTexto(service.textoNota(user.getUsername(),this.cargaNota));
    }
    public String getCargaNota() {
      return cargaNota;
    }
    /*
    public String actionSidebar() {
      if(user.getUsername()!=null && user.getUsername().length() > 0) {
        return "sugerencias";
      }
      return null;
    } */
}
