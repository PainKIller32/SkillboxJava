<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("windows-1251");
    File downloadFolder = new File("C:/download");
    String[] uploadFiles = downloadFolder.list();
%>
<html>
<head>
    <title>Загрузка файлов</title>
    <script>
        function submitForm(fileName) {
            var form = document.getElementById("download");
            form.fileName.value = fileName;
            form.submit();
        }
    </script>
</head>
<body>
<form method="post" action="uploadServlet" enctype="multipart/form-data">
    <h2>Выберите файл</h2>
    <input type="file" name="file"><br><br>
    <input type="submit" value="Загрузить">
</form>
<form method="post" action="download.jsp" target="_blank" id="download">
    <h3>Загруженные файлы:</h3>
    <input type="hidden" name="fileName">
</form>
<%
    if (uploadFiles != null) {
        for (String fileName : uploadFiles) {
            out.write("<a href=\"#\" onclick=\"submitForm('" + fileName + "')\">" + fileName + "</a><br><br>");
        }
    }
%>
</body>
</html>
