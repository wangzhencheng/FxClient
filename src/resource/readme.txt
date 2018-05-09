使用：
  方法1：
    1 service-simple-socket.jar作为jar包，开发可执行jar，然后更名service.jar
    2 开发网页index.html，浏览器打开，copy地址，配置到config.properties中，或者直接index.html

  方法2：
    1 springBoot打包jar，更名service.jar, 日志info级别-默认
    2 config.properties配置要加载的首页

文件说明：

code 包含两部分，
1 是javaFx的CMD启动器+浏览器
2 自定义的轻量级基于Socket的Http服务器，get post请求。

客户端开发：
配置文件(服务名、程序名、端口号)： config.properties
index.html 是示例html，浏览器打开，地址栏的路径可以配置到配置文件中。
JavaFXApp.jar 客户端程序入口
service-simple-socket.jar /service.jar  轻量级http服务器
service-springboot.jar SpringBoot对应的jar,(日志不能清，需要JVM running for字样)

