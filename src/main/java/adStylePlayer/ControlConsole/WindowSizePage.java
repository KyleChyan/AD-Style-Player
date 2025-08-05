package adStylePlayer.ControlConsole;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class WindowSizePage {
    private VBox root;
    private TextField widthField;
    private TextField heightField;
    private Label previewLabel;

    public WindowSizePage() {
        root = new VBox(15);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        root.setPadding(new Insets(10));

        Label title = new Label("窗口大小");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        title.setTextFill(Color.BLACK);

        HBox sizeBox = new HBox(10);
        Label widthLabel = new Label("宽:");
        widthField = new TextField("320");
        widthField.setPrefWidth(80);
        Label heightLabel = new Label("高:");
        heightField = new TextField("180");
        heightField.setPrefWidth(80);

        sizeBox.getChildren().addAll(widthLabel, widthField, heightLabel, heightField);

        previewLabel = new Label("预览窗口大小");
        previewLabel.setStyle("-fx-border-color: gray; -fx-padding: 10;");
        updatePreview();

        // 监听输入改变，实时更新预览
        widthField.textProperty().addListener((obs, oldV, newV) -> updatePreview());
        heightField.textProperty().addListener((obs, oldV, newV) -> updatePreview());

        root.getChildren().addAll(title, sizeBox, previewLabel);
    }

    private void updatePreview() {
        try {
            int w = Integer.parseInt(widthField.getText());
            int h = Integer.parseInt(heightField.getText());
            previewLabel.setText("预览窗口大小: " + w + " x " + h);
        } catch (NumberFormatException e) {
            previewLabel.setText("请输入有效数字");
        }
    }

    public VBox getRoot() {
        return root;
    }

    public int getWidthValue() {
        try {
            return Integer.parseInt(widthField.getText());
        } catch (NumberFormatException e) {
            return 320;
        }
    }

    public int getHeightValue() {
        try {
            return Integer.parseInt(heightField.getText());
        } catch (NumberFormatException e) {
            return 180;
        }
    }
}
