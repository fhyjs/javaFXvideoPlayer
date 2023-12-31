package org.eu.hanana.reimu.vidplayer;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Utils {
    public static void addResizeEvent(Pane primaryStage, onResize l){
        // 监听舞台大小变化事件
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            l.resize(primaryStage.getWidth(), primaryStage.getHeight());
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            l.resize(primaryStage.getWidth(), primaryStage.getHeight());
        });
    }
    public interface onResize{
        void resize(double w, double h);
    }
}
