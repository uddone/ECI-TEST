<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OMR 채점</title>
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
  left: 230px;
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
    left: 236px;
    background-color: #eeecef !important;
   }
</style>
<script type="text/javascript">
var examcd = "";
var lsncd = "";
var rpageNum = 1;
var rlimit = 5;
var pageno = 1;
var limit = 20;
var bstorcd="";
var txtlen = 0;
var cursorlen = 0;

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
	   console.log("pn : ${searchscore}")
	   
	    $("input[name=searchcontent]").keydown(function(key) {
                //키의 코드가 13번일 경우 (13번은 엔터키)
                if (key.keyCode == 13) {
                	searchfilter();
                }
            });
	    $("#rsearchcontent").keydown(function(key) {
            //키의 코드가 13번일 경우 (13번은 엔터키)
            if (key.keyCode == 13) {
            	rsearch();
            }
        });

	    $(document).keyup(function(key){
	    	// console.log('dd')
			if (key.keyCode == 37) {
				// console.log('ee')
				leftFsCmn();
			}

			if (key.keyCode == 39) {
				rightFsCmn();
			}

			if (key.keyCode == 38) {
				// console.log('ee')
				upFsCmn();
			}

			if (key.keyCode == 40) {
				downFsCmn();
			}
			if (key.keyCode == 35) {
				cursorlen =$(':focus').text().length;
			}
			if (key.keyCode == 36) {
				cursorlen = 0;
			}
			// if (key.keyCode == 33) {
			// 	cursorlen = 0;
			// 	console.log('pageup');
			// 	 // for(var adx = 0; adx<11; adx++){
			// 	 	rightFsCmn()
			// 	 // }
			// }
		})

		// fn_fsMoveImp('gradbody');

	});

// /*37: ←, 38: ↑, 39: →, 40: ↓ */

/*왼쪽으로 이동*/
function leftFsCmn(){
	txtlen = $(':focus').text().length;
	if(txtlen == 0 || cursorlen == 0){
		var idx = $(':focus').index()
		if($(':focus').parents().children().eq(idx-1).attr("contenteditable")){
			 var nextlen = $(':focus').parents().children().eq(idx-1).text().length;
			$(':focus').parents().children().eq(idx-1).focus();
			 console.log(nextlen+", 1")
			//   for (var ni = 0;ni <nextlen; ni++){
			//   	rightFsCmn()
			//   }
		}else{
			// var nextlen = $(':focus').parents().children().eq(idx-2).text().length;
			$(':focus').parents().children().eq(idx-2).focus();
			// console.log(nextlen+", 2")
			 // for (var ni = 0; ni<nextlen; ni++){
			 // 	rightFsCmn()
			 // }
		}

	}else {
		cursorlen -=1;
	}
	var nextlen = $(':focus').parents().children().eq(idx-1).text().length;

}

/*오른쪽으로 이동*/
function rightFsCmn(){
	txtlen = $(':focus').text().length;
	// console.log(txtlen)
	if(txtlen == 0 || txtlen <= cursorlen){
		var idx = $(':focus').index()
		if($(':focus').parents().children().eq(idx+1).attr("contenteditable")){
			$(':focus').parents().children().eq(idx+1).focus();
		}else{
			$(':focus').parents().children().eq(idx+2).focus();
		}
		cursorlen = 0;
	}else{
		cursorlen +=1;
	}
}

function upFsCmn(){
	cursorlen = 0
	var tridx = $(':focus').parent().index()
	var tdidx = $(':focus').index()
	// console.log('img : ' + $(':focus').parents().parents().children().eq(tridx-1).children().eq(0).attr('id'))
	var imgnm = $(':focus').parents().parents().children().eq(tridx-1).children().eq(0).attr('id')
	// console.log('tr : ' + tridx);
	if(tridx!=0){selex(0,imgnm,0);}
	$(':focus').parents().parents().children().eq(tridx-1).children().eq(tdidx).focus();

}
function downFsCmn(){
	cursorlen = 0
	var tridx = $(':focus').parent().index()
	var tdidx = $(':focus').index()
	var imgnm = $(':focus').parents().parents().children().eq(tridx+1).children().eq(0).attr('id')
	var id1 = $(':focus').parent().parent().children().length
	// console.log('tr : ' + tridx);
	if(tridx!=(id1-1)){selex(0,imgnm,0);}
	$(':focus').parents().parents().children().eq(tridx+1).children().eq(tdidx).focus();

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
		 
   function chkall(){
	   console.log('start')
	   var chk = $('input:checkbox[id="chkalll"]').is(":checked")
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
   
   
	function disp_div(excd,lsn,bstor_cd) {
		$("#rsearchcontent").val('');
   		$(".sub").each(function() {
      		$(this).removeClass("select");
   		})
   		if(lsn != 4 && lsn != 34 ){
   		 $('#lsnselbtn').removeAttr("disabled");
    		
   		}else{
   		 $('#lsnselbtn').attr("disabled","disabled");
    			
   		}
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
        // console.log(examcd+","+lsncd+","+bstorcd)
		var st = new Date().getTime();
		//console.log("st : " +st);
         $.ajax({
            url:"recoglist.ai",
            data:formData,
            type:'POST',
            processData:false,
            contentType:false,
            dataType:'json',
            cache:false,
            success:function(result){
            	// console.log(result)
           	 	drawresult(result);
            }
        });
   }	
   function drawresult(data){
	   var endTime = new Date().getTime();
	   // console.log("et : " +endTime);
	   var tble = ""
  		 tble = '<table id="gradtable" style="width : 100%">';//47 107
		 tble += '<thead><tr><th style="background-color: #f0f0f0; width:13px;"><input type="checkbox" id="chkalll" onclick="chkall()"></th><th style="background-color: #f0f0f0; width:96px;" id="exnoth" >수험번호</th><th style="background-color: #f0f0f0; width:47px;" id="sexth">성별</th><th style="background-color: #f0f0f0; width:107px;" id="bthdayth">생년월일</th><th style="background-color: #f0f0f0;" id="stnmth">성명</th><th style="background-color: #f0f0f0;" id="scrth">점수</th>';
	   var qcnt = data.length-1;
	   for(var qi = 1; qi<data[0].length; qi++){
		   tble += '<th id="gradtableth" style="background-color: #f0f0f0; width:25px;" >'+qi+'</th>';  
	   }
		 tble +='</tr></thead><tbody id="gradbody">';
		 for(var sti = 0 ; sti < data.length; sti++){
			 tble += '<tr class="ex" id="'+data[sti][0].OMR_KEY+'k" onclick="selex(\''+data[sti][0].EXMN_NO+'\',\''+data[sti][0].OMR_IMG+'\',\''+data[sti][0].OMR_KEY+'\')">';
				 // console.log(data[sti][0].LSN_SEQ)
				 if(data[sti][0].LSN_SEQ == 1){
					 tble += '<td style="background-color: #A9D0F5; width:13px;" id="'+data[sti][0].OMR_IMG+'" ><input type="checkbox" class="chk" name="chk" id="'+data[sti][0].EXMN_NO+'" ></td>';
				 }else if(data[sti][0].LSN_SEQ == 2){
					 tble += '<td style="background-color: #F5F6CE; width:13px;" id="'+data[sti][0].OMR_IMG+'" ><input type="checkbox" class="chk" name="chk" id="'+data[sti][0].EXMN_NO+'" ></td>';		 
				 }else{
					 tble += '<td style="background-color: #eeecef; width:13px;" id="'+data[sti][0].OMR_IMG+'" ><input type="checkbox" class="chk" name="chk" id="'+data[sti][0].EXMN_NO+'" ></td>';
					 
				 }
				
			 tble += '<td id="exno" style="color:black;';
			 if(data[sti][0].NO_ERR_YN == 'Y'){
				 tble +='background-color: #ff3c3c;';
			 }else{
				 tble +='background-color: #eeecef;';
			 }
			 tble +='" name="'+data[sti][0].OMR_KEY+'" contenteditable="true">'+data[sti][0].EXMN_NO+'</td>';
			// tble += '<td id="stnm" contenteditable="true" ';
			tble += '<td id="sex" contenteditable="true" style="color:black; ';
			if(data[sti][0].SEX_ERR_YN == 'Y' && (data[sti][0].OMR_MST_CD > 10 || data[sti][0].OMR_MST_CD < 16)){
				 tble +='background-color: #ff3c3c;';
			 }else{
				 tble +='background-color: #eeecef;';
			 }
			if(data[sti][0].BIRTH_ERR_YN == 'Y'){
				// console.log("y"+data[sti][0].STDN_NM)
			}
			tble += '" >'+data[sti][0].SEX+'</td><td id="birth" contenteditable="true" style="color:black; ';
			if(data[sti][0].BIRTH_ERR_YN == 'Y' && (data[sti][0].OMR_MST_CD < 10 || data[sti][0].OMR_MST_CD == 16) ){

				 tble +='background-color: #ff3c3c;';
			 }else{
				 tble +='background-color: #eeecef;';
			 }
			tble+='" >'+data[sti][0].BIRTH+'</td><td id="stnm" contenteditable="true" ';
			 
			 if(data[sti][0].ERR_YN == 'Y'|| data[sti][0].NAME_ERR_YN == 'Y'){
			 	// if(data[sti][0].ERR_YN =='Y'){
			 	// 	console.log('에러')
				// }else{
				// 	console.log('NOERR')
				// }
				 tble +=' style="background-color : #ff3c3c; color:black; "';
			 } else if(data[sti][0].ERR_YN == 'Z'){
				 tble += ' style="background-color : #F4FA58; color:black; "';	 
			  }else {
				 tble +=' style="background-color : #eeecef; color:black;"';
			 }
			 
			 tble +='>'+data[sti][0].STDN_NM+'</td><td id="scr" style="color:black;">'+data[sti][0].TOT_SC+'</td>';
			 for(var i = 1; i<data[sti].length; i++){
				 tble += '<td contenteditable="true" style=" width:20px; ';
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
		
		// console.log(key+"k")
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
  		// console.log(sscore+","+sbstor+","+smock+","+sgrade+","+ssub)
  		// console.log(page)
  		pageno=page
  		f = document.searchform;
	   	f.pageNum.value=page
	    var a = f.pageNum.value
	    console.log(a)
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
       	f.limit.value=limit;
       	f.pageNum.value=1;
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
   		var form = $('#recogform');
        var formData = new FormData(form[0]);
        formData.append("examcd",examcd)
        formData.append("lsncd",lsncd)
        formData.append("bstorcd",bstorcd)
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
		var escape = false
   		tr.each(function(i){
   			var okeyk = tr.eq(i).attr("id")
   			var okey = okeyk.substring(0,okeyk.length-1)
   			//console.log(okey);
   			var td = tr.eq(i).children();
   			//var td = tr.eq(i).children();
   			var tdArr = new Array();

			td.each(function(j){
				if(j == 1 && td.eq(j).text().length < 5){
					alert('수험번호는 5자리 이상으로 수정해주세요')
					escape = true;
				}
			})

   			td.each(function(j){
   				if(j == 0){
   					tdArr.push(okey)
   	   			}else{
					var ead = td.eq(j).text();
					var bgin = ead.substr(0,1);
					var ennd = ead.substr(ead.length-1,ead.length);
					//console.log('a'+ennd+'a')
					var midtx = '-1';

					if(bgin.trim() == '' || bgin.trim() == '□'){
						if(ennd.trim() == '' || ennd.trim() == '□'){
							midtx += ead.substr(1,ead.length-2)+'-1';
						}else{
							midtx += ead.substr(1,ead.length-1);
						}
						var tdtext = midtx.replace(/□/gi,'-1');
						//	console.log(tdtext)
						tdArr.push(tdtext);
					} else{
						var tdtext = ead.replace(/□/gi,'-1');
						//console.log(tdtext)
						tdArr.push(tdtext);
					}
				}
   			});

   			console.log('escape : '+escape);
   			var key = "tdArr" + index;
   	     	formData.append(key,tdArr);
			// console.log('tdarr : '+tdArr);
   	     	index += 1 ;
     	});
		console.log('escape2 : '+escape);

   		if (escape == false){
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
            		location.reload();
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
   		var op ="width=600, height=800, left=50, top=150";
	   	open(view+".ai?limit=5&pageNum=1&searchcontent=","",op);
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
   		pageno=1
   		f = document.searchform;
       	f.searchbstor.value=sbstor;
       	f.searchmock.value=smock;
       	f.searchgrade.value=sgrade;
       	f.searchsub.value=ssub;
       	f.searchscore.value=sscore;
       	f.pageNum.value = 1;
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
				   console.log(unikey);
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
            	 alert('채점완료 되었습니다.');
               	 location.reload();
            }
        });
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
    
    function selectlsn(){
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
		if(stdArr.length == 0){
			alert('수정할 학생을 선택해주세요')
		}else{
			var op ="width=600, height=400, left=50, top=150";
			open("SEL_LSN.ai?bstorcd="+bstorcd+"&examcd="+examcd+"&lsncd="+lsncd+"&keylist="+stdArr,"",op);
		}
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
							<div class="iq-card iq-border-radius-20" style="height: 1040px; width: 30%; margin-bottom: 10px;" align="center">
								<div style="height: 40px; width: 100%;" align="left">
									<div class="row" style="width: 100%; margin-left: 10px;" >
										<div style="width: 55%;"><h4><b>모의고사 과목 선택</b></h4></div>
										<div style="width: 45%; padding-right: 15px;" align="right" >
                                 		<!--
                                 		//0405_수정사항
                                 		검색 버튼과 기능 동일 -> 버튼 제거
                                 		<button class="btn bg_01 btn btn-primary mb-3" onclick="searchfilter()">조회</button>
                                 		-->
											<button class="btn bg_01 btn btn-primary mb-3" onclick="deletegrdgrec()">삭제</button>
											<button class="btn bg_01 btn btn-primary mb-3" onclick="ALLGRDG()">채점</button>
										</div>
									</div>
								</div>
								<div style="width: 97%; height: 93%; padding-top: 1%">
									<div class="iq-card" style="width: 100%; height: 100%;" >
										<div class="iq-card-body" style="width: 100%; height: 100%;" >
											<div class="iq-todo-page" style="height: 100%;">
												<form class="position-relative" name="searchform" id="searchform" action="OMR_GRDG.ai" method="post" style="height: 100%;" onsubmit="return false">
													<div class="form-group mb-0" style="padding-bottom: 5px; height: 50px;">
														<input type="hidden" name="searchbstor" value="${param.searchbstor}">
														<input type="hidden" name="searchmock" value="${param.searchmock}">
														<input type="hidden" name="searchgrade" value="${param.searchgrade}">
														<input type="hidden" name="searchsub" value="${param.searchsub}">
														<input type="hidden" name="searchscore" value="${param.searchscore}">
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
														<table style="width: 100%; text-align: center;">
															<tr>
																<th><input type="checkbox" id="bstorchkall" onclick="bchkall()"></th>
																<th id="(3,4,5,6)" onclick="win_open('FILT_BSTOR')" name="sbstor">지점</th>
																<th id="(1,8,11)" onclick="win_open('FILT_MOCK')" name="smock">모의고사</th>
																<th id="('고1','고2','고3')" onclick="win_open('FILT_GRDG')" name="sgrade">학년</th>
																<th>채점</th>
																<th id=""  onclick="win_open('FILT_SCORE')" name="sscore" >완료/전체(총)</th>
																<th>오류수</th>
																<th id="('국어','영어','수학')" onclick="win_open('FILT_SUB')" name="ssub">교과</th>
																<th>과목</th>
																<th>평균점수</th>
															</tr>
															<c:forEach items="${EXAMList}" var="ex"><tr id ="${ex.EXAM_CD}a${ex.LSN_CD}a${ex.BSTOR_CD}" class="sub" onclick="disp_div('${ex.EXAM_CD}','${ex.LSN_CD}','${ex.BSTOR_CD}')">
																<td id="${ex.LSN_CD}"><input type="checkbox" name="grdgch"></td>
																<td id="${ex.BSTOR_CD}">${ex.BSTOR_NM}</td>
																<td id="${ex.EXAM_CD}">${ex.EXAM_NM}</td>
																<td>${ex.SCHYR}</td>
																<c:if test="${ex.CRA_YN != ex.CRA_CNT}"><td>
																	<button type="button" onclick="scoring('${ex.EXAM_CD}','${ex.LSN_CD}','${ex.BSTOR_CD}')" style=" border: grey;">채점</button></td>
                               								<!-- class="btn bg_01 btn btn-primary mb-3"  -->
																</c:if>
																<c:if test="${ex.CRA_YN == ex.CRA_CNT}"><td>
																	<button type="button" onclick="scoring('${ex.EXAM_CD}','${ex.LSN_CD}','${ex.BSTOR_CD}')" style="background: red; border: red; color: white;">채점</button></td>
																</c:if>
																<td>${ex.CRA_YN}/${ex.CRA_CNT} <c:if test="${ex.LSN_CD != -1 && ex.LSN_CD != -2 }">(${ex.LSN_GRP_CNT})</c:if> </td>
																<td>${ex.ERR_CNT}</td>
																<td>${ex.SUB_NM}</td>
																<td>${ex.LSN_NM}</td>
																<td>${ex.AVG_SC}</td>
															</tr></c:forEach>
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
															<c:if test="${pageNum <= 1}">이전</c:if>
															<c:forEach var="a" begin="${startpage }" end="${endpage }">
																<c:if test="${a == pageNum }">[${a }]</c:if>
																<c:if test="${a != pageNum }"><a href="javascript:listdo('${a }')">[${a }]</a></c:if>
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
														<span><button type="button" class="btn btn-primary mb-3" onclick="transdata()" style="display: none;" >전송</button></span>
														<span><button type="button" class="btn btn-primary mb-3" onclick="selectlsn()" disabled="disabled" id="lsnselbtn">과목선택</button></span>
														<span><button type="button" class="btn btn-primary mb-3" onclick="deletedata()">삭제</button></span>
														<span><button type="button" class="btn btn-primary mb-3" onclick="gradsave()">저장</button></span>
													</div>
												</div>
											</div>
											<div style="height: 335px; padding-top: 1%" >
												<div class="iq-card" style="width: 98%; height: 100%; margin-bottom: 30px;">
													<div class="iq-card-body" style="width: 100%; height: 100%;" >
														<div class="iq-todo-page" style="height: 100%;">
															<form name="recogform" id="recogform" class="position-relative" onsubmit="return false">
																<div class="form-group mb-0" style="padding-bottom: 5px; height: 50px;">
																	<table style="border: none; width: 100%; background-color: white;"  >
																		<tr style="border: none; width: 100%; background-color: white;">
																			<th style="border: none; width: 80%; background-color: white;">
																				<input type="hidden" name="pageNum" value="${param.pageNum }">
																				<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  name="rsearchcontent" id="rsearchcontent" value="${param.rsearchcontent }">
																			</th>
																			<th style="border: none; width: 5%; padding-top: 15px; background-color: white;">
																				<button onclick="rsearch()" type="button" class="btn bg_01 btn btn-primary mb-3">검색</button>
																			</th>
																		</tr>
																	</table>
																</div>
																<div class="no-wrap" id="graddiv" style="height: 220px; margin-top : 20px;" align="left"></div>
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