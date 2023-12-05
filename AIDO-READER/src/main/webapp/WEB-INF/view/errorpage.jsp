<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<!-- Required meta tags -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>Vito - Responsive Bootstrap 4 Admin Dashboard Template</title>
	<!-- Favicon -->
	<link rel="shortcut icon" href="images/favicon.ico" />
	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<!-- Typography CSS -->
	<link rel="stylesheet" href="css/typography.css">
	<!-- Style CSS -->
	<link rel="stylesheet" href="css/style.css">
	<!-- Responsive CSS -->
	<link rel="stylesheet" href="css/responsive.css">
</head>
<body>
<div id="content-page" class="content-page">
	<div class="container-fluid">
		<div class="row row-eq-height">
			<div class="col-md-12" style="width: 50%;">
				<div>
					<div class="iq-card iq-border-radius-20" style="width: 50%;" >
						<div class="iq-card" >
							<div class="iq-card-body" >
								<div class="iq-todo-page">
									<table class="table">
										<tr>
											<c:if test="${requestScope['javax.servlet.error.status_code'] == 400}">
												<td><h3>잘못 된 요청입니다.</h3></td>
											</c:if>

											<c:if test="${requestScope['javax.servlet.error.status_code'] == 404}">
												<td><h3>요청하신 페이지를 찾을 수 없습니다. </h3> </td>
											</c:if>

											<c:if test="${requestScope['javax.servlet.error.status_code'] == 405}">
												<td><h3>요청된 메소드가 허용되지 않습니다.</h3></td>
											</c:if>

											<c:if test="${requestScope['javax.servlet.error.status_code'] == 500}">
												<td><h3>서버에 오류가 발생하여 요청을 수행할 수 없습니다.</h3></td>
											</c:if>

											<c:if test="${requestScope['javax.servlet.error.status_code'] == 503}">
												<td><h3>서비스를 사용할 수 없습니다.</h3></td>
											</c:if>
										</tr>
										<tr>
											<td>
												<button type="button" class="btn btn-primary float-right" onclick="javascript:location.href='BSTOR_RC_REG_SG.ai'">HOME</button>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="js/jquery.min.js"></script>
<script src="js/popper.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<!-- Appear JavaScript -->
<script src="js/jquery.appear.js"></script>
<!-- Countdown JavaScript -->
<script src="js/countdown.min.js"></script>
<!-- Counterup JavaScript -->
<script src="js/waypoints.min.js"></script>
<script src="js/jquery.counterup.min.js"></script>
<!-- Wow JavaScript -->
<script src="js/wow.min.js"></script>
<!-- Apexcharts JavaScript -->
<script src="js/apexcharts.js"></script>
<!-- Slick JavaScript -->
<script src="js/slick.min.js"></script>
<!-- Select2 JavaScript -->
<script src="js/select2.min.js"></script>
<!-- Owl Carousel JavaScript -->
<script src="js/owl.carousel.min.js"></script>
<!-- Magnific Popup JavaScript -->
<script src="js/jquery.magnific-popup.min.js"></script>
<!-- Smooth Scrollbar JavaScript -->
<script src="js/smooth-scrollbar.js"></script>
<!-- lottie JavaScript -->
<script src="js/lottie.js"></script>
<!-- Chart Custom JavaScript -->
<script src="js/chart-custom.js"></script>
<!-- Custom JavaScript -->
<script src="js/custom.js"></script>

</body>
</html>