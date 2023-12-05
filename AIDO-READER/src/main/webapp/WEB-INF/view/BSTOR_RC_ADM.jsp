<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
   <script src="http://code.jquery.com/jquery-latest.js"></script>
<!-- 
<script type="text/javascript">
 
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
 
    $(function (){
        // 파일 드롭 다운
        fileDropDown();
    });
 
    // 파일 드롭 다운
    function fileDropDown(){
        var dropZone = $("#dropZone");
        //Drag기능 
        dropZone.on('dragenter',function(e){
            e.stopPropagation();
            e.preventDefault();
            // 드롭다운 영역 css
            dropZone.css('background-color','#E3F2FC');
        });
        dropZone.on('dragleave',function(e){
            e.stopPropagation();
            e.preventDefault();
            // 드롭다운 영역 css
            dropZone.css('background-color','#FFFFFF');
        });
        dropZone.on('dragover',function(e){
            e.stopPropagation();
            e.preventDefault();
            // 드롭다운 영역 css
            dropZone.css('background-color','#E3F2FC');
        });
        dropZone.on('drop',function(e){
            e.preventDefault();
            // 드롭다운 영역 css
            dropZone.css('background-color','#FFFFFF');
            
            var files = e.originalEvent.dataTransfer.files;
            if(files != null){
                if(files.length < 1){
                    alert("폴더 업로드 불가");
                    return;
                }
                selectFile(files)
            }else{
                alert("ERROR");
            }
        });
    }
 
    // 파일 선택시
    function selectFile(files){
        // 다중파일 등록
        if(files != null){
            for(var i = 0; i < files.length; i++){
                // 파일 이름
                var fileName = files[i].name;
                var fileNameArr = fileName.split("\.");
                // 확장자
                var ext = fileNameArr[fileNameArr.length - 1];
                // 파일 사이즈(단위 :MB)
                var fileSize = files[i].size / 1024 / 1024;
                
                if($.inArray(ext, ['exe', 'bat', 'sh', 'java', 'jsp', 'html', 'js', 'css', 'xml']) >= 0){
                    // 확장자 체크
                    alert("등록 불가 확장자");
                    break;
                }else if(fileSize > uploadSize){
                    // 파일 사이즈 체크
                    alert("용량 초과\n업로드 가능 용량 : " + uploadSize + " MB");
                    break;
                }else{
                    // 전체 파일 사이즈
                    totalFileSize += fileSize;
                    
                    // 파일 배열에 넣기
                    fileList[fileIndex] = files[i];
                    
                    // 파일 사이즈 배열에 넣기
                    fileSizeList[fileIndex] = fileSize;
 
                    // 업로드 파일 목록 생성
                    addFileList(fileIndex, fileName, fileSize);
 
                    // 파일 번호 증가
                    fileIndex++;
                }
            }
        }else{
            alert("ERROR");
        }
    }
 
    // 업로드 파일 목록 생성
    function addFileList(fIndex, fileName, fileSize){
        var html = "";
        html += "<tr id='fileTr_" + fIndex + "'>";
        html += "    <td class='left' >";
        html +=         fileName + " / " + fileSize + "MB "  + "<a href='#' onclick='deleteFile(" + fIndex + "); return false;' class='btn small bg_02'>삭제</a>"
        html += "    </td>"
        html += "</tr>"
 
        $('#fileTableTbody').append(html);
    }
 
    // 업로드 파일 삭제
    function deleteFile(fIndex){
        // 전체 파일 사이즈 수정
        totalFileSize -= fileSizeList[fIndex];
        
        // 파일 배열에서 삭제
        delete fileList[fIndex];
        
        // 파일 사이즈 배열 삭제
        delete fileSizeList[fIndex];
        
        // 업로드 파일 테이블 목록에서 삭제
        $("#fileTr_" + fIndex).remove();
    }
 
    // 파일 등록
    function uploadFile(){
        // 등록할 파일 리스트
        var uploadFileList = Object.keys(fileList);
 
        // 파일이 있는지 체크
        if(uploadFileList.length == 0){
            // 파일등록 경고창
            alert("파일이 없습니다.");
            return;
        }
        
        // 용량을 500MB를 넘을 경우 업로드 불가
        if(totalFileSize > maxUploadSize){
            // 파일 사이즈 초과 경고창
            alert("총 용량 초과\n총 업로드 가능 용량 : " + maxUploadSize + " MB");
            return;
        }
            
        if(confirm("등록 하시겠습니까?")){
            // 등록할 파일 리스트를 formData로 데이터 입력
            var form = $('#uploadForm');
            var formData = new FormData(form[0]);
            for(var i = 0; i < uploadFileList.length; i++){
                formData.append('files', fileList[uploadFileList[i]]);
            }
            
            $.ajax({
                url:"s3upload.ai",
                data:{formData,},
                type:'POST',
                processData:false,
                contentType:false,
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
</script> -->
 
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
                        <h4><b>OMR 카드 선택</b></h4>
                      </div>
                     <div class="iq-card" style="width: 95%; height: 90%; margin-bottom: 1%;" >
                     <div class="iq-card-body" style="width: 100%; height: 100%;" >
                        <div class="iq-todo-page" style="height: 99%;">
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
                                 <th>종류</th>
                                 <th>모의고사</th>
                                 <th>학년</th>
                               </tr>
                             </thead>
                             <tbody>
                               <tr>
                                 <td>평가원</td>
                                 <td>평가원 10월</td>
                                 <td>고1</td>
                               </tr>  
                               <tr>
                                 <td contenteditable="true">평가원</td>
                                 <td contenteditable="true">평가원 10월</td>
                                 <td contenteditable="true">고1</td>
                                 
                               </tr>
                               <tr>
                                 <td contenteditable="true">평가원</td>
                                 <td contenteditable="true">평가원 10월</td>
                                 <td contenteditable="true">고1</td>
                                 
                               </tr>
                               <tr>
                                 <td >평가원</td>
                                 <td >평가원 10월</td>
                                 <td >고1</td>
                                 
                               </tr>
                                <tr>
                                 <td >평가원</td>
                                 <td >평가원 10월</td>
                                 <td >고1</td>
                                 
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
                                   	 			<input type="hidden" name="pageNum" value="${param.rpageNum }">
                                     			<input type="text" class="form-control todo-search" placeholder="검색어를 입력하세요"  id="rsearchcontent" name="rsearchcontent" value="${param.rsearchcontent }" style="margin-left : 10px;">
                                   	 		</th>
                                   	 		<th style="border: none; width: 10%; padding-top: 15px;"> 
                                   	 			<button onclick="rsearch()" class="btn bg_01 btn btn-primary mb-3">검색</button>
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
                                 <th>상태</th>
                               </tr>
                             </thead>
                             <tbody>
                               <tr>
                                 <td>11111</td>
                                 <td><button class="btn btn-danger rounded-pill mb-3">Error</button></td>
                               </tr>  
                               <tr>
                                 <td>11111</td>
                                 <td><button class="btn btn-danger rounded-pill mb-3">Error</button></td>
                               </tr>  
                               <tr>
                                 <td>11111</td>
                                 <td><button class="btn btn-success rounded-pill mb-3">Success</button></td>
                               </tr>  
                               <tr>
                                 <td>11111</td>
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
                                 <h4><b>결과 이미지 확인</b></h4>
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
                           		<div style="height: 100%; background-color: white; border-radius: 10px;" >
                              	  <img src="images/exscore.png" width="100%">	
                           		</div>
                        	   </div>
                           </div>

                           <div class="iq-card iq-border-radius-20" style="height: 49%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                 <div class="row">
                                    <div style="width: 50%; margin-left: 2%"><h4><b>OMR 카드 등록</b></h4></div>
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
                                 <div style="height: 100%; background-color: white; border-radius: 10px;" >
                                     <img src="images/exocr.png" width="100%">  
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