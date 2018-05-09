package com.wzc.common;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ResourceLoader {

    public static void main(String[] args) {
        System.out.println(load("resource/logo.png"));
    }


    /**
     * 需要从/开始。
     *
     * @param classPath
     * @return
     */
    public static String load(String classPath) {
        URL url = ResourceLoader.class.getResource(classPath);
        if (url != null) {
            File file = new File(url.getFile());
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            String path = url.getPath();
            if (path.startsWith("file:/")) {//jar包中  url会添加file:/前缀，靠!
                path = path.substring("file:/".length());
            }
            return path;
        }
        return null;
    }

    /**
     * @param path
     * @return
     */
    public static FxmlInfo loadFxml(String path) {
        try {

            URL url = ResourceLoader.class.getResource(path);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            return new FxmlInfo(fxmlLoader.load(), fxmlLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class FxmlInfo {
        private Object root;
        private Object controller;

        public FxmlInfo(Object root, Object controller) {
            this.root = root;
            this.controller = controller;
        }

        public <T> T getRoot() {
            return (T) root;
        }

        public <T> T getController() {
            return (T) controller;
        }
    }
}
