# Spring5框架新功能

## 日志工具

Spring5框架整合了Log4j2

### 配置Log4j2

1. 引入jar包

   - log4j-api-2.11.2.jar
   - log4j-core-2.11.2.jar
   - log4j-slf4j-impl-2.11.2.jar
   - slf4j-api-1.7.30.jar

2. 创建log4j2.xml配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
   <!--Configuration后面的status用于设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，可以看到log4j2内部各种详细输出-->
   <configuration status="INFO">
       <!--先定义所有的appender-->
       <appenders>
           <!--输出日志信息到控制台-->
           <console name="Console" target="SYSTEM_OUT">
               <!--控制日志输出的格式-->
               <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
           </console>
       </appenders>
       <!--然后定义logger，只有定义了logger并引入的appender，appender才会生效-->
       <!--root：用于指定项目的根日志，如果没有单独指定Logger，则会使用root作为默认的日志输出-->
       <loggers>
           <root level="info">
               <appender-ref ref="Console"/>
           </root>
       </loggers>
   </configuration>
   ```

3. 运行配置了log4j2的项目



## @Nullable注解

Spring5框架核心容器支持@Nullable注解

1. @Nullable注解可以使用在方法、属性、参数上面，表示方法返回值可以为空，属性值可以为空，参数值可以为空。



## JUnit5





