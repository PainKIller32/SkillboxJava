<%@ page import="MyPackage.WorkTime" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="MyPackage.Loader" %>
<%@ page import="MyPackage.TimePeriod" %>
<%@ page import="java.util.TreeSet" %>
<%
    HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();
    StringBuilder table = new StringBuilder();
    StringBuilder tableContent = new StringBuilder();
    TreeSet<String> days = new TreeSet<>();
    Loader loader = new Loader();
    try {
        voteStationWorkTimes = loader.parseFile();
    } catch (Exception e) {
        e.printStackTrace();
    }
    table.append("<tr><td></td>");
    for (Integer voterStation : voteStationWorkTimes.keySet()) {
        tableContent.append("<tr><td>");
        tableContent.append(voterStation);
        tableContent.append("</td>");
        WorkTime workTime = voteStationWorkTimes.get(voterStation);
        for (TimePeriod timePeriod : workTime.getPeriods()) {
            String day = timePeriod.getDay();
            if (!days.contains(day)) {
                days.add(day);
                table.append("<td>");
                table.append(day);
                table.append("</td>");
            }
            tableContent.append("<td>");
            tableContent.append(timePeriod.getTime());
            tableContent.append("</td>");
        }
        tableContent.append("</tr>");
    }
    table.append("</tr>");
    table.append(tableContent);
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>VoteAnalyzer</title>
</head>
<body>
<h1>Voting station work times:</h1>
<table border="2">
    <%= table%>
</table>
</body>
</html>
