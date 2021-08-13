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





### nohup











## 文件名置换



## shell输入与输出



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

















