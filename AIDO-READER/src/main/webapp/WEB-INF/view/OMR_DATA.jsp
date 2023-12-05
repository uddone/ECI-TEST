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
	<div class="col-md-12" style="height: 100%; padding-top: 20px;" >
		<div class="iq-card" style="width: 100%; height: 100%;" >
			<div class="iq-card-body" style="width: 100%; height: 100%;" >
				<div class="iq-todo-page no-wrap" style="height: 100%; padding-top: 5%">
					<table id="profile" class="table" style="width: 100%;">
						<tr>
							<th style="background-color: #f0f0f0;">수험번호</th>
							<th style="background-color: #f0f0f0;">성명</th>
							<th style="background-color: #f0f0f0;">성별</th>
							<th style="background-color: #f0f0f0;">생년월일</th>
							<th style="background-color: #f0f0f0;">OMR_KEY</th>
						</tr>
						<tr>
							<td>${omrlist[0].EXMN_NO}</td>
							<td>${omrlist[0].STDN_NM}</td>
							<td>${omrlist[0].SEX}</td>
							<td>${omrlist[0].BTHDAY}</td>
							<td>${omrlist[0].OMR_KEY}</td>
						</tr>
					</table>
					<table class="table">
						<c:forEach begin="1" end="${fn:length(omrlist)/10}" var="i">
							<tr>
							<c:forEach begin="${(i-1)*10+1}" end="${i*10}" var="j">
								<th style="background-color: #f0f0f0;">${j}</th>
							</c:forEach>
							</tr>
							<tr>
							  <c:forEach begin="${(i-1)*10+1}" end="${i*10}" var="j">
								<c:if test="${omrlist[j].ERR_YN == 'Y'}">
									<td contenteditable="true" style="background-color:#ff3c3c;">${omrlist[j].MARK_NO}</td>
								</c:if>
								<c:if test="${omrlist[j].ERR_YN == 'N'}">
									<c:if test="${omrlist[j].CRA_YN == 'N'}">
										<td contenteditable="true" style="color:#FF0000;">${omrlist[j].MARK_NO}</td>
									</c:if>
									<c:if test="${omrlist[j].CRA_YN == 'Y' || omrlist[j].CRA_YN == 'Z'}">
										<td contenteditable="true">${omrlist[j].MARK_NO}</td>
									</c:if>
								</c:if>
								
							  </c:forEach>
							</tr>
						</c:forEach>
						<c:if test="${fn:length(omrlist)/10 gt 4}">
							<tr>
							<c:forEach begin="41" end="45" var="j">
								<th style="background-color: #f0f0f0;">${j}</th>
							</c:forEach>
							</tr>
							<tr>
							<c:forEach begin="41" end="45" var="j">
								<c:if test="${omrlist[j].ERR_YN == 'Y'}">
									<td contenteditable="true" style="background-color:#ff3c3c;">${omrlist[j].MARK_NO}</td>
								</c:if>
								<c:if test="${omrlist[j].ERR_YN == 'N'}">
									<c:if test="${omrlist[j].CRA_YN == 'N'}">
										<td contenteditable="true" style="color:#FF0000;">${omrlist[j].MARK_NO}</td>
									</c:if>
									<c:if test="${omrlist[j].CRA_YN == 'Y' || omrlist[j].CRA_YN == 'Z'}">
										<td contenteditable="true">${omrlist[j].MARK_NO}</td>
									</c:if>
								</c:if>
							</c:forEach>
							 </tr>
							</c:if>
					</table>
					
				</div>
			</div>
		</div>
	</div>
</body>
</html>