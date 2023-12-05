<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OCR 시험관리</title>
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
   .no-wrapt {white-space:nowrap;border-collapse:collapse}
   
   th{
   	background-color: #eeecef;
   }
</style>
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".Edit").each(function() {
        $(this).show();
     })
	 if("${param.limit}" != ""){
     	$("#limit").val("${param.limit}").prop("selected", true);
     	console.log(${param.limit})
     }else{
     	$("#limit").val("5").prop("selected", true);
     }
	$(".exkinds").each(function() {
        $(this).show();
     })
 })
 var examcd = "";
 var rpageNum = 1;
 var rlimit = 5;
 var pageno =1; 
 
function sele(ex) {
	$(".exam").each(function() {
      $(this).removeClass("select");
   })
   $("#" + ex).addClass("select");
	examcd = ex;
	rpageNum = 1
	var form = $('#rsearchform');
    var formData = new FormData(form[0]);
    formData.append('examCd',examcd)
    formData.append('rpageNum',rpageNum)
    formData.append('rlimit',rlimit)
	
	
	$.ajax({
		url: "examrec.ai",
		type: "POST",
		data: formData,
		processData:false,
        contentType:false,
        cache:false,
		dataType : "json",
		//contentType : "application/json",
		success: function(data){
			 
			//console.log(data);
			drawtable(data)
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })
}
function edit(ex){
	var tr = $("#Edit" + ex).parent().parent().parent();
	var td = tr.children();
	$("#"+ex+"exkind").hide();
	$("#exkind"+ex).show();
	td.eq(2).attr("contenteditable", true);
	td.eq(3).attr("contenteditable", true);
		  
	
	$("#Edit" + ex).hide();
	 
	$("#Save" + ex).show();
}

function save(ex){
	var tr = $("#Save" + ex).parent().parent().parent();
	var td = tr.children();
		$("#"+ex+"exkind").show();
		$("#exkind"+ex).hide();
		td.eq(2).removeAttr("contenteditable", true);
		td.eq(3).removeAttr("contenteditable", true);
		var exam_kind = $("#exkind"+ex).val();
		console.log(exam_kind)
		var exam_nm = td.eq(2).text();
		var schyr = td.eq(3).text();
	$("#Save" + ex).hide();
	 
	$("#Edit" + ex).show();
	console.log('save js : ' + rlimit + rpageNum  )
	$.ajax({
		url: "update_ocr_exam.ai",
		type: "GET",
		data:{'exam_kind':exam_kind,
	    'exam_nm':exam_nm,
	    'exam_cd':ex,
	    'schyr':schyr},
		//processData:false,
        //contentType:false,
        //cache:false,
		dataType : "text",
		//contentType : "application/json",
		success: function(data){
			console.log(data);
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })
	     location.reload();
}
function listdo(page) {
	f = document.searchform;
	f.pageNum.value=page;
	pageno=page;
	f.submit();
}
function rlistdo(page) {	
	rpageNum=page;
	document.rsearchform.rpageNum.value=rpageNum;
	var form = $('#rsearchform');
    var formData = new FormData(form[0]);
    formData.append('examCd',examcd)
//	console.log('rp  : ' + rpageNum + ', rl : '+ rlimit )
	$.ajax({
		url: "examrec.ai",
		type: "POST",
		data: formData,
		processData:false,
        contentType:false,
        cache:false,
		dataType : "json",
		//contentType : "application/json",
		success: function(data){
			 
			//console.log(data);
			drawtable(data)
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })
}

	function copyexrow(data){
 		$("#examhead").append(function(){
 			var output = ""
 			output += '<tr id="'+data[0].exam_cd+'"><td><input type="checkbox"></td><td><select name="cexamkind"><option value="1">대학수학능력시험</option><option value="2">모의고사</option><option value="3">학력평가</option></select></td>';
 			output += '<td contenteditable="true">'+data[0].exam_nm+'</td><td contenteditable="true">'+data[0].schyr+'</td>';
 			output +='<td><span><button type="button" data-icon="S" class="icon" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="createexrow"></button></span><span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="delexrow"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span></td></tr>'; 
 		 	return output;
 		}) 		
 	}

	$(document).on("click","button[name=createexrow]",function(){
        
        var tr = $(this).parent().parent().parent();
        var td = tr.children();
        var examkind = td.eq(1).children().eq(0).val();
        var schyr = td.eq(3).text();
		var exam_nm = td.eq(2).text();
		var exam_cd = tr.attr('id');
	/*	console.log('examcd'+ exam_cd)
		console.log('schyr'+schyr)
		console.log('examkind'+examkind)
		console.log('exam_nm'+exam_nm)
		*/
		 var form = $('#blankform');
	 	var formData = new FormData(form[0]);
	 	formData.append('examcd', exam_cd)
	 	formData.append('schyr',schyr)
		formData.append('examkind',examkind)
		formData.append('exam_nm',exam_nm)
		if(exam_cd != 0){
			$.ajax({
    			url: "copyexocrrow.ai",
    			type: "POST",
    			data: formData,
    			processData:false,
    	        contentType:false,
    	        cache:false,
    			dataType : "json",
    			//contentType : "application/json",
    			success: function(data){
    				resultalert(data);
    		       	},
    		    error : function(e){
    		    	alert("서버오류 : " + e.status);
    		       	} 
    		    })	
		}else{
			$.ajax({
    			url: "createexocrrow.ai",
    			type: "POST",
    			data: formData,
    			processData:false,
    	        contentType:false,
    	        cache:false,
    			dataType : "json",
    			//contentType : "application/json",
    			success: function(data){
    				resultalert(data);
    		       	},
    		    error : function(e){
    		    	alert("서버오류 : " + e.status);
    		       	} 
    		     })
		}
	 });
	function resultalert(data){
 		alert(data[0].result);
 		location.reload();
 	}

function rsearch(){
	var form = $('#rsearchform');
    var formData = new FormData(form[0]);
    formData.append('examCd',examcd)
    formData.append('rpageNum',rpageNum)
	$.ajax({
		url: "examrec.ai",
		type: "POST",
		data: formData,
		processData:false,
        contentType:false,
        cache:false,
		dataType : "json",
		//contentType : "application/json",
		success: function(data){
			 
			//console.log(data);
			drawtable(data)
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })

}
/*
function examrec(rlimit){
	
	var form = $('#rsearchform');
    var formData = new FormData(form[0]);
    formData.append('examCd',examcd)
    formData.append('rpageNum',rpageNum)
    formData.append('rlimit',rlimit)
	
	$.ajax({
		url: "examrec.ai",
		type: "POST",
		data: formData,
		processData:false,
        contentType:false,
        cache:false,
		dataType : "json",
		//contentType : "application/json",
		success: function(data){
			 
			console.log(data);
			drawtable(data)
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })

}
*/



function ralchk(){
	var bchk = $('input:checkbox[id="ralLchk"]').is(":checked")
	if(bchk){
		console.log('already')
	}else{
		console.log('yet')
	}
	var form = $('#rsearchform');
	var formData = new FormData(form[0]);
	formData.append("examcd",examcd)
	 $.ajax({
        url:"ralchk.ai",
        data:formData,
        type:'POST',
        processData:false,
        contentType:false,
        dataType:'json',
        cache:false,
        success:function(data){
     	// console.log(data)   
        }
    });
}

function drawtable(data){
	var tble = ""
	tble = '<div class="no-wrap" style="height:750px;"><table class="table table-bordered table-responsive-md text-center" style="width: 100%">';
	tble += '<tr><th><input type="checkbox" id="rallchk" onclick="ralchk()"></th><th>지점명</th><th>진행상태</th></tr>';
	var len = data.length - 1;
	var rstartpage = data[len].rstartpage;
	var rendpage = data[len].rendpage;
	var rmaxpage = data[len].rmaxpage;
	rpageNum = data[len].rpageNum;
	rlimit = data[len].rlimit;
	console.log('dr : '+rlimit+","+rpageNum)
    for (var i = 0; i < data.length-1; i++) {
        //console.log("draw");
        tble += '<tr><td><input type="checkbox" id="check'+data[i].bstor_cd+'" onchange="chk('+data[i].bstor_cd+')"';
        if(data[i].useyn == 'Y'){
        	tble +='checked="checked" ';
        	//console.log(data[i].useyn)
        }
        tble +='></td><td>' + data[i].bstor_nm +'</td><td> ' + data[i].sttus + '</td></tr>';
    }
    tble += '</table></div>';
    tble += '<div align="right" style="height:50px;"><div class="row" style="margin-top : 10px;"><div style="width: 30%; padding-top: 15px;"> 한 화면에 볼 글 갯수';
    tble += '<span> <select  id="rlimit" name = "rlimit" onchange="rlimitchg()"><option value="5">5</option><option value="10">10</option><option value="20">20</option> <option value="50">50</option> </select> </span></div> <div style="width: 60%" align="center">';
    if(rpageNum > 1){
    	tble += '<a href = "javascript:rlistdo(\''+ rpageNum+'-1\')" class="iq-icons-list"> [이전] </a>';
    } 
    if(rpageNum <= 1){
    	tble +=' [이전] ';
    }
    for(var j = rstartpage ; j<= rendpage ; j++){
    	if(j == rpageNum){
    		tble +=' ['+j+'] ';	
    	}
    	if(j != rpageNum){
    		tble +='<a href="javascript:rlistdo(\''+j+'\')"> ['+j+'] </a>';	
    	}
    }
    if(rpageNum < rmaxpage){
    	tble +='<a class="iq-icons-list" href="javascript:rlistdo('+ rpageNum +'+1)"> [다음] </a>';
    }
    if(rpageNum >= rmaxpage){
    	tble +=' [다음] ';
    }
    tble += '</div></div></div>';  
    $("#status").html(tble);
    rlimitsetting();
}
function limitchg(){
	 var lt = $("#limit option:selected").val();
	//console.log(lt);
	f = document.searchform;
	f.pageNum.value=pageno;
	f.limit.value=lt;
	f.submit();
}
function rlimitchg(){
	 var lt = $("#rlimit option:selected").val();
	 rlimit = lt;
	var form = $('#rsearchform');
    var formData = new FormData(form[0]);
    formData.append('examCd',examcd)
  
	
	$.ajax({
		url: "examrec.ai",
		type: "POST",
		data: formData,
		processData:false,
        contentType:false,
        cache:false,
		dataType : "json",
		//contentType : "application/json",
		success: function(data){
			 
		//	console.log(data);
			drawtable(data)
			
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })
}
function rlimitsetting(){
	//console.log(rlimit)
	$("#rlimit").val(rlimit).prop("selected", true);
}
function deleteocrex(excd){
	var form = $('#recogform');
    var formData = new FormData(form[0]);
    formData.append("examcd",excd)
    console.log(excd)
    if (confirm("삭제 하시겠습니까?")) {
        $.ajax({
            url:"delocrex.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            	console.log(result);
            	alert(result[0].al)
            }
        });
        location.reload();
    }
}

function copyex(ex){
	 var form = $('#blankform');
	 var formData = new FormData(form[0]);
	    formData.append('examCd',ex)
		$.ajax({
			url: "cexamcopy.ai",
			type: "POST",
			data: formData,
			processData:false,
	        contentType:false,
	        cache:false,
			dataType : "json",
			//contentType : "application/json",
			success: function(data){
				copyexrow(data)
		       	},
		    error : function(e){
		    	alert("서버오류 : " + e.status);
		       	} 
		     })
	}

function newex(){
	 var form = $('#blankform');
	 var formData = new FormData(form[0]);
		$.ajax({
			url: "newocrmexam.ai",
			type: "POST",
			data: formData,
			processData:false,
	        contentType:false,
	        cache:false,
			dataType : "json",
			success: function(data){
				copyexrow(data)
		       	},
		    error : function(e){
		    	alert("서버오류 : " + e.status);
		       	} 
		     })
	}
	
function chk(s){
	var chkocr = $("#check"+ s).is(":checked")
	var form = $('#rsearchform');
	var formData = new FormData(form[0]);        	
	formData.append('examcd',examcd)
	formData.append('bstorcd',s)
	
	if(chk){
		formData.append('useyn',"Y")
	}else{
   		formData.append('useyn',"N")
	}	
	$.ajax({
    	url: "chkocrex.ai",
    	type: "POST",
    	data: formData,
    	processData:false,
    	contentType:false,
    	cache:false,
    	dataType : "json",
    	//contentType : "application/json",
    	success: function(data){
    		console.log(data)
    	},
    	error : function(e){
    		alert("서버오류 : " + e.status);
    	} 
   })
}

	
</script>
</head>
<body>
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                     <div class="row" style="height: 1000px;">
                     <div style="width: 48%; height: 100%;">
                      <div class="iq-card iq-border-radius-20" style="height: 100%;" align="center">
                    <div align="left" style="padding-left: 3%; width: 100%; height: 50px; padding-top : 10px;">
                        <div class="row" style="width: 98%; height: 100%"> 
                           <div style="width: 48%; padding-left: 2%">
                              <h4><b>시험 선택</b></h4>
                           </div>
                           <div style="width: 50%" align="right">
                           	 <button class="btn btn-primary mb-3" onclick="newex()" >시험생성</button>
                           	 <button class="btn btn-primary mb-3">삭제</button>
                           </div>
                        </div>
                     </div>
                     <form name="blankform"></form>
                     <div class="iq-card" style="width: 97%; height: 900px;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                           <form class="position-relative" name="searchform" id="searchform" action="OCR_EXAM_ADM.ai" method="post">
                              <div class="form-group mb-0" style="height: 50px; margin-bottom: 10px;">
                                 <table style="border: none; width: 100%;"  >
                                   		<tr style="border: none; width: 100%;">
                                   	 		<th style="border: none; width: 90%;">
                                   	 			<input type="hidden" name="pageNum" value="${param.pageNum }">
                                     			<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="searchcontent" value="${param.searchcontent }" style="margin-left : 10px;">
                                   	 		</th>
                                   	 		<th style="border: none; width: 10%; padding-top: 15px;"> 
                                   	 			<button onclick="document.getElementById('searchform').submit()" class="btn bg_01 btn btn-primary mb-3">검색</button>
                                   	 		</th> 
                                   		</tr>
                                   	</table>
                              </div>
                           	  <div style="height: 750px; margin-top: 10px;" class="no-wrap">
                           <table class="table table-bordered table-responsive-md text-center" style="width: 100%">
                             <thead id="examhead">
                               <tr>
                               	 <th><input type="checkbox"></th>
                                 <th>종류</th>
                                 <th>시험</th>
                                 <th>학년</th>
                                 <th>수정/삭제</th>
                               </tr>
                             </thead>
                             <tbody>
                               <c:forEach items="${EXAMList}" var="ex">
                               	<tr id ="${ex.EXAM_CD}" class="exam" onclick="sele('${ex.EXAM_CD}')">
                               		<td><input type="checkbox" id ="${ex.EXAM_KIND}" name="chkex"></td>
                               		<td><h5 id="${ex.EXAM_CD}exkind" style="display: : none" class="exkinds">${ex.EXAM_KIND}</h5>
                               			<select id="exkind${ex.EXAM_CD}" style="display: none;">
                               				<option value="1">대학수학능력시험</option>
                               				<option value="2">모의고사</option>
                               				<option value="3">학력평가</option>
                               			</select>
                               		</td>
                               		<td>${ex.EXAM_NM}</td>
                               		<td>${ex.SCHYR}</td>
                               		<td>  
                                    <span>
                                    	<button class="icon Edit" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" id="Edit${ex.EXAM_CD}" onclick="edit('${ex.EXAM_CD}')">
                                    		<i class="ri-edit-line"></i>
                                    	</button>
                                     	<button class="icon Save" data-icon="S" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" id="Save${ex.EXAM_CD}" onclick="save('${ex.EXAM_CD}')"></button>
                                    </span>
                                    <span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" onclick="copyex('${ex.EXAM_CD}')"><i class="las la-copy" data-toggle="tooltip" data-placement="top" title="복제"></i></button></span>
                                    <span class="table-remove">
                                    	<button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" onclick="deleteocrex(${ex.EXAM_CD})">
                                    		<i class="ri-delete-bin-line" aria-hidden="true"></i>
                                    	</button>
                                    </span>
                                    </td>
                                </tr>  
                               </c:forEach>
                             </tbody>
                           </table>
                           </div>
                           <div align="right" style="height: 50px;">
                              <div class="row">
                                 <div style="width: 30%; padding-top: 14px;">
                                    한 화면에 볼 글 갯수 
                                    <span>
                                    <select id="limit" name = "limit" onchange="limitchg()">
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="15">15</option>
                                    </select>
                                    </span>
                                 </div>
                                 <div style="width: 60%" align="center">
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
                           </div>
                            </form>
                        </div>
                     </div>
                  </div>
                  </div>
               </div>
               <div style="width: 48%; height: 100%; margin-left: 1%;">
                      <div class="iq-card iq-border-radius-20" style="height: 100%;" align="center">
                    <div align="left" style="padding-left: 3%; width: 100%; height: 50px; padding-top: 10px;">
                        <div class="row" style="width: 98%"> 
                           <div style="width: 48%; padding-left: 2%">
                              <h4><b>지점 선택</b></h4>
                           </div>
                           <div style="width: 50%" align="right">
                              
                           </div>
                        </div>
                       
                      </div>
                     <div class="iq-card" style="width: 97%; height: 900px;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 99%;">
                           <form class="position-relative" id="rsearchform" name="rsearchform">
                              <div class="form-group mb-0" style="height: 50px; margin-bottom: 10px;">
                              	 <input type="hidden" name="rpageNum" value="1">
                                 <input type="text" class="form-control todo-search" name="rsearchcontent" id="rsearchcontent" value="${param.rsearchcontent} " placeholder="검색">
                                 <a class="search-link" href="javascript:rsearch()"><i class="ri-search-line"></i></a>
                              </div>
                           <div id="status" style="height: 830px; margin-top: 10px;" >
                                    
                           </div>
                           </form>
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