<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>모의고사 관리</title>
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
   
   <script src="http://code.jquery.com/jquery-latest.js"></script>
   
      <script type="text/javascript">
         $(document).ready(function() {
        	 $(".info").each(function() {
     	        $(this).hide();
     	  	})
     	     
     	  	$("#moinfo").show();
        	
        	$(".Edit").each(function() {
        		$(this).show();
        	})
        	 
        	 $("#input_file").bind('change', function() {
               selectFile(this.files);
               //this.files[0].size gets the size of your file.
               //alert(this.files[0].size);
            });
        	 /*$(".sidemenu").each(function() {
        	        $(this).removeAttr("active");
        	     })
        	     $("#BSTOR_RC_REG_SN").attr("active","active")*/
        	 
        	 if("${param.limit}" != ""){
     	     	$("#limit").val("${param.limit}").prop("selected", true);
     	     	console.log(${param.limit})
     	     }else{
     	     	$("#limit").val("5").prop("selected", true);
     	     }	
         });
         
      
         // 파일 리스트 번호
         var fileIndex = 0;
         // 등록할 전체 파일 사이즈
         var totalFileSize = 0;
         // 파일 리스트
         var fileList = new Array();
         // 파일 사이즈 리스트
         var fileSizeList = new Array();
         // 등록 가능한 파일 사이즈 MB
         var uploadSize = 50;
         // 등록 가능한 총 파일 사이즈 MB
         var maxUploadSize = 500;
         var pageno = 1;
         var examcd = "";
         var lcd = "";
         
         function disp_div(sub) {
             $(".sub").each(function() {
                $(this).removeClass("select");
             })
             $("#" + sub).addClass("select");
             examcd = ""+sub;
             $('#newlsn').removeAttr('disabled');
             
             var form = $('#blankform');
        	 var formData = new FormData(form[0]);
        	 console.log('sub :'+sub)
        	    formData.append('examCd',sub)
        		$.ajax({
        			url: "mlsnlist.ai",
        			type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				console.log(data)
        				lsnlist(data)
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
          	}
         function selectview(data){
        	 var view = data[0].view;
        	 $(".hidediv").each(function() {
     			$(this).hide();
          	 })
        	 if(view == 1){
        		 $("#upload_div").show();
        		 $('#fileTableTbody').text("");
        	 } else if (view == 2){
        		 $("#recog_div").show();
        		 var a = data[0].filecnt+'개의 파일 업로드 완료!!'
            	 $('#filecnt').html(a);
        	 } else {
        		 var reuld = '재 업로드 필요 파일 갯수 : ' +data[0].filecnt+'<br>재업로드 필요 파일 목록 : '+data[0].filename
        		 $('#reupload').html(reuld);
        		 $("#upload_div").show();
        		 $('#fileTableTbody').text("");
        	 }
        	 fileList=[];
         }
         function lsnlist(data){
        	 var tble = "";
        	 for(var i = 0;i<data.length;i++){
        		 tble +='<tr id="'+data[i].lsncd+'">';
        		 tble +='<td>'+data[i].period+'</td><td>'+data[i].subject+'</td><td><a onclick="win_open(\'OMR_SEL\',\''+examcd+'\',\''+data[i].lsncd+'\')"><input type="text" id="a'+examcd+data[i].lsncd+'" value="'+data[i].omrnm+'"></a></td><td>'+data[i].quei+'</td><td>'+data[i].tot+'</td>';
        		 tble +='<td><button class="btn btn-primary"  onclick="quest_div(\'questinfo\',\''+examcd+'\',\''+data[i].lsncd+'\')" >문항작성</button></td><td><span><button type="button" class="icon Editlsn" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="수정" onclick="editlsn(\''+data[i].lsncd+'\')" id="Editlsn'+data[i].lsncd+'"><i class="ri-edit-line"></i></button><button class="icon Savelsn" data-icon="S" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="저장"  id="Savelsn'+data[i].lsncd+'" onclick="savelsn(\''+data[i].lsncd+'\')"></button></span>';
        		 tble +='<span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" onclick="copylsn(\''+data[i].lsncd+'\')"><i class="las la-copy" data-toggle="tooltip" data-placement="top" title="복제"></i></button></span>';
            	 tble +='<span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" onclick="dellsn(\''+data[i].lsncd+'\')"><a href="#" data-toggle="tooltip" title="삭제" style="color: black;"><i class="ri-delete-bin-line" aria-hidden="true"></i></a></button></span></td>';
        		 tble +='<tr>';
        	 }
        	 $("#lsnlist").html(tble);
        	 
        	 $(".Editlsn").each(function() {
         		$(this).show();
         	})
         	
         }
         function dellsn(lsn){
        	 if (confirm("삭제 하시겠습니까?")) {
        	 var form = $('#blankform');
        	 var formData = new FormData(form[0]);        	
        	    formData.append('examcd',examcd)
        	    formData.append('lsncd',lsn)
        		$.ajax({
        			url: "dellsn.ai",
        			type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				console.log(data)
        				lsnlist(data)
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
        	}
         }
         $(function() {
            // 파일 드롭 다운
            fileDropDown();
         });
   
         // 파일 드롭 다운
         function fileDropDown() {
            var dropZone = $("#dropZone");
            //Drag기능 
            dropZone.on('dragenter', function(e) {
               e.stopPropagation();
               e.preventDefault();
               // 드롭다운 영역 css
               dropZone.css('background-color', '#E3F2FC');
            });
            dropZone.on('dragleave', function(e) {
               e.stopPropagation();
               e.preventDefault();
               // 드롭다운 영역 css
               dropZone.css('background-color', '#FFFFFF');
            });
            dropZone.on('dragover', function(e) {
               e.stopPropagation();
               e.preventDefault();
               // 드롭다운 영역 css
               dropZone.css('background-color', '#E3F2FC');
            });
            dropZone.on('drop', function(e) {
               e.preventDefault();
               // 드롭다운 영역 css
               dropZone.css('background-color', '#FFFFFF');
   
               var files = e.originalEvent.dataTransfer.files;
               if (files != null) {
                  if (files.length < 1) {
                     /* alert("폴더 업로드 불가"); */
                     console.log("폴더 업로드 불가");
                     return;
                  } else {
                     selectFile(files)
                  }
               } else {
                  alert("ERROR");
               }
            });
         }
   
         // 파일 선택시
         function selectFile(fileObject) {
            var files = null;
   
            if (fileObject != null) {
               // 파일 Drag 이용하여 등록시
               files = fileObject;
            } else {
               // 직접 파일 등록시
               files = $('#multipaartFileList_' + fileIndex)[0].files;
            }
   
            // 다중파일 등록
            if (files != null) {
               
               if (files != null && files.length > 0) {
                  $("#fileDragDesc").hide(); 
                  $("fileListTable").show();
               } else {
                  $("#fileDragDesc").show(); 
                  $("fileListTable").hide();
               }
               
               for (var i = 0; i < files.length; i++) {
                  // 파일 이름
                  var fileName = files[i].name;
                  var fileNameArr = fileName.split("\.");
                  // 확장자
                  var ext = fileNameArr[fileNameArr.length - 1];
                  
                  var fileSize = files[i].size; // 파일 사이즈(단위 :byte)
                  console.log("fileSize="+fileSize);
                  if (fileSize <= 0) {
                     console.log("0kb file return");
                     return;
                  }
                  
                  var fileSizeKb = fileSize / 1024; // 파일 사이즈(단위 :kb)
                  var fileSizeMb = fileSizeKb / 1024; // 파일 사이즈(단위 :Mb)
                  
                  var fileSizeStr = "";
                  if ((1024*1024) <= fileSize) {   // 파일 용량이 1메가 이상인 경우 
                     console.log("fileSizeMb="+fileSizeMb.toFixed(2));
                     fileSizeStr = fileSizeMb.toFixed(2) + " Mb";
                  } else if ((1024) <= fileSize) {
                     console.log("fileSizeKb="+parseInt(fileSizeKb));
                     fileSizeStr = parseInt(fileSizeKb) + " kb";
                  } else {
                     console.log("fileSize="+parseInt(fileSize));
                     fileSizeStr = parseInt(fileSize) + " byte";
                  }
   
                  /* if ($.inArray(ext, [ 'exe', 'bat', 'sh', 'java', 'jsp', 'html', 'js', 'css', 'xml' ]) >= 0) {
                     // 확장자 체크
                     alert("등록 불가 확장자");
                     break; */
                  if ($.inArray(ext, [ 'hwp', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'png', 'pdf', 'jpg', 'jpeg', 'gif', 'zip' ]) <= 0) {
                     // 확장자 체크
                     /* alert("등록이 불가능한 파일 입니다.");
                     break; */
                     alert("등록이 불가능한 파일 입니다.("+fileName+")");
                  } else if (fileSizeMb > uploadSize) {
                     // 파일 사이즈 체크
                     alert("용량 초과\n업로드 가능 용량 : " + uploadSize + " MB");
                     break;
                  } else {
                     // 전체 파일 사이즈
                     totalFileSize += fileSizeMb;
   
                     // 파일 배열에 넣기
                     fileList[fileIndex] = files[i];
   
                     // 파일 사이즈 배열에 넣기
                     fileSizeList[fileIndex] = fileSizeMb;
   
                     // 업로드 파일 목록 생성
                     addFileList(fileIndex, fileName, fileSizeStr);
   
                     // 파일 번호 증가
                     fileIndex++;
                  }
               }
            } else {
               alert("ERROR");
            }
            var uploadFileList = Object.keys(fileList);
            
            // 파일이 있는지 체크
            if (uploadFileList.length == 0) {
            	  // 파일등록 경고창
                alert("파일이 없습니다.");  
               return;
            }
   
            // 용량을 500MB를 넘을 경우 업로드 불가
            if (totalFileSize > maxUploadSize) {
               // 파일 사이즈 초과 경고창
               alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
               return;
            }
  
            if (confirm("등록 하시겠습니까?")) {
               // 등록할 파일 리스트를 formData로 데이터 입력
               /*var uploading = ""
               uploading +="<tr style='height: 90%;'> <td id='dropZone' style='padding-top:13%;'><img src='images/iconmonstr-note.png' width='15%'><img src='images/iconmonstr-arrow.png' width='5%'><img src='images/iconmonstr-idea.png' width='15%'><br><br><h3>"+fileIndex+"개의 파일 업로드 완료!!</h3><h3>OCR 인식중</h3></td></tr>";
                $('#uploadzone').html(uploading);*/
                var form = $('#uploadForm');
                var formData = new FormData(form[0]);
                for(var i = 0; i < uploadFileList.length; i++){
                    formData.append('files', fileList[uploadFileList[i]]);
                }
                
                formData.append("examcd",examcd)
                formData.append("lsncd",lcd)

                $.ajax({
                    url:"qexcelupload.ai",
                    data:formData,
                    type:'POST',
                    processData:false,
                    contentType:false,
                    dataType:'json',                    
                    cache:false,
                    success:function(result){
                        console.log(result);
                        drawquei(result);
                    }
                });
                fileList=[];
            }
         }
         function drawquei(data){
      		var output = ""
      		for(var i = 1; i<data.length; i++){
      			output += '<tr id="'+data[i].queino+'"><td>'+data[i].queino+'</td><td>'+data[i].crano+'</td><td>'+data[i].dismk+'</td><td>'+data[i].quearea+'</td><td>'+data[i].quedtlarea+'</td><td>'+data[i].quetype+'</td>';
      			output += '<td><span><button type="button" class="icon Editquei" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="수정" onclick="editquei(\''+data[i].queino+'\')" id="Editquei'+data[i].queino+'"><i class="ri-edit-line"></i></button><button class="icon Savequei" data-icon="S" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="저장"  id="Savequei'+data[i].queino+'" onclick="savequei(\''+data[i].queino+'\')"></button></span><span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="delqueirow" onclick="delquei(\''+data[i].queino+'\')"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span></td></tr>';
      		}
      		$("#queibody").html(output);
      		 
      		$(".Editquei").each(function() {
          		$(this).show();
          	})
        }
         function delquei(queino){
        	 if (confirm("삭제 하시겠습니까?")) {
				 var form = $('#blankform');
	        	 var formData = new FormData(form[0]);        	
	        	    formData.append('examcd',examcd)
	        	    formData.append('lsncd',lcd)
	        	    formData.append('queino',queino)
	        		$.ajax({
	        			url: "delquei.ai",
	        			type: "POST",
	        			data: formData,
	        			processData:false,
	        	        contentType:false,
	        	        cache:false,
	        			dataType : "json",
	        			//contentType : "application/json",
	        			success: function(data){
	        				reload()
	        		       	},
	        		    error : function(e){
	        		    	alert("서버오류 : " + e.status);
	        		       	} 
	        		     })
			 }
         }
         // 업로드 파일 목록 생성
         function addFileList(fIndex, fileName, fileSizeStr) {
            /* if (fileSize.match("^0")) {
               alert("start 0");
            } */
   
           // var html = fIndex+1+"개의 파일 업로드 대기중";
            //$('#fileTableTbody').text(html);
         }
   
         // 업로드 파일 삭제
         function deleteFile(fIndex) {
            console.log("deleteFile.fIndex=" + fIndex);
            // 전체 파일 사이즈 수정
            totalFileSize -= fileSizeList[fIndex];
   
            // 파일 배열에서 삭제
            delete fileList[fIndex];
   
            // 파일 사이즈 배열 삭제
            delete fileSizeList[fIndex];
   
            // 업로드 파일 테이블 목록에서 삭제
            $("#fileTr_" + fIndex).remove();
            
            console.log("totalFileSize="+totalFileSize);
            
            if (totalFileSize > 0) {
               $("#fileDragDesc").hide(); 
               $("fileListTable").show();
            } else {
               $("#fileDragDesc").show(); 
               $("fileListTable").hide();
            }
            
         }
   
         // 파일 등록
         function uploadFile() {
            // 등록할 파일 리스트
          
        }
         function drawtable(result){
        	 var a = result[0].filecnt+'개의 파일 업로드 완료!!'
        	 $('#filecnt').html(a);
         }
         function copyex(ex){
        	 var form = $('#blankform');
        	 var formData = new FormData(form[0]);
        	    formData.append('examCd',ex)
        		$.ajax({
        			url: "mexamcopy.ai",
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
         function copylsn(lsn){ 
        	 var form = $('#blankform');
        	 var formData = new FormData(form[0]);
        	 
        	    formData.append('examcd',examcd)
        	    formData.append('lsncd',lsn)  
        		$.ajax({
        			url: "mlsncopy.ai",
        			type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				drawcopylsnrow(data)
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
        			url: "newmexam.ai",
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
         	function copyexrow(data){
         		$("#examhead").append(function(){
         			var output = ""
         			output += '<tr id="'+data[0].exam_cd+'"><td></td><td contenteditable="true"><input type="date" value="'+data[0].examdt+'"></td><td contenteditable="true">'+data[0].year+'</td><td contenteditable="true">'+data[0].kind+'</td><td contenteditable="true">'+data[0].exname+'</td><td contenteditable="true">'+data[0].eci_exam_cd+'</td>';
         			output +='<td><span><button type="button" data-icon="S" class="icon" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="createexrow"></button></span><span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="delexrow"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span></td></tr>'; 
         		 	return output;
         		}) 		
         	}
         	$(document).on("click","button[name=createexrow]",function(){
                
                var tr = $(this).parent().parent().parent();
                var td = tr.children();
                var examdt = td.eq(1).children().eq(0).val();
                var schyr = td.eq(2).text();
                var exam_kind = td.eq(3).text();
        		var exam_nm = td.eq(4).text();
        		var eci_exam_cd = td.eq(5).text();	
        		var exam_cd = tr.attr('id');
        		console.log('examcd'+ exam_cd)
        		console.log('examdt'+ examdt)
        		console.log('schyr'+schyr)
        		console.log('exam_kind'+exam_kind)
        		console.log('eci_exam_cd'+eci_exam_cd)
        		console.log('exam_nm'+exam_nm)
        		
        		 var form = $('#blankform');
        	 	var formData = new FormData(form[0]);
        	 	formData.append('examcd', exam_cd)
        	    formData.append('examdt', examdt)
        		formData.append('schyr',schyr)
        		formData.append('exam_kind',exam_kind)
        		formData.append('eci_exam_cd',eci_exam_cd)
        		formData.append('exam_nm',exam_nm)
        		if(exam_cd != 0){
        			$.ajax({
            			url: "copyexrow.ai",
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
            			url: "createexrow.ai",
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

			$(document).on("click","button[name=delexrow]",function(){
                
                var trHtml = $(this).parent().parent().parent();
                //console.log(trHtml);
                trHtml.remove(); //tr 테그 삭제
                
            });
			
			
			function quest_div(a,exam_cd,lsn_cd){
				$(".info").each(function() {
		     	        $(this).hide();
		     	  })
		     	  examcd = exam_cd
		     	  lcd = lsn_cd
		     	  console.log(examcd+","+lcd)
		     	  $("#"+a).show();
					var form = $('#blankform');
	                var formData = new FormData(form[0]);
	                
	                formData.append("examcd",examcd)
	                formData.append("lsncd",lcd)

	                $.ajax({
	                    url:"qlist.ai",
	                    data:formData,
	                    type:'POST',
	                    processData:false,
	                    contentType:false,
	                    dataType:'json',                    
	                    cache:false,
	                    success:function(result){
	                        console.log(result);
	                        drawquei(result);
	                    }
	                });
			}
			function win_open(view,examcd,lsncd){
				var op ="width=900, height=400, left=50, top=150";
			   	open(view+".ai?examcd="+examcd+"&lsncd="+lsncd,"",op);
			}
			function usechk(id){
				console.log('start')
				var chk = $('input:checkbox[id="usechk'+id+'"]').is(":checked")
				if(chk){
					console.log('already')
					$('input:checkbox[id="usechk'+id+'"]').checked = true;
				}else{
					console.log('yet')
					$('input:checkbox[id="usechk'+id+'"]').checked = false;
				}
				var form = $('#blankform');
        	 	var formData = new FormData(form[0]);
        	 	formData.append('examcd', exam_cd)
        	    $.ajax({
        			url: "usechk.ai",
        			type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				alert(data.result)
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
			}
			function newlsnrow(){
				console.log(examcd);
				var form = $('#blankform');
        	 	var formData = new FormData(form[0]);
        	 	formData.append('examcd', examcd)
        	    $.ajax({
        			url: "newlsnrow.ai",
        			type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				console.log(data);
        				drawcreatelsnrow(data);
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
			}
			function drawcreatelsnrow(data){
				$("#lsnhead").append(function(){
         			var output = ""
         			lcd = data[1].lsncd;
         			output += '<tr id="'+data[0].examcd+'"><td></td><td><select id="lsnsel" onchange="lsnsel()">';
         			for(var i = 1; i<data.length; i++){
         				output +='<option value="'+data[i].lsncd+'">'+data[i].lsnnm+'</option>';	
         			}
         			output += ' </select></td><td><a onclick="win_open(\'OMR_SEL\',\''+data[0].examcd+'\',\''+lcd+'\')"><input type="text" id="a'+data[0].examcd+lcd+'" value=""></a></td><td contenteditable="true"></td><td contenteditable="true"></td><td></td>';
         			output +='<td><span><button type="button" data-icon="S" class="icon" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="createlsnrow"></button></span><span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="dellsnrow"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span></td></tr>'; 
         		 	return output;
         		}) 		
			}
			function drawcopylsnrow(data){
				$("#lsnhead").append(function(){
         			var output = ""
         			lcd = data[0].lsncd;
         			output += '<tr id="'+data[0].examcd+'"><td></td><td><select id="lsnsel" onchange="lsnsel()">';
         			for(var i = 1; i<data.length; i++){
         				output +='<option value="'+data[i].lsncd+'">'+data[i].lsnnm+'</option>';	
         			}
         			output += ' </select></td><td><a onclick="win_open(\'OMR_SEL\',\''+data[0].examcd+'\',\''+lcd+'\')"><input type="text" id="a'+data[0].examcd+lcd+'" value="'+data[0].omrnm+'"></a></td><td contenteditable="true">'+data[0].quei+'</td><td contenteditable="true">'+data[0].tot+'</td><td></td>';
         			output +='<td><span><button type="button" data-icon="S" class="icon" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="createlsnrow"></button></span><span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" name="dellsnrow"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span></td></tr>'; 
         		 	return output;
         		}) 		
			}
			function lsnsel(){
				 var lt = $("#lsnsel option:selected").val();
				 lcd = lt;
			}
			$(document).on("click","button[name=createlsnrow]",function(){
                
                var tr = $(this).parent().parent().parent();
                var td = tr.children();
                var lsn_cd = td.eq(1).children().eq(0).val();
                var omr_kind = td.eq(2).children().children().eq(0).val();
        		var quei = td.eq(3).text();
        		var exam_cd = tr.attr('id');
        		var tot = td.eq(4).text();
        		console.log('examcd'+ exam_cd)
        		console.log('lsn_cd'+lsn_cd)
        		console.log('omr_kind'+omr_kind)
        		console.log('quei'+quei)
        		console.log('tot'+tot)
        		
        		 var form = $('#blankform');
        	 	var formData = new FormData(form[0]);
        	 	formData.append('examcd', exam_cd)
        	    formData.append('lsn_cd', lsn_cd)
        		formData.append('quei',quei)
        		formData.append('omr_kind',omr_kind)
        		formData.append('tot',tot)
        		$.ajax({
            			url: "createlsnrow.ai",
            			type: "POST",
            			data: formData,
            			processData:false,
            	        contentType:false,
            	        cache:false,
            			dataType : "json",
            			//contentType : "application/json",
            			success: function(data){
            				alert("과목추가 완료");
            				tr.remove();
            				lsnlist(data);
            		       	},
            		    error : function(e){
            		    	alert("서버오류 : " + e.status);
            		       	} 
            		     })
            		     
        	 });
         	function resultalert(data){
         		alert(data[0].result);
         		location.reload();
         	}

			$(document).on("click","button[name=dellsnrow]",function(){
                
                var trHtml = $(this).parent().parent().parent();
                //console.log(trHtml);
                trHtml.remove(); //tr 테그 삭제
                
            });
			function editex(ex){
				var tr = $("#Editex" + ex).parent().parent().parent();
				var td = tr.children();
				td.eq(2).attr("contenteditable", true);
				td.eq(3).attr("contenteditable", true);
				td.eq(4).attr("contenteditable", true);
				td.eq(5).attr("contenteditable", true);	  
				
				$("#Editex" + ex).hide();
				 
				$("#Saveex" + ex).show();
			}

			function saveex(ex){
				if (confirm("저장 하시겠습니까?")) {
			
				var tr = $("#Saveex" + ex).parent().parent().parent();
				var td = tr.children();
					td.eq(2).removeAttr("contenteditable", true); 
					td.eq(3).removeAttr("contenteditable", true);
					td.eq(4).removeAttr("contenteditable", true);
					td.eq(5).removeAttr("contenteditable", true);
					var exam_dt = td.eq(1).children().eq(0).val()
					var schyr = td.eq(2).text();
					var exam_kind = td.eq(3).text();
					var exam_nm = td.eq(4).text();
					var eci_exam_cd = td.eq(5).text();
				$("#Saveex" + ex).hide();
				 
				$("#Editex" + ex).show();
				
				 var form = $('#blankform');
	        	 var formData = new FormData(form[0]);
	        	 formData.append('examcd',ex);
	        	 formData.append('exam_dt',exam_dt);
	        	 formData.append('schyr',schyr);
	        	 formData.append('exam_kind',exam_kind);
	        	 formData.append('exam_nm',exam_nm);
	        	 formData.append('eci_exam_cd',eci_exam_cd);
	        	 console.log('excd : '+ex+' , examnm : '+exam_nm)
				$.ajax({
					url: "update_omr_exam.ai",
					type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				lsnlist(data)
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
        		}
			}
			function editlsn(lsn){
				var tr = $("#Editlsn" + lsn).parent().parent().parent();
				var td = tr.children();
				td.eq(3).attr("contenteditable", true);
				td.eq(4).attr("contenteditable", true);
				
				$("#Editlsn" + lsn).hide();
				 
				$("#Savelsn" + lsn).show();
			}

			function savelsn(lsn){
				if (confirm("저장 하시겠습니까?")) {
				var tr = $("#Savelsn" + lsn).parent().parent().parent();
				var td = tr.children();
					td.eq(3).removeAttr("contenteditable", true);
					td.eq(4).removeAttr("contenteditable", true);
					var omr_kind = td.eq(2).children().children().eq(0).val();
					var queino = td.eq(3).text();
					var dismk = td.eq(4).text();
				$("#Savelsn" + lsn).hide();
				 console.log(omr_kind)
				$("#Editlsn" + lsn).show();
				 var form = $('#blankform');
	        	 var formData = new FormData(form[0]);
	        	 formData.append('examcd',examcd);
	        	 formData.append('lsncd',lsn);
	        	 formData.append('omr_kind',omr_kind);
	        	 formData.append('queino',queino);
	        	 formData.append('dismk',dismk);
				$.ajax({
					url: "update_omr_lsn.ai",
					type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				lsnlist(data);
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
        		 }
			}
			function editquei(quei){
				var tr = $("#Editquei" + quei).parent().parent().parent();
				var td = tr.children();
				td.eq(1).attr("contenteditable", true);
				td.eq(2).attr("contenteditable", true);
				td.eq(3).attr("contenteditable", true);
				td.eq(4).attr("contenteditable", true);
				td.eq(5).attr("contenteditable", true);
				$("#Editquei" + quei).hide();
				$("#Savequei" + quei).show();
			}

			function savequei(quei){
				if (confirm("저장 하시겠습니까?")) {
				var tr = $("#Savequei" + quei).parent().parent().parent();
				var td = tr.children();
					td.eq(1).removeAttr("contenteditable", true);
					td.eq(2).removeAttr("contenteditable", true);
					td.eq(3).removeAttr("contenteditable", true); 
					td.eq(4).removeAttr("contenteditable", true);
					td.eq(5).removeAttr("contenteditable", true);
					var queino = td.eq(0).text();
					var cra = td.eq(1).text();
					var dismk = td.eq(2).text();
					var qarea = td.eq(3).text();
					var qdtlarea = td.eq(4).text();
					var qtype = td.eq(5).text();
				$("#Savequei" + quei).hide();
				 
				$("#Editquei" + quei).show();
				 var form = $('#blankform');
	        	 var formData = new FormData(form[0]);
	        	 formData.append('examcd',examcd);
	        	 console.log(lcd)
	        	 formData.append('lsncd',lcd);
	        	 formData.append('queino',queino);
	        	 formData.append('cra',cra);
	        	 formData.append('dismk',dismk);
	        	 formData.append('qarea',qarea);
	        	 formData.append('qdtlarea',qdtlarea);
	        	 formData.append('qtype',qtype);
				$.ajax({
					url: "update_omr_quei.ai",
					type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				lsnlist(data);
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })
        		}
			}
			function mo_div(){
				$(".info").each(function() {
	     	        $(this).hide();
	     	  	})
	     	  	
	     	  $("#moinfo").show();
				var form = $('#blankform');
                var formData = new FormData(form[0]);
                
                formData.append("examCd",examcd)
               $.ajax({
        			url: "mlsnlist.ai",
        			type: "POST",
        			data: formData,
        			processData:false,
        	        contentType:false,
        	        cache:false,
        			dataType : "json",
        			//contentType : "application/json",
        			success: function(data){
        				console.log(data)
        				lsnlist(data)
        		       	},
        		    error : function(e){
        		    	alert("서버오류 : " + e.status);
        		       	} 
        		     })

			}
			function delex(ex){
				 if (confirm("삭제 하시겠습니까?")) {
					 var form = $('#blankform');
		        	 var formData = new FormData(form[0]);        	
		        	    formData.append('examcd',ex)
		        		$.ajax({
		        			url: "delex.ai",
		        			type: "POST",
		        			data: formData,
		        			processData:false,
		        	        contentType:false,
		        	        cache:false,
		        			dataType : "json",
		        			//contentType : "application/json",
		        			success: function(data){
		        				reload()
		        		       	},
		        		    error : function(e){
		        		    	alert("서버오류 : " + e.status);
		        		       	} 
		        		     })
				 }
			}
			function chkex(ex){
				var chk = $('input:checkbox[id="chk'+ex+'"]').is(":checked")
				var form = $('#blankform');
		        	 var formData = new FormData(form[0]);        	
		        	    formData.append('examcd',ex)
				if(chk){
					formData.append('useyn',"Y")
				}else{
	        	    formData.append('useyn',"N")
				}
				$.ajax({
		        	url: "chkex.ai",
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
			 function listdo(page) {
	        		f = document.blankform;
	        		f.pageNum.value=page;
	        		f.submit();
	        	}
			 
			 function limitchg(){
				 var lt = $("#limit option:selected").val();
				//console.log(lt);
				f = document.blankform;
				f.pageNum.value=pageno;
				f.limit.value=lt;
				f.submit();
			}
      </script>
</head>
<body>
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                     <div class="row info" id="moinfo" style="height: 100%;">
                        <div style="width: 49%; height: 900px;">
                           <div class="col-sm-12" style="height: 100%;">
                              <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;"  align="center">
                                 <div style="padding-left: 3%; width: 96%; height: 20px; margin-bottom: 2%" align="left">
                                    <div class="row">
                                    	<div style="width: 50%">
                                    		<h4><b>모의고사 목록</b></h4>
                                    	</div>
                                    	<div style="width: 50%; padding-right: 2.5%;" align="right">
                                    		<button class="btn btn-primary" onclick="newex()"> 신규등록</button>
                                    	</div>
                                    </div>
                                 </div>
                              <div style="width: 95%; height: 850px; padding-bottom: 2%;">
                                 <div class="iq-card" style="height: 100%">
                                    <div class="iq-card-body" style="height: 100%">
                                    <form name="blankform">
                                    	<input type="hidden" name="pageNum" value="1">
                                    	
                                    
                                       <div id="table" class="table no-wrap">
                                          <table class="table no-wrapt">
                                             <thead id="examhead">
                                                <tr>
                                                <th><input type="checkbox"></th>
                                 <th>일시</th>
                                 <th>학년</th>
                                 <th>종류</th>
                                 <th>모의고사명</th>
                                 <th>모의고사코드</th>
                                 <th>수정/복제/삭제</th>
                               </tr>
                             </thead>
                             <tbody>
                             <c:forEach items="${EXAMList}" var="ex">
                               	<tr id ="${ex.EXAM_CD}" class="sub" onclick="disp_div('${ex.EXAM_CD}')">
                               		<td><input type="checkbox"  <c:if test="${ex.USE_YN=='Y'}">checked="checked"</c:if> onchange="chkex('${ex.EXAM_CD}')" id="chk${ex.EXAM_CD}"></td>
                               		<td><input type="date" value="${ex.EXAM_DT}"> </td><!-- ${ex.EXAM_DT} -->
                               		<td>${ex.SCHYR}</td>
                               		<td>${ex.EXAM_KIND}</td>
                               		<td>${ex.EXAM_NM}</td>
                               		<td>${ex.ECI_EXAM_CD}</td>
                               		<td>
                                    <span><button type="button" class="icon Edit" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="수정" onclick="editex('${ex.EXAM_CD}')" id="Editex${ex.EXAM_CD}"><i class="ri-edit-line"></i></button>
                                    	  <button class="icon Save" data-icon="S" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="저장"  id="Saveex${ex.EXAM_CD}" onclick="saveex('${ex.EXAM_CD}')"></button></span>
                                    <span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" onclick="copyex('${ex.EXAM_CD}')"><i class="las la-copy" data-toggle="tooltip" data-placement="top" title="복제"></i></button></span>
                                    <span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" onclick="delex('${ex.EXAM_CD}')"><a href="" data-toggle="tooltip" title="삭제" style="color: black;"><i class="ri-delete-bin-line" aria-hidden="true"></i></a></button></span>
                                 </td>
                                </tr>  
                               </c:forEach>                              
                                             </tbody>
                                          </table>
                                       </div>
                                       <div>
                                       <table id="copies" class="table">
                                       
                                       </table>
                                       </div>
                                       
                                       <div align="right" style="padding-left: 3%">
                              <div class="row">
                                 <div style="width: 30%">
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
                        </div>
                        <div style="width: 50%; height: 100%;" >
                           <div class="col-sm-12" style="height: 900px; padding-left: 0px; padding-right: 0px">
                              <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;"  align="center">
                                 <div style="padding-left: 3%; width: 96%; height: 20px; margin-bottom: 2%" align="left">
                                     <div class="row">
                                    	<div style="width: 50%">
                                    		<h4><b>과목</b></h4>
                                    	</div>
                                    	<div style="width: 50%; padding-right: 2.5%;" align="right">
                                    		<button class="btn btn-primary" id="newlsn" disabled="disabled" onclick="newlsnrow()"> 신규등록</button>
                                    	</div>
                                    </div>
                              	</div>
                              	<div style="height: 850px; padding-bottom: 2%;" >
                                 <div class="iq-card" style="width: 95%; height: 100%;">
                                 	<div class="iq-card-body" style="height: 100%">
                                    	<div id="table" class="table no-wrap" style="height: 100%">
                                       		<table class="table">
                                          		<thead id="lsnhead">
                                             		<tr>
                                 						<th>교시</th>
                                 						<th>과목</th>
                                 						<th>OMR종류</th>
                                 						<th>문항수</th>
                                 						<th>총배점</th>
                                 						<th>문항작성</th>
                                 						<th>수정/복제/삭제</th>
                                 					</tr>
                                 				</thead>
                                 				<tbody id="lsnlist">                                             
                                 				</tbody>
                                          		<tfoot id="newlsnfoot">
                                          		</tfoot>
                                       		</table>
                                    	</div>
                                 	</div>
                              	</div>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div id="questinfo" class="info" style="display: none; width: 100%;" >
                     <div class="iq-card iq-border-radius-20 white" style="width: 100%" align="center">
                         <div class="row" style="width: 100%">
                            <div style="width: 22%; margin-right: 1%;">
                              <h3>1번 샘플 다운로드</h3><br><br>
                              <a style="text-decoration: underline;" href="sample.xlsx" download><button style="width: 60%;" type="button" class="btn btn-primary mb-3" >문항 정보 샘플 </button></a>
                        	</div>
                              <div style="width: 52%; margin-right: 1%;" >
                              <h3>2번 문항정보 업로드</h3>
                              	<form name="uploadForm" id="uploadForm" enctype="multipart/form-data" method="post" >         
                                 	<div id="dropZone" style="background-color: #f0f0f0; border-radius: 20px 20px 20px 20px;" >
                                    	<div align="center">
                                       		<img src="images/drag-icon.jpg" height="120px" style="margin-top: 5px; margin-bottom: 5px;">
                                    	</div>
                                    	<table id="fileListTable" width="100%" border="0px">
                                       		<tbody id="fileTableTbody">
                                       		</tbody>
                                    	</table>
                                 	</div>
                              	</form>
                              </div>
                              <div style="width: 22%; margin-right: 1%;">
                              	<h3>3번 이전 페이지</h3><br><br>
                              		<button style="width: 60%;" type="button" class="btn btn-primary mb-3" onclick="mo_div()" >뒤로 가기</button></a>
                        		</div>
                           </div>
                     	   
                     	   <!-- <div style="height: 10%;">
                     	   					
                     	   
                     	   </div>-->
                     	   <div align="center" style="padding-top: 20px; padding-bottom: 20px;">
                     	   <table class="table" style="width: 97%;">
                     	   <thead>
                     	   <tr><th>문항번호</th><th>정답</th><th>배점</th><th>영역</th><th>세부내용</th><th>유형</th><th>수정/삭제</th></tr>
                     	   	</thead>
                     	   	<tbody id="queibody">
                     	   	
                     	   	</tbody>
                     	   	</table>
                     	   </div>
                        </div>
                     
                  </div>
               </div>
            </div>
         </div>
      </div>
</body>
</html>