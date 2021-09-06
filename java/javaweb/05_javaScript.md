# javaScript

* js编写位置

  1. 可以编写到标签的指定属性中

     ```html
     <button onclick="alert('hello');">我是按钮</button>
     <a href="javascript:alert('aaa');">超链接</a>
     ```

  2. 可以编写到script标签中

     ```html
     <script type="text/javascript">
     	//编写js代码
     </script>
     ```

  3. 将代码编写到外部的js文件，然后通过标签将其引入

     ```html
     <script type="text/javascript" src="文件路径"></script>
     ```

* 输出语句

  1. alert("要输出的内容");
     * 该语句会在浏览器窗口中弹出一个警告框
  2. document.write("要输出的内容");
     * 该内容将会被写到body标签中，并在页面中显示
  3. console.log("要输出的内容");
     * 该内容会被写到开发者工具的控制台中

## ECMAScript

1. 基本语法

   1. 与html结合方式

      1. 内部js
         * 定义<script>，标签体内容就是js代码
      2. 外部JS：
         * 定义<script>，通过src属性引入外部的js文件

   2. 注释

      1. 单行注释：//注释内容
      2. 多行注释：/* 注释内容 */

   3. 数据类型：

      * js中一共有6中数据类型：

        1. String 字符串
           * JS中的字符串需要使用引号引起来双引号或单引号都行
           * 在字符串中使用 \ 作为转义字符
           * 使用typeof运算符检查字符串时，会返回"string"
        2. Number 数值
           * JS中所有的整数和浮点数都是Number类型
           * 特殊的数字
             * Infinity 正无穷
             * -Infinity 负无穷
             * NaN 非法数字
           * 使用typeof检查一个Number类型的数据时，会返回"number"
        3. Boolean 布尔值
           * 布尔值主要用来进行逻辑判断，布尔值只有两个：true和false
           * 使用typeof检查一个布尔值时，会返回"boolean"
        4. Null 空值
           * 空值专门用来表示为空的对象，Null类型的值只有一个：null
           * 使用typeof检查一个Null类型的值时会返回"object"
        5. Undefined 未定义
           * 如果声明一个变量但是没有为变量赋值此时变量的值就是undefined
           * 该类型的值只有一个 undefined
           * 使用typeof检查一个Undefined类型的值时，会返回"undefined"
        6. Object 对象
           * 引用数据类型，函数、类对象、null等都属于object类型

      * 数据类型转换

        * 转换为string

          1. 调用被转换数据的toString()方法：
             * 注意：这个方法不适用于null和undefined
          2. 调用String()函数：
             * 对于Number Boolean String都会调用他们的toString()方法来将其转换为字符串，对于null值，直接转换为字符串"null"。对于undefined直接转换为字符串"undefined"
          3. 为任意的数据类型 +""：

        * 转换为number：

          1. 调用Number()函数

             * 字符串 --> 数字
               * 如果字符串是一个合法的数字，则直接转换为对应的数字
               * 如果字符串是一个非法的数字，则转换为NaN
               * 如果是一个空串或纯空格的字符串，则转换为0
             * 布尔值 --> 数字
               * true转换为1
               * false转换为0
             * 空值 --> 数字
               * null转换为0
             * 未定义 --> 数字
               * undefined 转换为NaN

          2. 调用parseInt()或parseFloat()

             * parseInt()

               * 可以将一个字符串中的有效的整数位提取出来，并转换为Number

                 var a = "123.456px";

                 a = parseInt(a); //123

                 * 如果需要可以在parseInt()中指定一个第二个参数，来指定进制

             * parseFloat()

               * 可以将一个字符串中的有效的小数位提取出来，并转换为Number

                 var a = "123.456px";
                 a = parseFloat(a); //123.456

          3. 使用一元的+来进行隐式的类型转换

             var a = "123";
             a = +a;

        * 转换为布尔值

          1. 使用Boolean()函数
             * 字符串 --> 布尔
               * 除了空串其余全是true
             * 数值 --> 布尔
               * 除了0和NaN其余的全是true
             * null、undefined ---> 布尔
               * 都是false
             * 对象 ---> 布尔
               * 都是true
          2. 为任意的数据类型做两次非运算，即可将其转换为布尔值

   4. 变量

      * 语法：
           * var 变量名 = 初始化值;
      * typeof运算符：获取变量的类型。
        * 注：null运算后得到的是object

   5. 运算符

      1.  一元运算符：只有一个运算数的运算符
         ++，-- ， +(正号)  
            * ++ --: 自增(自减)
            * ++(--) 在前，先自增(自减)，再运算
            * ++(--) 在后，先运算，再自增(自减)
           * +(-)：正负号
            * 注意：在JS中，如果运算数不是运算符所要求的类型，那么js引擎会自动的将运算数进行类型转换
              * 其他类型转number：
                * string转number：按照字面值转换。如果字面值不是数字，则转为NaN（不是数字的数字）
                * boolean转number：true转为1，false转为0
      2. 算数运算符
      3. 赋值运算符
      4. 比较运算符
         * 比较方式：
           1. 类型相同：直接比较
              * 字符串：按照字典顺序比较。按位逐一比较，直到得出大小为止。
           2. 类型不同：先进行类型转换，再比较
              * ===：全等于。在比较之前，先判断类型，如果类型不一样，则直接返回false
      5. 逻辑运算符
         * 其他类型转boolean：
           1. number：0或NaN为假，其他为真
           2. string：除了空字符串("")，其他都是true
           3. null&undefined:都是false
           4. 对象：所有对象都为true
      6. 语法：
         * 语法：表达式 ? 值1:值2;
         * 判断表达式的值，如果是true则取值1，如果是false则取值2；

   6. 流程控制语句：

      1. if...else...
      2. switch：
         * 在js中,switch语句可以接受任意的原始数据类型
      3. while
      4. do...while
      5. for

2. 基本对象

   1. Function：函数对象

      1. 创建：

         1. function 函数名([形参1,形参2...形参N]){
            	语句...
            }

         2. var 函数名 = function([形参1,形参2...形参N]){
                语句...
            };

         3. 立即执行函数

            (function(){

            ​    语句...

            })();

      2. 方法：

         * call()和apply()
           * 这两个方法都是函数对象的方法需要通过函数对象来调用
           * 通过两个方法可以直接调用函数，并且可以通过第一个实参来指定函数中this
           * 不同的是call是直接传递函数的实参而apply需要将实参封装到一个数组中传递

      3. 属性：

         * this（上下文对象）
           * 我们每次调用函数时，解析器都会将一个上下文对象作为隐含的参数传递进函数。使用this来引用上下文对象，根据函数的调用形式不同，this的值也不同。
           * this的不同的情况：
             1. 以函数的形式调用时，this是window
             2. 以方法的形式调用时，this就是调用方法的对象
             3. 以构造函数的形式调用时，this就是新创建的对象
             4. 使用call和apply调用时，this是指定的那个对象
             5. 在全局作用域中this代表window
         * arguments

           * arguments和this类似，都是函数中的隐含的参数
           * arguments是一个类数组元素，它用来封装函数执行过程中的实参。所以即使不定义形参，也可以通过arguments来使用实参
           * arguments中有一个属性callee表示当前执行的函数对象

         * length：代表形参的个数

      4. 特点：

         1. 方法定义时，形参的类型不用写,返回值类型也不写。
         2. 方法是一个对象，如果定义名称相同的方法，会覆盖
         3. 在JS中，方法的调用只与方法的名称有关，和参数列表无关
         4. 在方法声明中有一个隐藏的内置对象（数组），arguments,封装所有的实际参数

      5. 调用：

         * 方法名称(实际参数列表);

   2. Array：数组对象

      1. 创建：

         1. var arr = new Array(元素列表);
         2. var arr = new Array(默认长度);
         3. var arr = [元素列表];

      2. 方法：

         * join()：可以将一个数组转换为一个字符串
           * 参数：需要一个字符串作为参数，这个字符串将会作为连接符来连接数组中的元素。如果不指定连接符则默认使用逗号隔开。

         * push()：用来向数组的末尾添加一个或多个元素，并返回数组新的长度

           * 语法：数组.push(元素1,元素2,元素N)

         * pop()：用来删除数组的最后一个元素，并返回被删除的元素

         * unshift()：向数组的前边添加一个或多个元素，并返回数组的新的长度

         * shift()：删除数组的前边的一个元素，并返回被删除的元素

         * slice()：可以从一个数组中截取指定的元素

           * 该方法不会影响原数组，而是将截取到的内容封装为一个新的数组并返回

           * 参数：

             * 参数1：截取开始位置的索引（包括开始位置）

             * 参数2：截取结束位置的索引（不包括结束位置）

             * 第二个参数可以省略不写，如果不写则一直截取到最后
             * 参数可以传递一个负值，如果是负值，则从后往前数

         * splice()：可以用来删除数组中指定元素，并使用新的元素替换

           * 该方法会将删除的元素封装到新数组中返回
           * 参数：
             * 参数1：删除开始位置的索引
             * 参数2：删除的个数
             * 从第三个参数开始都是替换的元素，这些元素将会插入到开始位置索引的前边

         * reverse()：可以用来反转一个数组，它会对原数组产生影响

         * concat()：可以连接两个或多个数组，它不会影响原数组，而是新数组作为返回值返回

         * sort()：可以对一个数组中的内容进行排序，默认是按照Unicode编码进行排序
           	调用以后，会直接修改原数组。

           * 可以自己指定排序的规则，需要一个回调函数作为参数：

             ```js
             function(a,b){
                 //升序排列
                 //return a-b;
                 //降序排列
                 return b-a;
             }
             ```

      3. 属性：

         * length:数组的长度

      4. 调用：

         * 一般情况我们都是使用for循环来遍历数组

         * 使用forEach()方法来遍历数组：

           * 语法格式：

             ```js
             //value:正在遍历的元素
             //index:正在遍历元素的索引
             //obj:被遍历对象
             数组.forEach(function(value , index , obj){
             
             });
             ```

           * 说明：

             * forEach()方法需要一个回调函数作为参数，数组中有几个元素，回调函数就会被调用几次；

             * 每次调用时，都会将遍历到的信息以实参的形式传递进来，可以定义形参来获取这些信息。

   3. Boolean

   4. Date

      1. 创建：
         * var date = new Date();
      2. 方法：
         * toLocaleString()：返回当前date对象对应的时间本地字符串格式
         * getTime():获取毫秒值。返回当前如期对象描述的时间到1970年1月1日零点的毫秒值差

   5. Math

      1. 创建：
         * Math对象不用创建，直接使用。Math.方法名();
      2. 方法：
         * random()：返回 0 ~ 1 之间的随机数。 含0不含1
         * ceil(x)：对数进行上舍入。
         * floor(x)：对数进行下舍入。
         * round(x)：把数四舍五入为最接近的整数。
      3. 属性：
         * PI：圆周率

   6. Number

   7. String

   8. RegExp

      * 创建正则表达式：

        * var reg = new RegExp("正则","匹配模式");
        * var reg = /正则表达式/匹配模式

      * 语法：

        * 匹配模式：

          * i：忽略大小写
          * g：全局匹配模式
          * 设置匹配模式时，可以都不设置，也可以设置1个，也可以全设置，设置时没有顺序要求

        * 正则语法：

          ```
          | 		或
          [] 		或
          [^ ] 	除了
          [a-z] 	小写字母
          [A-Z] 	大写字母
          [A-z] 	任意字母
          [0-9] 	任意数字
          
          {n} 	正好n次
          {m,n} 	m-n次
          {m,} 	至少m次
          +		至少1次 {1,}
          ?   	0次或1次 {0,1}
          *   	0次或多次 {0,}
          
          \ 		在正则表达式中使用\作为转义字符
          \. 		表示.
          \\ 		表示\
          . 		表示任意字符
          \w		相当于[A-z0-9_]
          \W		相当于[^A-z0-9_]
          \d		任意数字
          \D		除了数字
          \s		空格
          \S		除了空格
          \b		单词边界
          \B		除了单词边界
          ^ 		表示开始
          $ 		表示结束
          ```

      * 方法：

        * test()：可以用来检查一个字符串是否符合正则表达式，如果符合返回true，否则返回false

   9. Global

      * 特点：全局对象，这个Global中封装的方法不需要对象就可以直接调用。  方法名();

      2. 方法：
          
          * encodeURI()：url编码
          * decodeURI()：url解码
          
          * encodeURIComponent()：url编码,编码的字符更多
          * decodeURIComponent()：url解码
          
          * parseInt():将字符串转为数字
            * 逐一判断每一个字符是否是数字，直到不是数字为止，将前边数字部分转为number
          * isNaN():判断一个值是否是NaN
            * NaN六亲不认，连自己都不认。NaN参与的==比较全部问false
          
          * eval():讲 JavaScript 字符串，并把它作为脚本代码来执行。

## BOM

1. 概念：Browser Object Model 浏览器对象模型
    * 将浏览器的各个组成部分封装成对象。

2. 组成：

    * Window：窗口对象
    * Navigator：浏览器对象
    * Screen：显示器屏幕对象
    * History：历史记录对象
    * Location：地址栏对象

3. Window：窗口对象

    1. 创建
    2. 方法
         1. 与弹出框有关的方法：

            * alert(str)：显示带有一段消息和一个确认按钮的警告框。
            * confirm(str)：显示带有一段消息以及确认按钮和取消按钮的对话框。
              * 如果用户点击确定按钮，则方法返回true
              * 如果用户点击取消按钮，则方法返回false
            * prompt(str)：显示可提示用户输入的对话框。
                 * 返回值：获取用户输入的值

         2. 与打开关闭有关的方法：

            * close()：关闭浏览器窗口。
              * 谁调用我 ，我关谁
            * open()：打开一个新的浏览器窗口
              * 返回新的Window对象

         3. 与定时器有关的方式

            * setTimeout()	在指定的毫秒数后调用函数或计算表达式。
              * 参数：
                1. js代码或者方法对象
                2. 毫秒值
              * 返回值：唯一标识，用于取消定时器
            * clearTimeout()：取消由 setTimeout() 方法设置的 timeout。

            * setInterval()：按照指定的周期（以毫秒计）来调用函数或计算表达式。
              * 参数：
                1. js代码或者方法对象
                2. 毫秒值
              * 返回值：唯一标识，用于取消定时器
            * clearInterval()：取消由 setInterval() 设置的 timeout。

    3. 属性：
        1. 获取其他BOM对象：
            history
            location
            Navigator
            Screen:
        2. 获取DOM对象
            document
    4. 特点
        * Window对象不需要创建可以直接使用 window使用。 window.方法名();
        * window引用可以省略。  方法名();


4. Location：地址栏对象
    1. 创建(获取)：
        1. window.location
        2. location

    2. 方法：
        * reload()：重新加载当前文档。刷新网页
    3. 属性
        * href：设置或返回完整的URL地址


5. History：历史记录对象
    1. 创建(获取)：
        1. window.history
        2. history

    2. 方法：
        * back()：加载 history 列表中的前一个 URL。
        * forward()：加载 history 列表中的下一个 URL。
        * go(参数)：加载 history 列表中的某个具体页面。
            * 参数：
                * 正数：前进几个历史记录
                * 负数：后退几个历史记录
    3. 属性：
        * length	返回当前窗口历史列表中的 URL 数量。

## DOM

* 概念： Document Object Model 文档对象模型
  * 将标记语言文档的各个组成部分，封装为对象。可以使用这些对象，对标记语言文档进行CRUD的动态操作

* 功能：控制html文档的内容

* 获取页面标签(元素)对象：Element

  * document.getElementById("id值")：通过元素的id获取元素对象

* 操作Element对象：

  1. 修改属性值：
     1. 明确获取的对象是哪一个？
     2. 查看API文档，找其中有哪些属性可以设置
  2. 修改标签体内容：
     * 属性：innerHTML
       1. 获取元素对象
       2. 使用innerHTML属性修改标签体内容

* 核心DOM模型：

  * Document：文档对象
      1. 创建(获取)：在html dom模型中可以使用window对象来获取
          1. window.document
          2. document
      2. 方法：
          1. 获取Element对象：
              1. getElementById()：根据id属性值获取元素对象。id属性值一般唯一
              2. getElementsByTagName()：根据元素名称获取元素对象们。返回值是一个数组
              3. getElementsByClassName()：根据Class属性值获取元素对象们。返回值是一个数组
              4. getElementsByName()：根据name属性值获取元素对象们。返回值是一个数组
          2. 创建其他DOM对象：
              * createAttribute(name)
              * createComment()
              * createElement()
              * createTextNode()
  		3. 属性
  * Element：元素对象
      1. 获取/创建：通过document来获取和创建
      2. 方法：
          1. removeAttribute()：删除属性
          2. setAttribute()：设置属性
  * Node：节点对象，其他5个的父对象
      * 特点：所有dom对象都可以被认为是一个节点
      * 方法：
          * CRUD dom树：
              * appendChild()：向节点的子节点列表的结尾添加新的子节点。
              * removeChild()	：删除（并返回）当前节点的指定子节点。
              * replaceChild()：用新节点替换一个子节点。
      * 属性：
          * parentNode 返回节点的父节点。

* HTML DOM

  1. 标签体的设置和获取：innerHTML

  2. 使用html元素对象的属性

  3. 控制元素样式
      1. 使用元素的style属性来设置，如：

          ```
          //修改样式方式1
          div1.style.border = "1px solid red";
          div1.style.width = "200px";
          //font-size--> fontSize
          div1.style.fontSize = "20px";
          ```

      2. 提前定义好类选择器的样式，通过元素的className属性来设置其class属性值。

## 事件

* 概念：某些组件被执行了某些操作后，触发某些代码的执行。
* 如何绑定事件
  1. 直接在html标签上，指定事件的属性(操作)，属性值就是js代码
     1. 事件：onclick---单击事件
  2. 通过js获取元素对象，指定事件属性，设置一个函数
* 常见的事件：
  1. 点击事件：
      1. onclick：单击事件
      2. ondblclick：双击事件
  2. 焦点事件
      1. onblur：失去焦点
      2. onfocus:元素获得焦点。

  3. 加载事件：
      1. onload：一张页面或一幅图像完成加载。

  4. 鼠标事件：
      1. onmousedown	鼠标按钮被按下。
      2. onmouseup	鼠标按键被松开。
      3. onmousemove	鼠标被移动。
      4. onmouseover	鼠标移到某元素之上。
      5. onmouseout	鼠标从某元素移开。
      
  5. 键盘事件：
      1. onkeydown	某个键盘按键被按下。	
      2. onkeyup		某个键盘按键被松开。
      3. onkeypress	某个键盘按键被按下并松开。

  6. 选择和改变
      1. onchange	域的内容被改变。
      2. onselect	文本被选中。

  7. 表单事件：
      1. onsubmit	确认按钮被点击。
      2. onreset	重置按钮被点击。















