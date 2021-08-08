# Git的使用

## git简介

git是最先进的分布式版本控制系统

### Git的安装

1.   通过官网下载安装包，按照步骤安装

2.   安装完成后配置当前机器的用户名和email用于git识别，email可以随便设置，不做校验

     ``` 
     git config --global user.name "username"
     git config --global user.email "email@example.com"
     ```

### 创建版本库

版本库又名仓库，是一个被git管理的文件夹，git能够跟踪仓库下所有文件的修改删除操作

1.   创建一个空目录

     ```
     mkdir learngit
     cd lerngit
     ```

2.   初始化文件夹成仓库

     ```
     git init
     ```

3.   向仓库中添加文件

     ```
     # 1. 将文件移动或复制到learngit文件夹下
     # 2. 执行添加命令，该步骤可以执行多次
     git add readme.txt
     # 3. 向仓库声明提交的内容，该步骤会向之前add的内容一次性提交给仓库
     git commit -m "wrote a readme file"
     ```

## 时光机穿梭

*   当仓库中的文件修改之后，可以使用`git status`查看仓库下所有文件的状态

```
# git status可以查看文件修改的文件是否被添加进仓库，添加进仓库的文件是否被提交
git status
```

*   上面的命令虽然会提示文件被修改，但是无法显示文件被修改的地方，需要使用`git diff`命令。

``` 
git diff <file>
```

### 版本回退

1.   在git中可以使用`git log`命令查看所有的版本提交日志，添加`--pretty=oneline`参数可以查看简化信息

```
# 查看版本日志，其中commit id为版本号
git log
git log --pretty=oneline # 简化查询结果
```

2.   git使用`HEAD`表示当前版本，上一个版本就是`HEAD^`，往前100个版本可以写成`HEAD~100`

```
# 恢复成上一个版本
git reset --hard HEAD^
# 使用commit id定位版本时，版本号没必要写全，git会自动去定位
git reset --heard commit_id
```

3.   假如恢复成之前的版本后想要再换回最新版本，Git提供了一个命令`git reflog`用来记录你的每一次命令，在这个命令中能够找到最新版本的commit id。

```
git reflog
```

### 撤销修改

*   场景1：当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命令`git checkout -- file`。这个命令的意思是，把`file`文件在工作区的修改全部撤销，这里有两种情况：
    1.   一种是`file`自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；
    2.   一种是`file`已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。
*   场景2：当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令`git reset HEAD <file>`，就回到了场景1，第二步按场景1操作。
*   场景3：已经提交了不合适的修改到版本库时，想要撤销本次提交，参考版本回退一节，不过前提是没有推送到远程库。

### 删除文件及误删文件恢复

*   删除仓库下的文件

``` 
# 方法1. 直接在文件管理器中删除
# 方法2. 使用rm命令
rm <file>
```

*   文件删除后，工作区和版本库不一致，使用`git status`命令可以查看哪些文件被删除
*   被删除的文件假如想要永久删除，可以使用`git rm`命令删除版本库中对应的文件再提交。

```
# 1. 删除版本库中的文件
git rm <file>
# 2. 提交修改
git commit -m "remove file"
```

*   从版本库中恢复误删的文件

```
# 从版本库中恢复误删的文件
git checkout -- <file>
```

*   注意：
    1.   从来没有被添加到版本库就被删除的文件，是无法恢复的！
    2.   你只能恢复文件到最新版本，你会丢失**最近一次提交后你修改的内容**。

## 远程仓库

在本地电脑配置github远程仓库

1.   创建SSH Key。

``` 
ssh-keygen -t rsa -C "604995331@qq.com"
```

2.   在C盘/用户/当前用户/.ssh文件夹中，会生成id_rsa和id_rsa.pub这两个文件。
3.   打开github，找到setting——Account settings——SSH and GPG ekys。创建一个新SSH key，将id_rsa.pub中的内容复制并保存。

### 添加远程仓库

先有本地库再有远程库的情况下关联远程库。

1.   在github中创建一个仓库：learning
2.   在本地将这个仓库和github上的仓库创建关联

```
git remote add origin git@github.com:baixz/learning.git
# origin是远程仓库的名字，则是git的默认叫法，也可以修改成别的
```

3.   将本地库的所有内容推送到远程库上

```
git push -u origin master
# 本地库推送到远程库，使用git push命令，实际上是把当前分支master推送到远程
# 由于远程库是空的，我们第一次推送master分支时，加上-u参数，git不但会把本地的分支内容推送到远程新的master分支，还会把本地的master分支和远程的master分支关联起来，以后的推送或者拉取就可以简化命令。
# 再次向远程库推送master分支时，可以使用下面的命令
git push origin master
```

4.   删除远程库

```
# 查看远程库信息
git remote -v
# 根据远程库的名称，删除远程库
git remote rm origin
```

### 从远程库克隆

先创建远程库再将远程库克隆到本地

1.   将已有内容的远程库克隆至本地

```
git clone git@github.com:baixz/learning.git
```

## 分支管理

















