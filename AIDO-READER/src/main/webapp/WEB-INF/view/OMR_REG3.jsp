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
    
   
   .white{background-color: white;}
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
   
     <script> 
           function win_open(page) {
            var op ="width=450, height=255, left=50, top=150";
            open(page+".html","",op);
      }
      </script>
      <script type="text/javascript">
   $(document).ready(function() {
      $("#omrinfo").show();  //회원정보는 보이도록
      $("#xyinfo").hide();  //주문내역은 안보이도록
      /*$(".saleLine").each(function() {
         $(this).hide();
      })*/
   })
   
   function disp_div(id) {
      $(".info").each(function() {
         $(this).hide();
      })
      $("#" + id).show();
   }
</script>
</head>
<body>
<!-- Page Content  -->
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
                        <div class="iq-todo-page" style="height: 100%;">
                           <form class="position-relative">
                              <div class="form-group mb-0">
                                 <input type="text" class="form-control todo-search" id="exampleInputEmail002"  placeholder="검색">
                                 <a class="search-link" href="#"><i class="ri-search-line"></i></a>
                              </div>
                              <br>
                           </form>
                           <div>
                           <table class="table" style="width: 100%">
                             <thead>
                               <tr>
                                 <th>등록일시</th>
                                 <th>OMR카드 명</th>
                                 <th>Action</th>
                               </tr>
                             </thead>
                             <tbody>
                               <tr>
                                 <td>2020.10.21</td>
                                 <td>2020 10월 평가원 국어영역</td>
                                 <td>  
                                    <span class="table-remove"><button type="button"
                                      style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-edit-line"></i></button></span>

                                    <span class="table-remove"><button type="button"
                                       style="border: none; background-color:rgba( 255, 255, 255, 0 );" data-toggle="modal" data-target="#deleteModal"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span>
                                    

                                    <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document" style="height: 200px">
                                       <div class="iq-card iq-border-radius-20 white" style="height: 100%">
                                          <div style="height: 100%">
                                             <div style="height: 10%;">
                                             </div>
                                             <div align="center" style="height: 40%; margin-top: 5%;">
                                                <h4>삭제하시겠습니까?</h4>
                                             </div>
                                             <div align="center" style="height: 30%">
                                                <div class="row">
                                                   <div style="width: 50%;">
                                                      <button type="button" class="btn btn-primary"style="width: 60%;"><h4 style="color: white;"><b>예</b></h4></button>
                                                   </div>
                                                   <div align="right" style="width: 40%; ">
                                                      <button type="button" class="btn iq-bg-danger" style="width: 75%;" data-dismiss="modal"><h4 style="color: #f36157;"><b>아니요</b></h4></button>
                                                   </div>
                                                </div>
                                             </div>
                                             <div style="height: 20%;">
                                             </div>
                                          </div>
                                       </div>
                                    </div>
                                 </div>
                                 

                                 </td>
                               </tr>  
                               <tr>
                                 <td>2020.10.20</td>
                                 <td contenteditable="true">2020 10월 평가원 국어영역</td>
                                 <td>  
                                    <span><button type="button" data-icon="S" class="icon" style="border: none; background-color:rgba( 255, 255, 255, 0 );"></button></span>
                                    <span class="table-remove"><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span>
                                 </td>
                               </tr>
                               <tr>
                                 <td>2020.10.19</td>
                                 <td contenteditable="true">2020 10월 평가원 국어영역</td>
                                 <td>  
                                    <span><button type="button" data-icon="S" class="icon" style="border: none; background-color:rgba( 255, 255, 255, 0 );"></button></span>
                                    <span class="table-remove"><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span>
                                 </td>
                               </tr>
                               <tr>
                                 <td>2020.10.18</td>
                                 <td>2020 10월 평가원 국어영역</td>
                                 <td>  
                                    <span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-edit-line"></i></button></span>
                                    <span class="table-remove"><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span>
                                 </td>
                               </tr>
                                <tr>
                                 <td>2020.10.17</td>
                                 <td>2020 10월 평가원 국어영역</td>
                                 <td>  
                                    <span><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-edit-line"></i></button></span>
                                    <span class="table-remove"><button type="button" style="border: none; background-color:rgba( 255, 255, 255, 0 );"><i class="ri-delete-bin-line" aria-hidden="true"></i></button></span>
                                 </td>
                               </tr>
                            </tbody>
                           </table>
                           </div>
                           <div align="center" style="padding-left: 3%">
                                    <div class="row">
                                 <div style="width: 40%">
                                    한 화면에 볼 글 갯수 
                                    <span><select>
                                       <option>5</option>
                                       <option>10</option>
                                       <option>15</option>
                                    </select>
                                    </span>
                                 </div>
                                 <div style="width: 30%">
                                    1 ~ 5번 글 13개의 글                                          
                                 </div>
                                 <div style="width: 25%">
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
                        <div style="width: 55%">
                           <div style="width: 100%; margin-left: 1%; height: 100%;">
                                 <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                                    <div style="height: 8%;" align="center">
                                       <div class="row" style="width: 95%; margin-bottom: 2%" >
                                          <div align="left" style="width: 50%;">
                                             <h4><b>OMR 카드 등록</b></h4>
                                          </div>
                                          <div align="right" style="width: 50%; ">
                                             <button style="display: none;" onclick="uploadFile(); return false;" class="btn bg_01 btn btn-primary mb-3"> 파일 업로드 </button>
                                             <button class="btn bg_01 btn btn-primary mb-3" onclick="win_open('CRD_REG_MST')">좌표등록</button>
                                             <button type="button" class="btn btn-primary mb-3" data-toggle="modal" data-target="#exampleModal">저장</button>
                                             <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#printModal">출력</button>
                                             <a href="images/exomr2.jpg" download><button class="btn btn-primary mb-3">내려 받기</button></a>
                                          </div>
                                       </div>
                                       
                                             <!-- Modal -->
                                             <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                <div class="modal-dialog" role="document" style="height: 200px">
                                                   <div class="iq-card iq-border-radius-20 white" style="height: 100%">
                                                      <div style="height: 100%">
                                                         <div style="height: 10%;">
                                                         </div>
                                                         <div align="center" style="height: 40%; margin-top: 5%;">
                                                            <h4>OMR 카드 명 <input type="text"></h4>
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
                                          <div align="center" style="height: 87%; width: 95%;">
                                             <div>
                                                <img src="images/exomr2.jpg" width="100%">
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