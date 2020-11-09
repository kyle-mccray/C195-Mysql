package homeScreen.Model;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Appointment {
    private int appointmentId;
    private String myAppOnSunday;
    private String myAppOnMon;
    private String myAppOnTue;
    private String myAppOnWen;
    private String myAppOnThru;
    private String myAppOnFri;
    private String myAppOnSat;

    private String startDate;
    private String endDate;
    private String customerName;
    private String appTitle;





    public Appointment(String title, ZonedDateTime start, ZonedDateTime end, int appointmentId) {


        switch (start.getDayOfWeek()){
           case SUNDAY:
               this.myAppOnSunday = title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;
           case MONDAY:
               this.myAppOnMon = title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;
           case TUESDAY:
               this.myAppOnTue = title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;
           case WEDNESDAY:
               this.myAppOnWen = title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;
           case THURSDAY:
               this.myAppOnThru = title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;
           case FRIDAY:
               this.myAppOnFri = title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;
           case SATURDAY:
               this.myAppOnSat= title + " from " + start.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL)) + " to " + end.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));
               break;

       }
       this.appointmentId = appointmentId;

    }

    public Appointment(ZonedDateTime startDate, ZonedDateTime endDate, String customerName, String appTitle, int appointmentId){
        this.startDate = startDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL));
        this.endDate = endDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL));
        this.customerName = customerName;
        this.appTitle = appTitle;
        this.appointmentId = appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getMyAppOnSunday(){return myAppOnSunday;}

    public String getMyAppOnMon() {
        return myAppOnMon;
    }

    public String getMyAppOnTue() {
        return myAppOnTue;
    }

    public String getMyAppOnWen() {
        return myAppOnWen;
    }

    public String getMyAppOnThru() {
        return myAppOnThru;
    }

    public String getMyAppOnFri() {
        return myAppOnFri;
    }

    public String getMyAppOnSat() {
        return myAppOnSat;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAppTitle() {
        return appTitle;
    }
}
