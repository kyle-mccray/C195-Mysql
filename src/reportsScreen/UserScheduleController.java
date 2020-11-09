package reportsScreen;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class UserScheduleController implements Initializable {

    @FXML private TableView<Appointment> myTable;
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endDateCol;
    @FXML private TableColumn<Appointment, String> customerNameCol;
    @FXML private TableColumn<Appointment, String> appointmentTitleCol;
    public ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();



    public void initData(int selectedUserId){
        try {
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        Class.forName("com.mysql.jdbc.Driver");

        conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");

        String query = "select customerName, title, start, end, appointmentId from appointment join customer c on appointment.customerId = c.customerId where userId = ? and date(start) between current_date() and adddate(current_date, 7)";
        ps = conn.prepareStatement(query);

        ps.setInt(1, selectedUserId);
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
            myTable.setItems(appointmentsList);


        }
        catch(Exception e){
            e.printStackTrace();

        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("appTitle"));

      }

    public void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/reportsScreen/reports.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
