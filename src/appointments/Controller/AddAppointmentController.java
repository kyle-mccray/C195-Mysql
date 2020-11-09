package appointments.Controller;

import appointments.Model.AppointmentTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AddAppointmentController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextArea descArea;
    @FXML private TextField locationField;
    @FXML private TextField contactField;
    @FXML private TextField urlField;
    @FXML private DatePicker startDate;
    @FXML private ComboBox<Enum<AppointmentTypes.Types>> appType;
    @FXML private ComboBox<String> customerIdBox;
    @FXML private ComboBox<String> consultantId;
    @FXML private ComboBox<String> startTimeBox;
    @FXML private ComboBox<String> endTimeBox;

    String[] startArray = { "09:00:00", "09:30:00",
            "10:00:00", "10:30:00","11:00:00", "11:30:00",
            "12:00:00", "12:30:00", "13:00:00", "13:30:00",
            "14:00:00", "14:30:00","15:00:00", "15:30:00",
            "16:00:00", "16:30:00"};

    List<ZonedDateTime> myTimes = new ArrayList<>();
    DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
        textFieldListener(titleField, 255);
        textFieldListener(locationField, 65535);
        textFieldListener(contactField, 65535 );
        areaFiledListener(descArea, 65535 );
        textFieldListener(urlField, 255);
        startDateListener();

    }

    public void initChoiceBoxes(){
        //add all enums to the appointment Box
        for (Enum<AppointmentTypes.Types> e: AppointmentTypes.Types.values()) {
            appType.getItems().add(e);
        }
        //add all the appointment start times
        for (int i = 0; i < startArray.length ; i++) {
            myTimes.add(ZonedDateTime.of(LocalDate.now(), LocalTime.parse(startArray[i]), ZoneId.systemDefault()));

        }


        for (ZonedDateTime y: myTimes) {
            startTimeBox.getItems().add(y.format(myFormatter));
        }

        startDate.setValue(LocalDate.now());

        Connection conn;
        PreparedStatement ps;
        PreparedStatement ps2;
        ResultSet rs;
        ResultSet rs2;

        try{
            Class.forName("com.mysql.jdbc.Driver");


            conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
            String query = "select customerId, customerName from customer";
            String query2 = "select userName, userId from user";

            ps = conn.prepareStatement(query);
            ps2 = conn.prepareStatement(query2);

            rs = ps.executeQuery();
            rs2 = ps2.executeQuery();

            while(rs.next()){
               String name = rs.getString("customerName");
               String cuId =  rs.getString("customerId");
               String formatted = name + ": " + cuId;
               customerIdBox.getItems().addAll(formatted);

            }
            while(rs2.next()){
                String name = rs2.getString("userName");
                String usId = rs2.getString("userId");
                String formatted = name + ": " + usId;
                consultantId.getItems().addAll(formatted);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleCancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByWeek.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleSave(ActionEvent event) {
        if (!inValidInput()) {

        } else {

            try {

                Connection conn;
                PreparedStatement ps;
                PreparedStatement ps2;
                ResultSet rs;
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");

                String query = "insert into appointment (customerId, userId, title, description, location, contact, " +
                        "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES\n" +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_date, current_user, current_timestamp , current_user) ";

                String query2 = "select start from appointment where start = ? and userId = ?";

                ps = conn.prepareStatement(query);
                ps2 = conn.prepareStatement(query2);

                LocalTime localStartTime = LocalTime.parse(startTimeBox.getSelectionModel().getSelectedItem(), myFormatter);
                LocalDate appDate = startDate.getValue();
                LocalDateTime startDateTime = LocalDateTime.of(appDate, localStartTime);

                LocalDateTime endDateTime = LocalDateTime.of(appDate, LocalTime.parse(endTimeBox.getSelectionModel().getSelectedItem(), myFormatter));

                ZonedDateTime localStartZoneTime = startDateTime.atZone(ZoneId.systemDefault());
                ZonedDateTime utcStartTime = localStartZoneTime.withZoneSameInstant(ZoneId.of("UTC"));
                ZonedDateTime localEndZoneTime = endDateTime.atZone(ZoneId.systemDefault());
                ZonedDateTime utcEndTime = localEndZoneTime.withZoneSameInstant(ZoneId.of("UTC"));

                Timestamp startTime = Timestamp.valueOf(utcStartTime.toLocalDateTime());
                Timestamp endTime = Timestamp.valueOf(utcEndTime.toLocalDateTime());

                ps2.setString(1, String.valueOf(startTime));

                //check to make sure that the consultant does not already have an app scheduled for that time
                String id = consultantId.getSelectionModel().getSelectedItem();
                String[] conArray = id.split(":");
                int usId = Integer.parseInt(conArray[1].replaceAll("\\s+", ""));
                ps2.setInt(2, usId);

                rs = ps2.executeQuery();

                if (rs.next()) {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("ERROR");
                    a.setContentText("An appointment is already scheduled for the selected time");
                    a.showAndWait();
                } else {
                    String item = customerIdBox.getSelectionModel().getSelectedItem();
                    String[] itemArray = item.split(":");
                    int cuId = Integer.parseInt(itemArray[1].replaceAll("\\s+", ""));
                    ps.setInt(1, cuId); // set customerID
                    ps.setInt(2, usId); // set userID
                    ps.setString(3, titleField.getText()); //set title text
                    ps.setString(4, descArea.getText()); // set desc text
                    ps.setString(5, locationField.getText()); // set location text
                    ps.setString(6, contactField.getText()); // set contact text
                    ps.setString(7, String.valueOf(appType.getSelectionModel().getSelectedItem())); // set appointment type
                    ps.setString(8, urlField.getText()); // set url text


                    ps.setString(9, String.valueOf(startTime)); // set the start date and time of the appointment;


                    ps.setString(10, String.valueOf(endTime)); // set the end date and time of the appointment;

                    ps.executeUpdate();

                    //System.out.println("Record was created");
                    //System.out.println(startTime);
                    //System.out.println(endTime);

                    conn.close();
                    Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByWeek.fxml"));
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public boolean inValidInput(){
        if(customerIdBox.getSelectionModel().isEmpty() || consultantId.getSelectionModel().isEmpty() || appType.getSelectionModel().isEmpty()){
            return false;
        }
        if(titleField.getText() == null){
            return false;
        }
        if(startDate.getValue() == null){
            return false;
        }
        return true;
    }

    public void textFieldListener(TextField tf, int maxLength){
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue.length() > maxLength){
               //System.out.println("Limit has been reached");
               tf.setText(oldValue);
               //System.out.println(tf.getText().length());
           }

        });
    }

    public void areaFiledListener(TextArea ta, int maxLength){
        ta.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > maxLength){
                //System.out.println("Limit has been reached");
                ta.setText(oldValue);
                //System.out.println(ta.getText().length());
            }

        });
    }

    public void startDateListener(){
        startTimeBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            int selectedStart = startTimeBox.getSelectionModel().getSelectedIndex();
            if(startTimeBox.getItems().get(selectedStart).equals("04:30:00 PM")){
                endTimeBox.getItems().setAll("05:00:00 PM");
            }
            else {
                endTimeBox.getItems().setAll(startTimeBox.getItems().get(selectedStart + 1));
            }
            endTimeBox.getSelectionModel().select(0);
        }));
    }




}
