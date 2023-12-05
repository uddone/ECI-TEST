<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>좌표등록</title>
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

<script> 
           function win_open(page) {
               var op ="width=1100, height=850, left=500, top=200";
               open(page+".ai","",op);
            }
            function sv(){
                  opener.location.href='OMR_REG3.ai'
            }
      </script>
</head>
<body>
      <div class="row">
            <div style="width: 50%"> 
                  <div class="col-md-12" style="height: 380px; padding-top: 20px" >
                        <div class="iq-card" style="width: 100%; height: 100%;" >
                              <div class="iq-card-body" style="width: 100%; height: 100%;" >
                                    <div class="iq-todo-page" style="height: 70%; padding-top: 5%">
                                          <form name="f">
                                    <table width="100%">
                                          <tr><th style="width: 50px">이미지</th>
                                          	<td width="120px">
                                          		<input type="text" style="width: 120px">
                                          	</td> <th style="width: 50px">답안</th>
            <td width="120px"><input type="text" onclick="win_open('CRD_REG')" name="CRD" placeholder="(125,125),(250,250)" style="width: 120px"></td>
      </tr>
      
      </table>
      
</form>
</div>
      <div style="padding-top: 5%; height: 25%;" align="center" >
            <button class="btn btn-primary" style="width: 30%">추가</button>
            <button class="btn btn-primary" style="width: 30%; margin-left: 2% ">제거</button> 
            <button class="btn btn-primary" style="width: 30%; margin-left: 2% " onclick="sv()">저장</button>
      </div>

</div>
</div>
</div>
      </div>
      <div style="width: 50%"> 
            <div class="col-md-12" style="height: 220px; padding-top: 20px" >
                  <img src="images/exomr2.jpg" width="100%">
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