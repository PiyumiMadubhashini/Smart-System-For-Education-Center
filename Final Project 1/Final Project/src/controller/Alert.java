package controller;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class Alert {
    public void confirmationMsg(String text){
        Image image = new Image("/assest/conf.png");
        Notifications notifications=Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text(text);
        notifications.title("Confirmation");
        notifications.hideAfter(Duration.seconds(3));
        notifications.position(Pos.TOP_CENTER);

        notifications.show();

    }

    public void ErrMsg(String text){
        Image image = new Image("/assest/er.png");
        Notifications notifications=Notifications.create();
        notifications.graphic(new ImageView(image));
        notifications.text(text);
        notifications.title("Warning");
        notifications.hideAfter(Duration.seconds(3));
        notifications.position(Pos.BOTTOM_RIGHT);

        notifications.show();

    }

}
