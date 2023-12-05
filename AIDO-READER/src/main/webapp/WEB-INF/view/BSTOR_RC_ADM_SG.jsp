<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
                location.href = "BSTOR_RC_REG2.html"  
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
               var uploading = ""
               uploading +="<tr style='height: 90%;'> <td id='dropZone' style='padding-top:13%;'><img src='images/iconmonstr-note.png' width='15%'><img src='images/iconmonstr-arrow.png' width='5%'><img src='images/iconmonstr-idea.png' width='15%'><br><br><h3>"+fileIndex+"개의 파일 업로드 완료!!</h3><h3>OCR 인식중</h3></td></tr>";
                $('#uploadzone').html(uploading);

                $.ajax({
                    url:"s3upload.ai",
                    data:formData,
                    type:'POST',
                    processData:false,
                    contentType:false,
                    dataType:'json',
                    cache:false,
                    success:function(result){
                        if(result.data.length > 0){
                            alert("성공");
                            location.reload();
                        }else{
                            alert("실패");
                            location.reload();
                        }
                    }
                });
            }
        }
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
                     <div class="row" style="height: 100%;">
                     <div style="width: 28%; height: 100%;">
                      <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                    <div align="left" style="padding-left: 3%; width: 100%; height: 6%; margin-bottom: 1%">
                        <h4><b>지점별 성적표 선택</b></h4>
                      </div>
                     <div class="iq-card" style="width: 95%; height: 90%; margin-bottom: 1%;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 99%;">
                           <div style="padding-top: 10px; padding-bottom: 10px;" align="left">
                                 <h5>모의고사 선택<span> <select>
                                    <option>2020 10월 이투스 모의고사</option>
                                    <option>2020 9월 이투스 모의고사</option>
                                    <option>2020 8월 이투스 모의고사</option>
                                    <option>2020 7월 이투스 모의고사</option>
                                 </select> </span></h5>
                              </div>

                           <form class="position-relative">
                              <div class="form-group mb-0">
                                 <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                 <a class="search-link" href="#"><i class="ri-search-line"></i></a>
                              </div>
                           </form>
                           <br>
                           <div>
                           <table class="table table-bordered table-responsive-md table-striped text-center" style="width: 100%">
                             <thead>
                               <tr>
                                 <th>지점명</th>
                                 <th>학생수</th>
                               </tr>
                             </thead>
                             <tbody>
                               <tr>
                                 <td>종로점</td>
                                 <td>200</td>
                               </tr>  
                               <tr>
                                 <td>목동점</td>
                                 <td>150</td>
                               </tr>  
                               <tr>
                                 <td>강남점</td>
                                 <td>200</td>
                               </tr>  
                               <tr>
                                 <td>의정부점</td>
                                 <td>50</td>
                               </tr>  
                               <tr>
                                 <td>양재점</td>
                                 <td>70</td>
                               </tr>  
                            </tbody>
                           </table>
                           </div>
                           <div style="padding-left: 3%">
                              
                                 <div style="width: 100%" align="left">
                                    한 화면에 볼 글 갯수 
                                    <span><select>
                                       <option>5</option>
                                       <option>10</option>
                                       <option>15</option>
                                    </select>
                                    </span>
                                 </div>
                                 <div class="row" style="padding-top: 5%;">
                                    <div style="width: 20%;"></div>
                                    <div style="width: 40%">
                                       1 ~ 5번 글 13개의 글                                          
                                    </div>
                                 <div style="width: 40%">
                                    <a class="iq-icons-list" href="#" ><i class="las la-angle-left"></i></a>
                                    <a class="iq-icons-list" href="#" ><i class="las la-angle-right"></i></a>
                                 </div>
                              </div> 
                           </div>
                        </div>
                     </div>
                  </div>
                  </div>
               </div>

               <div style="width: 28%; height: 100%; margin-left: 1%">
                  <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                     <div align="left" style="padding-left: 3%; width: 100%; height: 6%; margin-bottom: 1%">
                         <h4><b>내역 확인</b></h4>
                     </div>
                     <div class="iq-card" style="width: 95%; height: 90%; margin-bottom: 1%;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 100%;">
                           <form class="position-relative">
                              <div class="form-group mb-0">
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
                              <br>
                           </form>
                           <div>
                           <table class="table table-bordered table-responsive-md table-striped text-center" style="width: 100%">
                             <thead>
                               <tr>
                                 <th>수험번호</th>
                                 <th>이름</th>
                                 <th>상태</th>
                               </tr>
                             </thead>
                             <tbody>
                               <tr>
                                 <td>310930516</td>
                                 <td>조**</td>
                                 <td><button class="btn btn-danger rounded-pill mb-3">Error</button></td>
                               </tr>  
                               <tr>
                                 <td>318764875</td>
                                 <td>이**</td>
                                 <td><button class="btn btn-danger rounded-pill mb-3">Error</button></td>
                               </tr>  
                               <tr>
                                 <td>384566781</td>
                                 <td>김**</td>
                                 <td><button class="btn btn-success rounded-pill mb-3">Success</button></td>
                               </tr>  
                               <tr>
                                 <td>348868273</td>
                                 <td>박**</td>
                                 <td><button class="btn btn-success rounded-pill mb-3">Success</button></td>
                               </tr>  
                               
                            </tbody>
                           </table>
                           </div>
                           <div style="padding-left: 3%">
                              
                                 <div style="width: 100%" align="left">
                                    한 화면에 볼 글 갯수 
                                    <span><select>
                                       <option>5</option>
                                       <option>10</option>
                                       <option>15</option>
                                    </select>
                                    </span>
                                 </div>
                                 <div class="row" style="padding-top: 5%;">
                                    <div style="width: 20%;"></div>
                                    <div style="width: 40%">
                                       1 ~ 5번 글 13개의 글                                          
                                    </div>
                                 <div style="width: 40%">
                                    <a class="iq-icons-list" href="#" ><i class="las la-angle-left"></i></a>
                                    <a class="iq-icons-list" href="#" ><i class="las la-angle-right"></i></a>
                                 </div>
                              </div> 
                           </div>
                        </div>
                     </div>
                  </div>
                  </div>
               </div>
                  


                        <div style="width: 41%; height: 100%; margin-left: 1%">
                         <div class="iq-card iq-border-radius-20" style="height: 49%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                 <h4><b>OCR 결과 확인</b></h4>
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
                           		<div style="height: 100%; background-color: white; border-radius: 10px; padding-top: 10%;" >
                              	  <img src="images/ocr_summary2.png" width="100%">  
                           		</div>
                        	   </div>
                           </div>

                           <div class="iq-card iq-border-radius-20" style="height: 49%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                 <div class="row">
                                    <div style="width: 50%; margin-left: 2%"><h4><b>OCR 이미지</b></h4></div>
                                    <div align="right" style="width: 42%;">
                                       <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#printModal">저장</button>
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
                                                   <h4>저장하시겠습니까?</h4>
                                                </div>
                                                <div align="center" style="height: 30%">
                                                   <div class="row">
                                                      <div style="width: 50%;">
                                                         <button type="button" class="btn btn-primary"style="width: 60%;"><h4 style="color: white;"><b>저장</b></h4></button>
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
                                 <div style="height: 100%; background-color: white; border-radius: 10px; padding-top: 10%;" >
                                     <img src="images/ocr_summary.png" width="100%"> 
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