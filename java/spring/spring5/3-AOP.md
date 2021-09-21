# AOP

## AOP概念

1. 面向切面编程，利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各个部分之间的耦合度降低，提高代码的可重用性，同时统计高了开发的效率。
2. 通俗描述：不通过修改源代码的方式，在主干功能里添加新功能



## 实现原理

1. AOP底层使用动态代理

   1. 有接口的情况下，使用JDK动态代理

      创建接口实现类的代理对象，通过代理对象增强类的方法

   2. 没有接口的情况下，使用CGLIB动态代理

      创建子类的代理对象，通过代理对象增强类的方法



## AOP(JDK动态代理)

1. JDK动态代理，使用Proxy类里面的方法创建代理对象

   - 调用newProxyInstance方法：

     ![newProxyInstance](newProxyInstance.bmp)

   - 方法参数说明：

     1. 类加载器
     2. 增强方法所在的类，这个类实现的接口，支持多个接口。
     3. 实现这个接口InvocationHandler，创建代理对象，写增强的部分。

2. 编写JDK动态代理的代码

   1. 创建接口，定义方法

      ```java
      public interface UserDao {
          public int add(int a, int b);
          public String update(String id);
      }
      ```

   2. 创建接口实现类，实现方法

      ```java
      public class UserDaoImpl implements UserDao{
          @Override
          public int add(int a, int b) {
              return a+b;
          }
      
          @Override
          public String update(String id) {
              return id;
          }
      }
      ```

   3. 使用Proxy类创建接口代理对象

      ```java
      public class JdkProxy {
          public static void main(String[] args) {
              //创建接口实现类代理对象
              Class[] interfaces = {UserDao.class};
              UserDaoImpl userDao = new UserDaoImpl();
              UserDao dao = (UserDao)Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), interfaces, new UserDaoProxy(userDao));
              int result = dao.add(1, 2);
              System.out.println("result = " + result);
          }
      }
      
      //创建代理对象代码
      class UserDaoProxy implements InvocationHandler {
          //传入代理对象
          private Object obj;
          public UserDaoProxy(Object obj) {
              this.obj = obj;
          }
      
          //增强逻辑
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              //方法之前
              System.out.println("方法之前执行。。。" + method.getName() + " : 传递的参数。。。" + Arrays.toString(args));
              //执行原始的被增强的方法
              Object res = method.invoke(obj, args);
              //方法之后
              System.out.println("方法之后执行。。。" + res);
              return res;
          }
      }
      ```

      

## AOP(操作术语)

1. 连接点

   类中那些可以被增强的方法被称为连接点

2. 切入点

   实际上真正被增强的方法被称为切入点

3. 通知（增强）

   1. 实际增强的逻辑部分被称为通知（增强）
   2. 通知有多种类型
      - 前置通知
      - 后置通知
      - 环绕通知
      - 异常通知
      - 最终通知

4. 切面

   把通知引用到切入点的操作被称为切面



## AOP操作

### AOP操作（准备）

1. Spring框架一般都是基于AspectJ实现AOP操作

   1. AspactJ不是Spring组成部分，是一个独立的AOP框架，一般吧AspactJ和Spring框架一起使用，进行AOP操作。

2. 基于AspectJ实现AOP操作

   1. 基于xml配置文件实现
   2. 基于注解方式实现（使用）

3. 在项目工程中引入AOP依赖

4. 切入点表达式

   1. 切入点表达式的作用：知道对哪个类里面的哪个方法进行增强。

   2. 语法结构

      ```java
      execution([权限修饰符] [返回类型] [类全路径].[方法名称]([参数列表]))
      ```

   3. 举例：

      1. 对com.atguigu.dao.BookDao类里面的add进行增强

         ```java
         execution(* com.atguigu.dao.BookDao.add(..))
         ```

      2. 对com.atguigu.dao.BookDao类里面的所有方法进行增强

         ```java
         execution(* com.atguigu.dao.BookDao.*(..))
         ```

      3. 对com.atguigu.dao包里所有类，类里面的所有方法进行增强

         ```java
         execution(* com.atguigu.dao.*.*(..))
         ```



### AOP操作（AspectJ注解）

1. 创建被增强类，在类里面定义方法

2. 创建增强类（编写增强逻辑）

3. 进行通知配置

   1. 在spring配置文件中添加context和aop名称空间
   2. 在Spring配置文件中开启注解扫描
   3. 使用注解创建增强类和被增强类
   4. 在增强类上添加注解@Aspect
   5. 在Spring配置文件中开启生成代理对象

4. 配置不同类型的通知

   1. 在增强类的里面，在作为通知方法上面添加通知类型注解，使用切入点表达式配置

5. 相同切入点的抽取

6. 有多个增强类对同一个方法进行增强，设置增强类的优先级

   1. 在增强类上面你添加注解@Order(数字类型值)，数字类型值越小，优先级越高

7. 完全使用注解开发

   1. 创建配置类，不需要创建xml配置文件

   ```java
   @Configuration
   @ComponentScan(basePackages = {"com.atguigu.spring"})
   @EnableAspectJAutoProxy(proxyTargetClass = true)
   public class ConfigAop {
   }
   ```

   

### AOP操作（AspectJ配置文件）

1. 创建两个类，增强类和被增强类，创建方法
2. 在spring配置文件中创建两个类对象
3. 在spring配置文件中配置切入点





