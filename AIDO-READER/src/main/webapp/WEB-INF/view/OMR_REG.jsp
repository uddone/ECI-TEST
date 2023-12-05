<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OMR 카드 등록</title>
<style>
td,th,table,tr{border: 1px solid #dee2e6; text-align: center;}
    
	
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
            $("#input_file").bind('change', function() {
               selectFile(this.files);
               //this.files[0].size gets the size of your file.
               //alert(this.files[0].size);
            });
            $(".Edit").each(function() {
        		$(this).show();
        	})
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
   
            var html = "";
            html += "<tr id='fileTr_" + fIndex + "'>";
            html += "    <td id='dropZone' class='left' >";
            html += fileName + " (" + fileSizeStr +") " 
                  //+ "<a href='#' onclick='deleteFile(" + fIndex + "); return false;' class='btn small bg_02'> 삭제</a>"
                  
                  + "<input value='삭제' type='button' href='#' onclick='deleteFile(" + fIndex + "); return false;'>"
            html += "    </td>"
            html += "</tr>"
   
            $('#fileTableTbody').append(html);
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
                location.href = "OMR_REG2.ai"  
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
               var form = $('#uploadForm');
               var formData = new FormData(form);
               for (var i = 0; i < uploadFileList.length; i++) {
                  formData.append('files', fileList[uploadFileList[i]]);
               }
   /*
               $.ajax({
                  url : "업로드 경로",
                  data : formData,
                  type : 'POST',
                  enctype : 'multipart/form-data',
                  processData : false,
                  contentType : false,
                  dataType : 'json',
                  cache : false,
                  success : function(result) {
                     if (result.data.length > 0) {
                        alert("성공");
                        location.reload();
                     } else {
                        alert("실패");
                        location.reload();
                     }
                  }
               });*/
            }
            
         }
      </script>
</head>
<body>
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                     <div class="row" style="height: 100%;">
                     <div style="width: 43%; height: 100%;">
                      <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                    <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                        <h4><b>OMR 카드 선택</b></h4>
                      </div>
                     <div class="iq-card" style="width: 95%; height: 87%;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 99%;">
                           <form class="position-relative">
                              <div class="form-group mb-0">
                                 <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                 <a class="search-link" href="#"><i class="ri-search-line"></i></a>
                              </div>
                              <br>
                           <div>
                           <table class="table" style="width: 100%">
                             <thead>
                               <tr>
                                 <th>등록일시</th>
                                 <th>OMR카드 명</th>
                                 <th>수정/삭제</th>
                               </tr>
                             </thead>
                            <tbody>
                               <c:forEach items="${OMRLIST}" var="omr">
                               	<tr id ="${omr.OMR_MST_CD}" class="sub" onclick="disp_div('${omr.OMR_MST_CD}')">
                               		<td><fmt:formatDate value="${omr.LOAD_DTM}" pattern="yyyy-MM-dd"/></td>
                               		<td>${omr.OMR_NM}</td>
                               		<td>
                               		<span><button type="button" class="icon Edit" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="수정" onclick="editex('${ex.EXAM_CD}')" id="Editex${ex.EXAM_CD}"><i class="ri-edit-line"></i></button>
                                    	  <button class="icon Save" data-icon="S" type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 ); display: none;" data-toggle="tooltip" data-placement="top" title="저장"  id="Saveex${ex.EXAM_CD}" onclick="saveex('${ex.EXAM_CD}')"></button></span>
                                    <span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><a href="" data-toggle="tooltip" title="삭제" style="color: black;"><i class="ri-delete-bin-line" aria-hidden="true"></i></a></button></span>
                               		</td>
                                </tr>  
                               </c:forEach>
                            </tbody>
                           </table>
                           </div>
                           <div align="right" style="padding-left: 3%; height: 40px;">
                              <div class="row" style="height:100%;">
                                 <div style="width: 40%; height: 100%; padding-top: 10px;">
                                    한 화면에 볼 글 갯수  
                                    <script type="text/javascript">
                                    $("#limit").val("${param.limit}").prop("selected", true);
                                    </script>
                                    <select id="limit" name = "limit" onchange="limitchg()">
                                    <option value="5">5</option>
                                    <option value="10">10</option>
                                    <option value="15">15</option>
                                    </select>
                                 </div>
                                 <div style="width: 55%; height: 100%;" align="center">
                                  <c:if test="${pageNum > 1}">
						<a href="javascript:listdo('${pageNum-1 }')" class="iq-icons-list">[이전]</a>
					</c:if>
					<c:if test="${pageNum <= 1}">
						[이전]
					</c:if>
					<c:forEach var="a" begin="${startpage }" end="${endpage }">
						<c:if test="${a == pageNum }">[${a }]</c:if>
						<c:if test="${a != pageNum }">
							<a href="javascript:listdo('${a }')">[${a }]</a>
						</c:if>
					</c:forEach>
					<c:if test="${pageNum < maxpage }">
						<a class="iq-icons-list" href="javascript:listdo('${pageNum+1 }')">[다음]</a>
					</c:if>
					<c:if test="${pageNum >= maxpage }">[다음]</c:if>                                          
                                 </div>
                              </div> 
                           </div>
                            </form>
                        </div>
                     </div>
                  </div>
                  </div>
               </div>
                        <div style="width: 55%; height: 100%; margin-left: 1%">
                         <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                 <div class="row">
                                    <div style="width: 50%; margin-left: 2%"><h4><b>OMR 카드 등록</b></h4></div>
                                    <div align="right" style="width: 42%;">
                                       <button onclick="uploadFile(); return false;" class="btn bg_01 btn btn-primary mb-3"> 파일 업로드 </button>
                                       <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#printModal">출력</button>
                                       <button class="btn btn-primary mb-3">내려 받기</button>
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
                                 			
                        	   <div style="width: 95%; height: 87%;">
                           		<div style="height: 100%; background-color: white; border-radius: 10px;" >
                              	 
                              		<div class="upload-btn-wrapper  ">
                                 		<input type="file" id="input_file" multiple="multiple" style="height: 100%;" />  
                              		</div>
                              		<form name="uploadForm" id="uploadForm" enctype="multipart/form-data" method="post">         
                                 	<div id="dropZone" style="width: 90%; height: 100%;" >
                                    	<div align="center">
                                       		<img src="images/file_upload.png" height="200px" style="margin-top: 150px;">
                                    	</div>
                                    	<table id="fileListTable" width="100%" border="0px">
                                       		<tbody id="fileTableTbody">
                                       		</tbody>
                                    	</table>
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
</body>
</html>