package adStylePlayer.ControlConsole;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestLocalVideoPageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GeneralSettingsPage localVideoPage = new GeneralSettingsPage();

        // 设置场景，尺寸固定
        Scene scene = new Scene(localVideoPage.getRoot(), 700, 500);

        primaryStage.setTitle("测试本地视频页");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
