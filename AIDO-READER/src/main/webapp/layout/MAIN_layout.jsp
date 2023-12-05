<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/webapp/layout/layout.jsp --%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html lang="en">
   <head>
   <!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=G-V270SW8GJ3"></script>
<script>
window.dataLayer = window.dataLayer || [];
function gtag(){dataLayer.push(arguments);}
gtag('js', new Date());

gtag('config', 'UA-185906402-1');


(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-185906402-1');
	var userid = "${MBR.NAME}";	
	ga('set', 'dimension3',userid);

	ga('send','pageview')
</script>
      <!-- Required meta tags -->
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <title><decorator:title /></title>
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
       
      <style type="text/css">
      	
      </style>
      <script src="js/jquery.min.js"></script>
 <!--  
      <script type="text/javascript" src="http://jsgetip.appspot.com/?getip"></script>
 -->
      <script type="text/javascript">
      $(document).ready(function() {
    	//  var check_ip = getip(); 
    	  var al = 0;
    	  var pageName = "";
    	  var a = "";
    	  var tempPageName = window.location.href;
    	  var strPageName = tempPageName.split("/");
    	    a = strPageName[strPageName.length-1].split("?")[0];
    	    pageName = a.split(".")[0]
    	    //console.log(check_ip);    
    	    $(".sidemenu").each(function() {
    	        $(this).removeClass("active");
    	     })
    	     $("#"+pageName).addClass("active")
    	      
    	     $.ajax({
        			url: "alarm.ai",
        			type: "GET",
        			data:{
        				'al':al
        			},
        			dataType:'json',
        			processData:false,
                    contentType:false,
                    cache:false,
        			success: function(data){
        				console.log(data);
        				alarmlist(data)
        				
        				//
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
      })
       /*function callback(data) {
    	document.getElementById('client-ip').textContent = data.ip;
  		}*/	
      function alarmlist(data){
    	var html = "";
    	html +=' <div class="bg-primary p-3">';
    	html +='<h5 class="mb-0 text-white">알림<small class="badge  badge-light float-right pt-1">'+data.length+'</small></h5></div>';
    	for(var len = 0;len<data.length;len++){
    		html +='<a href="'+data[len].view+'" class="iq-sub-card" >';
        	html +='<div class="media align-items-center">';
        	html +='<div class="media-body ml-3">';
        	html +='<h6 class="mb-0 ">'+data[len].exam_nm+' 인식이 완료 되었습니다.</h6>';
        	html +='<small class="float-right font-size-12">'+data[len].al_date+'</small>';
        	html +='</div></div></a>';	
    	}
    	$("#alarm").html(html);
      }
  		function logout(){
  			location.href="LOGIN.ai"
  		}
      </script>
      <decorator:head />
   </head>
    <body class="icon-with-text">
   <!-- loader Start -->
        <div id="loading"><div id="loading-center"></div></div>
        <!-- loader END -->
        <!-- Wrapper Start -->
        <div class="wrapper"  style="padding-bottom : 30px;">
         <!-- Sidebar  -->
            <div class="iq-sidebar sidebar-dark-blue">
                <div class="iq-sidebar-logo d-flex justify-content-between">
                    <a href="MAIN.ai">
                        <img src="images/aido_reader.png" width="135px" height="45px" class="img-fluid" alt="">
                    </a>
                </div>
                <hr style="border-color: white;">
                <div id="sidebar-scrollbar">
                    <nav class="iq-sidebar-menu">
                        <ul id="iq-sidebar-toggle" class="iq-menu">
                            <c:if test="${sessionScope.MBR.MBR_NO == 3 }">
                                <li class="sidemenu" onclick="sidesele('OMR_REG')" id="OMR_REG">
                                    <a href="OMR_REG.ai" class="iq-waves-effect"><i class="ri-home-4-line"></i><span>OMR 관리</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y'}">
                                <li class="sidemenu" onclick="sidesele('MOCK_ADM')" id="MOCK_ADM">
                                    <a href="MOCK_ADM.ai" class="iq-waves-effect"><i class="ri-home-3-line"></i><span>모의고사 관리</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'N' || sessionScope.MBR.MBR_NO == 3}">
                                <li class="sidemenu" onclick="sidesele('OMR_RECOG')" id="OMR_RECOG" style="display: none;">
                                    <a href="OMR_RECOG.ai" class="iq-waves-effect"><i class="ri-menu-3-line"></i><span>OMR 인식</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y' }">
                                <li class="sidemenu" onclick="sidesele('OMR_RECOG2')" id="OMR_RECOG2">
                                    <a href="OMR_RECOG2.ai" class="iq-waves-effect"><i class="ri-menu-3-line"></i><span>OMR 인식</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y'}">
                                <li class="sidemenu" onclick="sidesele('REUPLD_OMR')" id="REUPLD_OMR">
                                    <a href="REUPLD_OMR.ai"><i class="ri-remixicon-line"></i>재 업로드 대상 OMR</a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y' }">
                                <li class="sidemenu" onclick="sidesele('OMR_GRDG')" id="OMR_GRDG">
                                    <a href="OMR_GRDG.ai" class="iq-waves-effect"><i class="ri-pencil-ruler-line"></i><span>OMR 채점</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_NO ==  83 || sessionScope.MBR.MBR_NO ==  88 || sessionScope.MBR.MBR_NO ==  96 || sessionScope.MBR.MBR_NO ==  97 || sessionScope.MBR.MBR_NO ==  98}">
                                <li class="sidemenu" onclick="sidesele('OMR_GRDG_ADM')" id="OMR_GRDG_ADM">
                                    <a href="OMR_GRDG_ADM.ai" class="iq-waves-effect"><i class="ri-pencil-ruler-line"></i><span>OMR 채점</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_NO !=  83 && sessionScope.MBR.MBR_NO !=  88 && sessionScope.MBR.MBR_NO !=  96 && sessionScope.MBR.MBR_NO !=  97 && sessionScope.MBR.MBR_NO !=  98}">
                                <li class="sidemenu" onclick="sidesele('OMR_REPORT')" id="OMR_GRDG_ADM">
                                    <a href="OMR_REPORT.ai" class="iq-waves-effect"><i class="ri-keynote-line"></i><span>OMR 리포트</span></a>
                                </li>
                            </c:if>
                            <hr style="border-color: white;">
                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y'}">
                                <li class="sidemenu" onclick="sidesele('OCR_EXAM_ADM')" id="OCR_EXAM_ADM">
                                    <a href="OCR_EXAM_ADM.ai" class="iq-waves-effect"><i class="ri-profile-line"></i><span>OCR 시험 관리</span></a>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.MBR.MBR_NO == 3 || sessionScope.MBR.MBR_NO == 7}">
                                <li class="sidemenu" onclick="sidesele('OCR_TYPE_ADM')" id="OCR_TYPE_ADM" style="display: none;">
                                    <a href="OCR_TYPE_ADM.ai" class="iq-waves-effect"><i class="ri-grid-line"></i><span>OCR 유형 관리</span></a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'N' || sessionScope.MBR.MBR_NO == 1 || sessionScope.MBR.MBR_NO == 4 || sessionScope.MBR.MBR_NO == 3 || sessionScope.MBR.MBR_NO == 7 || sessionScope.MBR.MBR_NO == 8}">
                                <li class="sidemenu" onclick="sidesele('BSTOR_RC_REG_SN')" id="BSTOR_RC_REG_SN">
                                    <a href="BSTOR_RC_REG_SN.ai"><i class="ri-record-circle-line"></i>수능 개인성적표 등록</a>
                                </li>
                                <li class="sidemenu" onclick="sidesele('BSTOR_RC_REG_MOCK')" id="BSTOR_RC_REG_MOCK">
                                    <a href="BSTOR_RC_REG_MOCK.ai"><i class="ri-record-circle-line"></i>평가원 모의고사 <br>개인 성적표 등록</a>
                                </li>
                                <li style="display: none;">
                                    <a href="#"><i class="ri-record-circle-line"></i>생활기록부 등록</a>
                                </li>
                                <li class="sidemenu" onclick="sidesele('BSTOR_RC_REG_IR')" id="BSTOR_RC_REG_IR" >
                                    <a href="BSTOR_RC_REG_IR.ai"><i class="ri-record-circle-line"></i>평가원 모의고사 <br>성적 일람표 등록</a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y'}">
                                <li class="sidemenu" onclick="sidesele('BSTOR_RC_ADM_SN')" id="BSTOR_RC_ADM_SN">
                                    <a href="BSTOR_RC_ADM_SN.ai"><i class="ri-record-circle-line"></i>수능 개인성적표</a>
                                </li>
                                <li class="sidemenu" onclick="sidesele('BSTOR_RC_ADM_MOCK')" id="BSTOR_RC_ADM_MOCK">
                                    <a href="BSTOR_RC_ADM_MOCK.ai"><i class="ri-record-circle-line"></i>평가원 모의고사 <br>개인 성적표</a>
                                </li>
                                <li style="display: none;">
                                    <a href="#"><i class="ri-record-circle-line"></i>생활기록부</a>
                                </li>
                                <li class="sidemenu" onclick="sidesele('BSTOR_RC_ADM_IR')" id="BSTOR_RC_ADM_IR"  >
                                    <a href="BSTOR_RC_ADM_IR.ai"><i class="ri-record-circle-line"></i>평가원 모의고사 <br>성적 일람표</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                    <div class="p-3"></div>
                </div>
            </div>
         <!-- Sidebar END -->
         <!-- TOP Nav Bar -->
            <div class="iq-top-navbar">
                <div class="iq-navbar-custom">
                    <div class="iq-sidebar-logo">
                        <div class="top-logo">
                            <a href="main.ai" class="logo">
                                <img src="images/exomr.jpg" class="img-fluid" alt="">
                            </a>
                        </div>
                    </div>
                    <nav class="navbar navbar-expand-lg navbar-light p-0">
                        <div class="navbar-left"><div class="iq-search-bar"></div></div>
                        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"  aria-label="Toggle navigation">
                            <i class="ri-menu-3-line"></i>
                        </button>
                        <div class="iq-menu-bt align-self-center">
                            <div class="wrapper-menu">
                                <div class="main-circle"><i class="ri-arrow-left-s-line"></i></div>
                                <div class="hover-circle"><i class="ri-arrow-right-s-line"></i></div>
                            </div>
                        </div>
                        <div class="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul class="navbar-nav ml-auto navbar-list">
                                <li class="nav-item">
                                    <a href="#" class="search-toggle iq-waves-effect">
                                        <div id="lottie-beil"></div>
                                        <span class="bg-danger dots"></span>
                                    </a>
                                    <div class="iq-sub-dropdown">
                                        <div class="iq-card shadow-none m-0">
                                            <div class="iq-card-body p-0 " id="alarm"></div>
                                        </div>
                                    </div>
                                </li>
                                <!--
                            <c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y'}">
                        	<li class="nav-item dropdown">
                           <a href="MAIN-INQR.ai" class="search-toggle iq-waves-effect">
                             <div id="lottie-mail"></div>
                              <span class="bg-primary count-mail"></span>
                           </a>
                           <div class="iq-sub-dropdown">
                              <div class="iq-card shadow-none m-0">
                                 <div class="iq-card-body p-0 ">
                                    <div class="bg-primary p-3">
                                       <h5 class="mb-0 text-white">문의<small class="badge  badge-light float-right pt-1">5</small></h5>
                                    </div>
                                    <a href="#" class="iq-sub-card" >
                                       <div class="media align-items-center">
                                          <div class="">
                                             <img class="avatar-40 rounded" src="images/user/05.jpg" alt="">
                                          </div>
                                          <div class="media-body ml-3">
                                             <h6 class="mb-0 ">EDU-AI</h6>
                                             <small class="float-left font-size-12">23 Oct</small>
                                          </div>
                                       </div>
                                    </a>
                                 </div>
                              </div>
                           </div>
                        </li>
                        </c:if>
                        -->
                            </ul>
                        </div>
                        <c:if test="${!empty sessionScope.MBR}">
                            <ul class="navbar-list">
                                <li>
                                    <a href="#" class="search-toggle iq-waves-effect d-flex align-items-center bg-primary rounded">
                                        <img src="images/newe247logo.png" class="img-fluid rounded mr-3" alt="user">
                                        <div class="caption">
                                            <h6 class="mb-0 line-height text-white">${sessionScope.MBR.NAME}</h6>
                                            <span class="font-size-12 text-white">${sessionScope.MBR.CMPN_NM} ${sessionScope.MBR.BSTOR_NM}</span>
                                        </div>
                                        <div style="padding-top: 6px;">
                                            <button onclick="logout()" class="btn bg_01 btn btn-primary mb-3" style="background: white; color: white;"><img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTYgOXYtNGw4IDctOCA3di00aC04di02aDh6bS0xNi03djIwaDE0di0yaC0xMnYtMTZoMTJ2LTJoLTE0eiIvPjwvc3ZnPg==" height="25px" style="background-color: white;"></button>
                                            <!--  style="border-radius: 10px 10px 10px 10px; height: 69px; background-color: white;"-->
                                        </div>
                                    </a>
                                    <div class="iq-sub-dropdown iq-user-dropdown">
                                        <div class="iq-card shadow-none m-0">
                                            <div class="iq-card-body p-0 ">
                                                <div class="bg-primary p-3">
                                                    <h5 class="mb-0 text-white line-height">${sessionScope.MBR.NAME}</h5>
                                                    <span class="text-white font-size-12">${sessionScope.MBR.CMPN_NM} ${sessionScope.MBR.BSTOR_NM}</span>
                                                </div>
                                                <c:if test="${sessionScope.MBR.MBR_NO == 3 || sessionScope.MBR.MBR_MST_YN == 'Y'}">
                                                    <a href="MAIN-INI_ADM.ai" class="iq-sub-card iq-bg-primary-hover">
                                                        <div class="media align-items-center">
                                                            <div class="rounded iq-card-icon iq-bg-primary">
                                                                <i class="ri-lock-line"></i>
                                                            </div>
                                                            <div class="media-body ml-3">
                                                                <h6 class="mb-0 ">사용자 정보 수정</h6>
                                                                <p class="mb-0 font-size-12">비밀번호 등을 변경 할 수 있습니다.</p>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </c:if>
                        <c:if test="${empty sessionScope.MBR}">
                            <ul class="navbar-list">
                                <li><button onclick="location.href='LOGIN.ai'" class="btn btn-primary">로그인</button></li>
                            </ul>
                        </c:if>
                    </nav>
                </div>
            </div>
         <!-- TOP Nav Bar END -->
         <!-- Page Content  -->
            <decorator:body />
        </div>
      <!-- Wrapper END -->
      <!-- Footer -->
        <footer class="bg-white iq-footer">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-6">
                        <ul class="list-inline mb-0"></ul>
                    </div>
                    <div class="col-lg-6 text-right">
                        Copyright 2020 <a href="#">EDU-AI</a> All Rights Reserved.
                    </div>
                </div>
            </div>
        </footer>
      <!-- Footer END -->
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
