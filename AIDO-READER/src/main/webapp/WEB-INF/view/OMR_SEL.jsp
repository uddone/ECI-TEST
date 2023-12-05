<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
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
       <!-- Full calendar -->
      <link href='fullcalendar/core/main.css' rel='stylesheet' />
      <link href='fullcalendar/daygrid/main.css' rel='stylesheet' />
      <link href='fullcalendar/timegrid/main.css' rel='stylesheet' />
      <link href='fullcalendar/list/main.css' rel='stylesheet' />

      <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<meta charset="UTF-8">
<style type="text/css">
	.select {
      padding:3px;
      background-color: #f9fbe7;
   }
</style>
<script type="text/javascript">
var omrcd = 0;
var examcd = 0;
var lsncd = 0;
var omrnm = "";

	function sel(omr_cd,exam_cd,lsn_cd,omr_nm){
		$(".sub").each(function() {
      		$(this).removeClass("select");
   		})
   		
   		$("#" + omr_cd).addClass("select");
		omrcd = omr_cd;
		examcd = exam_cd;
		lsncd = lsn_cd;
		omrnm = omr_nm;
		
	}
	function kind(){
		var id = "a"+examcd+lsncd
		console.log(omrnm)
		$("#"+id,opener.document).val(omrnm);
		self.close();
	}
	function clo(){
		self.close();
	}
</script>
<title>Insert title here</title>
</head>
<body>
  <div class="col-md-12" style="height: 100%; margin-top: 5%;"  >
            <div class="iq-card" style="width: 100%; height: 100%;" >
                  <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                        	<form class="position-relative" style="height: 35%;">
                              <div class="form-group mb-0">
                                 <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                 <a class="search-link" href="#"><i class="ri-search-line"></i></a>
                              </div>
                              <br>
                           </form>
                              <div class="listbox " style="width: 95%; height: 45%;">
                                    <div class="namelist no-wrap" style="width: 95%; height: 100%;">
                                          <c:forEach var="mmst" items="${mlist}">
                                          	<li onclick="sel('${mmst.OMR_MST_CD}','${examcd}','${lsncd}','${mmst.OMR_NM}')" class="sub" id="${mmst.OMR_MST_CD}">${mmst.OMR_NM}</li>
                                          </c:forEach>
                                    </div>      
                              </div>
                              <br>
                              <div style="height: 20%;" align="center">
                                    <button class="btn btn-primary" style="width: 30%" onclick="kind()">선택</button>
                                    <button class="btn btn-primary" style="width: 30%; margin-left: 2% " onclick="clo()">닫기</button> 
                              </div>
                        </div>
                  </div>
            </div>
      </div>
      <!-- Optional JavaScript -->
      <!-- jQuery first, then Popper.js, then Bootstrap JS -->
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
      <!-- am core JavaScript -->
      <script src="js/core.js"></script>
      <!-- am charts JavaScript -->
      <script src="js/charts.js"></script>
      <!-- am animated JavaScript -->
      <script src="js/animated.js"></script>
      <!-- am kelly JavaScript -->
      <script src="js/kelly.js"></script>
      <!-- Flatpicker Js -->
      <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
      <!-- Chart Custom JavaScript -->
      <script src="js/chart-custom.js"></script>
      <!-- Custom JavaScript -->
      <script src="js/custom.js"></script>
</body>
</html>