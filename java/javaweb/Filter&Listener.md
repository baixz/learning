# Filter和Listener

## Filter：过滤器

1. 概念：

   * web中的过滤器：当访问服务器的资源时，过滤器可以将请求拦截下来，完成一些特殊的功能。
   * 过滤器的作用：一般用于完成通用的操作。如：登录验证、统一编码处理、敏感字符过滤...

2. 快速入门：

   1. 步骤：

      1. 定义一个类，实现接口Filter
      2. 复写方法
      3. 配置拦截路径
         1. web.xml方式配置
         2. 注解方式配置

   2. 代码示例：

      ```java
      @WebFilter("/*")
      public class FilterDemo1 implements Filter {
          public void destroy() {
          }
          public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
              System.out.println("filter被调用了。。。。。。");
              chain.doFilter(req, resp);
          }
          public void init(FilterConfig config) throws ServletException {
          }
      }
      ```

   3. 过滤器详解：

      1. web.xml配置：

         ```xml
         <filter>
             <filter-name>demo1</filter-name>
             <filter-class>cn.itcast.web.filter.FilterDemo1</filter-class>
         </filter>
         <filter-mapping>
             <filter-name>demo1</filter-name>
             <!-- 拦截路径 -->
             <url-pattern>/*</url-pattern>
         </filter-mapping>
         ```

      2. 过滤器执行流程

         1. 执行过滤器
         2. 执行放行后的资源
         3. 回来执行过滤器放行代码下边的代码

      3. 过滤器生命周期方法
          1. init:在服务器启动后，会创建Filter对象，然后调用init方法。只执行一次。用于加载资源
          2. doFilter:每一次请求被拦截资源时，会执行。执行多次
          3. destroy:在服务器关闭后，Filter对象被销毁。如果服务器是正常关闭，则会执行destroy方法。只执行一次。用于释放资源
      4. 过滤器配置详解
          * 拦截路径配置：
              1. 具体资源路径： /index.jsp   只有访问index.jsp资源时，过滤器才会被执行
              2. 拦截目录： /user/*	访问/user下的所有资源时，过滤器都会被执行
              3. 后缀名拦截： *.jsp		访问所有后缀名为jsp资源时，过滤器都会被执行
              4. 拦截所有资源：/*		访问所有资源时，过滤器都会被执行
          * 拦截方式配置：资源被访问的方式
              * 注解配置：
                  * 设置dispatcherTypes属性
                      1. REQUEST：默认值。浏览器直接请求资源
                      2. FORWARD：转发访问资源
                      3. INCLUDE：包含访问资源
                      4. ERROR：错误跳转资源
                      5. ASYNC：异步访问资源
                      
                      ```java
                      //浏览器直接请求index.jsp资源时，该过滤器会被执行
                      //@WebFilter(value="/index.jsp",dispatcherTypes = DispatcherType.REQUEST)
                      //只有转发访问index.jsp时，该过滤器才会被执行
                      //@WebFilter(value="/index.jsp",dispatcherTypes = DispatcherType.FORWARD)
                      //浏览器直接请求index.jsp或者转发访问index.jsp。该过滤器才会被执行
                      //@WebFilter(value="/*",dispatcherTypes ={ DispatcherType.FORWARD,DispatcherType.REQUEST})
                      ```
              * web.xml配置
                  * 设置<dispatcher></dispatcher>标签即可
          
      5. 过滤器链(配置多个过滤器)
          * 执行顺序：如果有两个过滤器：过滤器1和过滤器2
              1. 过滤器1
              2. 过滤器2
              3. 资源执行
              4. 过滤器2
              5. 过滤器1 

          * 过滤器先后顺序问题：
              1. 注解配置：按照类名的字符串比较规则比较，值小的先执行
                  * 如： AFilter 和 BFilter，AFilter就先执行了。
              2. web.xml配置： <filter-mapping>谁定义在上边，谁先执行

   4. 案例：

      1. 案例1：登录验证
         * 需求：
             1. 访问day17_case案例的资源。验证其是否登录
             2. 如果登录了，则直接放行。
             3. 如果没有登录，则跳转到登录页面，提示"您尚未登录，请先登录"
             
         * 分析：
             * 排除登录相关的资源
               * 是，直接放行
               * 不是，判断是否登录
                 * 已经登录，放行
                 * 没有登录，跳转到登录页面。
             
         * 示例代码：
         
             ```java
             @WebFilter("/*")
             public class LoginFilter implements Filter {
                 public void destroy() {
                 }
                 
                 public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
                     HttpServletRequest request = (HttpServletRequest) req;
                     //获取资源请求路径
                     String uri = request.getRequestURI();
                     //判断是否包含登录相关资源路径,要注意排除掉 css/js/图片/验证码等资源
                     if (uri.contains("/login.jsp") || uri.contains("/loginUserServlet") || uri.contains("/css/") || uri.contains("/fonts/") ||
                         uri.contains("/js/") || uri.contains("/checkCode")) {
                         //包含这些，表示用户想要登陆，允许放行
                         chain.doFilter(req, resp);
                     } else {
                         //不包含这些信息，需要验证用户是否已经登录
                         //从session中获取user
                         Object user = request.getSession().getAttribute("admin");
                         if (user != null) {
                             //登录了，放行
                             chain.doFilter(req, resp);
                         } else {
                             //没有登录过
                             request.setAttribute("loginMessage", "您尚未登录，请登录");
                             request.getRequestDispatcher("/login.jsp").forward(request, resp);
                         }
                     }
                     //chain.doFilter(req, resp);
                 }
                 
                 public void init(FilterConfig config) throws ServletException {
                 }
             }
             ```
         
      2. 案例2：敏感词汇过滤
          * 需求：
              1. 对day17_case案例录入的数据进行敏感词汇过滤
              2. 敏感词汇参考《敏感词汇.txt》
              3. 如果是敏感词汇，替换为 *** 
              
          * 分析：
              1. 对request对象进行增强。增强获取参数相关方法
              2. 放行。传递代理对象
              
          * 增强对象的功能：
              * 设计模式：一些通用的解决固定问题的方式
                  1. 装饰模式
                  2. 代理模式
                     * 概念：
                       1. 真实对象：被代理的对象
                       2. 代理对象：
                       3. 代理模式：代理对象代理真实对象，达到增强真实对象功能的目的
                     * 实现方式：
                       1. 静态代理：有一个类文件描述代理模式
                       2. 动态代理：在内存中形成代理类
                          * 实现步骤：
                            1. 代理对象和真实对象实现相同的接口
                            2. 代理对象 = Proxy.newProxyInstance();
                            3. 使用代理对象调用方法
                            4. 增强方法
                            
                          * 增强方式：
                            1. 增强参数列表
                            2. 增强返回值类型
                            3. 增强方法体执行逻辑
                            
                          * 动态代理代码示例：
                          
                            ```java
                            /*接口*/
                            public interface SaleComputer {
                                public String sale(double money);
                                public void show();
                            }
                            
                            /*实现接口的真实方法*/
                            public class Lenovo implements SaleComputer {
                                @Override
                                public String sale(double money) {
                                    System.out.println("花了" + money + "元买了一台联想电脑。。。。。。");
                                    return "联想电脑";
                                }
                                @Override
                                public void show() {
                                    System.out.println("展示电脑。。。。。。");
                                }
                            }
                            
                            /*动态代理方法*/
                            public class ProxyTest {
                                public static void main(String[] args) {
                                    //创建真实对象
                                    Lenovo lenovo = new Lenovo();
                            
                                    //动态代理增强lenovo对象
                                    /*
                                        三个参数：
                                            1. 类加载器：真实对象.getClass().getClassLoader()
                                            2. 接口数组：真实对象.getClass().getInterfaces()
                                            3. 处理器：new InvocationHandler()
                                     */
                                    SaleComputer proxy_lenovo = (SaleComputer)Proxy.newProxyInstance(lenovo.getClass().getClassLoader(), lenovo.getClass().getInterfaces(), new InvocationHandler() {
                                            /*
                                                代理逻辑编写的方法：代理对象调用的所有方法都会触发该方法执行
                                                参数：
                                                    1. proxy:代理对象
                                                    2. method：代理对象调用的方法，被封装为的对象
                                                    3. args:代理对象调用的方法时，传递的实际参数
                                             */
                                                @Override
                                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                                    //判断是否是sale方法
                                                    if (method.getName().equals("sale")) {
                                                        //1.增强参数
                                                        double money = (double) args[0];
                                                        money = money * 1.25;
                                                        //2.增强方法体
                                                        System.out.println("专车接你。。。");
                                                        //使用真实对象调用该方法
                                                        String obj = (String) method.invoke(lenovo, money);
                                                        System.out.println("免费送货...");
                                                        //3.增强返回值
                                                        return obj + "，鼠标垫";
                                                    } else {
                                                        //其他方法不做处理
                                                        Object obj = method.invoke(lenovo, args);
                                                        return obj;
                                                    }
                                                }
                                            });
                            
                                    //调用方法
                                    String sale = proxy_lenovo.sale(8000);
                                    System.out.println(sale);
                                }
                            }
                            ```
                          
                            
              
          * 铭感词过滤代码示例：
          
              ```java
              /**
               * 敏感词汇过滤器
               */
              @WebFilter("/*")
              public class SensitiveWordsFilter implements Filter {
                  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
                      //1.创建代理对象，增强getParameter方法
                      ServletRequest proxy_req = (ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {
                          @Override
                          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                              //增强getParameter方法
                              //判断是否是getParameter方法
                              if(method.getName().equals("getParameter")){
                                  //增强返回值
                                  //获取返回值
                                  String value = (String) method.invoke(req,args);
                                  if(value != null){
                                      for (String str : list) {
                                          if(value.contains(str)){
                                              value = value.replaceAll(str,"***");
                                          }
                                      }
                                  }
                                  return  value;
                              }
                              //判断方法名是否是 getParameterMap
                              //判断方法名是否是 getParameterValue
                              return method.invoke(req,args);
                          }
                      });
              
                      //2.放行
                      chain.doFilter(proxy_req, resp);
                  }
                  private List<String> list = new ArrayList<String>();//敏感词汇集合
                  public void init(FilterConfig config) throws ServletException {
                      try{
                          //1.获取文件真实路径
                          ServletContext servletContext = config.getServletContext();
                          String realPath = servletContext.getRealPath("/WEB-INF/classes/敏感词汇.txt");
                          //2.读取文件
                          BufferedReader br = new BufferedReader(new FileReader(realPath));
                          //3.将文件的每一行数据添加到list中
                          String line = null;
                          while((line = br.readLine())!=null){
                              list.add(line);
                          }
                          br.close();
                          //System.out.println(list);
                      }catch (Exception e){
                          e.printStackTrace();
                      }
                  }
                  public void destroy() {
                  }
              }
              ```
              

## Listener：监听器

* 事件监听机制

  * 事件：一件事情
  * 事件源 ：事件发生的地方
  * 监听器 ：一个对象
  * 注册监听：将事件、事件源、监听器绑定在一起。 当事件源上发生某个事件后，执行监听器代码

* ServletContextListener:监听ServletContext对象的创建和销毁

  * 方法：

    * void contextDestroyed(ServletContextEvent sce) ：ServletContext对象被销毁之前会调用该方法
    * void contextInitialized(ServletContextEvent sce) ：ServletContext对象创建后会调用该方法

  * 步骤：

    1. 定义一个类，实现ServletContextListener接口

    2. 复写方法

    3. 配置

       1. web.xml

          ```xml
          <listener>
           <listener-class>cn.itcast.web.listener.ContextLoaderListener</listener-class>
          </listener>
          <!--指定初始化参数-->
          ```

       2. 注解：

          * @WebListener