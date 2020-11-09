package appointments.Controller;

import homeScreen.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;


public class AppointmentsByMonthController implements Initializable {
    @FXML private Label monthLabel;
    @FXML private TableView<Appointment> monthTable;
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endDateCol;
    @FXML private TableColumn<Appointment, String> customerNameCol;
    @FXML private TableColumn<Appointment, String> appointmentTitleCol;
    public  LocalDate currMonth = LocalDate.now();
    public  ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("appTitle"));
        setAppointments(currMonth);
        monthLabel.setText(String.valueOf(currMonth.getMonth()));
    }

    public void setAppointments(LocalDate selectedMonth){

        while(!appointmentsList.isEmpty()){
            int i = 0;
            appointmentsList.remove(i);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
            PreparedStatement ps;
            ResultSet rs;

            String query = "select customerName, userId, title, start, end, appointmentId from appointment " +
                           "join customer c on appointment.customerId = c.customerId where MONTH(start) = MONTH(?)" +
                           "and YEAR(start) = year(?)";

            ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(selectedMonth));
            ps.setString(2, String.valueOf(selectedMonth));

            rs = ps.executeQuery();

            while(rs.next()){
                String cName = rs.getString("customerName");
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

                    appointmentsList.addAll(new Appointment(localStart, localEnd, cName, title, appId));
                }

                catch(Exception e){
                    e.printStackTrace();

                }

            }
            monthTable.setItems(appointmentsList);


        }
        catch(Exception e){
            e.printStackTrace();

        }

    }

    public void handleNextMonth(ActionEvent event){
        currMonth = currMonth.plusMonths(1);
        setAppointments(currMonth);
        monthLabel.setText(String.valueOf(currMonth.getMonth()));
    }

    public void handlePreviousMonth(ActionEvent event){
        currMonth = currMonth.minusMonths(1);
        setAppointments(currMonth);
        monthLabel.setText(String.valueOf(currMonth.getMonth()));
    }

    public void handleNewAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/addAppointment.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleDeleteAppointment(ActionEvent event) {
        Appointment selectedRecord =  monthTable.getSelectionModel().getSelectedItem();
        deleteAppointment(selectedRecord);
        appointmentsList.remove(selectedRecord);
    }

    public void handleModAppointment(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/appointments/View/modAppointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ModAppointmentController controller = loader.getController();
        Appointment selectedItem = monthTable.getSelectionModel().getSelectedItem();
        controller.initOldData(selectedItem);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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
        Parent root = FXMLLoader.load(getClass().getResource("/reportsScreen/reports.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleAppByWeek(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByWeek.fxml"));
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
