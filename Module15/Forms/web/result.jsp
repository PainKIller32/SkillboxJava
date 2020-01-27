<%@ page import="java.util.Date"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.ParseException"%><%@ page import="java.util.HashMap"%><%@ page import="java.util.Enumeration"%><%@ page import="java.net.URLEncoder"%><%@ page import="java.nio.charset.StandardCharsets"%>
<%@ page contentType="application/json;charset=UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");

    HashMap<String,String> formData = new HashMap<>();
    Enumeration<String> parameterNames = request.getParameterNames();

    while (parameterNames.hasMoreElements()) {
        String parameterName = parameterNames.nextElement();
        formData.put(parameterName, request.getParameter(parameterName));
    }

    for (String value : formData.keySet()) {
        response.addCookie(new Cookie(value, URLEncoder.encode(formData.get(value), StandardCharsets.UTF_8)));
    }
    session.setAttribute("values", formData);

    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    long EIGHTEEN_YEARS = (long) 567648000000.;
    String result = "Одобрено";
    Date date = new Date();

    try {
        date = dateFormat.parse(request.getParameter("date"));
    } catch (ParseException e) {
        e.printStackTrace();
    }

    if (System.currentTimeMillis() - date.getTime() < EIGHTEEN_YEARS){
        result = "Отказано";
    } else if(Integer.parseInt(request.getParameter("salary")) / 2 < (Integer.parseInt(request.getParameter("money"))/Integer.parseInt(request.getParameter("period")))) {
        result = "Отказано";
    }
%>
{
    "Сумма кредита": "<%=formData.get("money")%>",
    "Срок": "<%=formData.get("period")%>",
    "Фамилия": "<%=formData.get("surname")%>",
    "Имя": "<%=formData.get("name")%>",
    "Отчество": "<%=formData.get("patronymic")%>",
    "Дата рождения": "<%=formData.get("date")%>",
    "Контакты": {
        "Телефон": "<%=formData.get("telephone")%>",
        "E-mail": "<%=formData.get("email")%>"
    },
    "Проживает": "<%=formData.get("residentialAddress")%>",
    "Прописка": "<%=formData.get("registrationAddress")%>",
    "Работа": {
        "Компания": "<%=formData.get("work")%>",
        "Должность": "<%=formData.get("vacancy")%>",
        "Зарплата": "<%=formData.get("salary")%>"
    },
    "Решение о выдачи кредита": "<%=result%>"
}