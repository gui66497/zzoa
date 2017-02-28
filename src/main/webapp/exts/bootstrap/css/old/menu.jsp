<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>导航条</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <script type="text/javascript" src="js/jquery-1.12.3.min.js" />
  <script src="js/bootstrap/js/bootstrap.min.js"></script>
  <script src="js/bootstrap/js/bootstrap-dropdown.js"></script>
  <link href="js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen" />
</head>
<body>
<div class="navbar navbar-inverse" style="font-size:14px;font-family: Arial, 微软雅黑, 宋体;border:0px solid blue;padding: 0px;">
  <div class="navbar-inner">
    <div class="container" style="height:48px;margin-top:15px;">
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".navbar-responsive-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
      <a class="brand" style="margin-top:-2px;" href="#">网上学习在线考试系统</a>
      <div class="nav-collapse collapse navbar-responsive-collapse">
        <ul class="nav">
          <li><a href="#">首页</a>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">我的考试<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">我的考试</a></li>
              <li><a href="#">我的成绩</a></li>
            </ul>
          </li>
          <li><a href="table.html">题库管理</a></li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">试卷管理<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">试卷管理</a></li>
              <li><a href="#">考试计划</a></li>
              <li><a href="#">我的成绩</a></li>
            </ul>
          </li>
          <li><a href="#">考生管理</a></li>
          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">系统设置<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">基础设置</a></li>
              <li><a href="#">公告管理</a></li>
            </ul>
          </li>

        </ul>
        <ul class="nav" style="margin-right:10px;float:right;">
          <li class="dropdown" style="float:right;">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">admin<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="#">个人中心</a></li>
              <li><a href="#">重置密码</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
</body>
</html>
