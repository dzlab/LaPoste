<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<jsp:directive.page import="com.google.appengine.api.datastore.Entity" /> 
<jsp:directive.page import="com.google.appengine.api.datastore.DatastoreServiceFactory" />
<jsp:directive.page import="com.google.appengine.api.datastore.DatastoreService" />
<jsp:directive.page import="com.google.appengine.api.datastore.PreparedQuery" />
<jsp:directive.page import="com.google.appengine.api.datastore.Query" />

<%! String LETTER_TABLE = "Letter"; %>
<ol>
<%
DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
for (Entity result : query.asIterable()) {
	out.print("<li>");
	out.print(result.getProperty("identifier"));
	out.print(result.getProperty("nameSender"));
	out.print(result.getProperty("nameReceiver"));
	out.print(result.getProperty("addrReceiver"));
	out.print(result.getProperty("state"));
	out.println("</li>");
}
%>
coucou
</ol>

</body>
</html>