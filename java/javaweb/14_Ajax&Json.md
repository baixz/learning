# Ajax&Json

- ajax
- json

## ajax

### 概念

ASynchronous JavaScript And XML：异步的JavaScript 和 XML

异步和同步：客户端和服务器端相互通信的基础上

- 客户端必须等待服务器端的响应。在等待的期间客户端不能做其他操作。
- 客户端不需要等待服务器端的响应。在服务器处理请求的过程中，客户端可以进行其他的操作。

### 实现方式：

1. 原生的JS实现方式

2. JQeury实现方式

   1. $.ajax()

      - 语法：$.ajax({键值对});

        ```js
        //使用$.ajax()发送异步请求
        $.ajax({
            url:"ajaxServlet1111" , // 请求路径
            type:"POST" , //请求方式
            //data: "username=jack&age=23",//请求参数
            data:{"username":"jack","age":23},
            success:function (data) {
                alert(data);
            },//响应成功后的回调函数
            error:function () {
                alert("出错啦...")
            },//表示如果请求响应出现错误，会执行的回调函数
            dataType:"text"//设置接受到的响应数据的格式
        });
        ```

   2. $.get()：发送get请求

      - 语法：$.get(url, [data], [callback], [type])
        - 参数：
          - url：请求路径
          - data：请求参数
          - callback：回调函数
          - type：响应结果的类型

   3. $.post()：发送post请求

      - 语法：$.post(url, [data], [callback], [type])
        - 
        - 参数：
          - url：请求路径
          - data：请求参数
          - callback：回调函数
          - type：响应结果的类型

## Json

### 概念

JavaScript Object Notation：JavaScript对象表示法

```
Person p = new Person();
p.setName("张三");
p.setAge(23);
p.setGender("男");

var p = {"name":"张三","age":23,"gender":"男"};
```

- json现在多用于存储和交换文本信息的语法
- 进行数据的传输
- JSON 比 XML 更小、更快，更易解析。

### 语法

1. 基本规则

   - 数据在名称/值对中：json数据是由键值对构成的
     - 键用引号(单双都行)引起来，也可以不使用引号
     - 取值类型：
       1. 数字（整数或浮点数）
          2. 字符串（在双引号中）
       3. 逻辑值（true 或 false）
          4. 数组（在方括号中）	{"persons":[{},{}]}
          5. 对象（在花括号中） {"address":{"province"："陕西"....}}
          6. null
   - 数据由逗号分隔：多个键值对由逗号分隔
   - 花括号保存对象：使用{}定义json 格式
   - 方括号保存数组：[]

2. 获取数据

   1. json对象.键名

   2. json对象["键名"]

   3. 数组对象[索引]

   4. 遍历：

      ```js
      //1.定义基本格式
      var person = {"name": "张三", age: 23, 'gender': true};
      var ps = [{"name": "张三", "age": 23, "gender": true},
                {"name": "李四", "age": 24, "gender": true},
                {"name": "王五", "age": 25, "gender": false}];
      
      //获取person对象中所有的键和值
      //for in 循环
      for(var key in person){
          //这样的方式获取不行。因为相当于  person."name"
          //alert(key + ":" + person.key);
          alert(key+":"+person[key]);
      }
      
      //获取ps中的所有值
      for (var i = 0; i < ps.length; i++) {
          var p = ps[i];
          for(var key in p){
              alert(key+":"+p[key]);
          }
      }
      ```

3. JSON数据和Java对象的相互转换

   * 使用json解析器完成转换工作

   1. json转换为java对象

      1. 导入jackson的相关jar包

      2. 创建Jackson核心对象 ObjectMapper

      3. 调用ObjectMapper的相关方法进行转换

         1. readValue(json字符串数据,Class)

      4. 示例：

         ```java
         //演示 JSON字符串转为Java对象
         @Test
         public void test5() throws Exception {
            //1.初始化JSON字符串
             String json = "{\"gender\":\"男\",\"name\":\"张三\",\"age\":23}";
         
             //2.创建ObjectMapper对象
             ObjectMapper mapper = new ObjectMapper();
             //3.转换为Java对象 Person对象
             Person person = mapper.readValue(json, Person.class);
         
             System.out.println(person);
         }
         ```

   2. java对象转换为json

      1. 使用步骤：

         1. 导入jackson的相关jar包

         2. 创建Jackson核心对象 ObjectMapper

         3. 调用ObjectMapper的相关方法进行转换

            1. 转换方法：

               - writeValue(参数1，obj)：
                 - 参数1：
                   - File：将obj对象转换为JSON字符串，并保存到指定的文件中
                   - Writer：将obj对象转换为JSON字符串，并将json数据填充到字符输出流中
                   - OutputStream：将obj对象转换为JSON字符串，并将json数据填充到字节输出流中
               - writeValueAsString(obj)：将对象转为json字符串

            2. 注解：

               1. @JsonIgnore：排除属性。
               2. @JsonFormat：属性值得格式化
                  - @JsonFormat(pattern = "yyyy-MM-dd")

            3. 示例：

               ```java
               public class Person {
                   private String name;
                   private int age ;
                   private String gender;
                   //@JsonIgnore // 忽略该属性
                   @JsonFormat(pattern = "yyyy-MM-dd")
                   private Date birthday;
               }
               
               //Java对象转为JSON字符串
               @Test
               public void test1() {
                   //1.创建Person对象
                   Person p  = new Person();
                   p.setName("张三");
                   p.setAge(23);
                   p.setGender("男");
                   p.setBirthday(new Date());
                   //2.创建Jackson的核心对象  ObjectMapper
                   ObjectMapper mapper = new ObjectMapper();
                   //3.转换
                   String json = mapper.writeValueAsString(p);
                   System.out.println(json);
               }
               
               @Test
               public void test3() throws Exception {
                   //1.创建Person对象
                   Person p = new Person();
                   p.setName("张三");
                   p.setAge(23);
                   p.setGender("男");
                   p.setBirthday(new Date());
               	//P1,P2省略
               
                   //创建List集合
                   List<Person> ps = new ArrayList<Person>();
                   ps.add(p);
               
                   //2.转换
                   ObjectMapper mapper = new ObjectMapper();
                   String json = mapper.writeValueAsString(ps);
                   // [{},{},{}]
                   System.out.println(json);
               }
               
               @Test
               public void test4() throws Exception {
                   //1.创建map对象
                   Map<String,Object> map = new HashMap<String,Object>();
                   map.put("name","张三");
                   map.put("age",23);
                   map.put("gender","男");
               
                   //2.转换
                   ObjectMapper mapper = new ObjectMapper();
                   String json = mapper.writeValueAsString(map);
                   System.out.println(json);//{"gender":"男","name":"张三","age":23}
               }
               ```

            4. 复杂java对象转换

               1. List：数组
               2. Map：对象格式一致

## 案例

### 校验用户名是否存在

1. 服务器响应的数据，在客户端使用时，要想当做json数据格式使用。有两种解决方案：

   1. $.get(type):将最后一个参数type指定为"json"

   2. 在服务器端设置MIME类型

      response.setContentType("application/json;charset=utf-8");

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script src="js/jquery-3.3.1.min.js"></script>
    <script>
      //在页面加载完成后
      $(function () {
        //绑定blur事件，当元素失去焦点时触发
        $("#uname").blur(function () {
          //获取输入框的文本
          let username = $(this).val()
          //发送ajax请求
          //期望服务器响应数据格式
          //{"userExsit":true,"msg":"此用户名太受欢迎,请更换一个"}
          //{"userExsit":false,"msg":"用户名可用"}
          $.get("findUserServlet",{username:username},function (data) {
            //判断userExist键的值是否为true
            let span = $("#msg_username");
            if (data.userExist) {
              //用户名存在
              span.css("color", "red");
              span.html(data.msg);
            } else {
              span.css("color", "green");
              span.html(data.msg);
            }
          });
        })
      });
    </script>
  </head>
  <body>
  <form>
    <input type="text" id="uname" name="username" placeholder="请输入用户名">
    <span id="msg_username"></span>
    <br>
    <input type="password" name="password" placeholder="请输入密码"><br>
    <input type="submit" value="登录">
  </form>
  </body>
</html>
```

```java
@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名
        String username = request.getParameter("username");
        //调用service层判断用户名是否已经存在
        //期望服务器响应数据格式
        //{"userExsit":true,"msg":"此用户名太受欢迎,请更换一个"}
        //{"userExsit":false,"msg":"用户名可用"}
        //设置响应的数据格式为json
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();

        if ("tom".equals(username)){
            map.put("userExist", true);
            map.put("msg", "此用户名太受欢迎,请更换一个");
        } else {
            map.put("userExist", false);
            map.put("msg", "用户名可用");
        }

        //将map转换为json，并传递给客户端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), map);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
```
