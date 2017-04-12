<%-- 
    Document   : bookList
    Created on : Apr 3, 2017, 10:57:19 AM
    Author     : drew
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Book List</h1>
        <table border="1">
            <tr>
                <td>Book ID</td>
                <td>Title</td>
                <td>Author</td>
            </tr>
            <c:forEach items="${books}" var="b" varStatus = "status">
                <tr>
                    <td>${b.bookId}</td>
                    <td> <c:out value="${b.title}" /></td>
                    <td> <c:out value="${b.authorEntity.authorName}" /></td>
                </tr>
                
            </c:forEach>
            
        </table>
    </body>
</html>
