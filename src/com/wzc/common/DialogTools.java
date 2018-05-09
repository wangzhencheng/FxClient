package com.wzc.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class DialogTools {

    public static void alert(Stage primaryStage, String title, String text) {
        Alert alert = new Alert(Alert.AlertType.NONE,
                text, new ButtonType("确定", ButtonBar.ButtonData.YES));
        alert.setTitle(title);
        alert.initOwner(primaryStage);
        alert.show();
    }
}
