<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
 <div id="content-page" class="content-page">
            <div class="container-fluid">
               <div class="row row-eq-height">
                  <div class="col-md-12">
                     <div class="row">
                        <div class="col-md-12">
                           <div class="iq-card iq-border-radius-20">
                              <div class="iq-card-body" style="background-color: white;">
                                 <div class="row">
                                    <div class="col-md-12 mb-3">
                                       <h5 class="text-primary card-title"><i class="ri-pencil-fill"></i> 문의 보내기</h5>
                                    </div>
                                 </div>
                                 <form class="email-form" action="MAIN-INQR.ai" method="post" enctype="multipart/form-data">
                                    <div class="form-group row">
                                       <label for="inputEmail3" class="col-sm-2 col-form-label">수신</label>
                                       <div class="col-sm-10">
                                          <input id="inputEmail3" class="select2jsMultiSelect form-control" name="memail" value="${sessionScope.MBR.EMAIL_ADR}" readonly="readonly" >
                                       </div>
                                    </div>
                                    <div class="form-group row">
                                       <label for="subject" class="col-sm-2 col-form-label">제목</label>
                                       <div class="col-sm-10">
                                          <input type="text"  id="subject" name="title" class="form-control">
                                       </div>
                                    </div>
                                    <div class="form-group row">
                                       <label for="subject" class="col-sm-2 col-form-label">문의사항</label>
                                       <div class="col-md-10">
                                          <textarea class="textarea form-control" rows="5" name="content"></textarea>
                                       </div>
                                    </div>
                                    <div class="row">
                                       <div style="width: 90%"></div>
                                       <div style="width: 10%">
                                          <div class="row">                                          
                                             <div class="send-btn pl-3">
                                                <button type="submit" class="btn btn-primary">Send</button>
                                             </div>
                                             <div class="send-panel">
                                                <label class="ml-2 mb-0 iq-bg-primary rounded" for="file"> <input type="file" id="file" name="file1" style="display: none"> <a><i class="ri-attachment-line"></i> </a> </label>
                                             </div>
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
            </div>
         </div>
</body>
</html>