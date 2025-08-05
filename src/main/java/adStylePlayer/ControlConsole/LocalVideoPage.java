package adStylePlayer.ControlConsole;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class LocalVideoPage {
    private VBox root;

    private File selectedFolder;
    private ListView<String> videoListView = new ListView<>();
    private ToggleGroup playLogicGroup = new ToggleGroup();

    public LocalVideoPage() {
        root = new VBox(10);
        root.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, null, null)));
        root.setPadding(new Insets(10));
        root.setPrefSize(600, 400);  // 设个固定大小方便测试

        Label title = new Label("本地视频");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        title.setTextFill(javafx.scene.paint.Color.BLACK);

        Button chooseFolderBtn = new Button("选择本地文件夹");
        Label folderLabel = new Label("未选择文件夹");
        folderLabel.setWrapText(true);
        folderLabel.setMaxWidth(580);
        folderLabel.setStyle("-fx-border-color: gray; -fx-padding: 5;");

        chooseFolderBtn.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("选择视频文件夹");
            File folder = chooser.showDialog(null);
            if (folder != null && folder.isDirectory()) {
                selectedFolder = folder;
                folderLabel.setText(folder.getAbsolutePath());
                scanVideos(folder);
            }
        });

        videoListView.setPrefHeight(200);
        videoListView.setStyle("-fx-border-color: black;");

        // 播放逻辑单选按钮
        RadioButton rbSeq = new RadioButton("顺序播放");
        rbSeq.setToggleGroup(playLogicGroup);
        rbSeq.setSelected(true);
        RadioButton rbRand = new RadioButton("随机播放");
        rbRand.setToggleGroup(playLogicGroup);
        RadioButton rbLoop = new RadioButton("单个循环播放");
        rbLoop.setToggleGroup(playLogicGroup);

        HBox playLogicBox = new HBox(15, rbSeq, rbRand, rbLoop);
        playLogicBox.setPadding(new Insets(10, 0, 0, 0));
        playLogicBox.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");

        root.getChildren().addAll(title, chooseFolderBtn, folderLabel, videoListView, new Label("播放逻辑"), playLogicBox);
    }

    private void scanVideos(File folder) {
        File[] files = folder.listFiles(f -> {
            String n = f.getName().toLowerCase();
            return f.isFile() && (n.endsWith(".mp4") || n.endsWith(".avi") || n.endsWith(".mkv") || n.endsWith(".mov"));
        });
        videoListView.getItems().clear();
        if (files != null) {
            for (File f : files) videoListView.getItems().add(f.getName());
        }
    }

    public VBox getRoot() {
        return root;
    }

    public File getSelectedFolder() {
        return selectedFolder;
    }

    public String getPlayLogic() {
        RadioButton sel = (RadioButton) playLogicGroup.getSelectedToggle();
        return sel != null ? sel.getText() : "顺序播放";
    }
}
