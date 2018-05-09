package com.wzc.view;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import com.wzc.common.CMDTools;
import com.wzc.common.ConfigTools;
import com.wzc.common.ResourceLoader;
import com.wzc.common.SimpleTools;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    private Stage primaryStage;

    @FXML
    private BorderPane rootPane;

    //loading组件
    private Parent prepareLoadingView;
    private Prepare prepare;

    private CMDTools cmdTools;
    private Browser browser;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //添加loading页面
                ResourceLoader.FxmlInfo prepareXmlInfo = ResourceLoader.loadFxml("/com/wzc/view/prepare.fxml");
                prepareLoadingView = prepareXmlInfo.getRoot();
                prepare = prepareXmlInfo.getController();
                rootPane.setCenter(prepareLoadingView);

                //检查服务是否已经启动
//        checkService();
                startService();
            }
        });
    }

    public void onDestroy() {
        if (null != browser) {
            browser.stop();
            browser.dispose();
        }
        if (null != cmdTools) cmdTools.destroy();
    }

    private void loadHtml() {
//        WebView webView = new WebView();
//        rootPane.setCenter(webView);
//
//        final WebEngine engine = webView.getEngine();
//
//        engine.onErrorProperty().setValue(new EventHandler<WebErrorEvent>() {
//            @Override
//            public void handle(WebErrorEvent event) {
//                event.getException().printStackTrace();
//                System.out.println(event.getMessage());
//            }
//        });
//
//        engine.setJavaScriptEnabled(true);
//        engine.setOnAlert(new EventHandler<WebEvent<String>>() {
//            @Override
//            public void handle(WebEvent<String> event) {
//                DialogTools.alert(primaryStage, "提示", event.getData());
//            }
//        });
        browser = new Browser();
        BrowserView browserView = new BrowserView(browser);
        rootPane.setCenter(browserView);
        //file:///E:/work_source/javaFx/JavaFxApp/index.html
        String htmlUrl = ConfigTools.getConfig().getHomeUrl();
        if (!htmlUrl.startsWith("http")) {//如果不是http开头，则默认使用当前目录中的文件。
            htmlUrl = "file:///" + SimpleTools.getRootClassPath() + "/" + htmlUrl;
        }
        System.out.println("load html :" + htmlUrl);
        browser.loadURL(htmlUrl);
    }

    private void onResourceOk() {
        //移除进度条
        rootPane.getChildren().remove(prepareLoadingView);
        //加载网页
        loadHtml();
    }

    private void onResourceError(String msg) {
//        移除进度条
//        rootPane.getChildren().remove(prepareLoadingView);
        prepare.updateText(msg);
    }

    private void checkService() {
        //检查网址是否ok
        new Thread(new Runnable() {
            @Override
            public void run() {
                String homeUrl = ConfigTools.getConfig().getHomeUrl();
                boolean urlIsOk = urlIsOk(homeUrl);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (urlIsOk) {
                            onResourceOk();
                        } else {
                            startService();
                        }
                    }
                });

            }
        }).start();
    }

    /**
     * 启动服务
     */
    private void startService() {
        String jarPath = ConfigTools.getConfig().getServicePath();
        String cmd = "java -Dfile.encoding=UTF-8 -jar " + jarPath;
        File jarFile = new File(jarPath);
        if (!jarFile.exists()) {
            System.out.println("service not found!" + jarFile.getAbsolutePath());
//            onResourceError("服务jar包路径错误");
            //jar虽然没有，扔尝试加载
            onResourceOk();
            return;
        }

        cmdTools = new CMDTools();
        cmdTools.setOnGetLine(new CMDTools.OnGetLine() {
            @Override
            public void onGetLine(CMDTools cmdTools, String tag, String line) {
                //使用该日志判断是否已经启动了程序。
                boolean loadOver = line != null && line.contains("JVM running for");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(line);
                        if (loadOver) {
                            onResourceOk();
                        } else {
                            prepare.updateText(line);
                        }

                    }
                });

            }
        });
        cmdTools.exec(cmd);
    }

    private void tryHomeUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (urlIsOk(ConfigTools.getConfig().getHomeUrl())) {
                    Platform.runLater(() -> {
                        onResourceOk();
                    });
                }
            }
        }).start();

    }

    public boolean urlIsOk(String strurl) {
        boolean urlIsOk = false;
        try {
            //创建URL对象
            URL url = new URL(strurl);//Get请求可以在Url中带参数： ①url + "?bookname=" + name;②url="http://www.baidu.com?name=zhang&pwd=123";
            //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //在这里设置一些属性，详细见UrlConnection文档，HttpURLConnection是UrlConnection的子类
            //设置连接超时为5秒
            httpURLConnection.setConnectTimeout(1000);
            //设定请求方式(默认为get)
            httpURLConnection.setRequestMethod("GET");
            //建立到远程对象的实际连接
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            urlIsOk = code == 200;
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlIsOk;
    }

//    @FXML
//    public void onBtnClick(ActionEvent actionEvent) {
//        System.out.println(actionEvent);
//        System.out.println(rootPane);
//    }


    public static void main(String[] args) {
        String htmlUrl = ConfigTools.getConfig().getHomeUrl();
        if (!htmlUrl.startsWith("http")) {
            htmlUrl = "file:///" + SimpleTools.getRootClassPath() + "/" + htmlUrl;
        }
        System.out.println(htmlUrl);
//        System.out.println(new Home().urlIsOk("http://www.baidu.com"));
    }
}
