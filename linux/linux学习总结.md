# linux



## 邮件的使用

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









