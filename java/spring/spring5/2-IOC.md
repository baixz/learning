# IOC

1. IOC概念底层原理
2. IOC接口（BeanFactory）
3. IOC操作——Bean管理（基于xml）
4. IOC操作——Bean管理（基于注解）



## IOC概念底层原理

### 概念

1. 控制反转，把对象创建和对象之间的调用过程，交给Spring进行管理
2. 使用IOC的目的：为了将耦合度降低
3. 入门案例就是通过IOC实现的



### 底层原理

IOC通过xml解析、工厂模式、反射实现现有功能



### 画图讲解IOC底层原理

1. 在xml配置文件中配置要创建的java对象
2. 创建工厂类
3. 在工厂类中解析xml文件，获取java对象中的class属性值
4. 通过反射获取java对象的class
5. 返回通过反射获取的java对象

![ioc实现过程](ioc实现过程.bmp)



## IOC接口（BeanFactory）

1. IOC思想基于IOC容器完成，IOC容器底层就是对象工厂

2. Spring提供IOC容器实现两种方式：（两个接口）

   1. BeanFactory：IOC容器基本实现，是Spring内部的使用接口，不提供开发人员进行使用。
      - 加载配置文件时候不会创建对象，在获取对象（使用）才去创建对象
   2. ApplicationContext：BeanFactory接口的子接口，提供更多更强大的功能，一般由开发人
      员进行使用。
      - 加载配置文件时候就会把在配置文件对象进行创建

3. ApplicationContext接口的实现类

   - FileSystemXmlApplicationContext：以传入文件路径的形式加载xml配置文件
   - ClassPathXmlApplicationContext：以class的形式加载配置文件，当xml文件在src目录下使用这种方式。

   ![applicationcontext接口的实现类](applicationcontext接口的实现类.bmp)



## IOC操作——Bean管理（基于xml文件创建bean）

### 基于xml方式创建对象

1. 在spring配置文件中，使用bean标签，标签里面添加对应属性，就可以实现对象创建。
2. 在bean标签有很多属性，介绍常用的属性
   - id：表示类的别名，是唯一标识
   - class：类的全路径
3. 创建对象时，默认执行无参数构造方法完成对象创建。



### 基于xml方式注入属性

DI：依赖注入，就是注入属性。

注入属性有两种方式：

1. 使用set方法进行注入
2. 使用有参构造方法进行注入



### 使用set方法注入属性

1. 实现步骤：

   1. 创建类，定义属性和对应的set方法
   2. 在spring配置文件中添加要创建的类，并配置**property**标签
      - property标签中的属性：
        - name：java类中定义的属性名
        - value：向属性中注入的值
   3. 在测试类中加载配置文件，获取对象

2. 示例：

   ```java
   public class Book {
       private String bname;
       private String bauther;
   
       public void setBname(String bname) {
           this.bname = bname;
       }
   
       public void setBauther(String bauther) {
           this.bauther = bauther;
       }
   
       public void show() {
           System.out.println(bname + "--" + bauther);
       }
   }
   ```

   ```xml
       <!--配置Book类-->
       <bean id="book" class="com.atguigu.spring.Book">
           <property name="bname" value="易筋经"></property>
           <property name="bauther" value="达摩祖师"></property>
       </bean>
   ```

   ```java
       @Test
       public void test2() {
           //加载xml文件
           ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");
           //获取配置对象
           Book book = context.getBean("book", Book.class);
           //调用方法
           System.out.println(book);
           book.show();
       }
   ```

   

### 使用构造函数注入属性

1. 实现步骤：

   1. 创建类，定义属性和对应的有参构造方法
   2. 在spring配置文件中添加要创建的类，并配置**constructor-arg**标签
      - constructor-arg标签中的属性：
        - name：java类中定义的属性名
        - index：构造方法中的第几个参数
        - value：向属性中注入的值
   3. 在测试类中加载配置文件，获取对象

2. 代码示例：

   ```java
   public class Order {
       private String oname;
       private String address;
   
       public Order(String oname, String address) {
           this.oname = oname;
           this.address = address;
       }
       
       public void show(){
           System.out.println(oname + "--" + address);
       }
   }
   ```

   ```xml
       <!--配置Order类-->
       <bean id="order" class="com.atguigu.spring.Order">
           <constructor-arg name="oname" value="computer"></constructor-arg>
           <constructor-arg name="address" value="China"></constructor-arg>
           <!--<constructor-arg index="0" value="computer"></constructor-arg>-->
       </bean>
   ```

   ```java
       @Test
       public void test3() {
           //加载xml文件
           ApplicationContext context = new ClassPathXmlApplicationContext("bean1.xml");
           //获取配置对象
           Order order = context.getBean("order", Order.class);
           //调用方法
           System.out.println(order);
           order.show();
       }
   ```



## IOC操作——Bean管理（通过XML注入其他类型属性）

### 字面量

字面量是指在创建类或初始化类对象过程中传入的属性值。

1. null值

   ```xml
           <!--配置null值-->
           <property name="baddress">
               <null />
           </property>
   ```

2. 属性值中包含特殊符号

   ```xml
           <!--配置特殊符号-->
   		<!--
   			1. 把<>进行转义，&lt;&gt;
   			2. 把带特殊符号的内容写到CDATA里
   		-->
           <property name="baddress">
               <value><![CDATA[<<南京>>]]></value>
           </property>
   ```

   

### 外部Bean

1. 实现步骤

   1. 创建两个类service和dao
   2. 在service中创建dao类型属性，并生成set方法
   3. 在spring配置文件中配置创建service和dao对象，并将dao的id属性值传入到service的bean标签中。
   4. 在测试方法中加载配置文件，获取对象

2. 代码示例：

   ```java
   public class UserService {
   
       //设置UserDao属性
       private UserDao userDao;
   
       //设置set方法
       public void setUserDao(UserDao userDao) {
           this.userDao = userDao;
       }
   
       //调用UserDao中的方法
       public void update() {
           System.out.println("UserService......");
           userDao.show();
       }
   }
   ```

   ```xml
       <!--创建service和dao对象-->
       <bean name="userService" class="com.atguigu.spring.service.UserService">
           <!--
               设置属性值：
               name：类里面属性名称
               ref：创建userDao对象bean标签的id值
           -->
           <property name="userDao" ref="userDaoImpl"></property>
       </bean>
       <bean name="userDaoImpl" class="com.atguigu.spring.dao.impl.UserDaoImpl"></bean>
   ```

   ```java
       @Test
       public void test4() {
           //加载xml文件
           ApplicationContext context = new ClassPathXmlApplicationContext("bean2.xml");
           //获取配置对象
           UserService userService = context.getBean("userService", UserService.class);
           System.out.println(userService);
           userService.update();
       }
   ```



### 内部Bean

1. 场景：

   一对多关系：部门和员工

2. 代码示例：

   ```java
   //部门类
   public class Dept {
       private String dname;
   
       public void setDname(String dname) {
           this.dname = dname;
       }
   
       @Override
       public String toString() {
           return "Dept{" +
                   "dname='" + dname + '\'' +
                   '}';
       }
   }
   //员工类
   public class Emp {
       private String ename;
       private String egender;
       private Dept dept;
   
       public void setEname(String ename) {
           this.ename = ename;
       }
   
       public void setEgender(String egender) {
           this.egender = egender;
       }
   
       public void setDept(Dept dept) {
           this.dept = dept;
       }
   
       public void show() {
           System.out.println(ename + "--" + egender + "--" + dept);
       }
   }
   ```

   ```xml
       <!--内部bean-->
       <bean name="emp" class="com.atguigu.spring.Emp">
           <!--设置两个普通类型属性-->
           <property name="ename" value="Lucy"></property>
           <property name="egender" value="female"></property>
           <!--设置对象类型的属性-->
           <property name="dept">
               <bean name="dept" class="com.atguigu.spring.Dept">
                   <property name="dname" value="安保部"></property>
               </bean>
           </property>
       </bean>
   ```

   

### 级联赋值

级联赋值类似内部Bean的配置方法

1. 第一种写法

   ```xml
       <!--级联赋值-->
       <bean id="emp" class="com.atguigu.spring.Emp">
           <!--设置两个普通属性-->
           <property name="ename" value="lucy"></property>
           <property name="egender" value="female"></property>
           <property name="dept" ref="dept"></property>
       </bean>
       <bean id="dept" class="com.atguigu.spring.Dept">
           <property name="dname" value="财务部"></property>
       </bean>
   ```

2. 第二种写法

   ```xml
   	<!--级联赋值-->
       <bean id="emp" class="com.atguigu.spring.Emp">
           <!--设置两个普通属性-->
           <property name="ename" value="lucy"></property>
           <property name="egender" value="female"></property>
           <property name="dept" ref="dept"></property>
           <!--需要在Emp类中实现Dept的get方法-->
           <property name="dept.dname" value="技术部"></property>
       </bean>
       <bean id="dept" class="com.atguigu.spring.Dept">
           <property name="dname" value="财务部"></property>
       </bean>
   ```

   

### 注入数组、集合类型的属性

1. 创建类，定义数组、list、set、map类型的属性，并生成set方法

   ```java
   public class Student {
       //数组类型
       private String[] courses;
       //list集合类型
       private List<String> list;
       //set集合类型
       private Set<String> sets;
       //map集合类型
       private Map<String, String> map;
   
       public void setCourses(String[] courses) {
           this.courses = courses;
       }
       public void setList(List<String> list) {
           this.list = list;
       }
       public void setSets(Set<String> sets) {
           this.sets = sets;
       }
       public void setMap(Map<String, String> map) {
           this.map = map;
       }
   }
   ```

2. 在spring配置文件中添加注入配置

   ```xml
       <bean id="student" class="com.atguigu.spring5.collection.Student">
           <!--数组类型注入-->
           <property name="courses">
               <array>
                   <value>java课程</value>
                   <value>数据库</value>
               </array>
           </property>
           <!--list类型注入-->
           <property name="list">
               <list>
                   <value>张三</value>
                   <value>小三</value>
               </list>
           </property>
           <property name="sets">
               <set>
                   <value>mysql</value>
                   <value>redis</value>
               </set>
           </property>
           <!--map类型注入-->
           <property name="map">
               <map>
                   <entry key="JAVA" value="java"></entry>
                   <entry key="PHP" value="php"></entry>
               </map>
           </property>
       </bean>
   ```

   

### 在集合中设置对象类型的值

```xml
    <bean id="student" class="com.atguigu.spring5.collection.Student">
        <!--注入list集合类型，值是对象-->
        <property name="courseList">
            <list>
                <ref bean="course1"></ref>
                <ref bean="course2"></ref>
            </list>
        </property>
    </bean>
	<!--创建多个course对象-->
    <bean id="course1" class="com.atguigu.spring5.collection.Course">
        <property name="name" value="语文"></property>
    </bean>
    <bean id="course2" class="com.atguigu.spring5.collection.Course">
        <property name="name" value="数学"></property>
    </bean>
```



### 把集合中注入的部分提取出来作为公共部分

1. 在spring配置文件中添加命名空间：util

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:util = "http://www.springframework.org/schema/util"
          xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                              http://www.springframework.org/schema/util  http://www.springframework.org/schema/util/spring-util.xsd">
   
   </beans>
   ```

2. 使用util标签完成list集合注入提取

   ```xml
       <!--1. 提取list集合类型属性注入-->
       <util:list id="bookList">
           <value>易筋经</value>
           <value>九阴</value>
           <value>九阳</value>
       </util:list>
       <!--2. 提取list集合类型属性使用-->
       <bean id="book" class="com.atguigu.spring5.collection.Book">
           <property name="list" ref="bookList"></property>
       </bean>
   ```
   
   

## IOC操作——Bean管理（FactoryBean）

spring中有两种类型的bean，一种普通bean，另一种工厂bean（FactoryBean）

1. 普通bean：在配置文件中定义bean类型就是返回类型
2. 工厂bean：在配置文件中定义bean类型可以返回不同的类型。

工厂bean创建步骤：

1. 创建类，让这个类作为工厂bean，实现接口FactoryBean
2. 实现接口中的方法，在实现方法找那个定义返回的bean类型。

示例：

```java
//创建工厂bean
public class MyBean implements FactoryBean<Course> {

    //定义返回bean对象
    @Override
    public Course getObject() throws Exception {
        Course course = new Course();
        return course;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
```

```xml
	<!--xml配置-->
    <bean id="myBean" class="com.atguigu.spring.facbean.MyBean">
    </bean>
```

```java
    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean3.xml");
        Course course = context.getBean("myBean", Course.class);
        System.out.println(course);
    }
```



## IOC操作——Bean管理（bean作用域）

1. 在Spring里可以设置bean实例是单实例或多实例
2. 在Spring里，默认情况下，bean是单实例对象（默认情况下一个类只会创建一个对象）
3. 如何设置单实例还是多实例
   1. 在Spring配置文件的bean标签中，使用**scope**属性来设置实例是但实例还是多实例
   2. scope属性的属性值
      1. singleton，默认值，表示是单实例对象
      2. prototype，表示是多实例对象
   3. singleton和prototype的区别：
      1. singleton表示单实例，prototype表示多实例
      2. 设置scope值是singleton的时候，加载spring配置文件时就会创建单实例对象
      3. 设置scope值是prototype的时候，不是在加载spring配置文件时创建多实例对象，在调用getBean()方法的时候才会创建多实例对象。



## IOC操作——Bean管理（bean生命周期）

1. bean的生命周期
   1. 通过构造器创建bean实例（调用类的无参构造器）
   2. 为bean的属性设置值和对其他bean的使用（调用set方法）
   3. 调用bean的初始化方法（需要配置初始化的方法）
   4. 使用bean（对象获取到了）
   5. 当容器关闭的时候，会调用bean的销毁方法（需要配置销毁的方法）

2. 示例：

   ```java
   public class Orders {
   
       private String oname;
   
       //无参构造器
       public Orders() {
           System.out.println("第一步：构造器");
       }
   
       public void setOname(String oname) {
           System.out.println("第二步：调用set方法设置属性值");
           this.oname = oname;
       }
   
       //初始化方法
       public void initMethod() {
           System.out.println("第三步：执行初始化方法");
       }
   
       //销毁方法
       public void destroyMethod() {
           System.out.println("第五步：执行销毁方法");
       }
   }
   ```

   ```xml
       <bean id="orders" class="com.atguigu.spring.bean.Orders" init-method="initMethod" destroy-method="destroyMethod">
           <property name="oname" value="手机"></property>
       </bean>
   ```

   ```java
       @Test
       public void test4() {
           ApplicationContext context = new ClassPathXmlApplicationContext("bean4.xml");
           Orders orders = context.getBean("orders", Orders.class);
           System.out.println("第四步：获取bean实例对象");
           System.out.println(orders);
   
           //手动销毁bean实例
           ((ClassPathXmlApplicationContext) context).close();
       }
   ```

3. bean的后置处理器，bean的生命周期为7步。

   1. 通过构造器创建bean实例（调用类的无参构造器）
   2. 为bean的属性设置值和对其他bean的使用（调用set方法）
   3. 把bean实例传递给bean后置处理器的方法：postProcessBeforeInitialization
   4. 调用bean的初始化方法（需要配置初始化的方法）
   5. 把bean实例传递给bean后置处理器的方法：postProcessAfterInitialization
   6. 使用bean（对象获取到了）
   7. 当容器关闭的时候，会调用bean的销毁方法（需要配置销毁的方法）

4. 演示添加后置处理器效果

   1. 创建类，实现接口BeanPostProcessor，创建后置处理器

      ```java
      public class MyBeanPost implements BeanPostProcessor {
          @Override
          public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
              System.out.println("在初始化之前执行的方法");
              return bean;
          }
      
          @Override
          public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
              System.out.println("在初始化之后执行的方法");
              return bean;
          }
      }
      ```

   2. 在配置文件中添加配置

      ```xml
          <!--配置后置处理器-->
          <bean id="myBeanPost" class="com.atguigu.spring.bean.MyBeanPost"></bean>
      ```

   3. 配置了后置处理器的配置文件中，会对当前文件中的其他所有bean实例对象产生影响，即所有其他bean实例对象都会添加后置处理器。



## IOC操作——Bean管理（xml自动装配）

1. 自动装配的含义

   根据指定装配规则（属性名称或属性类型），Spring自动将匹配的属性值进行注入，简化xml文件。

2. 演示自动装配过程

   ```xml
       <!--实现自动装配
           bean标签属性autowire，配置自动装配
           autowire常用属性值：
           byName：根据属性名称注入，注入值bean的id要与类属性名称一致
           byType：根据属性类型注入，当有多个相同属性类型的bean时，会报错。
       -->
   	<!--根据属性值自动装配-->
       <bean id="emp" class="com.atguigu.spring5.autowire.Emp" autowire="byName">
           <!--<property name="dept" ref="dept"></property>-->
       </bean>
       <bean id="dept" class="com.atguigu.spring5.autowire.Dept"></bean>
   
   	<!--根据属性名称自动装配-->
       <bean id="emp" class="com.atguigu.spring5.autowire.Emp" autowire="byType">
           <!--<property name="dept" ref="dept"></property>-->
       </bean>
       <bean id="dept" class="com.atguigu.spring5.autowire.Dept"></bean>
   ```
   
   

## IOC操作——Bean管理（外部属性文件）

1. 直接配置druid连接池

   1. 导入jar包

   2. 编写xml文件

      ```xml
          <!-- 直接配置数据库连接池 -->
          <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
                destroy-method="close">
              <property name="url" value="jdbc:mysql:///user_db" />
              <property name="username" value="root" />
              <property name="password" value="root" />
              <property name="driverClassName" value="com.mysql.jdbc.Driver" />
          </bean>
      ```

2. 引入外部属性文件配置

   1. 创建外部属性文件

      ```properties
      prop.driverClass=com.mysql.jdbc.Driver
      prop.url=jdbc:mysql:///day18
      prop.username=root
      prop.password=root
      ```

   2. 把properties配置文件引入到spring配置文件中

      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <beans xmlns="http://www.springframework.org/schema/beans"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:context="http://www.springframework.org/schema/context"
             xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                                 http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
      	<!--1.引入context标签-->
          
          <!--2.引入外部属性文件-->
          <context:property-placeholder location="classpath:jdbc.properties" />
      
          <!--3.配置连接池-->
          <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
                destroy-method="close">
              <property name="url" value="${prop.url}" />
              <property name="username" value="${prop.username}" />
              <property name="password" value="${prop.password}" />
              <property name="driverClassName" value="${prop.driverClass}" />
          </bean>
      
      </beans>
      ```



## IOC操作——Bean管理（基于注解方式）

### Spring针对Bean管理中创建对象提供的注解

1. @Component
2. @Service
3. @Controller
4. @Repository

- 上面四个注解实现的功能都是一样的，都可以用来创建bean实例



### 基于注解方式创建对象

1. 操作步骤：
   1. 导入依赖的jar包：spring-aop-5.2.6.jar
   2. 在配置文件中引入context命名空间，再开启组件扫描
   3. 创建类，并在类上面添加创建对象的注解

2. 代码示例：

   ```xml
       <!--开启组件扫描-->
       <!--
           同时扫描多个包
           1.base-package中设置多个值，用逗号隔开
           2.base-package设置这些包的上层目录
       -->
       <context:component-scan base-package="com.atguigu.spring" ></context:component-scan>
   ```

   ```java
   //注解中的value为默认值，
   //value属性可以不设置，默认会使用当前类的类名作为value的属性值，且首字母小写。
   @Component(value = "userService")
   public class UserService {
       public void add() {
           System.out.println("add.......");
       }
   }
   ```

3. 组件扫描中的细节说明

   1. 扫描配置时，默认会扫描com.atguigu.spring包下所有文件
   2. 如果希望扫描包下的某些文件，可以添加**use-default-filters="false"**配置，这样的话，只会扫描符合规则的文件

   ```xml
       <!--use-default-filters="false"后，只会扫描包下符合规则的文件-->
       <context:component-scan base-package="com.atguigu.spring" use-default-filters="false">
           <!--配置扫描规则-->
           <!--这个规则表示只扫描加了@Controller注解的的类-->
           <context:include-filter type="annotation"
                                   expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
   
       <!--扫描所有文件-->
       <context:component-scan base-package="com.atguigu.spring">
           <!--这个规则表示排除加了@Controller注解的类-->
           <context:exclude-filter type="annotation"
                                   expression="org.springframework.stereotype.Controller"/>
       </context:component-scan>
   ```



### 基于注解方式注入属性

1. 用于注入属性的注解

   1. @Autowire：根据属性类型自动装配
   2. @Qualifier：根据属性名称进行注入
      - 这个注解需要和@Autowire一起使用
      - 当某一个接口有多个实现类时，可以使用这个注解定位
   3. @Resource：可以根据类型类型注入，可以根据名称注入
   4. @Value：注入普通类型属性

2. @Autowire注入演示

   ```java
   //UserDao接口的实现类
   @Repository
   public class UserDaoImpl implements UserDao {
       @Override
       public void add() {
           System.out.println("userDao......");
       }
   }
   
   @Service
   public class UserService {
   
       //添加userDao属性
       //不需要添加set方法
       //添加属性注解
       @Autowired
       private UserDao userDao;
   
       public void add() {
           System.out.println("add.......");
           userDao.add();
       }
   }
   ```

3. @Qualifier注入演示

   ```java
   //UserDao接口的实现类之一
   @Repository(value = "userDaoImpl1")
   public class UserDaoImpl implements UserDao {
       @Override
       public void add() {
           System.out.println("userDao......");
       }
   }
   
   @Service
   public class UserService {
   
       //添加userDao属性
       //不需要添加set方法
       //添加属性注解
       @Autowired
       @Qualifier(value = "userDaoImpl1")
       private UserDao userDao;
   
       public void add() {
           System.out.println("add.......");
           userDao.add();
       }
   }
   ```

4. @Resource注解演示：

   ```java
       //@Resource   //根据类型注入
       @Resource(name = "userDaoImpl1")    //根据名称注入
       private UserDao userDao;
   ```

5. @Value注解的使用

   ```java
       //注入普通类型属性
       @Value(value = "abc")
       private String str;
   ```

   

### 完全注解开发

1. 创建配置类，代替xml配置文件

   ```java
   //创建配置类，代替配置文件
   @Configuration
   @ComponentScan(basePackages = {"com.atguigu.spring"})
   public class SpringConfig {
   }
   ```

2. 编写测试类

   ```java
       @Test
       public void test2() {
           //加载配置类
           ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
           UserService userService = context.getBean("userService", UserService.class);
           System.out.println(userService);
           userService.add();
       }
   ```








