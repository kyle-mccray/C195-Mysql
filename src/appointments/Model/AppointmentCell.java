package appointments.Model;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import homeScreen.Model.Appointment;

public class AppointmentCell extends TableCell<Appointment, String> {

    public AppointmentCell(){}

    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item, empty);

        if (item == null || item.isEmpty()){
            super.setText(null);
            super.setGraphic(null);
        } else {
            super.setText(null);
            Label l = new Label(item);
            l.setWrapText(true);
            VBox box = new VBox(l);
            l.heightProperty().addListener((observable,oldValue,newValue)-> {
                box.setPrefHeight(newValue.doubleValue()+7);
                Platform.runLater(()->this.getTableRow().requestLayout());
            });
            super.setGraphic(box);
        }
    }

}
