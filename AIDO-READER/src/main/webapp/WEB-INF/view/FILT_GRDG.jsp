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
  <script src="js/jquery.min.js"></script>
  
<script type="text/javascript">
var omrcd = 0;
var examcd = 0;
var lsncd = 0;
var omrnm = "";
var limit = 5;
var pageno = 1;

$(document).ready(function() {
	if("${param.limit}" != ""){
	  	$("#limit").val("${param.limit}").prop("selected", true);
	  	console.log(${param.limit})
	  }else{
	  	$("#limit").val("5").prop("selected", true);
	  }
})


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
		bstr = "(";
		 $('input:checkbox[name="chk"]').each(function() {
			   var as = $(this).is(":checked")
			   if(as){
				   var bstorcd =$(this).attr("id")
				   bstr +="\'"+bstorcd+"\',"
			   }
			})
			bstr=bstr.substring(0,bstr.length-1)
			bstr+=")"
			console.log(bstr)
		$("th[name=sgrade]",opener.document).attr("id",bstr);
		
		self.close();
	}
	function clo(){
		self.close();
	}
	function chkall(){
		console.log('start')
		var chk = $('input:checkbox[id="allchk"]').is(":checked")
		if(chk){
			console.log('already')
			$('input:checkbox[name="chk"]').each(function() {
			    this.checked = true;
			})
		}else{
			console.log(chk)
			$('input:checkbox[name="chk"]').each(function() {
			    this.checked = false;
			})
		}
	}
	 function listdo(page) {
		 pageno=page;
 		f = document.searchform;
 		f.pageNum.value=page;
 		f.submit();
 	}
	 function limitchg(){
       	 var lt = $("#limit option:selected").val();
       	console.log(lt);
       	limit = lt
       	var f = document.searchform;
       	f.pageNum.value=pageno;
       	f.limit.value=limit;
       	f.submit();
       }
</script>
<title>Insert title here</title>
</head>
<body>
  <div class="col-md-12" style="height: 100%; margin-top: 5%;"  >
            <div class="iq-card" style="width: 100%; height: 100%;" >
                  <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                        	<form class="position-relative" name="searchform" id="searchform" action="FILT_GRDG.ai" method="post">
                              <div class="form-group mb-0">
                              	<table style="border: none; width: 100%;"  >
                                   		<tr style="border: none; width: 100%;">
                                   	 		<th style="border: none; width: 85%;">
                                   	 			<input type="hidden" name="pageNum" value="${param.pageNum }">
                                     			<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="searchcontent" value="${param.searchcontent }">
                                   	 		</th>
                                   	 		<th style="border: none; width: 15%; padding-top: 15px;"> 
                                   	 			<button onclick="listdo('1')" class="btn bg_01 btn btn-primary mb-3">검색</button>
                                   	 		</th> 
                                   		</tr>
                                   	</table>
                                </div>
                              <br>
                           
                              <div style="width: 95%; height: 45%;">
                                    <div class="namelist no-wrap" style="width: 95%; height: 100%;">
                                    	<table class="table" style="width: 100%;">
                                    		<tr style="width: 100%;">
                                    		<th style="width: 50%;"><input type="checkbox" id="allchk" onclick="chkall()" checked="checked"></th>
                                    		<th style="width: 50%;">지점명</th> 
                                    		</tr>
                                    		<c:forEach var="bst" items="${blist}">
                                    			<tr style="width: 100%;">
                                    				<td style="width: 50%;"><input type="checkbox" id="${bst.SCHYR}" name="chk" checked="checked"></td>
                                    				<td style="width: 50%;">${bst.SCHYR}</td>
                                    			</tr>
                                    		</c:forEach>
                                    	</table>
                                    </div>      
                              </div>
                              <div align="right" style="padding-left: 3%; height: 10%;" class="row">
                              				  
                                 				<div style="width: 30%; padding-top: 0px; height: 100%;">
                                    				글 갯수  
                                    				<select id="limit" name = "limit" onchange="limitchg()">
                                    					<option value="5">5</option>
                                    					<option value="10">10</option>
                                    					<option value="15">15</option>
                                    					
                                    				</select>                                    
                                 				</div>
                                 				<div style="width: 70%; padding-top: 0px; height: 100%;" align="center">
                                  					<c:if test="${pageNum > 1}">
														<a href="javascript:listdo('${pageNum-1 }')" class="iq-icons-list">이전</a>
													</c:if>
													<c:if test="${pageNum <= 1}">
														이전
													</c:if>
													<c:forEach var="a" begin="${startpage }" end="${endpage }">
													  <c:if test="${a == pageNum }">[${a }]</c:if>
													  <c:if test="${a != pageNum }">
														<a href="javascript:listdo('${a }')">[${a }]</a>
													  </c:if>
													</c:forEach>
													<c:if test="${pageNum < maxpage }">
													  <a class="iq-icons-list" href="javascript:listdo('${pageNum+1 }')">다음</a>
													</c:if>
													<c:if test="${pageNum >= maxpage }">다음</c:if>                                          
                                 				 </div>
                              				   
                           					</div>
                           					
                           				  </form>
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