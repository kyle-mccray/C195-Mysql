package recordSearch.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import homeScreen.Model.ValidInputException;

import java.io.IOException;
import java.sql.*;

public class AddCustomerController {

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TextField customerAddress2Field;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private TextField customerCityField;
    @FXML
    private TextField customerPostalField;
    @FXML
    private TextField customerCountryField;
    @FXML
    private CheckBox customerIsActive;

    public Alert a = new Alert(Alert.AlertType.WARNING);


    public void handleCancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/searchCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void handleNewCustomer(ActionEvent event) throws IOException{
        if(!validateInput()){
            //if input was wrong do nothing
        }
        else {
            //Continue with adding input

            addRecord();
            Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/searchCustomer.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }

    }
    public void addRecord(){
        try{
            PreparedStatement country;
            PreparedStatement city;
            PreparedStatement address;
            PreparedStatement customer;

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn =DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");

            String query = "insert into country(country, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + "values (?, now(), current_user, current_timestamp, current_user)";

            String query2 = "insert into city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "values (?, last_insert_id(), now(), current_user, current_timestamp, current_user)";

            String query3 = "insert into address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "values (?, ?, last_insert_id(), ?, ? , now(), current_user, current_timestamp, current_user)" ;

            String query4 = "insert into customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "values (?, last_insert_id(), ?, now(), current_user, current_timestamp, current_user)";






            country = conn.prepareStatement(query);
            country.setString(1, customerCountryField.getText());
            country.executeUpdate();

            city = conn.prepareStatement(query2);
            city.setString(1, customerCityField.getText());
            city.executeUpdate();

            address = conn.prepareStatement(query3);
            address.setString(1, customerAddressField.getText());
            address.setString(2, customerAddress2Field.getText());
            address.setString(3, customerPostalField.getText());
            address.setString(4, customerPhoneField.getText());
            address.executeUpdate();

            customer = conn.prepareStatement(query4);
            customer.setString(1, customerNameField.getText());
            if(customerIsActive.isSelected()){
                customer.setInt(2, 1);
            }
            else {
                customer.setInt(2, 0);
            }
            customer.executeUpdate();

        }

        catch(Exception e){
            e.printStackTrace();

        }





    }

    public boolean validateInput(){
        boolean nameFieldWasWrong = false;
        boolean addressFieldWasWrong = false;
        boolean address2FieldWasWrong = false;
        boolean postFieldWasWrong = false;
        boolean phoneFieldWasWrong = false;
        boolean cityFieldWasWrong = false;
        boolean countryFieldWasWrong = false;

        ValidInputException o = new ValidInputException();
        //If the exception does not return true clear the field
        if(!o.verifyVarChar45(customerNameField.getText())){
            customerNameField.clear();
            nameFieldWasWrong = true;
        }
        if(!o.verifyVarChar50(customerAddressField.getText())){
            customerAddressField.clear();
            addressFieldWasWrong = true;
        }
        if(!o.verifyVarChar50withNull(customerAddress2Field.getText())){
            customerAddress2Field.clear();
            address2FieldWasWrong = true;
        }
        if(!o.verifyVarChar10(customerPostalField.getText())){
            customerPostalField.clear();
            postFieldWasWrong = true;
        }
        if(!o.verifyVarChar20(customerPhoneField.getText())){
            customerPhoneField.clear();
            phoneFieldWasWrong = true;
        }
        if(!o.verifyVarChar50(customerCityField.getText())){
            customerCityField.clear();
            cityFieldWasWrong = true;
        }
        if(!o.verifyVarChar50(customerCountryField.getText())){
            customerCountryField.clear();
            countryFieldWasWrong = true;
        }
        
        if(nameFieldWasWrong || addressFieldWasWrong || address2FieldWasWrong || postFieldWasWrong
                || phoneFieldWasWrong || cityFieldWasWrong || countryFieldWasWrong){
            a.setContentText("The fields with invalid input have been cleared");
            a.showAndWait();

            return false;


            
        }


    return true;

    }

}
