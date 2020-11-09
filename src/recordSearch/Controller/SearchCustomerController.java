package recordSearch.Controller;

import recordSearch.Model.CustomersTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SearchCustomerController implements Initializable {

    public ObservableList<CustomersTable> customersObsList = FXCollections.observableArrayList();
    @FXML
    private TableView<CustomersTable> customerTableView;
    @FXML
    private TableColumn<CustomersTable, String> customerNameCol;
    @FXML
    private TableColumn<CustomersTable, String> customerAddressCol;
    @FXML
    private TableColumn<CustomersTable, String> customerPhoneCol;
    @FXML
    private TextField searchCustomerField;

    @Override
    public void initialize(URL url, ResourceBundle rb){

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
            String query = "select customerName, address, address2, phone, customerId\n" +
                    "from customer join address on customer.addressId = address.addressId;";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while(rs.next()){
                String address = rs.getString("address") + " " + rs.getString("address2");
                CustomersTable o = new CustomersTable(rs.getString("customerName"), address , rs.getString("Phone") , rs.getString("customerId"));
                customersObsList.addAll(o);

            }

            customerNameCol.setCellValueFactory((new PropertyValueFactory<>("customerName")));
            customerAddressCol.setCellValueFactory((new PropertyValueFactory<>("address")));
            customerPhoneCol.setCellValueFactory((new PropertyValueFactory<>("customerPhone")));

            FilteredList<CustomersTable> filteredCustomerTable = new FilteredList<>(customersObsList, p ->true);

            searchCustomerField.textProperty().addListener((observable, oldValue, newValue) -> filteredCustomerTable.setPredicate(CustomersTable -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(CustomersTable.getCustomerName()).toLowerCase().contains(lowerCaseFilter)){
                    //filter matches customerName
                    return true;
                }
                else if (String.valueOf(CustomersTable.getAddress()).toLowerCase().contains(lowerCaseFilter)) {
                    //filter matches address
                    return true;
                }
                else if (String.valueOf(CustomersTable.getCustomerPhone()).toLowerCase().contains(lowerCaseFilter)){
                    //filter matches customerPhone
                    return true;
                }
                return false;
            }));

            SortedList<CustomersTable> sortedCustomerData = new SortedList<>(filteredCustomerTable);

            sortedCustomerData.comparatorProperty().bind(customerTableView.comparatorProperty());
            customerTableView.setItems(sortedCustomerData);

        }
        catch (Exception e){
            System.err.print(e.getMessage());


        }

    }

    public void handleAddCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/addCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();



    }

    public void handleModifyCustomer(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/recordSearch/View/ModifyCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        ModifyCustomerController controller = loader.getController();
        CustomersTable selectedItem = customerTableView.getSelectionModel().getSelectedItem();
        controller.initOldRecord(selectedItem);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();




    }

    public void handleDeleteCustomer(ActionEvent event) {
        CustomersTable selectedRecord = customerTableView.getSelectionModel().getSelectedItem();
        deleteCustomerRecord(selectedRecord);
        customersObsList.remove(selectedRecord);
    }

    public void deleteCustomerRecord(CustomersTable selectedRecord){

        try {
            if(selectedRecord != null) {
                PreparedStatement preparedStatement;
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
                String query = "delete cu, a, c, c2\n" +
                        "    from customer cu join address a on cu.addressId = a.addressId\n" +
                        "    join city c on a.cityId = c.cityId\n" +
                        "    join country c2 on c.countryId = c2.countryId\n" +
                        "where customerId=?";

                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(selectedRecord.getCustomerId()));
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





}
