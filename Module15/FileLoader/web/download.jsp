<%@ page import="java.io.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8");
    String fileName = request.getParameter("fileName");
    File file = new File("C:/download/" + fileName);
    InputStream is = new FileInputStream(file);
    response.setHeader("Content-Disposition","attachment; filename=" + fileName);
    OutputStream os = response.getOutputStream();
    int i;
    while ((i = is.read()) != -1) {
        os.write(i);
    }
    is.close();
    os.flush();
    os.close();
%>

