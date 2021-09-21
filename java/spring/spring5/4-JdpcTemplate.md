# JdbcTemplate

## JdbcTemplate（概念和准备）

### 概念

Spring框架对JDBC进行封装，使用JdbcTemplate方便实现对数据库的操作



### 准备工作

1. 导入相关jar包

2. 在spring配置文件中配置数据库连接池

   ```xml
       <!-- 数据库连接池 -->
       <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
             destroy-method="close">
           <property name="url" value="jdbc:mysql:///user_db" />
           <property name="username" value="root" />
           <property name="password" value="root" />
           <property name="driverClassName" value="com.mysql.jdbc.Driver" />
       </bean>
   ```

3. 配置JdbcTemplate对象，注入DataSource

   ```xml
       <!--创建JdbcTemplate对象-->
       <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
           <!--使用set方法注入DataSource-->
           <property name="dataSource" ref="dataSource"></property>
       </bean>
   ```

4. 创建service类，创建dao类，在dao中注入jdbcTemplate对象

   ```xml
       <!--组件扫描-->
       <context:component-scan base-package="com.atguigu.spring" ></context:component-scan>
   ```

   ```java
   @Service
   public class BookService {
       //注入dao
       @Autowired
       private BookDao bookDao;
   }
   ```

   ```java
   @Repository
   public class BookDaoImpl implements BookDao {
   
       //注入JdbcTemplate
       @Autowired
       private JdbcTemplate jdbcTemplate;
   }
   ```

   

## JdbcTemplate操作数据库

### 添加

1. 对应数据库创建实体类

   ```java
   public class User {
   
       private String userId;
       private String username;
       private String ustatus;
    	
       //创建get和set方法
   }
   ```

2. 编写service和dao

   1. 

































































