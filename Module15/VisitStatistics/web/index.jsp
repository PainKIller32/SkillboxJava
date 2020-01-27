<%@ page import="Package.DBHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    DBHandler count = new DBHandler();
    String userAgent = request.getHeader("User-Agent").replaceAll("\\(.+?\\)", "");
    String[] browsers = userAgent.split(" ");
    String browser = browsers[browsers.length - 1].replaceAll("[^a-zA-Z]", "");
    switch (browser) {
        case "OPR":
            count.countVisits("Opera");
            break;
        case "Chrome":
            count.countVisits("Chrome");
            break;
        case "Firefox":
            count.countVisits("Mozilla Firefox");
            break;
        case "MSIE":
            count.countVisits("InternetExplorer");
            break;
        default:
            count.countVisits("Unknown");
    }
%>
<html>
<head>
    <title>Статистика</title>
    <link href="style.css" rel="stylesheet">
</head>
<body>
<h2>Статистика посещения по браузерам</h2>
<table border="2">
    <tr>
        <th>Браузер</th>
        <th>Количество посещений</th>
    </tr>
    <%=count.getBrowserStatistics()%>
</table>
</body>
</html>
