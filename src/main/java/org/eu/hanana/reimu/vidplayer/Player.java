package org.eu.hanana.reimu.vidplayer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.eu.hanana.reimu.vidplayer.player.PlayerController;

import javax.swing.text.LabelView;
import java.io.IOException;
import java.util.Objects;

public class Player extends Application  {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Label label = new Label("加载中!");
        BorderPane root = new BorderPane();
        root.setCenter(label);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("加载中!");
        primaryStage.show();

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource("player_main.fxml")));
        root = loader.load();
        scene = new Scene(root,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JAVAFX-视频播放器");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
    }

}
