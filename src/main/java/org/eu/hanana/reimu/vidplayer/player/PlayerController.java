package org.eu.hanana.reimu.vidplayer.player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.eu.hanana.reimu.vidplayer.Utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {
    public MediaPlayer mediaPlayer;
    public MediaView mediaView;
    public Pane videoPane;
    public Button btnPlay;
    public Button btnStop;
    public Slider playProgress;
    public ToolBar toolBar;
    public HBox btnBox;
    private boolean updPlayProgress=true;
    private int seekTime;

    @FXML
    public void handlePlayButtonAction(ActionEvent event) {
        if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING){
            mediaPlayer.play();
            reFlashAll(true);
        }else {
            mediaPlayer.pause();
            reFlashAll(false);
        }
    }
    @FXML
    public void onStopBtnAction(ActionEvent event) {
        mediaPlayer.stop();

        reFlashAll(false);
    }
    private Thread seeker;
    public void playProgressDrag(MouseEvent mouseDragEvent) {
        if (mouseDragEvent.getEventType() == MouseDragEvent.DRAG_DETECTED) {
            updPlayProgress = false;
            seeker = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(10);
                        seekTime--;
                    } catch (InterruptedException ignored) {

                    }
                }
            });
            seeker.start();
        }
        if (mouseDragEvent.getEventType() == MouseDragEvent.MOUSE_DRAGGED) {
            if (seekTime<=0) {
                mediaPlayer.seek(Duration.millis(playProgress.getValue() / Double.MAX_VALUE * mediaPlayer.getTotalDuration().toMillis()));
                seekTime=30;
            }
        }
        if (mouseDragEvent.getEventType() == MouseDragEvent.MOUSE_RELEASED) {
            updPlayProgress = true;
            seeker.interrupt();
        }

    }
    public void reFlashAll(boolean isPlaying){
        if (isPlaying){
            btnPlay.setText("暂停");
        }else {
            btnPlay.setText("播放");
        }

        // 设置MediaView的大小
        mediaView.setFitWidth(videoPane.getWidth()); // 设置宽度
        mediaView.setFitHeight(videoPane.getHeight()); // 设置高度

        playProgress.setPrefWidth(videoPane.widthProperty().get()-btnBox.getWidth()-40);
    }
    private void onTimeChange(double currentTime) {
        if (updPlayProgress)
            playProgress.setValue(currentTime/mediaPlayer.getTotalDuration().toMillis()*Double.MAX_VALUE);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File mediaFile = new File("C:\\Users\\a\\Downloads\\Video\\av12.mp4");
        Media media = null;
        try {
            media = new Media(mediaFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);

        // 使用StackPane来居中显示MediaView
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        videoPane.getChildren().add(stackPane);
        stackPane.getChildren().add(mediaView);
        playProgress.setMax(Double.MAX_VALUE);


        Utils.addResizeEvent(videoPane, (w, h) -> {
            reFlashAll(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING);
        });
        mediaPlayer.setOnError(() -> {

        });
        mediaPlayer.setOnEndOfMedia(()->{
            mediaPlayer.pause();
            reFlashAll(false);
        });
        // 添加当前时间变化的监听器
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            // 处理当前时间变化的逻辑
            double currentTime = newValue.toMillis();
            onTimeChange(currentTime);
        });
        playProgress.addEventHandler(MouseEvent.ANY, this::playProgressDrag);

        reFlashAll(false);
    }

}
