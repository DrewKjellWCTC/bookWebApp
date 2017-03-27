<%-- 
    Document   : authorList
    Created on : Feb 8, 2017, 10:59:34 AM
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
        <title>Author List</title>
    </head>
    <body>
        <h1>Administration - Author List</h1>
        <ul>
            <li><a href="index.html">Menu</a></li>
        </ul>
        
        <table border="1">
            <tr>
                <td>Author ID</td>
                <td>Author Name</td>
                <td>Date Added</td>
            </tr>
            <c:forEach items="${authors}" var="a" varStatus = "status">
                <tr>
                    <td>${a.authorId}</td>
                    <td> <c:out value="${a.authorName}" /></td>
                    <td><fmt:formatDate pattern="M/d/yyyy" value="${a.dateAdded}" /></td>
                </tr>
                
            </c:forEach>
            
        </table>
        <form id="ctrlForm" name="ctrlForm" action="AuthorController?action=form">
            <input type="submit" value="Add" name="add">
            <input type="submit" value="Edit" name="edit">
            <input type="submit" value="Delete" name="delete">
        </form>
    </body>
</html>
