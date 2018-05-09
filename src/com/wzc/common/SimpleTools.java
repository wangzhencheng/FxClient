package com.wzc.common;

import java.io.File;
import java.net.URL;

public class SimpleTools {
    //获取jar或者class根目录。 对于使用外置配置文件很有效。
    private static String rootClassPath = null;

    /**
     * URL必须是存在的，否则可能寻址其他目录或为空。
     * URL对应的path是有协议的file:/ 或者jar:file:/，如果需要普通的FilePath需要去掉前缀协议。
     *
     * @param classPath
     * @return
     */
    public static URL getURL(String classPath) {
        return SimpleTools.class.getResource(classPath);
    }

    public static String getURLPath(String classPath) {
        return getURLPath(getURL(classPath));
    }

    public static String getURLPath(URL url) {
        if (url == null) {
            return null;
        }
        String path = url.toExternalForm();//相当于toString，不同于getPath getFile
        if (path.startsWith("file:/")) {
            path = path.substring("file:/".length());
        }
        if (path.startsWith("jar:file:/")) {
            path = path.substring("jar:file:/".length());
        }

        return path;
    }

    /**
     * class或者jar所处的位置。
     *
     * @return
     */
    public static String getRootClassPath() {
        if (rootClassPath == null) {
            String packageName = SimpleTools.class.getPackage().getName();
            int cycleCount = packageName.split("\\.").length + 1;
            packageName = packageName.replaceAll("\\.", "/");
            String currClassPath = getURLPath(getURL("/" + packageName));

            File file = new File(currClassPath);
            for (int i = 0; i < cycleCount; i++) {
                file = file.getParentFile();
            }
            rootClassPath = file.getAbsolutePath();
        }
        return rootClassPath;
    }

    public static void main(String[] args) {
        System.out.println(getURL("/readme.txt").toExternalForm());
        System.out.println(getURL("/readme.txt").getFile());
        System.out.println(getRootClassPath());

    }

}
