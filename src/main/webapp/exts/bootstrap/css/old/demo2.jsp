<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<html>
<head>
  <title>demo</title>
  <style type="text/css">
    html {
      height:100%;
      margin:0;
    }
    body {
      height:100%;
      margin:0;
    }
    #mainDiv {
      width:100%;
      height:100%;
      border:0px solid blue;
    }

    #headDiv {
      width:100%;
      height:70px;
      border:0px solid red;
      float:left;
      position: relative;
    }
    #contentDiv {
      width:100%;
      height:70%;
      border:0px solid green;
      margin-top:10px;
      float:left;
      position: relative;
    }
  </style>

</head>
<body>
<div id="mainDiv">
  <div id="headDiv">
    <jsp:include page="menu.jsp" />
  </div>
  <div id="contentDiv">

  </div>
</div>

</body>
<script type="text/javascript" src="js/jquery-1.12.3.min.js" />
</html>