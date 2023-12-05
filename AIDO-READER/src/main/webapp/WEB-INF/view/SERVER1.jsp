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
		function server(no){
			var form = $('#frm');
			var formData = new FormData(form[0]);
			formData.append("serverno",no)
			$.ajax({
				url: "serverno.ai",
				type: "POST",
				data:formData,
				dataType:'json',
				processData:false,
				contentType:false,
				cache:false,
				success: function(data){
					console.log("http://"+data[0].serverip+"/MAIN2.ai");
					//$("#formId").attr("action", "http://"+data[0].serverip+"/MAIN2.ai");
				},
				error : function(e){
					alert("서버오류 : " + e.status);
				}
			})
		}
	</script>
</head>
<body>
<form id="frm" name="frm" action="http://3.36.94.224/MAIN2.ai" method="POST">
	<input type="hidden" id="ID" name="ID" value="${ID}">
	<input type="hidden" id="PWD" name="PWD" value="${PWD}">
</form>

<script type="text/javascript">
	$(document).ready(function(){
		server(1);
		//$("#frm").submit();

	});
</script>
</body>
</html>