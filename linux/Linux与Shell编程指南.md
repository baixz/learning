# linux与shell编程指南

## 文件安全与权限

* 文件和目录的权限
  * 每个文件或目录都有三组权限，控制文件的读写和执行。
  * 目录一定要有执行权限。否则无法cd到这个目录下
* suid和sgid
* chown和chgrp
  * 修改文件或目录的用户和用户组
* umask
  * 查看或设置文件或目录的默认权限，很少使用
* 链接
  * 不带参数表示创建硬链接，带参数 -s 会创建软链接

## 使用find和xargs

### find



### xargs





## 后台执行命令

### at

* at是可以处理仅执行一次就结束的指令

* 启动atd服务

  ```bash
  [root@study ~]# systemctl restart atd  # 重新啟動 atd 這個服務
  [root@study ~]# systemctl enable atd   # 讓這個服務開機就自動啟動
  [root@study ~]# systemctl status atd   # 查閱一下 atd 目前的狀態
  ```

* at命令的执行权限

  * /etc/at.{allow,deny}文件中配置使用at命令的权限，在文件中配置的用户，就可以拥有/禁止使用at命令
  * 假如这两个文件都不存在，则只有root用户可以使用at命令

* at的使用：

  * 基本语法：

    ```
    [root@study ~]# at [-mldv] TIME
    [root@study ~]# at -c 工作號碼
    選項與參數：
    -m  ：當 at 的工作完成後，即使沒有輸出訊息，亦以 email 通知使用者該工作已完成。
    -l  ：at -l 相當於 atq，列出目前系統上面的所有該使用者的 at 排程；
    -d  ：at -d 相當於 atrm ，可以取消一個在 at 排程中的工作；
    -v  ：可以使用較明顯的時間格式列出 at 排程中的工作列表；
    -c  ：可以列出後面接的該項工作的實際指令內容。
    
    TIME：時間格式，這裡可以定義出『什麼時候要進行 at 這項工作』的時間，格式有：
      HH:MM				ex> 04:00
    	在今日的 HH:MM 時刻進行，若該時刻已超過，則明天的 HH:MM 進行此工作。
      HH:MM YYYY-MM-DD		ex> 04:00 2015-07-30
    	強制規定在某年某月的某一天的特殊時刻進行該工作！
      HH:MM[am|pm] [Month] [Date]	ex> 04pm July 30
    	也是一樣，強制在某年某月某日的某時刻進行！
      HH:MM[am|pm] + number [minutes|hours|days|weeks]
    	ex> now + 5 minutes	ex> 04pm + 3 days
    	就是說，在某個時間點『再加幾個時間後』才進行。
    ```

  * 示例：

    ```
    範例一：再過五分鐘後，將 /root/.bashrc 寄給 root 自己
    [root@study ~]# at now + 5 minutes  <==記得單位要加 s 喔！
    at> /bin/mail -s "testing at job" root < /root/.bashrc
    at> <EOT>   <==這裡輸入 [ctrl] + d 就會出現 <EOF> 的字樣！代表結束！
    job 2 at Thu Jul 30 19:35:00 2015
    # 上面這行資訊在說明，第 2 個 at 工作將在 2015/07/30 的 19:35 進行！
    # 而執行 at 會進入所謂的 at shell 環境，讓你下達多重指令等待運作！
    
    範例二：將上述的第 2 項工作內容列出來查閱
    [root@study ~]# at -c 2
    
    範例三：由於機房預計於 2015/08/05 停電，我想要在 2015/08/04 23:00 關機？
    [root@study ~]# at 23:00 2015-08-04
    at> /bin/sync
    at> /bin/sync
    at> /sbin/shutdown -h now
    at> <EOT>
    job 3 at Tue Aug  4 23:00:00 2015
    # 您瞧瞧！ at 還可以在一個工作內輸入多個指令呢！不錯吧！
    
    范例4：执行shell文件
    [root@baixz ~]# cat a.sh 
    #!/bin/bash
    /bin/mail -s "testing at job" root < /root/.bashrc
    [root@baixz ~]# at now + 5 minutes -f /root/a.sh 
    job 4 at Fri Aug 13 14:26:00 2021
    ```

  * 补充说明：

    1. 使用at执行命令时，最好使用绝对路径，否则可能会有问题
    2. 使用at命令执行时，标准输出和标准错误输出都会被传送到执行者的mailbox中，不会显示在控制台。比如使用at命令执行 `echo "hello"`的话，不会有信息显示。

* at工作管理

  ```
  [root@study ~]# atq
  [root@study ~]# atrm (jobnumber)
  
  範例一：查詢目前主機上面有多少的 at 工作排程？
  [root@study ~]# atq
  3       Tue Aug  4 23:00:00 2015 a root
  # 上面說的是：『在 2015/08/04 的 23:00 有一項工作，該項工作指令下達者為root』而且，該項工作的工作號碼 (jobnumber) 為 3 號喔！
  
  範例二：將上述的第 3 個工作移除！
  [root@study ~]# atrm 3
  [root@study ~]# atq
  # 沒有任何資訊，表示該工作被移除了！
  ```

### crontab

* crontab可以将所设定的工作循环执行

* crontab使用

  * /etc/cron.allow：不在该文件中的账号不允许使用crontab，该文件优先级高于cron.deny文件。
  * /etc/cron.deny：在该文件中的账号不允许使用crontab
  * 默认情况下，没有设置cron.allow和deny文件，所有用户都有权限使用crontab。
  * 當使用者使用 crontab 這個指令來建立工作排程之後，該項工作就會被紀錄到 /var/spool/cron/ 裡面去了，而且是以帳號來作為判別的。
  * 不要使用 vi 直接編輯該檔案， 因為可能由於輸入語法錯誤，會導致無法執行 cron。
  * cron 執行的每一項工作都會被紀錄到 /var/log/cron 這個日志文件里。

* 语法和示例

  ```
  [root@study ~]# crontab [-u username] [-l|-e|-r]
  選項與參數：
  -u  ：只有 root 才能進行這個任務，亦即幫其他使用者建立/移除 crontab 工作排程；
  -e  ：編輯 crontab 的工作內容
  -l  ：查閱 crontab 的工作內容
  -r  ：移除所有的 crontab 的工作內容，若僅要移除一項，請用 -e 去編輯。
  
  範例一：用 dmtsai 的身份在每天的 12:00 發信給自己
  [dmtsai@study ~]$ crontab -e
  # 此時會進入 vi 的編輯畫面讓您編輯工作！注意到，每項工作都是一行。
  0   12  *  *  * mail -s "at 12:00" dmtsai < /home/dmtsai/.bashrc
  #分 時 日 月 週 |<==============指令串========================>|
  
  # 时间格式说明：
  分钟	   小时	  日期	月份	   周
  0-59	0-23	1-31	1-12	0-7（0和7都表示周日）
  # 特殊时间符号
  *	代表任意时刻。日期显示为星号表示每隔一天，小时为星号表示每隔一个小时
  ,	代表分隔时段。比如每天的3点和6点执行命令：0 3,6 * * * command
  -	代表一段范围内。比如每天8点到12点的20分执行命令：20 8-12 * * * command
  /n	n表示数字，表示每隔n单位的时间间隔。比如每隔五分钟一次：*/5 * * * * command
  
  [dmtsai@study ~]$ crontab -l
  0 12 * * * mail -s "at 12:00" dmtsai < /home/dmtsai/.bashrc
  
  # 注意，若僅想要移除一項工作而已的話，必須要用 crontab -e 去編輯～
  # 如果想要全部的工作都移除，才使用 crontab -r 喔！
  [dmtsai@study ~]$ crontab -r
  [dmtsai@study ~]$ crontab -l
  no crontab for dmtsai
  ```

### &

* 使用&命令可以把命令放到后台执行

* 格式：

  ```
  命令 &
  ```

### nohup

* nohup命令可以在用户退出账户之后继续运行相应的进程。

* 使用nohup命令提交点单个命令

  ```
  nohup command > myout.file 2>&1
  ```

* 使用nohup命令处理多个命令

  ```
  # 1.编写shell文件
  # 2.使用nohup命令执行shell文件
  nohup ./nohuptest.sh > notest.out 2>&1 &
  ```

## 文件名置换

### 使用*

* 使用星号*可以匹配文件名中的任意字符串。

  ```bash
  $ ls app*	# 查询以app开头的字符串
  $ ls *.doc	# 查询.doc结尾的文件
  $ ls cl*.sed # 查询以cl开头，以.sed结尾的文件
  ```

### 使用？

* 使用问号可以匹配文件名中的任意单个字符。

  ```bash
  $ ls ??R*	# 查询以任意两个字符开头第三个字符是R，后面接任意字符的文件
  $ ls conf??.log	# 查询以conf开头，中间是任意两个字符，最后以.log结尾的文件
  $ ls f??*s # 查询以f开头，中间是任意两个字符，后面跟任意字符串，并以s结尾的文件
  ```

### 使用[...]和[!...]

* 使用[...]可以用来匹配方括号中的任意一个字符。可以使用横杠-来链接两个字母或数字表示一个范围。

  ```bash
  $ ls [io]*	# 查询以i或o开头的文件名
  $ ls log.[0-9]*	# 查询所有以log.开头，后面跟随一个数字，然后可以是任何字符的文件名
  $ ls LSP??[!0-9]* # 查询以LSP开头、中间可以是任意两个字符，后面跟随一个非数字字符、然后是任意字符串的文件名。
  ```

## shell输入与输出

### echo

* 语法格式

  * echo [选项] [文本]

* 选项

  * -n：不要自动换行
  * -E：不解析转义字符（默认参数）
  * -e：若字符串中出现一下字符，需要进行特别处理，不能将它当做一般文字输出
    * \c：最后不加换行符
    * \f：换行但是光标依旧停留在原来的位置
    * \t：插入tab
    * \n：换行且光标移动至行首
    * \nnn：插入nnn（八进制）所代表的的ASCII字符。

* 使用范例

  ```bash
  $ echo hello world!		# 打印字符串
  $ echo 'hello world'	# 打印字符串
  $ echo "hello world\!"	# 感叹号需要转义
  $ echo -e 'hello\tworld'	# \t需要做特殊处理
  $ echo 'hello world' >> hello.txt	# 将内容写入文件
  $ echo -n "oldboy";echo "oldboy"	# 第一个输出不换行
  $ echo $PATH	# 打印变量
  ```

### read

* 语法格式

  * read varible1 varible2 ...

* 使用范例：

  ```bash
  $ read name		# 如果只指定一个变量，则会将所有输入赋值给该变量知道遇到文件结束符或回车
  baixz 123
  $ echo ${name}
  baixz 123
  $ read name1 name2	# 如果指定两个变量，但是输入的字符串数量大于2个，从第二个开始的字符串都会赋值给第二个变量。
  baixz 123 456 789
  $ echo ${name2}
  123 456 789
  [baixz@baixz ~]$ cat var_test.sh # 读取三个字符串
  #!/bin/sh
  # var_test
  echo -e "first name: \c"
  read firstname
  echo -e "middle name: \c"
  read middle
  echo -e "lastname: \c"
  read surname
  echo -e ${firstname}" "${middle}" "${surname}
  ```

### cat

* 语法格式

  * cat [选项] [文件]

* 选项说明：

  * -n：从1开始对所有输出内容按行编号
  * -b：和-n选项功能类似，但是会忽略显示空白行行号
  * -s：当遇到有连续两行以上的空白行时，就替换为一行空白行
  * -A：等价于-vET三个选项之和
  * -e：等价于-vE
  * -E：在每一行的行尾显示$符号
  * -t：与-vT等价
  * -T：将Tab字符显示为^I
  * -v：除了LFD和TAB之外，使用^和M-引用

* 使用范例：

  ```bash
  # 创建新文件
  $ cat > test.txt <<EOF
  > 这是第一行内容
  > 
  > 空了一行
  > 
  > 
  > 又空了两行
  > EOF
  $ cat test1.txt test2.txt	# 一次显示多个文件
  $ cat test1.txt test2.txt > testnew.txt	# 将前两个文件的内容写入到新文件里
  $ cat -E test.txt	# 显示每一行结尾的结束符
  ```
  

### 管道 |

* 通过管道可以把一个命令的输出传递给另一个命令作为输入。

* 语法：

  * 命令1 | 命令2

* 示例：

  ```bash
  $ ls | grep test.txt	# 在ls的结果中查找test.txt
  $ who | awk '{print $1"\t"$2}'
  ```

### tee

* tee命令可以把输出的一个副本输送到标准输出，另一个副本拷贝到响应的文件中。

* 语法：

  * tee [-a] files	# -a表示追加到文件末尾

* 示例：

  ```bash
  $ who | tee who.out	# who命令将结果输入到屏幕上，同时使用tee命令将输出保存到who.out文件中。
  ```

### 标准输入、输出和错误

* 标准输入：标准输入是文件描述符0。它是命令的输入，缺省为键盘，也可以是文件或其他命令的输出。
* 标准输出：标准输出是文件描述符1。它是命令的输出，缺省是屏幕，也可以是文件。
* 标准错误：标准错误是文件描述符2。它是命令错误的输出，缺省是屏幕，也可以是文件。

### 文件重定向

* 常用文件重定向命令

| 命令                            | 说明 |
| :------------------------------ | ---- |
| command > filename              |      |
| command >> filename             |      |
| command 1 > filename            |      |
| command > filename 2>&1         |      |
| command 2 > filename            |      |
| command 2 >> filename           |      |
| command >> filename 2>&1        |      |
| command < filename1 > filename2 |      |
| command < filename              |      |
| command << delimiter            |      |
| command <                       |      |
|                                 |      |



## 命令执行顺序





## 正则表达式



## grep



## sed



## awk



## 合并与分割



## tr的用法



## 登录环境





## 环境和shell变量







## 引号





## shell脚本介绍

















