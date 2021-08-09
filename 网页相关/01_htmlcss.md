# html与CSS

## html

### html结构

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>标题</title>
</head>
<body>
</body>
</html>
```

### html常用标签

*   meta：meta标签可提供有关页面的元信息，比如针对搜索引擎和更新频度的描述和关键词。

    1.   设置页面的字符集

         `<meta charset="utf-8">`

    2.   设置网页的描述
         `<meta name="description" content="">`

    3.   设置网页的关键字

         `<meta name="keywords" content="">`

    4.   请求的重定向

         `<meta http-equiv="refresh" content="5;url=地址"/>`

*   head： <head>标签用来表示网页的元数据，head中包含了浏览器和搜索引擎使用的其他不可见信息。

*   title： <title>标签表示网页的标题，一般会在网页的标题栏上显示。

*   body： <body>标签用来设置网页的主体，所有在页面中能看到的内容都应该编写到body标签中。

*   h1-h6：h1-h6都是网页中的标题标签，用来表示网页中的一个标题，不同的是，从h1~h6重要性越来越低。

*   p：<p>标签表示网页中的一个段落。是块元素

*   br：br标签表示一个换行标签，使用br标签可以使br标签后的内容另起一行。

*   hr：hr标签是水平线标签，使用hr标签可以在页面中打印一条水平线，水平线可以将页面分成上下两个部分。

*   img：img标签是图片标签，可以用来向页面中引入一张外部的图片。

    *   src：指向一个外部的图片的路径。
    *   alt：图片的描述

*   a：a标签是超链接标签，通过a标签，可以快速跳转到其他页面。

    *   href：指向一个链接地址
    *   target：设置打开目标页面的位置，可选值：_blank新窗口、 _self当前窗口。

## CSS

### 基本语法

* CSS的写法

  ```
  选择器{样式名:样式值;样式名:样式值;}
  ```

* 内部样式表

  ```css
  <style>
  	p{color:red; font-size: 30px;}
  </style>
  ```

* 外部样式表

  ```css
  <link rel="stylesheet" type="text/css" href="style.css">
  ```

### 选择器

* 元素选择器
  * 标签名{}
* 类选择器
  * .className{}
* id选择器
  * #idName{}
* 复合选择器
  * 选择器1选择器2{}
* 群组选择器
  * 选择器1,选择器2{}
* 通用选择器
  * *{}
* 后代元素选择器
  * 祖先元素 后代元素{}
* 伪类选择器
  * :link 正常的超链接
  * :visited 访问过的链接(只能定义字体颜色)
  * :hover 鼠标滑过的链接
  * :active 点击的链接
  * :focus 获取焦点（文本输入框等）
  * :before 在指定元素前
  * :after 在指定元素后
  * ::selection 选中的元素
  * :first-letter 首字母
  * :first-line 首行
  * :not(选择器){} 否定伪类，将括号中的选择器从当前选择中排除
* 属性选择器
  * [属性名] 选取含有指定属性的元素
  * [属性名="属性值"] 选取含有指定属性值的元素
  * [属性名~="属性值"]
  * [属性名|="属性值"]
  * [属性名^="属性值"] 选取属性值以指定内容开头的元素
  * [属性名$="属性值"] 选取属性值以指定内容结尾的元素
  * [属性名*="属性值"] 选取属性值以包含指定内容的元素
* 子元素选择器
  * 父元素 > 子元素{}
  * :first-child 第一个子元素
  * :last-child 最后一个子元素
  * :nth-child() 选择指定位置的子元素
    * 数字：指定位置
    * even：表示偶数位置的子元素
    * odd：表示奇数位置的子元素
  * :first-of-type 选择指定类型的子元素
  * :last-of-type
  * :nth-of-type
    * 数字：指定位置
    * even：表示偶数位置的子元素
    * odd：表示奇数位置的子元素
* 兄弟元素选择器
  * 兄弟元素+兄弟元素{}
  * 兄弟元素~兄弟元素{}

### 继承

像儿子可以继承父亲的遗产一样，在CSS中，祖先元素上的样式，也会被他的后代元素所继承。

利用继承，可以将一些基本的样式设置给祖先元素，这样所有的后代元素将会自动继承这些样式。

但是并不是所有的样式都会被子元素所继承，比如：背景相关的样式都不会被继承 边框相关的样式 定位相关的

### 选择器的权重

* 不同的选择器有不同的权重值：
  * 内联样式：权重为1000
  * id选择器：权重为100
  * 类、属性、伪类选择器：权重为10
  * 元素选择器：权重为1
  * 通配符：权重为0
  * 继承的样式：没有权重
* 当选择器中包含多种选择器时，需要将多种选择器的优先级相加然后在比较。但是注意，选择器优先级计算不会超过他的最大的数量级，如果选择器的优先级一样，则使用靠后的样式。
* 群组选择器的优先级是单独计算
* 可以在样式的最后，添加一个!important，则此时该样式将会获得一个最高的优先级，将会优先于所有的样式显示甚至超过内联样式，但是在开发中尽量避免使用!important

## 文本样式

* font-size 文字大小
* font-family 文字字体，可以同时指定多个字体
* font-style 斜体
  * italic 斜体
  * normal 非斜体，默认值
* font-weight 粗体
  * bold 粗体
  * normal 非粗体，默认值
* font-variant 小型大写字体
  * small-caps 设置小型大写字体，不设置则默认正常显示
* font 字体属性的简写
  * font:加粗 斜体 小型大写 大小/行高 字体
  * 加粗、斜体、小型大写的顺序无所谓，也可以不写，但是字体大小和字体必须写在最后面。
* line-height 行高，行高越大行间距越大，文字默认显示在行高的中间。
* text-transform 将所有字母全都变成大写或小写
  * uppercase 全都变成大写字母
  * lowercase 全都变成小写字母
  * capitalize 首字母大写
  * none 默认值，正常显示
* text-decoration 给文本添加修饰
  * underline
  * overline
  * line-through
  * none
* letter-spacing 设置字符之间的间距
* word-spacing 设置单词之间的间距
* text-align 设置文本的对齐方式
  * left 左对齐
  * right 右对齐
  * justify 两边对齐
  * center 居中对齐
* text-indent 设置首行缩进，需要指定一个长度，并且只对第一行生效。

## 盒模型

### 基本盒模型





### 浮动





### 定位









## 背景

* background-color 设置背景颜色
  * 设置了背景颜色后，整个元素的可见区域都会使用该颜色
  * 如果不设置背景颜色，元素默认背景颜色为透明，会显示父元素的背景颜色
* backgound-image 为元素指定背景图片
  * 需要指定url地址为参数，url地址指向一个外部图片的路径
* background-repeat 控制背景图片的重复方式
  * repeat 默认值，图片会左右上下平铺
  * no-repeat 只显示图片一次，不会平铺
  * repeat-x 只沿着X轴水平平铺图片
  * repeat-y 只沿着y轴平铺图片
* background-position 控制图片在元素中的位置，通过下面三种方式设置图片在水平和垂直方向的起点：
  * 关键字：top right bottom left center
  * 百分比
  * 数值
* background-attachment 设置背景图片是否随页面滚动
  * scroll 随页面滚动
  * fixed 不随页面滚动
* background 背景图片的简写属性，通过这个属性可以一次性设置多个样式，而且样式的顺序没有要求。

## 表格





## 表单

