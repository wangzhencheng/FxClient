package com.wzc;

import com.sun.glass.ui.Screen;
import com.sun.javafx.tk.Toolkit;
import com.wzc.common.ConfigTools;
import com.wzc.common.ResourceLoader;
import com.wzc.view.Home;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    private Stage primaryStage;
    private Home home;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("start");
        this.primaryStage = primaryStage;


        ResourceLoader.FxmlInfo fxmlInfo = ResourceLoader.loadFxml("/com/wzc/view/home.fxml");
        Parent root = fxmlInfo.getRoot();
        home = fxmlInfo.getController();
        home.setPrimaryStage(primaryStage);

        primaryStage.setTitle(ConfigTools.getConfig().getServiceName());
        //设置窗口的图标.
        primaryStage.getIcons().add(new Image("/resource/logo.png"));

        Screen primaryScreen = (Screen) Toolkit.getToolkit().getPrimaryScreen();
        int width = (int) (primaryScreen.getVisibleWidth() * 0.8);
        int height = (int) ((primaryScreen.getVisibleHeight() - 28) * 0.8);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                String closeType = ConfigTools.getConfig().getCloseType();
                if (closeType != null && "confirm".equals(closeType)) {
                    event.consume();
                    //询问是否彻底关闭
                    confirmClose();
                }
            }
        });

    }

    private void confirmClose() {
        Alert _alert = new Alert(Alert.AlertType.NONE,
                "是否关闭?",
                new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("退出", ButtonBar.ButtonData.YES));
//        设置窗口的标题
        _alert.setTitle("确认");
//        _alert.setHeaderText(p_header);
//        设置对话框的 icon 图标，参数是主窗口的 stage
        _alert.initOwner(this.primaryStage);
//        showAndWait() 将在对话框消失以前不会执行之后的代码
        Optional<ButtonType> _buttonType = _alert.showAndWait();
//        根据点击结果返回  CANCEL_CLOSE=叉号
        if (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
            primaryStage.close();
        }

    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init!");
        //加载解析一些配置文件。

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //关闭后台服务
        home.onDestroy();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
