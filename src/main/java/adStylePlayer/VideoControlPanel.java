package adStylePlayer;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class VideoControlPanel extends VBox {

    private final Button playPauseBtn = new Button();
    private final Button rewindBtn = new Button();
    private final Button forwardBtn = new Button();
    private final Slider bufferSlider = new Slider();
    private final Slider progressSlider = new Slider();
    private final Label timeLabel = new Label("00:00 / 00:00");

    private final PauseTransition hideTransition = new PauseTransition(Duration.seconds(2));
    private final FontIcon playIcon = new FontIcon("fas-play");
    private final FontIcon pauseIcon = new FontIcon("fas-pause");
    private final FontIcon rewindIcon = new FontIcon("fas-backward");
    private final FontIcon forwardIcon = new FontIcon("fas-forward");

    public VideoControlPanel(MediaPlayer player, MediaView mediaView) {
        setSpacing(6);
        setPadding(new Insets(8));
        setAlignment(Pos.BOTTOM_CENTER);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        // 设置图标大小
        int iconSize = 16;
        playIcon.setIconSize(iconSize);
        pauseIcon.setIconSize(iconSize);
        rewindIcon.setIconSize(iconSize);
        forwardIcon.setIconSize(iconSize);

        // 设置按钮图标
        playPauseBtn.setGraphic(pauseIcon);
        rewindBtn.setGraphic(rewindIcon);
        forwardBtn.setGraphic(forwardIcon);

        // 控制按钮行为
        playPauseBtn.setOnAction(e -> {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                player.pause();
                playPauseBtn.setGraphic(playIcon);
            } else {
                player.play();
                playPauseBtn.setGraphic(pauseIcon);
            }
        });

        rewindBtn.setOnAction(e -> player.seek(player.getCurrentTime().subtract(Duration.seconds(5))));
        forwardBtn.setOnAction(e -> player.seek(player.getCurrentTime().add(Duration.seconds(5))));

        // 缓冲条（不可操作）
        bufferSlider.setMin(0);
        bufferSlider.setMax(100);
        bufferSlider.setDisable(true);
        bufferSlider.setStyle("-fx-control-inner-background: rgba(255,255,255,0.2);");

        // 进度条
        progressSlider.setMin(0);
        progressSlider.setMax(100);
        progressSlider.setValue(0);
        progressSlider.setStyle("-fx-accent: white;");
        progressSlider.setMaxWidth(Double.MAX_VALUE);

        // 拖拽进度条跳转
        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging && player.getTotalDuration().greaterThan(Duration.ZERO)) {
                double millis = (progressSlider.getValue() / 100.0) * player.getTotalDuration().toMillis();
                player.seek(Duration.millis(millis));
            }
        });

        // 点击进度条跳转（不影响拖拽跳转）
        progressSlider.setOnMousePressed(e -> {
            if (!progressSlider.isValueChanging() && player.getTotalDuration().greaterThan(Duration.ZERO)) {
                double mouseX = e.getX();
                double width = progressSlider.getWidth();
                double percent = mouseX / width;
                percent = Math.min(Math.max(percent, 0), 1);

                double millis = percent * player.getTotalDuration().toMillis();
                player.seek(Duration.millis(millis));
                progressSlider.setValue(percent * 100);
            }
        });

        // 时间文本样式
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        // 更新时间、进度
        player.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!progressSlider.isValueChanging() && player.getTotalDuration().greaterThan(Duration.ZERO)) {
                double progress = newTime.toMillis() / player.getTotalDuration().toMillis() * 100;
                progressSlider.setValue(progress);
            }
            timeLabel.setText(formatTime(newTime) + " / " + formatTime(player.getTotalDuration()));
        });

        // 缓冲条更新
        player.bufferProgressTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (player.getTotalDuration().greaterThan(Duration.ZERO)) {
                double buffered = newTime.toMillis() / player.getTotalDuration().toMillis() * 100;
                bufferSlider.setValue(buffered);
            }
        });

        // 控制栏隐藏逻辑
        setVisible(true);
        setupMouseControl(this);
        setupMouseControl(mediaView);

        // 添加鼠标事件监听到整个 adStylePlayer.VideoControlPanel 区域和 mediaView
        Runnable addMouseHandlers = () -> {
            Node[] watchNodes = new Node[]{this, mediaView};
            for (Node node : watchNodes) {
                node.addEventFilter(MouseEvent.MOUSE_MOVED, e -> resetHideTimer());
                node.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> resetHideTimer());
                node.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> resetHideTimer());
            }
        };

        addMouseHandlers.run(); // 执行绑定

        // 初始化自动隐藏
        resetHideTimer();


        // 控制按钮行
        HBox controls = new HBox(12, rewindBtn, playPauseBtn, forwardBtn);
        controls.setAlignment(Pos.CENTER);

        // 进度+时间行
        StackPane sliderStack = new StackPane(bufferSlider, progressSlider);
        sliderStack.setAlignment(Pos.CENTER_LEFT);

        HBox progressRow = new HBox(10, sliderStack, timeLabel);
        progressRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(sliderStack, Priority.ALWAYS);

        // 布局组装
        getChildren().addAll(progressRow, controls);
        setMaxWidth(Double.MAX_VALUE);
    }

    private void setupMouseControl(Node node) {
        node.setOnMouseMoved(e -> showControls());
        node.setOnMouseEntered(e -> showControls());

        node.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (node instanceof MediaView mediaView) {
                MediaPlayer player = mediaView.getMediaPlayer();
                double width = mediaView.getBoundsInLocal().getWidth();
                double clickX = e.getX();
                double ratio = clickX / width;

                Duration total = player.getTotalDuration();
                if (total.greaterThan(Duration.ZERO)) {
                    Duration seekTime = total.multiply(ratio);
                    player.seek(seekTime);
                    progressSlider.setValue(ratio * 100);
                }
            }
            showControls();
        });
    }

    void showControls() {
        setVisible(true);
        hideTransition.stop();
        hideTransition.playFromStart();
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void resetHideTimer() {
        setVisible(true);
        hideTransition.stop();
        hideTransition.setOnFinished(e -> setVisible(false));
        hideTransition.setDuration(Duration.seconds(2)); // 无操作 2 秒后隐藏
        hideTransition.playFromStart();
    }

}
