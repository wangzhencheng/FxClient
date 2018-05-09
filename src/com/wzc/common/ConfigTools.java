package com.wzc.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 用于处理当前的默认配置。
 */
public class ConfigTools {
    private static ConfigBean configBean = null;

    public static ConfigBean getConfig() {
        if (configBean == null) {
            //初始化一下；
            configBean = new ConfigBean();
            String classPath = ResourceLoader.load("/resource");
            String classPathParent = new File(classPath).getParentFile().getParentFile().getAbsolutePath();
            try {
                File configFile = new File(classPathParent, configBean.getConfigFile());
                System.out.println("configFile:" + configFile);
                if (null != configFile && configFile.exists()) {
                    System.out.println("reset config!!");
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(configFile));
                    Field[] fields = ConfigBean.class.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String value = properties.getProperty(field.getName());
                        if (null != value && value.trim().length() > 0) {
                            //转码
                            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                            field.set(configBean, value);
                        }
                    }
                } else {
                    System.out.println("default config!!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //设置service默认值。
            String serviceFile = configBean.getServicePath();
            if (!new File(serviceFile).exists()) {
                System.out.println("service not exists and reset path");
                configBean.setServicePath(new File(classPathParent, serviceFile).getAbsolutePath());
            } else {
                System.out.println("service exists:" + new File(serviceFile).getAbsolutePath());
            }
        }
        return configBean;
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(ResourceLoader.load("./"));
//        System.out.println(ResourceLoader.class.getResource("/com/wzc/view/home.fxml").openStream());
        Properties p = new Properties();
        p.load(ConfigTools.class.getResourceAsStream("/resource/config.properties"));
        System.out.println(p.getProperty("serviceName"));
    }
}
