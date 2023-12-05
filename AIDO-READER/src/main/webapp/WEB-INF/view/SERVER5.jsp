<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<script src="js/jquery.min.js"></script>
	<script type="text/javascript">
		console.log(${ID})
		console.log(${PWD})
	</script>
</head>
<body>
<form id="frm" name="frm" action="http://3.36.93.173/MAIN2.ai" method="POST">
	<input type="hidden" id="ID" name="ID" value="${ID}">
	<input type="hidden" id="PWD" name="PWD" value="${PWD}">
</form>

<script type="text/javascript">
	$(document).ready(function(){

		$("#frm").submit();

	});
</script>
</body>
</html>