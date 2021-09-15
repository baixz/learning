# 常用类

## String类

* String的不可变性

  1. 当对字符串重新赋值时，需要重写指定内存区域赋值，不能使用原有的value进行赋值。
  2. 当对现的字符串进行连接操作时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值。
  3. 当调用String的replace()方法修改指定字符或字符串时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值。

* 字符串拼接方式赋值的对比

  1. 常量与常量的拼接结果在常量池。且常量池中不会存在相同内容的常量。
  2. 只要其中一个是变量，结果就在堆中。
  3. 如果拼接的结果调用intern()方法，返回值就在常量池中

* 常用方法：

  ```java
  int length()：返回字符串的长度： return value.length
  char charAt(int index)： 返回某索引处的字符return value[index]
  boolean isEmpty()：判断是否是空字符串：return value.length == 0
  String toLowerCase()：使用默认语言环境，将 String 中的所字符转换为小写
  String toUpperCase()：使用默认语言环境，将 String 中的所字符转换为大写
  String trim()：返回字符串的副本，忽略前导空白和尾部空白
  boolean equals(Object obj)：比较字符串的内容是否相同
  boolean equalsIgnoreCase(String anotherString)：与equals方法类似，忽略大小写
  String concat(String str)：将指定字符串连接到此字符串的结尾。 等价于用“+”
  int compareTo(String anotherString)：比较两个字符串的大小
  String substring(int beginIndex)：返回一个新的字符串，它是此字符串的从beginIndex开始截取到最后的一个子字符串。
  String substring(int beginIndex, int endIndex) ：返回一个新字符串，它是此字符串从beginIndex开始截取到endIndex(不包含)的一个子字符串。
  
  boolean endsWith(String suffix)：测试此字符串是否以指定的后缀结束
  boolean startsWith(String prefix)：测试此字符串是否以指定的前缀开始
  boolean startsWith(String prefix, int toffset)：测试此字符串从指定索引开始的子字符串是否以指定前缀开始
  
  boolean contains(CharSequence s)：当且仅当此字符串包含指定的 char 值序列时，返回 true
  int indexOf(String str)：返回指定子字符串在此字符串中第一次出现处的索引
  int indexOf(String str, int fromIndex)：返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始
  int lastIndexOf(String str)：返回指定子字符串在此字符串中最右边出现处的索引
  int lastIndexOf(String str, int fromIndex)：返回指定子字符串在此字符串中最后一次出现处的索引，从指定的索引开始反向搜索
  
  注：indexOf和lastIndexOf方法如果未找到都是返回-1
  
  替换：
  String replace(char oldChar, char newChar)：返回一个新的字符串，它是通过用 newChar 替换此字符串中出现的所 oldChar 得到的。
  String replace(CharSequence target, CharSequence replacement)：使用指定的字面值替换序列替换此字符串所匹配字面值目标序列的子字符串。
  String replaceAll(String regex, String replacement)：使用给定的 replacement 替换此字符串所匹配给定的正则表达式的子字符串。
  String replaceFirst(String regex, String replacement)：使用给定的 replacement 替换此字符串匹配给定的正则表达式的第一个子字符串。
  匹配:
  boolean matches(String regex)：告知此字符串是否匹配给定的正则表达式。
  切片：
  String[] split(String regex)：根据给定正则表达式的匹配拆分此字符串。
  String[] split(String regex, int limit)：根据匹配给定的正则表达式来拆分此字符串，最多不超过limit个，如果超过了，剩下的全部都放到最后一个元素中。
  ```

* String与其它结构的转换

  1. 与基本数据类型、包装类之间的转换

     * String --> 基本数据类型、包装类：调用包装类的静态方法：parseXxx(str)
     * 基本数据类型、包装类 --> String:调用String重载的valueOf(xxx)

  2. 与字符数组之间的转换

     * String --> char[]:调用String的toCharArray()
     * char[] --> String:调用String的构造器

  3. 与字节数组之间的转换

     * 编码：String --> byte[]:调用String的getBytes()
     * 解码：byte[] --> String:调用String的构造器
       * 说明：解码时，要求解码使用的字符集必须与编码时使用的字符集一致，否则会出现乱码。

     ```java
     @Test
     public void test3() throws UnsupportedEncodingException {
         String str1 = "abc123中国";
         byte[] bytes = str1.getBytes();//使用默认的字符集，进行编码。
         System.out.println(Arrays.toString(bytes));
         byte[] gbks = str1.getBytes("gbk");//使用gbk字符集进行编码。
         System.out.println(Arrays.toString(gbks));
         System.out.println("******************");
         String str2 = new String(bytes);//使用默认的字符集，进行解码。
         System.out.println(str2);
         String str3 = new String(gbks);
         System.out.println(str3);//出现乱码。原因：编码集和解码集不一致！
         String str4 = new String(gbks, "gbk");
         System.out.println(str4);//没出现乱码。原因：编码集和解码集一致！
     }
     ```

  4. 与StringBuffer、StringBuilder之间的转换

     * String -->StringBuffer、StringBuilder:调用StringBuffer、StringBuilder构造器
     * StringBuffer、StringBuilder -->String:
       1. 调用String构造器；
       2. StringBuffer、StringBuilder的toString()

## StringBuffer和StringBuilder

* String、StringBuffer、StringBuilder三者的对比
  * String:不可变的字符序列；底层使用char[]存储
  * StringBuffer:可变的字符序列；线程安全的，效率低；底层使用char[]存储
  * StringBuilder:可变的字符序列；jdk5.0新增的，线程不安全的，效率高；底层使用char[]存储

* 对比String、StringBuffer、StringBuilder三者的执行效率

  * 从高到低排列：StringBuilder > StringBuffer > String

* StringBuffer、StringBuilder中的常用方法

  ```java
  增：append(xxx)
  删：delete(int start,int end)
  改：setCharAt(int n ,char ch) / replace(int start, int end, String str)
  查：charAt(int n )
  插：insert(int offset, xxx)
  长度：length();
  遍历：for() + charAt() / toString()
  ```

## JDK8之前的时间API

* System类中的currentTimeMillis()

  ```java
  long time = System.currentTimeMillis();
  //返回当前时间与1970年1月1日0时0分0秒之间以毫秒为单位的时间差。
  //称为时间戳
  System.out.println(time);
  ```

* java.util.Date类与java.sql.Date类

  ```java
  /*
  java.util.Date类
  	|---java.sql.Date类
  1.两个构造器的使用
      >构造器一：Date()：创建一个对应当前时间的Date对象
      >构造器二：创建指定毫秒数的Date对象
  2.两个方法的使用
      >toString():显示当前的年、月、日、时、分、秒
      >getTime():获取当前Date对象对应的毫秒数。（时间戳）
  3. java.sql.Date对应着数据库中的日期类型的变量
      >如何实例化
      >如何将java.util.Date对象转换为java.sql.Date对象
  */
  @Test
  public void test2(){
      //构造器一：Date()：创建一个对应当前时间的Date对象
      Date date1 = new Date();
      System.out.println(date1.toString());//Sat Feb 16 16:35:31 GMT+08:00 2019
      System.out.println(date1.getTime());//1550306204104
      //构造器二：创建指定毫秒数的Date对象
      Date date2 = new Date(155030620410L);
      System.out.println(date2.toString());
      //创建java.sql.Date对象
      java.sql.Date date3 = new java.sql.Date(35235325345L);
      System.out.println(date3);//1971-02-13
      //如何将java.util.Date对象转换为java.sql.Date对象
      //情况一：
  	//Date date4 = new java.sql.Date(2343243242323L);
  	//java.sql.Date date5 = (java.sql.Date) date4;
      //情况二：
      Date date6 = new Date();
      java.sql.Date date7 = new java.sql.Date(date6.getTime());
  }
  ```

* java.text.SimpleDataFormat类

  * SimpleDateFormat对日期Date类的格式化和解析
    1. 格式化：日期 --->字符串
    2. 解析：格式化的逆过程，字符串 ---> 日期
  * SimpleDateFormat的实例化:new + 构造器

  ```java
  //*************照指定的方式格式化和解析：调用带参的构造器*****************
  //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyy.MMMMM.dd GGG hh:mm aaa");
  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  //格式化
  String format1 = sdf1.format(date);
  System.out.println(format1);//2019-02-18 11:48:27
  //解析:要求字符串必须是符合SimpleDateFormat识别的格式(通过构造器参数体现),
  //否则，抛异常
  Date date2 = sdf1.parse("2020-02-18 11:48:27");
  System.out.println(date2);
  ```

* Calendar类：日历类、抽象类

  ```java
  //1.实例化
  //方式一：创建其子类（GregorianCalendar的对象
  //方式二：调用其静态方法getInstance()
  Calendar calendar = Calendar.getInstance();
  //System.out.println(calendar.getClass());
  //2.常用方法
  //get()
  int days = calendar.get(Calendar.DAY_OF_MONTH);
  System.out.println(days);
  System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
  //set()
  //calendar可变性
  calendar.set(Calendar.DAY_OF_MONTH,22);
  days = calendar.get(Calendar.DAY_OF_MONTH);
  System.out.println(days);
  //add()
  calendar.add(Calendar.DAY_OF_MONTH,-3);
  days = calendar.get(Calendar.DAY_OF_MONTH);
  System.out.println(days);
  //getTime():日历类---> Date
  Date date = calendar.getTime();
  System.out.println(date);
  //setTime():Date ---> 日历类
  Date date1 = new Date();
  calendar.setTime(date1);
  days = calendar.get(Calendar.DAY_OF_MONTH);
  System.out.println(days);
  ```

## JDK8之后的时间API

* 本地日期、本地时间、本地日期时间的使用：LocalDate / LocalTime / LocalDateTime

  * LocalDateTime相较于LocalDate、LocalTime，使用频率要高

    ```java
    @Test
    public void test1(){
        //now():获取当前的日期、时间、日期+时间
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDate);
        System.out.println(localTime);
        System.out.println(localDateTime);
    
        //of():设置指定的年、月、日、时、分、秒。没有偏移量
        LocalDateTime localDateTime1 = LocalDateTime.of(2020, 10, 6, 13, 23, 43);
        System.out.println(localDateTime1);
    
        //getXxx()：获取相关的属性
        System.out.println(localDateTime.getDayOfMonth());
        System.out.println(localDateTime.getDayOfWeek());
        System.out.println(localDateTime.getMonth());
        System.out.println(localDateTime.getMonthValue());
        System.out.println(localDateTime.getMinute());
    
        //体现不可变性
        //withXxx():设置相关的属性
        LocalDate localDate1 = localDate.withDayOfMonth(22);
        System.out.println(localDate);
        System.out.println(localDate1);
    
        LocalDateTime localDateTime2 = localDateTime.withHour(4);
        System.out.println(localDateTime);
        System.out.println(localDateTime2);
    
        //不可变性
        LocalDateTime localDateTime3 = localDateTime.plusMonths(3);
        System.out.println(localDateTime);
        System.out.println(localDateTime3);
    
        LocalDateTime localDateTime4 = localDateTime.minusDays(6);
        System.out.println(localDateTime);
        System.out.println(localDateTime4);
    }
    ```

* 时间点：Instant

  ```java
  /*
      Instant的使用
      类似于 java.util.Date类
  */
  @Test
  public void test2(){
      //now():获取本初子午线对应的标准时间
      Instant instant = Instant.now();
      System.out.println(instant);//2019-02-18T07:29:41.719Z
  
      //添加时间的偏移量
      OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.ofHours(8));
      System.out.println(offsetDateTime);//2019-02-18T15:32:50.611+08:00
  
      //toEpochMilli():获取自1970年1月1日0时0分0秒（UTC）开始的毫秒数  ---> Date类的getTime()
      long milli = instant.toEpochMilli();
      System.out.println(milli);
  
      //ofEpochMilli():通过给定的毫秒数，获取Instant实例  -->Date(long millis)
      Instant instant1 = Instant.ofEpochMilli(1550475314878L);
      System.out.println(instant1);
  }
  ```

* 日期时间格式化类：DateTimeFormatter

  ```java
  /*
      DateTimeFormatter:格式化或解析日期、时间
      类似于SimpleDateFormat
   */
  @Test
  public void test3(){
  	//方式一：预定义的标准格式。如：ISO_LOCAL_DATE_TIME;ISO_LOCAL_DATE;ISO_LOCAL_TIME
      DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
      //格式化:日期-->字符串
      LocalDateTime localDateTime = LocalDateTime.now();
      String str1 = formatter.format(localDateTime);
      System.out.println(localDateTime);
      System.out.println(str1);//2019-02-18T15:42:18.797
      //解析：字符串 -->日期
      TemporalAccessor parse = formatter.parse("2019-02-18T15:42:18.797");
      System.out.println(parse);
      //方式二：
      //本地化相关的格式。如：ofLocalizedDateTime()
      //FormatStyle.LONG / FormatStyle.MEDIUM / FormatStyle.SHORT :适用于LocalDateTime
      DateTimeFormatter formatter1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
      //格式化
      String str2 = formatter1.format(localDateTime);
      System.out.println(str2);//2019年2月18日 下午03时47分16秒
  	//本地化相关的格式。如：ofLocalizedDate()
  	//FormatStyle.FULL / FormatStyle.LONG / FormatStyle.MEDIUM / FormatStyle.SHORT : 适用于LocalDate
      DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
      //格式化
      String str3 = formatter2.format(LocalDate.now());
      System.out.println(str3);//2019-2-18
  
  	//重点： 方式三：自定义的格式。如：ofPattern(“yyyy-MM-dd hh:mm:ss”)
      DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
      //格式化
      String str4 = formatter3.format(LocalDateTime.now());
      System.out.println(str4);//2019-02-18 03:52:09
      //解析
      TemporalAccessor accessor = formatter3.parse("2019-02-18 03:52:09");
      System.out.println(accessor);
  }
  ```

## java比较器

* 自然排序：使用Comparable接口

  * 说明：

    1. 像String、包装类等实现了Comparable接口，重写了compareTo(obj)方法，给出了比较两个对象大小的方式。
    2. 像String、包装类重写compareTo()方法以后，进行了从小到大的排列
    3. 重写compareTo(obj)的规则：
       * 如果当前对象this大于形参对象obj，则返回正整数；
       * 如果当前对象this小于形参对象obj，则返回负整数；
       * 如果当前对象this等于形参对象obj，则返回零。
    4. 对于自定义类来说，如果需要排序，我们可以让自定义类实现Comparable接口，重写compareTo(obj)方法。在compareTo(obj)方法中指明如何排序

  * 自定义类代码举例：

    ```java
    public class Goods implements  Comparable{
        private String name;
        private double price;
        
        //指明商品比较大小的方式:照价格从低到高排序,再照产品名称从高到低排序
        @Override
        public int compareTo(Object o) {
            if(o instanceof Goods){
                Goods goods = (Goods)o;
                //方式一：
                if(this.price > goods.price){
                    return 1;
                }else if(this.price < goods.price){
                    return -1;
                }else{
                   	return -this.name.compareTo(goods.name);
                }
                //方式二：
    			//return Double.compare(this.price,goods.price);
            }
    		//return 0;
            throw new RuntimeException("传入的数据类型不一致！");
        }
    	//getter、setter、toString()、构造器：省略
    }
    ```

* 定制排序：使用Comparator接口

  * 说明：
    1. 当元素的类型没实现java.lang.Comparable接口而又不方便修改代码，或者实现了java.lang.Comparable接口的排序规则不适合当前的操作，那么可以考虑使用 Comparator 的对象来排序
    2. 重写compare(Object o1,Object o2)方法，比较o1和o2的大小：
       * 如果方法返回正整数，则表示o1大于o2；
       * 如果返回0，表示相等；
       * 返回负整数，表示o1小于o2。

  * 代码举例：

    ```java
    Comparator com = new Comparator() {
        //指明商品比较大小的方式:照产品名称从低到高排序,再照价格从高到低排序
        @Override
        public int compare(Object o1, Object o2) {
            if(o1 instanceof Goods && o2 instanceof Goods){
                Goods g1 = (Goods)o1;
                Goods g2 = (Goods)o2;
                if(g1.getName().equals(g2.getName())){
                    return -Double.compare(g1.getPrice(),g2.getPrice());
                }else{
                    return g1.getName().compareTo(g2.getName());
                }
            }
            throw new RuntimeException("输入的数据类型不一致");
        }
    }
    ```

  * 使用：

    * Arrays.sort(goods,com);
    * Collections.sort(coll,com);
    * new TreeSet(com);

## 其他类

