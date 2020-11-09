package appointments.Controller;

import appointments.Model.AppointmentCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import homeScreen.Model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ResourceBundle;


public class AppointmentsByWeekController implements Initializable {

    @FXML private Label monthLabel;
    @FXML private TableView<Appointment> myTable;

    @FXML private TableColumn<Appointment,String> sundayAppointmentsCol;
    @FXML private TableColumn<Appointment,String> mondayAppointmentsCol;
    @FXML private TableColumn<Appointment,String> tuesdayAppointmentsCol;
    @FXML private TableColumn<Appointment,String> wednesdayAppointmentsCol;
    @FXML private TableColumn<Appointment,String> thursdayAppointmentsCol;
    @FXML private TableColumn<Appointment,String> fridayAppointmentsCol;
    @FXML private TableColumn<Appointment,String> saturdayAppointmentsCol;

    private LocalDate switchDate;
    public  LocalDate localDate = LocalDate.now();
    public  ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initTable();
        myTable.getSelectionModel().setCellSelectionEnabled(true);
        monthLabel.setText(String.valueOf(localDate.getMonth()));

        switch (localDate.getDayOfWeek())
        {
            case SUNDAY:
                switchDate = localDate.plusDays(0);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(1)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(2)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(3)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(4)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(5)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(6)));

                setAppointments(localDate.plusDays(0), localDate.plusDays(6));
                break;

            case MONDAY:
                switchDate = localDate.plusDays(-1);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-1)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(1)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(2)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(3)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(4)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(5)));

                setAppointments(localDate.plusDays(-1), localDate.plusDays(5));
                break;

            case TUESDAY:
                switchDate = localDate.plusDays(-2);
                //System.out.print(switchDate);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-2)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-1)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(1)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(2)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(3)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(4)));

                setAppointments(localDate.plusDays(-2), localDate.plusDays(4));
                break;

            case WEDNESDAY:
                switchDate = localDate.plusDays(-3);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-3)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-2)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-1)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(1)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(2)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(3)));

                setAppointments(localDate.plusDays(-3), localDate.plusDays(3));
                break;


            case THURSDAY:
                switchDate = localDate.plusDays(-4);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-4)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-3)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-2)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-1)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(1)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(2)));

                setAppointments(localDate.plusDays(-4), localDate.plusDays(2));
                break;

            case FRIDAY:
                switchDate = localDate.plusDays(-5);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-5)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-4)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-3)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-2)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-1)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(1)));

                setAppointments(localDate.plusDays(-5), localDate.plusDays(1));
                break;

            case SATURDAY:
                switchDate = localDate.plusDays(-6);
                sundayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-6)));
                mondayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-5)));
                tuesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-4)));
                wednesdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-3)));
                thursdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-2)));
                fridayAppointmentsCol.setText(String.valueOf(localDate.plusDays(-1)));
                saturdayAppointmentsCol.setText(String.valueOf(localDate.plusDays(0)));

                setAppointments(localDate.plusDays(-6), localDate.plusDays(0));
                break;
        }
    }

    /*setAppointments takes two dates and preforms a sql query based on the start date in the database.
    Any data that is between the two dates will be returned and added to the appointmentsList*/
    public void setAppointments(LocalDate sunday, LocalDate saturday){
        PreparedStatement ps;
        ResultSet rs;
        while(!appointmentsList.isEmpty()){
            int i = 0;
            appointmentsList.remove(i);
        }
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
            String query = "Select * from appointment where start between ? and ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(sunday));
            ps.setString(2, String.valueOf(saturday));
            rs = ps.executeQuery();

            while(rs.next()){
               String title = rs.getString("title");
               Timestamp sStart = rs.getTimestamp("start");
               Timestamp sEnd = rs.getTimestamp("end");
               int appId = rs.getInt("appointmentId");

                try{
                    LocalDateTime start = sStart.toLocalDateTime();
                    LocalDateTime end = sEnd.toLocalDateTime();
                    ZonedDateTime startUTCTime = start.atZone(ZoneId.of("UTC"));
                    ZonedDateTime endUTCTime = end.atZone(ZoneId.of("UTC"));

                    ZonedDateTime localStart = startUTCTime.withZoneSameInstant(ZoneId.systemDefault());
                    ZonedDateTime localEnd = endUTCTime.withZoneSameInstant(ZoneId.systemDefault());

                    appointmentsList.addAll(new Appointment(title, localStart, localEnd, appId));

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            myTable.setItems(appointmentsList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initTable(){
        sundayAppointmentsCol.setCellFactory(param -> new AppointmentCell(){});
        mondayAppointmentsCol.setCellFactory(param -> new AppointmentCell(){});
        tuesdayAppointmentsCol.setCellFactory(param -> new AppointmentCell(){});
        wednesdayAppointmentsCol.setCellFactory(param -> new AppointmentCell(){});
        thursdayAppointmentsCol.setCellFactory(param -> new AppointmentCell(){});
        fridayAppointmentsCol.setCellFactory(param -> new AppointmentCell(){});
        saturdayAppointmentsCol.setCellFactory(param -> new AppointmentCell());


        sundayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnSunday"));
        mondayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnMon"));
        tuesdayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnTue"));
        wednesdayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnWen"));
        thursdayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnThru"));
        fridayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnFri"));
        saturdayAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("myAppOnSat"));
        myTable.setItems(appointmentsList);

    }

    public void handleNextWeek(ActionEvent event){
        LocalDate nextSunday = switchDate.plusDays(7);
        setAgendaBasedOnSunday(nextSunday);
    }

    public void handleLastWeek(ActionEvent event){
        LocalDate lastSunday = switchDate.plusDays(-7);
        setAgendaBasedOnSunday(lastSunday);
    }

    /*this function updates the GUI dates and calls setAppointments*/
    private void setAgendaBasedOnSunday(LocalDate sunday) {
        switchDate = sunday;

        sundayAppointmentsCol.setText(String.valueOf(sunday.plusDays(0)));
        mondayAppointmentsCol.setText(String.valueOf(sunday.plusDays(1)));
        tuesdayAppointmentsCol.setText(String.valueOf(sunday.plusDays(2)));
        wednesdayAppointmentsCol.setText(String.valueOf(sunday.plusDays(3)));
        thursdayAppointmentsCol.setText(String.valueOf(sunday.plusDays(4)));
        fridayAppointmentsCol.setText(String.valueOf(sunday.plusDays(5)));
        saturdayAppointmentsCol.setText(String.valueOf(sunday.plusDays(6)));
        setAppointments(sunday.plusDays(0), sunday.plusDays(6));

        monthLabel.setText(String.valueOf(sunday.getMonth()));

    }

    public void handleNewAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/addAppointment.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleModAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/appointments/View/modAppointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ModAppointmentController controller = loader.getController();
        Appointment selectedItem = myTable.getSelectionModel().getSelectedItem();
        controller.initOldData(selectedItem);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleDeleteAppointment(ActionEvent event) {
        Appointment selectedRecord = myTable.getSelectionModel().getSelectedItem();
        deleteAppointment(selectedRecord);
        appointmentsList.remove(selectedRecord);
    }

    public void deleteAppointment(Appointment selectedRecord){
        try {
            if(selectedRecord != null) {
                PreparedStatement preparedStatement;
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
                String query = "delete from appointment where appointmentId = ?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, selectedRecord.getAppointmentId());
                preparedStatement.executeUpdate();
                //System.out.println("Record has been deleted");
            }

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleMainScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/homeScreen/View/MainScreen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleReports(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/reportsScreen/reports"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleAppByMonth(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByMonth.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleCustomerSearch(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/searchCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


}
