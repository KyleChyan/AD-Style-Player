package adStylePlayer.ControlConsole;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlayerControlConsoleApp extends Application {

    private VBox sideBar;
    private StackPane contentPane;
    private ToggleGroup toggleGroup = new ToggleGroup();

    // 三个页面
    private LocalVideoPage localVideoPage;
    private WindowSizePage windowSizePage;
    private GeneralSettingsPage generalSettingsPage;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("启动控制台");

        contentPane = new StackPane();
        contentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        contentPane.setPadding(new Insets(20));

        sideBar = createSidebar();

        localVideoPage = new LocalVideoPage();
        windowSizePage = new WindowSizePage();
        generalSettingsPage = new GeneralSettingsPage();

        contentPane.getChildren().addAll(
                localVideoPage.getRoot(),
                windowSizePage.getRoot(),
                generalSettingsPage.getRoot()
        );

        // 初始隐藏所有页面，确保可控
        contentPane.getChildren().forEach(node -> node.setVisible(false));

        // 默认显示第一个
        switchContent(0);
        toggleGroup.getToggles().get(0).setSelected(true);

        BorderPane root = new BorderPane();
        root.setLeft(sideBar);
        root.setCenter(contentPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AD Style Player 控制台");
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox box = new VBox();
        box.setPrefWidth(180);
        box.setStyle("-fx-background-color: white;");

        String[] names = {"本地视频", "窗口大小", "通用设置"};

        for (int i = 0; i < names.length; i++) {
            final int idx = i;
            ToggleButton btn = new ToggleButton(names[i]);
            btn.setToggleGroup(toggleGroup);
            btn.setUserData(i);
            btn.setMinWidth(Double.MAX_VALUE);
            btn.setPadding(new Insets(10, 15, 10, 20));
            btn.setFont(Font.font("System", 14));
            btn.setTextFill(Color.web("#444444"));
            btn.setBackground(Background.EMPTY);
            btn.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            btn.selectedProperty().addListener((obs, oldV, newV) -> {
                if (newV) {
                    System.out.println("切换到页面: " + names[idx]);
                    btn.setTextFill(Color.WHITE);
                    btn.setBackground(new Background(new BackgroundFill(
                            Color.web("#2E86FF"), new CornerRadii(12), Insets.EMPTY)));
                    btn.setStyle("-fx-background-insets: 0, 0; -fx-background-color: linear-gradient(to right, white 0%, #2E86FF 100%);");
                    switchContent(idx);
                } else {
                    btn.setTextFill(Color.web("#444444"));
                    btn.setBackground(Background.EMPTY);
                    btn.setStyle("");
                }
            });

            box.getChildren().add(btn);
        }

        // 底部信息
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label info1 = new Label("AD Style Player");
        Label info2 = new Label("KyleChyan-Github");
        Label info3 = new Label("V1.0.1");

        info1.setPadding(new Insets(5, 10, 0, 10));
        info2.setPadding(new Insets(2, 10, 0, 10));
        info3.setPadding(new Insets(2, 10, 10, 10));

        info1.setFont(Font.font(12));
        info2.setFont(Font.font(12));
        info3.setFont(Font.font(12));

        info1.setTextFill(Color.GRAY);
        info2.setTextFill(Color.GRAY);
        info3.setTextFill(Color.GRAY);

        box.getChildren().addAll(spacer, info1, info2, info3);

        return box;
    }

    private void switchContent(int idx) {
        System.out.println("显示页面索引: " + idx);
        for (int i = 0; i < contentPane.getChildren().size(); i++) {
            boolean visible = (i == idx);
            contentPane.getChildren().get(i).setVisible(visible);
            System.out.println("  页面" + i + " 可见: " + visible);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
