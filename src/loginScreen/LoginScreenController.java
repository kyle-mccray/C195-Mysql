package loginScreen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginScreenController implements Initializable {
    @FXML Label userNameLabel;
    @FXML Label passwordLabel;
    @FXML Button signInButton;
    @FXML Label welcomeMessage;
    @FXML TextField userNameField;
    @FXML PasswordField passWordField;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    public Locale getRegion(){
       Locale currentLocale = Locale.getDefault();
       //Locale currentLocale = Locale.FRANCE;

    return currentLocale;
    }

    public void handleLoginUser(ActionEvent event) throws SQLException, IOException {
        String usrName = userNameField.getText();
        String passWord = passWordField.getText();
        usrName = usrName.replaceAll("\\s", "`");
        passWord = passWord.replaceAll("\\s", "`");


        Connection conn;
        PreparedStatement ps;
        ResultSet rs;

        try{
            Class.forName("com.mysql.jdbc.Driver");


            conn = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U05MhH", "U05MhH", "53688542808");
            String query = "select * from user where userName = ? and password=?";

            ps = conn.prepareStatement(query);
            ps.setString(1, usrName);
            ps.setString(2, passWord);
            rs = ps.executeQuery();
            if(!rs.next()) {
                //if no record id found with the supplied username and password show error message and clear password fields
                alert.showAndWait();
                userNameField.clear();
                passWordField.clear();
                } else {

                    //System.out.println("Information was found logging to file");
                    logToFile(usrName, passWord);
                    Parent root = FXMLLoader.load(getClass().getResource("/homeScreen/View/MainScreen.fxml"));
                    Scene scene = new Scene(root);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }


        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void logToFile(String userName, String password) throws IOException {
        String logName = userName+"+"+password+".txt";
        Path currentDir = FileSystems.getDefault().getPath("userLogs");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String log = "Username: " + userName + " logged in at " + timestamp + '\n';

        if(!Files.exists(currentDir)) {
            Files.createDirectory(currentDir);
            File myFile = new File(currentDir + "/" + logName);
            PrintWriter writer = new PrintWriter(myFile);
            writer.println(log);
            writer.close();
        }
        else{
            FileWriter fileWriter = new FileWriter(currentDir + "/" + logName, true);
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.append(log);
            writer.close();
        }

    }

    public void initialize(URL url, ResourceBundle rb){
        ResourceBundle messages = ResourceBundle.getBundle("loginScreen.MessagesBundle", getRegion());
        userNameLabel.setText(messages.getString("username"));
        passwordLabel.setText(messages.getString("password"));
        signInButton.setText(messages.getString("signIn"));
        welcomeMessage.setText(messages.getString("welcomeMessage"));
        alert.setContentText(messages.getString("errorMessage"));
        alert.setTitle(messages.getString("warningTitle"));
        alert.setHeaderText(messages.getString("warningType"));




    }

}
