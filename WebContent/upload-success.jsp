<%@ page import="java.io.*"%>
<%
	boolean is_file_uploaded = Boolean.valueOf(request.getAttribute("is_file_uploaded").toString());
   String uploadedFileName = request.getAttribute("file_name").toString();
%><Br>

            
<%
if(!is_file_uploaded){
	%>
		<h1 style="color:red"> > File is not uploaded.</h1>
	<%
}else{
	%>
		<h1>File :: <span style="color:green"> <%=uploadedFileName%> </span> has been uploaded successfully !! </h1>
	<%
}
%>