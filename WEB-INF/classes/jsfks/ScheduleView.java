package jsfks;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.text.ParseException;
 
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import javax.faces.bean.SessionScoped;
 
//@ManagedBean
//@ViewScoped
public class ScheduleView implements Serializable {
 
    private ScheduleModel eventModel  = new DefaultScheduleModel();
     
    private ScheduleModel lazyEventModel;
 
    private DefaultScheduleEvent event = new DefaultScheduleEvent();
 
    @PostConstruct
    public void init() {
        //eventModel = new DefaultScheduleModel();
        /*SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	String dateInString = "9-Jun-2015";
        try {
             Date date1 = formatter.parse(dateInString);
             eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", date1, date1,"public-event"));
	} catch (ParseException e) {
		e.printStackTrace();
	}  */
        //eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
        //eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
        //eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
        //eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));
        lazyEventModel = new LazyScheduleModel() {
             
            @Override
            public void loadEvents(Date start, Date end) {
                Date random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));
                 
                random = getRandomDate(start);
                addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
            }   
        };
    }
    public void setDatos(String title,String d1) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
             Date date1 = formatter.parse(d1);
             DefaultScheduleEvent ev = new DefaultScheduleEvent(title, date1, date1);
             ev.setAllDay(true);
             this.eventModel.addEvent(ev);
	} catch (ParseException e) {
		e.printStackTrace();
	} 
    }
    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);//set random day of month
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }
 
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);
         
        return t.getTime();
    }
     
    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);
         
        return t.getTime();
    }
     
    private Date theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);     
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
 
    private Date today6Pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);
         
        return t.getTime();
    }
     
    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);
         
        return t.getTime();
    }
     
    private Date nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date fourDaysLater3pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
     
    public ScheduleEvent getEvent() {
       Date date= new Date();
       date=event.getStartDate();
       return event;
    }
 
    public void setEvent(DefaultScheduleEvent event) {
        this.event = event;
    }
     
    public void deleteEvent() {
         eventModel.deleteEvent(event);
    }
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null) {
            event.setAllDay(true);
            event.setEndDate(event.getStartDate());
            eventModel.addEvent(event);
        }
        else {
            eventModel.updateEvent(event);
        }
        event = new DefaultScheduleEvent();
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        DefaultScheduleEvent sce = (DefaultScheduleEvent) event.getScheduleEvent();
        String fecha = sce.getStartDate().toString();

        Calendar newCal = new GregorianCalendar();
        newCal.setTime(sce.getStartDate());
        newCal.add(Calendar.DATE, (-1 * event.getDayDelta()));
        String fecha1 = newCal.getTime().toString();

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved" + " ** fecha anterior " + fecha, " ** fecha actual " + fecha1 ); 
        sce.setStartDate(newCal.getTime());
        //grabarDatos();
          
        addMessage(message);
      }



        private void grabarDatos() {
      Date date = new Date();
      SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
      for(ScheduleEvent ev : getEventModel().getEvents()) {
         String fecha = sdf.format(ev.getStartDate());
      }
    }
 
         
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
