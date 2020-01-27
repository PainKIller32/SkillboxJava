<%@ page import="java.util.HashMap" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8");
    HashMap<String, String> values = new HashMap<>();

    if(session.getAttribute("values") != null) {
        values = (HashMap<String, String>) session.getAttribute("values");
    }else {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            values.put(cookie.getName(), URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8));
        }
    }
%>
<%!
    private String setValue(String name) {
        return name != null ? name : "";
    }
%>
<html>
<head>
    <title>Кредит</title>
    <link href="style.css" rel="stylesheet">
</head>
<body>
<h2>Заявка на кредит</h2>
<form method="post" action="result.jsp">
    <label> Интересуемая сумма кредита:
        <input type="number" required="required" class="field" name="money"
               value="<%= setValue(values.get("money"))%>">
    </label><br><br>
    <label>Срок кредита:
        <select name="period">
            <option value="6">6 месяцев</option>
            <option value="12">12 месяцев</option>
            <option value="18">18 месяцев</option>
            <option value="24">24 месяца</option>
        </select>
    </label><br><br>
    <label> Фамилия:
        <input type="text" required="required" class="field" name="surname"
               value="<%= setValue(values.get("surname"))%>">
    </label><br><br>
    <label> Имя:
        <input type="text" required="required" class="field" name="name"
               value="<%= setValue(values.get("name"))%>">
    </label><br><br>
    <label> Отчество:
        <input type="text" required="required" class="field" name="patronymic"
               value="<%= setValue(values.get("patronymic"))%>">
    </label><br><br>
    <label> Дата рождения:
        <input type="date" required="required" class="field" name="date"
               value="<%= setValue(values.get("date"))%>">
    </label><br><br>
    <label> Мобильный телефон:
        <input type="tel" required="required" class="field" name="telephone"
               value="<%= setValue(values.get("telephone"))%>">
    </label><br><br>
    <label> E-mail:
        <input type="email" class="field" name="email"
               value="<%= setValue(values.get("email"))%>">
    </label><br><br>
    <label> Адрес проживания:
        <input type="text" required="required" class="field" name="residentialAddress"
               value="<%= setValue(values.get("residentialAddress"))%>">
    </label><br><br>
    <label> Адрес прописки:
        <input type="text" required="required" class="field" name="registrationAddress"
               value="<%= setValue(values.get("registrationAddress"))%>">
    </label><br><br>
    <label> Место работы:
        <input type="text" required="required" class="field" name="work"
               value="<%= setValue(values.get("work"))%>">
    </label><br><br>
    <label> Среднемесячный доход:
        <input type="number" required="required" class="field" name="salary"
               value="<%= setValue(values.get("salary"))%>">
    </label><br><br>
    <label> Должность:
        <input type="text" required="required" class="field" name="vacancy"
               value="<%= setValue(values.get("vacancy"))%>">
    </label><br><br>
    <input type="checkbox" required="required" class="mark" id="mark" name="consent"><label for="mark">Даю согласие на
    обработку персональных данных</label><br><br>
    <input type="submit" class="button" value="Отправить">
</form>
</body>
</html>