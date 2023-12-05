<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>재 업로드 필요 파일</title>
	<style type="text/css">
		td,th,table,tr{border: 1px solid #dee2e6; text-align: center; width : 20px;}
		th{ background-color: #eeecef ; }
		.no-wrap {width:100%; overflow-y:scroll; overflow-x:scroll;  white-space:nowrap; border-collapse:collapse}
		.no-wraph {width:100%; overflow-x:scroll;  white-space:nowrap; border-collapse:collapse}
		.no-wrapv {width:100%; overflow-y:scroll;  white-space:nowrap; border-collapse:collapse}
		.no-wrapt {white-space:nowrap; border-collapse:collapse}
		.white{background-color: white;}
		.select {
		  padding:3px;
		  background-color: #f9fbe7;
		}
	   
		.select>a {
		  color:#ffffff;
		  text-decoration: none;  /* 하이퍼링크 밑줄 제거 */
		  font-weight:bold;    /* 글씨체 굵게 */
		}
		#gradtable thead th {
			/*position: -webkit-sticky; /* for Safari */
			position: sticky;
			top: 0px;
			background-color: #eeecef !important;
		}
		#gradtable thead th:nth-child(2){
			left: 0px;
			z-index: 1;
		}
		#gradtable thead th:nth-child(3){
			left: 94px;
			z-index: 1;
		}
		#gradtable thead th:nth-child(4){
			left: 123px;
			z-index: 1;
		}  
		#gradtable thead th:nth-child(5){
			left: 191px;
			z-index: 1;
		}  
		#gradtable thead th:nth-child(6){
			left: 259px;
			z-index: 1;
		}  
		#exno{
			position: sticky;
			left: 0px;
			/*background-color: #eeecef !important;*/
		}
		#sex{
			position: sticky;
			left: 94px;
			/*background-color: #eeecef !important;*/
		} 
	   #birth{
			position: sticky;
			left: 123px;
			/*background-color: #eeecef !important;*/
	   }
	   #stnm{
			position: sticky;
			left: 191px;
			/*background-color: #eeecef !important;*/
	   }
	   #scr{
			position: sticky;
			left: 259px;
			background-color: #eeecef !important;
	   }
	</style>
	<script type="text/javascript">
		var examcd = "";
		var mstcd = "";
		var rpageNum = 1;
		var rlimit = 5;
		var pageno = 1;
		var limit = 20;
		var bstorcd="";
		var pic = "";
		$(document).ready(function(){
			if("${param.limit}" != ""){
				$("#limit").val("${param.limit}").prop("selected", true);
				console.log(${param.limit})
			}else{
				$("#limit").val("20").prop("selected", true);
			}	
			$("th[name=sbstor]").attr("id","${searchbstor}")
			$("th[name=smock]").attr("id","${searchmock}")
			$("th[name=sgrade]").attr("id","${searchgrade}")
			$("th[name=ssub]").attr("id","${searchsub}")
		  // report('1');
		   //report('2');
		   //report('3');
		    console.log("pn : ${param.pageNum}")
		    
			$("input[name=searchcontent]").keydown(function(key) {
					//키의 코드가 13번일 경우 (13번은 엔터키)
				if (key.keyCode == 13) {
					searchfilter();
				}
			});
			$("input[name=rsearchcontent]").keydown(function(key) {
				//키의 코드가 13번일 경우 (13번은 엔터키)
				console.log("rsearchcontent")
				if (key.keyCode == 13) {
					console.log("enterkey")
					rsearch();
				}
			});
		 })
		 
		function disp_div(excd,bstor_cd,mst_cd) {
			$("#rsearchcontent").val('');
			$(".sub").each(function() {
				$(this).removeClass("select");
			})

			$('#picbtn').attr("disabled","disabled");
			$("#" +excd+"a"+bstor_cd+"a"+mst_cd).addClass("select");
			examcd = excd;
			mstcd = mst_cd;
			bstorcd = bstor_cd
			var form = $('#searchform');
			var formData = new FormData(form[0]);
			formData.append("examcd",examcd)
			formData.append("mstcd",mstcd)
			formData.append("bstorcd",bstorcd)
			console.log(examcd+","+mstcd+","+bstorcd)
			$.ajax({
				url:"REUPLD_MLIST.ai",
				data:formData,
				type:'POST',
				processData:false,
				contentType:false,
				dataType:'json',
				cache:false,
				success:function(result){
					drawtable(result);
				}
			});
		}
		function drawtable(data){
			console.log(data)
			var tble = ""
			tble = '<table id="f_nm_table" style="width : 100%" class="table">';//47 107
			tble += '<thead><tr><th style="background-color: #f0f0f0; width:13px;"><input type="checkbox" id="flistchkall" onclick="bchkall()"></th><th style="background-color: #f0f0f0;" id="exnoth" >파일명</th>';
			var qcnt = data.length-1;
			tble +='</tr></thead><tbody id="f_nm_body">';
			for(var fnm = 0 ; fnm < data.length; fnm++) {
				tble += '<tr class="imagerow" id="'+ data[fnm].CHG_FILE_NM + 'jpg" onclick="REUPLD_IMG(' + data[fnm].MST_CD + ',' + data[fnm].EXAM_CD + ',' + data[fnm].CHG_FILE_NM + ',' + data[fnm].BSTOR_CD + ')">';
				tble += '<td style="width:13px;"><input type="checkbox" name="reupdch" id="' + data[fnm].CHG_FILE_NM + '"></td>';
				tble += '<td>'+data[fnm].ORIGIN_FILE_NM+'</td>';
				tble += '</tr>';
			}
			tble += '</tbody></table>';
			$("#F_NM_DIV").html(tble);
		}

		function REUPLD_IMG(omrcd,examcd,fl,bstor_cd){
			$(".imagerow").each(function() {
				$(this).removeClass("select");
			})
			$("#"+fl+"jpg").addClass("select");
			var form = $('#recogform');
			var formData = new FormData(form[0]);
			formData.append('filename',fl)
			formData.append('omrcd',omrcd)
			formData.append('examcd',examcd)
			formData.append('bstorcd',bstor_cd)
			$.ajax({
				url:"REUPLD_IMG.ai",
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
		var pic = ""
		function drawresult(data){
			pic = data.jspimg
			var src = '${pageContext.request.contextPath}/'+data.jspimg
			$("#crop").attr("src", src);
			$('#picbtn').removeAttr('disabled');
		}
		function IMG_WID(){
			var ope ="width="+screen.width/2+", height="+screen.height*3/4+", left=50, top=0";
			open("IMG_WIN.ai?data="+encodeURI(pic),"newimgwin",ope);
		}

		function bchkall(){
			//console.log('start')
			var bchk = $('input:checkbox[id="flistchkall"]').is(":checked")
			if(bchk){
			//	console.log('already')
				$('input:checkbox[name="reupdch"]').each(function() {
					this.checked = true;
				})
			}else{
			//	console.log(bchk)
				$('input:checkbox[name="reupdch"]').each(function() {
					this.checked = false;
				})
			}
		}
		
		// function IMG_WID(SEQ){
		// 	var form = $('#recogform');
		//     var formData = new FormData(form[0]);
		// 	formData.append("SEQ",SEQ)
		// 	$.ajax({
		// 		url: "SEQ_IMG.ai",
		// 		type: "POST",
		// 		data:formData,
		// 		dataType:'json',
		// 		processData:false,
		// 		contentType:false,
		// 		cache:false,
		// 		success: function(data){
		// 			pic = data.jspimg;
		// 			//console.log(pic)
		// 			var ope ="width="+screen.width/2+", height="+screen.height*3/4+", left=50, top=0";
		// 			   open("IMG_WIN.ai?data="+encodeURI(pic),"newimgwin",ope);
		// 		},
		// 		error : function(e){
		// 			alert("서버오류 : " + e.status);
		// 		}
		// 	})
		//
		// }

		
		function listdo(page) {
			var sbstor = $("th[name=sbstor]").attr("id")
			var smock = $("th[name=smock]").attr("id")
			var sgrade = $("th[name=sgrade]").attr("id")
			var ssub = $("th[name=ssub]").attr("id")
			var sscore = $("th[name=sscore]").attr("id")
			//console.log(sscore+","+sbstor+","+smock+","+sgrade+","+ssub)
			//console.log(page)
			pageno=page
			f = document.searchform;
			f.pageNum.value=page
			var a = f.pageNum.value
			// console.log(a)
			f.searchbstor.value=sbstor;
			f.searchmock.value=smock;
			f.searchgrade.value=sgrade;
			f.searchsub.value=ssub;
			f.submit();
		}
			
		function limitchg(){
			var lt = $("#limit option:selected").val();
			// console.log(lt);
			limit = lt;
			var sbstor = $("th[name=sbstor]").attr("id")
			var smock = $("th[name=smock]").attr("id")
			var sgrade = $("th[name=sgrade]").attr("id")
			var ssub = $("th[name=ssub]").attr("id")
			var sscore = $("th[name=sscore]").attr("id")
			f = document.searchform;
			f.limit.value=limit;
			f.searchbstor.value=sbstor;
			f.searchmock.value=smock;
			f.searchgrade.value=sgrade;
			f.searchsub.value=ssub;
			f.submit();
		}
		
		
		function win_open(view){
			var op ="width=600, height=400, left=50, top=150";
			open(view+".ai?limit=5&pageNum=1&searchcontent=","",op);
		}
		
		function searchfilter(){
			var sbstor = $("th[name=sbstor]").attr("id")
			var smock = $("th[name=smock]").attr("id")
			var sgrade = $("th[name=sgrade]").attr("id")
			var ssub = $("th[name=ssub]").attr("id")
			pageno=1
			f = document.searchform;
			f.searchbstor.value=sbstor;
			f.searchmock.value=smock;
			f.searchgrade.value=sgrade;
			f.searchsub.value=ssub;
			f.pageNum.value = 1;
			f.submit();
			
			//location.href ="OMR_GRDG.ai?limit="+limit+"&pageNum="++"&searchcontent="++"&="++"&="++"&="+
		}
		function rsearch(){
			var form = $('#recogform');
			var formData = new FormData(form[0]);
			formData.append("examcd",examcd)
			formData.append("mstcd",mstcd)
			formData.append("bstorcd",bstorcd)
			$.ajax({
				url:"REUPLD_MLIST.ai",
				data:formData,
				type:'POST',
				processData:false,
				contentType:false,
				dataType:'json',
				cache:false,
				success:function(result){
					drawtable(result);
				}
			});
		}
		function reupld_view(){
			var form = $('#searchform');
	        var formData = new FormData(form[0]);
	        var index = 0
	        var seqArr = new Array();
	        $('input:checkbox[name="reupdch"]').each(function() {
			   var as = $(this).is(":checked")
			   if(as){
				   var seq =$(this).attr("id")
				   //console.log('seq :' +seq);
				   seqArr.push(seq);
			   }
			})
			console.log(seqArr);
			var op ="width=1000, height=500, left=50, top=150";
			open("REUPLD_VIEW.ai?seqArr="+seqArr,"reuldview",op);
			location.href="OMR_RECOG2.ai"
			
//			formData.append("seqArr",seqArr);
//			formData.append("index",index);
//			$.ajax({
//				url:"reupld_list.ai",
//	      	    data:formData,
//	            type:'POST',
//	            processData:false,
//	            contentType:false,
//	            dataType:'json',
//	            cache:false,
//	            success:function(result){
//	            	reupld_view(result);
//	            	location.href="OMR_RECOG2.ai"
//	            }
//	        });
		}
		
	</script>
</head>
<body>
	<div id="content-page" class="content-page">
		<div class="container-fluid">
			<div class="row row-eq-height">
				<div class="col-md-12">
					<div style="width : 100%; height: 100%;">
						<div class="row" style="width: 100%; height: 100%;">
							<div class="iq-card iq-border-radius-20" style="height: 100%; width: 40%; margin-bottom: 10px;" align="center">
								<div style="height: 40px; width: 100%; padding-top: 10px; " align="left">
									<div class="row" style="width: 100%; margin-left: 10px;" >
										<div style="width: 80%; padding-left: 20px;"><h4><b>재 업로드 파일 목록</b></h4></div>
										<div style="width: 20%; padding-right: 30px;" align="right" >
                                 	<!-- btn bg_01 btn btn-primary mb-3 -->
                                 		
                                 		<!--  	
                                 		
                                 		//0405_수정사항
                                 		
                                 		검색 버튼과 기능 동일 -> 버튼 제거
                                 		<button class="btn bg_01 btn btn-primary mb-3" onclick="searchfilter()">조회</button>
                                 		
                                 		-->

                                 		</div>
									</div>
								</div>
								<div style="width: 97%; height: 93%; padding-top: 1%">
									<div class="iq-card" style="width: 100%; height: 100%;" >
										<div class="iq-card-body" style="width: 100%; height: 100%;" >
											<div class="iq-todo-page" style="height: 100%;">
												<form class="position-relative" name="searchform" id="searchform" action="REUPLD_OMR.ai" method="post" style="height: 100%;" onsubmit="return false">
                              			 			<div class="form-group mb-0" style="padding-bottom: 5px; height: 50px;">
                                 		  				<input type="hidden" name="searchbstor" value="${param.searchbstor}">
                                 		  				<input type="hidden" name="searchmock" value="${param.searchmock}">
                                 		  				<input type="hidden" name="searchgrade" value="${param.searchgrade}">
                                 		  				<input type="hidden" name="searchsub" value="${param.searchsub}">
                                 		  				<table style="border: none; width: 100%; background-color: white;"  >
                                   							<tr style="border: none; width: 100%; background-color: white;">
                                   		 						<th style="border: none; width: 85%; background-color: white;">
                                   		 							<input type="hidden" name="pageNum" value="${param.pageNum }">
                                    	 							<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="searchcontent" value="${param.searchcontent }">
                                   		 						</th>
                                   		 						<th style="border: none; width: 15%; padding-top: 15px; background-color: white;">
                                   		 							<button type="button" onclick="searchfilter()"  class="btn bg_01 btn btn-primary mb-3">검색</button>
                                   		 						</th>
                                   							</tr>
														</table>
													</div>
													<br>
													<div class="no-wrap" style="height: 80%;">
														<table style="width: 100%; text-align: center;" class="table">
															<tr>
																<!--<th><input type="checkbox" id="bstorchkall" onclick="bchkall()"></th>-->
																<th id="(1,8,11)" onclick="win_open('FILT_MOCK')" name="smock">모의고사</th>
																<th id="('고1','고2','고3')" onclick="win_open('FILT_GRDG')" name="sgrade">학년</th>
																<th id="(3,4,5,6)" onclick="win_open('FILT_BSTOR')" name="sbstor">지점</th>
																<th id="('국어','영어','수학')" onclick="win_open('FILT_SUB')" name="ssub">교과</th>
																<th>갯수</th>
                           									</tr>
                           									<c:forEach items="${EXAMList}" var="ex">
																<tr id ="${ex.EXAM_CD}a${ex.BSTOR_CD}a${ex.MST_CD}" class="sub" onclick="disp_div(${ex.EXAM_CD},${ex.BSTOR_CD},${ex.MST_CD})">
																	<!--<td id="${ex.CHG_FILE_NM}"><input type="checkbox" name="reupdch"></td>-->
																	<td id="${ex.EXAM_CD}">${ex.EXAM_NM}</td>
																	<td>${ex.SCHYR}</td>
																	<td id="${ex.BSTOR_CD}">${ex.BSTOR_NM}</td>
																	<td>${ex.LSN_GRP_NM} </td>
																	<td>${ex.GRP_CNT} </td>
																</tr>
															</c:forEach>
														</table>
													</div>
													<div align="right" style="padding-left: 3%; height: 55px; margin-top: 5px;" class="row">
														<div style="width: 30%; padding-top: 0px; height: 100%;">글 갯수
                                    						<select id="limit" name = "limit" onchange="limitchg()">
																<option value="20">20</option>
																<option value="30">30</option>
																<option value="50">50</option>
																<option value="100">100</option>
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
											</div>
										</div>
									</div>
									<form id="BLANK"></form>
                            	</div>
                        	</div>
							<div style="width: 60%; height: 99%;">
								<div style="width: 100%; margin-left: 1%; height: 380px; margin-bottom: 10px;">
									<div style="height: 100%;">
										<div class="iq-card iq-border-radius-20" align="center" style="height: 100%;">
											<div style="height: 45px; padding-top: 1%; padding-left: 2%;" align="left">
												<div class="row" style="width: 100%" align="center">
													<div style="width: 80%; margin-left: 5%;" align="left"><h4><b>인식 정보 확인</b></h4></div>
													<div style="width: 15%">
														<span><button class="btn bg_01 btn btn-primary mb-3" onclick="reupld_view()">재업로드</button></span>
													</div>
												</div>
											</div>
											<div style="height: 335px; padding-top: 1%" >
												<div class="iq-card" style="width: 98%; height: 100%; margin-bottom: 30px;">
													<div class="iq-card-body" style="width: 100%; height: 100%; padding: 10px;" >
														<div class="iq-todo-page" style="height: 100%;">
															<form name="recogform" id="recogform" class="position-relative" onsubmit="return false">
																<div class="form-group mb-0" style="padding-bottom: 5px; height: 50px;">
																	<table style="border: none; width: 100%; background-color: white;"  >
																		<tr style="border: none; width: 100%; background-color: white;">
																			<th style="border: none; width: 90%; background-color: white;">
																				<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="rsearchcontent" id="rsearchcontent" value="${param.rsearchcontent }">
																			</th>
																			<th style="border: none; width: 10%; padding-top: 15px; background-color: white;">
																				<button onclick="rsearch()" type="button" class="btn bg_01 btn btn-primary mb-3">검색</button>
																			</th>
																		</tr>
																	</table>
																</div>
																<div class="no-wrap" id="F_NM_DIV" style="height: 220px; margin-top : 20px;" align="left"></div>
															</form>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div style="width: 100%; margin-left: 1%; height: 650px;">
									<div class="iq-card iq-border-radius-20 " style="height: 100%; padding-top: 1%;" align="center">
										<div style="height: 45px;" align="left">
											<div class="row" style="padding-top: 1%;">
												<div style="width: 85%; padding-left: 3%;"><h4><b>OMR 상세 정보 확인</b></h4></div>
												<div style="width: 15%; padding-right: 3%;" align="right"><button class="btn btn-primary mb-3" id ="picbtn" onclick="IMG_WID()" disabled="disabled">크게보기</button></div>
											</div>
										</div>
										<div style="height: 90%; padding-top: 1%" >
											<div class="iq-card" style="width: 98%; height: 100%; margin-bottom: 10px; background-color: black" >
												<div class="iq-card-body" style="width: 100%; height: 100%;" >
													<div class="iq-todo-page" style="height: 100%;">
														<div class="no-wrap" style="height: 100%;">
															<img id="crop" width="100%;" height="100%;">
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>