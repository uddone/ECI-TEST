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
   
   <script type="text/javascript">
   
   function disp_div(sub) {
      $(".sub").each(function() {
         $(this).removeClass("select");
      })
      $("#" + sub).addClass("select");
   }
</script>


   <script src="http://code.jquery.com/jquery-latest.js"></script>
   
      <script type="text/javascript">
         $(document).ready(function() {
            $("#input_file").bind('change', function() {
               selectFile(this.files);
               //this.files[0].size gets the size of your file.
               //alert(this.files[0].size);
            });
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
               alert("파일이 없습니다.");
               return;
            }
   
            // 용량을 500MB를 넘을 경우 업로드 불가
            if (totalFileSize > maxUploadSize) {
               // 파일 사이즈 초과 경고창
               alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
               return;
            }
   location.href = "omr-manage2.html"  
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
      
      <script type="text/javascript">
         function disp_div(sub) {
      $(".sub").each(function() {
         $(this).removeClass("select");
      })
      $("#" + sub).addClass("select");
   }
   
		
</script>
</head>
<body>
<div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                     <div class="row">
                        <div style="width : 39%;">
                           <div class="iq-card iq-border-radius-20" style="height: 50%; padding-top: 2%;"  align="center">
                        <div style="padding-left: 3%; width: 96%; height: 8%;" align="left">
                           <h4 ><b>모의고사 _ 과목 선택 </b></h4>
                        </div>
                        <div style="width: 95%; height: 87%; padding-top: 2%">                              
                           <div class="iq-card" style="height: 100%;" >
                              <div class="iq-card-body" style="height: 100%;" >
                                 <div class="iq-todo-page" style="height: 100%;">
                                    <form class="position-relative">
                                       <div class="form-group mb-0">
                                          <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                          <a class="search-link" href="#"><i class="ri-search-line"></i></a>
                                       </div>
                                       <br>
                                    </form>
                                    <div class="no-wrap" style="height: 60%;">
                                       <table class="table no-wrapt">
                             <thead>
                               <tr>
                                 <th>지점</th>
                                 <th>모의고사</th>
                                 <th>학년</th>
                                 <th>과목</th>
                               </tr>
                             </thead>
                             <tbody>
                               <tr id="sub1" class="sub" onclick="javascript:disp_div('sub1')">
                                 <td>종로점</td>
                                 <td>2020 10월 평가원 국어영역</td>
                                 <td>고1</td>
                                 <td>국어</td>
                               </tr>  
                               
                               <tr id="sub2" class="sub" onclick="javascript:disp_div('sub2')">
                                 <td>종로점</td>
                                 <td>2020 10월 평가원 국어영역</td>
                                 <td>고1</td>
                                 <td>국어</td>
                               </tr>
                               <tr>
                                 <td></td>
                                 <td></td>
                                 <td></td>
                                 <td></td>
                               </tr>
                               <tr>
                                 <td></td>
                                 <td></td>
                                 <td></td>
                                 <td></td>
                               </tr>
                                                          
                            </tbody>
                                       </table>
                                    </div>

                                    <div align="center">
                                    <nav aria-label="Page navigation example">
                              <ul class="pagination mb-0">
                                 <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                    </a>
                                 </li>
                                 <li class="page-item"><a class="page-link" href="#">1</a></li>
                                 <li class="page-item"><a class="page-link" href="#">2</a></li>
                                 <li class="page-item"><a class="page-link" href="#">3</a></li>
                                 <li class="page-item"><a class="page-link" href="#">4</a></li>
                                 <li class="page-item"><a class="page-link" href="#">5</a></li>
                                 <li class="page-item"><a class="page-link" href="#">6</a></li>
                                 <li class="page-item"><a class="page-link" href="#">7</a></li>
                                 <li class="page-item"><a class="page-link" href="#">8</a></li>
                                 <li class="page-item"><a class="page-link" href="#">9</a></li>
                                 <li class="page-item"><a class="page-link" href="#">10</a></li>
                                 <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                    </a>
                                 </li>
                              </ul>
                           </nav>
                           </div>
                                 </div>   
                              </div>
                           </div>
                        </div>
                     </div>
                           <div class="iq-card iq-border-radius-20" style="height: 47%; padding-top: 2%;"  align="center">
                        <div style="padding-left: 3%; width: 96%; height: 8%; margin-bottom: 2%" align="left">
                           <h4 ><b>OMR 카드 업로드 </b></h4>
                        </div>
                        <button style="display: none;" onclick="uploadFile(); return false;" class="btn bg_01 btn btn-primary mb-3"> 파일 업로드 </button>
                        <div class="upload-btn-wrapper">
                           <input type="file" id="input_file" multiple="multiple" style="height: 100%;"/>  
                        </div>
                        <form name="uploadForm" id="uploadForm" enctype="multipart/form-data" method="post">         
                           <div id="dropZone" style="width: 100%; height: 87%;" >
                              <div align="center">
                                 <a data-toggle="modal" data-target="#exampleModal"><img src="images/file_upload.png" height="150px" style="margin-top: 50px;"></a>
                              </div>
                              <table id="fileListTable" width="100%" border="0px">
                                 <tbody id="fileTableTbody">
                                 </tbody>
                              </table>
                           </div>   
                        </form>
                           <!-- Modal -->
                                 <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document" style="height: 200px; margin-top: 500px; margin-left: 200px;">
                                       <div class="iq-card iq-border-radius-20" style="height: 100%; background-color: white;">
                                          <div style="height: 100%">
                                             <div style="height: 5%;">
                                             </div>
                                             <div align="center" style="height: 50%; margin-top: 5%;">
                                                <h4>등록을 시작 할 파일 수는 아래와 같습니다.</h4>
                                                <h4>진행하시겠습니까?</h4>
                                                <h4>이미지 파일 : 499 / PDF : 1 </h4>
                                             </div>
                                             <div align="center" style="height: 30%">
                                                <div class="row">
                                                   <div style="width: 50%;">
                                                      <button type="button" class="btn btn-primary" onclick="javascript:location.href='loading.html'" style="width: 60%;"><h4 style="color: white;"><b>예</b></h4></button>
                                                   </div>
                                                   <div align="right" style="width: 40%; ">
                                                      <button type="button" class="btn iq-bg-danger" style="width: 75%;" data-dismiss="modal"><h4 style="color: #f36157;"><b>아니요</b></h4></button>
                                                   </div>
                                                </div>
                                             </div>
                                             <div style="height: 15%;">
                                             </div>
                                          </div>
                                       </div>
                                    </div>
                                 </div>
                                 <div class="modal fade" id="RegistResult" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document" style="height: 80%; margin-top: 8%; margin-left: 50%; ">
                                       <div class="iq-card iq-border-radius-20" style="height: 100%; background-color: white;">
                                          <div style="height: 100%">
                                             <div style="height: 30%;">
                                             </div>
                                             <div align="center" style="height: 50%; margin-top: 5%;">
                                                <h4>총 500건 등록</h4>
                                                <h4>- 정상 등록 : 498</h4>
                                                <h4>- 확인 필요 : &nbsp;&nbsp;&nbsp;&nbsp;2</h4>
                                             </div>
                                             <div align="center" style="height: 10%">
                                                <div class="row">
                                                   <div style="width: 50%;">
                                                   </div>
                                                   <div align="right" style="width: 40%; ">
                                                      <button type="button" class="btn btn-primary"  style="width: 75%;"><h4 style="color: white;"><b>확인</b></h4></button>
                                                   </div>
                                                </div>
                                             </div>
                                             <div style="height: 10%;">
                                             </div>
                                          </div>
                                       </div>
                                    </div>
                                 </div>
                     </div>
                  </div>
                        <div style="width : 59%; margin-left: 1%;">
                           <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 1%; padding-bottom: 2%"  align="center">
                              <div style="padding-left: 3%; width: 96%; height: 5%; margin-bottom: 1%" align="left">
                                 <h4 onclick="javascript:location.href='OMR_RECOG2.html'"><b>인식 내역 확인 </b></h4>
                              </div>
                            <div style="width: 95%; height: 90%;">
                           <div class="iq-card" style="width: 100%; height: 100%;" >
                              <div class="iq-card-body" style="width: 100%; height: 100%;" >
                                 <div class="iq-todo-page" style="height: 100%;">
                                    <form class="position-relative">
                                       <div class="form-group mb-0" style="padding-bottom: 1%;">
                                          <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                          <a class="search-link" href="OMR_RECOG3.html"><i class="ri-search-line"></i></a>
                                       </div>
                                    </form>
                                 <div class="no-wrap">
                                  <table class="table no-wrapt">
                                   <thead>
                                    <tr>
                                     <th>No</th>
                                     <th>수험번호</th>
                                     <th>과목</th>
                                     <th>성명</th>
                                     <th>상태</th>                                 
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr style="background-color: #fcf2f1">
                                     <td>1</td>
                                     <td>021***</td>
                                     <td>국어</td>
                                     <td>김**</td>
                                     <td><button class="btn btn-danger rounded-pill mb-3">WARNING</button></td>
                                    </tr>  
                                    <tr style="background-color: #fcf2f1">
                                     <td>2</td>
                                     <td>921***</td>
                                     <td>국어</td>
                                     <td>박**</td>
                                     <td><button class="btn btn-danger rounded-pill mb-3">WARNING</button></td>
                                    </tr>
                                    <tr>
                                     <td>3</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>
                                    <tr>
                                     <td>4</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>
                                    <tr>
                                     <td>5</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>
                                    <tr>
                                     <td>6</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>
                                    <tr>
                                     <td>7</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td>></td>
                                    </tr>
                                    <tr>
                                     <td>8</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>
                                    <tr>
                                     <td>9</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>
                                    <tr>
                                     <td>10</td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                     <td></td>
                                    </tr>                           
                                  </tbody>
                                 </table>
                               </div>
                               <div align="center" style="padding-left: 15%;">
                                    <nav aria-label="Page navigation example">
                              <ul class="pagination mb-0">
                                 <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                    </a>
                                 </li>
                                 <li class="page-item"><a class="page-link" href="#">1</a></li>
                                 <li class="page-item"><a class="page-link" href="#">2</a></li>
                                 <li class="page-item"><a class="page-link" href="#">3</a></li>
                                 <li class="page-item"><a class="page-link" href="#">4</a></li>
                                 <li class="page-item"><a class="page-link" href="#">5</a></li>
                                 <li class="page-item"><a class="page-link" href="#">6</a></li>
                                 <li class="page-item"><a class="page-link" href="#">7</a></li>
                                 <li class="page-item"><a class="page-link" href="#">8</a></li>
                                 <li class="page-item"><a class="page-link" href="#">9</a></li>
                                 <li class="page-item"><a class="page-link" href="#">10</a></li>
                                 <li class="page-item">
                                    <a class="page-link" href="#" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                    </a>
                                 </li>
                              </ul>
                           </nav>
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