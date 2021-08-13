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

* find
  * -depth
  * -maxdepth
* xargs

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

    ``` bash
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
    * OnActiveSec：
    * OnBootSec
    * OnStartupSec
    * OnUnitActiveSec
    * OnUnitInactiveSec
    * OnCalendar
    * Unit
    * Persistent







## 后台执行命令



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













