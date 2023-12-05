<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전송 데이터 확인</title>
<link rel="shortcut icon" href="images/favicon.ico" />
      <!-- Bootstrap CSS -->
      <link rel="stylesheet" href="css/bootstrap.min.css">
      <!-- Typography CSS -->
      <link rel="stylesheet" href="css/typography.css">
      <!-- Style CSS -->
      <link rel="stylesheet" href="css/style.css">
      <!-- Responsive CSS -->
      <link rel="stylesheet" href="css/responsive.css">
       <!-- Full calendar -->
      <link href='fullcalendar/core/main.css' rel='stylesheet' />
      <link href='fullcalendar/daygrid/main.css' rel='stylesheet' />
      <link href='fullcalendar/timegrid/main.css' rel='stylesheet' />
      <link href='fullcalendar/list/main.css' rel='stylesheet' />


      <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
       
      <style type="text/css">
      	
      </style>
      <script src="js/jquery.min.js"></script>
</head>
<body>
	<div class="col-md-12" style="height: 380px; padding-top: 20px" >
		<div class="iq-card" style="width: 100%; height: 100%;" >
			<div class="iq-card-body" style="width: 100%; height: 100%;" >
				<div class="iq-todo-page" style="height: 70%; padding-top: 5%">
					<h5>${DATA}</h5>
				</div>
			</div>
		</div>
	</div>
</body>
</html>