# redis

## 概念

redis是一款高性能的NOSQL系列的非关系型数据库



## 安装

1. 下载安装包之后直接解压使用
2. 双击redis-server.exe，启动redis服务
3. redis使用的端口号为：6379，存在风险，需要修改默认端口号





## redis的数据结构

redis存储的是：key，value格式的数据，启动key都是字符串，value有5中数据结构

1. 字符串类型 string
2. 哈希类型 hash：map格式
3. 列表类型 list：linkedlist
4. 集合类型 set：set
5. 有序集合类型 sortedset



## 命令操作

### 字符串

1. 存储：set key value
2. 获取：get key
3. 删除：del key

```
set username zhangsan
get username
set age 23
del age
```



### 哈希类型

1. 存储：hset key field value
2. 获取：
   1. hget key field：获取指定的field对应的值
   2. hgetall key：获取所有field和value
3. 删除：hdel key field

```
hset myhash username lisi
hset myhash password 123
hget myhash username
hgetall myhash
```



### 列表

可以添加一个元素到列表的头部（左边）或者尾部（右边）

1. 添加：
   1. lpush key value：将元素加入列表左表
   2. rpush key value：将元素加入列表右边
2. 获取：
   - lrange key start end ：范围获取
3. 删除：
   1. lpop key：删除列表最左边的元素，并将元素返回
   2. rpop key：删除列表最右边的元素，并将元素返回

```
lpush mylist a
lpush mylist b
lpush mylist c
lrange key mylist 0 -1	//获取所有数据，结果为：b,a,c
lpop mylist
```



### 集合

集合中存储的元素不允许重复

1. 存储：sadd key value
2. 获取：smembers key：获取set集合中所有元素
3. 删除：srem key value：删除set集合中的某个元素

```
sadd myset a
sadd myset a
smembers myset
sadd myset b c d
srem myset a
```



### 有序集合

有序集合中不允许重复元素，每个元素都有序

1. 存储：zadd key score value：存储key和value，并根据分数对value进行排序。
2. 获取：zrange key start end
3. 删除：zrem key value

```
zadd mysort 60 zhangsan
zadd mysort 50 lisi
zadd mysort 80 wangwu
zrange mysort 0 -1		//获取所有数据，结果为：lisi,zhangsan,wangwu
zrange mysort 0 -1 withscores	//获取所有数据，并显示score信息
zrem mysort lisi
```



### 通用命令：

1. keys *：查询所有的键
2. type key：获取指定键对应的value的类型
3. del key：删除指定的key value



## 持久化

redis是一个内存数据库，当redis服务器重启，获取电脑重启，数据会丢失，我们可以将redis内存中的数据持久化保存到硬盘的文件中。

### redis持久化机制

1. RDB：默认方式，不需要进行配置，默认就使用这种机制

   1. 说明：在一定的间隔时间中，检测key的变化情况，然后持久化数据

   2. 修改默认配置：

      1. 编辑redis.windwos.conf文件

         ```
         #   after 900 sec (15 min) if at least 1 key changed
         save 900 1
         #   after 300 sec (5 min) if at least 10 keys changed
         save 300 10
         #   after 60 sec if at least 10000 keys changed
         save 60 10000
         ```

      2. 通过控制台启动redis服务，并指定配置文件名称：redis-server.exe redis.windows.conf

2. AOF：日志记录的方式，可以记录每一条命令的操作。可以每一次命令操作后，持久化数据

   1. 开启AOF：

      1. 编辑redis.windwos.conf文件

         ```
         appendonly no（关闭aof） --> appendonly yes （开启aof）				
         # appendfsync always ： 每一次操作都进行持久化
         appendfsync everysec ： 每隔一秒进行一次持久化
         # appendfsync no	 ： 不进行持久化
         ```

      2. 通过控制台启动redis服务，并指定配置文件名称：redis-server.exe redis.windows.conf



## Jedis

### 使用步骤

1. 下载jedis的jar包

2. 使用

    ```java
    //1. 获取连接
    Jedis jedis = new Jedis("localhost",6379);
    //2. 操作
    jedis.set("username","zhangsan");
    //3. 关闭连接
    jedis.close();
    ```



### Jedis操作各种redis中的数据结构

1. 字符串

   ```java
   //1. 获取连接
   Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379端口
   //2. 操作
   //存储
   jedis.set("username","zhangsan");
   //获取
   String username = jedis.get("username");
   System.out.println(username);
   
   //可以使用setex()方法存储可以指定过期时间的 key value
   jedis.setex("activecode",20,"hehe");//将activecode：hehe键值对存入redis，并且20秒后自动删除该键值对
   
   //3. 关闭连接
   jedis.close();
   ```

   

2. 哈希

   ```java
   //1. 获取连接
   Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379端口
   //2. 操作
   // 存储hash
   jedis.hset("user","name","lisi");
   jedis.hset("user","age","23");
   jedis.hset("user","gender","female");
   
   // 获取hash
   String name = jedis.hget("user", "name");
   System.out.println(name);
   
   // 获取hash的所有map中的数据
   Map<String, String> user = jedis.hgetAll("user");
   
   // keyset
   Set<String> keySet = user.keySet();
   for (String key : keySet) {
       //获取value
       String value = user.get(key);
       System.out.println(key + ":" + value);
   }
   
   //3. 关闭连接
   jedis.close();
   ```

   

3. 列表

   ```java
   //1. 获取连接
   Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379端口
   //2. 操作
   // list 存储
   jedis.lpush("mylist","a","b","c");//从左边存
   jedis.rpush("mylist","a","b","c");//从右边存
   
   // list 范围获取
   List<String> mylist = jedis.lrange("mylist", 0, -1);
   System.out.println(mylist);
   
   // list 弹出
   String element1 = jedis.lpop("mylist");//c
   System.out.println(element1);
   
   String element2 = jedis.rpop("mylist");//c
   System.out.println(element2);
   
   // list 范围获取
   List<String> mylist2 = jedis.lrange("mylist", 0, -1);
   System.out.println(mylist2);
   
   //3. 关闭连接
   jedis.close();
   ```

   

4. 集合

   ```java
   //1. 获取连接
   Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379端口
   //2. 操作
   // set 存储
   jedis.sadd("myset","java","php","c++");
   
   // set 获取
   Set<String> myset = jedis.smembers("myset");
   System.out.println(myset);
   
   //3. 关闭连接
   jedis.close();
   ```

   

5. 有序集合

   ```java
   //1. 获取连接
   Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379端口
   //2. 操作
   // sortedset 存储
   jedis.zadd("mysortedset",3,"亚瑟");
   jedis.zadd("mysortedset",30,"后裔");
   jedis.zadd("mysortedset",55,"孙悟空");
   
   // sortedset 获取
   Set<String> mysortedset = jedis.zrange("mysortedset", 0, -1);
   System.out.println(mysortedset);
   
   //3. 关闭连接
   jedis.close();
   ```



### jedis连接池：JedisPool

```java
//0.创建一个配置对象
JedisPoolConfig config = new JedisPoolConfig();
config.setMaxTotal(50);
config.setMaxIdle(10);

//1.创建Jedis连接池对象
JedisPool jedisPool = new JedisPool(config,"localhost",6379);
//2.获取连接
Jedis jedis = jedisPool.getResource();
//3. 使用
jedis.set("hehe","heihei");
//4. 关闭 归还到连接池中
jedis.close();
```



### JedisUtils

```java
public class JedisPoolUtils {

    private static JedisPool jedisPool;

    static{
        //读取配置文件
        InputStream is = JedisPoolUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        //创建Properties对象
        Properties pro = new Properties();
        //关联文件
        try {
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取数据，设置到JedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));

        //初始化JedisPool
        jedisPool = new JedisPool(config,pro.getProperty("host"),Integer.parseInt(pro.getProperty("port")));
    }
    /**
     * 获取连接方法
     */
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}
```



## 案例：

1. 需求：
   1. 提供index.html页面，页面中有一个省份 下拉列表
   2. 当 页面加载完成后 发送ajax请求，加载所有省份

2. 注意：

   使用redis缓存一些不经常发生变化的数据。

   * 数据库的数据一旦发生改变，则需要更新缓存。
     * 数据库的表执行增删改的相关操作，需要将redis缓存数据清空，再次存入
     * 在service对应的增删改方法中，将redis数据删除。

```java
```



























