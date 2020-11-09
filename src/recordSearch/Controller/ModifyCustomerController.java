package recordSearch.Controller;

import recordSearch.Model.CustomersTable;
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


public class ModifyCustomerController{
    private int customerId;
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
    Alert a = new Alert(Alert.AlertType.WARNING);

    public void handleUpdateCustomer(ActionEvent event) throws IOException{
        if(!validateInput()){
            //System.out.println("Input was WRONG");
        }
        else {
            //Continue with adding input
            //System.out.println("Input was right");
            modifySelectedRecord();
            Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/searchCustomer.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
    public void modifySelectedRecord(){

        try {
            PreparedStatement preparedStatement;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");


            String query = "update\n" +
                    "    U05MhH.customer cu\n" +
                    "    join U05MhH.address a on cu.addressId = a.addressId\n" +
                    "    join U05MhH.city c on a.cityId = c.cityId\n" +
                    "    join U05MhH.country c2 on c.countryId = c2.countryId\n" +
                    "set cu.customerName = ?, cu.active = ?, a.address = ?, a.address2 = ?, a.postalCode = ?,\n" +
                    "    a.phone = ?, c.city = ?, c2.country = ?\n" +
                    "where customerId = ?;";

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, customerNameField.getText());
            if(customerIsActive.isSelected()){
                preparedStatement.setInt(2, 1);
            }
            else{
                preparedStatement.setInt(2, 0);
            }
            preparedStatement.setString(3, customerAddressField.getText());
            preparedStatement.setString(4, customerAddress2Field.getText());
            preparedStatement.setString(5, customerPostalField.getText());
            preparedStatement.setString(6, customerPhoneField.getText());
            preparedStatement.setString(7, customerCityField.getText());
            preparedStatement.setString(8, customerCountryField.getText());
            preparedStatement.setInt(9,customerId );
            preparedStatement.executeUpdate();
            //System.out.println("Made it to here");


        }
        catch (ClassNotFoundException | SQLException e) {
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

    public void handleCancel(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/recordSearch/View/searchCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();


    }

    public void initOldRecord(CustomersTable selectedRecord){
        customerId = Integer.parseInt(selectedRecord.getCustomerId());
        //System.out.println(selectedRecord.getCustomerId());

        try {
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");


            String query = "select customerName, active,  address, address2, postalCode, phone, city, country\n" +
                    "from customer join address a on customer.addressId = a.addressId\n" +
                    "join city c on a.cityId = c.cityId join country c2 on c.countryId = c2.countryId\n" +
                    "where customerId = ?";

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();


            //System.out.println("Made it to here");
            customerNameField.setText(resultSet.getString("customerName"));
            customerAddressField.setText(resultSet.getString("address"));
            customerAddress2Field.setText(resultSet.getString("address2"));
            customerPostalField.setText(resultSet.getString("postalCode"));
            customerPhoneField.setText(resultSet.getString("phone"));
            customerCityField.setText(resultSet.getString("city"));
            customerCountryField.setText(resultSet.getString("country"));
            if(resultSet.getInt("active") == 1){
                customerIsActive.setSelected(true);
            }
            else{
                customerIsActive.setSelected(false);
            }

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

}
