<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OCR 유형 관리</title>
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
</style>
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$(".Edit").each(function() {
        $(this).show();
     })
      $(".hidediv").each(function() {
     	        $(this).hide();
     	  })
     	     $("#upload_div").show();
        	 
        	 $("#input_file").bind('change', function() {
               selectFile(this.files);
               //this.files[0].size gets the size of your file.
               //alert(this.files[0].size);
            });
	 if("${param.limit}" != ""){
	     	$("#limit").val("${param.limit}").prop("selected", true);
	     	console.log(${param.limit})
	     }else{
	     	$("#limit").val("5").prop("selected", true);
	     }	 
 })
 var examcd = "";
 var rpageNum = 1;
 var rlimit = 5;
 var pageno =1; 
 
function edit(ex){
	var tr = $("#Edit" + ex).parent().parent().parent();
	var td = tr.children();
	td.eq(0).attr("contenteditable", true);
	td.eq(1).attr("contenteditable", true);
	td.eq(2).attr("contenteditable", true);
		  
	
	$("#Edit" + ex).hide();
	 
	$("#Save" + ex).show();
}

function save(ex){
	var tr = $("#Save" + ex).parent().parent().parent();
	var td = tr.children();
		td.eq(0).removeAttr("contenteditable", true); 
		td.eq(1).removeAttr("contenteditable", true);
		td.eq(2).removeAttr("contenteditable", true);
		var exam_kind = td.eq(0).text();
		var exam_nm = td.eq(1).text();
		var schyr = td.eq(2).text();
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
			drawtable(data)
			//$("#status").html(data);
	       	},
	    error : function(e){
	    	alert("서버오류 : " + e.status);
	       	} 
	     })
}
function listdo(page) {
	f = document.searchform;
	f.pageNum.value=page;
	pageno=page;
	f.submit();
}
function limitchg(){
	 var lt = $("#limit option:selected").val();
	console.log(lt);
	f = document.searchform;
	f.pageNum.value=pageno;
	f.limit.value=lt;
	f.submit();
}
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
         
         var examcd = "";
         
         function disp_div(sub) {
        	 $('#fileuploadbtn').removeAttr('disabled');
             $(".sub").each(function() {
                $(this).removeClass("select");
             })
             $("#" + sub).addClass("select");
             examcd = ""+sub;
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
         }
   
         // 업로드 파일 목록 생성
         function addFileList(fIndex, fileName, fileSizeStr) {
            /* if (fileSize.match("^0")) {
               alert("start 0");
            } */
   
            var html = fIndex+1+"개의 파일 업로드 대기중";
            $('#fileTableTbody').text(html);
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

                $.ajax({
                    url:"s3upload.ai",
                    data:formData,
                    type:'POST',
                    processData:false,
                    contentType:false,
                    dataType:'json',
                    cache:false,
                    success:function(result){
                        drawtable(result)
                    }
                });
                fileList=[];
                $("#upload_div").hide();
                $("#recog_div").show();
            }
        }
         function drawtable(result){
        	 var a = result[0].filecnt+'개의 파일 업로드 완료!!'
        	 $('#filecnt').html(a);
         }
      </script>
</head>
<body>
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                     <div class="row" style="height: 100%;">
                     <div style="width: 38%; height: 100%;">
                      <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                    <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                        <div class="row" style="width: 98%"> 
                           <div style="width: 48%; padding-left: 2%">
                              <h4><b>표 선택</b></h4>
                           </div>
                           <div style="width: 50%" align="right">
                           </div>
                        </div>
                     </div>
                     <div class="iq-card" style="width: 95%; height: 87%;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 99%;">
                           <form class="position-relative" name="searchform" id="searchform" action="OCR_EXAM_ADM.ai" method="post">
                              <div class="form-group mb-0">
                                 <input type="hidden" name="pageNum" value="1">
                                 <input type="text" class="form-control todo-search" placeholder="검색"  name="searchcontent" value="${param.searchcontent }">
                                 <a class="search-link" onclick="document.getElementById('searchform').submit()"><i class="ri-search-line"></i></a>
                              </div>
                              <br>
                          
                           <div>
                           <table class="table table-bordered table-responsive-md text-center" style="width: 100%">
                             <thead>
                               <tr>
                                 <th>종류</th>
                                 <th>시험</th>
                                 <th>학년</th>
                                 <th>수정/삭제</th>
                               </tr>
                             </thead>
                             <tbody>
                               <c:forEach items="${EXAMList}" var="ex">
                               	<tr id ="${ex.EXAM_CD}" class="sub" onclick="disp_div('${ex.EXAM_CD}')">
                               		<td>${ex.EXAM_KIND}</td>
                               		<td>${ex.EXAM_NM}</td>
                               		<td>${ex.SCHYR}</td>
                               		<td>  
                                    <span><button class="icon Edit" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" id="Edit${ex.EXAM_CD}" onclick="edit('${ex.EXAM_CD}')">
                                    
                                    <i class="ri-edit-line"></i>
                                    </button>
                                     <button class="icon Save" data-icon="S" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" id="Save${ex.EXAM_CD}" onclick="save('${ex.EXAM_CD}')"></button>
                                     </span>
                                    <span class="table-remove"><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );" data-toggle="modal" data-target="#deleteModal"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span>
                                    </td>
                                </tr>  
                               </c:forEach>
                             </tbody>
                           </table>
                           </div>
                           <div align="right" style="padding-left: 3%">
                              <div class="row">
                                 <div style="width: 40%">
                                    한 화면에 볼 글 갯수 
                                    <span>
                                    <select id="limit" name = "limit" onchange="limitchg()">
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="15">15</option>
                                    </select>
                                    </span>
                                 </div>
                                 <div style="width: 30%">
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
               <div style="width: 60%; height: 100%; margin-left: 1%;" id="upload_div" class="hidediv">
                         <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 6%; margin-bottom: 1%">
                                 <div class="row">
                                    <div style="width: 50%; margin-left: 2%"><h4><b>성적표 양식 등록</b></h4></div>
                                    <div align="right" style="width: 42%;">
                                       <button onclick="uploadFile(); return false;" disabled="disabled" class="btn bg_01 btn btn-primary mb-3" id="fileuploadbtn"> 파일 업로드 </button>
                                    </div>
                                 </div>
                              </div>
                              <div style="width: 97%; height: 90%; padding-bottom: 1%;">
                                 <div style="height: 100%; background-color: white; border-radius: 10px;" >
                                  
                                    <form name="uploadForm" id="uploadForm" enctype="multipart/form-data" method="post" style="height: 100%">
        								<table class="table" width="100%" height="100%" border="1px">
            								<tbody id = "uploadzone">
            								<tr><th id="reupload" style="height: 20px"></th></tr>
                								<tr style="height: 80%;">
                    								<td id="dropZone" align="center">
                        								<img src="images/drag-icon.jpg" style="width: 25%; height: 25%; margin-top: 7%;"  >
                    								</td>
                								</tr>
                								<tr id="fileTableTbody"></tr>
            								</tbody>
        								</table>
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
</body>
</html>