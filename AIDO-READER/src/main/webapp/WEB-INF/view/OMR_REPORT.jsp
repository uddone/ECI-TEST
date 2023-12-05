<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OMR 인식</title>
<style>
td,th,table,tr{border: 1px solid #dee2e6; text-align: center;}
th{background-color: #eeecef ;}
    .select {
      padding:3px;
      background-color: #f9fbe7;
   }
   
   .select>a {
      color:#ffffff;
      text-decoration: none;  /* 하이퍼링크 밑줄 제거 */
      font-weight:bold;    /* 글씨체 굵게 */
   }
   
   .white {background-color: white;}
   .no-wrap {width:100%; overflow-y:scroll; overflow-x:scroll;  white-space:nowrap; border-collapse:collapse}
   .no-wrapt {white-space:nowrap;border-collapse:collapse}
      .upload-btn-wrapper {
         position: relative;
         overflow: hidden;
         display: inline-block;
      }
      
      .upload-btn-wrapper input[type=file] {
         font-size: 100px;
         position: absolute;
         left: 0;
         top: 0;
         opacity: 0;
      }
      
      #fileDragDesc {
         width: 100%; 
         height: 100%; 
         margin-left: auto; 
         margin-right: auto; 
         padding: 5px; 
         text-align: center; 
         line-height: 300px; 
         vertical-align:middle;
      }
   </style>
   
  <script src="js/jquery.min.js"></script>
   
      <script type="text/javascript">
      var examcd = "";
      var lsncd = "";
      var rpageNum = 1;
      var rlimit = 5;
      var pageno = 1;
      var limit = 5;
      var bstorcd="";
         $(document).ready(function() {
        	  
        	 if("${param.limit}" != ""){
 		     	$("#limit").val("${param.limit}").prop("selected", true);
 		     	console.log(${param.limit})
 		     }else{
 		     	$("#limit").val("5").prop("selected", true);
 		     }
         });
         
      
          
       /*  function disp_div(a) {
        	 $(".sub").each(function() {
           		$(this).removeClass("select");
        		})
        		//console.log(a)
        		$("#report"+a ).addClass("select");
        	  //	report(a)
           }
         function disp_di(a,b) {
        	 $(".sub").each(function() {
           		$(this).removeClass("select");
        		})
        		$("#report"+a ).addClass("select");
        	 //report4(a,b)
             }*/
         
         
         function limitchg(){
           	 var lt = $("#limit option:selected").val();
           	console.log(lt);
           	limit = lt
           	var f = document.searchform;
           	f.pageNum.value=pageno;
           	f.limit.value=limit;
           	f.submit();
           }
     
        
       function bchkall(){
    		console.log('start')
    		var bchk = $('input:checkbox[id="bstorchkall"]').is(":checked")
    		if(bchk){
    			console.log('already')
    			$('input:checkbox[name="grdgch"]').each(function() {
    			    this.checked = true;
    			})
    		}else{
    			console.log(bchk)
    			$('input:checkbox[name="grdgch"]').each(function() {
    			    this.checked = false;
    			})
    		}
    	}
       
       function listdo(page) {
   	 	var f = document.searchform;
          	f.pageNum.value=page;
          	f.submit();
   	}
       
       function report4(a,b){
    	   var form = $('#recogform');
    	   var formData = new FormData(form[0]);
    	   formData.append("REPORT","REPORT4");
    	   formData.append("repor","report4");
    	   formData.append("bstorcd",b);
       
       		var index = 0
       		var stdArr = new Array();
       		
       $('input:checkbox[name="grdgch"]').each(function() {
		   var as = $(this).is(":checked")
		   if(as){
			   var tr =$(this).parent().parent()
			   tr.each(function(i){
			  	 var exacd = tr.eq(i).attr("id")
			   	 stdArr.push(exacd);
		     	});
		   }
		})
		 
			formData.append("stdArr",stdArr);
			$.ajax({ 
				url:"report4.ai",
           		data:formData,
        	   	type:'POST',
      		   	processData:false,
      		   	contentType:false,
      		   	dataType:'json',
     		   	cache:false,
      		   	success:function(result){
      		   		console.log(result)
       		   		download(result)
          	 	}
       		});
       }
       
        function report(a){
        	console.log(a)
        	var form = $('#recogform');
            var formData = new FormData(form[0]);
            
            var index = 0
            var stdArr = new Array();
            $('input:checkbox[name="grdgch"]').each(function() {
    		   var as = $(this).is(":checked")
    		   if(as){
    			   var tr =$(this).parent().parent()
    			   tr.each(function(i){
    				   var exacd = tr.eq(i).attr("id")
    				   stdArr.push(exacd);
    		     	});
    		   }
    		})

    		formData.append("stdArr",stdArr);
        	formData.append("index",index);
          	
        	formData.append("REPORT","REPORT"+a);
            formData.append("repor","report"+a);
               $.ajax({ 
                   url:"report.ai",
                   data:formData,
                   type:'POST',
                   processData:false,
                   contentType:false,
                   dataType:'json',
                   cache:false,
                   success:function(result){
                   	console.log(result)
                   	download(result)
                   }
             });	
       	 }
        function download(data){
      		 var rpath = '${pageContext.request.contextPath}/'+data[0].RPATH;
      		$("#"+data[0].report+"atag").attr("href", rpath);
      		$("#"+data[0].report+"btn").show();
      	}
        function reportcreate(mst){
         	console.log('mst : ' +mst);
        	var cnt = 0;
        	$('input:checkbox[name="grdgch"]').each(function() {
     		   var as = $(this).is(":checked")
     		   if(as){
     			   cnt += 1
     		   }
     		})
     		if(cnt == 0){
        		alert('시험을 체크해주세요')
     		}else if(mst == 'Y'){
				console.log('yes')
            	report(1);
				report(2);
            	report(3);
            	var bstorcd = "${sessionScope.MBR.BSTOR_CD}";
            	report4(4,bstorcd);
     		}else{
				var bstorcd = "${sessionScope.MBR.BSTOR_CD}";
				report4(4,bstorcd);
			}
        }
        function chkchg(){
        	$(".reportbtn").each(function() {
              	$(this).hide();
           	})
        }
      </script>
</head>
<body>
	<div id="content-page" class="content-page">
		<div class="container-fluid">
			<div class="row row-eq-height">
				<div class="col-md-12">
					<h2>리포트 생성은  1분정도 소요됩니다.</h2>
					<div class="row">
						<div style="width : 43%; height: 600px;">
							<div class="iq-card iq-border-radius-20" style="height: 97%; padding-top: 2%; margin-left: 1%; margin-bottom: 2%; padding-bottom: 2%;" align="center">
								<div style="padding-left: 3%; padding-top: 5px; padding-bottom : 5px; height: 7%;"  align="left">
									<h4><b>모의고사 과목 선택 </b></h4>
								</div>
								<div style="width: 95%; height: 500px; ">
									<div class="iq-card" style="height: 500px;">
										<div class="iq-card-body" style="height: 100%;" >
											<div class="iq-todo-page" style="height: 100%;">
												<form class="position-relative" name="searchform" id="searchform" action="OMR_REPORT.ai" method="post">
													<div class="form-group mb-0" style="height: 70px;">
														<table style="border: none; width: 100%; background-color: white;"  >
															<tr style="border: none; width: 100%; background-color: white;">
																<th style="border: none; width: 90%; background-color: white;">
																	<input type="hidden" name="pageNum" value="${param.pageNum }">
																	<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="searchcontent" value="${param.searchcontent }">
																</th>
																<th style="border: none; width: 10%; padding-top: 15px; background-color: white;">
                                   	 								<button onclick="document.getElementById('searchform').submit()" class="btn bg_01 btn btn-primary mb-3">검색</button>
                                   	 							</th>
                                   							</tr>
                                   						</table>
                                     				</div>
                           		    				<div style="width: 99%; height: 330px; margin-top: 10px; margin-bottom: 10px;" class="no-wrap" >
                           			  					<table class="table" id="seltable">
                               								<thead>
                               			  						<tr>
																	<th><input type="checkbox" id="bstorchkall" onclick="bchkall()"></th>
																	<th>시험명</th>
																</tr>
															</thead>
															<tbody><c:forEach items="${EXAMList}" var="ex">
																<tr id ="${ex.EXAM_CD}" class="sub">
																	<td><input type="checkbox" name="grdgch" onchange="chkchg();"></td>
																	<td>${ex.EXAM_NM}</td>
																</tr>
															</c:forEach></tbody>
														</table>
													</div>
													<div align="right" style="padding-left: 3%; height: 70px;">
														<div class="row">
															<div style="width: 35%; padding-top: 10px;">글 갯수
                                    						<script type="text/javascript">
                                    							$("#limit").val("${param.limit}").prop("selected", true);
                                    						</script>
                                    							<select id="limit" name = "limit" onchange="limitchg()">
																	<option value="5">5</option>
																	<option value="10">10</option>
                                   									<option value="15">15</option>
																</select>
															</div>
															<div style="width: 65%;" align="center">
																<c:if test="${param.pageNum > 1}">
																	<a href="javascript:listdo('${param.pageNum-1 }')" class="iq-icons-list">[이전]</a>
																</c:if>
																<c:if test="${param.pageNum <= 1}">[이전]</c:if>
																<c:forEach var="a" begin="${startpage }" end="${endpage }">
																	<c:if test="${a == param.pageNum }">[${a }]</c:if>
																	<c:if test="${a != param.pageNum }">
																		<a href="javascript:listdo('${a }')">[${a }]</a>
																	</c:if>
																</c:forEach>
																<c:if test="${param.pageNum < maxpage }">
																	<a class="iq-icons-list" href="javascript:listdo('${param.pageNum+1 }')">[다음]</a>
																</c:if>
																<c:if test="${param.pageNum >= maxpage }">[다음]</c:if>
															</div>
														</div>
													</div>
												</form>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div style="width : 53%; height: 100%; margin-left: 1%;" id="recogdiv">
							<div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 1%; padding-bottom: 2%"  align="center">
								<div style="padding-left: 3%; width: 96%; height: 5%; margin-bottom: 1%" align="left">
									<div class="row">
                        	  			<div style="width: 30%"><h4><b>리포트 선택</b></h4></div>
                        	  			<div style="width: 70%" align="right">
                        	  				<button class="btn bg_01 btn btn-primary mb-3" onclick="reportcreate('${sessionScope.MBR.MBR_MST_YN}')">리포트 생성</button>
                        	  			</div>
                        	  		</div>
                          	  	</div>
							  	<div style="width: 95%; height: 95%;">
                           			<div class="iq-card" style="width: 100%; height: 100%;" >
										<div class="iq-card-body" style="width: 100%; height: 100%;" >
											<div class="iq-todo-page" style="height: 100%;">
                                     <!-- <div class="form-group mb-0">
                                 		<input type="text" class="form-control todo-search" name="rsearchcontent" id="rsearchcontent" value="${param.rsearchcontent} " placeholder="검색">
                                 		  <a class="search-link" href="javascript:rsearch()"><i class="ri-search-line"></i></a>
                              		  </div>
                              		  <br> -->
												<div id="reportlist">
													<table class="table">
														<tr><th colspan="2">리포트명</th></tr>
														<c:if test="${sessionScope.MBR.MBR_MST_YN == 'Y'}">
                                    					<tr class="sub" id="report1" name="1">
                                    						<td>1.문항마킹정보</td>
                                    						<td><a id="report1atag" download><button class="btn bg_01 btn btn-primary mb-3 reportbtn" style="display: none;" id="report1btn" >다운로드</button> </a> </td>
                                    					</tr>
                                    					<tr class="sub" id="report2" name="2">
                                    						<td>2.전체성적</td>
                                    						<td><a id="report2atag" download><button class="btn bg_01 btn btn-primary mb-3 reportbtn" style="display: none;" id="report2btn">다운로드</button> </a> </td>
                                    					</tr>
                                    					<tr class="sub" id="report3" name="3">
                                    						<td>3.회차정보</td>
                                    						<td><a id="report3atag" download><button class="btn bg_01 btn btn-primary mb-3 reportbtn" style="display: none;"  id="report3btn">다운로드</button> </a> </td>
														</tr>
                                    					</c:if>
                                    					<c:if test="${sessionScope.MBR.MBR_MST_YN == 'N' ||sessionScope.MBR.MBR_NO == 3}">
                                    						<tr name="4" class="sub" id="report4">
                                    							<td>4.${sessionScope.MBR.BSTOR_NM}_전체성적표 다운로드</td>
                                    							<td><a id="report4atag" download> <button class="btn bg_01 btn btn-primary mb-3 reportbtn" style="display: none;" id="report4btn">다운로드</button></a></td>
                                    						</tr>
                                    					</c:if>
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
			</div>
		</div>
	</div>
</body>
</html>