# linux





为何2>&1要写在后面？

```
index.php task testOne >/dev/null 2>&1
```

我们可以理解为，左边是标准输出，好，现在标准输出直接输入到 /dev/null 中，而2>&1是将标准错误重定向到标准输出，所以当程序产生错误的时候，相当于错误流向左边，而左边依旧是输入到/dev/null中。

可以理解为，如果写在中间，那会把隔断标准输出指定输出的文件

你可以用

ls 2>1测试一下，不会报没有2文件的错误，但会输出一个空的文件1；
ls xxx 2>1测试，没有xxx这个文件的错误输出到了1中；
ls xxx 2>&1测试，不会生成1这个文件了，不过错误跑到标准输出了；
ls xxx >out.txt 2>&1, 实际上可换成 ls xxx 1>out.txt 2>&1；重定向符号>默认是1,错误和输出都传到out.txt了。





## linux邮件功能

收件目录

/var/spool/mail

```
# 将邮件发给root自己
/bin/mail -s "testing at job" root < /root/a.sh 

```









## CentOS7.X系统进程管理

* systemctl相关的几个目录
  * /usr/lib/systemd/system/：使用CentOS官方提供的软件安装后，预设的启动脚本设定文件都在这里，这里的资料尽量不要修改，要修改时请到/etc/systemd/system目录下修改
  * /run/systemd/system：系统执行过程中产生的服务脚本，这些脚本有限极高于上一个目录
  * /etc/systemd/system：管理员根据系统的需求建立的脚本，优先级最高
  * /etc/sysconfig/*：几乎所有的服务都会将初始化的一些选项设定写入到这个目录下
  * /var/lib：一些会产生资料的服务都会将他的资料写入到/var/lib目录下
  * /run/：放置了很多服务运行时的临时文件

* 使用systemctl命令管理单一服务

  * 命令格式：systemctl [command] [unit]

  * command 主要有

    * start：立即启动unit
    * stop：立即关闭unit
    * restart：立即重启unit。即先stop再start
    * reload：不关闭unit的情况下，重新载入配置文件，让配置生效
    * enable：设定unit开机自启动
    * disable：设定unit开机不会自启动
    * status：列出unit的当前状态，会显示是否在运行、是否开机自启动等信息
    * is-active：目前有没有运行
    * is-enabled：是否开启自启动
    * mask：注销脚本，使服务永远都无法启动
    * umask：解除注销脚本的操作

  * 示例：

    ```bash
    # 显示防火墙服务的状态
    systemctl status firewalld.service
    # 启动防火墙
    systemctl start firewalld.service
    ```

* 使用systemctl管理所有服务

  * 命令格式：systemctl [command] [--type=TYPE] [--all]

  * command：

    * list-units：列出所有目前启动的服务
    * list-unit-files：根据/usr/lib/systemd/system/内的档案，将所有档案列表说明
    * --type=TYPE：type就是服务的类型，主要有：service、socket、target

  * 示例

    ```bash
    # 列出所有启动的unit
    systemctl list-units
    # 列出的項目中，主要的意義是：
    # UNIT   ：項目的名稱，包括各個 unit 的類別 (看副檔名)
    # LOAD   ：開機時是否會被載入，預設 systemctl 顯示的是有載入的項目而已喔！
    # ACTIVE ：目前的狀態，須與後續的 SUB 搭配！就是我們用 systemctl status 觀察時，active 的項目！
    # DESCRIPTION ：詳細描述囉
    # 另外，systemctl 都不加參數，其實預設就是 list-units 的意思！
    
    # 列出所有已經安裝的 unit 有哪些
    systemctl list-unit-files
    # 列出所有.service服务
    systemctl list-units --type=service --all
    ```

* 将自己的服务添加到systemctl管理下

  1. 创建脚本

     ```bash
     [root@study ~]# vim /backups/backup.sh
     #!/bin/bash
     
     source="/etc /home /root /var/lib /var/spool/{cron,at,mail}"
     target="/backups/backup-system-$(date +%Y-%m-%d).tar.gz"
     [ ! -d /backups ] && mkdir /backups
     tar -zcvf ${target} ${source} &> /backups/backup.log
     
     [root@study ~]# chmod a+x /backups/backup.sh
     [root@study ~]# ll /backups/backup.sh
     -rwxr-xr-x. 1 root root 220 Aug 13 01:57 /backups/backup.sh
     # 記得要有可執行的權限才可以喔！
     ```

  2. 编写.service配置文件

     ```bash
     [root@study ~]# vim /etc/systemd/system/backup.service
     [Unit]
     Description=backup my server
     Requires=atd.service
     
     [Service]
     Type=simple
     ExecStart=/bin/bash -c " echo /backups/backup.sh | at now"
     
     [Install]
     WantedBy=multi-user.target
     # 因為 ExecStart 裡面有用到 at 這個指令，因此， atd.service 就是一定要的服務！
     ```

  3. 重新加载配置文件

     ```bash
     systemctl daemon-reload
     ```

  4. 启动服务

     ```bash
     systemctl start backup.service
     systemctl status backup.service
     ```

* 使用systemd.timer服务实现定期处理

  * 前提：
    1. timer.target一定要启动
    2. 要有sname.service的服务存在（sname是用户自己指定的名称）
    3. 要有sname.timer的时间启动服务存在
    
  * sname.timer配置文件参数说明：
    * OnActiveSec：当timers.target启动多久之后才执行这个unit
    * OnBootSec：当开机完成后多久才执行
    * OnStartupSec：当systemd第一次启动后多久才执行
    * OnUnitActiveSec：当这个timer配置文件所管理的那个unit服务再最后一次启动后，隔多久再执行一次
    * OnUnitInactiveSec：这个timer配置文件所管理的那个unit服务再最后一次停止之后，隔多久再执行一次
    * OnCalendar：使用实际时间（非循环时间）的方式来启动服务
    * Unit：如果你设置的sname.server文件名不一致，则需要在.timer文件中指定
    * Persistent：当使用OnClender的设定时，指定该功能要不要持续进行的意思。通常设定为yes。
    
  * OnCalendar设定使用的时间格式

    ```
    語法：英文周名  YYYY-MM-DD  HH:MM:SS
    範例：Thu       2015-08-13  13:40:00
    
    常用时间间隔单位：
    us 或 usec：微秒 (10-6 秒)
    ms 或 msec：毫秒 (10-3 秒)
    s, sec, second, seconds
    m, min, minute, minutes
    h, hr, hour, hours
    d, day, days
    w, week, weeks
    month, months
    y, year, years
    
    # 通常英文的寫法，小單位寫前面，大單位寫後面～所以先秒、再分、再小時、再天數等～
    隔 3 小時：             3h  或 3hr 或 3hours
    隔 300 分鐘過 10 秒：   10s 300m
    隔 5 天又 100 分鐘：    100m 5day
    
    # 使用英文常用的口語化日期代表
    # 英语口语	 # 實際的時間格式代表
    now			Thu 2015-08-13 13:50:00
    today		Thu 2015-08-13 00:00:00
    tomorrow	Thu 2015-08-14 00:00:00
    hourly		*-*-* *:00:00
    daily		*-*-* 00:00:00
    weekly		Mon *-*-* 00:00:00
    monthly		*-*-01 00:00:00
    +3h10m		Thu 2015-08-13 17:00:00
    2015-08-16	Sun 2015-08-16 00:00:00
    ```

  * 循环时间运作的案例：

    ```bash
    [root@study ~]# vim /etc/systemd/system/backup.timer
    [Unit]
    Description=backup my server timer
    
    [Timer]
    OnBootSec=2hrs
    OnUnitActiveSec=2days
    
    [Install]
    WantedBy=multi-user.target
    # 只要這樣設定就夠了！儲存離開吧！
    
    [root@study ~]# systemctl daemon-reload
    [root@study ~]# systemctl enable backup.timer
    [root@study ~]# systemctl restart backup.timer
    [root@study ~]# systemctl list-unit-files | grep backup
    backup.service          disabled   # 這個不需要啟動！只要 enable backup.timer 即可！
    backup.timer            enabled
    
    [root@study ~]# systemctl show timers.target
    ConditionTimestamp=Thu 2015-08-13 14:31:11 CST      # timer 這個 unit 啟動的時間！
    
    [root@study ~]# systemctl show backup.service
    ExecMainExitTimestamp=Thu 2015-08-13 14:50:19 CST   # backup.service 上次執行的時間
    
    [root@study ~]# systemctl show backup.timer
    NextElapseUSecMonotonic=2d 19min 11.540653s         # 下一次執行距離 timers.target 的時間
    ```

  * 固定时间运作的案例：

    ```bash
    [root@study ~]# vim /etc/systemd/system/backup2.timer
    [Unit]
    Description=backup my server timer2
    
    [Timer]
    OnCalendar=Sun *-*-* 02:00:00
    Persistent=true
    Unit=backup.service
    
    [Install]
    WantedBy=multi-user.target
    
    [root@study ~]# systemctl daemon-reload
    [root@study ~]# systemctl enable backup2.timer
    [root@study ~]# systemctl start backup2.timer
    [root@study ~]# systemctl show backup2.timer
    NextElapseUSecRealtime=45y 7month 1w 6d 10h 30min
    # 與循環時間運作差異比較大的地方，在於這個 OnCalendar 的方法對照的時間並不是 times.target 的啟動時間，而是 Unix 標準時間！ 亦即是 1970-01-01 00:00:00 去比較的！所以下次执行时间会在45年之后。
    ```

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







