<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>성적 일람표 관리</title>
<style>
td,th,table,tr{border: 1px solid #dee2e6; text-align: center;}
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
   </style>

<script src="js/jquery.min.js"></script>
   
      <script type="text/javascript">
      var pageno =1; 
      var examCd = 5;
      var rpageNum = 1;
      var rlimit = 5;
      var limit = 5;
      var BSTOR_CD = 0;
      var EXMN_NO = 0;
      var stdno="";var name= "";var schol="" ;
      var mathlsn="";var tam1lsn="" ;var tam2lsn="" ;var fllsn="" ;
      var korstd="" ;var mathstd="" ;var tam1std="" ;var tam2std="" ;var flstd="" ;
      var korrank="";var mathrank ="";var tam1rank ="";var tam2rank ="";var flrank ="";
      var histgrade="";var korgrade="";var mathgrade="";var enggrade="";var tam1grade="";var tam2grade="";var flgrade="";
      $(document).ready(function() {
    	  if("${param.limit}" != ""){
    		  $("#limit").val("${param.limit}").prop("selected", true);
    		  console.log(${param.limit})
    		  } else {
    			  $("#limit").val("5").prop("selected", true);
    			  }
    	  if("${param.searchtype}" != ""){
      	  	$("#searchtype").val("${param.searchtype}").prop("selected", true);
      	  	examCd = "${param.searchtype}";
      	  } else{
      		  $("#searchtype").val(examCd).prop("selected", true);
      	  }
      })
      
      function sele(sn) {
        		$(".sn").each(function() {
        	      $(this).removeClass("select");
        	   })
        	   $("#" + sn).addClass("select");        		
        		var form = $('#searchform');
                var formData = new FormData(form[0]);
                formData.append("BSTOR_NM",sn)
                 formData.append("rpageNum",rpageNum)
        	    formData.append('rlimit',rlimit)
        	    formData.append('examCd',examCd)
        		//var 
        		
        		$.ajax({
        			url: "IR_OCR_LIST.ai",
        			type: "POST",
        			data:formData,
        			dataType:'json',
        			processData:false,
                    contentType:false, 
                    cache:false,
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
         function drawtable(data){
        	 var tble = "";
        		var len = data.length - 1;
        		var rstartpage = data[len].rstartpage;
        		var rendpage = data[len].rendpage;
        		var rmaxpage = data[len].rmaxpage;
        		BSTOR_CD = data[0].BSTOR_CD;
        		console.log(BSTOR_CD)
        			tble = '<div class="no-wrap" style="height : 750px;"><table class="table table-bordered table-responsive-md text-center" style="width: 100%">';
        			tble += '<tr><th><input type="checkbox"></th><th>수험번호</th><th>이름</th><th>상태</th></tr>';
        		    for (var i = 0; i < data.length-1; i++) {
        		        console.log("draw");
        		        tble += '<tr class="ex" id="'+data[i].std_no+'" onclick="selex(\''+data[i].std_no+'\')"><td><input type="checkbox"></td><td>' + data[i].std_no
        		                + '</td><td> ' + data[i].name + '</td><td> ' ;
        		        if(data[i].sttus == "WARNING"){
        		        	tble += '<button class="btn btn-danger rounded-pill mb-3" type="button">WARNING</button>';
        				}else {
        					tble += '<button class="btn btn-success rounded-pill mb-3" type="button">SUCCESS</button>';
        				}
        		      tble+='</td></tr>';
        		    }
        		    tble += '</table></div>';
           		 
        		    tble += '<div align="right" style="height : 50px;"><div class="row"><div style="width: 50%"> 한 화면에 볼 글 갯수';
         		   tble += '<span><select id="rlimit" name = "rlimit" onchange="rlimitchg()"><option value="5">5</option><option value="10">10</option><option value="15">15</option></select></span></div> <div style="width: 50%" align="center">';
        		    if(rpageNum > 1){
        		    	tble += '<a href = "javascript:rlistdo('+ rpageNum +'-1)" class="iq-icons-list">[이전]</a>';
        		    } 
        		    if(rpageNum <= 1){
        		    	tble +='[이전]';
        		    }
        		    console.log(rstartpage)
        		    console.log(rendpage)
        		    for(var j = rstartpage ; j<= rendpage ; j++){
        		    	if(j == rpageNum){
        		    		tble +='['+j+']';	
        		    	}
        		    	if(j != rpageNum){
        		    		tble +='<a href="javascript:rlistdo(\''+j+'\')">['+j+']</a>';	
        		    	}
        		    }
        		    if(rpageNum < rmaxpage){
        		    	tble +='<a class="iq-icons-list" href="javascript:rlistdo('+ rpageNum +'+1)">[다음]</a>';
        		    }
        		    if(rpageNum >= rmaxpage){
        		    	tble +='[다음]';
        		    }
        		    tble += '</div></div></div>'; 
        		  $("#status").html(tble);
         }
        
         function selex(ex) {
     		$(".ex").each(function() {
     	      $(this).removeClass("select");
     	   })
     	   $("#" + ex).addClass("select");
     		
     		var form = $('#searchform');
            var formData = new FormData(form[0]);
            EXMN_NO = ex;
            formData.append("EXAMNO",ex)
            formData.append("examCd",examCd)
            formData.append("BSTOR_CD",BSTOR_CD)
            console.log(BSTOR_CD)
            $.ajax({
    			url: "s3download.ai",
    			type: "POST",
    			data:formData,
    			dataType:'json',
    			processData:false,
                contentType:false,
                cache:false,
    			success: function(data){
    				console.log(data);
    				drawocr(data)
    				//
    		       	},
    		    error : function(e){
    		    	alert("서버오류 : " + e.status);
    		       	} 
    		     })
         }
         function drawocr(data){
        	 stdno="";  name= "";  schol="" ;
             mathlsn="";  tam1lsn="" ;  tam2lsn="" ;  fllsn="" ;
             korstd="" ;  mathstd="" ;  tam1std="" ;  tam2std="" ;  flstd="" ;
             korrank="";  mathrank ="";  tam1rank ="";  tam2rank ="";  flrank ="";
             histgrade="";  korgrade="";  mathgrade="";  enggrade="";  tam1grade="";  tam2grade="";  flgrade="";
             var tble = ""
        		 tble = '<table class="table" style="width: 80%; height: 80%;" >';
    			tble += '<tr><th style="background-color: #f0f0f0;">수험번호</th><th colspan="2" style="background-color: #f0f0f0;">성명</th><th style="background-color: #f0f0f0;">생년월일</th><th style="background-color: #f0f0f0;">성별</th><th colspan="3" style="background-color: #f0f0f0;">출신고교(반 또는 졸업년도)</th></tr>';
    		    tble += '<tr><td contenteditable="true" id="stdno">'+data[0].std_no+'</td><td colspan="2" contenteditable="true" id="std_nm">'+data[0].std_nm+'</td><td contenteditable="true">';
    		    //+data[0].extra+'
    		    stdno=data[0].std_no;name=data[0].std_nm;schol=data[0].schol;
    		    
    		    tble +='</td><td contenteditable="true">';
    		    //data[0].extra+
    		    tble += '</td><td contenteditable="true" colspan="3" id="schol">'+data[0].schol+'</td></tr>';
    		    tble += '<tr><td style="background-color: #f0f0f0;" rowspan="2">구분</td><td style="background-color: #f0f0f0;" rowspan="2">한국사</td><td style="background-color: #f0f0f0;" rowspan="2">국어</td><td style="background-color: #f0f0f0;">수학 영역</td><td style="background-color: #f0f0f0;"  rowspan="2">영어영역</td><td colspan="2" style="background-color: #f0f0f0;">';
    		    for(var a = 0; a<data.length; a++){
    		    	if(data[a].lsn_cd >= 5){
    		    		if(data[a].lsn_cd <= 14){
    		    			tble +='사회탐구 영역</td><td style="background-color: #f0f0f0;">제2외국어/한문 영역</td></tr>';
    		    			break;	
    		    		}
    		    	}
    		    	if(data[a].lsn_cd >= 15){
    		    		if(data[a].lsn_cd <= 23){
    		    			tble +='과학탐구 영역</td><td style="background-color: #f0f0f0;">제2외국어/한문 영역</td></tr>';
    		    			break;	
    		    		}
    		    	}
    		    }
    		    var kor = 0;
    		    var math = 0;
    		    var eng = 0;
    		    var tam = [];        		    
    		    var fl = 0;
    		    var hist = 0;
    		  	var rkor = 0;
  		    var rmath = 0;
  		    var reng = 0;
  		    var rfl = 0;
  		    var rhist = 0;
    		    var tamlsn =[];
    			tble +='<tr>';
    			for(var a = 0; a<data.length; a++){
    				
    				if(data[a].lsn_cd == 1 ){
    					kor = a;
    					rkor ++;
    				}
    				if(data[a].lsn_cd == 4 ){
    					eng = a;
    					reng ++;
    				}
    				if(data[a].lsn_cd == 34 ){
    					hist = a;
    					rhist ++;
    				}
    				if(data[a].lsn_cd == 2 ){
    					tble +='<td style="background-color: #f0f0f0;">나 형</td>';
    					math = a;
    					mathlsn="2";
    					rmath ++;
    				}else if(data[a].lsn_cd == 3){
    					tble +='<td style="background-color: #f0f0f0;">가 형</td>';
    					math = a;
    					mathlsn="3";
    					rmath ++;
    				}
    		    	if(data[a].lsn_cd >= 5 && data[a].lsn_cd <= 23){
    		    		tble += '<td style="background-color: #f0f0f0;">'+data[a].lsn_nm;+'</td>';
    		    		tam.push(a)
    		    		tamlsn.push(data[a].lsn_cd)
    		    	}
    		    	if(data[a].lsn_cd == 6 || data[a].lsn_cd == 7 || data[a].lsn_cd == 8 || data[a].lsn_cd == 9 || (data[a].lsn_cd >=28 && data[a].lsn_cd <=33)){
    					tble += data[a].lsn_nm;
    					fl = a;
    					fllsn=data[a].lsn_cd;
    					rfl ++;
    				}
    			}
    			tam1lsn=tamlsn[0];
    			tam2lsn=tamlsn[1];
    			tble += '<td style="background-color: #f0f0f0;">';
    			for(var a = 0; a<data.length; a++){	
    				
    		    	}
    			tble += '</td></tr><tr><td style="background-color: #f0f0f0;">표준점수</td><td></td><td id="korstd" ';
    			if(rkor > 0){
    				if(data[kor].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
    				tble += ' contenteditable="true">'+data[kor].std_sc+'</td><td id="mathstd" ';
    			}else{
    				tble += ' contenteditable="true"></td><td id="mathstd" ';
    			}
    			
    			if(rmath > 0){
    				if(data[math].sttus == "WARNING"){
    					tble += 'style = "background-color: #fcf2f1;"';
    				}
    				tble +=' contenteditable="true">'+data[math].std_sc+'</td><td></td><td id="tam1std" ';
    			}else{
    				tble +=' contenteditable="true"></td><td></td><td id="tam1std" ';
    			}
    			if(tamlsn.length > 0){
    				if(tamlsn.length == 1){
    					if(data[tam[0]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    	      			tble +=' contenteditable="true">'+data[tam[0]].std_sc+'</td><td id="tam2std" contenteditable="true"></td><td id="flstd"';
    				}else {
    					if(data[tam[0]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    					tble += 'contenteditable="true">'+data[tam[0]].std_sc+'</td><td id="tam2std"';
    					if(data[tam[1]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    	      			tble +=' contenteditable="true">'+data[tam[1]].std_sc+'</td><td id="flstd" ';
    				}
    			}else{
    				tble +=' contenteditable="true"></td><td id="tam2std"></td><td id="flstd" ';
    			}
    			if(rfl > 0){
    				if(data[fl].sttus == "WARNING"){
    					tble += 'style = "background-color: #fcf2f1;"';
    				}
    				tble +=' contenteditable="true">'+data[fl].std_sc+'</td></tr> ';
    			}else{
    				tble +=' contenteditable="true"></td></tr> ';
    			}
    			tble += '<tr><td style="background-color: #f0f0f0;">백분위</td><td></td><td id="korrank" ';
    			
    			if(rkor > 0){
    				if(data[kor].sttus == "WARNING"){
	      				tble += 'style = "background-color: #fcf2f1;"';
  	  			}
    				tble +=' contenteditable="true">'+data[kor].prcn_rank+'</td><td id="mathrank" ';
    			}else{
    				tble +=' contenteditable="true"></td><td id="mathrank" ';
    			}
    			
    			if(rmath > 0){
    				if(data[math].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
    				tble +=' contenteditable="true">'+data[math].prcn_rank+'</td><td></td><td id="tam1rank" ';
    			}else{
    				tble +=' contenteditable="true"></td><td></td><td id="tam1rank" ';
    			}
    			if(tamlsn.length > 0){
    				if(tamlsn.length == 1){
    					if(data[tam[0]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    	      			tble +=' contenteditable="true">'+data[tam[0]].prcn_rank+'</td><td id="tam2rank" contenteditable="true"></td><td id="flrank"';
    				}else {
    					if(data[tam[0]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    					tble += 'contenteditable="true">'+data[tam[0]].prcn_rank+'</td><td id="tam2rank"';
    					if(data[tam[1]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    	      			tble +=' contenteditable="true">'+data[tam[1]].prcn_rank+'</td><td id="flrank" ';
    				}
    			}else{
    				tble +=' contenteditable="true"></td><td id="tam2rank"></td><td id="flrank" ';
    			}
   			if(rfl > 0){
    				if(data[fl].sttus == "WARNING"){
    					tble += 'style = "background-color: #fcf2f1;"';
    				}
    				tble +=' contenteditable="true">'+data[fl].prcn_rank+'</td></tr> ';
    			}else{
    				tble +=' contenteditable="true"></td></tr> ';
    			}
    			tble += '<tr><td style="background-color: #f0f0f0;">등급</td><td id="histgrade" ';
    		
    			if(rhist > 0){
    				if(data[hist].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
    				tble +=' contenteditable="true">'+data[hist].grade+'</td><td id="korgrade" ';
    			}else{
    				tble +=' contenteditable="true"></td><td id="korgrade" ';
    			}
    			
    			if(rkor > 0){
    				if(data[kor].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
        			tble +=' contenteditable="true">'+data[kor].grade+'</td><td id="mathgrade" ';
    			}else{
    				tble +=' contenteditable="true"></td><td id="mathgrade" ';
    			}
    			
    			if(rmath > 0){
    				if(data[math].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
        			tble +=' contenteditable="true">'+data[math].grade+'</td><td id="enggrade" ';
    			}else{
    				tble +=' contenteditable="true"></td><td id="mathgrade" ';
    			}
    			if(reng > 0){
    				if(data[eng].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
        			tble +=' contenteditable="true">'+data[eng].grade+'</td><td id="tam1grade" ';
    			}else{
    				tble +=' contenteditable="true"></td><td id="tam1grade" ';
    			}
    			if(tamlsn.length > 0){
    				if(tamlsn.length == 1){
    					if(data[tam[0]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    	      			tble +=' contenteditable="true">'+data[tam[0]].grade+'</td><td id="tam2grade" contenteditable="true"></td><td id="flgrade"';
    				}else {
    					if(data[tam[0]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    					tble += 'contenteditable="true">'+data[tam[0]].grade+'</td><td id="tam2grade"';
    					if(data[tam[1]].sttus == "WARNING"){
    	      				tble += 'style = "background-color: #fcf2f1;"';
    	      			}
    	      			tble +=' contenteditable="true">'+data[tam[1]].grade+'</td><td id="flgrade" ';
    				}
    			}else{
    				tble +=' contenteditable="true"></td><td id="tam2grade"></td><td id="flgrade" ';
    			}if(rfl > 0){
    				if(data[fl].sttus == "WARNING"){
        				tble += 'style = "background-color: #fcf2f1;"';
        			}
        			tble +=' contenteditable="true">'+data[fl].grade+'</td></tr>';
    			}else{
    				tble +=' contenteditable="true"></td></tr>';
    			}
        		    tble += '</table>';
        		    $("#ocrtable").html(tble);
        		    var src = '${pageContext.request.contextPath}/'+data[0].jspimg
        		    $("#crop").attr("src", src);
        }
         function rlistdo(page) {	
        		rpageNum=page;
        		console.log(rpageNum)
        		var form = $('#rsearchform');
        	    var formData = new FormData(form[0]);
        	    formData.append('examCd',examCd)
        	    formData.append('rpageNum',rpageNum)
        	    formData.append('rlimit',rlimit)
        		formData.append('BSTOR_CD',BSTOR_CD)
        		$.ajax({
        			url: "IR_OCR_LIST.ai",
        			type: "POST",
        			data:formData,
        			dataType:'json',
        			processData:false,
                    contentType:false, 
                    cache:false,
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
         function rlimitchg() {
        	 var lt = $("#rlimit option:selected").val();
        	 rf = document.rsearchform;
        	 rlimit = lt;
             rpageNum = 1;
        	 //rf.rlimit.value=lt;
     		var form = $('#rsearchform');
     	    var formData = new FormData(form[0]);
     	    formData.append('examCd',examCd)
     	    formData.append('rpageNum',rpageNum)
     	    formData.append('rlimit',rlimit)
     		formData.append('BSTOR_CD',BSTOR_CD)
     		$.ajax({
     			url: "IR_OCR_LIST.ai",
     			type: "POST",
     			data:formData,
     			dataType:'json',
     			processData:false,
                 contentType:false, 
                 cache:false,
     			success: function(data){
     				console.log(data);
     				drawtable(data)
     				rlimitsetting();
     				//$("#status").html(data);
     		       	},
     		    error : function(e){
     		    	alert("서버오류 : " + e.status);
     		       	} 
     		     })
     	}
 function rlimitsetting(){
	console.log(rlimit)
	$("#rlimit").val(rlimit).prop("selected", true);
}
        	function rsearch(){
        		var form = $('#rsearchform');
        	    var formData = new FormData(form[0]);
        	    rlimit=$('#rlimit').val() 
        	    formData.append('examCd',examCd)
        	    formData.append('rpageNum',rpageNum)
        	    formData.append('BSTOR_CD',BSTOR_CD)
        		
        		$.ajax({
        			url: "IR_OCR_LIST.ai",
        			type: "POST",
        			data:formData,
        			dataType:'json',
        			processData:false,
                    contentType:false, 
                    cache:false,
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
        	
/*
        	function examrec(rlimit){
        		
        		var form = $('#rsearchform');
        	    var formData = new FormData(form[0]);
        	    formData.append('examCd',examCd)
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

        	}*/
        	
            function listdo(page) {
        		pageno = page
         		f = document.searchform;
         		f.pageNum.value=page;
         		f.submit();
         	}
            function ocredit(){
            	if (confirm("저장 하시겠습니까?")) {
            	name=$("#std_nm").text();schol=$("#schol").text();
            	korstd=$("#korstd").text();mathstd=$("#mathstd").text();tam1std=$("#tam1std").text();tam2std=$("#tam2std").text();flstd=$("#flstd").text();
            	korrank=$("#korrank").text();mathrank=$("#mathrank").text();tam1rank=$("#tam1rank").text();tam2rank=$("#tam2rank").text();flrank=$("#flrank").text();
            	histgrade=$("#histgrade").text();korgrade=$("#korgrade").text();mathgrade=$("#mathgrade").text();enggrade=$("#enggrade").text();tam1grade=$("#tam1grade").text();tam2grade=$("#tam2grade").text();flgrade=$("#flgrade").text();
            	var form = $('#searchform');
                var formData = new FormData(form[0]);
                formData.append("EXAMNO",EXMN_NO);formData.append("examCd",examCd);formData.append("BSTOR_CD",BSTOR_CD);
                formData.append("std_nm",name);formData.append("schol",schol);
                formData.append("mathlsn",mathlsn);formData.append("tam1lsn",tam1lsn);formData.append("tam2lsn",tam2lsn);formData.append("fllsn",fllsn);
                formData.append("korstd",korstd);formData.append("mathstd",mathstd);
                formData.append("mathstd",mathstd);formData.append("tam1std",tam1std);formData.append("tam2std",tam2std);formData.append("flstd",flstd);
                formData.append("korrank",korrank);formData.append("mathrank",mathrank);formData.append("tam1rank",tam1rank);formData.append("tam2rank",tam2rank);formData.append("flrank",flrank);
                formData.append("histgrade",histgrade);formData.append("korgrade",korgrade);formData.append("mathgrade",mathgrade);formData.append("enggrade",enggrade);formData.append("tam1grade",tam1grade);formData.append("tam2grade",tam2grade);formData.append("flgrade",flgrade);
                
                $.ajax({
        			url: "s3download2.ai",
        			type: "POST",
        			data:formData,
        			dataType:'json',
        			processData:false,
                    contentType:false,
                    cache:false,
        			success: function(data){
        				console.log(data);
        				drawocr(data)
        				//
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
        		     }
             
            }
            function limitchg(){
           	 var lt = $("#limit option:selected").val();
           	console.log(lt);
           	limit = lt
           	f = document.searchform;
           	f.pageNum.value=pageno;
           	f.limit.value=limit;
           	f.submit();
           }
            function exchg(){
              	 var ex = $("#searchtype option:selected").val();
              	console.log(ex);
              	examCd = ex;
              	f = document.searchform;
              	f.pageNum.value=pageno;
              	f.limit.value=limit;
              	f.submit();
              }
            function send(){
            	
            }
      </script>
</head>
<body>
<div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                   <div class="row" style="height: 1000px;">
                     <div style="width: 28%; height: 100%;">
                      <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 20px;" align="center">
                    <div align="left" style="padding-left: 10px; width: 100%; height: 30px; margin-bottom: 10px;">
                        <h4><b>지점별 성적표 선택</b></h4>
                      </div>
                    <div class="iq-card" style="width: 97%; height: 900px; margin-bottom: 10px;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                           
                           <form class="position-relative" name="searchform" id="searchform" action="BSTOR_RC_ADM_IR.ai">
                               <div class="form-group mb-0" style="height: 50px;">
                                 <table style="border: none; width: 100%;"  >
                                   		<tr style="border: none; width: 100%;">
                                   	 		<th style="border: none; width: 80%;" >
                                   	 			<input type="hidden" name="pageNum" value="${param.pageNum }">
                                     			<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="searchcontent" value="${param.searchcontent }" style=" margin-right: 20px;">
                                   	 		</th>
                                   	 		<th style="border: none; width: 20%; padding-top: 15px;">
                                   	 			<button onclick="document.getElementById('searchform').submit()" class="btn bg_01 btn btn-primary mb-3">검색</button>
                                   	 		</th> 
                                   		</tr>
                                   	</table>
                              </div>
                              <div style="padding-top: 10px; padding-bottom: 10px; height: 50px;" align="left">
                                 <h5>시험 선택<span> 
                                 <script type="text/javascript">
                                 	$("#searchtype").val("${param.searchtype}").prop("selected", true);
                                    </script>
                                 <select name="searchtype" id="searchtype" onchange="exchg()">
                                    <c:forEach items="${EXAMList}" var="sn">
                                    	<option value="${sn.EXAM_CD}">${sn.EXAM_NM}</option>
                                    </c:forEach>
                                  </select>
                                  
						</span></h5>
                              </div>
                          
                           <div class="no-wrap" style="height: 700px;">
                           <table class="table table-bordered table-responsive-md text-center" style="width: 100%">
                             <thead>
                               <tr >
                                 <th>지점명</th>
                                 <th>오류학생수</th>
                               </tr>
                             </thead>
                             <tbody>
                            <c:forEach items="${BSTORList}" var="bs"> 
                            	<tr class="sn" id="${bs.BSTOR_NM}" onclick="sele('${bs.BSTOR_NM}')">
                                 <td>${bs.BSTOR_NM}</td>
                                 <td>${bs.WARN_CNT}</td>
                               </tr>
                            </c:forEach>   
                            </tbody>
                           </table>
                           </div>
                           <div align="right" style="padding-left: 3%">
                              <div class="row">
                                 <div style="width: 50%">
                                    한 화면에 볼 글 갯수 
                                    <span>
                                    <select id="limit" name = "limit" onchange="limitchg()">
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="15">15</option>
                                    </select>
                                    </span>
                                    <span>
                                 </div>
                                 <div style="width: 50%" align="center">
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

              <div style="width: 28%; height: 100%; margin-left: 1%">
                   <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 10px; " align="center">
                     <div align="left" style="padding-left: 10px; width: 100%; height: 30px; margin-bottom: 10px;">
                         <div class="row">
                         	<div style="width: 50%; padding-left: 20px;">
                         		<h4><b>내역 확인</b></h4>
                         	</div>
                         	<div align="right" style="width: 42%;">
                         		<button class="btn btn-primary mb-3" onclick="send()">전송</button>
                         	</div>
                         </div>
                     </div>
                     <div class="iq-card" style="width: 97%; height: 900px; margin-bottom: 10px;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                           <form class="position-relative" id="rsearchform" name="rsearchform">
                              <div class="form-group mb-0" style="height: 50px;">
                                 <table style="border: none; width: 100%;"  >
                                   		<tr style="border: none; width: 100%;">
                                   	 		<th style="border: none; width: 80%;">
                                   	 			<input type="hidden" name="pageNum" value="${param.rpageNum }">
                                     			<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  id="rsearchcontent" name="rsearchcontent" value="${param.rsearchcontent }" style="margin-left : 10px;">
                                   	 		</th>
                                   	 		<th style="border: none; width: 20%; padding-top: 15px; padding-left: 10px;">
                                   	 			<button onclick="rsearch()" type="button" class="btn bg_01 btn btn-primary mb-3">검색</button>
                                   	 		</th> 
                                   		</tr>
                                   	</table>
                              </div>
                              <div id="status" style="height: 800px; padding-top: 10px;">
                                    
                           </div>
                           </form>
                        </div>
                     </div>
                  </div>
                  </div>
               </div>
                  


                        <div style="width: 41%; height: 100%; margin-left: 1%">
                         <div class="iq-card iq-border-radius-20" style="height: 49%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                 <div class="row">
                                    <div style="width: 50%; margin-left: 2%"><h4><b>OCR 결과 확인</b></h4></div>
                                    <div align="right" style="width: 42%;">
                                       <button class="btn btn-primary mb-3" onclick="ocredit()">저장</button>
                                   </div>

                                 </div>
                                              <!-- Modal -->
                                    <div class="modal fade" id="printModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                       <div class="modal-dialog" role="document" style="height: 200px">
                                          <div class="iq-card iq-border-radius-20 white" style="height: 100%">
                                             <div style="height: 100%">
                                                <div style="height: 10%;">
                                                </div>
                                                <div align="center" style="height: 40%; margin-top: 5%;">
                                                   <h4>출력하시겠습니까?</h4>
                                                </div>
                                                <div align="center" style="height: 30%">
                                                   <div class="row">
                                                      <div style="width: 50%;">
                                                         <button type="button" class="btn btn-primary"style="width: 60%;"><h4 style="color: white;"><b>출력</b></h4></button>
                                                      </div>
                                                      <div align="right" style="width: 40%; ">
                                                         <button type="button" class="btn iq-bg-danger" style="width: 75%;" data-dismiss="modal"><h4 style="color: #f36157;"><b>취소</b></h4></button>
                                                      </div>
                                                   </div>
                                                </div>
                                                <div style="height: 20%;">
                                                </div>
                                             </div>
                                          </div>
                                       </div>
                                    </div>

                              </div>
                                 			
                        	   <div style="width: 95%; height: 85%; padding-bottom: 2%;">
                           		<div style="height: 100%; background-color: white; border-radius: 10px;" id="ocrtable" class="no-wrap">
                              	      
                           		</div>
                        	   </div>
                           </div>

                           <div class="iq-card iq-border-radius-20" style="height: 49%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                 <h4><b>OCR 이미지</b></h4>
                                     
                              </div>
                                          
                              <div style="width: 95%; height: 85%; padding-bottom: 2%;">
                                 <div style="height: 100%; background-color: white; border-radius: 10px; padding-top: 10%;" >
                                      <img id="crop" width="100%" height="100%" ><!-- src="${pageContext.request.contextPath}/images/2/1/1/1.jpg" -->
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