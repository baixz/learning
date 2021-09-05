# JDBC连接池&JDBCTemplate

## 数据库连接池

1. 概念：其实就是一个容器(集合)，存放数据库连接的容器。当系统初始化好后，容器被创建，容器中会申请一些连接对象，当用户来访问数据库时，从容器中获取连接对象，用户访问完之后，会将连接对象归还给容器。
2. 好处：
   1. 节约资源
   2. 用户访问高效

### C3P0

* 使用步骤：

  1. 导入jar包 (两个) c3p0-0.9.5.2.jar mchange-commons-java-0.2.12.jar
     * 不要忘记导入数据库驱动jar包
  2. 定义配置文件：
     * 名称： c3p0.properties 或者 c3p0-config.xml
     * 路径：直接将文件放在src目录下即可。
  3. 创建核心对象 数据库连接池对象 ComboPooledDataSource
  4. 获取连接： getConnection

* 配置文件说明：

  ```xml
  <c3p0-config>
    <!-- 使用默认的配置读取连接池对象 -->
    <default-config>
    	<!--  连接参数 -->
      <property name="driverClass">com.mysql.jdbc.Driver</property>
      <property name="jdbcUrl">jdbc:mysql://localhost:3306/db4</property>
      <property name="user">root</property>
      <property name="password">root</property>
      <!-- 连接池参数 -->
      <!--初始化申请的连接数量-->
      <property name="initialPoolSize">5</property>
      <!--最大的连接数量-->
      <property name="maxPoolSize">10</property>
      <!--超时时间-->
      <property name="checkoutTimeout">3000</property>
    </default-config>
  
    <named-config name="otherc3p0"> 
      <!--  连接参数 -->
      <property name="driverClass">com.mysql.jdbc.Driver</property>
      <property name="jdbcUrl">jdbc:mysql://localhost:3306/db3</property>
      <property name="user">root</property>
      <property name="password">root</property>
      <!-- 连接池参数 -->
      <property name="initialPoolSize">5</property>
      <property name="maxPoolSize">8</property>
      <property name="checkoutTimeout">1000</property>
    </named-config>
  </c3p0-config>
  ```

* 代码示例：

  ```java
  /**
   * c3p0演示
   */
  public class C3P0Demo2 {
      public static void main(String[] args) throws SQLException {
         //1. 获取DataSource，使用默认配置
          DataSource ds  = new ComboPooledDataSource();
          //2.获取连接
          for (int i = 1; i <= 11 ; i++) {
              Connection conn = ds.getConnection();
              System.out.println(i+":"+conn);
              if(i == 5){
                  conn.close();//归还连接到连接池中
              }
          }
          //testNamedConfig();
      }
      public static void testNamedConfig() throws SQLException {
          // 1.1 获取DataSource，使用指定名称配置
          DataSource ds  = new ComboPooledDataSource("otherc3p0");
          //2.获取连接
          for (int i = 1; i <= 10 ; i++) {
              Connection conn = ds.getConnection();
              System.out.println(i+":"+conn);
          }
      }
  }
  ```

### Druid

* 使用步骤：

  1. 导入jar包 druid-1.0.9.jar和数据库驱动jar包
  2. 定义配置文件：
     * 是properties形式的
     * 可以叫任意名称，可以放在任意目录下
  3. 加载配置文件Properties
  4. 获取数据库连接池对象：通过工厂类来获取DruidDataSourceFactory
  5. 获取连接：getConnection

* 配置文件说明：

  ```properties
  driverClassName=com.mysql.jdbc.Driver
  url=jdbc:mysql:///db3
  username=root
  password=root
  # 初始化连接数量
  initialSize=5
  # 最大连接数
  maxActive=10
  # 最大等待时间
  maxWait=3000
  ```

* 代码示例：

  ```java
  /**
   * Druid演示
   */
  public class DruidDemo {
      public static void main(String[] args) throws Exception {
          //1.导入jar包
          //2.定义配置文件
          //3.加载配置文件
          Properties pro = new Properties();
          InputStream is = DruidDemo.class.getClassLoader().getResourceAsStream("druid.properties");
          pro.load(is);
          //4.获取连接池对象
          DataSource ds = DruidDataSourceFactory.createDataSource(pro);
          //5.获取连接
          Connection conn = ds.getConnection();
          System.out.println(conn);
      }
  }
  ```

* 定义工具类：

  ```java
  /**
   * JDBC工具类 使用Durid连接池
   */
  public class JDBCUtils {
      private static DataSource ds ;
      static {
          try {
              //1.加载配置文件
              Properties pro = new Properties();
              //使用ClassLoader加载配置文件，获取字节输入流
              InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
              pro.load(is);
              //2.初始化连接池对象
              ds = DruidDataSourceFactory.createDataSource(pro);
          } catch (IOException e) {
              e.printStackTrace();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      /**
       * 获取连接池对象
       */
      public static DataSource getDataSource(){
          return ds;
      }
      /**
       * 获取连接Connection对象
       */
      public static Connection getConnection() throws SQLException {
          return  ds.getConnection();
      }
  }
  ```


## JDBCTemplate

* Spring框架对JDBC的简单封装。提供了一个JDBCTemplate对象，简化JDBC的开发

* 使用步骤：

  1. 导入jar包
  2. 创建JdbcTemplate对象。依赖于数据源DataSource
     * JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
  3. 调用JdbcTemplate的方法来完成CRUD的操作
     * update():执行DML语句。增、删、改语句
     * queryForMap():查询结果将结果集封装为map集合，将列名作为key，将值作为value 将这条记录封装为一个map集合
       * 注意：这个方法查询的结果集长度只能是1
     * queryForList():查询结果将结果集封装为list集合
       * 注意：将每一条记录封装为一个Map集合，再将Map集合装载到List集合中
     * query():查询结果，将结果封装为JavaBean对象
       * query的参数：RowMapper
       * 一般我们使用BeanPropertyRowMapper实现类。可以完成数据到JavaBean的自动封装
         * new BeanPropertyRowMapper<类型>(类型.class)
     * queryForObject()：查询结果，将结果封装为对象
       * 一般用于聚合函数的查询

* 代码示例：

  ```java
  public class JdbcTemplateDemo2 {
      //1. 获取JDBCTemplate对象
      private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
      /**
       * 1. 修改1号数据的 salary 为 10000
       */
      @Test
      public void test1(){
          //2. 定义sql
          String sql = "update emp set salary = 10000 where id = 1001";
          //3. 执行sql
          int count = template.update(sql);
          System.out.println(count);
      }
      /**
       * 2. 添加一条记录
       */
      @Test
      public void test2(){
          String sql = "insert into emp(id,ename,dept_id) values(?,?,?)";
          int count = template.update(sql, 1015, "郭靖", 10);
          System.out.println(count);
      }
      /**
       * 3.删除刚才添加的记录
       */
      @Test
      public void test3(){
          String sql = "delete from emp where id = ?";
          int count = template.update(sql, 1015);
          System.out.println(count);
      }
      /**
       * 4.查询id为1001的记录，将其封装为Map集合
       * 注意：这个方法查询的结果集长度只能是1
       */
      @Test
      public void test4(){
          String sql = "select * from emp where id = ? or id = ?";
          Map<String, Object> map = template.queryForMap(sql, 1001,1002);
          System.out.println(map);
      }
      /**
       * 5. 查询所有记录，将其封装为List
       */
      @Test
      public void test5(){
          String sql = "select * from emp";
          List<Map<String, Object>> list = template.queryForList(sql);
          for (Map<String, Object> stringObjectMap : list) {
              System.out.println(stringObjectMap);
          }
      }
      /**
       * 6. 查询所有记录，将其封装为Emp对象的List集合
       */
      @Test
      public void test6_2(){
          String sql = "select * from emp";
          List<Emp> list = template.query(sql, new BeanPropertyRowMapper<Emp>(Emp.class));
          for (Emp emp : list) {
              System.out.println(emp);
          }
      }
      /**
       * 7. 查询总记录数
       */
      @Test
      public void test7(){
          String sql = "select count(id) from emp";
          Long total = template.queryForObject(sql, Long.class);
          System.out.println(total);
      }
  }
  ```

  









































