package reportsScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML private ListView<String> appTypes;
    @FXML private ListView<String> customersByCity;
    @FXML private ListView<String> usersInSystem;



    public void handleMainScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/homeScreen/View/MainScreen.fxml"));
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

    public void handleAppByMonth(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/appointments/View/appointmentsByMonth.fxml"));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initReports();


    }

    public void initReports() {
        try {
            Connection conn;
            PreparedStatement ps;
            PreparedStatement ps2;
            PreparedStatement ps3;
            ResultSet rs;
            ResultSet rs2;
            ResultSet rs3;
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");


            String query = "SELECT city, count(city) as number from customer join address a on customer.addressId = a.addressId join city c on a.cityId = c.cityId group by city";


            String query2 = "select count(*) as Number, type from appointment where month(start) = month(current_date()) " +
                    " and year(start) = year(current_date()) group by type";

            String query3 = "Select userId, userName from user";

            ps = conn.prepareStatement(query);
            ps2 = conn.prepareStatement(query2);
            ps3 = conn.prepareStatement(query3);
            rs = ps.executeQuery();
            rs2 = ps2.executeQuery();
            rs3 = ps3.executeQuery();
            while(rs.next()){
               String city = rs.getString("city");
               String number = rs.getString("number");
               customersByCity.getItems().add(number + ": " + city);
            }

            while(rs2.next()){
                String count = rs2.getString("Number");
                String type = rs2.getString("type");
                appTypes.getItems().add(count + ": " + type);
            }

            while(rs3.next()){
                String userId = rs3.getString("userId");
                String userName = rs3.getString("userName");
                usersInSystem.getItems().add(userName + " with id: " + userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSchedule(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/reportsScreen/userSchedule.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        UserScheduleController controller = loader.getController();
        String selectedItem = usersInSystem.getSelectionModel().getSelectedItem();
        if(usersInSystem.getSelectionModel().getSelectedItem() == null){
            //do nothing
        }
        else{
            String[] x = selectedItem.split(":");
            int userId = Integer.parseInt(x[1].replaceAll("\\s+", ""));
            controller.initData(userId);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }



    }

}
