package appointments.Controller;

import appointments.Model.AppointmentTypes;
import homeScreen.Model.Appointment;
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


public class ModAppointmentController implements Initializable {

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
    private String selectedAppType;
    private LocalTime selectedStartTime;
    private int selectedCustomer;
    private int selectedUser;
    int appId;

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

        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
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

    public void initOldData(Appointment selectedAppointment){
        appId = selectedAppointment.getAppointmentId();

        try {
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");


            String query = "select * from appointment where appointmentId = ?";

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, appId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            titleField.setText(resultSet.getString("title"));
            descArea.setText(resultSet.getString("description"));
            locationField.setText(resultSet.getString("location"));
            contactField.setText(resultSet.getString("contact"));
            urlField.setText(resultSet.getString("url"));
            LocalTime startTime = resultSet.getObject("start", LocalTime.class);
            LocalDate localDate = resultSet.getObject("start", LocalDate.class);
            LocalDateTime startDateTime = LocalDateTime.of(localDate, startTime);
            ZonedDateTime startUTCTime = startDateTime.atZone(ZoneId.of("UTC"));
            ZonedDateTime localStart = startUTCTime.withZoneSameInstant(ZoneId.systemDefault());
            startDate.setValue(localDate);
            startTimeBox.setValue(localStart.format(myFormatter));
            selectedAppType = resultSet.getString("type");
            selectedCustomer = resultSet.getInt("customerId");
            selectedUser = resultSet.getInt("userId");

            //System.out.println("Made it to here");

            appType.setValue(AppointmentTypes.Types.valueOf(selectedAppType));

            for (int i = 0; i < customerIdBox.getItems().size(); i++) {
                String x = String.valueOf(customerIdBox.getItems().toArray()[i]);
                String[] itemArray = x.split(":");
                int cuId = Integer.parseInt(itemArray[1].replaceAll("\\s+", ""));
                if(cuId == selectedCustomer){
                    customerIdBox.getSelectionModel().select(i);
                }

            }

            for (int i = 0; i < consultantId.getItems().size(); i++) {
                String x = String.valueOf(consultantId.getItems().toArray()[i]);
                String[] itemArray = x.split(":");
                int cuId = Integer.parseInt(itemArray[1].replaceAll("\\s+", ""));
                if(cuId == selectedUser){
                    consultantId.getSelectionModel().select(i);
                }

            }


        }
        catch (Exception e) {
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

    public void handleSave(ActionEvent event){
        if (!inValidInput()){
            //System.out.println("Invalid input");
        }
        else {
            //System.out.println("Input was correct");
            try {

                Connection conn;
                PreparedStatement ps;
                PreparedStatement ps2;
                ResultSet rs;
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");

                //Update the record in the record in the database
                String query = "update appointment set customerId = ?, userId = ?, title = ?, description = ?, location = ?, contact = ?, " +
                        "type = ?, url = ?, start = ?, end = ?,  lastUpdate = current_timestamp, lastUpdateBy = current_user where appointmentId= ? ";

                //check to make sure that the record we are updating will not cause a double booking
                String query2 = "select start from appointment where (select appointmentId from appointment where appointmentId = ?) and start = ? and userId = ? ";

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


                ps2.setInt(1, appId);
                ps2.setString(2, String.valueOf(startTime));

                //check to make sure that the consultant does not already have an app scheduled for that time
                String id = consultantId.getSelectionModel().getSelectedItem();
                String[] conArray = id.split(":");
                int usId = Integer.parseInt(conArray[1].replaceAll("\\s+", ""));
                ps2.setInt(3, usId);

                rs = ps2.executeQuery();

                if(rs.next()) {
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setTitle("ERROR");
                    a.setContentText("An appointment is already scheduled for the selected time");
                    a.showAndWait();
                }

                else {
                    String item = customerIdBox.getSelectionModel().getSelectedItem();
                    String[] itemArray = item.split(":");
                    int cuId = Integer.parseInt(itemArray[1].replaceAll("\\s+", ""));
                    ps.setInt(1, cuId);

                    ps.setInt(2, usId);
                    ps.setString(3, titleField.getText());
                    ps.setString(4, descArea.getText());
                    ps.setString(5, locationField.getText());
                    ps.setString(6, contactField.getText());
                    ps.setString(7, String.valueOf(appType.getSelectionModel().getSelectedItem()));
                    ps.setString(8, urlField.getText());

                    ps.setString(9, String.valueOf(startTime)); // set the start date timetime
                    ps.setString(10, String.valueOf(endTime)); //set the end date and time
                    ps.setInt(11, appId);

                    ps.executeUpdate();

                    //System.out.println("Record was updated");

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
        tf.textProperty().addListener((observable, oldValue, newValue) -> { //by using a lambda instead of an anonymous  class the code is easier to read
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
               // System.out.println("Limit has been reached");
                ta.setText(oldValue);
                //System.out.println(ta.getText().length());
            }

        });
    }

    public void startDateListener(){
        startTimeBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> { //By using a lamanda here you do not have to create an anyomous class and the code is easier to read
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
