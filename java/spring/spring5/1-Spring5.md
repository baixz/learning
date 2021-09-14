# Spring5

1. Spring框架概述
2. IOC容器
3. AOP
4. JdbcTemplate
5. 事务管理
6. Spring5新特性

## 框架概述

1. Spring是一个轻量级javaee框架
2. Spring可以解决企业应用开发的复杂性
3. Spring有两个核心部分：IOC和AOP
   1. IOC：控制反转，把创建对象过程交给Spring管理
   2. AOP：面向切面，不修改源代码的情况下，进行功能增强
4. Spring特点：
   1. 方便解耦，简化开发
   2. AOP编程支持
   3. 方便程序的测试
   4. 方便集成各种框架
   5. 方便进行事务操作
   6. 降低API开发难度
   7. java源码是经典学习范例

## 入门案例

1. 创建javabean工程

2. 导入spring的jar包

3. 创建普通类，在这个类创建普通方法

   ```java
   public class User {
       public void add() {
           System.out.println("add......");
       }
   }
   ```

4. 创建Spring配置文件，在配置文件中配置创建的对象

   ```xml
   	<!--在src目录下创建bean1.xml文件-->
   	<!--配置User对象创建-->
       <bean id="user" class="com.atguigu.spring.User"></bean>
   ```

5. 编写测试代码

   ```java
       @Test
       public void test1() {
           //加载配置文件
   		//配置文件在src目录下，所以使用ClassPathXmlApplicationContext来获取
           ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");
           //获取配置创建的对象
           User user = context.getBean("user", User.class);
           System.out.println(user);
           user.add();
       }
   ```

   

