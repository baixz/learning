# JdbcTemplate

## JdbcTemplate概念和准备

Spring框架对JDBC进行封装，使用JdbcTemplate方便实现对数据库的操作。

### 准备工作

1. 引入相关jar包

   - druid-1.1.9.jar：druid数据库连接池
   - mysql-connector-java-5.1.7-bin.jar：mysql数据连接
   - spring-jdbc-5.2.6.RELEASE.jar：spring的jdbc
   - spring-orm-5.2.6.RELEASE.jar：用于整合其他框架
   - spring-tx-5.2.6.RELEASE.jar：spring事务相关

2. 在Spring配置文件中配置数据库连接池

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

3. 在spring配置文件中配置JdbcTemplate对象，注入DataSource

   ```xml
       <!--创建jdbcTemplate对象-->
       <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
           <!--通过set方法注入dataSource对象-->
           <property name="dataSource" ref="dataSource" ></property>
       </bean>
   ```

4. 创建service类，创建dao类，在dao中注入jdbcTemplate对象

   ```xml
       <!--开启组件扫描-->
       <context:component-scan base-package="com.atguigu.spring" ></context:component-scan>
   ```

   ```java
   @Service
   public class BookService {
       //注入dao
       @Autowired
       private BookDao bookDao;
   }
   
   @Repository
   public class BookDaoImpl implements BookDao {
       //注入jdbcTemplate
       @Autowired
       private JdbcTemplate jdbcTemplate;
   }
   ```



## JdbcTempate操作数据库

### 添加

1. 对应数据库创建实体类

2. 编写service和dao

   1. 在dao中进行数据库添加操作
   2. 调用JdbcTemplate对象里的update()方法
      - update(String sql, Object... args)
      - 参数1：sql语句
      - 参数2：可变参数数组，用于设置sql语句的输入值

3. 编写测试类

4. 示例：

   ```java
       //添加方法
       @Override
       public void add(User user) {
           String sql = "insert into t_user values(?,?,?)";
           String[] args = {user.getUserId(), user.getUsername(), user.getUstatus()};
           jdbcTemplate.update(sql, args);
       }
   ```

   

### 修改和删除

1. 修改

   ```java
       //修改方法
       @Override
       public int updateUser(User user) {
           String sql = "update t_user set username = ? where user_id = ?";
           String[] args = {user.getUsername(), user.getUserId()};
           int update = jdbcTemplate.update(sql, args);
   
           return update;
       }
   ```

2. 删除

   ```java
       //删除方法
       @Override
       public int delUser(User user) {
           String sql = "delete from t_user where user_id = ?";
           String[] args = {user.getUserId()};
           int update = jdbcTemplate.update(sql, args);
   
           return update;
       }
   ```

   

### 查询返回某个值

1. 查询表里有多少条记录，返回记录条数

2. 使用JdbcTemplate实现查询返回记录数

   - queryForObject(String sql, Class<T> requiredType)
   - 参数1：sql语句
   - 参数2：返回值类型的class

3. 示例：

   ```java
       @Override
       public int findCount() {
           String sql = "select count(user_id) from t_user";
           int count = jdbcTemplate.queryForObject(sql, Integer.class);
   
           return count;
       }
   ```

   

### 查询返回对象

1. 查询某条记录的所有信息

2. 使用JdbcTemplate实现查询返回对象

   - queryForObject(String sql, RowMapper<T> rowMapper, Object... args)
   - 参数1：sql语句
   - 参数2：RowMapper是一个接口，针对返回不同数据类型，使用这个接口的实现类完成数据封装。
   - 参数3：可变参数数组，用于设置sql语句的输入值

3. 示例：

   ```java
       @Override
       public User findUserInfo(String userId) {
           String sql = "select * from t_user where user_id = ?";
           String[] args = {userId};
           User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), args);
   
           return user;
       }
   ```



### 查询返回集合

1. 查询所有信息

2. 使用JdbcTemplate实现查询返回集合

   - query(String sql, RowMapper<T> rowMapper, Object... args)
   - 参数1：sql语句
   - 参数2：RowMapper是一个接口，针对返回不同数据类型，使用这个接口的实现类完成数据封装。
   - 参数3：可变参数数组，用于设置sql语句的输入值

3. 示例：

   ```java
       @Override
       public List<User> listUser() {
           String sql = "select * from t_user";
           List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
   
           return users;
       }
   ```



### 批量操作

1. 批量操作表中的多条记录

2. 使用JdbcTemplate实现批量添加操作

   - batchUpdate(String sql, List<Object[]> batchArgs)
   - 参数1：sql语句
   - 参数2：list集合，添加多条记录

3. 示例：

   ```java
       @Override
       public void batchAdd(List<Object[]> batchArgs) {
           String sql = "insert into t_user values (?,?,?)";
           int[] ints = jdbcTemplate.batchUpdate(sql, batchArgs);
           System.out.println(Arrays.toString(ints));
       }
   	
   		//测试
           Object[] o1 = {"3", "java", "a"};
           Object[] o2 = {"4", "C++", "b"};
           Object[] o3 = {"5", "python", "c"};
           List<Object[]> batchArgs = new ArrayList<>();
           batchArgs.add(o1);
           batchArgs.add(o2);
           batchArgs.add(o3);
   
           bookService.batchAdd(batchArgs);
   ```

4. 批量修改

   ```java
       @Override
       public void batchModify(List<Object[]> batchArgs) {
           String sql = "update t_user set username = ? where user_id = ?";
           int[] ints = jdbcTemplate.batchUpdate(sql, batchArgs);
           System.out.println(Arrays.toString(ints));
       }
   ```

5. 批量删除

   ```java
       @Override
       public void batchDel(List<Object[]> batchArgs) {
           String sql = "delete from t_user where user_id = ?";
           int[] ints = jdbcTemplate.batchUpdate(sql, batchArgs);
           System.out.println(Arrays.toString(ints));
       }
   ```



## 事务管理



























