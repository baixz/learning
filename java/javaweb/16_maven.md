# Maven

- Maven介绍
- Maven的使用
- Maven常用命令
- Maven工程运行调试



## Maven介绍

Maven是一个项目管理工具，它包含了一个项目对象模型(POM：Project Object Model)，一组标准集合，一个项目生命周期(Project Lifecycle)，一个依赖管理系统(Dependency Management System)，和用来运行定义在生命周期阶段(phase)中插件(plugin)目标(goal)的逻辑。



## Maven的使用

### 安装Maven

1. Maven是绿色版的，解压后可以直接使用。
2. 配置maven环境变量
   1. 在环境变量中新建系统变量：**MAVEN_HOME**，变量值为maven文件夹根目录
   2. 在PATH变量中添加：**%MAVEN_HOME%\bin**

### maven仓库

- 本地仓库：

  当前项目所在计算机的文件夹，可以通过配置文件配置文件夹的目录。

- 远程仓库：

- 中央仓库：

### maven获取资源的过程

1. Maven本地项目通过pom.xml文件夹配置本次项目使用的jar包或插件
2. maven会先查找本地仓库，假如本地仓库中没有找到对应版本的资源，本地仓库会向中央仓库下载对应的资源。
3. 资源下载完成后，本地项目会使用本地项目中的资源。
4. 新的项目使用相同的jar包时，可以直接使用本地仓库的资源，不需要再向中央仓库下载。

### 修改Maven本地仓库路径

1. 打开**apache-maven-3.6.3\conf\settings.xml**文件，找到**localRepository**标签，配置新的本地仓库路径。

   ```xml
   <localRepository>D:\apache-maven-3.6.3\mvn_repository</localRepository>
   ```

### 配置远程仓库

1. 打开**apache-maven-3.6.3\conf\settings.xml**文件，找到**mirrors**标签，在这个标签中配置阿里云的远程仓库。

   ```xml
   	 <mirror>  
   		  <id>alimaven</id>  
   		  <name>aliyun maven</name>  
   		  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
   		  <mirrorOf>central</mirrorOf>          
   	</mirror>
   ```

<font color='red'>**注意：第一次使用maven时，不要添加阿里云的远程仓库配置，否则在下载资源的时候会提示错误**</font>

### maven项目目录结构

- src：源码
  - main：主工程代码
    - java：主工程代码
    - resource：主工程使用的配置文件
    - webapp：web项目的资源文件夹（jsp/html/WEB-INF...）
  - test：测试代码
    - java：测试代码
    - resource：测试需要使用的配置文件
- pom.xml：项目的核心配置文件



## maven常用命令

1. compile：compile是maven工程的编译命令，作用是将src/main/java下的文件编译为class文件输出到target目录下。
2. clean：clean是maven工程的清理命令，执行clean会删除target目录及内容。
3. package：package是maven工程的打包命令，对于java工程执行package打成jar包，对于web工程打成war包。
4. install：install是maven工程的安装命令，执行install将maven打成jar包或war包发布到本地仓库。

### maven指令的生命周期

- 清理

- 编译——测试——打包——安装

- 站点发布

<font color='red'>**同一套生命周期中，执行后面的操作，会自动执行前面所有的操作**</font>



## IDEA配置Maven

1. 打开IDEA——file——settings——Build,Execution——Build Tools——Maven
2. 配置Maven home directory、User settings file、Local repository。
3. 保存并退出。



## 使用Maven创建项目

### 创建java项目

1. 新建模块或项目
2. 选择Maven项目，勾选**Create from archetype**，选择模板**maven-archetype-quickstrt**后，点击下一步
3. 配置项目名称Name，GroupId，点击下一步
4. 检查Maven软件的路径和其他配置是否正确，点击完成

### 创建javaweb项目

1. 新建模块或项目
2. 选择Maven项目，勾选**Create from archetype**，选择模板**maven-archetype-webapp**后，点击下一步
3. 配置项目名称Name，GroupId，点击下一步
4. 检查Maven软件的路径和其他配置是否正确，点击完成



## pom.xml配置文件说明

### 配置项目信息

```xml
  <!--当前项目的坐标-->
  <!--组织名称-->
  <groupId>cn.itcast</groupId>
  <!--模块名称-->
  <artifactId>maven_java</artifactId>
  <!--版本号-->
  <version>1.0-SNAPSHOT</version>
  <!--
    打包方式：
		1. jar：java项目，默认值
		2. war：web项目
		3. pom
  -->
  <packaging>jar</packaging>
```

### 配置依赖jar包

```xml
<dependencies>
    <!--一个dependency就是一个坐标，唯一标识当前的jar包-->
    <dependency>
        <!--组织名称-->
        <groupId>junit</groupId>
        <!--模块名称-->
        <artifactId>junit</artifactId>
        <!--版本号-->
        <version>4.9</version>
        <!--依赖范围-->
        <scope>test</scope>
    </dependency>
<dependencies>
```

### 配置插件

```xml
<build>
    <plugins>
        <!--配置tomcat7插件-->
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.2</version>
            <configuration>
                <!--修改端口号-->
            	<port>80</port>
                <!--修改tomcat部署当前项目的虚拟目录-->
                <path>/</path>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 依赖范围

















































