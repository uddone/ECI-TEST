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
      	.no-wrap {width:100%; overflow-y:scroll; overflow-x:scroll;  white-space:nowrap; border-collapse:collapse}
      </style>
      <script src="js/jquery.min.js"></script>
      <script type="text/javascript">
      $(document).ready(function() {
    	  var tr = $("#lsnselbody").children();
    	  tr.each(function(i){
    		var omrkey = tr.eq(i).attr("id");
    		var lsncd = tr.eq(i).attr("name");
    		console.log(lsncd);
    		if(lsncd != -1 && lsncd != -2){
    			$("#"+omrkey+"sel").val(lsncd).prop("selected", true);
    		}
		
    	  })
      })
    	
      	function selectsave(){
      		var tr = $("#lsnselbody").children()
       		var form = $('#F');
        	var formData = new FormData(form[0]);
      		var index = 0
			var keylsnlist = new Array();
       		tr.each(function(i){
       			var okeyk = tr.eq(i).attr("id")
       			var td= tr.eq(i).children();
       			var lsncd = td.eq(3).children().eq(0).val();
       			console.log('omrkey : '+okeyk+', lsncd : '+lsncd);
       			if(lsncd != 100){
					var keylsn = okeyk+'a'+lsncd
					keylsnlist.push(keylsn);
				}
      		})
      		formData.append("keylsnlist",keylsnlist);
    		
    	   	$.ajax({ 
                url:"updatenolsn.ai",
                data:formData,
                type:'POST',
                processData:false,
                contentType:false,
                dataType:'json',
                cache:false,
                success:function(result){
                	console.log(result)
                	clo(result);
                }
            });
      	}
      	function clo(result){
      		var cnt = 0
      		for(var a= 0 ; a<result.length;a++){
      			if(result[a].msg != "변경완료"){
					cnt ++;
      			}
      		}
      		if(cnt == 0){
      			alert("변경 완료.")
      			opener.location.reload();
      			self.close();
      		}else{
      			alert("변경중 오류 발생 본사로 문의 바랍니다 ")
      		}
      	}
      </script>
</head>
<body>
	<div class="col-md-12" style="height: 100%; padding-top: 20px;" >
		<div class="iq-card" style="width: 100%; height: 100%;" >
			<div class="iq-card-body" style="width: 100%; height: 100%;" >
				<div class="iq-todo-page" style="height: 100%; padding-top: 5%;">
					<div>
					<form name="F" id="F"></form>
						<table class="table">
							<thead>
								<tr><th>학번</th><th>학생명</th><th>교과</th><th>선택과목</th></tr>
							</thead>
							
							<tbody id="lsnselbody">
								<c:forEach items="${lsnlist}" var="lli">
                               			<tr id ="${lli.OMR_KEY}" class="sub" name="${lli.LSN_CD}">
                               				<td>${lli.EXMN_NO}</td>
                               				<td>${lli.STDN_NM}</td>
                               				<c:if test="${lli.OMR_MST_CD == 6 || lli.OMR_MST_CD == 10 }">
                               					<td>국어</td>
                               				</c:if>
                               				<c:if test="${lli.OMR_MST_CD == 7 || lli.OMR_MST_CD == 11 }">
                               					<td>수학</td> 
                               				</c:if> 
                               				<c:if test="${lli.OMR_MST_CD == 9 || lli.OMR_MST_CD == 14 }">
                               					<td>탐구</td>	
                               				</c:if>
                               				<c:if test="${lli.OMR_MST_CD == 15 }">
                               					<td>제2외국어/한문</td>	
                               				</c:if>	
                               					<td> 
                               						<select name="lsnsel" id="${lli.OMR_KEY}sel">
														<option value="100">선택과목없음</option>
                               						<c:if test="${lli.OMR_MST_CD == 6 || lli.OMR_MST_CD == 10}">
                               							<option value="39">화법과작문</option>
                               							<option value="40">언어와매체</option>
                               						</c:if>
                               						<c:if test="${lli.OMR_MST_CD == 7 || lli.OMR_MST_CD == 11}">
                               							<option value="36">확률과통계</option>
                               							<option value="37">미적분</option>
                               							<option value="38">기하</option>
                               						</c:if>
                               						<c:if test="${lli.OMR_MST_CD == 9 || lli.OMR_MST_CD == 14}">
                               							<option value="11">생활과윤리</option>
                               							<option value="12">윤리와사상</option>
                               							<option value="13">한국지리</option>
                               							<option value="14">세계지리</option>
                               							<option value="15">동아시아</option>
                               							<option value="16">세계사</option>
                               							<option value="17">경제</option>
                               							<option value="18">정치와법</option>
                               							<option value="19">사회문화</option>
                               							<option value="20">물리학I</option>
                               							<option value="21">화학I</option>
                               							<option value="22">생명과학I</option>
                               							<option value="23">지구과학I</option>
                               							<option value="24">물리학II</option>
                               							<option value="25">화학II</option>
                               							<option value="26">생명과학II</option>
                               							<option value="27">지구과학II</option>
                               						</c:if>
                               						<c:if test="${lli.OMR_MST_CD == 15}">
                               							<option value="7">독일어I</option>
                               							<option value="8">프랑스어I</option>
                               							<option value="9">스페인어I</option>
                               							<option value="42">중국어I</option>
                               							<option value="43">일본어I</option>
                               							<option value="44">러시아어I</option>
                               							<option value="45">아랍어I</option>
                               							<option value="47">베트남어</option>
                               							<option value="46">한문</option>
                               						</c:if>
                               						</select>
                               					</td>
                               				
                                		</tr>  
                               			</c:forEach>
							</tbody>
					</table>
					</div>
					<div>
					<button class="btn bg_01 btn btn-primary mb-3" onclick="selectsave()">저장</button>
					</div>
					<!--  <img src="${pageContext.request.contextPath}/${jspimg}" width="100%;" height="100%;">-->
				</div>
			</div>
		</div>
	</div>
</body>
</html>