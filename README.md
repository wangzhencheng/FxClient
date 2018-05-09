# FxClient

```
该项目利用JavaFx打包一个浏览器，用来实现客户端开发；
可以在同目录下添加html页面和自己处理复杂业务逻辑的service(java图片处理、本机文件访问、跨域http请求等)

SampleUse文件夹中提供了一个使用示例，config.properties配置文件（端口、启动页、客户端名称等）;
FxClient.jar是程序入口;
service-http.jar用于作为普通Java项目的lib，通过
    new SocketHttpHelper().addHandlerByClass(HomeHandler.class).start()来启动Socket模拟的Http服务；
 
HomeHandler.class是一个业务处理的Demo，注意参数Request和注解@Handler即可。返回值可以是字符串 InputStream File Object。
index.html就是自己的html页面，可以是JQuery、Vue、React等技术开发的普通页面；
主流浏览器开发即可，Http-get-post去调用使用了service-http.jar的项目即可
 
现在java程序员可以使用自己的java技术，一个普通的含service-http.jar的Java项目，就可以开发API
(相比其他web框架和服务器，极其简单粗暴、秒启动)；
前端用html做view层；FxClient.jar为程序入口和壳子；轻松开发客户端。
```
 起码做个小工具之类的客户端，开发足够迅速简单！
