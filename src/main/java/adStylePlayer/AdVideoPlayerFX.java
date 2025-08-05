package adStylePlayer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class AdVideoPlayerFX extends Application {

    private int windowWidth = 300;
    private int windowHeight = 180;
    private int playLogic = 0;
    private File selectedFolder = null;
    private Color topBarColor = Color.web("#5FB6D9");
    private Color backgroundColor = Color.web("#F5F5F5");
    private Color borderColor = Color.web("#DCDCDC");

    private String muteShortcut = "M";
    private boolean isMuted = false;

    private int autoCloseSeconds = 5;


    private double xOffset = 0;
    private double yOffset = 0;

    private double xResizeOffset = 0;
    private double yResizeOffset = 0;

    private boolean isResizing = false;

    private static final int RESIZE_MARGIN = 8;

    private enum ResizeDirection {
        NONE, LEFT, RIGHT, TOP, BOTTOM,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    private ResizeDirection resizeDir = ResizeDirection.NONE;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);

        // 标题栏
        Label titleLabel = new Label("114新闻网");
        titleLabel.setFont(Font.font(14));
        Button closeBtn = new Button("X");
        closeBtn.setOnAction(e -> primaryStage.close());
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: gray; -fx-font-weight: bold;");
        closeBtn.setFocusTraversable(false);

//        HBox titleBar = new HBox(titleLabel, new Region(), closeBtn);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label adLabel = new Label("广告");
        adLabel.setTextFill(Color.web("#800020"));  // 酒红色
//        adLabel.setTextFill(Color.web("#f2cc13"));  // 酒黄色
        adLabel.setStyle("-fx-font-weight: bold;"); // 加粗
        adLabel.setAlignment(Pos.CENTER_RIGHT);
        adLabel.setPadding(new Insets(0, 10, 0, 10)); // 适当左右间距

        HBox titleBar = new HBox(titleLabel, spacer, adLabel, closeBtn);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(5, 10, 5, 10));

        HBox.setHgrow(titleBar.getChildren().get(1), Priority.ALWAYS);
        titleBar.setPadding(new Insets(5, 10, 5, 10));
        titleBar.setAlignment(Pos.CENTER_LEFT);
//        titleBar.setStyle("-fx-cursor: move;");//窗口上边栏样式
//        titleBar.setStyle("-fx-cursor: move; -fx-background-color: #29A0CA;");
        titleBar.setStyle(
                "-fx-cursor: move; " +
                        "-fx-background-color: #5FB6D9; " +
                        "-fx-background-radius: 15 15 0 0; " +
                        "-fx-border-radius: 15 15 0 0; " +
                        "-fx-border-color: transparent;"
        );


        // 视频播放区域
        String videoPath = "/Users/kylemac/Pictures/1457213813-1-192.mp4";
        Media media = new Media(new File(videoPath).toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        mediaView.setFitWidth(300);
        mediaView.setFitHeight(180);

        StackPane videoPane = new StackPane(mediaView);
        videoPane.setStyle("-fx-background-color: black;");
        player.setAutoPlay(true);

        VideoControlPanel controlPanel = new VideoControlPanel(player, mediaView);
        controlPanel.setVisible(true);  // 初始显示

        StackPane videoStack = new StackPane(mediaView, controlPanel);
        StackPane.setAlignment(controlPanel, Pos.BOTTOM_CENTER);

        videoStack.setOnMouseClicked(e -> {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                player.pause();
            } else {
                player.play();
            }
            controlPanel.showControls();
        });


        // 广告内容
        Label contentLabel = new Label("「绘夏!烈日?度假村!」现已推出!《原神》是由米哈游自研的一款开放世界冒险RPG。你将在游戏中探索一个被称作「提瓦特」的幻想世界。");
        contentLabel.setFont(Font.font(12));
        contentLabel.setWrapText(true);
        contentLabel.setPrefHeight(60);
        contentLabel.setMaxWidth(Double.MAX_VALUE);

        // 下载按钮
        Button downloadBtn = new Button("立即下载");
        downloadBtn.setMaxWidth(Double.MAX_VALUE);
        downloadBtn.setStyle("-fx-background-color: #00CC00; -fx-text-fill: white; -fx-font-weight: bold;");
        downloadBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "模拟下载链接打开");
            alert.showAndWait();
        });

        VBox bottomBox = new VBox(contentLabel, downloadBtn);
        bottomBox.setSpacing(5);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER_LEFT);

//        VBox mainLayout = new VBox(titleBar, videoPane, bottomBox);
        VBox mainLayout = new VBox(titleBar, videoStack, bottomBox);
//        mainLayout.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");//窗口主布局设置


        mainLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY)));//窗口圆角
        mainLayout.setEffect(new DropShadow(10, Color.gray(0, 0.3)));
//        mainLayout.setStyle("-fx-border-color: red; -fx-border-width: 4px; -fx-border-radius: 15px; -fx-background-radius: 15px;");//红色圈

        // 添加闪烁动画
/*
        final Background redBackground = new Background(new BackgroundFill(Color.RED, new CornerRadii(15), Insets.EMPTY));
        final Background yellowBackground = new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(15), Insets.EMPTY));

        Timeline blinkTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> mainLayout.setBackground(redBackground)),
                new KeyFrame(Duration.millis(500), e -> mainLayout.setBackground(yellowBackground))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.setAutoReverse(true);
        blinkTimeline.play();
        */


        Scene scene = new Scene(mainLayout, 310, 320);//窗口大小-宽-长
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);

        // 标题栏拖动窗口
        titleBar.setOnMousePressed(event -> {
            if (resizeDir == ResizeDirection.NONE) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        titleBar.setOnMouseDragged(event -> {
            if (resizeDir == ResizeDirection.NONE) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        // 窗口拖拽缩放监听
        mainLayout.setOnMouseMoved(event -> {
            ResizeDirection dir = getResizeDirection(primaryStage, event);
            Cursor cursor = getCursorForDirection(dir);
            mainLayout.setCursor(cursor);
            resizeDir = dir;
        });

        mainLayout.setOnMousePressed(event -> {
            isResizing = (resizeDir != ResizeDirection.NONE);
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainLayout.setOnMouseDragged(event -> {
            double mouseX = event.getScreenX();
            double mouseY = event.getScreenY();
            double stageX = primaryStage.getX();
            double stageY = primaryStage.getY();
            double stageWidth = primaryStage.getWidth();
            double stageHeight = primaryStage.getHeight();

            if (isResizing) {
                double minWidth = 200;
                double minHeight = 150;

                switch (resizeDir) {
                    case RIGHT:
                        double newWidth = mouseX - stageX;
                        if (newWidth >= minWidth) primaryStage.setWidth(newWidth);
                        break;
                    case BOTTOM:
                        double newHeight = mouseY - stageY;
                        if (newHeight >= minHeight) primaryStage.setHeight(newHeight);
                        break;
                    case BOTTOM_RIGHT:
                        newWidth = mouseX - stageX;
                        newHeight = mouseY - stageY;
                        if (newWidth >= minWidth) primaryStage.setWidth(newWidth);
                        if (newHeight >= minHeight) primaryStage.setHeight(newHeight);
                        break;
                    case LEFT:
                        newWidth = stageWidth - (mouseX - stageX);
                        if (newWidth >= minWidth) {
                            primaryStage.setX(mouseX);
                            primaryStage.setWidth(newWidth);
                        }
                        break;
                    case TOP:
                        newHeight = stageHeight - (mouseY - stageY);
                        if (newHeight >= minHeight) {
                            primaryStage.setY(mouseY);
                            primaryStage.setHeight(newHeight);
                        }
                        break;
                    case TOP_LEFT:
                        newWidth = stageWidth - (mouseX - stageX);
                        newHeight = stageHeight - (mouseY - stageY);
                        if (newWidth >= minWidth) {
                            primaryStage.setX(mouseX);
                            primaryStage.setWidth(newWidth);
                        }
                        if (newHeight >= minHeight) {
                            primaryStage.setY(mouseY);
                            primaryStage.setHeight(newHeight);
                        }
                        break;
                    case TOP_RIGHT:
                        newWidth = mouseX - stageX;
                        newHeight = stageHeight - (mouseY - stageY);
                        if (newWidth >= minWidth) primaryStage.setWidth(newWidth);
                        if (newHeight >= minHeight) {
                            primaryStage.setY(mouseY);
                            primaryStage.setHeight(newHeight);
                        }
                        break;
                    case BOTTOM_LEFT:
                        newWidth = stageWidth - (mouseX - stageX);
                        newHeight = mouseY - stageY;
                        if (newWidth >= minWidth) {
                            primaryStage.setX(mouseX);
                            primaryStage.setWidth(newWidth);
                        }
                        if (newHeight >= minHeight) primaryStage.setHeight(newHeight);
                        break;
                    default:
                        break;
                }
            } else {
                // 拖动窗口
                primaryStage.setX(mouseX - xOffset);
                primaryStage.setY(mouseY - yOffset);
            }
        });

        primaryStage.show();
    }

    private ResizeDirection getResizeDirection(Stage stage, MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();
        double width = stage.getWidth();
        double height = stage.getHeight();

        boolean left = mouseX >= 0 && mouseX < RESIZE_MARGIN;
        boolean right = mouseX <= width && mouseX > width - RESIZE_MARGIN;
        boolean top = mouseY >= 0 && mouseY < RESIZE_MARGIN;
        boolean bottom = mouseY <= height && mouseY > height - RESIZE_MARGIN;

        if (left && top) return ResizeDirection.TOP_LEFT;
        if (left && bottom) return ResizeDirection.BOTTOM_LEFT;
        if (right && top) return ResizeDirection.TOP_RIGHT;
        if (right && bottom) return ResizeDirection.BOTTOM_RIGHT;
        if (left) return ResizeDirection.LEFT;
        if (right) return ResizeDirection.RIGHT;
        if (top) return ResizeDirection.TOP;
        if (bottom) return ResizeDirection.BOTTOM;
        return ResizeDirection.NONE;
    }

    private Cursor getCursorForDirection(ResizeDirection dir) {
        switch (dir) {
            case TOP_LEFT:
            case BOTTOM_RIGHT:
                return Cursor.NW_RESIZE;
            case TOP_RIGHT:
            case BOTTOM_LEFT:
                return Cursor.NE_RESIZE;
            case TOP:
            case BOTTOM:
                return Cursor.V_RESIZE;
            case LEFT:
            case RIGHT:
                return Cursor.H_RESIZE;
            default:
                return Cursor.DEFAULT;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
