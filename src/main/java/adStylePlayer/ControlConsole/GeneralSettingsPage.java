package adStylePlayer.ControlConsole;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GeneralSettingsPage {
    private VBox root;

    private ColorPicker topBarColorPicker;
    private ColorPicker bgColorPicker;
    private ColorPicker borderColorPicker;
    private TextField autoCloseField;
    private Button muteButton;

    public GeneralSettingsPage() {
        root = new VBox(15);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        root.setPadding(new Insets(10));

        Label title = new Label("通用设置");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        title.setTextFill(Color.BLACK);

        Label styleLabel = new Label("样式设置");
        topBarColorPicker = new ColorPicker(Color.web("#FCE806"));
        bgColorPicker = new ColorPicker(Color.WHITE);
        borderColorPicker = new ColorPicker(Color.RED);

        VBox colorsBox = new VBox(5,
                new HBox(10, new Label("顶部栏颜色:"), topBarColorPicker),
                new HBox(10, new Label("背景颜色:"), bgColorPicker),
                new HBox(10, new Label("边框颜色:"), borderColorPicker)
        );

        Label timerLabel = new Label("定时关闭（秒）");
        autoCloseField = new TextField("0");
        autoCloseField.setPromptText("0表示不定时");

        muteButton = new Button("一键静音");

        Hyperlink githubLink = new Hyperlink("访问 Github");
        githubLink.setOnAction(e -> {
            // 打开浏览器
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/KyleChyan"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(title, styleLabel, colorsBox, timerLabel, autoCloseField, muteButton, githubLink);
    }

    public VBox getRoot() {
        return root;
    }

    public Color getTopBarColor() {
        return topBarColorPicker.getValue();
    }

    public Color getBackgroundColor() {
        return bgColorPicker.getValue();
    }

    public Color getBorderColor() {
        return borderColorPicker.getValue();
    }

    public int getAutoCloseSeconds() {
        try {
            return Integer.parseInt(autoCloseField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getMuteShortcut() {
        // 这里示例没绑定快捷键，返回空
        return "";
    }
}
