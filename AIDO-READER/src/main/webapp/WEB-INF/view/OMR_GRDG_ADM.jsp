<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OMR 채점</title>
<style type="text/css">
	td,th,table,tr{border: 1px solid #dee2e6; text-align: center;}
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
  left: 96px;
  z-index: 1;
}
#gradtable thead th:nth-child(4){
  left: 140px;
  z-index: 1;
}  
    #exno{
    position: sticky;
    left: 0px;
    background-color: #eeecef !important;
   }
   #stnm{
    position: sticky;
    left: 96px;
    /*background-color: #eeecef !important;*/
   }
   #scr{
    position: sticky;
    left: 140px;
    background-color: #eeecef !important;
   }
</style>
<script type="text/javascript">
var examcd = "";
var lsncd = "";
var rpageNum = 1;
var rlimit = 5;
var pageno = 1;
var limit = 5;
var bstorcd="";
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
  		$("th[name=sscore]").attr("id","${searchscore}")
	  // report('1');
	   //report('2');
	   //report('3');
	 })
	 function chkall(){
			console.log('start')
			var chk = $('input:checkbox[id="chkall"]').is(":checked")
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
		 
   
	function disp_div(excd,lsn,bstor_cd) {
		$("#rsearchcontent").val('');
   		$(".sub").each(function() {
      		$(this).removeClass("select");
   		})
   		 $('#picbtn').attr("disabled","disabled");
   		$("#" + excd+"a"+lsn+"a"+bstor_cd).addClass("select");
   		examcd = ""+excd;
   		lsncd = ""+lsn 
   		bstorcd = ""+bstor_cd
   		var form = $('#recogform');
        var formData = new FormData(form[0]);
        formData.append("examcd",examcd)
        formData.append("lsncd",lsncd)
        formData.append("bstorcd",bstorcd)
        console.log(examcd+","+lsncd+","+bstorcd)
         $.ajax({
            url:"recoglist.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            	console.log(result);
           	 	drawresult(result);
            }
        });
   }	
   function drawresult(data){
	   var tble = ""
  		 tble = '<table id="gradtable">';
		 tble += '<thead><tr><th style="background-color: #f0f0f0;" ><input type="checkbox" id="chkall" onclick="chkall()"></th><th style="background-color: #f0f0f0; width:96px;" id="exnoth" >수험번호</th><th style="background-color: #f0f0f0;" id="stnmth">성명</th><th style="background-color: #f0f0f0;" id="scrth">점수</th>';
	   var qcnt = data.length-1;
	   for(var qi = 1; qi<data[0].length; qi++){
		   tble += '<th id="gradtableth" style="background-color: #f0f0f0; width:25px;" >'+qi+'</th>';  
	   }
		 tble +='</tr></thead><tbody id="gradbody">';
		 for(var sti = 0 ; sti < data.length; sti++){
			 tble +='<tr class="ex" id="'+data[sti][0].OMR_KEY+'k" onclick="selex(\''+data[sti][0].EXMN_NO+'\',\''+data[sti][0].OMR_IMG+'\',\''+data[sti][0].OMR_KEY+'\')"><td style="background-color: #eeecef;" id="'+data[sti][0].OMR_IMG+'" ><input type="checkbox" class="chk" name="chk" id="'+data[sti][0].EXMN_NO+'" ></td><td id="exno" style="color:black;" name="'+data[sti][0].OMR_KEY+'" contenteditable="true">'+data[sti][0].EXMN_NO+'</td><td id="stnm" contenteditable="true" ';
			 if(data[sti][0].ERR_YN == 'Y'){
				 tble +=' style="background-color : #ff3c3c; color:black; "';
			 }else{
				 tble +=' style="background-color : #eeecef; color:black;"';
			 }
			 
			 tble +='>'+data[sti][0].STDN_NM+'</td><td id="scr" style="color:black;">'+data[sti][0].TOT_SC+'</td>';
			 for(var i = 1; i<data[sti].length; i++){
				 tble += '<td contenteditable="true" style=" ';
				 if(data[sti][i].ERR_YN == 'Y'){
					 tble += ' background-color:#ff3c3c; color:#000000; ';	 
				 }
				 else if(data[sti][i].CRA_YN == 'N'){
					 tble += ' color:#FF0000; ';	 
				 }else{
					 tble += ' color:#000000; ';
				 }
				 tble +=' width:25px; " >'+data[sti][i].MARK_NO+'</td>';
			   }
			 tble += '</tr>';	
		 }
		 tble += '</tbody></table>';
		 $("#graddiv").html(tble);
   }
   
   function selex(ex,img,key) {
		$(".ex").each(function() {
	      $(this).removeClass("select");
	   })
	   $("#" +key+"k").addClass("select");
		
		console.log(key+"k")
		var form = $('#recogform');
       var formData = new FormData(form[0]);
       formData.append("omrimg",img)
       formData.append("exmnno",ex)
       formData.append("examcd",examcd)
       formData.append("lsncd",lsncd)
       formData.append("bstorcd",bstorcd)
       $.ajax({
			url: "omrs3download.ai",
			type: "POST",
			data:formData,
			dataType:'json',
			processData:false,
           contentType:false,
           cache:false,
			success: function(data){
				//console.log(data);
				omrpic(data)
		       	},
		    error : function(e){
		    	alert("서버오류 : " + e.status);
		       	} 
		     })
    }
   var pic = ""
   function omrpic(data){
	   var src = '${pageContext.request.contextPath}/'+data[0].jspimg
		$("#crop").attr("src", src);
	   pic = data[0].jspimg;
	   $('#picbtn').removeAttr('disabled');
   }
   function IMG_WID(){
	   var ope ="width="+screen.width/2+", height="+screen.height*3/4+", left=50, top=0";
       open("IMG_WIN.ai?data="+encodeURI(pic),"newimgwin",ope);
   }
   function listdo(page) {
	   var sbstor = $("th[name=sbstor]").attr("id")
  		var smock = $("th[name=smock]").attr("id")
  		var sgrade = $("th[name=sgrade]").attr("id")
  		var ssub = $("th[name=ssub]").attr("id")
  		var sscore = $("th[name=sscore]").attr("id")
  		f = document.searchform;
      	f.pageNum.value=page;
      	f.limit.value=limit;
      	f.searchbstor.value=sbstor;
      	f.searchmock.value=smock;
      	f.searchgrade.value=sgrade;
      	f.searchsub.value=ssub;
      	f.searchscore.value=sscore;
      	f.submit();
	}
   		
   	 function limitchg(){
       	 var lt = $("#limit option:selected").val();
       	console.log(lt);
       	limit = lt;
       	var sbstor = $("th[name=sbstor]").attr("id")
   		var smock = $("th[name=smock]").attr("id")
   		var sgrade = $("th[name=sgrade]").attr("id")
   		var ssub = $("th[name=ssub]").attr("id")
   		var sscore = $("th[name=sscore]").attr("id")
   		f = document.searchform;
       	f.pageNum.value=pageno;
       	f.limit.value=limit;
       	f.searchbstor.value=sbstor;
       	f.searchmock.value=smock;
       	f.searchgrade.value=sgrade;
       	f.searchsub.value=ssub;
       	f.searchscore.value=sscore;
       	f.submit();
       }
   	 function scoring(excd,lsn,bstor){
   		examcd = ""+excd;
   		lsncd = ""+lsn
   		bstorcd = ""+bstor
   		var rsearchcontent = $("")
   		var form = $('#recogform');
        var formData = new FormData(form[0]);
        formData.append("examcd",examcd)
        formData.append("lsncd",lsncd)
        formData.append("bstorcd",bstorcd)
        formData.append("rsearchcontent",rsearchcontent)
        console.log(examcd+","+lsncd+","+bstorcd)
        if (confirm("채점 하시겠습니까?")) {
         $.ajax({
            url:"scoring.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
           	 drawresult(result)
           	 alert('채점완료 되었습니다.');
           	 location.reload();
            }
        });
       }
   	 }
   	 function gradsave(){
   		var form = $('#recogform');
   	 	var formData = new FormData(form[0]);
   		var tr = $("#gradbody").children()
   		var index = 0
   		tr.each(function(i){
   			var td = tr.eq(i).children();
   			//var td = tr.eq(i).children();
   			var tdArr = new Array();
   			td.each(function(j){
   				if(j == 0){
   					tdArr.push(td.eq(j).attr('id'))
   	   			}
   				else{
   					tdArr.push(td.eq(j).text());		
   	   			}
   			});
   			console.log(tdArr);
   			var key = "tdArr" + index;
   	     	formData.append(key,tdArr);
   	     	index += 1 ;
     	});
   		
   		formData.append("index",index);
   		formData.append("lsncd",lsncd);
   		formData.append("examcd",examcd);
   		formData.append("bstorcd",bstorcd)
   		$.ajax({
            url:"gradsave.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            	alert('저장되었습니다.');
            	drawresult(result);
            }
        });
   	 }
   	 function transdata(){
   		console.log('as')
   		var form = $('#recogform');
        var formData = new FormData(form[0]);
        var index = 0
        $('input:checkbox[name="chk"]').each(function() {
		   var as = $(this).is(":checked")
		   if(as){
			   var tr =$(this).parent().parent()
		   		tr.each(function(i){
		   			var td = tr.eq(i).children();
		   			var tdArr = new Array();
		   			td.each(function(j){
		   				if(j == 0 ){
		   					tdArr.push(td.eq(j).attr("id"));
		   				}else{
		   					tdArr.push(td.eq(j).text());	
		   				}
		   			});
		   			console.log(tdArr);
		   			var key = "tdArr" + index;
		   			console.log(key);
		   	     	formData.append(key,tdArr);
		   	     	index += 1 ;
		     	});   
		   }
		})
		formData.append("index",index);
	   	formData.append("lsncd",lsncd);
	   	formData.append("examcd",examcd);
		formData.append("bstorcd",bstorcd);
	   	$.ajax({ 
            url:"transdata.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            	send(result.senddata);
            }
        }); 
   	 }
   	 function deletedata(){
   		var form = $('#recogform');
        var formData = new FormData(form[0]);
        var index = 0
        var stdArr = new Array();
        $('input:checkbox[name="chk"]').each(function() {
		   var as = $(this).is(":checked")
		   if(as){
			   var tr =$(this).parent().parent()
			   tr.each(function(i){
		   			var td = tr.eq(i).children();
		   			var omrkey = td.eq(1).attr("name")
		   			console.log(omrkey)
		   			stdArr.push(omrkey);
		     	});
		   }
		})
		formData.append("stdArr",stdArr);
		formData.append("index",index);
	   	formData.append("lsncd",lsncd);
	   	formData.append("examcd",examcd);
	   	formData.append("bstorcd",bstorcd);
	   	if (confirm("삭제 하시겠습니까?")) {
	   		$.ajax({ 
            	url:"deletedata.ai",
            	data:formData,
            	type:'POST',
            	processData:false,
            	contentType:false,
            	dataType:'json',
            	cache:false,
            	success:function(result){
            		alert('삭제되었습니다.');
            		drawresult(result);
            	}
        	});
	   	}
   	 }
   	 function send(result){
   		var op ="width=900, height=400, left=50, top=150";
   	   open("SEND_DATA.ai?data="+encodeURI(result),"",op);
   	 }
   	 function report(a){
   		var form = $('#recogform');
        var formData = new FormData(form[0]);
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
   		$("#"+data[0].report).attr("href", rpath)
   		setTimeout(function() {
   			$("#"+data[0].report).trigger("click");
   		}, 1000);
   	}
   	 
   	function win_open(view){
   		var op ="width=600, height=400, left=50, top=150";
	   	open(view+".ai","",op);
   	}
   	
   	function deletegrdgrec(){
   		var form = $('#searchform');
        var formData = new FormData(form[0]);
        var index = 0
        var stdArr = new Array();
        $('input:checkbox[name="grdgch"]').each(function() {
		   var as = $(this).is(":checked")
		   if(as){
			   var tr =$(this).parent().parent()
			   tr.each(function(i){
				   var td = tr.eq(i).children();
				   var lscd = td.eq(0).attr("id")
				   var bstcd= td.eq(1).attr("id")
				   var exacd= td.eq(2).attr("id")
				   unikey = exacd+"/"+bstcd+"/"+lscd
				   stdArr.push(unikey);
		     	});
		   }
		})
		
		formData.append("stdArr",stdArr);
		formData.append("index",index);
		if (confirm("삭제 하시겠습니까?")) {
	   		$.ajax({ 
            	url:"deletegrdgrec.ai",
      	    	data:formData,
            	type:'POST',
            	processData:false,
            	contentType:false,
            	dataType:'json',
            	cache:false,
            	success:function(result){
            		alert('삭제되었습니다.');
            	}
        	});
	   	}
        location.reload()
   	}
   	function searchfilter(){
   		var sbstor = $("th[name=sbstor]").attr("id")
   		var smock = $("th[name=smock]").attr("id")
   		var sgrade = $("th[name=sgrade]").attr("id")
   		var ssub = $("th[name=ssub]").attr("id")
   		var sscore = $("th[name=sscore]").attr("id")
   		f = document.searchform;
       	f.pageNum.value=pageno;
       	f.limit.value=limit;
       	f.searchbstor.value=sbstor;
       	f.searchmock.value=smock;
       	f.searchgrade.value=sgrade;
       	f.searchsub.value=ssub;
       	f.searchscore.value=sscore;
       	f.submit();
       	
   		//location.href ="OMR_GRDG.ai?limit="+limit+"&pageNum="++"&searchcontent="++"&="++"&="++"&="+
   	}
   	function ALLGRDG(){
   		var form = $('#searchform');
        var formData = new FormData(form[0]);
        var index = 0
        var stdArr = new Array();
        $('input:checkbox[name="grdgch"]').each(function() {
		   var as = $(this).is(":checked")
		   if(as){
			   var tr =$(this).parent().parent()
			   tr.each(function(i){
				   var td = tr.eq(i).children();
				   var lscd = td.eq(0).attr("id")
				   var bstcd= td.eq(1).attr("id")
				   var exacd= td.eq(2).attr("id")
				   unikey = exacd+"/"+bstcd+"/"+lscd
				   stdArr.push(unikey);
		     	});
		   }
		})
		
		formData.append("stdArr",stdArr);
		formData.append("index",index);
   		
	   	$.ajax({ 
            url:"scoregrdgrec.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            	alert('채점되었습니다.');
            }
        });
        location.reload()
   	}
    function rsearch(){
    	var form = $('#recogform');
    	var formData = new FormData(form[0]);
    	formData.append("examcd",examcd)
    	formData.append("lsncd",lsncd)
    	formData.append("bstorcd",bstorcd)
    	console.log(examcd+","+lsncd+","+bstorcd)
     	$.ajax({
     		url:"recoglist.ai",
        	data:formData,
        	type:'POST',
        	processData:false,
        	contentType:false,
        	dataType:'json',
        	cache:false,
        	success:function(result){
        		console.log(result);
       	 		drawresult(result);
        	}
    	});
    }
</script>
</head>
<body >
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                        <div style="width : 100%; height: 100%;">
                        <div class="row" style="width: 100%; height: 100%;">
                           <div class="iq-card iq-border-radius-20" style="height: 100%; width: 30%; margin-bottom: 10px;" align="center">
                              <div style="height: 40px; width: 100%;" align="left">
                                 <div class="row" style="width: 100%; margin-left: 10px;" >
                                 	<div style="width: 35%;"><h4><b>모의고사 과목 선택</b></h4></div>
                                 	<div style="width: 60%;" align="right" >
                                 	<!-- btn bg_01 btn btn-primary mb-3 -->
                                 		<!--<a style="width: 32%;" id="report1" download><button class="btn bg_01 btn btn-primary mb-3" style="width: 32%; font-size: x-small;">1.문항마킹정보.xlsx</button></a>
                                 		<a style="width: 32%;" id="report2" download><button class="btn bg_01 btn btn-primary mb-3" style="width: 32%; font-size: x-small;">2.전체성적.xlsx</button></a>
                                 		<a style="width: 32%;" id="report3" download><button class="btn bg_01 btn btn-primary mb-3" style="width: 32%; font-size: x-small;">3.회차정보.xlsx</button></a>-->
                                 		<button class="btn bg_01 btn btn-primary mb-3" onclick="searchfilter()">조회</button>
                                 		<button class="btn bg_01 btn btn-primary mb-3" onclick="deletegrdgrec()">삭제</button>
                                 		<button class="btn bg_01 btn btn-primary mb-3" disabled="disabled" onclick="ALLGRDG()">채점</button>
                                 	</div>
                                 </div> 
                              </div>
                              <div style="width: 97%; height: 93%; padding-top: 1%">
                                 <div class="iq-card" style="width: 100%; height: 100%;" >
                                    <div class="iq-card-body" style="width: 100%; height: 100%;" >
                                       <div class="iq-todo-page" style="height: 100%;">
                                          
                                          <form class="position-relative" name="searchform" id="searchform" action="OMR_GRDG.ai" method="post" style="height: 100%;">
                              		 
                              		 		<div class="form-group mb-0" style="padding-bottom: 5px; height: 50px;">
                                 	  			<input type="hidden" name="pageNum" value="${param.pageNum}">
                                 	  			<input type="hidden" name="searchbstor" value="${param.searchbstor}">
                                 	  			<input type="hidden" name="searchmock" value="${param.searchmock}">
                                 	  			<input type="hidden" name="searchgrade" value="${param.searchgrade}">
                                 	  			<input type="hidden" name="searchsub" value="${param.searchsub}">
                                 	  			<input type="hidden" name="searchscore" value="${param.searchscore}">
                                 	    		<input type="text" class="form-control todo-search" placeholder="검색"  name="searchcontent" value="${param.searchcontent }">
                                 				<a class="search-link" onclick="document.getElementById('searchform').submit()"><i class="ri-search-line"></i></a>
                              		  		</div>
                           					
                           					<div class="no-wrap" style="height: 80%;">
                           						<table style="width: 100%; text-align: center;">
                           							<tr>
                           								<th><input type="checkbox" id="bstorchkall" onclick="bchkall()"></th>
                           								<th id="(3,4,5,6)" onclick="win_open('FILT_BSTOR')" name="sbstor">지점</th>
                           								<th id="(1,8,11)" onclick="win_open('FILT_MOCK')" name="smock">모의고사</th>
                           								<th id="('고1','고2','고3')" onclick="win_open('FILT_GRDG')" name="sgrade">학년</th>
                           								<th>채점</th>
                           								<th id=""  onclick="win_open('FILT_SCORE')" name="sscore" >완료/전체</th>
                           								<th>오류수</th>
                           								<th id="('국어','영어','수학')" onclick="win_open('FILT_SUB')" name="ssub">교과</th>
                           								<th>과목</th>
                           								<th>평균점수</th>
                           								
                           							</tr>
                           							<c:forEach items="${EXAMList}" var="ex">
                               						  <tr id ="${ex.EXAM_CD}a${ex.LSN_CD}a${ex.BSTOR_CD}" class="sub" onclick="disp_div('${ex.EXAM_CD}','${ex.LSN_CD}','${ex.BSTOR_CD}')">
                               						  	<td id="${ex.LSN_CD}"><input type="checkbox" name="grdgch"></td>
                               							<td id="${ex.BSTOR_CD}">${ex.BSTOR_NM}</td>
                               							<td id="${ex.EXAM_CD}">${ex.EXAM_NM}</td>
                               							<td>${ex.SCHYR}</td>
                               							<c:if test="${ex.CRA_YN != ex.CRA_CNT}">
                               								<td><button type="button" onclick="scoring('${ex.EXAM_CD}','${ex.LSN_CD}','${ex.BSTOR_CD}')" style=" border: grey;" disabled="disabled">채점</button></td>
                               								<!-- class="btn bg_01 btn btn-primary mb-3"  -->
                               							</c:if>
                               							<c:if test="${ex.CRA_YN == ex.CRA_CNT}">
                               								<td><button type="button" onclick="scoring('${ex.EXAM_CD}','${ex.LSN_CD}','${ex.BSTOR_CD}')" disabled="disabled" style="background: red; border: red; color: white;">채점</button></td>
                               							</c:if>
                               							<td>${ex.CRA_YN}/${ex.CRA_CNT}</td>
                               							<td>${ex.ERR_CNT}</td>
                               							<td>${ex.SUB_NM}</td>
                               							<td>${ex.LSN_NM}</td>
                               							
                               							<td>${ex.AVG_SC}</td>
                                					  </tr>  
                               						</c:forEach>                           							
                                             	</table>
                                          	</div>
                                          	
                                       		<div align="right" style="padding-left: 3%; height: 55px; margin-top: 5px;" class="row">
                              				  
                                 				<div style="width: 30%; padding-top: 0px; height: 100%;">
                                    				글 갯수  
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
                              </div>
                           </div>
                           
                          <!-- <div class="row" style="height: 500px; width: 68%;" align="right"> -->
                          <div style="width: 70%; height: 99%;">
                              <div style="width: 100%; margin-left: 1%; height: 380px; margin-bottom: 10px;">
                              	<div style="height: 100%;">
                              		<div class="iq-card iq-border-radius-20" align="center" style="height: 100%;">
                                       <div style="height: 45px; padding-top: 1%; padding-left: 2%;" align="left">
                                          <div class="row" style="width: 95%" align="center">
                                             <div style="width: 50%; margin-left: 5%;" align="left"><h4><b>인식 정보 확인</b></h4></div>
                                             <div style="width: 45%" align="right">
                                                <span><button type="button" class="btn btn-primary mb-3" onclick="transdata()" >전송</button></span>
                                                <span><button type="button" class="btn btn-primary mb-3" onclick="deletedata()">삭제</button></span>
                                                <span><button type="button" class="btn btn-primary mb-3" onclick="gradsave()">저장</button></span>
                                             </div>
                                          </div>
                                 		</div>
                                       	<div style="height: 335px; padding-top: 1%" >
                                    	<div class="iq-card" style="width: 98%; height: 100%; margin-bottom: 30px;">
                                       	  <div class="iq-card-body" style="width: 100%; height: 100%;" >
                                            <div class="iq-todo-page" style="height: 100%;">
                                              <form name="recogform" id="recogform" class="position-relative">
                                                <div class="form-group mb-0" style="padding-bottom: 5px; height: 50px;">
                                 				 <input type="text" class="form-control todo-search" name="rsearchcontent" id="rsearchcontent" value="${param.rsearchcontent} " placeholder="검색">
                                 		  		 <a class="search-link" href="javascript:rsearch()"><i class="ri-search-line"></i></a>
                              		  		    </div>
                                                <div class="no-wrap" id="graddiv" style="height: 250px;" align="left"></div>
                                               </form>
                                             </div>
                                            </div>
                                          </div>
                                          
                                          <!--<div class="iq-card" style="width: 100%; height: 100%;" >
                                 			<div class="iq-card-body" style="width: 100%; height: 100%;" >
                                   				<div class="iq-todo-page" style="height: 100%;">
                                    				<form class="position-relative" id="rsearchform" name="rsearchform">
                              		  					<div class="form-group mb-0">
                                 							<input type="text" class="form-control todo-search" name="rsearchcontent" id="rsearchcontent" value="${param.rsearchcontent} " placeholder="검색">
                                 		  					<a class="search-link" href="javascript:rsearch()"><i class="ri-search-line"></i></a>
                              		  					</div>
                              		   					<br>
                           								<div id="status"></div>
                           							</form>
                        		   				</div>
                     			  			</div>
                  			     		</div>-->
                  			     		
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
                                    	<div class="iq-card" style="width: 98%; height: 100%; margin-bottom: 10px;" >
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
                           <!-- </div> -->
                        
                        
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>         
</body>
</html>