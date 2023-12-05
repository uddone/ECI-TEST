<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재업로드 파일 확인</title>
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

<style type="text/css">
.select {
      padding:3px;
      background-color: #f9fbe7;
   }
   
   .select>a {
      color:#ffffff;
      text-decoration: none;  /* 하이퍼링크 밑줄 제거 */
      font-weight:bold;    /* 글씨체 굵게 */
   }
   .no-wrap {width:100%; overflow-y:scroll; overflow-x:scroll;  white-space:nowrap; border-collapse:collapse}
   
</style>
<script type="text/javascript">
var omrcd = '${omrcd}'
var examcd ='${examcd}'
var bstorcd ='${bstorcd}'
	function disp_div(fl){
		$(".sub").each(function() {
            $(this).removeClass("select");
         })
         $("#"+fl).addClass("select");
		 var form = $('#f');
    	 var formData = new FormData(form[0]);
    	 formData.append('filename',fl)
    	 formData.append('omrcd',omrcd)
    	 formData.append('examcd',examcd)
    	 formData.append('bstorcd',bstorcd)
        $.ajax({
            url:"reuploadmimg.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(data){
         	   drawresult(data)
            }
        });
	}
	function drawresult(data){
		var src = '${pageContext.request.contextPath}/'+data[0].jspimg
		$("#crop").attr("src", src);
	}
	function clo(){
		self.close();
	}
	function s3delete(){
		var stdArr = new Array();
		$('input:checkbox[name="chk"]').each(function() {
		   var as = $(this).is(":checked")
		   if(as){
			   var chgfile =$(this).attr("id");
			   console.log(chgfile)
			   	stdArr.push(chgfile);
		   }
		})
		var form = $('#f');
    	var formData = new FormData(form[0]);
    	formData.append('filelist',stdArr)
    	formData.append('omrcd',omrcd)
    	formData.append('examcd',examcd)
    	formData.append('bstorcd',bstorcd)
    	if(confirm('삭제하시겠습니까?')){
    		$.ajax({
                url:"s3delete.ai",
                data:formData,
                type:'POST',
                processData:false,
                contentType:false,
                dataType:'json',
                cache:false,
                success:function(data){
             	   console.log(data[0].msg)
                }
            }); 		
    	}
	}
	function recogstart(){
		var form = $('#f');
    	var formData = new FormData(form[0]);
    	formData.append('omrcd',omrcd)
    	formData.append('examcd',examcd)
    	formData.append('bstorcd',bstorcd)
    	if(confirm('인식하시겠습니까?')){
    		$.ajax({
                url:"recogstart.ai",
                data:formData,
                type:'POST',
                processData:false,
                contentType:false,
                dataType:'json',
                cache:false,
                success:function(data){
             	   opener.location.reload();
             	   self.close();
             	   
                }
            });
    	}
	}
</script>
</head>
<body>
	<div class="row">
		<div style="width: 50%"> 
			<div class="col-md-12" style="height: 380px; padding-top: 20px" >
				<div class="iq-card" style="width: 100%; height: 100%;" >
					<div class="iq-card-body" style="width: 100%; height: 100%;" >
						<div class="iq-todo-page" style="height: 300px;">
							<div style="height: 50px;">
								<div class="row">
									<div style="width: 80%"><h4 style="padding-left: 10px;">검수 대기중인 omr 카드 목록</h4></div>
									<div style="width: 20%">
										<button class="btn bg_01 btn btn-primary mb-3" onclick="s3delete()">삭제</button>
										<button class="btn bg_01 btn btn-primary mb-3" onclick="recogstart()">인식시작</button>
									</div>
								</div>
							</div>
							<div class="no-wrap" style="height: 250px;">
							<form name="f" id="f"></form>
								<table width="100%" class="table">
									<tr>
										<th style="width: 50px">파일명</th>
            						</tr>
            						<c:forEach items="${flist}" var="fl">
            							<tr id ="${fl.CHG_FILE_NM}" class="sub" onclick="disp_div('${fl.CHG_FILE_NM}')">
            								<td><input type="checkbox" id="${fl.CHG_FILE_NM}" name="chk"></td> 
            								<td>${fl.ORIGIN_FILE_NM}</td>
            							</tr>  
            						</c:forEach>
            					</table>
							</div>
						</div>
      					<div style="height: 50px;" align="center" >
            				<button class="btn btn-primary" style="width: 30%" onclick="clo()">닫기</button>
      					</div>
					</div>
				</div>
			</div>
      	</div>
      	<div style="width: 50%"> 
            <div class="col-md-12" style="height: 220px; padding-top: 20px" >
            	<img id ="crop" src="" width="100%">
            </div>
      	</div>
     </div>
</body>
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
</html>