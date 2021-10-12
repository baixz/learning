# Thymeleaf

## 学习过程中使用到的内容

### 在页面中引入thymeleaf

```html
<!DOCTYPE html>
<!-- 通过添加标签名的方式引入thymeleaf -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">
	<head>
    	<meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
    </body>
</html>
```

### 超链接

```html
<!--通过th:href可以使配置的超链接地址被服务器端解析成：上下文路径/target-->
<a th:href="@{/target}">访问目标页面target.html</a>
```

### 获取request域对象数据

```html
<!--th:test，将文本信息显示在p标签中-->
<!--${}中的变量值是request域对象-->
<p th:text="${testRequestScope}"></p>
<p th:text="${session.testSScope}"></p>
```



















































































