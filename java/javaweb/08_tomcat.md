# tomcat

## windows环境下配置tomcat

1. 下载

2. 安装

   * 解压文件到指定目录下

3. 卸载

   * 删除安装目录文件夹

4. 启动

   * bin/startup.bat ,双击运行该文件即可

   * 访问：浏览器输入：http://localhost:8080 回车访问自己；http://别人的ip:8080 访问别人

   * 启动过程中黑窗口一闪而过

     * 原因：没有正确配置JAVA_HOME环境变量

   * 修改自身端口号

     * conf/server.xml

       ```xml
       <!--假如出现与其他程序冲突的情况，可以吧8888这个端口修改成其他数字-->
       <Connector port="8888" protocol="HTTP/1.1" 
                  connectionTimeout="20000"
                  redirectPort="8445" />
       ```

5. 关闭

   * bin/shutdown.bat，双击运行该文件即可

6. 配置

   * 部署项目的方式：

     1. 直接将项目放到webapps目录下即可。

        *  /hello：项目的访问路径-->虚拟目录
        * 简化部署：将项目打成一个war包，再将war包放置到webapps目录下。
        * war包会自动解压缩

     2. 配置conf/server.xml文件

        * 在<Host>标签体中配置

          ```xml
          <Context docBase="D:\hello" path="/hehe" />
          ```

          * docBase：项目存放的路径
          * path：虚拟目录

     3. 在conf\Catalina\localhost创建任意名称的xml文件。在文件中编写

        ```xml
        <Context docBase="D:\hello" />
        ```

        * docBase：项目存放的路径
        * 虚拟目录：xml文件的名称

