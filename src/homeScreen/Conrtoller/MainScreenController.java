package homeScreen.Conrtoller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class MainScreenController implements Initializable {


    public void handleCustomerSearch(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/searchCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    public void handleAppointmentsByWeek(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByWeek.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    public void handleAppointmentsByMonth(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByMonth.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    public void handleReports(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/reportsScreen/reports.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    public void handleExit(ActionEvent event){
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            checkForAppointment();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void checkForAppointment() throws SQLException {

        try{
            Connection conn;
            PreparedStatement ps;
            ResultSet rs;
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
            String query = "select * from appointment where date(start) = current_date()";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            ArrayList<ZonedDateTime> myList = new ArrayList<>();

            while(rs.next()){
                Timestamp start = rs.getTimestamp("start");
                LocalDateTime lStart = start.toLocalDateTime();
                ZonedDateTime startUTCTime = lStart.atZone(ZoneId.of("UTC"));
                ZonedDateTime localZoneTime = startUTCTime.withZoneSameInstant(ZoneId.systemDefault());
                myList.add(localZoneTime);
            }

            for (int i = 0; i < myList.size(); i++) {
                if(ZonedDateTime.now().isBefore(myList.get(i)) && myList.get(i).isBefore(ZonedDateTime.now().plusMinutes(15))){
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("Appointment Notification");
                    a.setContentText("You have appointment within 15 minutes");
                    a.setHeaderText("Upcoming Appointment");
                    a.showAndWait();
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
