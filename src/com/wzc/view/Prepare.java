package com.wzc.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class Prepare implements Initializable {

    @FXML
    private Text text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateText(String str) {
        int maxLen = 40;
        if (str == null) str = "";
        if (str.length() > maxLen) {
            str = "..."+str.substring(str.length() - maxLen + 3) ;
        }
        text.setText(str);
    }
}
