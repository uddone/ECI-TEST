<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OCR 시험 등록</title>
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
                        <h4><b>동일한 OCR 선택</b></h4>
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
                           </form>
                           <div>
                           <table class="table table-bordered table-responsive-md text-center" style="width: 100%">
                             <thead>
                               <tr>
                                 <th>등록일시</th>
                                 <th>OCR 명</th>
                                 <th>수정/삭제</th>
                               </tr>
                             </thead>
                             <tbody>
                              <c:forEach items="${EXAMList}" var="ex">
                               	<tr id ="${ex.EXAM_CD}" class="exam" onclick="sele('${ex.EXAM_CD}')">
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
                           <div style="padding-left: 3%">
                              
                                 <div style="width: 100%" align="left">
                                   한 화면에 볼 글 갯수 
                                    <span><input type="text" name = "limit" value="${param.limit}" style="width: 50px;">
                                    </span>
                                 </div>
                                 <div class="row" style="padding-top: 5%;">
                                 <c:if test="${pageNum > 1}">
						<a href="javascript:listdo('${pageNum-1 }')">[이전]</a>
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
						<a href="javascript:listdo('${pageNum+1 }')">[다음]</a>
					</c:if>
					<c:if test="${pageNum >= maxpage }">[다음]</c:if>   
                                 
                              </div> 
                           </div>
                        </div>
                     </div>
                  </div>
                  </div>
               </div>
                        <div style="width: 60%; height: 100%; margin-left: 1%">
                         <div class="iq-card iq-border-radius-20" style="height: 100%; padding-top: 2%;" align="center">
                              <div align="left" style="padding-left: 3%; width: 100%; height: 8%; margin-bottom: 2%">
                                    <a href="OCR_TYPE_ADM2.HTML"><h4><b>성적표 양식 보기</b></h4></a>
                              </div>
                                          
                              <div style="width: 95%; height: 87%; padding-bottom: 2%;">
                                 <div style="height: 100%; background-color: white; border-radius: 10px;" >
                                    
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